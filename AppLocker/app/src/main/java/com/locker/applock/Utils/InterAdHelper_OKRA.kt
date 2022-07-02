package com.screen.mirror.Utils

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.locker.applock.BuildConfig
import com.locker.applock.R


class InterAdHelper_OKRA {

    companion object {
        var adCallBack: AdLoadCallBack? = null
        @JvmStatic
        var isInterAdVisible=false
        @JvmStatic
        var interstitialAdGoogle: InterstitialAd? = null

        @JvmStatic
        fun loadAdmobInterstitial(context: Context) {
            if(!isAppInstalledFromPlay(context))
                return

            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
            InterstitialAd.load(
                context,
                context.resources.getString(R.string.admob_interstitial),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        super.onAdLoaded(ad)
                        interstitialAdGoogle = ad

                        interstitialAdGoogle?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    // Log.d(TAG, 'Ad was dismissed.')
//                                    adCallBack?.adClosed()

                                        loadAdmobInterstitial(context)

                                    isInterAdVisible=false
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    Log.d("TAG", "Ad failed to show.")
//                                    adCallBack?.adClosed()
                                    isInterAdVisible=false
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Log.d(TAG, 'Ad showed fullscreen content.')
                                    interstitialAdGoogle = null;
                                }
                            }
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
//                        adCallBack?.adClosed()
                        Log.d("addloadfailederror", p0.toString())

                        interstitialAdGoogle = null
                    }
                })
        }


        @JvmStatic
        fun showAdmobIntersitial(activity: Activity) {
            if (interstitialAdGoogle != null) {
                Log.e("Full", "Full ADS ")
                interstitialAdGoogle?.show(activity)
//                adCallBack=activity as AdLoadCallBack
                isInterAdVisible=true
            } else {
                Log.d("TAG", "showFullAdAdmob: add null")
//                adCallBack=activity as AdLoadCallBack
//                adCallBack?.adClosed()
            }
        }






        @JvmStatic
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

                //return true
            }
        }
      /*  @JvmStatic
        fun showFbInterstitialAd(callBack: AdLoadCallBack) {

            if (fbInterstitialAd != null && fbInterstitialAd?.isAdLoaded == true) {
                Log.d(TAG, "showAdd: ")
                fbInterstitialAd?.show()
                adCallBack = callBack
            } else {
                callBack.adClosed()
            }
        }*/






        @JvmStatic
        fun getAdmobBannerAdSize(mContext: Activity): com.google.android.gms.ads.AdSize {
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

    }


}

