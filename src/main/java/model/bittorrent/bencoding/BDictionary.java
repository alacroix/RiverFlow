package model.bittorrent.bencoding;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Dictionaries are encoded as follows: d<bencoded string><bencoded element>e
 * <p>
 * The initial d and trailing e are the beginning and ending delimiters.
 * <p>
 * Note that the keys must be bencoded strings.
 * The values may be any bencoded type, including integers, strings, lists, and other dictionaries.
 * Keys must be strings and appear in sorted order (sorted as raw strings, not alphanumerics).
 * The strings should be compared using a binary comparison, not a culture-specific "natural" comparison.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class BDictionary extends TreeMap<String, BType> implements BType {
	public final static char DELIMITER_START = 'd';
	public final static char DELIMITER_END = 'e';

	@Override
	public String getBencodedValue() {
		String encodedValue = "" + DELIMITER_START;

		// for each item, concat bencoded value
		for (Map.Entry<String, BType> entry : this.entrySet()) {
			encodedValue += new BString(entry.getKey()).getBencodedValue();
			encodedValue += entry.getValue().getBencodedValue();
		}

		return encodedValue + DELIMITER_END;
	}

	public static BDictionary read(byte[] content, AtomicInteger index) {
		BDictionary d = new BDictionary();

		if (content[index.get()] == DELIMITER_START) {
			index.set(index.get() + 1);
		} else {
			throw new RuntimeException("Invalid call for dictionary");
		}

		while (content[index.get()] != DELIMITER_END) {
			BString key = (BString) Reader.read(content, index);
			BType val = Reader.read(content, index);
			d.put(key.getValue(), val);
		}

		index.set(index.get() + 1);

		return d;
	}
}
