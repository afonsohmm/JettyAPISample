/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiwebsocket.server;

import com.tecnologiabasica.jettyapiwebsocket.receiver.JWebSocketClientReceiver;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import com.tecnologiabasica.jettyapiwebsocket.listener.IWebSocketListener;

/**
 *
 * @author afonso
 */
public class JWebSocketServerCreator implements WebSocketCreator {

    private static JWebSocketServerCreator instance = null;
    private Set<JWebSocketClientReceiver> clientReceiverList = null;
    private WebSocketClientListener clientListener = null;
    private IWebSocketListener serverListener = null;

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

    public void setServerListener(IWebSocketListener serverListener) {
        this.serverListener = serverListener;
    }   

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        JWebSocketClientReceiver clientReceiver = new JWebSocketClientReceiver(clientListener);
        return clientReceiver;
    }

    public boolean sendMessage(JWebSocketClientReceiver instance, String message) {
        boolean returnValue = false;
        if (clientReceiverList.contains(instance)) {
            if (instance.getSession() != null && instance.getSession().isOpen()) {
                try {
                    instance.getSession().getRemote().sendString(message);
                    returnValue = true;
                } catch (IOException ex) {
                    Logger.getLogger(JWebSocketServerCreator.class).error(ex);
                }
            }
        }
        return returnValue;
    }
    
    public boolean closeWebSocket(JWebSocketClientReceiver instance) {
        boolean returnValue = false;
        if (clientReceiverList.contains(instance)) {
            if (instance.getSession() != null && instance.getSession().isOpen()) {
                try {
                    instance.getSession().close();                    
                    returnValue = true;
                } catch (Exception ex) {
                    Logger.getLogger(JWebSocketServerCreator.class).error(ex);
                }
            }
        }
        return returnValue;
    }

    public boolean sendMessageForAll(String message) {
        boolean returnValue = false;
        for (JWebSocketClientReceiver next : clientReceiverList) {
            if (next.getSession() != null && next.getSession().isOpen()) {
                try {
                    next.getSession().getRemote().sendString(message);
                    returnValue = true;
                } catch (IOException ex) {
                    Logger.getLogger(JWebSocketServerCreator.class).error(ex);
                }
            }
        }
        return returnValue;
    }

    public boolean isOpen(JWebSocketClientReceiver instance) {
        boolean returnValue = false;
        if (clientReceiverList.contains(instance)) {
            if (instance.getSession() != null && instance.getSession().isOpen()) {
                returnValue = true;
            } else {
                returnValue = false;
            }
        }
        return returnValue;
    }

    private class WebSocketClientListener implements IWebSocketListener {

        @Override
        public void onWebSocketConnect(JWebSocketClientReceiver instance, Session session) {
            clientReceiverList.add(instance);
            if (serverListener != null) {
                serverListener.onWebSocketConnect(instance, session);
            }
            //Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketConnect: " + session.getRemoteAddress().getHostName() + " - list: " + clientReceiverList.size());
        }

        @Override
        public void onWebSocketClose(JWebSocketClientReceiver instance, int statusCode, String reason, Session session) {
            if (serverListener != null) {
                serverListener.onWebSocketClose(instance, statusCode, reason, session);
            }
            clientReceiverList.remove(instance);
            //Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketClose: statusCode: " + statusCode + " - reason: " + reason + " - session: " + session.getRemoteAddress().getHostName() + " - list: " + clientReceiverList.size());
        }

        @Override
        public void onWebSocketError(JWebSocketClientReceiver instance, Throwable cause, Session session) {
            if (serverListener != null) {
                serverListener.onWebSocketError(instance, cause, session);
            }
            //Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketError: cause: " + cause.getMessage() + " - session: " + session.getRemoteAddress().getHostName());
        }

        @Override
        public void onWebSocketMessageReceive(JWebSocketClientReceiver instance, String message, Session session) {
            if (serverListener != null) {
                serverListener.onWebSocketMessageReceive(instance, message, session);
            }
            //Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketMessageReceive: message: " + message + " - session: " + session.getRemoteAddress().getHostName());
        }

    }

}
