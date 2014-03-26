package com.jonwelzel.ejb.session;

public class Session {

    private String key;

    private String userId;

    public Session() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
