/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiclient.api.controller;

import com.tecnologiabasica.jettyapiclient.api.JUserInfoApiInterface;
import java.util.LinkedList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoListener;

/**
 *
 * @author afonso
 */
public class JUserInfoApiController {

    private IUserInfoListener createListener = null;
    private IUserInfoListener updateListener = null;
    private IUserInfoListener deleteListener = null;
    private IUserInfoListener readListener = null;

    public void create(JUserInfoEntity entity, IUserInfoListener listener) {
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
                createListener.onFailure(thrwbl.getMessage());
            }
        });
    }
    
    public void update(JUserInfoEntity entity, IUserInfoListener listener) {
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
                updateListener.onFailure(thrwbl.getMessage());
            }
        });
    }

    public void delete(String email, IUserInfoListener listener) {
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
                deleteListener.onFailure(thrwbl.getMessage());
            }
        });
    }

    public void read(String domainId, String groupId, IUserInfoListener listener) {
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
                readListener.onFailure(thrwbl.getMessage());
            }
        });
    }

}
