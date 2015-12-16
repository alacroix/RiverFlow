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
}