/*******************************************************************************
 * DownloadHistory.java
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
package net.seedboxer.mule.processor;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.logic.ContentManager;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class DownloadHistory implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadHistory.class);

	private ContentManager contentManager;

	@Autowired
	public void setContentManager(ContentManager contentManager) {
		this.contentManager = contentManager;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();

		User user = msg.getHeader(Configuration.USER, User.class);
		Content content = msg.getHeader(Configuration.CONTENT, Content.class);
		if (content != null) {
			contentManager.saveInHistory(content, user);
		} else {
			LOGGER.error("Is not a content or can't be parsed. File: {}", msg.getHeader(Configuration.FILE_NAME, String.class));
		}
	}

}
