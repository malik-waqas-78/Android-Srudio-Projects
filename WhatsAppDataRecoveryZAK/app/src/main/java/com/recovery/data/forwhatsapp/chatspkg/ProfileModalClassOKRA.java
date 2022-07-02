package com.recovery.data.forwhatsapp.chatspkg;

public class ProfileModalClassOKRA {
    String profile_name;
    String last_msg;
    String time;
    long timeinmilies=0;
    byte[] bytearray;
    String url;

    public ProfileModalClassOKRA(String profile_name, String last_msg, String time, long timeinmilies, byte[] bytearray, String url) {
        this.profile_name = profile_name;
        this.last_msg = last_msg;
        this.time = time;
        this.timeinmilies = timeinmilies;
        this.bytearray = bytearray;
        this.url = url;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public String getTime() {
        return time;
    }

    public long getTimeinmilies() {
        return timeinmilies;
    }

    public byte[] getBytearray() {
        return bytearray;
    }

    public String getUrl() {
        return url;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTimeinmilies(long timeinmilies) {
        this.timeinmilies = timeinmilies;
    }

    public void setBytearray(byte[] bytearray) {
        this.bytearray = bytearray;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
