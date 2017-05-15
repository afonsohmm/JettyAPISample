/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapidatabase.dao;

import com.tecnologiabasica.jettyapicommons.entity.JGenericInfoEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author afonso
 */
public class JGenericInfoDAO {

    protected void getGenericEntityFromDB(JGenericInfoEntity entity, ResultSet result) {
        try {
            entity.setId(result.getLong(JGenericInfoEntity.ID));
            entity.setRemoteId(result.getLong(JGenericInfoEntity.REMOTE_ID));
            entity.setDateTimeReceived(result.getLong(JGenericInfoEntity.DATETIME_RECEIVED));
            entity.setDateTime(result.getLong(JGenericInfoEntity.DATETIME));
            entity.setEmail(result.getString(JGenericInfoEntity.EMAIL));
            entity.setDomainId(result.getString(JGenericInfoEntity.DOMAIN_ID));
            entity.setGroupId(result.getString(JGenericInfoEntity.GROUP_ID));

        } catch (SQLException ex) {
            Logger.getLogger(JGenericInfoDAO.class.getName()).error(ex);
        }
    }

    protected void getGenericMapFromEntity(HashMap<String, Object> map, JGenericInfoEntity entity) {        
        map.put(JGenericInfoEntity.REMOTE_ID, entity.getRemoteId());
        map.put(JGenericInfoEntity.DATETIME_RECEIVED, entity.getDateTimeReceived());
        map.put(JGenericInfoEntity.DATETIME, entity.getDateTime());
        map.put(JGenericInfoEntity.DATETIME_STRING, entity.getDateTimeString());
        map.put(JGenericInfoEntity.DATEONLY, entity.getDateOnly());
        map.put(JGenericInfoEntity.TIMEONLY, entity.getTimeOnly());
        map.put(JGenericInfoEntity.HOUROFDAY, entity.getHourOfDay());
        map.put(JGenericInfoEntity.MINUTESOFHOUR, entity.getMinutesOfHour());
        map.put(JGenericInfoEntity.DAYOFWEEK, entity.getDayOfWeek());
        map.put(JGenericInfoEntity.DAYOFMONTH, entity.getDayOfMonth());
        map.put(JGenericInfoEntity.MONTHOFYEAR, entity.getMonthofyear());
        map.put(JGenericInfoEntity.YEAR, entity.getYear());
        map.put(JGenericInfoEntity.EMAIL, entity.getEmail());
        map.put(JGenericInfoEntity.DOMAIN_ID, entity.getDomainId());
        map.put(JGenericInfoEntity.GROUP_ID, entity.getGroupId());

    }
    
}
