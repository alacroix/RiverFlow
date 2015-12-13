package model.bittorrent.metainfo;

/**
 * All data in a metainfo file is bencoded. The specification for bencoding is defined above.
 * <p>
 * The content of a metainfo file (the file ending in ".torrent") is a bencoded dictionary,
 * containing the keys listed below.
 * <p>
 * All character string values are UTF-8 encoded.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class MetainfoFile {
	/**
	 * A dictionary that describes the file(s) of the torrent.
	 * <p>
	 * There are two possible forms: one for the case of a 'single-file'
	 * torrent with no directory structure, and one for the case of a 'multi-file' torrent
	 */
	private AbstractInfoDictionary info;

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
	private String announceList;

	/**
	 * (optional)
	 * <p>
	 * the creation time of the torrent, in standard UNIX
	 * epoch format (integer, seconds since 1-Jan-1970 00:00:00 UTC)
	 */
	private Integer creationDate;

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

	public MetainfoFile(AbstractInfoDictionary info, String announce) {
		this(info, announce, null, null, null, null, null);
	}

	public MetainfoFile(AbstractInfoDictionary info, String announce, String announceList,
	                    Integer creationDate, String comment, String createdBy, String encoding) {
		this.info = info;
		this.announce = announce;
		this.announceList = announceList;
		this.creationDate = creationDate;
		this.comment = comment;
		this.createdBy = createdBy;
		this.encoding = encoding;
	}
}
