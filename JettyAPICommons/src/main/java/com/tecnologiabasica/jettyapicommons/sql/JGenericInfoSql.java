/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons.sql;

import com.tecnologiabasica.jettyapicommons.entity.JGenericInfoEntity;
import com.tecnologiabasica.jettyapicommons.enums.EDatabaseType;

/**
 *
 * @author afonso
 */
public class JGenericInfoSql {
    
    public static final int DATABASE_VERSION = 7;

    protected static String getIntegerString(EDatabaseType databaseType) {
        String returnValue = null;
        switch (databaseType) {
            case H2DB:
                returnValue = "INT";
                break;
            case PostgreSQL:
                returnValue = "INT";
                break;
            case SQLite:
                returnValue = "INTEGER";
                break;
        }

        return returnValue;
    }

    protected static String getGenericFields(String tableName, EDatabaseType databaseType) {
        String sql = null;
        sql = "create table " + tableName + " (";
        if (null != databaseType) {
            switch (databaseType) {
                case H2DB:
                    sql += JGenericInfoEntity.ID + " " + getLongString(databaseType) + " PRIMARY KEY AUTO_INCREMENT,";
                    break;
                case PostgreSQL:
                    sql += JGenericInfoEntity.ID + " " + getLongString(databaseType) + " PRIMARY KEY NOT NULL,";
                    break;
                case SQLite:
                    sql += JGenericInfoEntity.ID + " " + getIntegerString(databaseType) + " PRIMARY KEY AUTOINCREMENT NOT NULL,";
                    break;
            }
        }

        sql += JGenericInfoEntity.REMOTE_ID + " " + getLongString(databaseType) + ",";
        sql += JGenericInfoEntity.DATETIME_RECEIVED + " " + getLongString(databaseType) + ",";
        sql += JGenericInfoEntity.DATETIME + " " + getLongString(databaseType) + ",";
        sql += JGenericInfoEntity.DATETIME_STRING + " " + getTextString(databaseType) + ",";
        sql += JGenericInfoEntity.DATEONLY + " " + getLongString(databaseType) + ",";
        sql += JGenericInfoEntity.TIMEONLY + " " + getLongString(databaseType) + ",";
        sql += JGenericInfoEntity.HOUROFDAY + " " + getIntegerString(databaseType) + ",";
        sql += JGenericInfoEntity.MINUTESOFHOUR + " " + getIntegerString(databaseType) + ",";
        sql += JGenericInfoEntity.DAYOFWEEK + " " + getIntegerString(databaseType) + ",";
        sql += JGenericInfoEntity.DAYOFMONTH + " " + getIntegerString(databaseType) + ",";
        sql += JGenericInfoEntity.MONTHOFYEAR + " " + getIntegerString(databaseType) + ",";
        sql += JGenericInfoEntity.YEAR + " " + getIntegerString(databaseType) + ",";
        sql += JGenericInfoEntity.EMAIL + " " + getTextString(databaseType) + ",";
        sql += JGenericInfoEntity.DOMAIN_ID + " " + getTextString(databaseType) + ",";
        sql += JGenericInfoEntity.GROUP_ID + " " + getTextString(databaseType) + ",";

        return sql;
    }

    protected static String getLongString(EDatabaseType databaseType) {
        String returnValue = null;
        switch (databaseType) {
            case H2DB:
                returnValue = "BIGINT";
                break;
            case PostgreSQL:
                returnValue = "BIGINT";
                break;
            case SQLite:
                returnValue = "INTEGER";
                break;
        }

        return returnValue;
    }

    protected static String getTextString(EDatabaseType databaseType) {
        String returnValue = null;
        switch (databaseType) {
            case H2DB:
                returnValue = "TEXT";
                break;
            case PostgreSQL:
                returnValue = "TEXT";
                break;
            case SQLite:
                returnValue = "TEXT";
                break;
        }

        return returnValue;
    }

    protected static String getDoubleString(EDatabaseType databaseType) {
        String returnValue = null;
        switch (databaseType) {
            case H2DB:
                returnValue = "DOUBLE";
                break;
            case PostgreSQL:
                returnValue = "DOUBLE PRECISION";
                break;
            case SQLite:
                returnValue = "REAL";
                break;
        }

        return returnValue;
    }

    protected static String[] getIndex(String tableName, EDatabaseType databaseType) {
        String sqlReturn[] = null;
        String sql = null;
        //Criando o auto incremento para o PostgreSQL
        if (databaseType == EDatabaseType.PostgreSQL) {
            sqlReturn = new String[6];
            sql = "CREATE SEQUENCE " + tableName + "_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1";
            sqlReturn[0] = sql;

            sql = "ALTER TABLE " + tableName + " ALTER COLUMN " + JGenericInfoEntity.ID + " SET DEFAULT NEXTVAL('" + tableName + "_id_seq')";
            sqlReturn[1] = sql;

            sql = "CREATE INDEX " + tableName + "_" + JGenericInfoEntity.EMAIL + "_idx ON " + tableName + " (" + JGenericInfoEntity.EMAIL + ")";
            sqlReturn[2] = sql;

            sql = "CREATE INDEX " + tableName + "_" + JGenericInfoEntity.DOMAIN_ID + "_idx ON " + tableName + " (" + JGenericInfoEntity.DOMAIN_ID + ")";
            sqlReturn[3] = sql;

            sql = "CREATE INDEX " + tableName + "_" + JGenericInfoEntity.GROUP_ID + "_idx ON " + tableName + " (" + JGenericInfoEntity.GROUP_ID + ")";
            sqlReturn[4] = sql;

            sql = "CREATE INDEX " + tableName + "_" + JGenericInfoEntity.DATETIME + "_idx ON " + tableName + " (" + JGenericInfoEntity.DATETIME + ")";
            sqlReturn[5] = sql;
            

        }
        return sqlReturn;
    }

}
