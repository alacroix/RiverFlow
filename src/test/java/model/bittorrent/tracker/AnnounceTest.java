package model.bittorrent.tracker;

import model.bittorrent.communication.Peer;
import model.bittorrent.torrent.Metainfo;
import model.bittorrent.torrent.TorrentFileReader;
import model.client.PeerID;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class AnnounceTest {

	private final static String RESOURCES_PATH = "src/test/resources/torrent_files/";

	private String createRequest() throws IOException {
		Path file = Paths.get(RESOURCES_PATH, "ubuntu-15.10-server-amd64.iso.torrent");

		Metainfo f = TorrentFileReader.readTorrentFile(file);

		String announce = f.getAnnounce();
		String peerIdString = PeerID.generatePeerID();
		byte[] infoHash = f.getInfoHash();
		Assert.assertNotNull(f.getInfoHash());
		byte[] peerId = peerIdString.getBytes(StandardCharsets.UTF_8);

		return new AnnounceRequest(announce, infoHash, peerId, 6888,
				0, 0, f.getTotalLength(), 1, 0, null).getRequest();
	}

	@Test
	public void testAnnounce() throws IOException {
		// get request
		String request = createRequest();

		// connect
		URL obj = new URL(request);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		// get response code
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + request);
		System.out.println("Response Code : " + responseCode);

		// get response
		BufferedInputStream in;
		if (responseCode != HttpURLConnection.HTTP_OK) {
			in = new BufferedInputStream(con.getErrorStream());
		} else {
			in = new BufferedInputStream(con.getInputStream());
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		int current;
		while ((current = in.read()) != -1) {
			output.write(current);
		}
		in.close();

		//print result
		if (responseCode != HttpURLConnection.HTTP_OK) {
			System.out.println(output.toString());
		} else {
			AnnounceResponse response = ResponseReader.readAnnounceResponse(output.toByteArray());
			for (Peer p : response.getPeers()) {
				System.out.println(p);
			}
		}

	}
}