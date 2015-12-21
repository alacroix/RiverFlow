package model.bittorrent.bencoding;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Read a bencoded type
 *
 * @author Adrien Lacroix
 * @version 0.3.0
 */
public class Reader {

	public static BType read(byte[] bytes, AtomicInteger index) {
		switch (bytes[index.get()]) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return BString.read(bytes, index);
			case 'i':
				return BInteger.read(bytes, index);
			case BList.DELIMITER_START:
				return BList.read(bytes, index);
			case BDictionary.DELIMITER_START:
				return BDictionary.read(bytes, index);
			default:
				throw new BException("Unknown bencoded type : " + bytes[index.get()]);
		}
	}
}
