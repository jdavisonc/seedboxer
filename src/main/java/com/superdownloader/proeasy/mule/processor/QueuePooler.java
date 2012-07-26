package com.superdownloader.proeasy.mule.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.logic.DownloadsQueueManager;
import com.superdownloader.proeasy.core.type.DownloadQueueItem;

/**
 * @author harley
 *
 */
@Component
public class QueuePooler implements Processor {

	@Autowired
	private DownloadsQueueManager queueManager;

	@Override
	public void process(Exchange exchange) throws Exception {
		List<DownloadQueueItem> items = queueManager.pop();
		if (!items.isEmpty()) {
			exchange.getIn().setBody(items);
		}
	}

}
