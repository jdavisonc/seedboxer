
package com.superdownloader.proEasy.processors;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proEasy.persistence.UsersDao;

/**
 * @author jdavison
 *
 */
@Service(value = "fileRecievedProcessor")
public class FileReceivedProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileReceivedProcessor.class);

	@Autowired
	private UsersDao usersDao;

	private Pattern pattern = null;

	@Value("${proEasy.includePattern}")
	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		LOGGER.debug(msg.toString());

		Matcher m = pattern.matcher((String) msg.getHeader(Exchange.FILE_NAME));
		if (m.matches()) {
			String username = m.group(1);

			msg.setHeader(Headers.USERNAME, username);
			Map<String, String> configs = usersDao.getUserConfigs(username);
			for (Entry<String, String> entry : configs.entrySet()) {
				msg.setHeader(entry.getKey(), entry.getValue());
			}

			LOGGER.debug("USERNAME={}", username);
			LOGGER.debug("CONFIGS={}", configs);

		} else {
			throw new Exception("The file doesn't compile with the pattern.");
		}
	}

}
