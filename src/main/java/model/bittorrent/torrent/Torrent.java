package model.bittorrent.torrent;

import org.apache.commons.codec.binary.Hex;

/**
 * Class representation of a torrent
 *
 * @author Adrien Lacroix
 * @version 0.2.0
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

	public void setUploaded(int uploaded) {
		this.uploaded = uploaded;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		Torrent o = (Torrent) obj;
		return this.getHexInfoHash().equals(o.getHexInfoHash());
	}

	@Override
	public int hashCode() {
		return getHexInfoHash().hashCode();
	}
}
