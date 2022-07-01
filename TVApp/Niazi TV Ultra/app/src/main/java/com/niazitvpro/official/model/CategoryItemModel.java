package com.niazitvpro.official.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemModel {

    public String categoryID,categoryName,categoryImage;
    public List<CategoryItemModel> categoryItemList = new ArrayList<>();


    public CategoryItemModel(JSONArray jsonArray) throws JSONException {

        for(int i=0;i<jsonArray.length();i++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            CategoryItemModel categoryItemModel = new CategoryItemModel(jsonObject);
            categoryItemList.add(categoryItemModel);

        }

    }


    public CategoryItemModel(JSONObject jsonObject) throws JSONException {

        categoryID = jsonObject.getString("id");
        categoryImage = jsonObject.getString("image");
        categoryName = jsonObject.getString("title");
    }
}
