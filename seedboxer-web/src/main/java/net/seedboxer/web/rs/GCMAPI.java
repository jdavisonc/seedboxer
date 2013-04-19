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
package net.seedboxer.web.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.seedboxer.core.logic.GCMController;
import net.seedboxer.web.type.APIResponse;
import net.seedboxer.web.type.GCMProjectIdResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * WebService for list files
 * @author Jorge Davison (jdavisonc)
 *
 */
@Path("/notifications/gcm")
@Component
@Scope("request")
public class GCMAPI extends SeedBoxerAPI {

	private static final Logger LOGGER = LoggerFactory.getLogger(GCMAPI.class);

	@Autowired
	private GCMController controller;

	@GET
	@Path("/registerDevice")
	@Produces("application/json")
	public APIResponse registerDevice(@QueryParam("registrationId") String registrationId) {
		try {
			controller.registerDevice(getUser(), registrationId);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Error registering device", e);
			return APIResponse.createErrorResponse("The device can not be registered");
		}
	}
	
	@GET
	@Path("/unregisterDevice")
	@Produces("application/json")
	public APIResponse unregisterDevice(@QueryParam("registrationId") String registrationId) {
		try {
			controller.unregisterDevice(getUser(), registrationId);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Error registering device", e);
			return APIResponse.createErrorResponse("The device can not be registered");
		}
	}
	
	@GET
	@Path("/projectId")
	@Produces("application/json")
	public APIResponse projectId() {
		try {
			String projectId = controller.getProjectId();
			return new GCMProjectIdResponse(projectId);
		} catch (Exception e) {
			LOGGER.error("Error returning GCM project identifier", e);
			return APIResponse.createErrorResponse("The isnt a GCM project identifier configured");
		}
	}

}
