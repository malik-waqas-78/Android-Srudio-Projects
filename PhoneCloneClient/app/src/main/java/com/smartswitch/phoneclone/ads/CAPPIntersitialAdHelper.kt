package com.smartswitch.phoneclone.ads
//
//import android.app.Activity
//import android.content.Context
//import android.content.pm.PackageManager
//import android.util.DisplayMetrics
//import android.util.Log
//import android.view.View
//import android.widget.RelativeLayout
//import com.google.android.gms.ads.*
//import com.google.android.gms.ads.interstitial.InterstitialAd
//import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
//import com.switchphone.transferdata.BuildConfig
//import com.switchphone.transferdata.R
//
//
//class AATIntersitialAdHelper {
//    companion object {
//
//        var interstitialAdGoogle: InterstitialAd? = null
//        var adCallBack: AdLoadCallBack? = null
//
//        @JvmStatic
//        fun loadAdmobInterstitial_mediated(context: Context) {
//            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
//            InterstitialAd.load(
//                context,
//                context.resources.getString(R.string.admob_interstitial_mediated),
//                adRequest,
//                object : InterstitialAdLoadCallback() {
//                    override fun onAdLoaded(ad: InterstitialAd) {
//                        super.onAdLoaded(ad)
//                        interstitialAdGoogle = ad
//
//                        interstitialAdGoogle?.fullScreenContentCallback =
//                            object : FullScreenContentCallback() {
//                                override fun onAdDismissedFullScreenContent() {
//                                    // Log.d(TAG, 'Ad was dismissed.')
//                                    loadAdmobInterstitial_mediated(context)
//                                    adCallBack?.adClosed()
//
//                                }
//
//                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
//                                    //  Log.d(TAG, 'Ad failed to show.')
//                                }
//
//                                override fun onAdShowedFullScreenContent() {
//                                    // Log.d(TAG, 'Ad showed fullscreen content.')
//                                    interstitialAdGoogle = null;
//                                    adCallBack?.adClosed()
//                                }
//                            }
//                    }
//
//                    override fun onAdFailedToLoad(p0: LoadAdError) {
//                        super.onAdFailedToLoad(p0)
//                        interstitialAdGoogle = null
//                        adCallBack?.adClosed()
//                    }
//                })
//        }
//
//
//        @JvmStatic
//        fun showAdmobIntersitial_mediated(activity: Activity, adcallback: AdLoadCallBack?) {
//            adCallBack = adcallback
//            if (interstitialAdGoogle != null) {
//                Log.e("Full", "Full ADS ")
//                interstitialAdGoogle?.show(activity)
//            } else {
//                adCallBack?.adClosed()
//                Log.d("Full", "showFullAdAdmob: add null")
//            }
//        }
//
//
//        var interstitialAdGoogle_simple: InterstitialAd? = null
//
//        @JvmStatic
//        fun loadAdmobInterstitial_simple(context: Context) {
//            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
//            InterstitialAd.load(
//                context,
//                context.resources.getString(R.string.admob_interstitial_simple),
//                adRequest,
//                object : InterstitialAdLoadCallback() {
//                    override fun onAdLoaded(ad: InterstitialAd) {
//                        super.onAdLoaded(ad)
//                        interstitialAdGoogle_simple = ad
//
//                        interstitialAdGoogle_simple?.fullScreenContentCallback =
//                            object : FullScreenContentCallback() {
//                                override fun onAdDismissedFullScreenContent() {
//                                    // Log.d(TAG, 'Ad was dismissed.')
//                                    loadAdmobInterstitial_simple(context)
//                                }
//
//                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
//                                    //  Log.d(TAG, 'Ad failed to show.')
//                                }
//
//                                override fun onAdShowedFullScreenContent() {
//                                    // Log.d(TAG, 'Ad showed fullscreen content.')
//                                    interstitialAdGoogle_simple = null;
//                                }
//                            }
//                    }
//
//                    override fun onAdFailedToLoad(p0: LoadAdError) {
//                        super.onAdFailedToLoad(p0)
//                    }
//                })
//        }
//
//
//        @JvmStatic
//        fun showAdmobInterstitial_simple(activity: Activity) {
//            if (interstitialAdGoogle_simple != null) {
//                Log.e("Full", "Full ADS ")
//                interstitialAdGoogle_simple?.show(activity)
//            } else {
//                Log.d("Full_simple", "showFullAdAdmob: add null")
//            }
//        }
//
//
//        @JvmStatic
//        fun getAdSize(mContext: Activity): com.google.android.gms.ads.AdSize {
//            // Step 2 - Determine the screen width (less decorations) to use for the ad width.
//            val display = mContext.windowManager.defaultDisplay
//            val outMetrics = DisplayMetrics()
//            display.getMetrics(outMetrics)
//
//            val widthPixels = outMetrics.widthPixels.toFloat()
//            val density = outMetrics.density
//
//            val adWidth = (widthPixels / density).toInt()
//
//            // Step 3 - Get adaptive ad size and return for setting on the ad view.
//            return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
//                mContext,
//                adWidth
//            )
//        }
//
//
//        interface AdLoadCallBack {
//            fun adClosed()
//        }
//
//
//        fun isAppInstalledFromPlay(mContext: Context): Boolean {
//            return if (BuildConfig.DEBUG) {
//                /* try{
//                            val applicationInfo: ApplicationInfo =
//                                mContext.packageManager.getApplicationInfo(
//                                    "com.phoneclone.oldtonewphone.data.transfer.unlimited",//com.phoneclone.oldtonewphone
//                                    0
//                                )
//                            return "com.android.vending" == mContext.packageManager.getInstallerPackageName(
//                                applicationInfo.packageName
//                            )
//                        }catch (e:PackageManager.NameNotFoundException){
//                            e.printStackTrace()
//                            return false
//                        }*/
//                return false
//            } else {
//                try {
//                    val applicationInfo = mContext.packageManager.getApplicationInfo(
//                        mContext.applicationInfo.packageName,
//                        0
//                    )
//                    return "com.android.vending".equals(
//                        mContext.packageManager.getInstallerPackageName(
//                            applicationInfo.packageName
//                        )
//                    );
//
//
//                } catch (e: PackageManager.NameNotFoundException) {
//                    e.printStackTrace()
//                    return false
//                }
//            }
//        }
//
//
//        @JvmStatic
//        fun loadAdmobBanner(context: Context, view: RelativeLayout, bannerAdId: String) {
//
//            if (!isAppInstalledFromPlay(context))
//                return
//
//
//            val mAdView = AdView(context)
//            val adSize: AdSize = getAdSize(context as Activity)
//            mAdView.adSize = adSize
//
//            mAdView.adUnitId = bannerAdId
//
//            val adRequest = AdRequest.Builder().build()
//
//            val adViewLayout = view
//            adViewLayout.addView(mAdView)
//
//            mAdView.loadAd(adRequest)
//
//            mAdView.adListener = object : AdListener() {
//                override fun onAdClosed() {
//                    super.onAdClosed()
//                }
//
//                override fun onAdFailedToLoad(p0: LoadAdError?) {
//                    super.onAdFailedToLoad(p0)
//                    adViewLayout.visibility = View.INVISIBLE
//                }
//
//                override fun onAdOpened() {
//                    super.onAdOpened()
//                }
//
//                override fun onAdLoaded() {
//                    super.onAdLoaded()
//                    adViewLayout.visibility = View.VISIBLE
//                }
//
//                override fun onAdClicked() {
//                    super.onAdClicked()
//                }
//
//                override fun onAdImpression() {
//                    super.onAdImpression()
//                }
//            }
//
//        }
//
//
//    }
//}