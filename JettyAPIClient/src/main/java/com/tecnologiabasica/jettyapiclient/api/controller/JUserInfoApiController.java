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
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoGetUserListListener;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoCreateUserListener;

/**
 *
 * @author afonso
 */
public class JUserInfoApiController {

    private IUserInfoCreateUserListener listenerCreateUser = null;
    private IUserInfoGetUserListListener listenerGetUserList = null;

    public void createUser(JUserInfoEntity entity, String email, IUserInfoCreateUserListener listener) {
        listenerCreateUser = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.createUser(entity, email);
        call.enqueue(new Callback<JUserInfoEntity>() {
            @Override
            public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                switch (response.code()) {
                    //CREATED
                    case 201:
                        JUserInfoEntity entity201 = response.body();
                        listenerCreateUser.created(entity201);
                        break;
                    //BAD REQUEST
                    case 400:
                        listenerCreateUser.badRequest();
                        break;
                    //CONFLICT
                    case 409:
                        listenerCreateUser.conflict();
                        break;
                    //NOT ACCEPTABLE
                    case 406:
                        listenerCreateUser.notAcceptable();
                        break;                                            
                    default:
                        listenerCreateUser.unknow();
                        break;                        
                        
                }
            }

            @Override
            public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                listenerCreateUser.failure(thrwbl.getMessage());
            }
        });
    }

    public void getUserList(String domainId, String groupId, IUserInfoGetUserListListener listener) {
        listenerGetUserList = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<LinkedList<JUserInfoEntity>> call = serviceApi.getUserList(domainId, groupId);
        call.enqueue(new Callback<LinkedList<JUserInfoEntity>>() {
            @Override
            public void onResponse(Call<LinkedList<JUserInfoEntity>> call, Response<LinkedList<JUserInfoEntity>> response) {
                switch (response.code()) {
                    //OK
                    case 200:
                        LinkedList<JUserInfoEntity> list = null;
                        list = response.body();
                        listenerGetUserList.ok(list);
                        break;
                    //NO CONTENT
                    case 204:
                        listenerGetUserList.noContent();
                        break;
                    default:
                        listenerGetUserList.unknow();
                        break;
                }
            }

            @Override
            public void onFailure(Call<LinkedList<JUserInfoEntity>> call, Throwable thrwbl) {
                listenerGetUserList.failure(thrwbl.getMessage());
            }
        });
    }

}
