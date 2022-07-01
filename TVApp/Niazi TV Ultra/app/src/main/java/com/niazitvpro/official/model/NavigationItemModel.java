package com.niazitvpro.official.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NavigationItemModel {

    public String navItemId,navItemName,navItemImage;
    public List<NavigationItemModel> navigationItemModelList = new ArrayList<>();
    public String channelType,userAgent,directUrl;
    public String menuType;


    public NavigationItemModel(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray categoryArray = jsonObject.getJSONArray("category");
        JSONArray subCategoryArray = jsonObject.getJSONArray("sub_category");
        JSONArray channelArray = jsonObject.getJSONArray("channel");

        for(int i=0;i<categoryArray.length();i++){

            JSONObject categoryObject = categoryArray.getJSONObject(i);

            NavigationItemModel navigationItemModel = new NavigationItemModel(categoryObject);
            navigationItemModel.menuType = "category";
            navigationItemModelList.add(navigationItemModel);
        }

        for(int i=0;i<subCategoryArray.length();i++){

            JSONObject subCategoryObject = subCategoryArray.getJSONObject(i);

            NavigationItemModel navigationItemModel = new NavigationItemModel(subCategoryObject);
            navigationItemModel.menuType = "subcategory";
            navigationItemModelList.add(navigationItemModel);

        }


        for(int i=0;i<channelArray.length();i++){

            JSONObject channelObject = channelArray.getJSONObject(i);

            NavigationItemModel navigationItemModel = new NavigationItemModel(channelObject);
            navigationItemModel.menuType = "channel";
            navigationItemModelList.add(navigationItemModel);

        }

    }


    public NavigationItemModel(JSONObject jsonObject) throws JSONException {

        navItemId = jsonObject.getString("id");
        navItemImage = jsonObject.getString("image");

        if(jsonObject.has("name"))
            navItemName = jsonObject.getString("name");
        if(jsonObject.has("title"))
            navItemName = jsonObject.getString("title");
        if(jsonObject.has("channel_type"))
        channelType = jsonObject.getString("channel_type");
        if(jsonObject.has("channel_type"))
            userAgent = jsonObject.getString("user_agent");
        if(jsonObject.has("channel_type"))
            directUrl = jsonObject.getString("direct_url");
    }
}
