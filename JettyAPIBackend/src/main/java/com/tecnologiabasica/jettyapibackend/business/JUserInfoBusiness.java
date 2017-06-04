/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapibackend.business;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import com.tecnologiabasica.jettyapicommons.json.JsonExcludeStrategy;
import java.util.LinkedList;

/**
 *
 * @author afonso
 */
public class JUserInfoBusiness {

    private static JUserInfoBusiness instance = null;

    public static JUserInfoBusiness getInstance() {
        if (instance == null) {
            instance = new JUserInfoBusiness();
        }
        return instance;
    }
    
    private JUserInfoBusiness() {
        
    }
    
    public void createUser(JUserInfoEntity entity) {
        
    }
}
