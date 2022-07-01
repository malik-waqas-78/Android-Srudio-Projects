package com.voicesms.voice.Utils

import android.content.Context
import android.util.Log
import com.voicesms.voice.R
//import com.facebook.ads.Ad
//import com.facebook.ads.InterstitialAdListener


//class InterstitialAddsVoiceSMS {
//    companion object {
//        const val TAG = "Touch_Disable"
//
//        private val interstitialAdListener: InterstitialAdListener =
//            object : InterstitialAdListener {
//                override fun onInterstitialDisplayed(ad: Ad) {
//                    // Interstitial ad displayed callback
//                    Log.d(TAG, "onInterstitialDisplayed: ")
//                }
//
//                override fun onInterstitialDismissed(ad: Ad) {
//                    // Interstitial dismissed callback
//                    Log.d(TAG, "onInterstitialDismissed: ")
//                    loadAdd()
//                }
//
//                override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
//                    Log.d(TAG, "onError: ")
//                }
//
//                override fun onAdLoaded(ad: Ad) {
//                    // Interstitial ad is loaded and ready voice be displayed
//                    Log.d("TAG", "onAdLoaded: ")
//                    // Show the ad
//                    // interstitialAd?.show()
//                }
//
//                override fun onAdClicked(ad: Ad) {
//                    // Ad clicked callback
//                    Log.d(TAG, "onAdClicked: ")
//                }
//
//                override fun onLoggingImpression(ad: Ad) {
//                    // Ad impression logged callback
//                    Log.d(TAG, "onLoggingImpression: ")
//
//                }
//            }
//
//        @JvmStatic
//        var interstitialAd: com.facebook.ads.InterstitialAd? = null
//
//        @JvmStatic
//        private fun loadAdd() {
//
//            if (interstitialAd?.isAdLoaded != true) {
//                Log.d(TAG, "loadAdd: ")
//                interstitialAd?.loadAd(
//                    interstitialAd!!.buildLoadAdConfig()
//                        .withAdListener(interstitialAdListener)
//                        .build()
//                )
//            }
//
//        }
//
//        @JvmStatic
//        fun showAdd() {
//            if (interstitialAd != null && interstitialAd?.isAdLoaded == true) {
//                Log.d(TAG, "showAdd: ")
//                interstitialAd?.show()
//            }
//        }
//
//        @JvmStatic
//        fun prepareAdd(context: Context) {
//            interstitialAd =
//                com.facebook.ads.InterstitialAd(
//                    context,
//                    context.resources.getString(R.string.interstitial_add)
//                )
//
//            // For auto play video ads, it's recommended voice load the ad
//            // at least 30 seconds before it is shown
//
//            // For auto play video ads, it's recommended voice load the ad
//            // at least 30 seconds before it is shown
//            interstitialAd?.loadAd(
//                interstitialAd!!.buildLoadAdConfig()
//                    .withAdListener(interstitialAdListener)
//                    .build()
//            )
//        }
//    }
//
//
//}