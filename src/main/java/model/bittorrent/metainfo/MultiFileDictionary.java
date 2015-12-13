package model.bittorrent.metainfo;

import java.util.List;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class MultiFileDictionary extends AbstractInfoDictionary {

	public class FileKeys {
		/**
		 * The length of the file, in bytes.
		 */
		private Integer length;

		/**
		 * A list of UTF-8 encoded strings corresponding to subdirectory names,
		 * the last of which is the actual file name (a zero length list is an error case).
		 */
		private String path;

		/**
		 * (optional)
		 * <p>
		 * A 32-character hexadecimal string corresponding to the MD5 sum
		 * of the file. This is not used by BitTorrent at all, but it is
		 * included by some programs for greater compatibility.
		 */
		private String md5Sum;

		public FileKeys(Integer length, String path) {
			this(length, path, null);
		}

		public FileKeys(Integer length, String path, String md5Sum) {
			this.length = length;
			this.path = path;
			this.md5Sum = md5Sum;
		}
	}

	/**
	 * List of the different files
	 */
	private List<FileKeys> files;

	public MultiFileDictionary(String name, Integer pieceLength, String pieces, List<FileKeys> files) {
		this(name, pieceLength, pieces, files, null);
	}

	public MultiFileDictionary(String name, Integer pieceLength, String pieces, List<FileKeys> files,
	                           Integer privateTracker) {
		super(name, pieceLength, pieces, privateTracker);
		this.files = files;
	}

	@Override
	public Mode getMode() {
		return Mode.MULTI_FILE;
	}
}
