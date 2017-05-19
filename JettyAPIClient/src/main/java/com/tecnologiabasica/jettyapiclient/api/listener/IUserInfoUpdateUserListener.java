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
public interface IUserInfoUpdateUserListener {

    public void onUserInfoUpdatedSucessfully(JUserInfoEntity entity);

    public void onUserInfoUpdateError();

    public void onUserInfoUpdateUnknow();

    public void onUserInfoUpdateFailure(String message);

}
