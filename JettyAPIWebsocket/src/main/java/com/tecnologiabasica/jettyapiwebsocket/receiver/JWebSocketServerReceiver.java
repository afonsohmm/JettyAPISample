package com.tecnologiabasica.jettyapiwebsocket.receiver;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

public class JWebSocketServerReceiver implements WebSocketListener {

    private Session session;

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        this.session = null;
        Logger.getLogger(JWebSocketServerReceiver.class).info("WebSocket Close: " + statusCode + " - " + reason);
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        Logger.getLogger(JWebSocketServerReceiver.class).info("WebSocket Connect: " + session);  
        sendMessage("Cliente conectado: " + session.getRemoteAddress().getHostName());
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        Logger.getLogger(JWebSocketServerReceiver.class).warn("WebSocket Error: " + cause);
    }

    @Override
    public void onWebSocketText(String message) {
        Logger.getLogger(JWebSocketServerReceiver.class).info("WebSocket receiveMessage: " + message);
    }

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    public boolean sendMessage(String message) {
        boolean returnValue = false;
        if (session != null && session.isOpen()) {
            try {
                session.getRemote().sendString(message, null);
                Logger.getLogger(JWebSocketServerReceiver.class).info("WebSocket sendMessage: " + message);
                returnValue = true;
            } catch (Exception ex) {
                Logger.getLogger(JWebSocketServerReceiver.class).error(ex);
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
