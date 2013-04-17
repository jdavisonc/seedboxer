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

package net.seedboxer.seedboxer.sources.processors;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import net.seedboxer.bencode.TorrentUtils;
import net.seedboxer.seedboxer.core.domain.Content;
import net.seedboxer.seedboxer.core.domain.User;
import net.seedboxer.seedboxer.core.logic.DownloadsQueueManager;
import net.seedboxer.seedboxer.core.util.FileUtils;
import net.seedboxer.seedboxer.sources.type.DownloadableItem;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 *
 * @author The-Sultan
 */
@Component
public class QueueProcessor implements Processor{

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueProcessor.class);

	@Autowired
	private DownloadsQueueManager queueManager;

	@Value(value="${watchDownloaderPath}")
	private String path;

	@Value(value="${completePath}")
	private String completePath;

	@Override
	public void process(Exchange exchange)  {
		DownloadableItem downloadableItem = (DownloadableItem) exchange.getIn().getBody();
		Content content = downloadableItem.getContent();
		URL url = content.getMatchableItem().getUrl();
		try {
			String fileName = downloadFile(url,path);
			String dirName = TorrentUtils.getTorrentName(new File(path + File.separator + fileName));
			LOGGER.debug("Downloaded torrent: "+path);
			for(User user : downloadableItem.getUsers()){
				String absoluteOutputDir = completePath + File.separator + dirName;
				queueManager.push(user, absoluteOutputDir);
			}
		} catch (IOException ex) {
			LOGGER.error("Error downloading file: {}", url, ex);
		}
	}

	private String downloadFile(URL url, String path) throws IOException {
		final URLConnection conn =  url.openConnection();

		String disposition = conn.getHeaderField("Content-Disposition");
		String fileNameProperty = "filename=\"";
		String fileName = disposition.substring(disposition.indexOf(fileNameProperty),disposition.lastIndexOf("\""));
		fileName = fileName.substring(fileNameProperty.length(),fileName.length());
		path += File.separator + fileName;

		FileUtils.copyFile(conn.getInputStream(), path, true, true);

		return fileName;
	}
}
