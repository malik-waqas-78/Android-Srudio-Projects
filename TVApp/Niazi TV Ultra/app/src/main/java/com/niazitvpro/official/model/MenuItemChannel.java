package com.niazitvpro.official.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuItemChannel implements Serializable {

    public String channelID;
    public String channelName;
    public String channelType;
    public String directLiveUrl;
    public String channelImage;
    public String userAgent;
    public List<MenuItemChannel> menuItemChannelList = new ArrayList<>();

    public MenuItemChannel(JSONArray jsonArray) throws JSONException {

        for(int i=0;i<jsonArray.length();i++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            MenuItemChannel menuItemChannel = new MenuItemChannel(jsonObject);
            menuItemChannelList.add(menuItemChannel);

        }

    }

    public MenuItemChannel(JSONObject jsonObject) throws JSONException {

        channelID = jsonObject.getString("id");
        channelName = jsonObject.getString("name");
        channelType = jsonObject.getString("channel_type");
        directLiveUrl = jsonObject.getString("direct_url");
        channelImage = jsonObject.getString("image");
        userAgent = jsonObject.getString("user_agent");
    }

    public MenuItemChannel() {

    }
}
