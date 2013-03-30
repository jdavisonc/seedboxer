/*******************************************************************************
 * DownloadsAPI.java
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

import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.seedboxer.seedboxer.core.type.FileValue;
import net.seedboxer.seedboxer.ws.controller.DownloadsController;
import net.seedboxer.seedboxer.ws.type.APIResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * WebService for handle downloads
 * @author Jorge Davison (jdavisonc)
 *
 */
@Path("/downloads")
@Component
@Scope("request")
public class DownloadsAPI extends SeedBoxerAPI {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsAPI.class);

	@Autowired
	private DownloadsController controller;

	@GET
	@Path("/list")
	@Produces("application/json")
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
	@Produces("application/json")
	public APIResponse put(@QueryParam("fileName") List<String> fileNames) {
		try {
			controller.putToDownload(getUser(), fileNames, true);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not put to download", e);
			return APIResponse.createErrorResponse("Can not put to download");
		}
	}

	@GET
	@Path("/delete")
	@Produces("application/json")
	public APIResponse delete(@QueryParam("downloadId") Integer downloadId) {
		try {
			controller.deleteDownloadInQueue(getUser(), downloadId);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not delete download", e);
			return APIResponse.createErrorResponse("Can not delete download from queue");
		}
	}

	@GET
	@Path("/queue")
	@Produces("application/json")
	public List<FileValue> queue() {
		try {
			return controller.downloadsInQueue(getUser());
		} catch (Exception e) {
			LOGGER.error("Can not read queue", e);
			return Collections.emptyList();
		}
	}


	@POST
	@Path("/update")
	@Consumes("application/json")
	@Produces("application/json")
	public APIResponse update(List<FileValue> queueItems) {
		try {
			controller.updateQueue(getUser(), queueItems);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not update queue", e);
			return APIResponse.createErrorResponse("Can not update queue");
		}
	}

}
