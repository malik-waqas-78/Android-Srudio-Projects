package com.niazitvpro.official.utils;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.niazitvpro.official.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.niazitvpro.official.utils.Constants.ADMOB_BANNER;
import static com.niazitvpro.official.utils.Constants.ADS_TYPE;
import static com.niazitvpro.official.utils.Constants.FACEBOOK_BANNER;


public class AdmobBannerAd {

    private static LinearLayout adBanner;
    private static com.facebook.ads.AdView fbadView;
    private LinearLayout facebookBanner;
    private SharedPrefTVApp sharedPrefTVApp;

    public AdmobBannerAd() {

    }

    public void loadAddBanner(View view,Activity activity){

        sharedPrefTVApp = new SharedPrefTVApp(activity);
        String adType =sharedPrefTVApp.getString(ADS_TYPE);

        if(adType.equalsIgnoreCase("Facebook")){

            faceBookBannerAdd(activity,view);

        }else {

            admobBannerAd(view,activity);
        }
    }

    public void admobBannerAd(View view,Activity activity) {

        String admob_banner_id = sharedPrefTVApp.getString(ADMOB_BANNER);

        AdSettings.setTestMode(true);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        AdView adView = new AdView(activity);

        adBanner = view.findViewById(R.id.adView_admob_banner);
        adView.setAdUnitId(admob_banner_id);
        adView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
        adBanner.setVisibility(View.VISIBLE);
        adBanner.addView(adView);
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener(){

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                adView.setVisibility(View.GONE);

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                adView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

    }

    public void faceBookBannerAdd(Activity activity,View view) {

        String facebook_banner_id = sharedPrefTVApp.getString(FACEBOOK_BANNER);

        AdSettings.setTestMode(true);
//        AdSettings.addTestDevice("0e43790a-e97d-4201-a8d2-b84492c6326d");
        fbadView = new com.facebook.ads.AdView(activity, facebook_banner_id, AdSize.BANNER_HEIGHT_50);
        fbadView.setVisibility(View.VISIBLE);
        facebookBanner = view.findViewById(R.id.ll_facebook_banner);
        facebookBanner.setVisibility(View.VISIBLE);
        facebookBanner.addView(fbadView);

        fbadView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

                fbadView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        fbadView.loadAd();

    }

}
