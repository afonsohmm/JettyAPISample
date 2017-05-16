/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapitest;

import com.tecnologiabasica.jettyapiclient.api.controller.JUserInfoApiController;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoCreateUserListener;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoGetUserListListener;
import com.tecnologiabasica.jettyapicommons.JAppCommons;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import com.tecnologiabasica.jettyapicommons.enums.EDatabaseType;
import com.tecnologiabasica.jettyapicommons.util.JThreadFactoryBuilder;
import com.tecnologiabasica.jettyapidatabase.JDatabaseConnector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 *
 * @author afonso
 */
public class JMainApplication implements Runnable {

    private static JMainApplication instance = null;
    private ScheduledExecutorService scheduler = null;
    private ScheduledFuture<?> scheduleHandler = null;
    private String dataBaseName = "databasetest";

    private CreateUserListener createUserListener = new CreateUserListener();
    private GetUserListListener getUserListListener = new GetUserListListener();

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
            JUserInfoEntity user = new JUserInfoEntity();
            user.setEmail(DateTime.now().getMillis() + "@gmail.com");
            user.setUserName("name: " + DateTime.now().getMillis());
            user.setUserToken("token: " + DateTime.now().getMillis());
            user.setUserPassword("password: " + DateTime.now().getMillis());
            JUserInfoApiController userInfoApiController = new JUserInfoApiController();
            userInfoApiController.createUser(user, "myemail", createUserListener);
            userInfoApiController.getUserList(null, null, getUserListListener);

        } catch (Exception ex) {
            Logger.getLogger(JMainApplication.class).error(ex);
        }
        scheduleHandler = scheduler.schedule(this, 1, TimeUnit.SECONDS);
    }

    private class CreateUserListener implements IUserInfoCreateUserListener {

        @Override
        public void created(JUserInfoEntity entity) {
            Logger.getLogger(JMainApplication.class).info("Usuário criado: " + entity.toString());
        }

        @Override
        public void badRequest() {
            Logger.getLogger(JMainApplication.class).info("Email informado não é válido.");
        }

        @Override
        public void conflict() {
            Logger.getLogger(JMainApplication.class).info("Email informado já existe.");
        }

        @Override
        public void notAcceptable() {
            Logger.getLogger(JMainApplication.class).info("Não foi possível cadastrar usuário.");
        }

        @Override
        public void unknow() {
            Logger.getLogger(JMainApplication.class).info("Resposta desconhecida.");
        }

        @Override
        public void failure(String message) {
            Logger.getLogger(JMainApplication.class).error(message);
        }

    }

    private class GetUserListListener implements IUserInfoGetUserListListener {

        @Override
        public void ok(LinkedList<JUserInfoEntity> list) {
            Logger.getLogger(JMainApplication.class).info("Lista de usuários: " + list.size());
        }

        @Override
        public void noContent() {
            Logger.getLogger(JMainApplication.class).info("Não há usuários cadastrados.");
        }

        @Override
        public void unknow() {
            Logger.getLogger(JMainApplication.class).info("Resposta desconhecida.");
        }

        @Override
        public void failure(String message) {
            Logger.getLogger(JMainApplication.class).error(message);
        }

    }

}
