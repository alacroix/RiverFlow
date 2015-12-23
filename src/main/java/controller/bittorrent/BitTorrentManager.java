package controller.bittorrent;

import controller.bittorrent.communication.PeerManager;
import controller.bittorrent.torrent.TorrentManager;
import controller.bittorrent.tracker.TrackerManager;
import model.bittorrent.torrent.Torrent;
import model.bittorrent.tracker.AnnounceRequest;
import model.bittorrent.tracker.AnnounceResponse;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class BitTorrentManager {

	private PeerManager peerManager;

	private TorrentManager torrentManager;

	private TrackerManager trackerManager;

	public BitTorrentManager(int port) {
		this.peerManager = new PeerManager(this, port);

		this.torrentManager = new TorrentManager();

		this.trackerManager = new TrackerManager();
	}

	public void addTorrent(Torrent torrent) {
		torrentManager.addTorrentToList(torrent);

		AnnounceResponse response = trackerManager.getAnnounceForTorrent(torrent,
				AnnounceRequest.Event.STARTED);

		peerManager.updatePeers(response);
	}

	public void removeTorrent(Torrent torrent) {
		torrentManager.removeTorrentFromList(torrent);

		AnnounceResponse response = trackerManager.getAnnounceForTorrent(torrent,
				AnnounceRequest.Event.COMPLETED);
	}

	public PeerManager getPeerManager() {
		return peerManager;
	}

	public TorrentManager getTorrentManager() {
		return torrentManager;
	}

	public TrackerManager getTrackerManager() {
		return trackerManager;
	}
}
