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
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoUpdateDeleteUserListener;

/**
 *
 * @author afonso
 */
public class JUserInfoApiController {

    private IUserInfoCreateUserListener listenerCreateUser = null;
    private IUserInfoUpdateDeleteUserListener listenerUpdateDeleteUser = null;
    private IUserInfoGetUserListListener listenerGetUserList = null;

    public void createUser(JUserInfoEntity entity, String key, IUserInfoCreateUserListener listener) {
        listenerCreateUser = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.createUser(entity, key);
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
    
    public void updateUser(JUserInfoEntity entity, String key, IUserInfoUpdateDeleteUserListener listener) {
        listenerUpdateDeleteUser = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.updateUser(entity, key);
        call.enqueue(new Callback<JUserInfoEntity>() {
            @Override
            public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                switch (response.code()) {
                     //OK
                    case 200:
                        JUserInfoEntity entity200 = response.body();
                        listenerUpdateDeleteUser.ok(entity200);
                        break;
                    //NO CONTENT
                    case 204:
                        listenerUpdateDeleteUser.noContent();
                        break;
                    default:
                        listenerUpdateDeleteUser.unknow();
                        break;                        
                        
                }
            }

            @Override
            public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                listenerUpdateDeleteUser.failure(thrwbl.getMessage());
            }
        });
    }

    public void deleteUser(JUserInfoEntity entity, String key, IUserInfoUpdateDeleteUserListener listener) {
        listenerUpdateDeleteUser = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.deleteUser(entity, key);
        call.enqueue(new Callback<JUserInfoEntity>() {
            @Override
            public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                switch (response.code()) {
                     //OK
                    case 200:
                        JUserInfoEntity entity200 = response.body();
                        listenerUpdateDeleteUser.ok(entity200);
                        break;
                    //NO CONTENT
                    case 204:
                        listenerUpdateDeleteUser.noContent();
                        break;
                    default:
                        listenerUpdateDeleteUser.unknow();
                        break;                        
                        
                }
            }

            @Override
            public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                listenerUpdateDeleteUser.failure(thrwbl.getMessage());
            }
        });
    }

    public void getUserList(String domainId, String groupId, String key, IUserInfoGetUserListListener listener) {
        listenerGetUserList = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<LinkedList<JUserInfoEntity>> call = serviceApi.getUserList(domainId, groupId, key);
        call.enqueue(new Callback<LinkedList<JUserInfoEntity>>() {
            @Override
            public void onResponse(Call<LinkedList<JUserInfoEntity>> call, Response<LinkedList<JUserInfoEntity>> response) {
                switch (response.code()) {
                    //OK
                    case 200:
                        LinkedList<JUserInfoEntity> list200 = null;
                        list200 = response.body();
                        listenerGetUserList.ok(list200);
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
