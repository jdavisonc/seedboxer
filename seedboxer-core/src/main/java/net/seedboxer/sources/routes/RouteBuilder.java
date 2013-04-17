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

package net.seedboxer.sources.routes;

import java.util.List;

import net.seedboxer.core.domain.RssFeed;
import net.seedboxer.core.logic.FeedsManager;
import net.seedboxer.sources.processors.rss.filter.RssDateFilter;
import net.seedboxer.sources.processors.rss.sorter.RssSortProcessor;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 *
 * @author The-Sultan
 */
@Component
public class RouteBuilder extends SpringRouteBuilder {

	@Value("${sources.mergefeeds.uri}")
	private String mergeFeedsEndoint;

	@Value("${sources.rsstimer.uri}")
	private String rssTimerEndpoint;

	@Autowired
	private FeedsManager feedsManager;

	public void setFeedsManager(FeedsManager feedsManager) {
		this.feedsManager = feedsManager;
	}

	public void setMergeFeedsEndoint(String mergeFeedsEndoint) {
		this.mergeFeedsEndoint = mergeFeedsEndoint;
	}

	public void setRssTimerEndpoint(String rssTimerEndpoint) {
		this.rssTimerEndpoint = rssTimerEndpoint;
	}

	@Override
	public void configure() throws Exception {
		List<RssFeed> feeds = feedsManager.getAllFeeds();
		for(RssFeed feed : feeds){
			configureFeedRoute(feed);
		}
	}

	private void configureFeedRoute(RssFeed feed) {
		from(rssTimerEndpoint)
			.to(feed.getUrl())
			.unmarshal()
			.rss()
			.bean(RssSortProcessor.class)
			.split().simple("body.entries")
			.filter().method(RssDateFilter.class, "isValidEntry")
		.to(mergeFeedsEndoint);
	}
}
