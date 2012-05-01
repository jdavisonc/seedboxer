package com.superdownloader.proEasy.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.superdownloader.proEasy.logic.UsersController;

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
	public String registerDevice(String username, String registrationId, String deviceId) {
		try {
			controller.registerDevice(username, registrationId, deviceId);
			return "<response>OK</response>";
		} catch (Exception e) {
			LOGGER.error("Error registering device", e);
			return "<response>Error</response>";
		}
	}

}
