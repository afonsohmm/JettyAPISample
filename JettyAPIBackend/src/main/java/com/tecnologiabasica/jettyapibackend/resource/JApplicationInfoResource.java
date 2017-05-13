/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapibackend.resource;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author afonso
 */
@Path("/applicationinfo")
public class JApplicationInfoResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/v1/getversion/")
    public Response getVersion() {
        Response response = null;
        response = Response.status(Response.Status.OK).entity(JAppCommons.VERSION).build();
        return response;
    }

}
