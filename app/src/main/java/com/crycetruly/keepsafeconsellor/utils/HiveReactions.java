package com.crycetruly.keepsafeconsellor.utils;

/**
 * Created by Elia on 9/30/2017.
 */

public class HiveReactions {
    private long time;
    private String user;
    private String said;

    public HiveReactions() {
    }

    public HiveReactions(long time, String user, String said) {
        this.time = time;
        this.user = user;
        this.said = said;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSaid() {
        return said;
    }

    public void setSaid(String said) {
        this.said = said;
    }
}
