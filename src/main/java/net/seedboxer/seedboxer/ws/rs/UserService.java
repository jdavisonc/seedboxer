/*******************************************************************************
 * StatusService.java
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
package net.seedboxer.seedboxer.ws.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import net.seedboxer.seedboxer.core.domain.User;
import net.seedboxer.seedboxer.ws.controller.DownloadsController;
import net.seedboxer.seedboxer.ws.type.Response;
import net.seedboxer.seedboxer.ws.type.UserAPIKeyResponse;
import net.seedboxer.seedboxer.ws.type.UserStatusResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * WebService to get the status of an upload.
 *
 * @author Jorge Davison (jdavisonc)
 *
 */
@Path("/")
@Component
@Scope("request")
public class UserService extends SeedBoxerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private DownloadsController controller;

	@GET
	@Path("/status")
	@Produces({"application/xml", "application/json"})
	public Response status() {
		try {
			return controller.getUserStatus(getUser());
		} catch (Exception e) {
			LOGGER.error("Wrong request", e);
			return Response.createErrorResponse("Can not stop downloads");
		}
	}

	@GET
	@Path("/stop")
	@Produces({"application/xml", "application/json"})
	public Response stop() {
		try {
			controller.stopDownloads(getUser());
			return Response.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not stop download in progress", e);
			return Response.createErrorResponse("Can not stop downloads");
		}
	}

	@GET
	@Path("/start")
	@Produces({"application/xml", "application/json"})
	public Response start() {
		try {
			controller.startDownloads(getUser());
			return Response.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not start downloads", e);
			return Response.createErrorResponse("Can not start download");
		}
	}

	@GET
	@Path("/apikey")
	@Produces({"application/xml", "application/json"})
	public Response apikey() {
		try {
			User user = controller.generateAPIKey(getUser());
			return new UserAPIKeyResponse(user.getApiKey());
		} catch (Exception e) {
			LOGGER.error("Can not start downloads", e);
			return Response.createErrorResponse("Can not start download");
		}
	}

}
