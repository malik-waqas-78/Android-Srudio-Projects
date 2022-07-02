package com.recovery.data.forwhatsapp

//import com.google.android.gms.ads.AdError
//import com.google.android.gms.ads.FullScreenContentCallback
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.interstitial.InterstitialAd
//import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class OKRAAdmobFullAdManager {
//    companion object {
//        var adCallBack: AdLoadCallBack? = null
//        var interstitialAdGoogle: InterstitialAd? = null
//
//        @JvmStatic
//        fun loadAdmobInterstitial(context: Context) {
//            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
//            InterstitialAd.load(
//                context,
//                context.resources.getString(R.string.admob_interstitial),
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
//                                    adCallBack?.adClosed()
//                                    loadAdmobInterstitial(context)
//                                }
//
//                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
//                                    Log.d(TAG, "Ad failed to show.")
//                                }
//
//                                override fun onAdShowedFullScreenContent() {
//                                    // Log.d(TAG, 'Ad showed fullscreen content.')
//                                    interstitialAdGoogle = null;
//                                }
//                            }
//                    }
//
//                    override fun onAdFailedToLoad(p0: LoadAdError) {
//                        super.onAdFailedToLoad(p0)
//                        interstitialAdGoogle = null
//                    }
//                })
//        }
//        @JvmStatic
//        fun showAdmobIntersitial(activity: Activity) {
//            if (interstitialAdGoogle != null) {
//                Log.e("Full", "Full ADS ")
//                interstitialAdGoogle?.show(activity)
//            } else {
//                Log.d(TAG, "showFullAdAdmob: add null")
//            }
//        }
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
//    }
//    interface AdLoadCallBack {
//        fun adClosed()
//    }
}