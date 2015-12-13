package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see BList
 */
public class BListTest {
	@Test
	public void testString() throws Exception {
		List<BType> l = new ArrayList<>();
		l.add(new BString("foo"));
		l.add(new BString("bar"));
		BType t = new BList(l);
		Assert.assertEquals("l3:foo3:bare", t.getBencodedValue());
	}

	@Test
	public void testInteger() throws Exception {
		List<BType> l = new ArrayList<>();
		l.add(new BInteger(4));
		l.add(new BInteger(-3));
		BType t = new BList(l);
		Assert.assertEquals("li4ei-3ee", t.getBencodedValue());
	}

	@Test
	public void testDictionary() throws Exception {
		List<BType> l = new ArrayList<>();

		Map<BType, BType> d = new LinkedHashMap<>();
		d.put(new BString("foo"), new BString("bar"));

		l.add(new BDictionary(d));
		BType t = new BList(l);
		Assert.assertEquals("ld3:foo3:baree", t.getBencodedValue());
	}

	@Test
	public void testVarious() throws Exception {
		List<BType> l = new ArrayList<>();
		l.add(new BString("foo"));
		l.add(new BInteger(4));
		BType t = new BList(l);
		Assert.assertEquals("l3:fooi4ee", t.getBencodedValue());
	}

	@Test
	public void testEmpty() throws Exception {
		BType t = new BList(new ArrayList<>());
		Assert.assertEquals("le", t.getBencodedValue());
	}

	@Test
	public void testNull() throws Exception {
		BType t = new BList(null);
		Assert.assertEquals("le", t.getBencodedValue());
	}
}