/*******************************************************************************
 * ParserProcessor.java
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

package com.seedboxer.seedboxer.sources.processors;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seedboxer.seedboxer.core.domain.Content;
import com.seedboxer.seedboxer.sources.parser.ParserManager;
import com.seedboxer.seedboxer.sources.type.MatchableItem;


/**
 *
 * @author The-Sultan
 */
@Component
public class ParserProcessor implements Processor{

	@Autowired
	ParserManager parserManager;

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		List<MatchableItem> items = (List<MatchableItem>) msg.getBody();
		List<Content> macheableItems = parserManager.parseMatchableItems(items);
		exchange.getOut().setBody(macheableItems);
	}
}
