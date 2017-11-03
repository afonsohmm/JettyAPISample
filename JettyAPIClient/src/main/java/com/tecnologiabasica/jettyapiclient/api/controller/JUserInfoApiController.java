/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiclient.api.controller;

import com.tecnologiabasica.jettyapiclient.api.JUserInfoApiInterface;
import com.tecnologiabasica.jettyapiclient.api.listener.IUserInfoListener;
import java.util.LinkedList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import java.io.IOException;

/**
 *
 * @author afonso
 */
public class JUserInfoApiController {

    private int responseCode = -1;
    private String responseMessage = null;
    private IUserInfoListener responseListener = null;

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public JUserInfoEntity create(JUserInfoEntity entity, IUserInfoListener listener) {
        responseListener = listener;
        JUserInfoEntity syncEntity = null;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.create(entity);
        //Ao chamar a função passando o listener como null, será uma chamada sincrona.
        if (listener != null) {
            call.enqueue(new Callback<JUserInfoEntity>() {
                @Override
                public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                    switch (response.code()) {
                        //OK
                        case 200:
                            JUserInfoEntity entityResponse = response.body();
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            responseListener.onOk(entityResponse);
                            break;
                        default:
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            responseListener.onError(responseCode, responseMessage);
                            break;

                    }
                }

                @Override
                public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                    responseCode = -1;
                    responseMessage = thrwbl.getMessage();
                    responseListener.onError(responseCode, responseMessage);
                }
            });
        } else {
            Response<JUserInfoEntity> response = null;
            try {
                response = call.execute();
                if (response != null && response.code() == 200) {
                    switch (response.code()) {
                        //OK
                        case 200:
                            syncEntity = response.body();
                            break;
                        default:
                            syncEntity = null;
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            break;
                    }

                }
            } catch (IOException ex) {
                syncEntity = null;
                responseCode = -1;
                responseMessage = ex.getMessage();
            }
        }
        return syncEntity;
    }

    public JUserInfoEntity update(JUserInfoEntity entity, IUserInfoListener listener) {
        responseListener = listener;
        JUserInfoEntity syncEntity = null;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.update(entity);
        if (listener != null) {
            call.enqueue(new Callback<JUserInfoEntity>() {
                @Override
                public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                    switch (response.code()) {
                        //OK
                        case 200:
                            JUserInfoEntity entityResponse = response.body();
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            responseListener.onOk(entityResponse);
                            break;
                        default:
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            responseListener.onError(responseCode, responseMessage);
                            break;

                    }
                }

                @Override
                public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                    responseCode = -1;
                    responseMessage = thrwbl.getMessage();
                    responseListener.onError(responseCode, responseMessage);
                }
            });
        } else {
            Response<JUserInfoEntity> response = null;
            try {
                response = call.execute();
                if (response != null && response.code() == 200) {
                    switch (response.code()) {
                        //OK
                        case 200:
                            syncEntity = response.body();
                            break;
                        default:
                            syncEntity = null;
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            break;
                    }

                }
            } catch (IOException ex) {
                syncEntity = null;
                responseCode = -1;
                responseMessage = ex.getMessage();
            }
        }
        return syncEntity;
    }

    public JUserInfoEntity delete(String email, IUserInfoListener listener) {
        responseListener = listener;
        JUserInfoEntity syncEntity = null;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<JUserInfoEntity> call = serviceApi.delete(email);
        if (listener != null) {
            call.enqueue(new Callback<JUserInfoEntity>() {
                @Override
                public void onResponse(Call<JUserInfoEntity> call, Response<JUserInfoEntity> response) {
                    switch (response.code()) {
                        //OK
                        case 200:
                            JUserInfoEntity entityResponse = response.body();
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            responseListener.onOk(entityResponse);
                            break;
                        default:
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            responseListener.onError(responseCode, responseMessage);

                    }
                }

                @Override
                public void onFailure(Call<JUserInfoEntity> call, Throwable thrwbl) {
                    responseCode = -1;
                    responseMessage = thrwbl.getMessage();
                    responseListener.onError(responseCode, responseMessage);
                }
            });
        } else {
            Response<JUserInfoEntity> response = null;
            try {
                response = call.execute();
                if (response != null && response.code() == 200) {
                    switch (response.code()) {
                        //OK
                        case 200:
                            syncEntity = response.body();
                            break;
                        default:
                            syncEntity = null;
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            break;
                    }

                }
            } catch (IOException ex) {
                syncEntity = null;
                responseCode = -1;
                responseMessage = ex.getMessage();
            }
        }
        return syncEntity;
    }

    public LinkedList<JUserInfoEntity> read(String domainId, String groupId, IUserInfoListener listener) {
        responseListener = listener;
        LinkedList<JUserInfoEntity> syncList = null;
        JUserInfoApiInterface.UserInfoApiInterface serviceApi = JUserInfoApiInterface.getUserInfoApiClient();
        Call<LinkedList<JUserInfoEntity>> call = serviceApi.read(domainId, groupId);
        //NULL listener iqual syncronized call
        if (listener != null) {
            call.enqueue(new Callback<LinkedList<JUserInfoEntity>>() {
                @Override
                public void onResponse(Call<LinkedList<JUserInfoEntity>> call, Response<LinkedList<JUserInfoEntity>> response) {
                    switch (response.code()) {
                        //OK
                        case 200:
                            LinkedList<JUserInfoEntity> listResponse = response.body();
                            responseListener.onOk(listResponse);
                            break;
                        default:
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            responseListener.onError(responseCode, responseMessage);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<LinkedList<JUserInfoEntity>> call, Throwable thrwbl) {
                    responseCode = -1;
                    responseMessage = thrwbl.getMessage();
                    responseListener.onError(responseCode, responseMessage);
                }
            });
        } else {
            Response<LinkedList<JUserInfoEntity>> response = null;
            try {
                response = call.execute();
                if (response != null && response.code() == 200) {
                    switch (response.code()) {
                        //OK
                        case 200:
                            syncList = response.body();
                            break;
                        default:
                            responseCode = response.code();
                            responseMessage = JApiResponseString.getApiRespnse(responseCode);
                            syncList = null;
                            break;
                    }

                }
            } catch (IOException ex) {
                syncList = null;
                responseCode = -1;
                responseMessage = ex.getMessage();
            }
        }
        return syncList;
    }
}
