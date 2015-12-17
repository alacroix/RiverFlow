package model.bittorrent.metainfo;

import java.util.List;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class MultiFileDictionary extends AbstractInfoDictionary {
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

	@Override
	public int getTotalLength() {
		int totalLength = 0;
		for (FileKeys k : files) {
			totalLength += k.getLength();
		}
		return totalLength;
	}
}
