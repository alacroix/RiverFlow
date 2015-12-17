package model.bittorrent.bencoding;

import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Bencoded strings are encoded as follows: <string length encoded in base ten ASCII>:<string data>, or key:value
 * <p>
 * Note that there is no constant beginning delimiter, and no ending delimiter.
 *
 * @author Adrien Lacroix
 * @version 0.2.0
 */
public class BString implements BType {
	private String value;

	/**
	 * if String is an Base64 encoded string
	 */
	private boolean hasBeenEncoded;

	public BString(String value) {
		this(value, false);
	}

	public BString(String value, boolean hasBeenEncoded) {
		this.value = (value != null ? value : "");
		this.hasBeenEncoded = hasBeenEncoded;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String getBencodedValue() {
		return value.length() + ":" + value;
	}

	@Override
	public byte[] getBencodedBytes() {
		if (hasBeenEncoded) {
			byte[] decoded = Base64.getDecoder().decode(value);

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			try {
				output.write(String.valueOf(decoded.length).getBytes());
				output.write(':');
				output.write(decoded);
			} catch (IOException e) {
				System.err.println("Error during bencoding bytes");
			}
			return output.toByteArray();
		} else {
			return getBencodedValue().getBytes(StandardCharsets.UTF_8);
		}
	}

	public static BString read(byte[] content, AtomicInteger index) {
		// get index of separator
		final int sepIndex = ArrayUtils.indexOf(content, (byte) ':', index.get());
		// get length of string
		// TODO: Improve the cast (ByteBuffer ?)
		String lengthS = new String(ArrayUtils.subarray(content, index.get(), sepIndex),
				StandardCharsets.UTF_8);
		final int length = Integer.valueOf(lengthS);

		// update index position
		index.set(sepIndex + 1);

		// get the string
		String value = null;
		byte[] val = {};
		if (length != 0) {
			val = ArrayUtils.subarray(content, index.get(), index.get() + length);
			value = new String(ArrayUtils.subarray(content, index.get(), index.get() + length), StandardCharsets.UTF_8);
		}

		// update index position
		index.set(index.get() + length);

		BString n = new BString(value);

		// if loss of characters (ex: sha1)
		boolean hasToBeEncoded = (n.getValue().length() != val.length);
		if (hasToBeEncoded) {
			value = Base64.getEncoder().encodeToString(val);
		}

		return new BString(value, hasToBeEncoded);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		BString o = (BString) obj;
		return this.value.equals(o.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value;
	}
}
