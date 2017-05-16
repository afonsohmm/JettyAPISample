/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons.entity;

/**
 *
 * @author afonso
 */
public class JUserInfoEntity extends JGenericInfoEntity {

    public static final String USER_NAME = "userName";
    public static final String USER_PASSWORD = "userPassword";
    public static final String USER_TOKEN = "userToken";

    public static final String TABLE_NAME = "userinfo";

    private String userName;
    private String userPassword;
    private String userToken;

    @Override
    public String toString() {
        String genericToString = " JGenericInfoEntity{" + "id=" + id + ", remoteId=" + remoteId + ", dateTimeReceived=" + dateTimeReceived + ", dateTime=" + dateTime + ", dateOnly=" + dateOnly + ", timeOnly=" + timeOnly + ", hourOfDay=" + hourOfDay + ", minutesOfHour=" + minutesOfHour + ", dayOfWeek=" + dayOfWeek + ", dayOfMonth=" + dayOfMonth + ", monthOfYear=" + monthOfYear + ", year=" + year + ", hourOfDayString=" + hourOfDayString + ", dateTimeString=" + dateTimeString + ", dateOnlyString=" + dateOnlyString + ", timeOnlyString=" + timeOnlyString + ", dayOfWeekString=" + dayOfWeekString + ", email=" + email + ", domainId=" + domainId + ", groupId=" + groupId + '}';
        return "JUserInfoEntity{" + "userName=" + userName + ", userPassword=" + userPassword + ", userToken=" + userToken + '}' + genericToString;
    }

    public JUserInfoEntity() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

}
