package model.client;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class PeerID {

	private final static int ID_LENGTH = 20;

	private static String peerID;

	public static String generatePeerID() {
		StringBuilder builder = new StringBuilder();
		builder.append('-');
		builder.append(Version.INITIALS);
		builder.append(Version.MAJOR).append(Version.MINOR).append(Version.CHANGE);
		builder.append('-');

		builder.append(RandomStringUtils.random(ID_LENGTH - builder.length(), false, true));

		peerID = builder.toString();

		return peerID;
	}

	public static String getLastGeneratedPeerID() {
		return peerID;
	}
}
