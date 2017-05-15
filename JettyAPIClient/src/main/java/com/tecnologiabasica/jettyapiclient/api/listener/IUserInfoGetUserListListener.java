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
public interface IUserInfoGetUserListListener {
    
    public void ok(LinkedList<JUserInfoEntity> list);
    public void noContent(); 
    public void unknow();
    public void failure(String message); 
    
}
