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
public interface IUserInfoCreateListener {

    public void onSucess(JUserInfoEntity entity);

    public void onEmailNotValid();

    public void onEmailInUse();

    public void onError();

    public void onUnknow();

    public void onFailure(String message);

}
