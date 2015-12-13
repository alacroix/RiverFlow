package model.bittorrent.bencoding;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dictionaries are encoded as follows: d<bencoded string><bencoded element>e
 *
 * The initial d and trailing e are the beginning and ending delimiters.
 *
 * Note that the keys must be bencoded strings.
 * The values may be any bencoded type, including integers, strings, lists, and other dictionaries.
 * Keys must be strings and appear in sorted order (sorted as raw strings, not alphanumerics).
 * The strings should be compared using a binary comparison, not a culture-specific "natural" comparison.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class BDictionary implements BType {
	private Map<BType, BType> dictionary;

	public BDictionary(Map<BType, BType> dictionary) {
		this.dictionary = (dictionary != null ? dictionary : new LinkedHashMap<>());
	}

	@Override
	public String getBencodedValue() {
		String encodedValue = "d";

		// for each item, concat bencoded value
		for (Map.Entry<BType, BType> e : dictionary.entrySet()) {
			encodedValue += e.getKey().getBencodedValue();
			encodedValue += e.getValue().getBencodedValue();
		}

		encodedValue += "e";

		return encodedValue;
	}
}
