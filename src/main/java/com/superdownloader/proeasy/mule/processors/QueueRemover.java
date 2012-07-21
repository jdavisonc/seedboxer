package com.superdownloader.proeasy.mule.processors;

import java.io.FileNotFoundException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.persistence.DownloadsQueueDao;

/**
 * @author harley
 *
 */
@Component
public class QueueRemover implements Processor {

	@Autowired
	private DownloadsQueueDao queueDao;

	@Override
	public void process(Exchange exchange) throws Exception {
		int downloadId = (Integer) exchange.getIn().getHeader(Headers.DOWNLOAD_ID);
		Exception exception = (Exception) exchange.getProperty("CamelExceptionCaught");

		if (exception != null && exception instanceof FileNotFoundException) {
			// If it was due to a FileNotFoundException, then the download is not ready to remove
			//   from queue, so it need to be pushed again.
			queueDao.repush(downloadId);
		} else {
			queueDao.remove(downloadId);
		}
	}

}
