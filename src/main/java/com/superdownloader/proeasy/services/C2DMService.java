package com.superdownloader.proeasy.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.logic.UsersController;
import com.superdownloader.proeasy.types.Response;

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
