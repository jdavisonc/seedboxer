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
package com.superdownloader.proeasy.ws.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.logic.UsersController;
import com.superdownloader.proeasy.ws.type.Response;

/**
 * WebService for list files
 * @author harley
 *
 */
@Path("/registerDevice")
@Component
@Scope("request")
public class C2DMService {

	private static final Logger LOGGER = LoggerFactory.getLogger(C2DMService.class);

	@Autowired
	private UsersController controller;

	@GET
	@Produces("text/xml")
	public Response registerDevice(@QueryParam("username") String username,
			@QueryParam("registrationId") String registrationId,
			@QueryParam("deviceId") String deviceId) {
		try {
			controller.registerDevice(username, registrationId, deviceId);
			return Response.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Error registering device", e);
			return Response.createErrorResponse("The device can not be registered");
		}
	}

}
