package com.superdownloader.proEasy.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/list")
@Component
@Scope("request")
public class ListService {

    @GET
    @Produces("text/plain")
    public String getIt() {
        return "Hi there!";
    }

}
