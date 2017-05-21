/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiwebsocket.server;

import com.tecnologiabasica.jettyapiwebsocket.listener.IWebSocketClientListener;
import com.tecnologiabasica.jettyapiwebsocket.receiver.JWebSocketClientReceiver;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 *
 * @author afonso
 */
public class JWebSocketServerCreator implements WebSocketCreator {

    private static JWebSocketServerCreator instance = null;
    private Set<JWebSocketClientReceiver> clientReceiverList = null;
    private WebSocketClientListener clientListener = null;

    public static JWebSocketServerCreator getInstance() {
        if (instance == null) {
            instance = new JWebSocketServerCreator();
        }
        return instance;
    }

    private JWebSocketServerCreator() {
        clientReceiverList = new HashSet<>();
        clientListener = new WebSocketClientListener();
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        JWebSocketClientReceiver clientReceiver = new JWebSocketClientReceiver(clientListener);        
        return clientReceiver;
    }

    private class WebSocketClientListener implements IWebSocketClientListener {

        @Override
        public void onWebSocketConnect(JWebSocketClientReceiver instance, Session session) {
            clientReceiverList.add(instance);
            Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketConnect: " + session.getRemoteAddress().getHostName() + " - list: " + clientReceiverList.size());
        }

        @Override
        public void onWebSocketClose(JWebSocketClientReceiver instance, int statusCode, String reason, Session session) {            
            clientReceiverList.remove(instance);
            Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketClose: statusCode: " + statusCode + " - reason: " + reason + " - session: " + session.getRemoteAddress().getHostName() + " - list: " + clientReceiverList.size());
        }

        @Override
        public void onWebSocketError(JWebSocketClientReceiver instance, Throwable cause, Session session) {
            Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketError: cause: " + cause.getMessage() + " - session: " + session.getRemoteAddress().getHostName());
        }

        @Override
        public void onWebSocketMessageReceive(JWebSocketClientReceiver instance, String message, Session session) {
            Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketMessageReceive: message: " + message + " - session: " + session.getRemoteAddress().getHostName());
        }

    }

}
