package com.niazitvpro.official.model;

import android.app.Activity;
import android.util.Log;

import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.MainActivity;
import com.niazitvpro.official.adapter.SearchShowListAdapter;
import com.niazitvpro.official.utils.ReadWriteJsonFileUtils;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchItemsModel {

    public String  searchItemName,searchItemUrl;
    public String searchItemImage;
    public String searchLinkType;
    public String searchID;
    public String userAgent,fileName;
    private static SharedPrefTVApp prefTVApp;
    public ArrayList<SearchItemsModel> searchItemsModelList = new ArrayList<>();

    public SearchItemsModel(String[] split) throws JSONException {
        for (int k=0;k<split.length;k++){

            JSONObject jsonObject = null;
            try {

                jsonObject = new JSONObject(split[k]);
                String status = jsonObject.getString("status");

                if (jsonObject.has("msg")) {

                    String message = jsonObject.getString("msg");


                } else {

                }

                if (status.equals("1")) {

                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject object = data.getJSONObject(i);

                        SearchItemsModel searchItemsModel = new SearchItemsModel(object, fileName);
                        searchItemsModelList.add(searchItemsModel);

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public SearchItemsModel(JSONObject jsonObject,String f) throws JSONException {

        searchID = jsonObject.getString("id");
        searchItemName = jsonObject.getString("name");
        searchLinkType = jsonObject.getString("channel_type");
        searchItemUrl = jsonObject.getString("direct_url");
        searchItemImage = jsonObject.getString("image");
        userAgent = jsonObject.getString("user_agent");
        fileName = f+searchID;
    }

    public SearchItemsModel() {

    }

    public static void setSearchList(Activity activity,String categoryID,boolean isUpdate){
        if(activity!=null){
            prefTVApp = new SharedPrefTVApp(activity);
            String getCacheResponse = new ReadWriteJsonFileUtils(activity).readJsonFileData("Subcategory"+categoryID);
            String getSearchCacheResponse = new ReadWriteJsonFileUtils(activity).readJsonFileData("SearchItemist");
            if(getSearchCacheResponse!=null && !getSearchCacheResponse.equals("")){
                if(!isUpdate){
                    try {
                        new ReadWriteJsonFileUtils(activity).createJsonFileData("SearchItemist",getSearchCacheResponse+"----------"+getCacheResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    String newCacheString = "";
                    String[] parts = getSearchCacheResponse.split("----------");
                    for(int i=0;i<parts.length;i++){
                        String response =  parts[i];
                        if (isResponseUpdate(response,getCacheResponse)) {
                            newCacheString = getCacheResponse;
                        } else {
                            if(newCacheString.equals("")){
                                newCacheString = newCacheString+response;
                            }else {
                                newCacheString = newCacheString+"----------"+response;
                            }
                        }
                    }
                    if(newCacheString!=null && !newCacheString.equals("")) {
                        try {
                            new ReadWriteJsonFileUtils(activity).createJsonFileData("SearchItemist", newCacheString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            } else {

                if(getCacheResponse!=null && !getCacheResponse.equals("")) {
                    try {
                        new ReadWriteJsonFileUtils(activity).createJsonFileData("SearchItemist", getCacheResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            updateSearchAdapter(activity);
        }
    }

    private static boolean isResponseUpdate(String response, String cacheResponse){
        boolean isUpdated= false;
        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equals("1")) {

                JSONArray data = jsonObject.getJSONArray("data");
                for(int i=0;i<data.length();i++){
                    JSONObject object = data.getJSONObject(i);
                    if(cacheResponse.contains(object.toString())){
                        isUpdated = true;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    private static void updateSearchAdapter(Activity activity){
        String getSearchCacheResponse = new ReadWriteJsonFileUtils(activity).readJsonFileData("SearchItemist");
        String[] parts = getSearchCacheResponse.split("----------");

        Log.d("string==",getSearchCacheResponse);
        if(getSearchCacheResponse!=null && !getSearchCacheResponse.equals("")){

            MainActivity.setRecycler_searchItemList(activity,parts);
        }
    }
}
