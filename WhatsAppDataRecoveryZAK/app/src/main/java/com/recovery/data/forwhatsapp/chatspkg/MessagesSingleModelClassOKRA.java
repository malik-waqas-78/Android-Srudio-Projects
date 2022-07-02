package com.recovery.data.forwhatsapp.chatspkg;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MessagesSingleModelClassOKRA extends RealmObject {
    String name;
    String msg_text;
    @PrimaryKey
    String id;
    String time;
    long timeinmilies=0;
    byte[] bytesarray;

    public byte[] getBytesarray() {
        return bytesarray;
    }

    public void setBytesarray(byte[] bytesarray) {
        this.bytesarray = bytesarray;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;
    //Icon icon=null;

    public MessagesSingleModelClassOKRA(String name, String msg_text, String time) {
        this.name = name;
        this.msg_text = msg_text;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MessagesSingleModelClassOKRA() {
    }

    public MessagesSingleModelClassOKRA(String msg_text, String time) {
        this.msg_text = msg_text;
        this.time = time;
    }

    public String getMsg_text() {
        return msg_text;
    }

    public void setMsg_text(String msg_text) {
        this.msg_text = msg_text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
