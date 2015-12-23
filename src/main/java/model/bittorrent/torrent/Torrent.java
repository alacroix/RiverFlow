package model.bittorrent.torrent;

import org.apache.commons.codec.binary.Hex;

import java.time.Instant;
import java.util.List;

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

	/* Metainfo getters */

	public AbstractInfoDictionary.Mode getMode() {
		return metainfo.getInfo().getMode();
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

	public boolean hasAnnounceList() {
		return metainfo.getAnnounceList() != null;
	}

	public List<List<String>> getAnnounceList() {
		return metainfo.getAnnounceList();
	}

	public boolean hasCreationDate() {
		return metainfo.getCreationDate() != null;
	}

	public Instant getCreationDate() {
		return metainfo.getCreationDate();
	}

	public boolean hasComment() {
		return metainfo.getComment() != null;
	}

	public String getComment() {
		return metainfo.getComment();
	}

	public boolean hasAuthor() {
		return metainfo.getCreatedBy() != null;
	}

	public String getAuthor() {
		return metainfo.getCreatedBy();
	}

	public boolean hasEncoding() {
		return metainfo.getEncoding() != null;
	}

	public String getEncoding() {
		return metainfo.getEncoding();
	}

	/* Torrent getters */

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

	/* Overriding */

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

	@Override
	public String toString() {
		String padding = "";
		for (int i = 0; i < this.getClass().getSimpleName().length() + 1; i++) {
			padding += " ";
		}

		StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());

		// info hash
		builder.append("<info hash=").append(getHexInfoHash());
		// announce
		builder.append(",\n").append(padding);
		builder.append("announce=").append(getAnnounce());
		// announce list
		if (hasAnnounceList()) {
			builder.append(",\n").append(padding);
			builder.append("announce list=").append(getAnnounceList());
		}
		// creation date
		if (hasCreationDate()) {
			builder.append(",\n").append(padding);
			builder.append("creation date=").append(getCreationDate());
		}
		// comment
		if (hasComment()) {
			builder.append(",\n").append(padding);
			builder.append("comment=").append(getComment());
		}
		// author
		if (hasAuthor()) {
			builder.append(",\n").append(padding);
			builder.append("created by=").append(getAuthor());
		}
		// encoding
		if (hasEncoding()) {
			builder.append(",\n").append(padding);
			builder.append("encoding=").append(getEncoding());
		}
		builder.append('>');

		return builder.toString();
	}
}
