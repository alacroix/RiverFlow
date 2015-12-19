package controller.bittorrent.communication;

import model.bittorrent.communication.Message;
import model.bittorrent.communication.MessageType;
import model.bittorrent.communication.Peer;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class PeerConnection {

	private PeerManager manager;

	private Peer peer;

	private Socket socket;

	private byte[] infoHash;

	public PeerConnection(PeerManager manager, Peer peer) {
		this.manager = manager;
		this.peer = peer;
	}

	private PeerConnection(PeerManager manager, Peer peer, Socket socket) {
		this.manager = manager;
		this.peer = peer;
		this.socket = socket;
	}

	/**
	 * If the client is the recipient
	 *
	 * @param peer
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	public static PeerConnection accept(PeerManager manager, Peer peer, Socket socket) throws IOException {
		PeerConnection c = new PeerConnection(manager, peer, socket);

		// receive handshake
		InputStream in = socket.getInputStream();
		ByteArrayOutputStream aBytes = new ByteArrayOutputStream();
		while (aBytes.size() != MessageType.HANDSHAKE.getLengthPrefix()) {
			aBytes.write(in.read());
		}
		Message request = Message.decode(aBytes.toByteArray());

		// if not handshake, close
		if (request.getType() != MessageType.HANDSHAKE) {
			socket.close();
			throw new IOException("Expected Handshake message");
		}

		// answer
		Message answer = manager.generateHandshake(request.getInfoHash());
		// if client has info hash
		if (answer != null) {
			OutputStream out = socket.getOutputStream();
			out.write(answer.toBytes());
			out.flush();
		} else {
			socket.close();
			throw new IOException("Doesn't have expected info hash");
		}

		// connection established
		return c;
	}

	/**
	 * If the client is the initiator
	 *
	 * @param handshake Handshake message
	 * @throws IOException
	 */
	public void connect(Message handshake) throws IOException {
		if (handshake.getType() != MessageType.HANDSHAKE) {
			throw new IllegalArgumentException("First message must be a Handshake message !");
		}

		// etablish connection
		socket = new Socket(peer.getIp(), peer.getPort());

		// send handshake
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		out.write(handshake.toBytes());
		out.flush();

		// what if socket is closed here ?

		// expect handshake answer
		InputStream in = socket.getInputStream();
		ByteArrayOutputStream aBytes = new ByteArrayOutputStream();
		while (aBytes.size() != MessageType.HANDSHAKE.getLengthPrefix()) {
			aBytes.write(in.read());
		}
		Message answer = Message.decode(aBytes.toByteArray());

		// if not handshake, close
		if (answer.getType() != MessageType.HANDSHAKE) {
			close();
		}

		// check peer id
		if (peer.getPeerID().equals(Peer.PEER_ID_DEFAULT)) {
			peer.setPeerID(answer.getPeerID());
		} else {
			/*
			 If the initiator of the connection receives a handshake
			 in which the peer_id does not match the expected peerid,
			 then the initiator is expected to drop the connection.

			 Note that the initiator presumably received the peer information
			 from the tracker, which includes the peer_id that was registered
			 by the peer. The peer_id from the tracker and in the handshake
			 are expected to match.
			 */
			if (!peer.getPeerID().equals(answer.getPeerID())) {
				close();
			}
		}

		// the connection is now etablished
	}

	public void close() throws IOException {
		if (!socket.isClosed()) {
			socket.close();
		}
	}

	public boolean isClosed() {
		return socket.isClosed();
	}

	public Peer getPeer() {
		return peer;
	}

	public byte[] getInfoHash() {
		return infoHash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		PeerConnection o = (PeerConnection) obj;
		return o.peer.equals(this.peer) && Arrays.equals(o.infoHash, this.infoHash);
	}

	@Override
	public int hashCode() {
		return peer.hashCode() + Arrays.hashCode(infoHash);
	}
}
