/*******************************************************************************
 * FileReceiver.java
 * 
 * Copyright (c) 2012 SeedBoxer Team.
 * 
 * This file is part of SeedBoxer.
 * 
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.seedboxer.seedboxer.mule.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import com.seedboxer.seedboxer.core.domain.User;
import com.seedboxer.seedboxer.core.logic.DownloadsQueueManager;
import com.seedboxer.seedboxer.core.logic.UsersController;


/**
 * @author jdavison
 *
 */
@Component
public class FileReceiver implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileReceiver.class);

	@Autowired
	private DownloadsQueueManager queueManager;

	@Autowired
	private UsersController usersController;

	private Pattern pattern = null;

	@Value("${includePattern}")
	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();

		Matcher m = pattern.matcher((String) msg.getHeader(Exchange.FILE_NAME));
		if (m.matches()) {
			String username = m.group(1);
			String filepath = (String) msg.getHeader(Exchange.FILE_PATH);
			User user = usersController.getUser(username);

			for (String path : getLines(filepath)) {
				String realPath = path.replaceFirst("file://", ""); // Removes prefix of Flexget
				LOGGER.info("File added to queue. USER_ID={}, FILE={}", user.getId(), realPath);
				queueManager.push(user, realPath);
			}
		} else {
			throw new Exception("The file doesn't compile with the pattern");
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
