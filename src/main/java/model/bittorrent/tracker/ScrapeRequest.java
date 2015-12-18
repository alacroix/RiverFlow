package model.bittorrent.tracker;

import org.apache.commons.codec.net.URLCodec;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * By convention most trackers support another form of request,
 * which queries the state of a given torrent (or all torrents)
 * that the tracker is managing. This is referred to as the "scrape page"
 * because it automates the otherwise tedious process of "screen scraping" the tracker's stats page.
 * <p>
 * The scrape URL is also a HTTP GET method, similar to the one described above. However the
 * base URL is different. To derive the scrape URL use the following steps:
 * <p>
 * Begin with the announce URL.
 * Find the last '/' in it.
 * If the text immediately following that '/' isn't 'announce' it will be taken as a
 * sign that that tracker doesn't support the scrape convention.
 * If it does, substitute 'scrape' for 'announce' to find the scrape page.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class ScrapeRequest {

	/**
	 * Base URL of the request
	 */
	private String scrapeURL;

	/**
	 * (optional)
	 * This restricts the tracker's report to that particular torrent.
	 * Otherwise stats for all torrents that the tracker is managing are returned.
	 */
	private Set<byte[]> infoHashes;

	public ScrapeRequest(String announceURL) {
		this(announceURL, new HashSet<>());
	}

	public ScrapeRequest(String announceURL, Set<byte[]> infoHashes) {
		int lastIndex = announceURL.lastIndexOf('/');
		String match = "announce";
		if (announceURL.length() > lastIndex + match.length() + 1 &&
				!announceURL.substring(lastIndex + 1, match.length()).equals(match)) {
			throw new IllegalArgumentException("This tracker doesn't have scrape support");
		}
		this.scrapeURL = announceURL.replace(match, "scrape");
		this.infoHashes = infoHashes;
	}

	public String getRequest() {
		StringBuilder builder = new StringBuilder();

		URLCodec codec = new URLCodec();

		builder.append(scrapeURL);
		boolean isFirst = true;
		for (byte[] hash : infoHashes) {
			if (isFirst) {
				builder.append('?');
				isFirst = false;
			} else {
				builder.append('&');
			}
			builder.append("info_hash=");
			builder.append(new String(codec.encode(hash), StandardCharsets.UTF_8));
		}

		return builder.toString();
	}
}
