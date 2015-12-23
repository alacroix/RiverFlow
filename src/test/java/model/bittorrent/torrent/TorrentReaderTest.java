package model.bittorrent.torrent;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class TorrentReaderTest {

	private final static String RESOURCES_PATH = "src/test/resources/torrent_files/";

	private Torrent getTorrent(String fileName) throws Exception {
		Path file = Paths.get(RESOURCES_PATH, fileName);
		Torrent torrent = TorrentReader.readTorrentFile(file);
		System.out.println(torrent);
		return torrent;
	}

	@Test
	public void testSingleFile() throws Exception {
		Torrent torrent = getTorrent("ubuntu-15.10-server-amd64.iso.torrent");

		Assert.assertNotNull(torrent.getAnnounce());
		Assert.assertEquals(AbstractInfoDictionary.Mode.SINGLE_FILE, torrent.getMode());
	}

	@Test
	public void testMultiFiles() throws Exception {
		Torrent torrent = getTorrent("Fedora-Live-Security-x86_64-23.torrent");

		Assert.assertNotNull(torrent.getAnnounce());
		Assert.assertEquals(AbstractInfoDictionary.Mode.MULTI_FILE, torrent.getMode());
	}
}