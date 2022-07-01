package com.niazitvpro.official.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryChannel {

    public String channelID;
    public String channelName;
    public String directLiveUrl;
    public String channelImage;
    public String isVideoDownload;
    public List<SubCategoryChannel> subCategoryChannelList = new ArrayList<>();

    public SubCategoryChannel(JSONArray jsonArray) throws JSONException {

        for(int i=0;i<jsonArray.length();i++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            SubCategoryChannel subCategoryChannel = new SubCategoryChannel(jsonObject);
            subCategoryChannelList.add(subCategoryChannel);

        }

    }

    public SubCategoryChannel(JSONObject jsonObject) throws JSONException {

        channelID = jsonObject.getString("id");
        channelName = jsonObject.getString("name");
        directLiveUrl = jsonObject.getString("url");
        channelImage = jsonObject.getString("image");
        isVideoDownload = jsonObject.getString("is_video_download");
    }

    public SubCategoryChannel() {

    }
}
