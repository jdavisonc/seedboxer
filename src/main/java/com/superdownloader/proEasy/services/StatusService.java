package com.superdownloader.proEasy.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.superdownloader.proEasy.processors.UploadSessionManager;
import com.superdownloader.proEasy.types.Upload;

/**
 * WebService to get the status of an upload
 * @author harley
 *
 */
@Path("/status")
@Component
@Scope("request")
public class StatusService {

	@Autowired
	private UploadSessionManager uploadSessionManager;

    @GET
    @Produces("text/xml")
    public List<Upload> status(@QueryParam("username") String username) {
    	return uploadSessionManager.getUserUploads(username);
    }

}
