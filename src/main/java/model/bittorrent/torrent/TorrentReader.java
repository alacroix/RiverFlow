package model.bittorrent.torrent;

import model.bittorrent.bencoding.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utils class to read torrent file
 *
 * @author Adrien Lacroix
 * @version 0.3.0
 */
public class TorrentReader {

	private final static String INFO = "info";

	private final static String ANNOUNCE = "announce";
	private final static String ANNOUNCE_LIST = "announce-list";
	private final static String CREATION_DATE = "creation date";
	private final static String COMMENT = "comment";
	private final static String CREATED_BY = "created by";
	private final static String ENCODING = "encoding";

	public static Torrent readTorrentFile(Path path) throws IOException {
		// read file
		byte[] bytes = Files.readAllBytes(path);

		final AtomicInteger index = new AtomicInteger(0);
		BDictionary master = (BDictionary) Reader.read(bytes, index);

		// mandatory elements
		BDictionary infoD = (BDictionary) master.get(INFO);
		AbstractInfoDictionary info = extractInfoDictionary(infoD);
		BString announce = (BString) master.get(ANNOUNCE);

		// optional elements
		List<List<String>> announceList = null;
		if (master.containsKey(ANNOUNCE_LIST)) {
			announceList = new ArrayList<>();
			List<BType> firstList = (BList) master.get(ANNOUNCE_LIST);
			for (int i = 0; i < firstList.size(); i++) {
				announceList.add(i, new ArrayList<>());
				BList secondList = (BList) firstList.get(i);
				for (BType t : secondList) {
					BString s = (BString) t;
					announceList.get(i).add(s.getValue());
				}
			}
		}
		Integer creationDate = null;
		if (master.containsKey(CREATION_DATE)) {
			creationDate = ((BInteger) master.get(CREATION_DATE)).getValue();
		}
		String comment = null;
		if (master.containsKey(COMMENT)) {
			comment = ((BString) master.get(COMMENT)).getValue();
		}
		String createdBy = null;
		if (master.containsKey(CREATED_BY)) {
			createdBy = ((BString) master.get(CREATED_BY)).getValue();
		}
		String encoding = null;
		if (master.containsKey(ENCODING)) {
			encoding = ((BString) master.get(ENCODING)).getValue();
		}

		Metainfo metainfo = new Metainfo(info, DigestUtils.sha1(infoD.getBencodedBytes()), announce.getValue(),
				announceList, creationDate, comment, createdBy, encoding);

		return new Torrent(metainfo);
	}

	private static AbstractInfoDictionary extractInfoDictionary(BDictionary info) {
		AbstractInfoDictionary infoDictionary;

		BString name = (BString) info.get("name");
		BInteger pieceLength = (BInteger) info.get("piece length");
		BString pieces = (BString) info.get("pieces");
		Integer privateTracker = 0;
		if (info.containsKey("private")) {
			privateTracker = ((BInteger) info.get("private")).getValue();
		}

		// single file mode
		if (info.containsKey("length")) {
			BInteger length = (BInteger) info.get("length");

			String md5sum = null;
			if (info.containsKey("md5sum")) {
				md5sum = ((BString) info.get("md5sum")).getValue();
			}

			infoDictionary = new SingleFileDictionary(name.getValue(), pieceLength.getValue(),
					pieces.getValue(), length.getValue(), md5sum, privateTracker);
		} else {
			// list of dictionaries
			BList files = (BList) info.get("files");

			List<FileKeys> filesKeys = new ArrayList<>();
			for (BType t : files) {
				BDictionary d = (BDictionary) t;

				BInteger length = (BInteger) d.get("length");
				BList path = (BList) d.get("path");
				List<String> pathString = new ArrayList<>();
				for (int i = 0; i < path.size(); i++) {
					pathString.add(((BString) path.get(0)).getValue());
				}
				String md5Sum = null;
				if (d.containsKey("md5sum")) {
					md5Sum = ((BString) d.get("md5sum")).getValue();
				}

				filesKeys.add(new FileKeys(length.getValue(), pathString, md5Sum));
			}

			infoDictionary = new MultiFileDictionary(name.getValue(), pieceLength.getValue(),
					pieces.getValue(), filesKeys, privateTracker);
		}

		return infoDictionary;
	}
}
