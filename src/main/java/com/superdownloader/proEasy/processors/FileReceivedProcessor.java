
package com.superdownloader.proEasy.processors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
		LOGGER.debug("{}", msg.getHeaders());

		Matcher m = pattern.matcher((String) msg.getHeader(Exchange.FILE_NAME));
		if (m.matches()) {
			String username = m.group(1);

			msg.setHeader(Headers.USERNAME, username);
			Map<String, String> configs = usersDao.getUserConfigs(username);
			for (Entry<String, String> entry : configs.entrySet()) {
				msg.setHeader(entry.getKey(), entry.getValue());
			}

			List<String> filesToUpload = getLines((String) msg.getHeader(Exchange.FILE_PATH));
			msg.setHeader(Headers.FILES, filesToUpload);

			LOGGER.debug("USERNAME={}", username);
			LOGGER.debug("CONFIGS={}", configs);
			LOGGER.debug("FILES_TO_UPLOAD={}", filesToUpload);

		} else {
			throw new Exception("The file doesn't compile with the pattern.");
		}
	}

	private List<String> getLines(String filePath) {
		List<String> lines = new ArrayList<String>();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(filePath));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	lines.add(str);
		    }
		    in.close();
		} catch (IOException e) {
			LOGGER.error("Cannot open file", e);
		}
		return lines;
	}

}
