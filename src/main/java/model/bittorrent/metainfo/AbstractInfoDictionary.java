package model.bittorrent.metainfo;

/**
 * A dictionary that describes the file(s) of the torrent.
 * <p>
 * There are two possible forms: one for the case of a 'single-file' torrent
 * with no directory structure, and one for the case of a 'multi-file' torrent
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public abstract class AbstractInfoDictionary {

	public enum Mode {SINGLE_FILE, MULTI_FILE}

	/**
	 * Suggested name to save the file (or directory) as. It is purely advisory.
	 */
	private String name;

	/**
	 * number of bytes in each piece the file is split into. For the purposes of transfer,
	 * files are split into fixed-size pieces which are all the same length except for
	 * possibly the last one which may be truncated. piece length is almost always a power
	 * of two, most commonly 2 18 = 256 K (BitTorrent prior to version 3.2 uses 2 20 = 1 M as default).
	 */
	private Integer pieceLength;

	/**
	 * String whose length is a multiple of 20. It is to be subdivided into strings
	 * of length 20, each of which is the SHA1 hash of the piece at the corresponding index.
	 */
	private String pieces;

	/**
	 * (optional)
	 * <p>
	 * If it is set to "1", the client MUST publish its presence to get other peers ONLY
	 * via the trackers explicitly described in the metainfo file. If this field is set
	 * to "0" or is not present, the client may obtain peer from other means, e.g. PEX peer
	 * exchange, dht. Here, "private" may be read as "no external peer source".
	 */
	private Integer privateTracker;

	public AbstractInfoDictionary(String name, Integer pieceLength, String pieces) {
		this(name, pieceLength, pieces, null);
	}

	public AbstractInfoDictionary(String name, Integer pieceLength, String pieces, Integer privateTracker) {
		this.name = name;
		this.pieceLength = pieceLength;
		this.pieces = pieces;
		this.privateTracker = privateTracker;
	}

	public abstract Mode getMode();

	public abstract int getTotalLength();

	public int getPiecesNumber() {
		return (int) Math.ceil(getTotalLength() / pieceLength);
	}

	public String getName() {
		return name;
	}

	public boolean isValidPiece(byte[] sha1, int index) {
		return false;
	}
}
