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
		LOGGER.debug("{}", msg.getHeaders());

		DownloadQueueItem item = (DownloadQueueItem) msg.getBody();
		User user = item.getUser();

		msg.setHeader(Headers.USER_ID, user.getId());
		msg.setHeader(Headers.DOWNLOAD_ID, item.getId());
		msg.setHeader(Headers.START_TIME, new Date());
		List<UserConfiguration> configs = usersController.getUserConfig(user.getId());
		for (UserConfiguration conf : configs) {
			msg.setHeader(conf.getName(), conf.getValue());
		}
		LOGGER.debug("USER_ID={}", user.getId());
		LOGGER.debug("CONFIGS={}", configs);
		LOGGER.debug("DOWNLOAD_ID={}", item.getId());

		processDownload(msg, user.getId(), item.getId(), item.getDownload());
	}

	private void processDownload(Message msg, long userId, long downloadId, String downloadPath) throws FileNotFoundException {
		// Calculate size of the upload
		String fileName;
		long totalSize = 0;

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
		uploadSessionManager.addUserDownload(userId, downloadId, downloadPath, totalSize);

		LOGGER.debug("FILES_TO_UPLOAD={}", toUpload);
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
