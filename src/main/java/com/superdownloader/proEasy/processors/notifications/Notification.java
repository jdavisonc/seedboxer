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
		Throwable t = (Throwable) exchange.getProperty(Exchange.FAILURE_ENDPOINT);
		if (t == null) {
			processSuccessNotification(msg);
		} else {
			processFailNotification(msg, t);
		}
	}

	protected abstract void processSuccessNotification(Message msg);

	protected abstract void processFailNotification(Message msg, Throwable t);

}
