package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see BList
 */
public class BListTest {
	@Test
	public void testString() throws Exception {
		BList l = new BList();
		l.add(new BString("foo"));
		l.add(new BString("bar"));
		Assert.assertEquals("l3:foo3:bare", l.getBencodedValue());
	}

	@Test
	public void testInteger() throws Exception {
		BList l = new BList();
		l.add(new BInteger(4));
		l.add(new BInteger(-3));
		Assert.assertEquals("li4ei-3ee", l.getBencodedValue());
	}

	@Test
	public void testDictionary() throws Exception {
		BList l = new BList();

		BDictionary d = new BDictionary();
		d.put("foo", new BString("bar"));

		l.add(d);
		Assert.assertEquals("ld3:foo3:baree", l.getBencodedValue());
	}

	@Test
	public void testVarious() throws Exception {
		BList l = new BList();
		l.add(new BString("foo"));
		l.add(new BInteger(4));
		Assert.assertEquals("l3:fooi4ee", l.getBencodedValue());
	}

	@Test
	public void testEmpty() throws Exception {
		BList l = new BList();
		Assert.assertEquals("le", l.getBencodedValue());
	}
}