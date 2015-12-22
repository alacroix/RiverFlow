package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Adrien Lacroix
 * @version 0.2.0
 * @see Reader
 */
public class ReaderTest {

	private Charset charset = StandardCharsets.UTF_8;

	@Test
	public void testString() throws Exception {
		AtomicInteger index = new AtomicInteger(0);
		String simple = "3:foo";
		String extended = "10:gabuzomeuh";
		String empty = "0:";
		byte[] value = (simple + extended + empty).getBytes(charset);

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
		byte[] value = (simple + extended + neg).getBytes(charset);

		BInteger i = (BInteger) Reader.read(value, index);
		Assert.assertEquals(simple, i.getBencodedValue());
		Assert.assertEquals(new Integer(3), i.getValue());
		Assert.assertEquals(simple.length(), index.get());

		i = (BInteger) Reader.read(value, index);
		Assert.assertEquals(extended, i.getBencodedValue());
		Assert.assertEquals(new Integer(123456789), i.getValue());

		i = (BInteger) Reader.read(value, index);
		Assert.assertEquals(neg, i.getBencodedValue());
		Assert.assertEquals(new Integer(-3), i.getValue());
	}

	@Test
	public void testList() throws Exception {
		AtomicInteger index = new AtomicInteger(0);
		String integer = "i3e";
		String string = "3:foo";
		byte[] value = ("l" + integer + string + "e").getBytes(charset);

		BList l = (BList) Reader.read(value, index);
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

	@Test
	public void testDictionary() throws Exception {
		AtomicInteger index = new AtomicInteger(0);
		BString key = new BString("pieces");
		byte[] pieces = new byte[]{-1, -2, -3, -4, 0, 0, 0, 0};
		BString encoded = new BString(Base64.getEncoder().encodeToString(pieces));
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write('d');
		output.write(key.getBencodedBytes());
		output.write(String.valueOf(pieces.length).getBytes(charset));
		output.write(':');
		output.write(pieces);
		output.write('e');
		byte[] value = output.toByteArray();

		BDictionary d = (BDictionary) Reader.read(value, index);
		Assert.assertEquals("d" + key.getBencodedValue() + encoded.getBencodedValue() + "e", d.getBencodedValue());

		Assert.assertEquals(1, d.size());
		Assert.assertTrue(d.get(key.getValue()) instanceof BString);

		BString s = (BString) d.get(key.getValue());

		Assert.assertEquals(encoded.getValue(), s.getValue());
		output.reset();
		output.write(String.valueOf(pieces.length).getBytes(charset));
		output.write(':');
		output.write(pieces);
		Assert.assertArrayEquals(output.toByteArray(), s.getBencodedBytes());
	}

	@Test(expected = BException.class)
	public void testException() throws Exception {
		byte[] garbage = new byte[]{-1, -1, -1};
		Reader.read(garbage, new AtomicInteger(0));
	}

	@Test
	public void testConstructor() throws Exception {
		Constructor<Reader> constructor = Reader.class.getDeclaredConstructor();
		Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}
}