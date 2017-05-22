/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiwebsocket.listener;

import com.tecnologiabasica.jettyapiwebsocket.receiver.JWebSocketClientReceiver;
import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author afonso
 */
public interface IWebSocketListener {

    public void onWebSocketConnect(JWebSocketClientReceiver instance,Session session);

    public void onWebSocketClose(JWebSocketClientReceiver instance,int statusCode, String reason, Session session);

    public void onWebSocketError(JWebSocketClientReceiver instance,Throwable cause, Session session);

    public void onWebSocketMessageReceive(JWebSocketClientReceiver instance, String message, Session session);
}
