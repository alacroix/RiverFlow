package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 * @see BDictionary
 */
public class BDictionaryTest {
	@Test
	public void testString() throws Exception {
		Map<BType, BType> d = new LinkedHashMap<>();
		d.put(new BString("foo"), new BString("bar"));
		d.put(new BString("cow"), new BString("moo"));
		BType t = new BDictionary(d);
		Assert.assertEquals("d3:foo3:bar3:cow3:mooe", t.getBencodedValue());
	}

	@Test
	public void testList() throws Exception {
		Map<BType, BType> d = new LinkedHashMap<>();
		List<BType> l = new ArrayList<>();
		l.add(new BString("b"));
		l.add(new BString("a"));
		l.add(new BString("r"));
		d.put(new BString("foo"), new BList(l));
		BType t = new BDictionary(d);
		Assert.assertEquals("d3:fool1:b1:a1:ree", t.getBencodedValue());
	}

	@Test
	public void testEmpty() throws Exception {
		BType t = new BDictionary(new LinkedHashMap<>());
		Assert.assertEquals("de", t.getBencodedValue());
	}

	@Test
	public void testNull() throws Exception {
		BType t = new BDictionary(null);
		Assert.assertEquals("de", t.getBencodedValue());
	}
}