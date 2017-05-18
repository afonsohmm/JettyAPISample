package com.tecnologiabasica.jettyapiwebsocket.receiver;

import com.tecnologiabasica.jettyapiwebsocket.listener.IWebSocketClientListener;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

public class JWebSocketClientReceiver implements WebSocketListener {

    private IWebSocketClientListener listener = null;
    
    private Session session;
    
    private JWebSocketClientReceiver() {
        
    }
    
    public JWebSocketClientReceiver (IWebSocketClientListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        listener.onWebSocketClose(statusCode, reason, session);
        this.session = null;        
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        listener.onWebSocketConnect(this.session);        
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        listener.onWebSocketError(cause, session);        
    }

    @Override
    public void onWebSocketText(String message) {
        listener.onWebSocketMessageReceive(message, session);        
    }

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }


}
