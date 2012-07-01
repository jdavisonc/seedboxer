package com.superdownloader.proEasy.services;

import java.io.File;
import java.io.IOException;
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

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.superdownloader.proEasy.logic.FilesController;
import com.superdownloader.proEasy.types.Response;

/**
 * WebService that expose method to work with torrent files
 * @author harley
 *
 */
@Path("/torrents")
@Component
@Scope("request")
public class TorrentsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsService.class);

	@Autowired
	private FilesController controller;

	@POST
	@Path("/add")
	@Produces("text/xml")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addTorrent(@FormDataParam("file") final InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		try {

			String uploadedFileLocation = "d://uploaded/" + fileDetail.getFileName();

			// save it
			writeToFile(uploadedInputStream, uploadedFileLocation);

			String output = "File uploaded to : " + uploadedFileLocation;

			Files.copy(new InputSupplier<InputStream>() {
				@Override
				public InputStream getInput() throws IOException {
					return uploadedInputStream;
				}
			}, new File(uploadedFileLocation));

			return Response.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not read list of downloads", e);
			return Response.createErrorResponse("Can not put to download");
		}
	}

}
