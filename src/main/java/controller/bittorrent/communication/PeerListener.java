package controller.bittorrent.communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class PeerListener extends Thread {

	private PeerManager manager;

	private int port;

	private boolean listening;

	private ServerSocket listener;

	public PeerListener(PeerManager manager, int port) {
		super("PeerListener<Port: " + port + '>');

		this.manager = manager;
		this.port = port;
		this.listening = true;
	}

	@Override
	public void run() {
		try {
			listener = new ServerSocket(port);
			System.out.println("PeerListener listening on port " + port);
			while (listening) {
				new PeerListenerThread(manager, listener.accept()).start();
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		} finally {
			if (listener != null && !listener.isClosed()) {
				try {
					listener.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}

	public void setListening(boolean listening) {
		this.listening = listening;
	}

	public boolean isListening() {
		return listening;
	}

	public InetAddress getLocalAddress() {
		return listener.getInetAddress();
	}

	public int getLocalPort() {
		return listener.getLocalPort();
	}
}
