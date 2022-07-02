package com.photo.recovery.ads

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.photo.recovery.BuildConfig
import com.photo.recovery.R


/*import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest*/


class InterAdHelperAAT {
    companion object {

        var normalInterstitialAdGoogle: InterstitialAd? = null
        var isInterAdVisible=false
        var adCallBack: AdLoadCallBack?=null
        @JvmStatic
        fun loadNormalAdmobInterstitial(context: Context) {

            if (!isAppFromPlayStore) {
                return
            }

            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
            InterstitialAd.load(
                context,
                context.resources.getString(R.string.admob_normal_interstitial_ad),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        super.onAdLoaded(ad)
                        normalInterstitialAdGoogle = ad

                        normalInterstitialAdGoogle?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    // Log.d(TAG, 'Ad was dismissed.')
                                    loadNormalAdmobInterstitial(context)
                                    adCallBack?.adClosed()
                                    isInterAdVisible =false
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    //  Log.d(TAG, 'Ad failed to show.')
                                    adCallBack?.adClosed()
                                    isInterAdVisible =false

                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Log.d(TAG, 'Ad showed fullscreen content.')
                                    normalInterstitialAdGoogle = null;
                                    isInterAdVisible =true
                                }
                            }
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        normalInterstitialAdGoogle = null
                        adCallBack?.adClosed()
                        isInterAdVisible =false
                    }
                })
        }


        @JvmStatic
        fun showNormalAdmobIntersitial(activity: Activity) {
            if (normalInterstitialAdGoogle != null) {
                Log.e("Full", "Full ADS ")
                normalInterstitialAdGoogle?.show(activity)
                isInterAdVisible =true
            } else {
                Log.d("waqas", "showFullAdAdmob: add null")
                adCallBack?.adClosed()
                isInterAdVisible =false
            }
        }


        var mediatedInterstitialAdGoogle: InterstitialAd? = null

        @JvmStatic
        fun loadMediatedAdmobInterstitial(context: Context) {

            if (!isAppFromPlayStore) {
                return
            }

            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
            InterstitialAd.load(
                context,
                context.resources.getString(R.string.admob_mediated_interstitial_ad),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        super.onAdLoaded(ad)
                        mediatedInterstitialAdGoogle = ad

                        mediatedInterstitialAdGoogle?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    // Log.d(TAG, 'Ad was dismissed.')
                                    loadMediatedAdmobInterstitial(context)
                                    adCallBack?.adClosed()
                                    isInterAdVisible =false
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    //  Log.d(TAG, 'Ad failed to show.')
                                    isInterAdVisible =false
                                    adCallBack?.adClosed()
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Log.d(TAG, 'Ad showed fullscreen content.')
                                    mediatedInterstitialAdGoogle = null;
                                    isInterAdVisible =true
                                }
                            }
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        mediatedInterstitialAdGoogle = null
                        adCallBack?.adClosed()
                        isInterAdVisible =false
                    }
                })
        }


        @JvmStatic
        fun showMediatedAdmobIntersitial(activity: Activity) {

            if (mediatedInterstitialAdGoogle != null) {
                Log.e("Full", "Full ADS ")
                mediatedInterstitialAdGoogle?.show(activity)
                isInterAdVisible =true
            } else {
                Log.d("waqas", "showFullAdAdmob: add null")
                adCallBack?.adClosed()
                isInterAdVisible =false
            }
        }

        /*@JvmStatic
        private val fbInterAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.d("TAG", "onInterstitialDisplayed: ")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                //  Log.d(TAG, "onInterstitialDismissed: ")
                loadAd()
            }

            override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
                //  Log.d(TAG, "onError: ")
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("TAG", "onAdLoaded: ")
                // Show the ad
                // interstitialAd?.show()
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
                //    Log.d(TAG, "onAdClicked: ")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                //     Log.d(TAG, "onLoggingImpression: ")

            }
        }

        @JvmStatic
        var fbInterAd: com.facebook.ads.InterstitialAd? = null

        @JvmStatic
        private fun loadAd() {

            if (isAppFromPlayStore && fbInterAd?.isAdLoaded != true) {
                //       Log.d(TAG, "loadAdd: ")
                fbInterAd?.loadAd(
                    fbInterAd!!.buildLoadAdConfig()
                        .withAdListener(fbInterAdListener)
                        .build()
                )
            }

        }

        @JvmStatic
        fun showFbInterAd(context: Context) {
            if (isAppInstalledFromPlay(context) && fbInterAd != null && fbInterAd?.isAdLoaded == true && fbInterAd?.isAdInvalidated == false) {
                //    Log.d(TAG, "showAdd: ")
                fbInterAd?.show()
            }
        }

        @JvmStatic
        fun prepareFbInterAd(context: Context) {
            fbInterAd =
                com.facebook.ads.InterstitialAd(
                    context!!,
                    context?.resources.getString(R.string.fb_inter_ad)
                )


            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown

            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown
            fbInterAd?.loadAd(
                fbInterAd!!.buildLoadAdConfig()
                    .withAdListener(fbInterAdListener)
                    .build()
            )
        }*/

        /*  @JvmStatic
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
  */

        interface AdLoadCallBack {
            fun adClosed()
        }

        var isAppFromPlayStore = false



    }




}
    fun isAppInstalledFromPlay(mContext: Context): Boolean {
        return if (BuildConfig.DEBUG) {
            /* try{
                        val applicationInfo: ApplicationInfo =
                            mContext.packageManager.getApplicationInfo(
                                "com.phoneclone.oldtonewphone.data.transfer.unlimited",//com.phoneclone.oldtonewphone
                                0
                            )
                        return "com.android.vending" == mContext.packageManager.getInstallerPackageName(
                            applicationInfo.packageName
                        )
                    }catch (e:PackageManager.NameNotFoundException){
                        e.printStackTrace()
                        return false
                    }*/
            InterAdHelperAAT.isAppFromPlayStore = true

            InterAdHelperAAT.isAppFromPlayStore
        } else {
            try {
                val applicationInfo = mContext.packageManager.getApplicationInfo(
                    mContext.applicationInfo.packageName,
                    0
                )
                InterAdHelperAAT.isAppFromPlayStore = "com.android.vending".equals(
                    mContext.packageManager.getInstallerPackageName(
                        applicationInfo.packageName
                    )
                );

                InterAdHelperAAT.isAppFromPlayStore

            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                InterAdHelperAAT.isAppFromPlayStore = false

                InterAdHelperAAT.isAppFromPlayStore
            }
        }
}