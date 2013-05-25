/*******************************************************************************
 * ContentsAPI.java
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

import net.seedboxer.web.service.ContentsService;
import net.seedboxer.web.type.ContentInfo;
import net.seedboxer.web.type.api.APIResponse;
import net.seedboxer.web.type.api.UserContentsAPIResponse;

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
@RequestMapping("/webservices/user/content")
public class ContentsAPI extends SeedBoxerAPI {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentsAPI.class);
	
	@Autowired
	private ContentsService contentsService;
	
	@RequestMapping(value="list", method = RequestMethod.GET)
	public @ResponseBody APIResponse listContents() {
		try {
			List<ContentInfo> contents = contentsService.getUserContents(getUser());
			return new UserContentsAPIResponse(contents);
		} catch (Exception e) {
			LOGGER.error("Can not list user contents", e);
			return APIResponse.createErrorResponse("Can not list user contents");
		}
	}
	
	@RequestMapping(value="save", method = RequestMethod.POST)
	public @ResponseBody APIResponse saveContents(@RequestBody ContentInfo contentInfo) {
		try {
			contentsService.saveUserContent(getUser(), contentInfo);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not save user content", e);
			return APIResponse.createErrorResponse("Can not save user content");
		}
	}
	
	@RequestMapping(value="delete", method = RequestMethod.GET)
	public @ResponseBody APIResponse deleteContents(ContentInfo contentInfo) {
		try {
			contentsService.deleteUserContent(getUser(), contentInfo);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not delete user content", e);
			return APIResponse.createErrorResponse("Can not delete user content");
		}
	}
	
}
