/*******************************************************************************
 * QueueProcessor.java
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

package com.seedboxer.seedboxer.sources.processors;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.klomp.snark.bencode.BDecoder;
import org.klomp.snark.bencode.BEValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seedboxer.seedboxer.core.domain.User;
import com.seedboxer.seedboxer.core.logic.DownloadsQueueManager;
import com.seedboxer.seedboxer.sources.domain.Content;
import com.seedboxer.seedboxer.sources.type.DownloadableItem;



/**
 *
 * @author The-Sultan
 */
@Component
public class QueueProcessor implements Processor{

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueProcessor.class);

	@Autowired
	private DownloadsQueueManager queueManager;

	@Value(value="${proeasy.watchDownloaderPath}")
	private String path;

	@Value(value="${proeasy.completePath}")
	private String completePath;

	@Override
	public void process(Exchange exchange)  {
		DownloadableItem downloadableItem = (DownloadableItem) exchange.getIn().getBody();
		Content content = downloadableItem.getContent();
		URL url = content.getMatchableItem().getUrl();
		String fileName = url.getFile().substring(url.getFile().lastIndexOf("/"));
		String filePath = path + "/" + fileName;
		LOGGER.debug("Filename: "+fileName);

		try {
			downloadFile(url,filePath);
			String dirName = getDirNameFromTorrentFile(filePath);
			LOGGER.info("Downloaded torrent: "+path);
			for(User user : downloadableItem.getUsers()){
				String absoluteOutputDir = completePath + "/"+ dirName;
				queueManager.push(user, absoluteOutputDir);
			}
		} catch (IOException ex) {
			LOGGER.error("Error downloading file: {}", url, ex);
		}
	}

	@SuppressWarnings("rawtypes")
	private String getDirNameFromTorrentFile(String path) throws FileNotFoundException, IOException{
		BDecoder decoder;
		decoder = new BDecoder(new FileInputStream(path));
		Map map = decoder.bdecode().getMap();
		BEValue info = (BEValue) map.get("info");
		Map mapInfo = info.getMap();
		return ((BEValue)mapInfo.get("name")).getString();

	}

	private File downloadFile(URL url, String path) throws IOException {
		URLConnection conn =  url.openConnection();
		InputStream in = conn.getInputStream();
		File file =   new File(path);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		byte[] bytes = new byte[1024];
		int read;
		while ((read = in.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		in.close();
		out.close();
		return file;
	}
}
