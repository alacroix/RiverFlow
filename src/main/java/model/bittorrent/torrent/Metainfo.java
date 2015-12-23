package model.bittorrent.torrent;

import java.time.Instant;
import java.util.List;

/**
 * All data in a metainfo file is bencoded. The specification for bencoding is defined above.
 * <p>
 * The content of a metainfo file (the file ending in ".torrent") is a bencoded dictionary,
 * containing the keys listed below.
 * <p>
 * All character string values are UTF-8 encoded.
 *
 * @author Adrien Lacroix
 * @version 0.2.0
 */
public class Metainfo {
	/**
	 * A dictionary that describes the file(s) of the torrent.
	 * <p>
	 * There are two possible forms: one for the case of a 'single-file'
	 * torrent with no directory structure, and one for the case of a 'multi-file' torrent
	 */
	private AbstractInfoDictionary info;

	/**
	 * 20-byte SHA1 hash of the value of the info key from the Metainfo file.
	 */
	private byte[] infoSHA1;

	/**
	 * The announce URL of the tracker
	 */
	private String announce;

	/**
	 * (optional)
	 * <p>
	 * this is an extention to the official specification,
	 * offering backwards-compatibility. (list of lists of strings)
	 */
	private List<List<String>> announceList;

	/**
	 * (optional)
	 * <p>
	 * the creation time of the torrent, in standard UNIX
	 * epoch format (integer, seconds since 1-Jan-1970 00:00:00 UTC)
	 */
	private Instant creationDate;

	/**
	 * (optional)
	 * <p>
	 * free-form textual comments of the author
	 */
	private String comment;

	/**
	 * (optional)
	 * <p>
	 * name and version of the program used to create the .torrent
	 */
	private String createdBy;

	/**
	 * (optional)
	 * <p>
	 * the string encoding format used to generate the pieces part of the info dictionary in the .torrent metafile
	 */
	private String encoding;

	public Metainfo(AbstractInfoDictionary info, byte[] infoSHA1, String announce) {
		this(info, infoSHA1, announce, null, null, null, null, null);
	}

	public Metainfo(AbstractInfoDictionary info, byte[] infoSHA1, String announce, List<List<String>> announceList,
	                Integer creationDate, String comment, String createdBy, String encoding) {
		this.info = info;
		this.infoSHA1 = infoSHA1;
		this.announce = announce;
		this.announceList = announceList;
		if (creationDate != null) {
			this.creationDate = Instant.ofEpochSecond(creationDate);
		}
		this.comment = comment;
		this.createdBy = createdBy;
		this.encoding = encoding;
	}

	public String getAnnounce() {
		return announce;
	}

	public List<List<String>> getAnnounceList() {
		return announceList;
	}

	public byte[] getInfoHash() {
		return infoSHA1;
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public String getComment() {
		return comment;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getEncoding() {
		return encoding;
	}

	public int getTotalLength() {
		return info.getTotalLength();
	}

	public AbstractInfoDictionary getInfo() {
		return info;
	}

}
