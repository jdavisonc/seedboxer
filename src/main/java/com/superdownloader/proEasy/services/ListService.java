package com.superdownloader.proEasy.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.superdownloader.proEasy.logic.FilesController;
import com.superdownloader.proEasy.types.FileValue;

/**
 * WebService for list files
 * @author harley
 *
 */
@Path("/list")
@Component
@Scope("request")
public class ListService {

	@Autowired
	private FilesController controller;

    @GET
    @Produces("text/xml")
    public List<FileValue> list() {
        return controller.getCompletedFiles();
    }

}
