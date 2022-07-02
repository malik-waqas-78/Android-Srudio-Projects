package com.recovery.data.forwhatsapp.chatspkg;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ChatsManageRealmOKRA {
    String sender;
    String time;
    String date;
    String text;
    long timeinmilies=0;
    byte[] bytearray;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String ur) {
        this.url = ur;
       // Log.d("url", "setUrl: "+url);
    }

    public long getTimeinmili() {
        return timeinmilies;
    }

    public void setTimeinmili(long timeinmili) {
        this.timeinmilies = timeinmili;
    }

    public Realm getRealm_chat() {
        return realm_chat;
    }

    public void setRealm_chat(Realm realm_chat) {
        this.realm_chat = realm_chat;
    }

    public Realm getRealm_msg() {
        return realm_msg;
    }

    public void setRealm_msg(Realm realm_msg) {
        this.realm_msg = realm_msg;
    }

    Realm realm_chat;
    Realm realm_msg;
    public ChatsManageRealmOKRA() {
        initRealm();
    }

    public byte[] getBytearray() {
        return bytearray;
    }

    public void setBytearray(byte[] bytearray) {
        this.bytearray = bytearray;
    }

    public ChatsManageRealmOKRA(String sender, String time, String date, String text) {
        this.sender = sender;
        this.time = time;
        this.date = date;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void manageData(){
        AllChatsProfilesModelClassOKRA profile=new AllChatsProfilesModelClassOKRA();
        //Bundle bundle=sbn.getNotification().extras;

//            for (String key:bundle.keySet()){
//                Log.d(TAG, "extrs keyss: "+key);
//            }
        MessagesSingleModelClassOKRA msg=new MessagesSingleModelClassOKRA();


//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                sbn.getNotification().extras.get("android.icon");
//
////                profile.setIcon(icon);
////                msg.setIcon(icon);
//                showToast(icon);
//            }
//            Bitmap bmp=sbn.getNotification().extras.get("")


        msg.setName(sender);
        msg.setMsg_text(text);
        msg.setTime(date);
        msg.setTimeinmilies(timeinmilies);
        msg.setId(date+System.currentTimeMillis());

        //msg.setUrl(url);
        msg.setBytesarray(bytearray);

        profile.setProfile_name(sender);
        profile.setLast_msg(text);
        profile.setTime(date);
        profile.setTimeinmilies(timeinmilies);
        //profile.setUrl(url);
        profile.setBytearray(bytearray);

        realm_msg.beginTransaction();
        realm_msg.copyToRealmOrUpdate(msg);
        realm_msg.commitTransaction();

        realm_chat.beginTransaction();
        realm_chat.copyToRealmOrUpdate(profile);
        realm_chat.commitTransaction();
    }
    private void initRealm() {
        RealmConfiguration realm_chats = new RealmConfiguration.Builder()
                .name("realmchat.realm")
                .build();
        realm_chat=Realm.getInstance(realm_chats);
        RealmConfiguration realm_msgs=new RealmConfiguration.Builder()
                .name("realmmsg.realm")
                .build();
        realm_msg=Realm.getInstance(realm_msgs);

    }
}
