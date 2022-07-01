package com.niazitvpro.official.model;

import org.json.JSONException;
import org.json.JSONObject;

public class AppDesignModel {

    public String status,message;
    public String menuType,menuIcon;
    public String searchIcon,notificationIcon,shareIcon;

    public AppDesignModel(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        status = jsonObject.getString("status");
        message = jsonObject.getString("msg");

        JSONObject menu = jsonObject.getJSONObject("menu_type");
        menuType = menu.getString("menu_type");
        menuIcon = menu.getString("image");

        JSONObject topbarIcons = jsonObject.getJSONObject("top_bar_icon");
        notificationIcon = topbarIcons.getString("notification_img");
        searchIcon = topbarIcons.getString("search_img");
        shareIcon = topbarIcons.getString("share_img");



    }
}

//D/OkHttp: {"status":"1","msg":"success",
// "color":{"background_color":"#03c029","menu_background_color":"#b71e05","icons_color":"#0a239c",
// "top_bar_icons_color":"#bba70d","active_section_color":"#12ccc4","inactive_section_color":"#ff187d",
// "section_separator_color":"#00542d","section_indicator_color":"#da7b95","style":"None","menu_underline_color"
// :"#2e0252"},
// "menu_type":{"menu_type":"table_menu","image":"http:\/\/vistagain.com\/niazi_tv\/asset\/images\/495ee23551b5758cc2436539fad000c7.png"},
// "main_icon":{"image":"http:\/\/vistagain.com\/niazi_tv\/asset\/images\/6ea2e9b32a43e13e39eb48f59517c597.png"},
// "top_bar_icon":{"search_img":"http:\/\/vistagain.com\/niazi_tv\/asset\/images\/5db007aac712093d0386741302edf732.png",
// "notification_img":"http:\/\/vistagain.com\/niazi_tv\/asset\/images\/6bac657aa87673965a32a0e02e1378bb.png",
// "share_img":"http:\/\/vistagain.com\/niazi_tv\/asset\/images\/3fa49c517059b75233c0e60e92bcc1d0.png"}}