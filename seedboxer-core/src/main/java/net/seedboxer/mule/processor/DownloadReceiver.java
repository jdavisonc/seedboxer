package net.seedboxer.mule.processor;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.DownloadQueueItem;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.logic.DownloadsSessionManager;
import net.seedboxer.core.logic.UsersManager;
import net.seedboxer.core.util.FileUtils;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class DownloadReceiver implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadReceiver.class);

	@Autowired
	private UsersManager usersController;
	
	@Autowired
	private DownloadsSessionManager sessionManager;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();

		DownloadQueueItem item = (DownloadQueueItem) msg.getBody();
		User user = item.getUser();
		long downloadId = item.getId();
		
		Map<String, String> configs = usersController.getUserConfig(user.getId());
		msg.setHeaders(new HashMap<String, Object>(configs));
		msg.setHeader(Configuration.USER, user);
		msg.setHeader(Configuration.DOWNLOAD_ID, item.getId());
		msg.setHeader(Configuration.START_TIME, new Date());
		
		processDownload(msg, item);
		LOGGER.info("USER_ID={}, DOWNLOAD_ID={}", user.getId(), downloadId);
	}

	private void processDownload(Message msg, DownloadQueueItem item) throws IOException {
		String downloadPath = item.getDownload();
		File toUpload = new File(downloadPath);
		if (!toUpload.exists()) {
			throw new FileNotFoundException("File " + downloadPath + " doesn't exists.");
		}
		String fileName = toUpload.getName();

		msg.setHeader(Configuration.FILE, downloadPath);
		msg.setHeader(Configuration.FILE_NAME, fileName);
		msg.setHeader(Exchange.FILE_NAME, fileName);
		msg.setBody(toUpload);
		
		sessionManager.addSession(item.getUser().getId(), fileName, FileUtils.calculateSize(toUpload));
	}

}
