
package com.superdownloader.proEasy.processors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * @author jdavison
 *
 */
public class FileReceivedProcessor implements Processor {

	private Pattern pattern = Pattern.compile("");
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		
		//String origFileName = (String) msg.getHeader(Exchange.FILE_NAME_ONLY);
		//File f = new File((String) msg.getHeader(Exchange.FILE_PATH));
		//String fileFolder = (String) msg.getHeader(Exchange.FILE_NAME);
		
		/*Matcher m = pattern.matcher((String) msg.getHeader(Exchange.FILE_NAME));
		if (m.matches()) {
			String userName = m.group(1);
			msg.setHeader("USER_NAME", userName);
		}else
			throw new Exception("The file doesn't compile with the pattern.");*/
		
		//TODO: Set header for FTP component to upload
		
		//TODO: Set header for SMTP component to send notification


	}

}
