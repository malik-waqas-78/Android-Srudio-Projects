package com.niazitvpro.official.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.VideoPlayerActivity;
import com.niazitvpro.official.fragment.AllVideoPlayingFragment;
import com.niazitvpro.official.fragment.ChannelSubcategoryFragment;

import static com.niazitvpro.official.fragment.AllVideoPlayingFragment.exoPlayer;
import static com.niazitvpro.official.fragment.AllVideoPlayingFragment.playerView;
import static com.niazitvpro.official.utils.Constants.ADMOB_INTERESTITIAL;
import static com.niazitvpro.official.utils.Constants.ADS_TYPE;
import static com.niazitvpro.official.utils.Constants.FACEBOOK_INTERESTITIAL;

public class AdmobIntrestrialAd extends Activity {


    public static int counter = 1;
    public static com.google.android.gms.ads.InterstitialAd mInterstitialAd;
    private static InterstitialAd mInterstitialFbAd;
    public static ProgressDialog progressDialog;
    private static AdmobIntrestrialAd myObj;
    public static AdRequest adRequest;
    private SharedPrefTVApp sharedPrefTVApp;

    static {

        myObj = new AdmobIntrestrialAd();
    }

    public AdmobIntrestrialAd() {
    }

    public static AdmobIntrestrialAd getInstance() {
        return myObj;
    }


    public void loadIntertitialAds(Activity activity){

            sharedPrefTVApp = new SharedPrefTVApp(activity);

            String adType =sharedPrefTVApp.getString(ADS_TYPE);

            if(adType.equalsIgnoreCase("Facebook")){

                loadFacebookInterstitial(activity);

            }else {

                loadAdmobInterstitial(activity);

        }

    }


    public  void loadAdmobInterstitial(final Activity parent) {

        String admob_interestitial_id = sharedPrefTVApp.getString(ADMOB_INTERESTITIAL);

        counter--;

        if (counter == 0) {

            counter = 2;

            mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(parent);
            mInterstitialAd.setAdUnitId(admob_interestitial_id);

            adRequest = new AdRequest.Builder()
                    .build();

            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    checkIfVideoIsPlayed(parent);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);


                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();

                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();

                    mInterstitialAd.show();

                }
            });

        }else {

        }

    }

    public void loadFacebookInterstitial( Activity parent) {

//        AudienceNetworkAds.initialize(parent);
        String facebook_interestitial_id = sharedPrefTVApp.getString(FACEBOOK_INTERESTITIAL);

        counter--;

        if (counter == 0) {

            counter = 2;

            AdSettings.setTestMode(true);

            mInterstitialFbAd = new com.facebook.ads.InterstitialAd(parent, facebook_interestitial_id);

            mInterstitialFbAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {


                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    checkIfVideoIsPlayed(parent);
                }

                @Override
                public void onError(Ad ad, AdError adError) {


                }

                @Override
                public void onAdLoaded(Ad ad) {

                    mInterstitialFbAd.show();
                    Log.d("Adloaded====","true");
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });

            mInterstitialFbAd.loadAd();

        } else {

        }

    }

    private void checkIfVideoIsPlayed(Activity activity){
        Fragment currentFragment = ((AppCompatActivity)activity).getSupportFragmentManager().findFragmentById(R.id.frame_home);
        if (AllVideoPlayingFragment.exoPlayer!=null && currentFragment instanceof AllVideoPlayingFragment ) {
            AllVideoPlayingFragment.playerView.onResume();
            AllVideoPlayingFragment.exoPlayer.setPlayWhenReady(true);
        }
        if (ChannelSubcategoryFragment.exoPlayer!=null && currentFragment instanceof ChannelSubcategoryFragment ) {
            ChannelSubcategoryFragment.playerView.onResume();
            ChannelSubcategoryFragment.exoPlayer.setPlayWhenReady(true);
        }
        if (VideoPlayerActivity.exoPlayer!=null) {
            VideoPlayerActivity.videoView.onResume();
            VideoPlayerActivity.exoPlayer.setPlayWhenReady(true);
        }
    }
}

