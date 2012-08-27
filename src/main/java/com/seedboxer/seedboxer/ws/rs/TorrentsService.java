/*******************************************************************************
 * TorrentsService.java
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
package com.seedboxer.seedboxer.ws.rs;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.seedboxer.seedboxer.ws.controller.DownloadsController;
import com.seedboxer.seedboxer.ws.type.Response;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 * WebService that expose method to work with torrent files
 * @author Jorge Davison (jdavisonc)
 *
 */
@Path("/torrents")
@Component
@Scope("request")
public class TorrentsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TorrentsService.class);

	@Autowired
	private DownloadsController controller;

	@POST
	@Path("/add")
	@Produces("text/xml")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addTorrent(@FormDataParam("file") final InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("username") String username) {
		try {
			if (fileDetail.getFileName().endsWith(".torrent")) {
				controller.addTorrent(username, fileDetail.getFileName(), uploadedInputStream);
				return Response.createSuccessfulResponse();
			} else {
				return Response.createErrorResponse("Wrong file type, only accept .torrent files");
			}
		} catch (Exception e) {
			LOGGER.error("Can not read list of downloads", e);
			return Response.createErrorResponse("Can not put to download");
		}
	}

}
