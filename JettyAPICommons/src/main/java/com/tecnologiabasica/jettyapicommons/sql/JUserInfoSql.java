/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons.sql;

import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import com.tecnologiabasica.jettyapicommons.enums.EDatabaseType;


/**
 *
 * @author afonso
 */
public class JUserInfoSql extends JGenericInfoSql {

    public static String[] createTable(EDatabaseType databaseType) {
        String sqlReturn[] = null;
        String sql = null;

        sql = getGenericFields(JUserInfoEntity.TABLE_NAME, databaseType);
        sql += JUserInfoEntity.USER_NAME + " " + getTextString(databaseType) + ",";
        sql += JUserInfoEntity.USER_PASSWORD + " " + getTextString(databaseType) + ",";
        sql += JUserInfoEntity.USER_TOKEN + " " + getTextString(databaseType);
        
        sql += " )";

        String[] sqlIndex = getIndex(JUserInfoEntity.TABLE_NAME, databaseType);
        if (sqlIndex != null) {
            sqlReturn = new String[sqlIndex.length + 2];
            sqlReturn[0] = sql;
            
            sql = "CREATE INDEX " + JUserInfoEntity.TABLE_NAME + "_" + JUserInfoEntity.USER_NAME + "_idx ON " + JUserInfoEntity.TABLE_NAME + " (" + JUserInfoEntity.USER_NAME + ")";
            sqlReturn[1] = sql;            
            
            for (int i = 0; i < sqlIndex.length; i++) {
                sqlReturn[i + 2] = sqlIndex[i];
            }
        } else {
            sqlReturn = new String[1];
            sqlReturn[0] = sql;
        }

        return sqlReturn;
    }


}
