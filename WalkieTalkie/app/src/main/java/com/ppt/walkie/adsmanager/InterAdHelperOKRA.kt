package com.walkie.talkie.ads

import android.app.Activity

import android.content.Context
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ppt.walkie.BuildConfig
import com.ppt.walkie.R


class InterAdHelperOKRA {
    companion object {

        var shouldShowFullAd = true
        var normalInterstitialAdGoogle: InterstitialAd? = null
        var isInterAdVisible = false
        var adCallBack: AdLoadCallBack? = null

        @JvmStatic
        fun loadNormalAdmobInterstitial(context: Context) {

            if (!isAppInstalledFromPlay(context)) {
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
                                    isInterAdVisible = false
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    //  Log.d(TAG, 'Ad failed to show.')
                                    adCallBack?.adClosed()
                                    isInterAdVisible = false

                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Log.d(TAG, 'Ad showed fullscreen content.')
                                    normalInterstitialAdGoogle = null;
                                    isInterAdVisible = true
                                }
                            }
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        normalInterstitialAdGoogle = null
                        isInterAdVisible = false
                        adCallBack?.adClosed()
                    }
                })
        }


        @JvmStatic
        fun showNormalAdmobIntersitial(activity: Activity) {
            if (normalInterstitialAdGoogle != null) {
                Log.e("Full", "Full ADS ")
                normalInterstitialAdGoogle?.show(activity)
                isInterAdVisible = true
            } else {
                Log.d("waqas", "showFullAdAdmob: add null")
                adCallBack?.adClosed()
                isInterAdVisible = false
            }
        }


        var mediatedInterstitialAdGoogle: InterstitialAd? = null

        @JvmStatic
        fun loadMediatedAdmobInterstitial(context: Context) {

            if (!isAppInstalledFromPlay(context)) {
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
                                    isInterAdVisible = false
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    //  Log.d(TAG, 'Ad failed to show.')
                                    isInterAdVisible = false
                                    adCallBack?.adClosed()
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Log.d(TAG, 'Ad showed fullscreen content.')
                                    mediatedInterstitialAdGoogle = null;
                                    isInterAdVisible = true
                                }
                            }
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        mediatedInterstitialAdGoogle = null
                        isInterAdVisible = false
                        adCallBack?.adClosed()
                    }
                })
        }


        @JvmStatic
        fun showMediatedAdmobIntersitial(activity: Activity, adCallBack: AdLoadCallBack?) {
            this.adCallBack = adCallBack
            if (mediatedInterstitialAdGoogle != null) {
                Log.e("Full", "Full ADS ")
                mediatedInterstitialAdGoogle?.show(activity)
                isInterAdVisible = true
            } else {
                Log.d("waqas", "showFullAdAdmob: add null")
                adCallBack?.adClosed()
                isInterAdVisible = false
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


        interface AdLoadCallBack {
            fun adClosed()
        }

//

        @JvmStatic
        fun isAppInstalledFromPlay(mContext: Context): Boolean {
            var isAppFromPlayStore = false
            return if (BuildConfig.DEBUG) {

                isAppFromPlayStore = true

                isAppFromPlayStore
            } else {
                try {
                    val applicationInfo = mContext.packageManager.getApplicationInfo(
                        mContext.applicationInfo.packageName,
                        0
                    )
                    isAppFromPlayStore = "com.android.vending".equals(
                        mContext.packageManager.getInstallerPackageName(
                            applicationInfo.packageName
                        )
                    )

                    isAppFromPlayStore

                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                    isAppFromPlayStore = false

                    isAppFromPlayStore
                }
            }
        }

    }


}
