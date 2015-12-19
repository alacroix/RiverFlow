package model.bittorrent.communication;

import java.net.Socket;

/**
 * A peer is any BitTorrent client participating in a download.
 * <p>
 * The client is also a peer, however it is the BitTorrent client that
 * is running on the local machine. Readers of this specification may
 * choose to think of themselves as the client which connects to numerous peers.
 *
 * @author Adrien Lacroix
 * @version 0.2.0
 */
public class Peer {
	/**
	 * Default peer ID for peers decoded from compact mode
	 */
	public final static String PEER_ID_DEFAULT = "PeerDefault";

	/**
	 * peer's self-selected ID, as described above for the tracker request
	 */
	private String peerID;

	/**
	 * peer's IP address either IPv6 (hexed) or IPv4 (dotted quad) or DNS name
	 */
	private String ip;

	/**
	 * peer's port number
	 */
	private int port;

	/**
	 * Used in compact mode
	 *
	 * @param ip   peer's IP address
	 * @param port peer's port number
	 */
	public Peer(String ip, int port) {
		this(PEER_ID_DEFAULT, ip, port);
	}

	/**
	 * Used in standard mode
	 *
	 * @param peerID peer's self-selected ID
	 * @param ip     peer's IP address
	 * @param port   peer's port number
	 */
	public Peer(String peerID, String ip, int port) {
		this.peerID = peerID;
		this.ip = ip;
		this.port = port;
	}

	public static Peer fromSocket(Socket socket) {
		return new Peer(socket.getInetAddress().getHostAddress(), socket.getPort());
	}

	public void setPeerID(String peerID) {
		this.peerID = peerID;
	}

	public String getPeerID() {
		return peerID;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return peerID + '<' + ip + ':' + port + '>';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		Peer o = (Peer) obj;
		return o.ip.equals(this.ip) && o.port == this.port;
	}

	@Override
	public int hashCode() {
		return ip.hashCode() + port;
	}
}
