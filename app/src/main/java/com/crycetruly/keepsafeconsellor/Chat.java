package com.crycetruly.keepsafeconsellor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elia on 3/1/2018.
 */

public class Chat  implements Parcelable{
    private String message;
    private Boolean seen;
    private String from;
    private long timestamp;

    public Chat() {
    }

    public Chat(String message, Boolean seen, String from, long timestamp) {
        this.message = message;
        this.seen = seen;
        this.from = from;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "message='" + message + '\'' +
                ", seen=" + seen +
                ", from='" + from + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static Creator<Chat> getCREATOR() {
        return CREATOR;
    }

    protected Chat(Parcel in) {
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
