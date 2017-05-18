/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapitest;

import com.tecnologiabasica.jettyapiclient.api.controller.JUserInfoApiController;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoCreateUserListener;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoDeleteUserListener;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoGetUserListListener;
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
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoUpdateUserListener;
import com.tecnologiabasica.jettyapiwebsocket.client.JWebSocketClient;

/**
 *
 * @author afonso
 */
public class JMainApplication implements Runnable {

    private static JMainApplication instance = null;
    private ScheduledExecutorService scheduler = null;
    private ScheduledFuture<?> scheduleHandler = null;
    private String dataBaseName = "databasetest";

    private CreateUserInfoListener createUserInfoListener = new CreateUserInfoListener();
    private GetUserInfoListListener getUserInfoListListener = new GetUserInfoListListener();
    private UpdateUserInfoListener updateUserInfoListener = new UpdateUserInfoListener();
    private DeleteUserInfoListener deleteUserInfoListener = new DeleteUserInfoListener();

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

            JWebSocketClient.getInstance().start();
            
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
            userInfoApiController.createUser(entity, createUserInfoListener);
            JWebSocketClient.getInstance().sendMessage(entity.toString());
            
        } catch (Exception ex) {
            Logger.getLogger(JMainApplication.class).error(ex);
        }
        scheduleHandler = scheduler.schedule(this, 10, TimeUnit.SECONDS);
    }

    private class CreateUserInfoListener implements IUserInfoCreateUserListener {

        @Override
        public void created(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário criado: " + entity.toString());
            entity.setUserPassword("123456");
            JUserInfoApiController userInfoApiController = new JUserInfoApiController();
            userInfoApiController.updateUser(entity, updateUserInfoListener);
        }

        @Override
        public void badRequest() {
            Logger.getLogger(JMainApplication.class).info("Email informado não é válido");
        }

        @Override
        public void conflict() {
            Logger.getLogger(JMainApplication.class).info("Email informado já está em uso");
        }

        @Override
        public void notAcceptable() {
            Logger.getLogger(JMainApplication.class).info("Não foi possível cadastrar usuário");
        }

        @Override
        public void unknow() {
            Logger.getLogger(JMainApplication.class).info("Resposta desconhecida");
        }

        @Override
        public void failure(String message) {
            Logger.getLogger(JMainApplication.class).info("Não foi possível cadastrar usuário: " + message);
        }
    }

    private class GetUserInfoListListener implements IUserInfoGetUserListListener {

        @Override
        public void ok(LinkedList<JUserInfoEntity> list) {
            Logger.getLogger(JMainApplication.class).info("Total de usuários cadastrados: " + list.size());
            if (list.size() > 1) {
                JUserInfoEntity entity = list.getFirst();
                JUserInfoApiController userInfoApiController = new JUserInfoApiController();
                userInfoApiController.deleteUser(entity.getEmail(), deleteUserInfoListener);
            }
        }

        @Override
        public void noContent() {
            Logger.getLogger(JMainApplication.class).info("Não há usuários cadastrados");
        }

        @Override
        public void unknow() {
            Logger.getLogger(JMainApplication.class).info("Resposta desconhecida");
        }

        @Override
        public void failure(String message) {
            Logger.getLogger(JMainApplication.class).info("Não foi possível listar usuários: " + message);
        }
    }

    private class UpdateUserInfoListener implements IUserInfoUpdateUserListener {

        @Override
        public void ok(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário atualizado: " + entity.toString());
            JUserInfoApiController userInfoApiController = new JUserInfoApiController();
            userInfoApiController.getUserList(null, null, getUserInfoListListener);
        }

        @Override
        public void noContent() {
            Logger.getLogger(JMainApplication.class).info("Usuário não encontrado");
        }

        @Override
        public void unknow() {
            Logger.getLogger(JMainApplication.class).info("Resposta desconhecida");
        }

        @Override
        public void failure(String message) {
            Logger.getLogger(JMainApplication.class).info("Não foi possível atualizar usuário: " + message);
        }

    }

    private class DeleteUserInfoListener implements IUserInfoDeleteUserListener {

        @Override
        public void ok(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário apagado: " + entity.toString());
        }

        @Override
        public void noContent() {
            Logger.getLogger(JMainApplication.class).info("Usuário não foi apagado.");
        }

        @Override
        public void notFound() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void unknow() {
            Logger.getLogger(JMainApplication.class).info("Usuário não foi encontrado.");
        }

        @Override
        public void failure(String message) {
            Logger.getLogger(JMainApplication.class).info("Não foi possível apagar usuário: " + message);
        }

    }
}
