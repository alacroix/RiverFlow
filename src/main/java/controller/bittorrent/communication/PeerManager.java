package controller.bittorrent.communication;

import model.bittorrent.communication.Message;
import model.bittorrent.communication.Peer;
import model.client.PeerID;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class PeerManager {

	private HashSet<Peer> peers;

	private Set<PeerConnection> peerConnections;

	private String peerID;

	private int port;

	private PeerListener listener;

	public PeerManager(HashSet<Peer> peers, int port) {
		this.peers = peers;
		this.port = port;
		this.peerID = PeerID.generatePeerID();

		this.peerConnections = new HashSet<>();

		this.listener = new PeerListener(this, port);
		// initialize listening on another thread
		listener.start();

	}

	public void connectTo(Peer peer, byte[] infoHash) {
		PeerConnection c = new PeerConnection(this, peer);
		try {
			c.connect(generateHandshake(infoHash));
			peerConnections.add(c);
		} catch (IOException e) {
			System.err.println("Failed to connect to " + peer);
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
		// TODO: check if client has info hash, else return null
		return Message.handshake(infoHash, peerID);
	}

	public void setListening(boolean listening) {
		listener.setListening(listening);
	}

	public boolean isListening() {
		return listener.isListening();
	}
}
