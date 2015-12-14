package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see BInteger
 */
public class BIntegerTest {
	@Test
	public void testNormal() throws Exception {
		BType t = new BInteger(-3);
		Assert.assertEquals("i-3e", t.getBencodedValue());
	}

	@Test
	public void testNull() throws Exception {
		BType t = new BInteger(null);
		Assert.assertEquals("i0e", t.getBencodedValue());
	}

	@Test
	public void testRead() throws Exception {
		String value = "i3e";
		BInteger s = BInteger.read(value, 0);
		Assert.assertEquals(value, s.getBencodedValue());
		Assert.assertEquals(new Integer(3), s.getValue());

		value = "i42e";
		s = BInteger.read(value, 0);
		Assert.assertEquals(value, s.getBencodedValue());
		Assert.assertEquals(new Integer(42), s.getValue());

		value = "i-3e";
		s = BInteger.read(value, 0);
		Assert.assertEquals(value, s.getBencodedValue());
		Assert.assertEquals(new Integer(-3), s.getValue());
	}

}