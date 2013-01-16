/*******************************************************************************
 * RouteBuilder.java
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

package net.seedboxer.seedboxer.sources.routes;

import java.util.List;

import net.seedboxer.seedboxer.core.domain.RssFeed;
import net.seedboxer.seedboxer.core.logic.FeedsManager;
import net.seedboxer.seedboxer.sources.processors.rss.filter.RssDateFilter;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * @author The-Sultan
 */
@Component
public class RouteBuilder extends SpringRouteBuilder {

	private static final String MERGE_FEEDS_ENDPOINT = "direct:mergeFeeds";
	private static final String RSS_TIMER_ENDPOINT = "rssTimer";

	@Autowired
	private FeedsManager feedsManager;

	@Override
	public void configure() throws Exception {
		List<RssFeed> feeds = feedsManager.getAllFeeds();
		for(RssFeed feed : feeds){
			configureFeedRoute(feed);
		}
	}

	private void configureFeedRoute(RssFeed feed) {
		from(RSS_TIMER_ENDPOINT)
			.to(feed.getUrl())
			.unmarshal()
			.rss()
			.split().simple("body.entries")
			.filter().method(RssDateFilter.class, "isValidEntry")
		.to(MERGE_FEEDS_ENDPOINT);
	}
}
