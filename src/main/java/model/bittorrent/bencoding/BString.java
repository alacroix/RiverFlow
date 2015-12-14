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

	public String getValue() {
		return value;
	}

	@Override
	public String getBencodedValue() {
		return value.length() + ":" + value;
	}

	public static BString read(String content, final Integer index) {
		final int sepIndex = content.indexOf(':', index);
		final int length = Integer.valueOf(content.substring(index, sepIndex));
		String value = null;
		if (length != 0) {
			value = content.substring(sepIndex + 1, sepIndex + 1 + length);
		}

		return new BString(value);
	}
}
