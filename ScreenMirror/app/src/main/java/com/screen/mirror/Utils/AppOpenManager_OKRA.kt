package com.screen.mirror.Utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner


import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.screen.mirror.MyApplication_OKRA
import com.screen.mirror.R
import com.screen.mirror.Utils.Constants_CA.TAG
import java.util.*


class AppOpenManager_OKRA(var myApplication: MyApplication_OKRA?) : Application.ActivityLifecycleCallbacks,LifecycleObserver{

    private val LOG_TAG = "AppOpenManager"

    private var appOpenAd: AppOpenAd? = null
    private var currentActivity:Activity?=null
    private var loadCallback: AppOpenAdLoadCallback? = null
    private var loadTime: Long = 0
    init {
        myApplication?.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
        Log.d(LOG_TAG, "onStart")
    }

    /** Request an ad  */
    fun fetchAd() {
        // We will implement this below.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =object: AppOpenAd.AppOpenAdLoadCallback() {
                /**
                 * Called when an app open ad has loaded.
                 *
                 * @param ad the loaded app open ad.
                 */
                override fun onAdLoaded( ad:AppOpenAd) {
                    appOpenAd = ad;
                    loadTime = Date().time
                }

                /**
                 * Called when an app open ad has failed to load.
                 *
                 * @param loadAdError the error.
                 */
                override fun onAdFailedToLoad( loadAdError: LoadAdError) {
                    // Handle the error.
                    Log.d(TAG, "onAdFailedToLoad: ")
                }


            };
        val request = getAdRequest();
        AppOpenAd.load(
            myApplication,  myApplication?.resources?.getString(R.string.app_open), request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    private var isShowingAd = false

    /** Shows the ad if one isn't already showing.  */
    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        if(currentActivity?.applicationContext?.let {
                                InterAdHelper_OKRA.isAppInstalledFromPlay(
                                    it
                                )
                            } == true) {
                            fetchAd()
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
                    }
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback

            appOpenAd?.show(currentActivity)
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            fetchAd()
        }
    }

    /** Creates and returns ad request.  */
    private fun getAdRequest(): AdRequest? {
        return AdRequest.Builder().build()
    }

    /** Utility method that checks if ad exists and can be shown.  */
    fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)&&!InterAdHelper_OKRA.isInterAdVisible
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity;
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity;

    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null;
    }
}
