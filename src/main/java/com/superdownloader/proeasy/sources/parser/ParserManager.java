/*******************************************************************************
 * ParserManager.java
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

package com.superdownloader.proeasy.sources.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.sources.domain.Content;
import com.superdownloader.proeasy.sources.type.MatchableItem;

/**
 *
 * @author Farid
 */
@Component
public class ParserManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParserManager.class);
	private List<ContentParser> parsers = new ArrayList<ContentParser>();

	public List<ContentParser> getParsers() {
		return parsers;
	}

	@Autowired
	public void setParsers(List<ContentParser> parsers) {
		this.parsers = parsers;
	}

	public List<Content> parseMatchableItems(List<MatchableItem> items){
		List<Content> parsedContentList = new ArrayList<Content>();
		for(MatchableItem item : items){
			int count = parsedContentList.size();
			for(ContentParser parser : parsers){
				Content parsedContent = parser.parse(item);
				if(parsedContent != null){
					parsedContentList.add(parsedContent);
					//LOGGER.info("P:"+item.getTitle()+"-->"+parsedContent.getName()+parsedContent.getClass().getSimpleName());
					break;
				}

			}
			//if(count == parsedContentList.size())
			//LOGGER.info("NP:"+item.getTitle());
		}
		return parsedContentList;
	}


}
