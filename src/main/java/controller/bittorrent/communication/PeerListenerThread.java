package controller.bittorrent.communication;

import java.net.Socket;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class PeerListenerThread extends Thread {

	private PeerManager manager;

	private Socket socket;

	public PeerListenerThread(PeerManager manager, Socket socket) {
		super("PeerListenerThread<" + socket.getInetAddress() + ':'
				+ socket.getLocalPort() + '>');
		this.manager = manager;
		this.socket = socket;
	}

	@Override
	public void run() {
		manager.acceptFrom(socket);
	}
}
