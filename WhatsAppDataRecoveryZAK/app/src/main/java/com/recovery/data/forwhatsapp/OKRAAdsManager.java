package com.recovery.data.forwhatsapp;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdOptionsView;
//import com.facebook.ads.InterstitialAdListener;
//import com.facebook.ads.MediaView;
//import com.facebook.ads.NativeAd;
//import com.facebook.ads.NativeAdLayout;
//import com.facebook.ads.NativeAdListener;


public class OKRAAdsManager {


    private static final String TAG = OKRAAdsManager.class.getName();
//    static InterstitialAd interstitial;
//    private static com.facebook.ads.InterstitialAd interstitialAdFacebook;
//    public static NativeAd nativeAdFacebook;
//
///*
//    public static void LoadInterstitialAd(Context mContext) {
//        try {
//
//            interstitial = new InterstitialAd(mContext);
//
//            interstitial.setAdUnitId(mContext.getString(R.string.admob_interstitial));
//
//            if (!interstitial.isLoading() && !interstitial.isLoaded()) {
//                AdRequest adRequest = new AdRequest.Builder().build();
//                interstitial.loadAd(adRequest);
//            }
//
//            interstitial.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                }
//
//                @Override
//                public void onAdFailedToLoad(LoadAdError errorCode) {
//                    super.onAdFailedToLoad(errorCode);
//                }
//
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//
//                    if (!interstitial.isLoading() && !interstitial.isLoaded()) {
//                        AdRequest adRequest = new AdRequest.Builder().build();
//                        interstitial.loadAd(adRequest);
//                    }
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//*/
//
///*
//    public static void showInterstitialAd() {
//        if (interstitial != null) {
//            if (interstitial.isLoaded()) {
//                interstitial.show();
//            }
//        }
//    }
//*/
//
///*
//    public static AdSize getAdSize(Activity mContext) {
//        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
//        Display display = mContext.getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float widthPixels = outMetrics.widthPixels;
//        float density = outMetrics.density;
//
//        int adWidth = (int) (widthPixels / density);
//
//        // Step 3 - Get adaptive ad size and return for setting on the ad view.
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, adWidth);
//    }
//*/
//
//    //************************************************facebook ads******************************//
//
//    static class fbAdListner implements InterstitialAdListener {
//
//        @Override
//        public void onInterstitialDisplayed(Ad ad) {
//
//        }
//
//        @Override
//        public void onInterstitialDismissed(Ad ad) {
//            if (interstitialAdFacebook != null) {
//                if (!interstitialAdFacebook.isAdLoaded()) {
////                        interstitialAdFacebook.loadAd();
//                    interstitialAdFacebook.loadAd(
//                            interstitialAdFacebook.buildLoadAdConfig()
//                                    .withAdListener(new fbAdListner())
//                                    .build());
//                }
//            }
//        }
//
//        @Override
//        public void onError(Ad ad, AdError adError) {
//            Log.e("AdsManagerWO", "fb full ad error:" + adError);
//        }
//
//        @Override
//        public void onAdLoaded(Ad ad) {
//            Log.e("AdsManagerWO", "fb full ad loaded:" + ad);
//        }
//
//        @Override
//        public void onAdClicked(Ad ad) {
//
//        }
//
//        @Override
//        public void onLoggingImpression(Ad ad) {
//
//        }
//    }
//
//
//    public static void LoadFullAdFacebook(Context mContext) {
//        interstitialAdFacebook = new com.facebook.ads.InterstitialAd(mContext, mContext.getResources().getString(R.string.fbinterstitialad));
//        interstitialAdFacebook.loadAd(
//                interstitialAdFacebook.buildLoadAdConfig()
//                        .withAdListener(new fbAdListner())
//                        .build());
//    }
//
//    public static void displayInterstitialFacebook() {
//        if (interstitialAdFacebook != null) {
//            if (interstitialAdFacebook.isAdLoaded()) {
//                Log.e("Full", "Full ADS ");
//                interstitialAdFacebook.show();
//            }
//        }
//    }
//
//    public static void loadNativeAdFacebook(Context mContext) {
//        nativeAdFacebook = new NativeAd(mContext, mContext.getString(R.string.fbnativead));
//
//        NativeAdListener nativeAdListener = new NativeAdListener() {
//            @Override
//            public void onMediaDownloaded(Ad ad) {
//                // Native ad finished downloading all assets
//                Log.e(TAG, "Native ad finished downloading all assets.");
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
////                onAdLoadListener.onAdFailure(adError.getErrorMessage());
//                // Native ad failed to load
//                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
////                onAdLoadListener.onAdSuccess(ad);
//                // Native ad is loaded and ready to be displayed
//                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Native ad clicked
//                Log.d(TAG, "Native ad clicked!");
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Native ad impression
//                Log.d(TAG, "Native ad impression logged!");
//            }
//        };
//
//        nativeAdFacebook.loadAd(
//                nativeAdFacebook.buildLoadAdConfig()
//                        .withAdListener(nativeAdListener)
//                        .build());
//    }
//
//
//    public static void inflateAd(NativeAd nativeAdFacebook, Activity mContext, NativeAdLayout nativeAdLayout, int adViewLayout) {
//        nativeAdFacebook.unregisterView();
//
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        LinearLayout adView = (LinearLayout) inflater.inflate(adViewLayout, nativeAdLayout, false);
//        nativeAdLayout.addView(adView);
//
//        // Add the AdOptionsView
//        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
//        AdOptionsView adOptionsView = new AdOptionsView(mContext, nativeAdFacebook, nativeAdLayout);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);
//
//        // Create native UI using the ad metadata.
//        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
//        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
//        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
//        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
//        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
//        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
//
//        // Set the Text.
//        nativeAdTitle.setText(nativeAdFacebook.getAdvertiserName());
//        nativeAdBody.setText(nativeAdFacebook.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAdFacebook.getAdSocialContext());
//        nativeAdCallToAction.setVisibility(nativeAdFacebook.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdCallToAction.setText(nativeAdFacebook.getAdCallToAction());
//        sponsoredLabel.setText(nativeAdFacebook.getSponsoredTranslation());
//
//        // Create a list of clickable views
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdTitle);
//        clickableViews.add(nativeAdCallToAction);
//
//        // Register the Title and CTA button to listen for clicks.
//        nativeAdFacebook.registerViewForInteraction(
//                adView, nativeAdMedia, nativeAdIcon, clickableViews);
//    }
//
////=================================================================================================================
//public static boolean isAppInstalledFromPlay(Context mContext ) {
//        if (BuildConfig.DEBUG) {
//              /* try{
//                   val applicationInfo: ApplicationInfo =
//                       mContext.packageManager.getApplicationInfo(
//                           "com.phoneclone.oldtonewphone.data.transfer.unlimited",//com.phoneclone.oldtonewphone
//                           0
//                       )
//                   return "com.android.vending" == mContext.packageManager.getInstallerPackageName(
//                       applicationInfo.packageName
//                   )
//               }catch (e:PackageManager.NameNotFoundException){
//                   e.printStackTrace()
//                   return false
//               }*/
//            return true;
//        } else {
//            try {
//                ApplicationInfo applicationInfo =
//                        mContext.getPackageManager().getApplicationInfo(
//                                mContext.getApplicationInfo().packageName,
//                                0
//                        );
//                return "com.android.vending" == mContext.getPackageManager().getInstallerPackageName(
//                        applicationInfo.packageName
//                );
//            }catch ( PackageManager.NameNotFoundException e)  {
//                e.printStackTrace();
//                return false;
//            }
//        }
//    }
}
