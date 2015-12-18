package model.bittorrent.bencoding;

/**
 * Bencoding is a way to specify and organize data in a terse format.
 * <p>
 * It supports the following types: byte strings, integers, lists, and dictionaries.
 *
 * @author Adrien Lacroix
 * @version 0.2.0
 */
public interface BType {
	String getBencodedValue();

	byte[] getBencodedBytes();
}
