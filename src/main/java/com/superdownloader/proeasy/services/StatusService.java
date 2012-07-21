package com.superdownloader.proeasy.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.core.logic.DownloadSessionManager;
import com.superdownloader.proeasy.core.types.Download;

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
	private DownloadSessionManager downloadSessionManager;

	@GET
	@Produces("text/xml")
	public List<Download> status(@QueryParam("username") String username) {
		return downloadSessionManager.getUserDownloads(username);
	}

}
