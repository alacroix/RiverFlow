package model.bittorrent.bencoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
 * @version 0.3.0
 */
public class BDictionary extends TreeMap<String, BType> implements BType {

	public final static char DELIMITER_START = 'd';
	public final static char DELIMITER_END = 'e';

	@Override
	public String getBencodedValue() {
		StringBuilder builder = new StringBuilder();
		// start char
		builder.append(DELIMITER_START);

		// for each item, concat bencoded value
		for (Map.Entry<String, BType> entry : this.entrySet()) {
			builder.append(new BString(entry.getKey()).getBencodedValue());
			builder.append(entry.getValue().getBencodedValue());
		}

		// end char
		builder.append(DELIMITER_END);

		return builder.toString();
	}

	@Override
	public byte[] getBencodedBytes() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		output.write(DELIMITER_START);

		// for each item, concat bencoded value
		for (Map.Entry<String, BType> entry : this.entrySet()) {
			try {
				output.write(new BString(entry.getKey()).getBencodedBytes());
				output.write(entry.getValue().getBencodedBytes());
			} catch (IOException e) {
				throw new BException("Error during bencoding bytes");
			}
		}

		output.write(DELIMITER_END);

		return output.toByteArray();
	}

	public static BDictionary read(byte[] content, AtomicInteger index) {
		BDictionary d = new BDictionary();

		if (content[index.get()] == DELIMITER_START) {
			index.set(index.get() + 1);
		} else {
			throw new BException("Invalid call for dictionary");
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
