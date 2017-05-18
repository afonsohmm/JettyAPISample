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

/**
 *
 * @author afonso
 */
public class JWebSocketClient {

    private static JWebSocketClient instance = null;
    private WebSocketClient webSocketClient = null;
    private Session session = null;
    private URI uri = null;

    public static JWebSocketClient getInstance() {
        if (instance == null) {
            instance = new JWebSocketClient();
        }
        return instance;
    }

    private JWebSocketClient() {
        uri = URI.create(JAppCommons.URL_WS + "events/");
        webSocketClient = new WebSocketClient();
    }

    public void start() {
        try {
            webSocketClient.start();
            JWebSocketClientReceiver webSocketListener = new JWebSocketClientReceiver();
            Future<Session> future = webSocketClient.connect(webSocketListener, uri);
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

    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getRemote().sendString(message);
            } catch (IOException ex) {
                Logger.getLogger(JWebSocketClient.class).error(ex);
            }
        }
    }

}
