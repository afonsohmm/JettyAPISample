/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;

/**
 *
 * @author afonso
 */
public class JGenericInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ID = "_id";
    public static final String REMOTE_ID = "remoteId";
    public static final String DATETIME_RECEIVED = "dateTimeReceived";
    public static final String DATETIME = "dateTime";
    public static final String DATETIME_STRING = "dateTimeString";
    public static final String DATEONLY = "dateOnly";
    public static final String TIMEONLY = "timeOnly";
    public static final String HOUROFDAY = "hourOfDay";
    public static final String MINUTESOFHOUR = "minutesOfHour";
    public static final String DAYOFWEEK = "dayOfWeek";
    public static final String DAYOFMONTH = "dayOfMonth";
    public static final String MONTHOFYEAR = "monthOfYear";
    public static final String YEAR = "year";
    public static final String EMAIL = "email";
    public static final String DOMAIN_ID = "domainId";
    public static final String GROUP_ID = "groupId";

    protected long id = -1;
    protected long remoteId = -1;           
    protected long dateTimeReceived = 0;
    protected long dateTime = 0;
    protected long dateOnly = 0;
    protected long timeOnly = 0;
    protected int hourOfDay = 0;
    protected int minutesOfHour = 0;
    protected int dayOfWeek = 0;
    protected int dayOfMonth = 0;
    protected int monthOfYear = 0;
    protected int year = 0;
    protected String hourOfDayString;
    protected String dateTimeString;
    protected String dateOnlyString;
    protected String timeOnlyString;
    protected String dayOfWeekString;
    protected String email;
    protected String domainId; 
    protected String groupId;

    public JGenericInfoEntity() {
        DateTime dtNow = new DateTime().now();
        setDateTimeReceived(dtNow.getMillis());
        setDateTime(dtNow.getMillis());
    }

    public long getDateTimeReceived() {
        return dateTimeReceived;
    }

    public void setDateTimeReceived(long dateTimeReceived) {
        this.dateTimeReceived = dateTimeReceived;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
        SimpleDateFormat datePattern = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateTimeString = datePattern.format(new Date(dateTime));

        DateTime dt = new DateTime(dateTime);
        setDateOnly(dt.withTimeAtStartOfDay().getMillis());
        setTimeOnly(dt.getSecondOfDay());
        setHourOfDay(dt.getHourOfDay());
        setMinutesOfHour(dt.getMinuteOfHour());
        setDayOfWeek(dt.getDayOfWeek());
        setDayOfMonth(dt.getDayOfMonth());
        setMonthofyear(dt.getMonthOfYear());
        setYear(dt.getYear());
    }

    private void setDateOnly(long dateOnly) {
        this.dateOnly = dateOnly;
        SimpleDateFormat datePattern = new SimpleDateFormat("dd/MM/yyyy");
        dateOnlyString = datePattern.format(new Date(dateOnly));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public long getDateOnly() {
        return dateOnly;
    }

    public long getTimeOnly() {
        return timeOnly;
    }

    public void setTimeOnly(long timeOnly) {
        this.timeOnly = timeOnly;
        SimpleDateFormat datePattern = new SimpleDateFormat("HH:mm:ss");
        timeOnlyString = datePattern.format(new Date(timeOnly));
    }

    public String getTimeOnlyString() {
        return timeOnlyString;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
        hourOfDayString = hourOfDay + "h00m - " + hourOfDay + "h59m";
    }

    public int getMinutesOfHour() {
        return minutesOfHour;
    }

    public void setMinutesOfHour(int minutesOfHour) {
        this.minutesOfHour = minutesOfHour;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        switch (this.dayOfWeek) {
            case 1:
                this.dayOfWeekString = "Segunda";
                break;
            case 2:
                this.dayOfWeekString = "Terça";
                break;
            case 3:
                this.dayOfWeekString = "Quarta";
                break;
            case 4:
                this.dayOfWeekString = "Quinta";
                break;
            case 5:
                this.dayOfWeekString = "Sexta";
                break;
            case 6:
                this.dayOfWeekString = "Sábado";
                break;
            case 7:
                this.dayOfWeekString = "Domingo";
                break;
        }
    }

    public String getDayOfWeekString() {
        return dayOfWeekString;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonthofyear() {
        return monthOfYear;
    }

    public void setMonthofyear(int monthofyear) {
        this.monthOfYear = monthofyear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getHourOfDayString() {
        return hourOfDayString;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public String getDateOnlyString() {
        return dateOnlyString;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        if (email != null) {
            String param[] = email.split("@");
            if (param.length > 0) {
                domainId = param[param.length - 1];
            }
        }
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
