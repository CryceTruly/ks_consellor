package com.crycetruly.keepsafeconsellor.utils;

/**
 * Created by Elia on 9/22/2017.
 */

public class InfoHive {
    private String image, post_text, uid;
    private long posted;

    public InfoHive() {
    }

    public InfoHive(String image, String post_text, String uid, long posted) {
        this.image = image;
        this.post_text = post_text;
        this.uid = uid;
        this.posted = posted;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getPosted() {
        return posted;
    }

    public void setPosted(long posted) {
        this.posted = posted;
    }
}
