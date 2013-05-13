/*******************************************************************************
 * UsersAPI.java
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

import net.seedboxer.core.domain.User;
import net.seedboxer.web.service.DownloadsService;
import net.seedboxer.web.type.APIResponse;
import net.seedboxer.web.type.UserAPIKeyResponse;
import net.seedboxer.web.type.UserConfig;
import net.seedboxer.web.type.UserConfigsAPIResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * WebService to get the status of an upload.
 *
 * @author Jorge Davison (jdavisonc)
 *
 */
@Controller
@RequestMapping("/webservices/user")
public class UsersAPI extends SeedBoxerAPI {

	private static final Logger LOGGER = LoggerFactory.getLogger(UsersAPI.class);

	@Autowired
	private DownloadsService controller;

	@RequestMapping(value="status", method = RequestMethod.GET)
	public @ResponseBody APIResponse status() {
		try {
			return controller.getUserStatus(getUser());
		} catch (Exception e) {
			LOGGER.error("Wrong request", e);
			return APIResponse.createErrorResponse("Can not stop downloads");
		}
	}

	@RequestMapping(value="stop", method = RequestMethod.GET)
	public @ResponseBody APIResponse stop() {
		try {
			controller.stopDownloads(getUser());
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not stop download in progress", e);
			return APIResponse.createErrorResponse("Can not stop downloads");
		}
	}

	@RequestMapping(value="start", method = RequestMethod.GET)
	public @ResponseBody APIResponse start() {
		try {
			controller.startDownloads(getUser());
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not start downloads", e);
			return APIResponse.createErrorResponse("Can not start download");
		}
	}

	@RequestMapping(value="apikey", method = RequestMethod.GET)
	public @ResponseBody APIResponse apikey() {
		try {
			User user = controller.generateAPIKey(getUser());
			return new UserAPIKeyResponse(user.getApikey());
		} catch (Exception e) {
			LOGGER.error("Can not start downloads", e);
			return APIResponse.createErrorResponse("Can not start download");
		}
	}
	
	@RequestMapping(value="configs/list", method = RequestMethod.GET)
	public @ResponseBody APIResponse listConfigurations() {
		try {
			List<UserConfig> configs = controller.getUserConfigs(getUser());
			return new UserConfigsAPIResponse(configs);
		} catch (Exception e) {
			LOGGER.error("Can not list user configurations", e);
			return APIResponse.createErrorResponse("Can not list user configurations");
		}
	}
	
	@RequestMapping(value="configs/save", method = RequestMethod.GET)
	public @ResponseBody APIResponse saveConfigurations(String key, String value) {
		try {
			controller.saveUserConfigs(getUser(), key, value);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not save user configuration", e);
			return APIResponse.createErrorResponse("Can not save user configuration");
		}
	}
	
	@RequestMapping(value="configs/delete", method = RequestMethod.GET)
	public @ResponseBody APIResponse deleteConfigurations(String key) {
		try {
			controller.deleteUserConfigs(getUser(), key);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not delete user configuration", e);
			return APIResponse.createErrorResponse("Can not delete user configuration");
		}
	}

}
