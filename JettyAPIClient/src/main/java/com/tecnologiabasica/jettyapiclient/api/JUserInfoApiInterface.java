/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiclient.api;

import com.tecnologiabasica.jettyapiclient.JApiClientSetup;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import java.util.LinkedList;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * @author afonso
 */
public class JUserInfoApiInterface {

    private static UserInfoApiInterface userInfoApiInterface;

    public static UserInfoApiInterface getUserInfoApiClient() {
        if (userInfoApiInterface == null) {
            Retrofit client = JApiClientSetup.getApiClient();
            userInfoApiInterface = client.create(UserInfoApiInterface.class);
        }
        return userInfoApiInterface;

    }

    public interface UserInfoApiInterface {
        @POST("api/userinfo/v1/createUser/{key}")
        Call<JUserInfoEntity> createUser(@Body JUserInfoEntity entity, @Path("key") String key);

        @PUT("api/userinfo/v1/updateUser/{key}")
        Call<JUserInfoEntity> updateUser(@Body JUserInfoEntity entity, @Path("key") String key);        
        
        @GET("api/userinfo/v1/getUserList/{key}")
        Call<LinkedList<JUserInfoEntity>> getUserList(@Query("domainId") String domainId, @Query("groupId") String groupId, @Path("key") String key);        
    }

}
