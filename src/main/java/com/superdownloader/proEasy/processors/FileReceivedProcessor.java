
package com.superdownloader.proEasy.processors;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jdavison
 *
 */
public class FileReceivedProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileReceivedProcessor.class);

	private Pattern pattern = null;

	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		LOGGER.debug(msg.toString());

		String origFileName = (String) msg.getHeader(Exchange.FILE_NAME_ONLY);
		File f = new File((String) msg.getHeader(Exchange.FILE_PATH));
		String fileFolder = (String) msg.getHeader(Exchange.FILE_NAME);

		Matcher m = pattern.matcher((String) msg.getHeader(Exchange.FILE_NAME));
		if (m.matches()) {
			String userName = m.group(1);
			msg.setHeader("USER_NAME", userName);
			LOGGER.debug("USERNAME={}", userName);
		}else
			throw new Exception("The file doesn't compile with the pattern.");

		//TODO: Set header for FTP component to upload

		//TODO: Set header for SMTP component to send notification


	}

}
