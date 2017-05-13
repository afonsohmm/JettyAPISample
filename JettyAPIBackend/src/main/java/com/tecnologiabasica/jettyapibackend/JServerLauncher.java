/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapibackend;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import com.tecnologiabasica.jettyapicommons.util.JLoggerUtil;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 *
 * @author afonso
 */
public class JServerLauncher {

    public static void main(String[] args) {
        int port = 8081;
        int portSSL = 8080;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        if (args.length >= 2) {
            portSSL = Integer.parseInt(args[1]);
        }
        
        JLoggerUtil.getInstance().start(JAppCommons.getHomeDir(), "JettyAPIBackend");

        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);

        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(JServerLauncher.class.getResource(
                "/keystore.jks").toExternalForm());
        sslContextFactory.setKeyStorePassword("123456");
        sslContextFactory.setKeyManagerPassword("123456");
        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https));
        sslConnector.setPort(portSSL);

        server.setConnectors(new Connector[]{connector, sslConnector});

        ServletHolder servletHolder = new ServletHolder(org.glassfish.jersey.servlet.ServletContainer.class);
        servletHolder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        servletHolder.setInitParameter("jersey.config.server.provider.packages", "com.tecnologiabasica.jettyapibackend.resource, com.tecnologiabasica.jettyapibackend.filter");//Set the package where the services reside
        servletHolder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        servletHolder.setInitParameter("jersey.config.server.provider.classnames", "org.glassfish.jersey.media.multipart.MultiPartFeature");
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(contextHandler);
        contextHandler.addServlet(servletHolder, "/api/*");

        try {
            server.start();
            //JMainController.getInstance();
            server.join();
        } catch (Exception ex) {            
            Logger.getLogger(JServerLauncher.class.getName()).error(ex);
            System.exit(1);
        }
    }

}
