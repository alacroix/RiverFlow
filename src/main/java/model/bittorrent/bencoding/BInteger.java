package model.bittorrent.bencoding;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Integers are encoded as follows: i<integer encoded in base ten ASCII>e
 * <p>
 * The initial i and trailing e are beginning and ending delimiters.
 * You can have negative numbers such as i-3e.
 * Only the significant digits should be used, one cannot pad the Integer with zeroes, such as i04e.
 * However, i0e is valid.
 *
 * @author Adrien Lacroix
 * @version 0.2.0
 */
public class BInteger implements BType {
	private Integer value;

	public BInteger(Integer value) {
		this.value = (value != null ? value : 0);
	}

	public Integer getValue() {
		return value;
	}

	@Override
	public String getBencodedValue() {
		return "i" + value + "e";
	}

	@Override
	public byte[] getBencodedBytes() {
		return getBencodedValue().getBytes(StandardCharsets.UTF_8);
	}

	public static BInteger read(byte[] content, AtomicInteger index) {
		// get index of integer's end
		final int indexEnd = ArrayUtils.indexOf(content, (byte) 'e', index.get());
		// get length of string
		String valueS = new String(ArrayUtils.subarray(content, index.get() + 1, indexEnd),
				StandardCharsets.UTF_8);
		final int value = Integer.valueOf(valueS);

		// update index position
		index.set(indexEnd + 1);

		return new BInteger(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		BInteger o = (BInteger) obj;
		return this.value.equals(o.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
