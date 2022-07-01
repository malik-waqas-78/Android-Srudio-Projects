package com.phoneclone.data.ads

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log

import com.facebook.ads.Ad
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


import com.phoneclone.data.R
import com.phoneclone.data.constants.MyConstants.get.TAG


class IntersitialAdHelper {
    companion object {

        var interstitialAdGoogle: InterstitialAd? = null
        var adCallBack:AdLoadCallBack?=null
        var isInterAdVisible=false
        @JvmStatic
        fun loadAdmobInterstitial(context: Context) {
            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
            InterstitialAd.load(
                context,
                context.resources.getString(R.string.admob_interstitial_ad),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        super.onAdLoaded(ad)
                        interstitialAdGoogle = ad

                        interstitialAdGoogle?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    // Log.d(TAG, 'Ad was dismissed.')
                                    adCallBack?.adClosed()
                                    isInterAdVisible=false
                                    loadAdmobInterstitial(context)
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    //  Log.d(TAG, 'Ad failed to show.')
                                    isInterAdVisible=false
                                    adCallBack?.adClosed()
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Log.d(TAG, 'Ad showed fullscreen content.')
                                    interstitialAdGoogle = null;
                                    isInterAdVisible=true
                                }
                            }
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        interstitialAdGoogle = null
                        isInterAdVisible=false
                        adCallBack?.adClosed()
                    }
                })
        }


        @JvmStatic
        fun showAdmobIntersitial(activity: Activity) {
            if (interstitialAdGoogle != null&& isAppInstalledFromPlay(activity)) {
                Log.e("Full", "Full ADS ")
                interstitialAdGoogle?.show(activity)
                isInterAdVisible=true
            } else {
                Log.d(TAG, "showFullAdAdmob: add null")
                isInterAdVisible=false
                adCallBack?.adClosed()
            }
        }



        var starEndInterstitialAdGoogle: InterstitialAd? = null
        var starEndAdCallBack:AdLoadCallBack?=null
        @JvmStatic
        fun loadStartEndAdmobInterstitial(context: Context) {

            if(!isAppInstalledFromPlay(context)){
                return
            }

            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
            InterstitialAd.load(
                context,
                context.resources.getString(R.string.satrt_end_inter_admob),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        super.onAdLoaded(ad)
                        starEndInterstitialAdGoogle = ad

                        starEndInterstitialAdGoogle?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    // Log.d(TAG, 'Ad was dismissed.')
                                    starEndAdCallBack?.adClosed()
                                    loadStartEndAdmobInterstitial(context)
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    //  Log.d(TAG, 'Ad failed to show.')
                                    starEndAdCallBack?.adClosed()
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Log.d(TAG, 'Ad showed fullscreen content.')
                                    starEndInterstitialAdGoogle = null;
                                }
                            }
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        starEndAdCallBack = null
                        starEndAdCallBack?.adClosed()
                    }
                })
        }


        @JvmStatic
        fun showStartEndAdmobInterstitial(activity: Activity) {
            if (starEndInterstitialAdGoogle != null&& isAppInstalledFromPlay(activity)) {
                Log.e("Full", "Full ADS ")
                starEndInterstitialAdGoogle?.show(activity)
                isInterAdVisible=true
            } else {
                Log.d(TAG, "showFullAdAdmob: add null")
                isInterAdVisible=false
                starEndAdCallBack?.adClosed()
            }
        }


        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.d(TAG, "onInterstitialDisplayed: ")
                isInterAdVisible=true
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                Log.d(TAG, "onInterstitialDismissed: ")
                loadAdd()
                adCallBack?.adClosed()
                isInterAdVisible=false
            }

            override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
                Log.d(TAG, "onError: ")
                isInterAdVisible=false
                adCallBack?.adClosed()
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("TAG", "onAdLoaded: ")
                // Show the ad
                // interstitialAd?.show()
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
                Log.d(TAG, "onAdClicked: ")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d(TAG, "onLoggingImpression: ")

            }
        }

        @JvmStatic
        var interstitialAd: com.facebook.ads.InterstitialAd? = null

        @JvmStatic
        fun loadAdd() {

                if (interstitialAd?.isAdLoaded != true) {
                    Log.d(TAG, "loadAdd: ")
                    interstitialAd?.loadAd(
                        interstitialAd!!.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build()
                    )
                }

        }

        @JvmStatic
        fun showAdd(context: Context) {
            if (interstitialAd != null && interstitialAd?.isAdLoaded == true&& isAppInstalledFromPlay(context)) {
                Log.d(TAG, "showAdd: ")
                interstitialAd?.show()
                isInterAdVisible=true
            }else{
                adCallBack?.adClosed()
                isInterAdVisible=false
            }
        }

        @JvmStatic
        fun prepareAdd(context: Context) {
            if(isAppInstalledFromPlay(context)){
                interstitialAd =
                    com.facebook.ads.InterstitialAd(
                        context!!,
                        context?.resources.getString(R.string.interstitial_add)
                    )


                // For auto play video ads, it's recommended to load the ad
                // at least 30 seconds before it is shown

                // For auto play video ads, it's recommended to load the ad
                // at least 30 seconds before it is shown
                interstitialAd?.loadAd(
                    interstitialAd!!.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build()
                )
            }
        }


        @JvmStatic
        fun getAdSize(mContext: Activity): com.google.android.gms.ads.AdSize {
            // Step 2 - Determine the screen width (less decorations) to use for the ad width.
            val display = mContext.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val widthPixels = outMetrics.widthPixels.toFloat()
            val density = outMetrics.density

            val adWidth = (widthPixels / density).toInt()

            // Step 3 - Get adaptive ad size and return for setting on the ad view.
            return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                mContext,
                adWidth
            )
        }


        interface  AdLoadCallBack{
            fun adClosed()
        }

    }




}