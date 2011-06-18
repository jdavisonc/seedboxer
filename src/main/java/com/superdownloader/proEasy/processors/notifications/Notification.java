package com.superdownloader.proEasy.processors.notifications;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 *
 * @author harley
 *
 */
public abstract class Notification implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		String failureEndpoint = (String) exchange.getProperty(Exchange.FAILURE_ENDPOINT);
		if (failureEndpoint == null) {
			processSuccessNotification(msg);
		} else {
			processFailNotification(msg, (Throwable) exchange.getProperty(Exchange.EXCEPTION_CAUGHT));
		}
	}

	protected abstract void processSuccessNotification(Message msg);

	protected abstract void processFailNotification(Message msg, Throwable t);

}
