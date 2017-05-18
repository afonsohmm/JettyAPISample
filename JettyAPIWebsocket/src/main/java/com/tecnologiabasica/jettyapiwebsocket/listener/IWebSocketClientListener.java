/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiwebsocket.listener;

import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author afonso
 */
public interface IWebSocketClientListener {

    public void onWebSocketConnect(Session session);

    public void onWebSocketClose(int statusCode, String reason, Session session);

    public void onWebSocketError(Throwable cause, Session session);

    public void onWebSocketMessageReceive(String message, Session session);
}
