package model.bittorrent.torrent;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see Metainfo
 */
public class MetainfoTest {

	private final static String RESOURCES_PATH = "src/test/resources/torrent_files/";

	@Test
	public void testBitTorrentFile() throws Exception {
		Path file = Paths.get(RESOURCES_PATH, "ubuntu-15.10-server-amd64.iso.torrent");

		TorrentFileReader.readTorrentFile(file);
	}
}