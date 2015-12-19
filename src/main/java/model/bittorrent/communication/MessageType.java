package model.bittorrent.communication;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public enum MessageType {
	HANDSHAKE,
	KEEP_ALIVE,
	CHOKE(1, 0),
	UNCHOKE(1, 1),
	INTERESTED(1, 2),
	NOT_INTERESTED(1, 3),
	HAVE(5, 4),
	BITFIELD(1, 5),
	REQUEST(13, 6),
	PIECE(9, 7),
	CANCEL(13, 8),
	PORT(3, 9);

	private int lengthPrefix;
	private int id;

	MessageType() {
		this(0, -1);
	}

	MessageType(int lengthPrefix, int id) {
		this.lengthPrefix = lengthPrefix;
		this.id = id;
	}

	public int getLengthPrefix() {
		return lengthPrefix;
	}

	public int getId() {
		return id;
	}
}