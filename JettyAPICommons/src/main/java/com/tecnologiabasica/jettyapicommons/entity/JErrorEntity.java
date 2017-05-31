package com.tecnologiabasica.jettyapicommons.entity;

import java.io.Serializable;

/**
 * Created by afonso on 31/05/17.
 */
public class JErrorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code = -1;
    private String internalMessage = null;
    private String userMessage = null;
    private String moreInfo = null;
    private String url = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JErrorEntity that = (JErrorEntity) o;

        if (code != that.code) return false;
        return internalMessage != null ? internalMessage.equals(that.internalMessage) : that.internalMessage == null;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (internalMessage != null ? internalMessage.hashCode() : 0);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInternalMessage() {
        return internalMessage;
    }

    public void setInternalMessage(String internalMessage) {
        this.internalMessage = internalMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
