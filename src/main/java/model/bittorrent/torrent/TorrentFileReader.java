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
 * Utils class to read torrent file and extract a Metainfo
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class TorrentFileReader {

	public static Metainfo readTorrentFile(Path path) throws IOException {
		// read file
		byte[] bytes = Files.readAllBytes(path);

		final AtomicInteger index = new AtomicInteger(0);
		BDictionary master = (BDictionary) Reader.read(bytes, index);
		BDictionary infoD = (BDictionary) master.get("info");

		// mandatory elements
		AbstractInfoDictionary info = extractInfoDictionary(infoD);
		BString announce = (BString) master.get("announce");

		// TODO: optional elements
		//BString comment = (BString) dictionary.get(COMMENT_ELEMENT);

		return new Metainfo(info, DigestUtils.sha1(infoD.getBencodedBytes()), announce.getValue());
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
