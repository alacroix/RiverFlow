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
 * @see BDictionary
 */
public class BDictionaryTest {

	private Charset charset = StandardCharsets.UTF_8;

	@Test
	public void testString() throws Exception {
		String expectedValue = "d3:cow3:moo3:foo3:bare";

		BDictionary d = new BDictionary();
		d.put("cow", new BString("moo"));
		d.put("foo", new BString("bar"));

		Assert.assertEquals(expectedValue, d.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), d.getBencodedBytes());
	}

	@Test
	public void testList() throws Exception {
		String expectedValue = "d3:fool1:b1:a1:ree";

		BDictionary d = new BDictionary();
		BList l = new BList();
		l.add(new BString("b"));
		l.add(new BString("a"));
		l.add(new BString("r"));
		d.put("foo", l);

		Assert.assertEquals(expectedValue, d.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), d.getBencodedBytes());
	}

	@Test
	public void testEmpty() throws Exception {
		String expectedValue = "de";

		BType d = new BDictionary();

		Assert.assertEquals(expectedValue, d.getBencodedValue());
		Assert.assertArrayEquals(expectedValue.getBytes(charset), d.getBencodedBytes());
	}

	@Test(expected = BException.class)
	public void testException() throws Exception {
		BDictionary.read(new byte[]{-1}, new AtomicInteger(0));
	}
}