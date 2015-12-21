package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see Reader
 */
public class ReaderTest {
	@Test
	public void testString() throws Exception {
		AtomicInteger index = new AtomicInteger(0);
		String simple = "3:foo";
		String extended = "10:gabuzomeuh";
		String empty = "0:";
		byte[] value = (simple + extended + empty).getBytes(StandardCharsets.UTF_8);

		BString s = BString.read(value, index);
		Assert.assertEquals(simple, s.getBencodedValue());
		Assert.assertEquals("foo", s.getValue());
		Assert.assertEquals(simple.length(), index.get());

		s = BString.read(value, index);
		Assert.assertEquals(extended, s.getBencodedValue());
		Assert.assertEquals("gabuzomeuh", s.getValue());
		Assert.assertEquals(simple.length() + extended.length(), index.get());

		s = BString.read(value, index);
		Assert.assertEquals(empty, s.getBencodedValue());
		Assert.assertEquals("", s.getValue());
		Assert.assertEquals(simple.length() + extended.length() + empty.length(), index.get());
	}

	@Test
	public void testInteger() throws Exception {
		AtomicInteger index = new AtomicInteger(0);
		String simple = "i3e";
		String extended = "i123456789e";
		String neg = "i-3e";
		byte[] value = (simple + extended + neg).getBytes(StandardCharsets.UTF_8);

		BInteger s = BInteger.read(value, index);
		Assert.assertEquals(simple, s.getBencodedValue());
		Assert.assertEquals(new Integer(3), s.getValue());
		Assert.assertEquals(simple.length(), index.get());

		s = BInteger.read(value, index);
		Assert.assertEquals(extended, s.getBencodedValue());
		Assert.assertEquals(new Integer(123456789), s.getValue());

		s = BInteger.read(value, index);
		Assert.assertEquals(neg, s.getBencodedValue());
		Assert.assertEquals(new Integer(-3), s.getValue());
	}

	@Test
	public void testList() throws Exception {
		AtomicInteger index = new AtomicInteger(0);
		String integer = "i3e";
		String string = "3:foo";
		byte[] value = ("l" + integer + string + "e").getBytes(StandardCharsets.UTF_8);

		BList l = BList.read(value, index);
		Assert.assertEquals("l" + integer + string + "e", l.getBencodedValue());

		Assert.assertEquals(2, l.size());
		Assert.assertTrue(l.get(0) instanceof BInteger);
		Assert.assertTrue(l.get(1) instanceof BString);

		BInteger i = (BInteger) l.get(0);
		BString s = (BString) l.get(1);

		Assert.assertEquals(new Integer(3), i.getValue());
		Assert.assertEquals(integer, i.getBencodedValue());
		Assert.assertEquals("foo", s.getValue());
		Assert.assertEquals(string, s.getBencodedValue());
	}
}