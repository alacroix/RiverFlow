package model.bittorrent.tracker;

import model.bittorrent.bencoding.Reader;
import model.bittorrent.metainfo.MetainfoFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see MetainfoFile
 */
public class TrackerRequestTest {

	private final static String RESOURCES_PATH = "src/test/resources/torrent_files/";

	@Test
	public void testSimpleRequest() throws IOException {
		Path file = Paths.get(RESOURCES_PATH, "ubuntu-15.10-server-amd64.iso.torrent");

		MetainfoFile f = Reader.readTorrentFile(file);

		String announce = f.getAnnounce();
		String peerIdString = "-RF0010-012345678910";
		byte[] infoHash = f.getInfoHash();
		Assert.assertNotNull(f.getInfoHash());
		byte[] peerId = peerIdString.getBytes(StandardCharsets.UTF_8);

		Assert.assertEquals(20, peerId.length);

		TrackerRequest request = new TrackerRequest(announce, infoHash, peerId, 6888,
				0, 0, f.getTotalLength(), 1, 0, TrackerRequest.Event.STARTED);

		String req = request.getRequest();

		URL obj = new URL(req);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + req);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in;
		if (responseCode != HttpURLConnection.HTTP_OK) {
			in = new BufferedReader(
					new InputStreamReader(con.getErrorStream()));
		} else {
			in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
		}
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
	}
}