/*******************************************************************************
 * IMDBResponseProcessor.java
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

package net.seedboxer.sources.thirdparty.imdb;

import java.util.ArrayList;
import java.util.List;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.Movie;
import net.seedboxer.core.domain.TvShow;
import net.seedboxer.core.type.Quality;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component("imdbResponseProcessor")
public class IMDBResponseProcessor implements Processor {

	private static final String IMDB_TVSHOW_TYPE = "TV Series";
	private static final String IMDB_MOVIE_TYPE = "Feature Film";

	private static final Logger LOGGER = LoggerFactory.getLogger(IMDBResponseProcessor.class);

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		List<List<String>> imdbResults = (List<List<String>>) msg.getBody();
		List<Content> imdbContent = new ArrayList<Content>();

		for (List<String> result : imdbResults) {
			String title = result.get(5); // 5 is the position in the CSV of the Title
			String type = result.get(6); // 6 is the position in the CSV of the Type
			Integer year = Integer.parseInt(result.get(11)); // 11 is the position in the CSV of the Year

			Content content = createContent(msg, title, year, type);
			if (content != null) {
				imdbContent.add(content);
			}
		}
		LOGGER.trace("get {} IMDB contents of {}", imdbContent.size(), msg.getHeader(Configuration.USER));
		msg.setBody(imdbContent);
	}

	private Content createContent(Message msg, String title, Integer year, String type) {
		Content content = null;
		String quality = (String) msg.getHeader(Configuration.IMDB_CONTENT_QUALITY);

		if (IMDB_TVSHOW_TYPE.equals(type)) {
			content = new TvShow(title, null, null, Quality.valueOf(quality));
		} else if (IMDB_MOVIE_TYPE.equals(type)) {
			content = new Movie(title, year, Quality.valueOf(quality));
		}
		return content;
	}

}
