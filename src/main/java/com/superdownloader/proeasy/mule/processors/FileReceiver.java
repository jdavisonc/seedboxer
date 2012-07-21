package com.superdownloader.proeasy.mule.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.logic.DownloadSessionManager;
import com.superdownloader.proeasy.core.logic.UsersController;


/**
 * @author jdavison
 *
 */
@Component
public class FileReceiver implements Processor {

	private static final long  MEGABYTE = 1024L * 1024L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FileReceiver.class);

	@Autowired
	private DownloadSessionManager uploadSessionManager;

	@Autowired
	private UsersController usersController;

	private Pattern pattern = null;

	@Value("${proeasy.includePattern}")
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
			int userId = usersController.getUserId(username);

			msg.setHeader(Headers.USER_ID, userId);
			msg.setHeader(Headers.START_TIME, new Date());
			Map<String, String> configs = usersController.userConfiguration(userId);
			for (Entry<String, String> entry : configs.entrySet()) {
				msg.setHeader(entry.getKey(), entry.getValue());
			}

			// Calculate size of the upload
			List<String> filesPaths = getLines(filepath);
			List<String> filesToUpload = new ArrayList<String>();
			List<String> filesName = new ArrayList<String>();
			long totalSize = 0;
			for (String path : filesPaths) {

				// Removes prefix of Flexget
				String realPath = path.replaceFirst("file://", "");

				File toUpload = new File(realPath);
				if (toUpload.exists()) {
					totalSize += calculateSize(toUpload);
					filesToUpload.add(realPath);
					filesName.add(toUpload.getName());
				} else {
					throw new FileNotFoundException("File " + realPath + " doesn't exists.");
				}
			}
			msg.setHeader(Headers.FILES, filesToUpload);
			msg.setHeader(Headers.FILES_NAME, filesName);

			// Size in Mbs
			totalSize = totalSize / MEGABYTE;
			//uploadSessionManager.setUserDownloadSize(userId, filepath, totalSize);

			LOGGER.debug("USERNAME={}", userId);
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
