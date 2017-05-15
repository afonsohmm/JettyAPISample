/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapibackend.resource;

import com.tecnologiabasica.jettyapibackend.business.JUserInfoBusiness;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import com.tecnologiabasica.jettyapicommons.util.JUtil;
import com.tecnologiabasica.jettyapidatabase.dao.JUserInfoDAO;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

/**
 *
 * @author afonso
 */
@Path("/userinfo")
public class JUserInfoResource {

    @Path("/v1/createUser/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createUser(@Context HttpServletRequest request, JUserInfoEntity entity, @PathParam("email") String email) {
        String remoteIP = request.getRemoteAddr();
        Response response = null;

        if (!JUtil.validateEmail(entity.getEmail())) {
            response = Response.status(Response.Status.BAD_REQUEST).entity(JUtil.buildJsonOutputMessage("Email inválido.")).build();
        } else {
            JUserInfoEntity userFound = JUserInfoDAO.getInstance().getUser(entity.getEmail());
            if (userFound != null) {
                response = Response.status(Response.Status.CONFLICT).entity(JUtil.buildJsonOutputMessage("Email em uso.")).build();
            }
        }

        if (response == null) {
            long id = JUserInfoDAO.getInstance().insert(entity);
            if (id != -1) {
                entity.setId(id);
                entity.setRemoteId(id);
                response = Response.status(Response.Status.CREATED).entity(JUserInfoBusiness.getOutputJSonUserInfoEntity(entity)).build();
                JUserInfoBusiness.getInstance().createUser(entity);
            } else {
                response = Response.status(Response.Status.NOT_FOUND).entity(JUserInfoBusiness.getOutputJSonUserInfoEntity(entity)).build();
            }

        }
        if (response != null) {
            Logger.getLogger(JUserInfoResource.class).info(request.getPathInfo() + " - remoteIP: " + remoteIP + " - " + response.getStatus());
        }
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/v1/getUserList")
    public Response getUserList(@Context HttpServletRequest request, @QueryParam("domainId") String domainId, @QueryParam("groupId") String groupId) {
        String remoteIP = request.getRemoteAddr();
        Response response = null;

        LinkedList<JUserInfoEntity> list = JUserInfoDAO.getInstance().getUserList(domainId, groupId);

        if (list != null) {
            response = Response.status(Response.Status.OK).entity(JUserInfoBusiness.getOutputJSonListUserInfo(list)).build();
        } else {
            list = new LinkedList<>();
            response = Response.status(Response.Status.NO_CONTENT).entity(JUserInfoBusiness.getOutputJSonListUserInfo(list)).build();
        }
        if (response != null) {
            Logger.getLogger(JUserInfoResource.class).info(request.getPathInfo() + " - remoteIP: " + remoteIP + " - " + response.getStatus());
        }
        return response;
    }

}
