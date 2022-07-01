package com.niazitvpro.official.model;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashModel {

   public String welcomeMessage,rateApp,rateAppMessage,privacyPolicy,splashImageUrl;
   public String background_color;
   public String adType;
   public String admobBanner,admobNative,admobInterestitial;
   public String facebookBanner,facebookNative,facebookInterestitial;
   public String appLogo;

   public SplashModel(JSONObject jsonObject) throws JSONException {

       welcomeMessage = jsonObject.getString("welcome_message");
       rateApp = jsonObject.getString("rate_app");
       rateAppMessage = jsonObject.getString("rate_app_message");
       privacyPolicy = jsonObject.getString("privacy_policy_url");
       splashImageUrl = jsonObject.getString("splash_screen_image");
       background_color = jsonObject.getString("background_color");
       adType = jsonObject.getString("ads_type");
       admobBanner = jsonObject.getString("admob_banner");
       admobNative = jsonObject.getString("admob_native");
       admobInterestitial = jsonObject.getString("admob_interstitial");
       facebookBanner = jsonObject.getString("facebook_banner");
       facebookNative = jsonObject.getString("facebook_native");
       facebookInterestitial = jsonObject.getString("facebook_interstitial");
       appLogo = jsonObject.getString("logo");
   }
}
