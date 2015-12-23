package controller.bittorrent.communication;

import controller.bittorrent.BitTorrentManager;
import model.bittorrent.communication.Message;
import model.bittorrent.communication.Peer;
import model.bittorrent.tracker.AnnounceResponse;
import model.client.PeerID;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class PeerManager {

	private BitTorrentManager manager;

	private Set<Peer> peers;

	private Set<PeerConnection> peerConnections;

	private String peerID;

	private PeerListener listener;

	public PeerManager(BitTorrentManager manager, int port) {
		this.manager = manager;
		this.peers = new HashSet<>();
		this.peerID = PeerID.generatePeerID();

		this.peerConnections = new HashSet<>();

		this.listener = new PeerListener(this, port);
		// initialize listening on another thread
		listener.start();
	}

	public void updatePeers(AnnounceResponse response) {
		for (Peer peer : response.getPeers()) {
			peers.add(peer);
		}
	}

	public Set<Peer> getPeers() {
		return peers;
	}

	public void connectTo(Peer peer, byte[] infoHash) {
		PeerConnection c = new PeerConnection(this, peer);
		try {
			c.connect(generateHandshake(infoHash));
			peerConnections.add(c);
		} catch (IOException e) {
			System.err.println("Failed to connect to " + peer + e.getMessage());
		}
	}

	public void acceptFrom(Socket socket) {
		Peer peer = Peer.fromSocket(socket);
		try {
			PeerConnection c = PeerConnection.accept(this, peer, socket);
			peerConnections.add(c);
		} catch (IOException e) {
			System.err.println("Failed to accept from " + peer);
		}
	}

	public Message generateHandshake(byte[] infoHash) {
		return manager.getTorrentManager().hasTorrent(infoHash) ?
				Message.handshake(infoHash, peerID) : null;
	}

	public void closeAllConnections() {
		Iterator<PeerConnection> it = peerConnections.iterator();
		while (it.hasNext()) {
			PeerConnection connection = it.next();
			try {
				connection.close();
			} catch (IOException e) {
				System.err.println("Failed to close connection with " + connection.getPeer());
			}
			it.remove();
		}
	}

	public void setListening(boolean listening) {
		listener.setListening(listening);
	}

	public boolean isListening() {
		return listener.isListening();
	}
}
