/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiwebsocket.client;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import com.tecnologiabasica.jettyapiwebsocket.receiver.JWebSocketClientReceiver;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import com.tecnologiabasica.jettyapiwebsocket.listener.IWebSocketListener;

/**
 *
 * @author afonso
 */
public class JWebSocketClient {

    private WebSocketClient webSocketClient = null;
    private Session session = null;
    private URI uri = null;
    private String endPoint = "events/";
    private JWebSocketClientReceiver webSocketReceiver = null;

    public JWebSocketClient() {
        webSocketClient = new WebSocketClient();
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        if (endPoint.contains("/") == false) {
            endPoint = endPoint + "/";
        }
        this.endPoint = endPoint;
    }

    public void start(IWebSocketListener listener) {
        try {
            uri = URI.create(JAppCommons.URL_WS + endPoint);
            webSocketClient.start();
            webSocketReceiver = new JWebSocketClientReceiver(listener);
            Future<Session> future = webSocketClient.connect(webSocketReceiver, uri);
            session = future.get();
        } catch (Exception ex) {
            Logger.getLogger(JWebSocketClient.class).error(ex);
        }
    }

    public void stop() {
        if (session != null && session.isOpen()) {
            session.close();
        }
        if (webSocketClient != null && webSocketClient.isStarted()) {
            try {
                webSocketClient.stop();
            } catch (Exception ex) {
                Logger.getLogger(JWebSocketClient.class).error(ex);
            }
        }
    }

    public boolean sendMessage(String message) {
        boolean returnValue = false;
        if (session != null && session.isOpen()) {
            try {
                session.getRemote().sendString(message);
                returnValue = true;
            } catch (IOException ex) {
                Logger.getLogger(JWebSocketClient.class).error(ex);
            }
        }
        return returnValue;
    }

    public boolean isOpen() {
        boolean returnValue = false;
        if (session != null && session.isOpen()) {
            returnValue = true;
        } else {
            returnValue = false;
        }
        return returnValue;
    }

}
