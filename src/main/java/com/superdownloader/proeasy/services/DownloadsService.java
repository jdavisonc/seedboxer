package com.superdownloader.proeasy.services;

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

import com.superdownloader.proeasy.core.logic.DownloadsController;
import com.superdownloader.proeasy.core.types.FileValue;
import com.superdownloader.proeasy.core.types.Response;

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
			controller.putToDownload(username, fileNames, true);
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
			@QueryParam("fileName") String fileNames) {
		try {
			if (controller.deleteDownloadInQueue(username, fileNames)) {
				return Response.createSuccessfulResponse();
			} else {
				return Response.createErrorResponse("Can not delete download from queue");
			}
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
