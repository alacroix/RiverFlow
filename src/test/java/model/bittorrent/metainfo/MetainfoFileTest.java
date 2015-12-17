package model.bittorrent.metainfo;

import model.bittorrent.bencoding.Reader;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see MetainfoFile
 */
public class MetainfoFileTest {

	private final static String RESOURCES_PATH = "src/test/resources/torrent_files/";

	@Test
	public void testBitTorrentFile() throws Exception {
		Path file = Paths.get(RESOURCES_PATH, "ubuntu-15.10-server-amd64.iso.torrent");

		Reader.readTorrentFile(file);
	}
}