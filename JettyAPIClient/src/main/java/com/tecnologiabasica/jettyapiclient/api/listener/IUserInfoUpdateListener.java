/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiclient.api.listener;

import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;


/**
 *
 * @author afonso
 */
public interface IUserInfoUpdateListener {

    public void onSucess(JUserInfoEntity entity);
   
    public void onError();

    public void onUnknow();

    public void onFailure(String message);

}
