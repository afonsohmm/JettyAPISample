/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapitest;

import com.tecnologiabasica.jettyapiapi.api.controller.JUserInfoApiController;
import com.tecnologiabasica.jettyapiapi.api.listener.IUserInfoListener;
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
    private String dataBaseName = "databasetest";

    private CreateUserInfoInfoListener createUserInfoListener = new CreateUserInfoInfoListener();
    private GetUserInfoListListener getUserInfoListListener = new GetUserInfoListListener();
    private UpdateUserInfoListener updateUserInfoListener = new UpdateUserInfoListener();
    private DeleteUserInfoListener deleteUserInfoListener = new DeleteUserInfoListener();

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
            userInfoApiController.create(entity, createUserInfoListener);
            webSocketClient.sendMessage(entity.toString());

        } catch (Exception ex) {
            Logger.getLogger(JMainApplication.class).error(ex);
        }
        scheduleHandler = scheduler.schedule(this, 10, TimeUnit.SECONDS);
    }

    private class CreateUserInfoInfoListener implements IUserInfoListener {

        @Override
        public void onSucess(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário criado: " + entity.toString());
            entity.setUserPassword("123456");
            JUserInfoApiController userInfoApiController = new JUserInfoApiController();
            userInfoApiController.update(entity, updateUserInfoListener);
        }

        @Override
        public void onSucess(LinkedList<JUserInfoEntity> collection) {

        }

        @Override
        public void onNotFound() {

        }

        @Override
        public void onEmailNotValid() {
            Logger.getLogger(JMainApplication.class).info("Email informado não é válido");
        }

        @Override
        public void onEmailInUse() {
            Logger.getLogger(JMainApplication.class).info("Email informado já está em uso");
        }

        @Override
        public void onError() {
            Logger.getLogger(JMainApplication.class).info("Não foi possível cadastrar usuário");
        }

        @Override
        public void onUnknow() {
            Logger.getLogger(JMainApplication.class).info("Resposta desconhecida");
        }

        @Override
        public void onFailure(String message) {
            Logger.getLogger(JMainApplication.class).info("Não foi possível cadastrar usuário: " + message);
        }
    }

    private class GetUserInfoListListener implements IUserInfoListener {

        @Override
        public void onSucess(JUserInfoEntity entity) {

        }

        @Override
        public void onSucess(LinkedList<JUserInfoEntity> collection) {
            Logger.getLogger(JMainApplication.class).info("Total de usuários cadastrados: " + collection.size());
            if (collection.size() > 1) {
                JUserInfoEntity entity = collection.getFirst();
                JUserInfoApiController userInfoApiController = new JUserInfoApiController();
                userInfoApiController.delete(entity.getEmail(), deleteUserInfoListener);
            }
        }

        @Override
        public void onNotFound() {
            Logger.getLogger(JMainApplication.class).info("Não há usuários cadastrados");
        }

        @Override
        public void onEmailNotValid() {

        }

        @Override
        public void onEmailInUse() {

        }

        @Override
        public void onError() {

        }

        @Override
        public void onUnknow() {
            Logger.getLogger(JMainApplication.class).info("Resposta desconhecida");
        }

        @Override
        public void onFailure(String message) {
            Logger.getLogger(JMainApplication.class).info("Não foi possível listar usuários: " + message);
        }
    }

    private class UpdateUserInfoListener implements IUserInfoListener {

        @Override
        public void onSucess(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário atualizado: " + entity.toString());
            JUserInfoApiController userInfoApiController = new JUserInfoApiController();
            userInfoApiController.read(null, null, getUserInfoListListener);
        }

        @Override
        public void onSucess(LinkedList<JUserInfoEntity> collection) {

        }

        @Override
        public void onNotFound() {

        }

        @Override
        public void onEmailNotValid() {

        }

        @Override
        public void onEmailInUse() {

        }

        @Override
        public void onError() {
            Logger.getLogger(JMainApplication.class).info("Usuário não encontrado");
        }

        @Override
        public void onUnknow() {
            Logger.getLogger(JMainApplication.class).info("Resposta desconhecida");
        }

        @Override
        public void onFailure(String message) {
            Logger.getLogger(JMainApplication.class).info("Não foi possível atualizar usuário: " + message);
        }

    }

    private class DeleteUserInfoListener implements IUserInfoListener {

        @Override
        public void onSucess(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário apagado: " + entity.toString());
        }

        @Override
        public void onSucess(LinkedList<JUserInfoEntity> collection) {

        }

        @Override
        public void onNotFound() {
            Logger.getLogger(JMainApplication.class).info("Usuário não foi encontrado.");
        }

        @Override
        public void onEmailNotValid() {

        }

        @Override
        public void onEmailInUse() {

        }

        @Override
        public void onError() {
            Logger.getLogger(JMainApplication.class).info("Usuário não foi apagado.");
        }

        @Override
        public void onUnknow() {
            Logger.getLogger(JMainApplication.class).info("Resposta desconhecida.");
        }

        @Override
        public void onFailure(String message) {
            Logger.getLogger(JMainApplication.class).info("Não foi possível apagar usuário: " + message);
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
