package model.bittorrent.bencoding;

/**
 * Bencoded strings are encoded as follows: <string length encoded in base ten ASCII>:<string data>, or key:value
 *
 * Note that there is no constant beginning delimiter, and no ending delimiter.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class BString implements BType {
	private String value;

	public BString(String value) {
		this.value = (value != null ? value : "");
	}

	@Override
	public String getBencodedValue() {
		return value.length() + ":" + value;
	}
}
