package com.niazitvpro.official.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.niazitvpro.official.R;
import com.niazitvpro.official.adapter.SubCategoryAdapter;

import static com.niazitvpro.official.utils.Constants.ADMOB_NATIVE;
import static com.niazitvpro.official.utils.Constants.ADS_TYPE;
import static com.niazitvpro.official.utils.Constants.FACEBOOK_NATIVE;


public class NativeBannerAd {

    private static UnifiedNativeAd nativeAd;
    private static com.facebook.ads.NativeBannerAd nativeBannerAd;
    private static SharedPrefTVApp sharedPrefTVApp;

    public static   void loadFacebookNative(Activity context,final LinearLayout nativeBanner){

        nativeBannerAd = new com.facebook.ads.NativeBannerAd(context, sharedPrefTVApp.getString(FACEBOOK_NATIVE));
        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                nativeBanner.setVisibility(View.GONE);

            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {

                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                nativeBanner.setVisibility(View.VISIBLE);
                View adView = NativeBannerAdView.render(context, nativeBannerAd, NativeBannerAdView.Type.HEIGHT_100);
                NativeAdLayout nativeAdLayout = new NativeAdLayout(context);
                nativeAdLayout.addView(adView);
                nativeBanner.removeAllViews();
                nativeBanner.addView(nativeAdLayout);
            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        nativeBannerAd.loadAd();
    }



    public static void loadNetiveAds(Activity activity, LinearLayout nativeBanner){

        sharedPrefTVApp = new SharedPrefTVApp(activity);

        String adType =sharedPrefTVApp.getString(ADS_TYPE);

        if(adType.equalsIgnoreCase("Facebook")){

            loadFacebookNative(activity,nativeBanner);

        }else {

            loadAdmobNativeAdd(activity,nativeBanner);
        }
    }



    private static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));

        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));

        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());


        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

    }

    private static void loadAdmobNativeAdd(Activity context,LinearLayout nativeAdLayout){
        final AdLoader adLoader = new AdLoader.Builder(context, sharedPrefTVApp.getString(ADMOB_NATIVE))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = unifiedNativeAd;

                        UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater()
                                .inflate(R.layout.cusom_admob_native_ad_banner, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        nativeAdLayout.setVisibility(View.VISIBLE);
                        nativeAdLayout.removeAllViews();
                        nativeAdLayout.addView(adView);

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {

                        nativeAdLayout.setVisibility(View.GONE);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();


        adLoader.loadAd(new AdRequest.Builder().build());
    }


}
