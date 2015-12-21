package model.bittorrent.bencoding;

/**
 * Read a bencoded type
 *
 * @author Adrien Lacroix
 * @version 0.2.0
 */
public class BException extends RuntimeException {

	public BException(String message) {
		super(message);
	}
}
