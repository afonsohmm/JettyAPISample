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

    public void ok(JUserInfoEntity entity);

    public void noContent();

    public void unknow();

    public void failure(String message);

}
