package controller.bittorrent;

import controller.bittorrent.communication.PeerManager;
import model.bittorrent.communication.Peer;
import model.bittorrent.torrent.Torrent;
import model.bittorrent.torrent.TorrentReader;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class BitTorrentManagerTest {

	private final static String RESOURCES_PATH = "src/test/resources/torrent_files/";

	@Test
	public void testHandshake() throws Exception {
		Path file = Paths.get(RESOURCES_PATH, "ubuntu-15.10-server-amd64.iso.torrent");
		Torrent torrent = TorrentReader.readTorrentFile(file);

		BitTorrentManager manager = new BitTorrentManager(6800);
		manager.addTorrent(torrent);

		PeerManager peerManager = manager.getPeerManager();
		for (Peer peer : peerManager.getPeers()) {
			System.err.println("Trying to handshake: " + peer);
			peerManager.connectTo(peer, torrent.getInfoHash());
		}

		manager.removeTorrent(torrent);

		peerManager.closeAllConnections();
		peerManager.setListening(false);
	}
}