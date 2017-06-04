/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiapi.api;

import com.tecnologiabasica.jettyapiapi.JApiSetup;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * @author afonso
 */
public class JUserInfoApiInterface {

    private static UserInfoApiInterface userInfoApiInterface;

    public static UserInfoApiInterface getUserInfoApiClient() {
        if (userInfoApiInterface == null) {
            Retrofit client = JApiSetup.getApiClient();
            userInfoApiInterface = client.create(UserInfoApiInterface.class);
        }
        return userInfoApiInterface;

    }

    public interface UserInfoApiInterface {
        @POST("api/v1/users/")
        Call<JUserInfoEntity> create(@Body JUserInfoEntity entity);

        @PUT("api/v1/users/")
        Call<JUserInfoEntity> update(@Body JUserInfoEntity entity);

        @DELETE("api/v1/users/")
        Call<JUserInfoEntity> delete(@Query("email") String email);

        @GET("api/v1/users/")
        Call<LinkedList<JUserInfoEntity>> read(@Query("domainId") String domainId, @Query("groupId") String groupId);
    }

}
