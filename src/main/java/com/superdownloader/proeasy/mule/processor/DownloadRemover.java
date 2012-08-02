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
		int userId = (Integer) exchange.getIn().getHeader(Headers.USER_ID);
		int downloadId = (Integer) exchange.getIn().getHeader(Headers.DOWNLOAD_ID);
		Exception exception = (Exception) exchange.getProperty("CamelExceptionCaught");

		if (exception != null && exception instanceof FileNotFoundException) {
			// If it was due to a FileNotFoundException, then the download is not ready to remove
			//   from queue, so it need to be pushed again.
			queueManager.repush(userId, downloadId);
		} else {
			queueManager.remove(userId, downloadId);
		}
		// Remove download session
		sessionManager.removeUserDownload(userId, downloadId);
	}

}
