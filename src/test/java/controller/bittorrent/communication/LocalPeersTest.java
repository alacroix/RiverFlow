package controller.bittorrent.communication;

import model.bittorrent.communication.Peer;
import model.bittorrent.torrent.Metainfo;
import model.bittorrent.torrent.TorrentFileReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class LocalPeersTest {

	private static int port1 = 50000, port2 = 60000;

	private PeerManager m1, m2;
	private Metainfo f;

	private final static String RESOURCES_PATH = "src/test/resources/torrent_files/";

	@Before
	public void setUp() throws Exception {
		Path file = Paths.get(RESOURCES_PATH, "ubuntu-15.10-server-amd64.iso.torrent");
		f = TorrentFileReader.readTorrentFile(file);

		m1 = new PeerManager(new HashSet<>(), port1);
		m2 = new PeerManager(new HashSet<>(), port2);
	}

	@Test
	public void testHandshake() throws Exception {
		Peer p = new Peer("127.0.0.1", port2);
		m1.connectTo(p, f.getInfoHash());
	}

	@After
	public void tearDown() throws Exception {
		m1.setListening(false);
		m2.setListening(false);
	}
}