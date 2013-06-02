/*******************************************************************************
 * AdminService.java
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
package net.seedboxer.web.service;

import java.util.List;

import javax.annotation.Nullable;

import net.seedboxer.core.domain.RssFeed;
import net.seedboxer.core.logic.FeedsManager;
import net.seedboxer.web.type.dto.RssFeedInfo;
import net.seedboxer.web.utils.mapper.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class AdminService {

	@Autowired
	private FeedsManager feedsManager;
	
	@Autowired
	private Mapper mapper;
	
	public List<RssFeedInfo> getAllRSSFeeds() {
		return Lists.transform(feedsManager.getAllFeeds(), new Function<RssFeed, RssFeedInfo>() {

			@Override
			@Nullable
			public RssFeedInfo apply(@Nullable RssFeed rss) {
				return (RssFeedInfo) mapper.map(rss);
			}
		});
	}
	
	public void saveRssFeed(RssFeedInfo rssFeed) {
		feedsManager.save((RssFeed) mapper.map(rssFeed));
	}
	
	public void deleteRssFeed(RssFeedInfo rssFeed) {
		feedsManager.delete((RssFeed) mapper.map(rssFeed));
	}
	
}
