package com.crycetruly.keepsafeconsellor;

public class Message {
    private String message, type, from;
    private long time;
    private boolean seen;
    private long sent;
    private String sendDate, reversedTime;
//TODO FIX THE FROM SOON


    public Message() {
    }

    //---------------------------FIRST IMPLEMENTATION------------------
    public Message(String message, String type, long time, boolean seen, String from, long sent) {
        this.message = message;
        this.type = type;
        this.time = time;

        this.seen = seen;
        this.sent = sent;
        this.from = from;
    }

    //--------------------ADDED REVERSED TIME------------------------------------
    public Message(String message, String type, String from, long time, boolean seen, long sent, String sendDate, String reversedTime) {
        this.message = message;
        this.type = type;
        this.from = from;
        this.time = time;
        this.seen = seen;
        this.sent = sent;
        this.sendDate = sendDate;
        this.reversedTime = reversedTime;
    }

    //---------------------------------ADDED THE SENTDATE------------------------------------------
    public Message(String message, String type, String from, long time, boolean seen, long sent, String sendDate) {
        this.message = message;
        this.type = type;
        this.from = from;
        this.time = time;
        this.seen = seen;
        this.sent = sent;
        this.sendDate = sendDate;
    }

    public String getReversedTime() {
        return reversedTime;
    }

    public void setReversedTime(String reversedTime) {
        this.reversedTime = reversedTime;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSent() {
        return sent;
    }

    public void setSent(long sent) {
        this.sent = sent;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}