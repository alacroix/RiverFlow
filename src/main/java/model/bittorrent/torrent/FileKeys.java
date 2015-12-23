package model.bittorrent.torrent;

import java.util.List;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class FileKeys {
	/**
	 * The length of the file, in bytes.
	 */
	private Integer length;

	/**
	 * A list of UTF-8 encoded strings corresponding to subdirectory names,
	 * the last of which is the actual file name (a zero length list is an error case).
	 */
	private List<String> path;

	/**
	 * (optional)
	 * <p>
	 * A 32-character hexadecimal string corresponding to the MD5 sum
	 * of the file. This is not used by BitTorrent at all, but it is
	 * included by some programs for greater compatibility.
	 */
	private String md5Sum;

	public FileKeys(Integer length, List<String> path, String md5Sum) {
		this.length = length;
		this.path = path;
		this.md5Sum = md5Sum;
	}

	public Integer getLength() {
		return length;
	}

	public List<String> getPath() {
		return path;
	}

	public String getMd5Sum() {
		return md5Sum;
	}
}
