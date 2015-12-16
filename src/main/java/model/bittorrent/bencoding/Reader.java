package model.bittorrent.bencoding;

import model.bittorrent.metainfo.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Used to read a .torrent file and extract its data
 *
 * @author Adrien Lacroix
 * @version 0.1.0
 */
public class Reader {

	public static MetainfoFile readTorrentFile(Path path) throws IOException {
		// read file
		byte[] bytes = Files.readAllBytes(path);

		final AtomicInteger index = new AtomicInteger(0);
		BDictionary master = (BDictionary) read(bytes, index);

		// mandatory elements
		BDictionary info = (BDictionary) master.get("info");
		BString announce = (BString) master.get("announce");

		AbstractInfoDictionary infoDictionary = extractInfoDictionary(info);

		// optional elements
		//BString comment = (BString) dictionary.get(COMMENT_ELEMENT);

		return new MetainfoFile(infoDictionary, announce.getValue());
	}

	private static AbstractInfoDictionary extractInfoDictionary(BDictionary info) {
		AbstractInfoDictionary infoDictionary = null;

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

	public static BType read(byte[] bytes, AtomicInteger index) {
		switch (bytes[index.get()]) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return BString.read(bytes, index);
			case 'i':
				return BInteger.read(bytes, index);
			case BList.DELIMITER_START:
				return BList.read(bytes, index);
			case BDictionary.DELIMITER_START:
				return BDictionary.read(bytes, index);
			default:
				throw new RuntimeException("Unknown bencoded type : " + bytes[index.get()]);
		}
	}
}
