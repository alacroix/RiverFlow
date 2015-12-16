package model.bittorrent.bencoding;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lists are encoded as follows: l<bencoded values>e
 * <p>
 * The initial l and trailing e are beginning and ending delimiters.
 * <p>
 * Lists may contain any bencoded type, including integers, strings, dictionaries, and even lists within other lists.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class BList extends ArrayList<BType> implements BType {
	public final static char DELIMITER_START = 'l';
	public final static char DELIMITER_END = 'e';

	@Override
	public String getBencodedValue() {
		String encodedValue = "" + DELIMITER_START;

		// for each item, concat bencoded value
		for (BType t : this) {
			encodedValue += t.getBencodedValue();
		}

		encodedValue += DELIMITER_END;

		return encodedValue;
	}

	public static BList read(byte[] content, AtomicInteger index) {
		BList l = new BList();

		if (content[index.get()] == DELIMITER_START) {
			index.set(index.get() + 1);
		} else {
			throw new RuntimeException("Invalid call for list");
		}

		while (content[index.get()] != DELIMITER_END) {
			l.add(Reader.read(content, index));
		}

		index.set(index.get() + 1);

		return l;
	}
}
