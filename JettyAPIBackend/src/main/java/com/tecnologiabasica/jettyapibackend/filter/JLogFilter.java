package com.tecnologiabasica.jettyapibackend.filter;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;

/**
 * @author afonso
 */
@Provider
public class JLogFilter implements ContainerResponseFilter {

    private @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        if (JAppCommons.API_DEBUG == true) {
            final UriInfo uriInfo = requestContext.getUriInfo();
            String remoteIP = request.getRemoteAddr();
            String log = "method: " + requestContext.getMethod();
            log += " - url: /" + uriInfo.getPath();
            log += " - remoteAddress: " + remoteIP;
            log += " - status: " + responseContext.getStatus();

            Logger.getLogger(JLogFilter.class).info(log);
        }

    }

}
