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

package net.seedboxer.seedboxer.sources.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.seedboxer.seedboxer.core.domain.Content;
import net.seedboxer.seedboxer.sources.type.MatchableItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * @author The-Sultan
 */
@Component
@SuppressWarnings("rawtypes")
public class ParserManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParserManager.class);

	private List<ContentParser> parsers = new ArrayList<ContentParser>();

	public List<ContentParser> getParsers() {
		return parsers;
	}

	/**
	 * Autowiring a list, means that any context bean with type
	 * ContentParser will be part of this list.
	 *
	 * @param parsers
	 */
	@Autowired
	public void setParsers(List<ContentParser> parsers) {
		this.parsers = parsers;
	}

	public List<Content> parseMatchableItems(List<MatchableItem> items){
		List<Content> parsedContentList = new ArrayList<Content>();
		for(MatchableItem item : items){
			Content parsedContent = parseItem(item);
			if (parsedContent != null) {
				parsedContentList.add(parsedContent);
			}
		}
		return parsedContentList;
	}

	private Content parseItem(MatchableItem item) {
		Iterator<ContentParser> it = parsers.iterator();
		Content parsedContent = null;
		while (it.hasNext() && parsedContent == null) {
			parsedContent = it.next().parse(item);
		}
		return parsedContent;
	}


}
