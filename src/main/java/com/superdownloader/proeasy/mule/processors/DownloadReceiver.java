package com.superdownloader.proeasy.mule.processors;

import java.io.File;
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

import com.superdownloader.proeasy.core.logic.UploadSessionManager;
import com.superdownloader.proeasy.core.logic.UsersController;
import com.superdownloader.proeasy.core.types.DownloadQueueItem;

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
	private UploadSessionManager uploadSessionManager;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		LOGGER.debug("{}", msg.getHeaders());

		DownloadQueueItem item = (DownloadQueueItem) msg.getBody();

		// Calculate size of the upload
		String fileName;
		long totalSize = 0;

		// Removes prefix of Flexget
		String realPath = item.getDownload().replaceFirst("file://", "");

		File toUpload = new File(realPath);
		if (toUpload.exists()) {
			totalSize += calculateSize(toUpload);
			fileName = toUpload.getName();
		} else {
			throw new Exception("File " + realPath + " doesn't exists.");
		}

		msg.setHeader(Headers.FILES, toUpload);
		msg.setHeader(Headers.FILES_NAME, fileName);
		msg.setHeader(Headers.USERNAME, username);
		msg.setHeader(Headers.START_TIME, new Date());
		Map<String, String> configs = usersController.userConfiguration(item.getUserId());
		for (Entry<String, String> entry : configs.entrySet()) {
			msg.setHeader(entry.getKey(), entry.getValue());
		}

		// Size in Mbs
		totalSize = totalSize / MEGABYTE;
		uploadSessionManager.setUserUploadSize(username, fileName, totalSize);

		LOGGER.debug("USERNAME={}", username);
		LOGGER.debug("CONFIGS={}", configs);
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
