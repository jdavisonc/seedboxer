/*******************************************************************************
 * RssFeedsAPI.java
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
package net.seedboxer.web.controller.rs;

import java.util.List;

import net.seedboxer.web.service.AdminService;
import net.seedboxer.web.type.api.APIResponse;
import net.seedboxer.web.type.api.RssFeedsAPIResponse;
import net.seedboxer.web.type.dto.RssFeedInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Controller
@RequestMapping("/webservices/admin/rss")
public class RssFeedsAPI extends SeedBoxerAPI  {

	private static final Logger LOGGER = LoggerFactory.getLogger(RssFeedsAPI.class);
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping(value="list", method = RequestMethod.GET)
	public @ResponseBody APIResponse listRssFeeds() {
		try {
			List<RssFeedInfo> allRSSFeeds = adminService.getAllRSSFeeds();
			return new RssFeedsAPIResponse(allRSSFeeds);
		} catch (Exception e) {
			LOGGER.error("Can not list rss feeds", e);
			return APIResponse.createErrorResponse("Can not list rss feeds");
		}
	}
	
	@RequestMapping(value="save", method = RequestMethod.POST)
	public @ResponseBody APIResponse saveRssFeed(@RequestBody RssFeedInfo feed) {
		try {
			adminService.saveRssFeed(feed);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not save rss feed", e);
			return APIResponse.createErrorResponse("Can not save rss feed");
		}
	}
	
	@RequestMapping(value="delete", method = RequestMethod.DELETE)
	public @ResponseBody APIResponse deleteRssFeed(@RequestBody RssFeedInfo feed) {
		try {
			adminService.deleteRssFeed(feed);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not delete rss feed", e);
			return APIResponse.createErrorResponse("Can not delete rss feed");
		}
	}
	
}
