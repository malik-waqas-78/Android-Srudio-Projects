package com.data.usage.manager.adds

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.util.Log
import com.data.usage.manager.R
import com.data.usage.manager.constants.Constants.get.TAG
import com.facebook.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class IntersitialAdds() {
    companion object {

        var interstitialAdGoogle: com.google.android.gms.ads.interstitial.InterstitialAd? = null
        @JvmStatic
        fun loadAdmobInterstitial(context: Context) {
            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
            com.google.android.gms.ads.interstitial.InterstitialAd.load(
                context,
                context.resources.getString(R.string.admob_interstitial_ad),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: com.google.android.gms.ads.interstitial.InterstitialAd) {
                        super.onAdLoaded(ad)
                        interstitialAdGoogle = ad

                        interstitialAdGoogle?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    // Log.d(TAG, 'Ad was dismissed.')
                                    loadAdmobInterstitial(context)
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError?) {
                                    //  Log.d(TAG, 'Ad failed to show.')
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Log.d(TAG, 'Ad showed fullscreen content.')
                                    interstitialAdGoogle = null;
                                }
                            }
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        interstitialAdGoogle = null
                    }
                })
        }


        @JvmStatic
        fun showFullAdAdmob(activity: Activity) {
            if (interstitialAdGoogle != null) {
                Log.e("Full", "Full ADS ")
                interstitialAdGoogle?.show(activity)
            } else {
                Log.d(TAG, "showFullAdAdmob: add null")
            }
        }


        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback

            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                loadAdd()

            }

            override fun onError(ad: Ad, adError: AdError) {
                // Ad error callback
                Log.d("TAG", "onAdLoaded: ")
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("TAG", "onAdLoaded: ")
                // Show the ad
                // interstitialAd?.show()
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback

            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback

            }
        }

        @JvmStatic
        var interstitialAd: InterstitialAd? = null

        @JvmStatic
        fun loadAdd() {
            if (interstitialAd?.isAdLoaded != true) {
                interstitialAd!!.loadAd(
                    interstitialAd!!.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build()
                )
            }
        }

        @JvmStatic
        fun showAdd() {
            if (interstitialAd != null && interstitialAd?.isAdLoaded == true) {
                interstitialAd?.show()
            }
        }

        @JvmStatic
        fun prepareAdd(context: Context) {
            interstitialAd =
                InterstitialAd(context!!, context?.resources.getString(R.string.interstitial_add))


            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown

            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown
            interstitialAd!!.loadAd(
                interstitialAd!!.buildLoadAdConfig()
                    .withAdListener(interstitialAdListener)
                    .build()
            )
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


        var nativeAd: NativeAd? = null

        @JvmStatic
        fun loadNativeAd(mContext: Context) {

            var flag1 = false

            nativeAd = NativeAd(mContext, mContext.resources.getString(R.string.native_add))

            /* nativeAd!!.setAdListener(object : NativeAdListener {

                 override fun onMediaDownloaded(ad: Ad) {}

                 override fun onError(ad: Ad, adError: AdError) {
                     Log.e("ExerciseListActivity", "" + adError)
                 }

                 override fun onAdLoaded(ad: Ad) {
                     // Race condition, load() called again before last ad was displayed
                     if (nativeAd == null || nativeAd !== ad) {
                         return
                     }
                 }

                 override fun onAdClicked(ad: Ad) {

                 }

                 override fun onLoggingImpression(ad: Ad) {

                 }
             })*/

            // Request an ad
            nativeAd?.loadAd()

        }


        fun isAppInstalledFromPlay(mContext: Context): Boolean {
            if (BuildConfig.DEBUG) {
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
                return true
            } else {
                try {
                    val applicationInfo: ApplicationInfo =
                        mContext.packageManager.getApplicationInfo(
                            mContext.applicationInfo.packageName,
                            0
                        )
                    return "com.android.vending" == mContext.packageManager.getInstallerPackageName(
                        applicationInfo.packageName
                    )
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                    return false
                }
            }
        }

    }

    }


