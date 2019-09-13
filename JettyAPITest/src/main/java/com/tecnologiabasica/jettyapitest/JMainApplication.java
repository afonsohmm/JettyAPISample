/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapitest;

import com.tecnologiabasica.jettyapiclient.api.controller.JUserInfoApiController;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoListener;
import com.tecnologiabasica.jettyapicommons.JAppCommons;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import com.tecnologiabasica.jettyapicommons.enums.EDatabaseType;
import com.tecnologiabasica.jettyapicommons.util.JThreadFactoryBuilder;
import com.tecnologiabasica.jettyapidatabase.JDatabaseConnector;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import com.tecnologiabasica.jettyapiwebsocket.client.JWebSocketClient;
import com.tecnologiabasica.jettyapiwebsocket.receiver.JWebSocketClientReceiver;
import org.eclipse.jetty.websocket.api.Session;
import com.tecnologiabasica.jettyapiwebsocket.listener.IWebSocketListener;

/**
 *
 * @author afonso
 */
public class JMainApplication implements Runnable {

    private static JMainApplication instance = null;
    private ScheduledExecutorService scheduler = null;
    private ScheduledFuture<?> scheduleHandler = null;
    private String dataBaseName = "jettyapitest";

    private UserInfoCreateListener userInfoCreateListener = new UserInfoCreateListener();
    private UserInfoReadListener userInfoReadListener = new UserInfoReadListener();
    private UserInfoUpdateListener userInfoUpdateListener = new UserInfoUpdateListener();
    private UserInfoDeleteListener userInfoDeleteListener = new UserInfoDeleteListener();

    private JWebSocketClient webSocketClient = null;
    private WebSocketClientListener webSocketClientListener = new WebSocketClientListener();

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

            webSocketClient = new JWebSocketClient();
            webSocketClient.start(webSocketClientListener);

            ThreadFactory customThreadfactory = new JThreadFactoryBuilder()
                    .setNamePrefix("thread_JMainApplication_scheduler").build();

            scheduler = Executors.newScheduledThreadPool(1, customThreadfactory);
            scheduleHandler = scheduler.schedule(this, 1, TimeUnit.SECONDS);

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
            JUserInfoEntity entity = new JUserInfoEntity();
            entity.setUserName("Nome-" + DateTime.now().getMillis());
            entity.setUserPassword("Password-" + DateTime.now().getMillis());
            entity.setUserToken("Token-" + DateTime.now().getMillis());
            entity.setEmail(DateTime.now().getMillis() + "@domain.com");
            entity.setDomainId("domain.com");
            entity.setGroupId("MyGroup");
            JUserInfoApiController userInfoApiController = new JUserInfoApiController();
            userInfoApiController.create(entity, userInfoCreateListener);
            webSocketClient.sendMessage(entity.toString());

        } catch (Exception ex) {
            Logger.getLogger(JMainApplication.class).error(ex);
        }
        scheduleHandler = scheduler.schedule(this, 10, TimeUnit.MILLISECONDS);
    }

    private class UserInfoCreateListener implements IUserInfoListener {

        @Override
        public void onOk(LinkedList<JUserInfoEntity> collection) {

        }

        @Override
        public void onOk(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário criado: " + entity.toString());
            entity.setUserPassword("123456");
            JUserInfoApiController userInfoApiController = new JUserInfoApiController();
            userInfoApiController.update(entity, userInfoUpdateListener);
        }

        @Override
        public void onError(int statusCode, String message) {
            Logger.getLogger(JMainApplication.class).info("Create: " + message);
        }
    }

    private class UserInfoReadListener implements IUserInfoListener {

        @Override
        public void onOk(LinkedList<JUserInfoEntity> collection) {
            Logger.getLogger(JMainApplication.class).info("Total de usuários cadastrados: " + collection.size());
            if (collection.size() > 4) {
                JUserInfoEntity entity = collection.getFirst();
                JUserInfoApiController userInfoApiController = new JUserInfoApiController();
                userInfoApiController.delete(entity.getEmail(), userInfoDeleteListener);
            }
        }

        @Override
        public void onOk(JUserInfoEntity entity) {

        }

        @Override
        public void onError(int statusCode, String message) {
            Logger.getLogger(JMainApplication.class).info("Read: " + message);
        }
    }

    private class UserInfoUpdateListener implements IUserInfoListener {


        @Override
        public void onOk(LinkedList<JUserInfoEntity> collection) {
            
        }

        @Override
        public void onOk(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário atualizado: " + entity.toString());
            JUserInfoApiController userInfoApiController = new JUserInfoApiController();
            userInfoApiController.read(null, null, userInfoReadListener);
        }

        @Override
        public void onError(int statusCode, String message) {
            Logger.getLogger(JMainApplication.class).info("Update: " + message);
        }

    }

    private class UserInfoDeleteListener implements IUserInfoListener {

        @Override
        public void onOk(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário apagado: " + entity.toString());
        }

        @Override
        public void onOk(LinkedList<JUserInfoEntity> collection) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onError(int statusCode, String message) {
            Logger.getLogger(JMainApplication.class).info("Delete: " + message);
        }

    }

    private class WebSocketClientListener implements IWebSocketListener {

        @Override
        public void onWebSocketConnect(JWebSocketClientReceiver instance, Session session) {
            Logger.getLogger(JMainApplication.class).info("onWebSocketConnect: " + session.getRemoteAddress().getHostName());
        }

        @Override
        public void onWebSocketClose(JWebSocketClientReceiver instance, int statusCode, String reason, Session session) {
            Logger.getLogger(JMainApplication.class).info("onWebSocketClose: statusCode: " + statusCode + " - reason: " + reason + " - session: " + session.getRemoteAddress().getHostName());
        }

        @Override
        public void onWebSocketError(JWebSocketClientReceiver instance, Throwable cause, Session session) {
            Logger.getLogger(JMainApplication.class).info("onWebSocketError: cause: " + cause.getMessage() + " - session: " + session.getRemoteAddress().getHostName());
        }

        @Override
        public void onWebSocketMessageReceive(JWebSocketClientReceiver instance, String message, Session session) {
            Logger.getLogger(JMainApplication.class).info("onWebSocketMessageReceive: message: " + message + " - session: " + session.getRemoteAddress().getHostName());
        }

    }
}
