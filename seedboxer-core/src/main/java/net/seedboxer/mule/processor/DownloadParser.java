/*******************************************************************************
 * DownloadParser.java
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
import net.seedboxer.sources.parser.ParserManager;
import net.seedboxer.sources.type.MatchableItem;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class DownloadParser implements Processor {
	
	@Autowired
	private ParserManager parserManager;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		String filename = msg.getHeader(Configuration.FILE_NAME, String.class);
		
		MatchableItem item = getMachableItem(filename);
		
		Content matchedContent = parserManager.parseMatchableItem(item);
		msg.setHeader(Configuration.CONTENT, matchedContent);
	}

	private MatchableItem getMachableItem(String filename) {
		return new MatchableItem(filename);
	}

}
