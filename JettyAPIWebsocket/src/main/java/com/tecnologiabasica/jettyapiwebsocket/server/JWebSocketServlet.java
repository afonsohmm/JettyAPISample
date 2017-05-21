package com.tecnologiabasica.jettyapiwebsocket.server;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
public class JWebSocketServlet extends WebSocketServlet
{
    @Override
    public void configure(WebSocketServletFactory factory)
    {        
        factory.setCreator(JWebSocketServerCreator.getInstance());
    }
}