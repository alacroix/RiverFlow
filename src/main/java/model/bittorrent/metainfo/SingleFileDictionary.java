package model.bittorrent.metainfo;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class SingleFileDictionary extends AbstractInfoDictionary {

	/**
	 * The length of the file, in bytes.
	 */
	private Integer length;

	/**
	 * (optional)
	 * <p>
	 * A 32-character hexadecimal string corresponding to the MD5 sum
	 * of the file. This is not used by BitTorrent at all, but it is
	 * included by some programs for greater compatibility.
	 */
	private String md5Sum;

	public SingleFileDictionary(String name, Integer pieceLength, String pieces, Integer length) {
		this(name, pieceLength, pieces, length, null);
	}

	public SingleFileDictionary(String name, Integer pieceLength, String pieces, Integer length,
	                            String md5Sum) {
		this(name, pieceLength, pieces, length, md5Sum, null);
	}

	public SingleFileDictionary(String name, Integer pieceLength, String pieces, Integer length,
	                            String md5Sum, Integer privateTracker) {
		super(name, pieceLength, pieces, privateTracker);
		this.length = length;
		this.md5Sum = md5Sum;
	}

	@Override
	public Mode getMode() {
		return Mode.SINGLE_FILE;
	}

	public int getTotalLength() {
		return length;
	}
}
