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

	@Autowired
	private FeedsManager feedsManager;

	@Value(value="${rssPollPeriod}")
	private String rssPollPeriod;

	@Override
	public void configure() throws Exception {
		List<RssFeed> feeds = feedsManager.getAllFeeds();
		for(RssFeed feed : feeds){
			from(String.format("timer://%s?fixedRate=true&delay=0&period=%s",feed.getId(),rssPollPeriod))
			.to(feed.getUrl())
			.unmarshal()
			.rss()
			.split().body()
			.to("direct:mergeFeeds");
		}
	}
}
