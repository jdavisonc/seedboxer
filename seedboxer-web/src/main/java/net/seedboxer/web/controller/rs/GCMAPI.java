/*******************************************************************************
 * C2DMService.java
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

import net.seedboxer.core.logic.GCMController;
import net.seedboxer.web.type.api.APIResponse;
import net.seedboxer.web.type.api.GCMProjectIdAPIResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * WebService for list files
 * @author Jorge Davison (jdavisonc)
 *
 */
@Controller
@RequestMapping("/webservices/otifications/gcm")
public class GCMAPI extends SeedBoxerAPI {

	private static final Logger LOGGER = LoggerFactory.getLogger(GCMAPI.class);

	@Autowired
	private GCMController controller;

	@RequestMapping(value="registerDevice", method = RequestMethod.GET)
	public @ResponseBody APIResponse registerDevice(String registrationId) {
		try {
			controller.registerDevice(getUser(), registrationId);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Error registering device", e);
			return APIResponse.createErrorResponse("The device can not be registered");
		}
	}
	
	@RequestMapping(value="unregisterDevice", method = RequestMethod.GET)
	public @ResponseBody APIResponse unregisterDevice(String registrationId) {
		try {
			controller.unregisterDevice(getUser(), registrationId);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Error registering device", e);
			return APIResponse.createErrorResponse("The device can not be registered");
		}
	}
	
	@RequestMapping(value="projectId", method = RequestMethod.GET)
	public @ResponseBody APIResponse projectId() {
		try {
			String projectId = controller.getProjectId();
			return new GCMProjectIdAPIResponse(projectId);
		} catch (Exception e) {
			LOGGER.error("Error returning GCM project identifier", e);
			return APIResponse.createErrorResponse("The isnt a GCM project identifier configured");
		}
	}

}
