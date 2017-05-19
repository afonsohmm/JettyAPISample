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
public interface IUserInfoCreateUserListener {

    public void onUserInfoCreatedSucessfully(JUserInfoEntity entity);

    public void onUserInfoEmailNotValid();

    public void onUserInfoEmailInUse();

    public void onUserInfoCreateError();

    public void onUserInfoCreateUnknow();

    public void onUserInfoCreateFailure(String message);

}
