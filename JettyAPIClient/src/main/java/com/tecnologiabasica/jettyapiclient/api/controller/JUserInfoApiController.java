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
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoDeleteUserListener;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoUpdateUserListener;

/**
 *
 * @author afonso
 */
public class JUserInfoApiController {

    private IUserInfoCreateUserListener listenerCreateUser = null;
    private IUserInfoUpdateUserListener listenerUpdateUser = null;
    private IUserInfoDeleteUserListener listenerDeleteUser = null;
    private IUserInfoGetUserListListener listenerGetUserList = null;

    public void createUser(JUserInfoEntity entity, IUserInfoCreateUserListener listener) {
        listenerCreateUser = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.createUser(entity);
        call.enqueue(new Callback<JUserInfoEntity>() {
            @Override
            public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                switch (response.code()) {
                    //CREATED
                    case 201:
                        JUserInfoEntity entity201 = response.body();
                        listenerCreateUser.onUserInfoCreatedSucessfully(entity201);
                        break;
                    //BAD REQUEST
                    case 400:
                        listenerCreateUser.onUserInfoEmailNotValid();
                        break;
                    //CONFLICT
                    case 409:
                        listenerCreateUser.onUserInfoEmailInUse();
                        break;
                    //NOT ACCEPTABLE
                    case 406:
                        listenerCreateUser.onUserInfoCreateError();
                        break;                                            
                    default:
                        listenerCreateUser.onUserInfoCreateUnknow();
                        break;                        
                        
                }
            }

            @Override
            public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                listenerCreateUser.onUserInfoCreateFailure(thrwbl.getMessage());
            }
        });
    }
    
    public void updateUser(JUserInfoEntity entity, IUserInfoUpdateUserListener listener) {
        listenerUpdateUser = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.updateUser(entity);
        call.enqueue(new Callback<JUserInfoEntity>() {
            @Override
            public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                switch (response.code()) {
                     //OK
                    case 200:
                        JUserInfoEntity entity200 = response.body();
                        listenerUpdateUser.onUserInfoUpdatedSucessfully(entity200);
                        break;
                    //NO CONTENT
                    case 204:
                        listenerUpdateUser.onUserInfoUpdateError();
                        break;
                    default:
                        listenerUpdateUser.onUserInfoUpdateUnknow();
                        break;                        
                        
                }
            }

            @Override
            public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                listenerUpdateUser.onUserInfoUpdateFailure(thrwbl.getMessage());
            }
        });
    }

    public void deleteUser(String email, IUserInfoDeleteUserListener listener) {
        listenerDeleteUser = listener;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.deleteUser(email);
        call.enqueue(new Callback<JUserInfoEntity>() {
            @Override
            public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                switch (response.code()) {
                     //OK
                    case 200:
                        JUserInfoEntity entity200 = response.body();
                        listenerDeleteUser.onUserInfoDeletedSucessfully(entity200);
                        break;
                    //NO CONTENT
                    case 204:
                        listenerDeleteUser.onUserInfoDeleteError();
                        break;
                    //NOT FOUND
                    case 404:
                        listenerDeleteUser.onUserInfoNotFound();
                        break;                        
                    default:
                        listenerDeleteUser.onUserInfoDeleteUnknow();
                        break;                        
                        
                }
            }

            @Override
            public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                listenerDeleteUser.onUserInfoDeleteFailure(thrwbl.getMessage());
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
                        LinkedList<JUserInfoEntity> list200 = null;
                        list200 = response.body();
                        listenerGetUserList.onUserInfoListFound(list200);
                        break;
                    //NO CONTENT
                    case 204:
                        listenerGetUserList.onUserInfoListEmpty();
                        break;
                    default:
                        listenerGetUserList.onUserInfoListUnknow();
                        break;
                }
            }

            @Override
            public void onFailure(Call<LinkedList<JUserInfoEntity>> call, Throwable thrwbl) {
                listenerGetUserList.onUserInfoListFailure(thrwbl.getMessage());
            }
        });
    }

}
