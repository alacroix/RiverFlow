package model.bittorrent.communication;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see Message
 */
public class MessageTest {

	@Test
	public void testKeepAlive() throws Exception {
		Message m = Message.keepAlive();
		Assert.assertEquals(MessageType.KEEP_ALIVE, m.getType());
		Assert.assertEquals(MessageType.KEEP_ALIVE.getLengthPrefix(), m.getLengthPrefix());
	}

	@Test
	public void testChoke() throws Exception {
		Message m = Message.choke();
		Assert.assertEquals(MessageType.CHOKE, m.getType());
		Assert.assertEquals(MessageType.CHOKE.getLengthPrefix(), m.getLengthPrefix());
		System.out.println(m);
	}

	@Test
	public void testUnchoke() throws Exception {
		Message m = Message.unchoke();
		Assert.assertEquals(MessageType.UNCHOKE, m.getType());
		Assert.assertEquals(MessageType.UNCHOKE.getLengthPrefix(), m.getLengthPrefix());
	}

	@Test
	public void testInterested() throws Exception {
		Message m = Message.interested();
		Assert.assertEquals(MessageType.INTERESTED, m.getType());
		Assert.assertEquals(MessageType.INTERESTED.getLengthPrefix(), m.getLengthPrefix());
	}

	@Test
	public void testNotInterested() throws Exception {
		Message m = Message.notInterested();
		Assert.assertEquals(MessageType.NOT_INTERESTED, m.getType());
		Assert.assertEquals(MessageType.NOT_INTERESTED.getLengthPrefix(), m.getLengthPrefix());
	}

	@Test
	public void testHave() throws Exception {
		Message m = Message.have(1);
		Assert.assertEquals(MessageType.HAVE, m.getType());
		Assert.assertEquals(MessageType.HAVE.getLengthPrefix(), m.getLengthPrefix());
	}

	@Test
	public void testBitfield() throws Exception {
		byte[] bitfield = new byte[]{0, 1, 1, 0};
		Message m = Message.bitfield(bitfield);
		Assert.assertEquals(MessageType.BITFIELD, m.getType());
		Assert.assertEquals(MessageType.BITFIELD.getLengthPrefix() + bitfield.length, m.getLengthPrefix());
		System.out.println(m);
	}

	@Test
	public void testRequest() throws Exception {
		Message m = Message.request(1, 2, 3);
		Assert.assertEquals(MessageType.REQUEST, m.getType());
		Assert.assertEquals(MessageType.REQUEST.getLengthPrefix(), m.getLengthPrefix());
	}

	@Test
	public void testPiece() throws Exception {
		byte[] piece = new byte[]{0, 1, 1, 0};
		Message m = Message.piece(1, 2, piece);
		Assert.assertEquals(MessageType.PIECE, m.getType());
		Assert.assertEquals(MessageType.PIECE.getLengthPrefix() + piece.length, m.getLengthPrefix());
	}

	@Test
	public void testCancel() throws Exception {
		Message m = Message.cancel(1, 2, 3);
		Assert.assertEquals(MessageType.CANCEL, m.getType());
		Assert.assertEquals(MessageType.CANCEL.getLengthPrefix(), m.getLengthPrefix());
	}

	@Test
	public void testPort() throws Exception {
		Message m = Message.port(6800);
		Assert.assertEquals(MessageType.PORT, m.getType());
		Assert.assertEquals(MessageType.PORT.getLengthPrefix(), m.getLengthPrefix());
	}
}