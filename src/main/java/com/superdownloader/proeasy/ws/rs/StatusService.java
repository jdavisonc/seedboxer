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
package com.superdownloader.proeasy.ws.rs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.type.Download;
import com.superdownloader.proeasy.ws.controller.DownloadsController;

/**
 * WebService to get the status of an upload
 * @author harley
 *
 */
@Path("/status")
@Component
@Scope("request")
public class StatusService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

	@Autowired
	private DownloadsController controller;

	@GET
	@Produces("text/xml")
	public List<Download> status(@QueryParam("username") String username) {
		try {
			return controller.getUserDownloads(username);
		} catch (Exception e) {
			LOGGER.error("Wrong request", e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

}
