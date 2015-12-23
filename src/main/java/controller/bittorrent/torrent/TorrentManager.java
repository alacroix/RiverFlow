package controller.bittorrent.torrent;

import model.bittorrent.torrent.Torrent;
import org.apache.commons.codec.binary.Hex;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class TorrentManager {

	private Map<String, Torrent> torrents;

	public TorrentManager() {
		this.torrents = new HashMap<>();
	}

	public void addTorrentToList(Torrent torrent) {
		String infoHash = torrent.getHexInfoHash();
		torrents.put(infoHash, torrent);
	}

	public void removeTorrentFromList(Torrent torrent) {
		String infoHash = torrent.getHexInfoHash();
		torrents.remove(infoHash);
	}

	public boolean hasTorrent(byte[] infoHash) {
		return torrents.containsKey(Hex.encodeHexString(infoHash));
	}

	public boolean hasTorrent(String infoHash) {
		return torrents.containsKey(infoHash);
	}

	public boolean createTorrentFile(Path path, Torrent torrent) {
		throw new UnsupportedOperationException();
	}

}
