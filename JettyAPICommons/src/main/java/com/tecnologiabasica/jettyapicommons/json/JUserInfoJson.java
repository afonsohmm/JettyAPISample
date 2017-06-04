/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import java.util.LinkedList;

/**
 *
 * @author afonso
 */
public class JUserInfoJson {

    public static String getOutputJSonUserInfoEntity(JUserInfoEntity entity) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new JsonExcludeStrategy()).create();
        String output = gson.toJson(entity);
        return output;
    }
    
    public static JUserInfoEntity getOutputUserInfoEntityJson(String json) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new JsonExcludeStrategy()).create();
        JUserInfoEntity output = gson.fromJson(json, JUserInfoEntity.class);
        return output;
    }

    public static String getOutputJSonListUserInfo(LinkedList<JUserInfoEntity> list) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new JsonExcludeStrategy()).create();
        String output = gson.toJson(list);
        return output;
    }
    
    public static LinkedList<JUserInfoEntity> getOutputListUserInfoJson(String json) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new JsonExcludeStrategy()).create();
        LinkedList<JUserInfoEntity> output = gson.fromJson(json, LinkedList.class);
        return output;
    }

}
