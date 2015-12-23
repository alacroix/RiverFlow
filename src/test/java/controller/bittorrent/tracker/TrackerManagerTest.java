package controller.bittorrent.tracker;

import model.bittorrent.torrent.Torrent;
import model.bittorrent.torrent.TorrentReader;
import model.bittorrent.tracker.AnnounceResponse;
import model.bittorrent.tracker.ScrapeResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class TrackerManagerTest {

	private TrackerManager manager;

	private Torrent torrent;

	private final static String RESOURCES_PATH = "src/test/resources/torrent_files/";

	@Before
	public void setUp() throws Exception {
		// get torrent
		Path file = Paths.get(RESOURCES_PATH, "ubuntu-15.10-server-amd64.iso.torrent");
		torrent = TorrentReader.readTorrentFile(file);
		// init manager
		manager = new TrackerManager();
	}

	@Test
	public void testGetAnnounceForTorrent() throws Exception {
		AnnounceResponse response = manager.getAnnounceForTorrent(torrent, null);
		Assert.assertNotNull(response);
		System.out.println(response);
	}

	@Test
	public void testGetScrapeForTorrents() throws Exception {
		Set<Torrent> torrents = new HashSet<>();
		torrents.add(torrent);

		ScrapeResponse response = manager.getScrapeForTorrents(torrent.getAnnounce(), torrents);
		Assert.assertNotNull(response);
		System.out.println(response);
	}
}