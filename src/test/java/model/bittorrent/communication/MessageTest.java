package model.bittorrent.communication;

import model.client.PeerID;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author Adrien Lacroix
 * @version 0.2.0
 * @see Message
 */
public class MessageTest {

	private final static int PREFIX_LEN = Integer.BYTES;

	@Test
	public void testHandshake() throws Exception {
		byte[] infoHash = new byte[20];
		byte[] peerID = PeerID.generatePeerID().getBytes(StandardCharsets.UTF_8);
		Assert.assertEquals(20, peerID.length);
		Message m = Message.handshake(infoHash, new String(peerID));
		Assert.assertEquals(MessageType.HANDSHAKE, m.getType());
		Assert.assertEquals(49 + 19, m.toBytes().length);
	}

	@Test
	public void testKeepAlive() throws Exception {
		Message m = Message.keepAlive();
		Assert.assertEquals(MessageType.KEEP_ALIVE, m.getType());
		Assert.assertEquals(MessageType.KEEP_ALIVE.getLengthPrefix(), m.getLengthPrefix());
		Assert.assertEquals(PREFIX_LEN, m.toBytes().length);
	}

	@Test
	public void testChoke() throws Exception {
		Message m = Message.choke();
		int expectedTotalLength = PREFIX_LEN + MessageType.CHOKE.getLengthPrefix();
		Assert.assertEquals(MessageType.CHOKE, m.getType());
		Assert.assertEquals(MessageType.CHOKE.getLengthPrefix(), m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}

	@Test
	public void testUnchoke() throws Exception {
		Message m = Message.unchoke();
		int expectedTotalLength = PREFIX_LEN + MessageType.UNCHOKE.getLengthPrefix();
		Assert.assertEquals(MessageType.UNCHOKE, m.getType());
		Assert.assertEquals(MessageType.UNCHOKE.getLengthPrefix(), m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}

	@Test
	public void testInterested() throws Exception {
		Message m = Message.interested();
		int expectedTotalLength = PREFIX_LEN + MessageType.INTERESTED.getLengthPrefix();
		Assert.assertEquals(MessageType.INTERESTED, m.getType());
		Assert.assertEquals(MessageType.INTERESTED.getLengthPrefix(), m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}

	@Test
	public void testNotInterested() throws Exception {
		Message m = Message.notInterested();
		int expectedTotalLength = PREFIX_LEN + MessageType.NOT_INTERESTED.getLengthPrefix();
		Assert.assertEquals(MessageType.NOT_INTERESTED, m.getType());
		Assert.assertEquals(MessageType.NOT_INTERESTED.getLengthPrefix(), m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}

	@Test
	public void testHave() throws Exception {
		Message m = Message.have(1);
		int expectedTotalLength = PREFIX_LEN + MessageType.HAVE.getLengthPrefix();
		Assert.assertEquals(MessageType.HAVE, m.getType());
		Assert.assertEquals(MessageType.HAVE.getLengthPrefix(), m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}

	@Test
	public void testBitfield() throws Exception {
		byte[] bitfield = new byte[]{0, 1, 1, 0};
		Message m = Message.bitfield(bitfield);
		int expectedTotalLength = PREFIX_LEN + MessageType.BITFIELD.getLengthPrefix() + bitfield.length;
		Assert.assertEquals(MessageType.BITFIELD, m.getType());
		Assert.assertEquals(MessageType.BITFIELD.getLengthPrefix() + bitfield.length, m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}

	@Test
	public void testRequest() throws Exception {
		Message m = Message.request(1, 2, 3);
		int expectedTotalLength = PREFIX_LEN + MessageType.REQUEST.getLengthPrefix();
		Assert.assertEquals(MessageType.REQUEST, m.getType());
		Assert.assertEquals(MessageType.REQUEST.getLengthPrefix(), m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}

	@Test
	public void testPiece() throws Exception {
		byte[] piece = new byte[]{0, 1, 1, 0};
		Message m = Message.piece(1, 2, piece);
		int expectedTotalLength = PREFIX_LEN + MessageType.PIECE.getLengthPrefix() + piece.length;
		Assert.assertEquals(MessageType.PIECE, m.getType());
		Assert.assertEquals(MessageType.PIECE.getLengthPrefix() + piece.length, m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}

	@Test
	public void testCancel() throws Exception {
		Message m = Message.cancel(1, 2, 3);
		int expectedTotalLength = PREFIX_LEN + MessageType.CANCEL.getLengthPrefix();
		Assert.assertEquals(MessageType.CANCEL, m.getType());
		Assert.assertEquals(MessageType.CANCEL.getLengthPrefix(), m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}

	@Test
	public void testPort() throws Exception {
		short port = 6800;
		Message m = Message.port(port);
		int expectedTotalLength = PREFIX_LEN + MessageType.PORT.getLengthPrefix();
		Assert.assertEquals(MessageType.PORT, m.getType());
		Assert.assertEquals(MessageType.PORT.getLengthPrefix(), m.getLengthPrefix());
		Assert.assertEquals(expectedTotalLength, m.toBytes().length);
	}
}