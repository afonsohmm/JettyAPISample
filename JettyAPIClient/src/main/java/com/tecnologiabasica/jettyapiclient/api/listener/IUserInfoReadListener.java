/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapiclient.api.listener;

import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;

import java.util.LinkedList;

/**
 *
 * @author afonso
 */
public interface IUserInfoReadListener {

    public void onSucess(LinkedList<JUserInfoEntity> collection);

    public void onNotFound();

    public void onUnknow(int statusCode, String message);

    public void onFailure(String message);

}
