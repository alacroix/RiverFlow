package model.bittorrent.communication;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public enum MessageType {
	CHOKE(1, 0),
	UNCHOKE(1, 1),
	INTERESTED(1, 2),
	NOT_INTERESTED(1, 3),
	HAVE(5, 4),
	BITFIELD(1, 5),
	REQUEST(13, 6),
	PIECE(9, 7),
	CANCEL(13, 8),
	PORT(3, 9),
	HANDSHAKE(49 + Message.HANDSHAKE_PSTR.length()),
	KEEP_ALIVE(0);

	private int lengthPrefix;
	private int id;

	MessageType(int lengthPrefix) {
		this(lengthPrefix, -1);
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

	public static MessageType fromID(int id) {
		switch (id) {
			case 0:
				return CHOKE;
			case 1:
				return UNCHOKE;
			case 2:
				return INTERESTED;
			case 3:
				return NOT_INTERESTED;
			case 4:
				return HAVE;
			case 5:
				return BITFIELD;
			case 6:
				return REQUEST;
			case 7:
				return PIECE;
			case 8:
				return CANCEL;
			case 9:
				return PORT;
			default:
				throw new IllegalArgumentException("Invalid MessageType id");
		}
	}
}