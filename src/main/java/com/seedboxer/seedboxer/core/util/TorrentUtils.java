/*******************************************************************************
 * TorrentUtils.java
 * 
 * Copyright (c) 2012 SeedBoxer Team.
 * 
 * This file is part of SeedBoxer.
 * 
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.seedboxer.seedboxer.core.util;

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
