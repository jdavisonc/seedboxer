package com.superdownloader.proeasy.service;

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

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.superdownloader.proeasy.service.controllers.DownloadsController;
import com.superdownloader.proeasy.service.types.Response;

/**
 * WebService that expose method to work with torrent files
 * @author harley
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
