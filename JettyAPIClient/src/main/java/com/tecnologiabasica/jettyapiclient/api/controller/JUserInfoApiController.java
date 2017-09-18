/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiclient.api.controller;

import com.tecnologiabasica.jettyapiclient.api.JUserInfoApiInterface;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoCreateListener;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoDeleteListener;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoReadListener;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoUpdateListener;
import java.util.LinkedList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author afonso
 */
public class JUserInfoApiController {

    private String errorMessage = null;
    private IUserInfoCreateListener createListener = null;
    private IUserInfoUpdateListener updateListener = null;
    private IUserInfoDeleteListener deleteListener = null;
    private IUserInfoReadListener readListener = null;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void create(JUserInfoEntity entity, IUserInfoCreateListener listener) {
        createListener = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.create(entity);
        call.enqueue(new Callback<JUserInfoEntity>() {
            @Override
            public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                switch (response.code()) {
                    //CREATED
                    case 201:
                        JUserInfoEntity entity201 = response.body();
                        createListener.onSucess(entity201);
                        break;
                    //BAD REQUEST
                    case 400:
                        createListener.onEmailNotValid();
                        break;
                    //CONFLICT
                    case 409:
                        createListener.onEmailInUse();
                        break;
                    //NOT ACCEPTABLE
                    case 406:
                        createListener.onError();
                        break;
                    default:
                        createListener.onUnknow();
                        break;

                }
            }

            @Override
            public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                errorMessage = thrwbl.getMessage();
                createListener.onFailure(thrwbl.getMessage());
            }
        });
    }

    public void update(JUserInfoEntity entity, IUserInfoUpdateListener listener) {
        updateListener = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.update(entity);
        call.enqueue(new Callback<JUserInfoEntity>() {
            @Override
            public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                switch (response.code()) {
                    //OK
                    case 200:
                        JUserInfoEntity entity200 = response.body();
                        updateListener.onSucess(entity200);
                        break;
                    //NO CONTENT
                    case 204:
                        updateListener.onError();
                        break;
                    default:
                        updateListener.onUnknow();
                        break;

                }
            }

            @Override
            public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                errorMessage = thrwbl.getMessage();
                updateListener.onFailure(thrwbl.getMessage());
            }
        });
    }

    public void delete(String email, IUserInfoDeleteListener listener) {
        deleteListener = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.delete(email);
        call.enqueue(new Callback<JUserInfoEntity>() {
            @Override
            public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                switch (response.code()) {
                    //OK
                    case 200:
                        JUserInfoEntity entity200 = response.body();
                        deleteListener.onSucess(entity200);
                        break;
                    //NO CONTENT
                    case 204:
                        deleteListener.onError();
                        break;
                    //NOT FOUND
                    case 404:
                        deleteListener.onNotFound();
                        break;
                    default:
                        deleteListener.onUnknow();
                        break;

                }
            }

            @Override
            public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                errorMessage = thrwbl.getMessage();
                deleteListener.onFailure(thrwbl.getMessage());
            }
        });
    }

    public void read(String domainId, String groupId, IUserInfoReadListener listener) {
        readListener = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<LinkedList<JUserInfoEntity>> call = serviceApi.read(domainId, groupId);
        call.enqueue(new Callback<LinkedList<JUserInfoEntity>>() {
            @Override
            public void onResponse(Call<LinkedList<JUserInfoEntity>> call, Response<LinkedList<JUserInfoEntity>> response) {
                switch (response.code()) {
                    //OK
                    case 200:
                        LinkedList<JUserInfoEntity> list200 = null;
                        list200 = response.body();
                        readListener.onSucess(list200);
                        break;
                    //NO CONTENT
                    case 204:
                        readListener.onNotFound();
                        break;
                    default:
                        readListener.onUnknow();
                        break;
                }
            }

            @Override
            public void onFailure(Call<LinkedList<JUserInfoEntity>> call, Throwable thrwbl) {
                errorMessage = thrwbl.getMessage();
                readListener.onFailure(thrwbl.getMessage());
            }
        });
    }

    public LinkedList<JUserInfoEntity> read(String domainId, String groupId) {
        LinkedList<JUserInfoEntity> list = null;
        Response<LinkedList<JUserInfoEntity>> response = null;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<LinkedList<JUserInfoEntity>> call = serviceApi.read(domainId, groupId);
        try {
            response = call.execute();
            if (response != null && response.code() == 200) {
                list = response.body();
            }
        } catch (IOException ex) {
            errorMessage = ex.getMessage();
            Logger.getLogger(JUserInfoApiController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
