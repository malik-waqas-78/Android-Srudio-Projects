package com.recovery.data.forwhatsapp.chatspkg;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AllChatsProfilesModelClassOKRA extends RealmObject {
    @PrimaryKey
    String profile_name;
    String last_msg;
    String time;
    long timeinmilies=0;
    byte[] bytearray;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
//Icon icon=null;

    public AllChatsProfilesModelClassOKRA() {
    }

    public AllChatsProfilesModelClassOKRA(String profile_name, String last_msg, String time) {
        this.profile_name = profile_name;
        this.last_msg = last_msg;
        this.time = time;
    }

    public long getTimeinmilies() {
        return timeinmilies;
    }

    public void setTimeinmilies(long timeinmilies) {
        this.timeinmilies = timeinmilies;
    }

    //    public Icon getIcon() {
//        return icon;
//    }
//
//    public void setIcon(Icon icon) {
//        this.icon = icon;
//    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public byte[] getBytearray() {
        return bytearray;
    }

    public void setBytearray(byte[] bytearray) {
        this.bytearray = bytearray;
    }
}
