package model.bittorrent.tracker;

import model.bittorrent.communication.Peer;

import java.util.Set;

/**
 * The tracker is an HTTP/HTTPS service which responds to HTTP GET requests.
 * The response includes a peer list that helps the client participate in the torrent.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class AnnounceResponse {
	/**
	 * If present, then no other keys may be present.
	 * The value is a human-readable error message as to why the request failed
	 */
	private String failureReason;

	/**
	 * (new, optional) Similar to failure reason, but the response still gets processed normally.
	 * The warning message is shown just like an error.
	 */
	private String warningMessage;

	/**
	 * Interval in seconds that the client should wait between sending regular requests to the tracker
	 */
	private int interval;

	/**
	 * (optional) Minimum announce interval.
	 * <p>
	 * If present clients must not reannounce more frequently than this.
	 */
	private int minInterval;

	/**
	 * A string that the client should send back on its next announcements.
	 * <p>
	 * If absent and a previous announce sent a tracker id, do not discard the old value; keep using it.
	 */
	private byte[] trackerID;

	/**
	 * number of peers with the entire file, i.e. seeders
	 */
	private int complete;

	/**
	 * number of non-seeder peers, aka "leechers"
	 */
	private int incomplete;

	/**
	 * list of peers
	 */
	private Set<Peer> peers;

	public AnnounceResponse(String failureReason) {
		this.failureReason = failureReason;
	}

	public AnnounceResponse(int complete, int incomplete, int interval, Set<Peer> peers) {
		this.complete = complete;
		this.incomplete = incomplete;
		this.interval = interval;
		this.peers = peers;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public int getComplete() {
		return complete;
	}

	public int getIncomplete() {
		return incomplete;
	}

	public int getInterval() {
		return interval;
	}

	public Set<Peer> getPeers() {
		return peers;
	}
}
