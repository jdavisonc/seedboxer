package com.superdownloader.proeasy.mule.processors;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.persistence.DownloadsQueueDao;
import com.superdownloader.proeasy.core.types.DownloadQueueItem;

/**
 * @author harley
 *
 */
@Component
public class QueuePooler implements Processor {

	@Autowired
	private DownloadsQueueDao queueDao;

	@Override
	public void process(Exchange exchange) throws Exception {
		List<DownloadQueueItem> items = queueDao.pop();
		if (!items.isEmpty()) {
			exchange.getIn().setBody(items);
		}
	}

}
