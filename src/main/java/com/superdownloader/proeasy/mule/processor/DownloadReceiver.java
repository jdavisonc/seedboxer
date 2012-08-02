package com.superdownloader.proeasy.mule.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.logic.DownloadsSessionManager;
import com.superdownloader.proeasy.core.logic.UsersController;
import com.superdownloader.proeasy.core.type.DownloadQueueItem;

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
		int userId = item.getUserId();
		int downloadId = item.getId();

		msg.setHeader(Headers.USER_ID, userId);
		msg.setHeader(Headers.DOWNLOAD_ID, downloadId);
		msg.setHeader(Headers.START_TIME, new Date());
		Map<String, String> configs = usersController.userConfiguration(userId);
		for (Entry<String, String> entry : configs.entrySet()) {
			msg.setHeader(entry.getKey(), entry.getValue());
		}
		LOGGER.debug("USER_ID={}", userId);
		LOGGER.debug("CONFIGS={}", configs);
		LOGGER.debug("DOWNLOAD_ID={}", downloadId);

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
		uploadSessionManager.setUserDownloadSize(item.getUserId(), item.getId(), totalSize);

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
