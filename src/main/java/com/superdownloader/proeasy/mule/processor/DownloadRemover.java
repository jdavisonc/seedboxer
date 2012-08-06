package com.superdownloader.proeasy.mule.processor;

import java.io.FileNotFoundException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.logic.DownloadsQueueManager;
import com.superdownloader.proeasy.core.logic.DownloadsSessionManager;

/**
 * @author harley
 *
 */
@Component
public class DownloadRemover implements Processor {

	@Autowired
	private DownloadsQueueManager queueManager;

	@Autowired
	private DownloadsSessionManager sessionManager;

	@Override
	public void process(Exchange exchange) throws Exception {
		Long downloadId = (Long) exchange.getIn().getHeader(Headers.DOWNLOAD_ID);
		Long userId = (Long) exchange.getIn().getHeader(Headers.USER_ID);
		Exception exception = (Exception) exchange.getProperty("CamelExceptionCaught");

		if (exception != null && exception instanceof FileNotFoundException) {
			// If it was due to a FileNotFoundException, then the download is not ready to remove
			//   from queue, so it need to be pushed again.
			queueManager.repush(downloadId);
		} else {
			queueManager.remove(downloadId);
		}
		// Remove download session
		sessionManager.removeUserDownload(userId, downloadId);
	}

}
