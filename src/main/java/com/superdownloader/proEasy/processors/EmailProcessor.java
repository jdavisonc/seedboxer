package com.superdownloader.proEasy.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class EmailProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		Message msg = exchange.getIn();

		String email = (String) msg.getHeader(Headers.NOTIFICATION_EMAIL);
		String username = (String) msg.getHeader(Headers.USERNAME);

		msg.setHeader("To", email);
		msg.setHeader("Subject", "Camel rocks");

		String body = "Hello " + username + ".\nYes it does.\n\nRegards H.";
		msg.setBody(body);

	}

}
