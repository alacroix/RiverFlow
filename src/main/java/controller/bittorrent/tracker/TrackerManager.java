package controller.bittorrent.tracker;

import model.bittorrent.torrent.Torrent;
import model.bittorrent.tracker.*;
import model.client.PeerID;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class TrackerManager {

	private int responseCode;

	private byte[] getResponseFromRequest(String request) throws IOException {
		// connect
		URL obj = new URL(request);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		// get response code
		responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + request);
		System.out.println("Response Code : " + responseCode);

		// get response
		BufferedInputStream in;
		if (responseCode != HttpURLConnection.HTTP_OK) {
			in = new BufferedInputStream(con.getErrorStream());
		} else {
			in = new BufferedInputStream(con.getInputStream());
		}

		// return result
		return IOUtils.toByteArray(in);
	}

	public AnnounceResponse getAnnounceForTorrent(Torrent torrent, AnnounceRequest.Event event) {
		AnnounceRequest request = new AnnounceRequest(torrent,
				PeerID.generatePeerID().getBytes(StandardCharsets.UTF_8),
				6800, 1, 0, event);

		byte[] response;
		try {
			response = getResponseFromRequest(request.getRequest());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}

		if (responseCode == HttpURLConnection.HTTP_OK) {
			return ResponseReader.readAnnounceResponse(response);
		} else {
			return null;
		}
	}

	public ScrapeResponse getScrapeForTorrents(String announceURL, Set<Torrent> torrents) {
		Iterator<Torrent> it = torrents.iterator();
		Collection<byte[]> infoHashes = new ArrayList<>();
		// if invalid announce
		while (it.hasNext()) {
			Torrent t = it.next();
			if (!t.getAnnounce().equals(announceURL)) {
				it.remove();
			} else {
				infoHashes.add(t.getInfoHash());
			}
		}

		ScrapeRequest request = new ScrapeRequest(announceURL, infoHashes);

		byte[] response;
		try {
			response = getResponseFromRequest(request.getRequest());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}

		if (responseCode == HttpURLConnection.HTTP_OK) {
			return ResponseReader.readScrapeResponse(response);
		} else {
			return null;
		}
	}
}
