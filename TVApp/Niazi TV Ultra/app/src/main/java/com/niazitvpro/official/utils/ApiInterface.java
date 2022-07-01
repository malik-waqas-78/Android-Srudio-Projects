package com.niazitvpro.official.utils;

import com.niazitvpro.official.exoplayer.YouTubeVideoInfoRetriever;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("setting.php")
    Call<ResponseBody> setting();

    @GET("get_category_list.php")
    Call<ResponseBody> getSideMenuCategory();

    @GET("get_design.php")
    Call<ResponseBody> getDesignApi();


    @FormUrlEncoded
    @POST("get_notification_list.php")
    Call<ResponseBody> getNotificationList(

            @Field("android_id") String androidID

    );

    @FormUrlEncoded
    @POST("delete_user_notification.php")
    Call<ResponseBody> clearUserNotificationList(

            @Field("android_id") String androidID

    );

    @FormUrlEncoded
    @POST("get_channel_list.php")
    Call<ResponseBody> getMenuItemsCategoryList(

            @Field("cat_id") String categoryID

    );

    @FormUrlEncoded
    @POST("get_sub_category.php")
    Call<ResponseBody> getSubcategoryList(

            @Field("cat_id") String categoryID

    );

    @FormUrlEncoded
    @POST("user_login.php")
    Call<ResponseBody> userLogin(

            @Field("android_id") String androidID

    );

    @FormUrlEncoded
    @POST("get_episode_list.php")
    Call<ResponseBody> getSubChannelsList(

            @Field("channel_id") String channelID

    );

    @FormUrlEncoded
    @POST("channel_search_by_text.php")
    Call<ResponseBody> getSearchList(

            @Field("text") String channelID

    );


}
