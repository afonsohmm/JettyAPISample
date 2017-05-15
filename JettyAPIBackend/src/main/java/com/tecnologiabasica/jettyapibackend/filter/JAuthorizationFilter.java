package com.tecnologiabasica.jettyapibackend.filter;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;
import org.json.JSONObject;


/**
 *
 * @author afonso
 */
@Provider
public class JAuthorizationFilter implements ContainerRequestFilter {

    //Mensagem e response para token vazio
    private static final String EMPTY_TOKEN_MESSAGE = new JSONObject().put("message", "Not found").toString();

    //Mensagem e response para token invalido
    private static final String INVALID_TOKEN_MESSAGE = new JSONObject().put("message", "Not acceptable").toString();    

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {        
        Response.ResponseBuilder baseResponse = Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON);
        Method method = resourceInfo.getResourceMethod();        
        if (!method.isAnnotationPresent(PermitAll.class)) {
            String token = requestContext.getHeaderString("Authorization");

            if (token == null || token.isEmpty()) {
                Response response = baseResponse.entity(EMPTY_TOKEN_MESSAGE).build();
                Logger.getLogger(JAuthorizationFilter.class).info(request.getRemoteAddr() + " - " + requestContext.getMethod() + " - " + requestContext.getUriInfo().getPath() + " - Response: UNAUTHORIZED - Authorization: " + token);
                requestContext.abortWith(response);

            } else if (!token.equals(JAppCommons.API_TOKEN)) {
                Response response = baseResponse.entity(INVALID_TOKEN_MESSAGE).build();
                Logger.getLogger(JAuthorizationFilter.class).info(request.getRemoteAddr() + " - " + requestContext.getMethod() + " - " + requestContext.getUriInfo().getPath() + " - Response: UNAUTHORIZED - Authorization: " + token);
                requestContext.abortWith(response);
            }
        }
    }

}
