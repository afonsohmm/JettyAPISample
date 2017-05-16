/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapidatabase.dao;

import com.tecnologiabasica.jettyapicommons.entity.JUserInfoEntity;
import com.tecnologiabasica.jettyapidatabase.JDatabaseConnector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author afonso
 */
public class JUserInfoDAO extends JGenericInfoDAO {

    private static JUserInfoDAO instance = null;

    public static JUserInfoDAO getInstance() {
        if (instance == null) {
            instance = new JUserInfoDAO();
        }
        return instance;
    }

    private JUserInfoEntity getEntityFromDB(ResultSet result) {
        JUserInfoEntity entity = new JUserInfoEntity();
        try {
            getGenericEntityFromDB(entity, result);
            entity.setUserName(result.getString(JUserInfoEntity.USER_NAME));
            entity.setUserPassword(result.getString(JUserInfoEntity.USER_PASSWORD));
            entity.setUserToken(result.getString(JUserInfoEntity.USER_TOKEN));

        } catch (SQLException ex) {
            Logger.getLogger(JUserInfoDAO.class.getName()).error(ex);
            entity = null;
        }
        return entity;
    }

    private HashMap<String, Object> getMapFromEntity(JUserInfoEntity entity) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        getGenericMapFromEntity(map, entity);
        map.put(JUserInfoEntity.USER_NAME, entity.getUserName());
        map.put(JUserInfoEntity.USER_PASSWORD, entity.getUserPassword());
        map.put(JUserInfoEntity.USER_TOKEN, entity.getUserToken());
        return map;
    }

    public long insert(JUserInfoEntity entity) {
        long returnValue = -1;
        if (JDatabaseConnector.getInstance().insert(JUserInfoEntity.TABLE_NAME, getMapFromEntity(entity)) != -1) {
            returnValue = getLastID();
        }
        return returnValue;
    }

    public long update(JUserInfoEntity entity) {
        long returnValue = -1;
        returnValue = JDatabaseConnector.getInstance().update(JUserInfoEntity.TABLE_NAME, getMapFromEntity(entity), JUserInfoEntity.ID + "=" + entity.getId(), null);
        return returnValue;
    }

    public long delete(JUserInfoEntity entity) {
        long returnValue = -1;
        String sql = "delete from " + JUserInfoEntity.TABLE_NAME + " where " + JUserInfoEntity.ID + "=" + entity.getId();
        JDatabaseConnector.getInstance().execute(sql);
        return returnValue;
    }

    public long getLastID() {
        long result = -1;
        ResultSet rs = JDatabaseConnector.getInstance().query("select max(" + JUserInfoEntity.ID + ") as _id from " + JUserInfoEntity.TABLE_NAME);
        if (rs != null) {
            try {
                rs.first();
                result = rs.getInt(JUserInfoEntity.ID);
            } catch (SQLException ex) {
                Logger.getLogger(JUserInfoDAO.class.getName()).error(ex);
            }
            JDatabaseConnector.getInstance().closeResult(rs);
        }
        return result;
    }

    public LinkedList<JUserInfoEntity> getUserList(String domainId, String groupId) {
        LinkedList<JUserInfoEntity> list = null;

        String sql = "select * ";
        sql += " from " + JUserInfoEntity.TABLE_NAME;
        sql += " where " + JUserInfoEntity.ID + " > 0 ";
        if (domainId != null) {
            sql += " and " + JUserInfoEntity.DOMAIN_ID + " = '" + domainId + "'";
        }
        if (groupId != null) {
            sql += " and " + JUserInfoEntity.GROUP_ID + " = '" + groupId + "'";
        }

        sql += " order by " + JUserInfoEntity.USER_NAME;

        ResultSet rs = JDatabaseConnector.getInstance().query(sql);
        if (rs != null) {
            try {
                if (rs.first() == true) {
                    list = new LinkedList<>();
                    do {
                        JUserInfoEntity entity = getEntityFromDB(rs);
                        entity.setUserPassword(null);
                        entity.setRemoteId(entity.getId());
                        list.add(entity);
                    } while (rs.next());

                }
            } catch (SQLException ex) {
                Logger.getLogger(JUserInfoDAO.class.getName()).error(ex);
            }
            JDatabaseConnector.getInstance().closeResult(rs);
        }

        return list;
    }

    public JUserInfoEntity getUser(String email) {

        JUserInfoEntity entity = null;

        String sql = "select *";
        sql += " from " + JUserInfoEntity.TABLE_NAME;
        sql += " where " + JUserInfoEntity.EMAIL + " = '" + email + "' LIMIT 1";

        ResultSet rs = JDatabaseConnector.getInstance().query(sql);
        if (rs != null) {
            try {
                if (rs.first() == true) {
                    entity = getEntityFromDB(rs);
                    entity.setUserPassword(null);
                    entity.setRemoteId(entity.getId());
                }
            } catch (SQLException ex) {
                Logger.getLogger(JUserInfoDAO.class.getName()).error(ex);
            }
            JDatabaseConnector.getInstance().closeResult(rs);
        }
        return entity;
    }

}
