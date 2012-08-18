/*******************************************************************************
 * DownloadsService.java
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

import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.seedboxer.seedboxer.core.type.FileValue;
import com.seedboxer.seedboxer.ws.controller.DownloadsController;
import com.seedboxer.seedboxer.ws.type.Response;

/**
 * WebService for handle downloads
 * @author harley
 *
 */
@Path("/downloads")
@Component
@Scope("request")
public class DownloadsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsService.class);

	@Autowired
	private DownloadsController controller;

	@GET
	@Path("/list")
	@Produces("text/xml")
	public List<FileValue> list() {
		try {
			List<FileValue> files = controller.getCompletedFiles();
			files.addAll(controller.getInProgressFiles());
			return files;
		} catch (Exception e) {
			LOGGER.error("Can not read list of downloads", e);
			return Collections.emptyList();
		}
	}

	@GET
	@Path("/put")
	@Produces("text/xml")
	public Response put(@QueryParam("username") String username,
			@QueryParam("fileName") List<String> fileNames) {
		try {
			controller.putToDownload(username, fileNames);
			return Response.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not put to download", e);
			return Response.createErrorResponse("Can not put to download");
		}
	}

	@GET
	@Path("/delete")
	@Produces("text/xml")
	public Response delete(@QueryParam("username") String username,
			@QueryParam("downloadId") Integer downloadId) {
		try {
			controller.deleteDownloadInQueue(username, downloadId);
			return Response.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not delete download", e);
			return Response.createErrorResponse("Can not delete download from queue");
		}
	}

	@GET
	@Path("/queue")
	@Produces("text/xml")
	public List<FileValue> queue(@QueryParam("username") String username) {
		try {
			return controller.downloadsInQueue(username);
		} catch (Exception e) {
			LOGGER.error("Can not read queue", e);
			return Collections.emptyList();
		}
	}

}
