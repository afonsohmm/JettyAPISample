package com.tecnologiabasica.jettyapiwebsocket.receiver;

import java.util.Objects;
import java.util.Random;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import com.tecnologiabasica.jettyapiwebsocket.listener.IWebSocketListener;

public class JWebSocketClientReceiver implements WebSocketListener {

    private IWebSocketListener listener = null;

    private Session session;

    private long handle = -1;

    private JWebSocketClientReceiver() {
        Random rand = null;
        handle = rand.nextLong();
    }

    public Session getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.session);
        hash = 23 * hash + (int) (this.handle ^ (this.handle >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JWebSocketClientReceiver other = (JWebSocketClientReceiver) obj;
        if (this.handle != other.handle) {
            return false;
        }
        if (!Objects.equals(this.session, other.session)) {
            return false;
        }
        return true;
    }

    public JWebSocketClientReceiver(IWebSocketListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        listener.onWebSocketClose(this, statusCode, reason, session);
        this.session = null;
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        listener.onWebSocketConnect(this, this.session);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        listener.onWebSocketError(this, cause, session);
    }

    @Override
    public void onWebSocketText(String message) {
        listener.onWebSocketMessageReceive(this, message, session);
    }

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

}
