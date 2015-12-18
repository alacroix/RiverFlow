package model.bittorrent.tracker;

import java.util.Map;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class ScrapeResponse {
	/**
	 * Map of files contained in the response
	 */
	private Map<String, ScrapeKeys> files;

	public ScrapeResponse(Map<String, ScrapeKeys> files) {
		this.files = files;
	}

	public Map<String, ScrapeKeys> getFiles() {
		return files;
	}

	public ScrapeKeys getFile(String infoHash) {
		return files.get(infoHash);
	}
}
