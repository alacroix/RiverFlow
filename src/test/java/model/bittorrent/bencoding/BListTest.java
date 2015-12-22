package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Adrien Lacroix
 * @version 0.2.0
 * @see BList
 */
public class BListTest {

	private Charset charset = StandardCharsets.UTF_8;

	@Test
	public void testString() throws Exception {
		String expectedValue = "l3:foo3:bare";

		BList l = new BList();
		l.add(new BString("foo"));
		l.add(new BString("bar"));

		Assert.assertEquals(expectedValue, l.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), l.getBencodedBytes());
	}

	@Test
	public void testInteger() throws Exception {
		String expectedValue = "li4ei-3ee";

		BList l = new BList();
		l.add(new BInteger(4));
		l.add(new BInteger(-3));

		Assert.assertEquals(expectedValue, l.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), l.getBencodedBytes());
	}

	@Test
	public void testDictionary() throws Exception {
		String expectedValue = "ld3:foo3:baree";

		BList l = new BList();
		BDictionary d = new BDictionary();
		d.put("foo", new BString("bar"));
		l.add(d);

		Assert.assertEquals(expectedValue, l.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), l.getBencodedBytes());
	}

	@Test
	public void testVarious() throws Exception {
		String expectedValue = "l3:fooi4ee";

		BList l = new BList();
		l.add(new BString("foo"));
		l.add(new BInteger(4));

		Assert.assertEquals(expectedValue, l.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), l.getBencodedBytes());
	}

	@Test
	public void testEmpty() throws Exception {
		String expectedValue = "le";

		BList l = new BList();

		Assert.assertEquals(expectedValue, l.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), l.getBencodedBytes());
	}

	@Test(expected = BException.class)
	public void testException() throws Exception {
		BList.read(new byte[]{-1}, new AtomicInteger(0));
	}
}