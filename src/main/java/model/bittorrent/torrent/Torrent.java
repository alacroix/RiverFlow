package model.bittorrent.torrent;

import org.apache.commons.codec.binary.Hex;

/**
 * Class representation of a torrent
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class Torrent {

	private Metainfo metainfo;

	private int uploaded;

	private int downloaded;

	private byte[] bitField;

	public Torrent(Metainfo metainfo) {
		this.metainfo = metainfo;
		int bitFieldSize = (int) Math.floor(metainfo.getInfo().getPiecesNumber() / Byte.SIZE);
		this.bitField = new byte[bitFieldSize];
	}

	public String getAnnounce() {
		return metainfo.getAnnounce();
	}

	public byte[] getInfoHash() {
		return metainfo.getInfoHash();
	}

	public String getHexInfoHash() {
		return Hex.encodeHexString(metainfo.getInfoHash());
	}

	public int getUploaded() {
		return uploaded;
	}

	public int getDownloaded() {
		return downloaded;
	}

	public int getLeft() {
		return metainfo.getTotalLength() - downloaded;
	}

	public byte[] getBitField() {
		return bitField;
	}

	public void updateBitField(int index, boolean downloaded) {
		throw new UnsupportedOperationException();
	}

	public int getPiecesNumber() {
		return metainfo.getInfo().getPiecesNumber();
	}
}
