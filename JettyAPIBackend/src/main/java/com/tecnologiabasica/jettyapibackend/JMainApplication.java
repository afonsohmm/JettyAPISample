/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapibackend;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import com.tecnologiabasica.jettyapicommons.enums.EDatabaseType;
import com.tecnologiabasica.jettyapicommons.util.JThreadFactoryBuilder;
import com.tecnologiabasica.jettyapidatabase.JDatabaseConnector;
import com.tecnologiabasica.jettyapiwebsocket.listener.IWebSocketListener;
import com.tecnologiabasica.jettyapiwebsocket.receiver.JWebSocketClientReceiver;
import com.tecnologiabasica.jettyapiwebsocket.server.JWebSocketServerCreator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author afonso
 */
public class JMainApplication implements Runnable {

    private static JMainApplication instance = null;
    private ScheduledExecutorService scheduler = null;
    private ScheduledFuture<?> scheduleHandler = null;
    private String dataBaseName = "databasesample";
    private WebSocketServerListener webSocketListener = null;

    public static JMainApplication getInstance() {
        if (instance == null) {
            instance = new JMainApplication();
        }
        return instance;
    }

    private JMainApplication() {
        //Como o aplicativo sempre é executado via terminal, esse log registra a versão do aplicativo e a versão da máquina virtual JAVA utilizada
        Logger.getLogger(JMainApplication.class).info("Aplicação iniciada - Versão " + JAppCommons.VERSION + " - JVM: " + System.getProperty("java.version"));

        if (JDatabaseConnector.getInstance().open(JAppCommons.getHomeDir(), dataBaseName, EDatabaseType.H2DB) != -1) {

            ThreadFactory customThreadfactory = new JThreadFactoryBuilder()
                    .setNamePrefix("thread_JMainApplication_scheduler").build();

            scheduler = Executors.newScheduledThreadPool(1, customThreadfactory);
            scheduleHandler = scheduler.schedule(this, 1, TimeUnit.SECONDS);
            
            webSocketListener = new WebSocketServerListener();
            JWebSocketServerCreator.getInstance().setServerListener(webSocketListener);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    Logger.getLogger(JMainApplication.class).info("Aplicação finalizada - Versão " + JAppCommons.VERSION);
                }
            });

        } else {
            Logger.getLogger(JMainApplication.class).info("Banco de dados inacessível. Aplicação finalizada - Versão " + JAppCommons.VERSION);
            System.exit(0);
        }
    }

    @Override
    public void run() {
        try {

        } catch (Exception ex) {
            Logger.getLogger(JMainApplication.class).error(ex);
        }
        scheduleHandler = scheduler.schedule(this, 1, TimeUnit.SECONDS);
    }

    private class WebSocketServerListener implements IWebSocketListener {

        @Override
        public void onWebSocketConnect(JWebSocketClientReceiver instance, Session session) {
            Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketConnect: " + session.getRemoteAddress().getHostName());
        }

        @Override
        public void onWebSocketClose(JWebSocketClientReceiver instance, int statusCode, String reason, Session session) {
            Logger.getLogger(JWebSocketServerCreator.class).info("onWebSocketClose: statusCode: " + statusCode + " - reason: " + reason + " - session: " + session.getRemoteAddress().getHostName());
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
