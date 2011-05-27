/**
 *
 */
package com.superdownloader.proEasy.processors;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * @author harley
 *
 */
public class FtpSender implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		Message msg = exchange.getIn();

		File file = new File((String) msg.getHeader(Exchange.FILE_PATH));

		//producerTemplate.sendBodyAndHeader("ftp://user@host.com/remoteDirectory?password=secret", file, Exchange.FILE_NAME, file.getName());
		// TODO Auto-generated method stub

	}

}
