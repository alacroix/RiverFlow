package model.bittorrent.tracker;

/**
 * Object representation of a file's entry in the Scrape response
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class ScrapeKeys {

	/**
	 * number of peers with the entire file, i.e. seeders
	 */
	private int complete;

	/**
	 * total number of times the tracker has registered a completion
	 * <p>
	 * ("event=complete", i.e. a client finished downloading the torrent)
	 */
	private int downloaded;

	/**
	 * number of non-seeder peers, aka "leechers"
	 */
	private int incomplete;

	/**
	 * (optional) the torrent's internal name,
	 * as specified by the "name" file in the info section of the .torrent file
	 */
	private String name;

	public ScrapeKeys(int complete, int downloaded, int incomplete, String name) {
		this.complete = complete;
		this.downloaded = downloaded;
		this.incomplete = incomplete;
		this.name = name;
	}

	public int getComplete() {
		return complete;
	}

	public int getDownloaded() {
		return downloaded;
	}

	public int getIncomplete() {
		return incomplete;
	}

	public String getName() {
		return name;
	}
}
