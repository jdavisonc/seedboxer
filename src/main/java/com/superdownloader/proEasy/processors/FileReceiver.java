
package com.superdownloader.proEasy.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
@Service(value = "fileReciever")
public class FileReceiver implements Processor {

	private static final long  MEGABYTE = 1024L * 1024L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FileReceiver.class);

	@Autowired
	private UploadSessionManager uploadSessionManager;

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
			String filepath = (String) msg.getHeader(Exchange.FILE_PATH);

			msg.setHeader(Headers.USERNAME, username);
			msg.setHeader(Headers.START_TIME, new Date());
			Map<String, String> configs = usersDao.getUserConfigs(username);
			for (Entry<String, String> entry : configs.entrySet()) {
				msg.setHeader(entry.getKey(), entry.getValue());
			}

			List<String> filesToUpload = getLines(filepath);
			msg.setHeader(Headers.FILES, filesToUpload);

			// Calculate size of the upload
			long totalSize = 0;
			for (String path : filesToUpload) {
				totalSize += calculateSize(new File(path));
			}
			// Size in Mbs
			totalSize = totalSize / MEGABYTE;
			uploadSessionManager.setUserUploadSize(username, filepath, totalSize);

			LOGGER.debug("USERNAME={}", username);
			LOGGER.debug("CONFIGS={}", configs);
			LOGGER.debug("FILES_TO_UPLOAD={}", filesToUpload);

		} else {
			throw new Exception("The file doesn't compile with the pattern.");
		}
	}

	/**
	 * Calculate the size of a file or directory
	 * @param upload
	 * @return
	 */
	private long calculateSize(File upload) {
		if (upload.isDirectory()) {
			long lengthDir = 0L;
			for (File fileInside : upload.listFiles()) {
				lengthDir += calculateSize(fileInside);
			}
			return lengthDir;
		} else {
			return upload.length();
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
