package model.bittorrent.bencoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Lists are encoded as follows: l<bencoded values>e
 *
 * The initial l and trailing e are beginning and ending delimiters.
 *
 * Lists may contain any bencoded type, including integers, strings, dictionaries, and even lists within other lists.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class BList implements BType {
	private List<BType> list;

	public BList(List<BType> list) {
		this.list = (list != null ? list : new ArrayList<>());
	}

	@Override
	public String getBencodedValue() {
		String encodedValue = "l";

		// for each item, concat bencoded value
		for (BType t : list) {
			encodedValue += t.getBencodedValue();
		}

		encodedValue += "e";

		return encodedValue;
	}
}
