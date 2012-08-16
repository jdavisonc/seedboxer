/*******************************************************************************
 * DownloadReceiver.java
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
package com.superdownloader.proeasy.mule.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.domain.DownloadQueueItem;
import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.core.domain.UserConfiguration;
import com.superdownloader.proeasy.core.logic.DownloadsSessionManager;
import com.superdownloader.proeasy.core.logic.UsersController;

/**
 * @author harley
 *
 */
@Component
public class DownloadReceiver implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadReceiver.class);
	private static final long  MEGABYTE = 1024L * 1024L;

	@Autowired
	private UsersController usersController;

	@Autowired
	private DownloadsSessionManager uploadSessionManager;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();

		DownloadQueueItem item = (DownloadQueueItem) msg.getBody();
		User user = item.getUser();
		long downloadId = item.getId();

		msg.setHeader(Headers.USER_ID, user.getId());
		msg.setHeader(Headers.DOWNLOAD_ID, item.getId());
		msg.setHeader(Headers.START_TIME, new Date());
		List<UserConfiguration> configs = usersController.getUserConfig(user.getId());
		for (UserConfiguration conf : configs) {
			msg.setHeader(conf.getName(), conf.getValue());
		}
		LOGGER.debug("USER_ID={}, DOWNLOAD_ID={}", user.getId(), downloadId);

		processDownload(msg, item);
	}

	private void processDownload(Message msg, DownloadQueueItem item) throws FileNotFoundException {
		// Calculate size of the upload
		String fileName;
		long totalSize = 0;

		String downloadPath = item.getDownload();
		File toUpload = new File(downloadPath);
		if (toUpload.exists()) {
			totalSize += calculateSize(toUpload);
			fileName = toUpload.getName();
		} else {
			throw new FileNotFoundException("File " + downloadPath + " doesn't exists.");
		}

		msg.setHeader(Headers.FILES, Collections.singletonList(downloadPath));
		msg.setHeader(Headers.FILES_NAME, Collections.singletonList(fileName));

		// Size in Mbs
		totalSize = totalSize / MEGABYTE;
		uploadSessionManager.setUserDownloadSize(item.getUser().getId(), item.getId(), totalSize);
	}

	/**
	 * Calculate the size of a file or directory
	 * @param download
	 * @return
	 */
	private long calculateSize(File download) {
		if (download.isDirectory()) {
			long lengthDir = 0L;
			for (File fileInside : download.listFiles()) {
				lengthDir += calculateSize(fileInside);
			}
			return lengthDir;
		} else {
			return download.length();
		}
	}

}
