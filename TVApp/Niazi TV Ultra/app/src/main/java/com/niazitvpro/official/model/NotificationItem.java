package com.niazitvpro.official.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationItem {

    public String notificationTitle;
    public String notificationDetail;
    public String notificationTime;
    public String notificationID;
    public List<NotificationItem> notificationItemList = new ArrayList<>();

    public NotificationItem(JSONArray jsonArray) throws JSONException {

        for(int i=0;i<jsonArray.length();i++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            NotificationItem notificationItem = new NotificationItem(jsonObject);
            notificationItemList.add(notificationItem);

        }

    }

    public NotificationItem(JSONObject jsonObject) throws JSONException {

        notificationTime = jsonObject.getString("created_date");
        notificationTitle = jsonObject.getString("title");
        notificationDetail = jsonObject.getString("description");
        notificationID = jsonObject.getString("id");

    }

    public NotificationItem() {

    }
}
