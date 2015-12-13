package model.bittorrent.bencoding;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
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