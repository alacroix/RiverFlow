package model.bittorrent.tracker;

import model.bittorrent.metainfo.MetainfoFile;
import model.bittorrent.metainfo.TorrentFileReader;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class ScrapeTest {

	private final static String RESOURCES_PATH = "src/test/resources/torrent_files/";

	private String infoHashHex;

	private String createRequest() throws IOException {
		Path file = Paths.get(RESOURCES_PATH, "ubuntu-15.10-server-amd64.iso.torrent");

		MetainfoFile f = TorrentFileReader.readTorrentFile(file);

		String announce = f.getAnnounce();
		byte[] infoHash = f.getInfoHash();
		infoHashHex = Hex.encodeHexString(infoHash);

		Set<byte[]> set = new HashSet<>();
		set.add(infoHash);

		return new ScrapeRequest(announce, set).getRequest();
	}

	@Test
	public void testScrape() throws IOException {
		// get request
		String request = createRequest();
		System.out.println(request);

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

		int current = 0;
		while ((current = in.read()) != -1) {
			output.write(current);
		}
		in.close();

		//print result
		if (responseCode != HttpURLConnection.HTTP_OK) {
			System.out.println(output.toString());
		} else {
			ScrapeResponse response = ResponseReader.readScrapeResponse(output.toByteArray());
			Assert.assertNotNull(response.getFile(infoHashHex));
		}

	}
}