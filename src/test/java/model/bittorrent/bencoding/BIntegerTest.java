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
}