package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adrien Lacroix
 * @version 0.2.0
 * @see BInteger
 */
public class BIntegerTest {

	private Charset charset = StandardCharsets.UTF_8;

	@Test
	public void testNormal() throws Exception {
		String expectedValue = "i-3e";

		BType t = new BInteger(-3);

		Assert.assertEquals(expectedValue, t.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), t.getBencodedBytes());
	}

	@Test
	public void testNull() throws Exception {
		String expectedValue = "i0e";
		BType t = new BInteger(null);
		Assert.assertEquals(expectedValue, t.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), t.getBencodedBytes());
	}

	@Test
	public void testEquals() throws Exception {
		Integer value = 42;
		BType t1 = new BInteger(value);
		BType t2 = new BInteger(value);

		Assert.assertEquals(t1, t1);
		Assert.assertFalse(t1.equals(new BString("42")));
		Assert.assertTrue(t1.equals(t2));

		Set<BType> set = new HashSet<>();
		set.add(t1);
		set.add(t2);

		Assert.assertEquals(1, set.size());
	}

	@Test
	public void testToString() throws Exception {
		Integer value = 42;
		BType t = new BInteger(value);
		Assert.assertEquals(String.valueOf(value), t.toString());
	}
}