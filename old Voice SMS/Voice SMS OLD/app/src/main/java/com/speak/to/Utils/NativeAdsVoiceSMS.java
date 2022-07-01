package com.speak.to.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speak.to.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import java.util.ArrayList;
import java.util.List;

public class NativeAdsVoiceSMS {
    static final String TAG = "Ad";
    public static NativeAdListener nativeAdListener = new NativeAdListener() {
        @Override
        public void onMediaDownloaded(Ad ad) {
            Log.e(TAG, "Native ad has downloaded all assets.");
        }

        @Override
        public void onError(Ad ad, AdError adError) {
            Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
        }

        @Override
        public void onAdLoaded(Ad ad) {
            Log.e(TAG, "Native Ad is loaded, Ready to display");
        }

        @Override
        public void onAdClicked(Ad ad) {
            Log.d(TAG, "Native ad clicked!");
        }

        @Override
        public void onLoggingImpression(Ad ad) {
            Log.d(TAG, "Native ad impression logged!");
        }
    };
    static NativeAd myNativeAds;

    public static void prepareAds(Context context) {
        // context and String name of Ad Id in debug mode
        myNativeAds = new NativeAd(context, context.getApplicationContext().getResources().getString(R.string.native_add));
        // Request an Ad
        myNativeAds.loadAd(myNativeAds
                .buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .build());
    }

    public static void showAd(Context context, NativeAdLayout nativeAdLayout) {
        if (myNativeAds == null || !myNativeAds.isAdLoaded()) {
            nativeAdLayout.setVisibility(View.GONE);
            return;
        }
        myNativeAds.unregisterView();
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.fb_native_layout_voice_sms, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        //Add the options
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, myNativeAds, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        nativeAdTitle.setText(myNativeAds.getAdvertiserName());
        nativeAdBody.setText(myNativeAds.getAdBodyText());
        nativeAdSocialContext.setText(myNativeAds.getAdBodyText());
        nativeAdCallToAction.setVisibility(myNativeAds.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(myNativeAds.getAdCallToAction());
        nativeAdSocialContext.setText(myNativeAds.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        myNativeAds.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews
        );
        Log.e(TAG, "Ad Configured");
    }
}
