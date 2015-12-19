package model.bittorrent.communication;

import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author Adrien Lacroix
 * @version 0.2.0
 */
public class Message {

	/**
	 * four byte big-endian value
	 */
	private int lengthPrefix;

	/**
	 * type of message
	 */
	private MessageType type;

	private byte[] infoHash;

	private String peerID;

	private int index;

	private int begin;

	private int length;

	private byte[] bitField;

	private byte[] block;

	private short port;

	private Message(MessageType type, byte[] infoHash, String peerID) {
		this.type = type;
		this.infoHash = infoHash;
		this.peerID = peerID;
	}

	private Message(MessageType type, int lengthPrefix) {
		this.type = type;
		this.lengthPrefix = lengthPrefix;
	}

	private Message(MessageType type, int lengthPrefix, byte[] bitField) {
		this(type, lengthPrefix);
		this.bitField = bitField;
	}

	private Message(MessageType type, int lengthPrefix, short port) {
		this(type, lengthPrefix);
		this.port = port;
	}

	private Message(MessageType type, int lengthPrefix, int index) {
		this(type, lengthPrefix);
		this.index = index;
	}

	private Message(MessageType type, int lengthPrefix, int index, int begin) {
		this(type, lengthPrefix, index);
		this.begin = begin;
	}

	private Message(MessageType type, int lengthPrefix, int index,
	                int begin, int length) {
		this(type, lengthPrefix, index, begin);
		this.length = length;
	}

	private Message(MessageType type, int lengthPrefix, int index,
	                int begin, byte[] block) {
		this(type, lengthPrefix, index, begin);
		this.block = block;
	}

	private static Message getSimpleMessage(MessageType type) {
		return new Message(type, type.getLengthPrefix());
	}

	/**
	 * The handshake is a required message and must be the first message
	 * transmitted by the client. It is (49+len(pstr)) bytes long.
	 *
	 * @param infoHash 20-byte SHA1 hash of the info key in the metainfo file.
	 *                 This is the same info_hash that is transmitted in tracker requests.
	 * @param peerID   20-byte string used as a unique ID for the client. This is usually
	 *                 the same peer_id that is transmitted in tracker requests
	 *                 (but not always e.g. an anonymity option in Azureus).
	 * @return handshake message
	 */
	public static Message handshake(byte[] infoHash, String peerID) {
		return new Message(MessageType.HANDSHAKE, infoHash, peerID);
	}

	/**
	 * The keep-alive message is a message with zero bytes, specified with
	 * the length prefix set to zero. There is no message ID and no payload.
	 * <p>
	 * Peers may close a connection if they receive no messages (keep-alive
	 * or any other message) for a certain period of time, so a keep-alive
	 * message must be sent to maintain the connection alive if no command
	 * have been sent for a given amount of time. This amount of time is generally two minutes.
	 *
	 * @return keep-alive message
	 */
	public static Message keepAlive() {
		return new Message(MessageType.KEEP_ALIVE, MessageType.KEEP_ALIVE.getLengthPrefix());
	}

	/**
	 * The choke message is fixed-length and has no payload.
	 *
	 * @return choke message
	 */
	public static Message choke() {
		return getSimpleMessage(MessageType.CHOKE);
	}

	/**
	 * The unchoke message is fixed-length and has no payload.
	 *
	 * @return unchoke message
	 */
	public static Message unchoke() {
		return getSimpleMessage(MessageType.UNCHOKE);
	}

	/**
	 * The interested message is fixed-length and has no payload.
	 *
	 * @return interested message
	 */
	public static Message interested() {
		return getSimpleMessage(MessageType.INTERESTED);
	}

	/**
	 * The not interested message is fixed-length and has no payload.
	 *
	 * @return not interested message
	 */
	public static Message notInterested() {
		return getSimpleMessage(MessageType.NOT_INTERESTED);
	}

	/**
	 * The have message is fixed length. The payload is the .
	 * <p>
	 * Implementer's Note: That is the strict definition, in reality some games may be
	 * played. In particular because peers are extremely unlikely to download pieces
	 * that they already have, a peer may choose not to advertise having a piece to
	 * a peer that already has that piece. At a minimum "HAVE suppression" will result
	 * in a 50% reduction in the number of HAVE messages, this translates to around
	 * a 25-35% reduction in protocol overhead. At the same time, it may be worthwhile
	 * to send a HAVE message to a peer that has that piece already since it will be
	 * useful in determining which piece is rare.
	 * <p>
	 * A malicious peer might also choose to advertise having pieces that it knows the
	 * peer will never download. Due to this attempting to model peers using this
	 * information is a bad idea.
	 *
	 * @param pieceIndex integer specifying the zero-based index of a piece that has
	 *                   just been successfully downloaded and verified via the hash
	 * @return have message
	 */
	public static Message have(int pieceIndex) {
		MessageType t = MessageType.HAVE;
		return new Message(t, t.getLengthPrefix(), pieceIndex);
	}

	/**
	 * The bitfield message may only be sent immediately after the handshaking sequence
	 * is completed, and before any other messages are sent. It is optional, and need
	 * not be sent if a client has no pieces.
	 * <p>
	 * The bitfield message is variable length, where X is the length of the bitfield.
	 * <p>
	 * Some clients (Deluge for example) send bitfield with missing pieces even if it
	 * has all data. Then it sends rest of pieces as have messages. They are saying this
	 * helps against ISP filtering of BitTorrent protocol. It is called lazy bitfield.
	 * <p>
	 * A bitfield of the wrong length is considered an error. Clients should drop the
	 * connection if they receive bitfields that are not of the correct size, or if
	 * the bitfield has any of the spare bits set.
	 *
	 * @param bitField The payload is a bitfield representing the pieces that have been
	 *                 successfully downloaded. The high bit in the first byte corresponds
	 *                 to piece index 0. Bits that are cleared indicated a missing piece,
	 *                 and set bits indicate a valid and available piece. Spare bits at
	 *                 the end are set to zero.
	 * @return bitfield message
	 */
	public static Message bitfield(byte[] bitField) {
		MessageType t = MessageType.BITFIELD;
		return new Message(t, t.getLengthPrefix() + bitField.length, bitField);
	}

	/**
	 * The request message is fixed length, and is used to request a block.
	 *
	 * @param index  integer specifying the zero-based piece index
	 * @param begin  integer specifying the zero-based byte offset within the piece
	 * @param length integer specifying the requested length.
	 * @return request message
	 */
	public static Message request(int index, int begin, int length) {
		MessageType t = MessageType.REQUEST;
		return new Message(t, t.getLengthPrefix(), index, begin, length);
	}

	/**
	 * The piece message is variable length, where X is the length of the block.
	 *
	 * @param index integer specifying the zero-based piece index
	 * @param begin integer specifying the zero-based byte offset within the piece
	 * @param block block of data, which is a subset of the piece specified by index.
	 * @return piece message
	 */
	public static Message piece(int index, int begin, byte[] block) {
		MessageType t = MessageType.PIECE;
		return new Message(t, t.getLengthPrefix() + block.length, index, begin, block);
	}

	/**
	 * The cancel message is fixed length, and is used to cancel block requests.
	 * <p>
	 * The payload is identical to that of the "request" message. It is typically
	 * used during "End Game"
	 *
	 * @param index  integer specifying the zero-based piece index
	 * @param begin  integer specifying the zero-based byte offset within the piece
	 * @param length integer specifying the requested length.
	 * @return cancel message
	 */
	public static Message cancel(int index, int begin, int length) {
		MessageType t = MessageType.CANCEL;
		return new Message(t, t.getLengthPrefix(), index, begin, length);
	}

	/**
	 * The port message is sent by newer versions of the Mainline that implements
	 * a DHT tracker.
	 * <p>
	 * The listen port is the port this peer's DHT node is listening on. This peer
	 * should be inserted in the local routing table (if DHT tracker is supported).
	 *
	 * @param port peer's DHT node listening port
	 * @return port message
	 */
	public static Message port(short port) {
		MessageType t = MessageType.PORT;
		return new Message(t, t.getLengthPrefix(), port);
	}

	/**
	 * Returns the message's type
	 *
	 * @return message's type
	 */
	public MessageType getType() {
		return type;
	}

	public int getLengthPrefix() {
		return lengthPrefix;
	}

	private byte[] intToBytesArray(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		buffer.putInt(value);
		return buffer.array();
	}

	private byte[] shortToBytesArray(short value) {
		ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
		buffer.putShort(value);
		return buffer.array();
	}

	public byte[] toBytes() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		// if handshake
		if (type == MessageType.HANDSHAKE) {
			byte[] pstr = "BitTorrent Protocol".getBytes(StandardCharsets.UTF_8);
			byte[] reserved = new byte[8];

			try {
				// pstrlen
				output.write(pstr.length);
				// pstre
				output.write(pstr);
				// reserved
				output.write(reserved);
				// info hash
				output.write(infoHash);
				// peer id
				output.write(peerID.getBytes(StandardCharsets.UTF_8));

				return output.toByteArray();
			} catch (IOException e) {
				System.err.println("Failed creating handshake message");
			}
		}

		// else, common builder
		try {
			// length prefix
			output.write(intToBytesArray(lengthPrefix));

			// write id if not keep alive
			if (type == MessageType.KEEP_ALIVE) {
				return output.toByteArray();
			} else {
				output.write(type.getId());
			}

			switch (type) {
				case CHOKE:
				case UNCHOKE:
				case INTERESTED:
				case NOT_INTERESTED:
					break;
				case HAVE:
					output.write(intToBytesArray(index));
					break;
				case BITFIELD:
					output.write(bitField);
					break;
				case REQUEST:
				case CANCEL:
					output.write(intToBytesArray(index));
					output.write(intToBytesArray(begin));
					output.write(intToBytesArray(length));
					break;
				case PIECE:
					output.write(intToBytesArray(index));
					output.write(intToBytesArray(begin));
					output.write(block);
					break;
				case PORT:
					output.write(shortToBytesArray(port));
					break;
			}

		} catch (IOException e) {
			System.err.println("Failed creating message");
		}

		return output.toByteArray();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(type.toString());

		// if handshake
		if (type == MessageType.HANDSHAKE) {
			String pstr = "BitTorrent Protocol";
			String reserved = "00000000";

			builder.append("<pstrlen=").append(pstr.length()).append('>');
			builder.append("<pstr").append(pstr).append('>');
			builder.append("<reserved=").append(reserved).append(">");
			builder.append("<info_hash=").append(Hex.encodeHexString(infoHash)).append(">");
			builder.append("<peer_id=").append(peerID).append(">");

			return builder.toString();
		} else {
			// else, common builder
			builder.append("<len=").append(lengthPrefix).append('>');
		}

		if (type == MessageType.KEEP_ALIVE) {
			return builder.toString();
		} else {
			builder.append("<id=").append(type.getId()).append('>');
		}

		switch (type) {
			case CHOKE:
			case UNCHOKE:
			case INTERESTED:
			case NOT_INTERESTED:
				break;
			case HAVE:
				builder.append('<').append("piece index=").append(index).append('>');
				break;
			case BITFIELD:
				builder.append('<').append("bitfield=").append(new String(bitField)).append('>');
				break;
			case REQUEST:
			case CANCEL:
				builder.append('<').append("index=").append(index).append('>');
				builder.append('<').append("begin=").append(begin).append('>');
				builder.append('<').append("length=").append(length).append('>');
				break;
			case PIECE:
				builder.append('<').append("index=").append(index).append('>');
				builder.append('<').append("begin=").append(begin).append('>');
				builder.append('<').append("block=").append(new String(block)).append('>');
				break;
			case PORT:
				builder.append('<').append("listen-port=").append(port).append('>');
				break;
		}

		return builder.toString();
	}
}
