package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see BDictionary
 */
public class BDictionaryTest {
	@Test
	public void testString() throws Exception {
		BDictionary d = new BDictionary();
		d.put("cow", new BString("moo"));
		d.put("foo", new BString("bar"));
		Assert.assertEquals("d3:cow3:moo3:foo3:bare", d.getBencodedValue());
	}

	@Test
	public void testList() throws Exception {
		BDictionary d = new BDictionary();
		BList l = new BList();
		l.add(new BString("b"));
		l.add(new BString("a"));
		l.add(new BString("r"));
		d.put("foo", l);
		Assert.assertEquals("d3:fool1:b1:a1:ree", d.getBencodedValue());
	}

	@Test
	public void testEmpty() throws Exception {
		BType t = new BDictionary();
		Assert.assertEquals("de", t.getBencodedValue());
	}
}