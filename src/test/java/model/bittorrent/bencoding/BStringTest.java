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
 * @version 0.1.0
 * @see BString
 */
public class BStringTest {

	private Charset charset = StandardCharsets.UTF_8;

	@Test
	public void testNormal() throws Exception {
		String expectedValue = "4:spam";
		BType t = new BString("spam");
		Assert.assertEquals(expectedValue, t.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), t.getBencodedBytes());
	}

	@Test
	public void testEmpty() throws Exception {
		String expectedValue = "0:";
		BType t = new BString("");
		Assert.assertEquals(expectedValue, t.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), t.getBencodedBytes());
	}

	@Test
	public void testNull() throws Exception {
		String expectedValue = "0:";
		BType t = new BString(null);
		Assert.assertEquals(expectedValue, t.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), t.getBencodedBytes());
	}

	@Test
	public void testEquals() throws Exception {
		String value = "foo";
		BType t1 = new BString(value);
		BType t2 = new BString(value);

		Assert.assertEquals(t1, t1);
		Assert.assertTrue(t1.equals(t2));
		Assert.assertFalse(t1.equals(new BInteger(3)));

		Set<BType> set = new HashSet<>();
		set.add(t1);
		set.add(t2);

		Assert.assertEquals(1, set.size());
	}

	@Test
	public void testToString() throws Exception {
		String value = "foo";
		BType t = new BString(value);
		Assert.assertEquals(value, t.toString());
	}
}