package model.bittorrent.bencoding;

/**
 * Bencoding is a way to specify and organize data in a terse format.
 *
 * It supports the following types: byte strings, integers, lists, and dictionaries.
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public interface BType {
	String getBencodedValue();
}
