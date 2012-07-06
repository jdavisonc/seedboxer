package com.superdownloader.proeasy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.klomp.snark.bencode.BDecoder;
import org.klomp.snark.bencode.BEValue;
import org.klomp.snark.bencode.InvalidBEncodingException;

public class TorrentUtils {

	public static String getName(File torrent) throws Exception {
		try {
			BDecoder decoder = new BDecoder(new FileInputStream(torrent));
			Map map = ((BEValue)decoder.bdecode()).getMap();
			BEValue info = (BEValue) map.get("info");
			Map mapInfo = info.getMap();
	
			return ((BEValue)mapInfo.get("name")).getString();
		} catch (InvalidBEncodingException e) {
			throw new Exception("Unexpected Exception when reading torrent file", e);
		}
	}

}
