package model.bittorrent.tracker;

import model.bittorrent.bencoding.*;
import model.bittorrent.communication.Peer;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utils class to read announce and scrape responses from tracker
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class ResponseReader {

	private final static String FAILURE_REASON = "failure reason";
	private final static String COMPLETE = "complete";
	private final static String INCOMPLETE = "incomplete";
	private final static String INTERVAL = "interval";
	private final static String PEERS = "peers";

	private final static String FILES = "files";
	private final static String DOWNLOADED = "downloaded";
	private final static String NAME = "name";

	public static AnnounceResponse readAnnounceResponse(byte[] response) {
		AtomicInteger index = new AtomicInteger(0);
		BDictionary rd = (BDictionary) Reader.read(response, index);

		AnnounceResponse ar;

		// if failure
		if (rd.containsKey(FAILURE_REASON)) {
			String failure = ((BString) rd.get(FAILURE_REASON)).getValue();
			ar = new AnnounceResponse(failure);
		} else {
			int complete = ((BInteger) rd.get(COMPLETE)).getValue();
			int incomplete = ((BInteger) rd.get(INCOMPLETE)).getValue();
			int interval = ((BInteger) rd.get(INTERVAL)).getValue();

			// peers in compact mode
			BString peersObject = (BString) rd.get(PEERS);
			Set<Peer> peers = decodePeers(Base64.getDecoder().decode(peersObject.getValue()));

			ar = new AnnounceResponse(complete, incomplete, interval, peers);
		}

		return ar;
	}

	private static Set<Peer> decodePeers(byte[] value) {
		Set<Peer> peers = new HashSet<>();

		// must be a multiple of 6 bytes
		if (value.length % 6 != 0) {
			throw new IllegalArgumentException("Value must be a multiple of 6 bytes");
		}

		for (int i = 0; i < value.length; i += 6) {
			try {
				byte[] ipBytes = ArrayUtils.subarray(value, i, i + 4);
				byte[] portBytes = ArrayUtils.subarray(value, i + 4, i + 6);
				InetAddress ip = InetAddress.getByAddress(ipBytes);

				ByteBuffer buffer = ByteBuffer.wrap(new byte[]{0, 0, portBytes[0], portBytes[1]});
				int port = buffer.getInt();

				peers.add(new Peer(ip.getHostAddress(), port));
			} catch (UnknownHostException e) {
				System.err.println("Invalid IP");
			}
		}

		return peers;
	}

	public static ScrapeResponse readScrapeResponse(byte[] response) {
		AtomicInteger index = new AtomicInteger(0);
		BDictionary rd = (BDictionary) Reader.read(response, index);

		BDictionary filesD = (BDictionary) rd.get(FILES);
		Map<String, ScrapeKeys> files = new HashMap<>();
		// for each file
		for (Map.Entry<String, BType> entry : filesD.entrySet()) {
			byte[] infoHash = Base64.getDecoder().decode(entry.getKey());

			BDictionary values = (BDictionary) entry.getValue();
			int complete = ((BInteger) values.get(COMPLETE)).getValue();
			int downloaded = ((BInteger) values.get(DOWNLOADED)).getValue();
			int incomplete = ((BInteger) values.get(INCOMPLETE)).getValue();
			String name = null;
			if (values.containsKey(NAME)) {
				name = ((BString) values.get(NAME)).getValue();
			}

			files.put(Hex.encodeHexString(infoHash), new ScrapeKeys(complete, downloaded, incomplete, name));
		}

		return new ScrapeResponse(files);
	}
}
