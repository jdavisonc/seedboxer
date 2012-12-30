/*******************************************************************************
 * TraktProcessor.java
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

package com.seedboxer.seedboxer.sources.thirdparty.trakt;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.TvShow;
import com.seedboxer.seedboxer.core.domain.Configuration;
import com.seedboxer.seedboxer.core.domain.Content;
import com.seedboxer.seedboxer.core.type.Quality;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component("traktProcessor")
public class TraktProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraktProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();

		String username = (String) msg.getHeader(Configuration.TRAKT_USERNAME);
		String password = (String) msg.getHeader(Configuration.TRAKT_PASSWORD);
		String authKey = (String) msg.getHeader(Configuration.TRAKT_AUTH_KEY);
		String quality = (String) msg.getHeader(Configuration.TRAKT_CONTENT_QUALITY);


		List<Content> traktContent = new ArrayList<Content>();
		ServiceManager manager = new ServiceManager();
		manager.setUseSsl(true);
		manager.setApiKey(authKey);
		manager.setAuthentication(username, password);

		traktContent.addAll(getWatchlist(manager, username, quality));

		LOGGER.debug("get {} Trakt contents of {}", traktContent.size(), msg.getHeader(Configuration.USER));
		msg.setBody(traktContent);
	}

	private List<Content> getWatchlist(ServiceManager manager, String username, String quality) {
		List<Content> watchlistContent = new ArrayList<Content>();

		List<TvShow> watchlistShows = manager.userService().watchlistShows(username).fire();
		for (TvShow tvShow : watchlistShows) {
			watchlistContent.add(createTvShow(tvShow, quality));
		}
		return watchlistContent;
	}

	private com.seedboxer.seedboxer.core.domain.TvShow createTvShow(TvShow tvShow, String quality) {
		return new com.seedboxer.seedboxer.core.domain.TvShow(tvShow.title, null, null, Quality.valueOf(quality));
	}

}
