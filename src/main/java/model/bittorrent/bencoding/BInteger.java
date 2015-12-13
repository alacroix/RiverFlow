package model.bittorrent.bencoding;

/**
 * Integers are encoded as follows: i<integer encoded in base ten ASCII>e
 *
 * The initial i and trailing e are beginning and ending delimiters.
 * You can have negative numbers such as i-3e.
 * Only the significant digits should be used, one cannot pad the Integer with zeroes, such as i04e.
 * However, i0e is valid.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class BInteger implements BType {
	private Integer value;

	public BInteger(Integer value) {
		this.value = (value != null ? value : 0);
	}

	@Override
	public String getBencodedValue() {
		return "i" + value + "e";
	}
}