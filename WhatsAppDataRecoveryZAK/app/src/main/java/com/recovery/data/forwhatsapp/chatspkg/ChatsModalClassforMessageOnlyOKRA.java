package com.recovery.data.forwhatsapp.chatspkg;

public class ChatsModalClassforMessageOnlyOKRA {
    String msgText;
    String id;
    String time;

    public ChatsModalClassforMessageOnlyOKRA(String msgText, String id, String time) {
        this.msgText = msgText;
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }
}
