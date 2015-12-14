package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see BString
 */
public class BStringTest {
	@Test
	public void testNormal() throws Exception {
		BType t = new BString("spam");
		Assert.assertEquals("4:spam", t.getBencodedValue());
	}

	@Test
	public void testEmpty() throws Exception {
		BType t = new BString("");
		Assert.assertEquals("0:", t.getBencodedValue());
	}

	@Test
	public void testNull() throws Exception {
		BType t = new BString(null);
		Assert.assertEquals("0:", t.getBencodedValue());
	}

	@Test
	public void testRead() throws Exception {
		String value = "3:foo";
		BString s = BString.read(value, 0);
		Assert.assertEquals(value, s.getBencodedValue());
		Assert.assertEquals("foo", s.getValue());

		value = "10:abcdefghij";
		s = BString.read(value, 0);
		Assert.assertEquals(value, s.getBencodedValue());
		Assert.assertEquals("abcdefghij", s.getValue());

		value = "0:";
		s = BString.read(value, 0);
		Assert.assertEquals(value, s.getBencodedValue());
		Assert.assertEquals("", s.getValue());
	}
}