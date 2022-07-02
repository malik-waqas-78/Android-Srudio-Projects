package com.zak.clone.zakads


class ZakIntersitialAdHelper {
    companion object {

       /* var interstitialAdGoogle: InterstitialAd? = null
        var adCallBack:AdLoadCallBack?=null
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
                                    loadAdmobInterstitial(context)
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
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
        fun showAdmobIntersitial(activity: Activity) {
            if (interstitialAdGoogle != null) {
                Log.e("Full", "Full ADS ")
                interstitialAdGoogle?.show(activity)
            } else {
                Log.d(TAG, "showFullAdAdmob: add null")
            }
        }



        var starEndInterstitialAdGoogle: InterstitialAd? = null
        var starEndAdCallBack:AdLoadCallBack?=null
        @JvmStatic
        fun loadStartEndAdmobInterstitial(context: Context) {
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
                    }
                })
        }


        @JvmStatic
        fun showStartEndAdmobInterstitial(activity: Activity) {
            if (starEndInterstitialAdGoogle != null) {
                Log.e("Full", "Full ADS ")
                starEndInterstitialAdGoogle?.show(activity)
            } else {
                Log.d(TAG, "showFullAdAdmob: add null")
            }
        }


        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.d(TAG, "onInterstitialDisplayed: ")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                Log.d(TAG, "onInterstitialDismissed: ")
                loadAdd()
                adCallBack?.adClosed()
            }

            override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
                Log.d(TAG, "onError: ")
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


                    Log.d(TAG, "loadAdd: ")
                    interstitialAd?.loadAd(
                        interstitialAd!!.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build()
                    )


        }

        @JvmStatic
        fun showAdd() {
            if (interstitialAd != null && interstitialAd?.isAdLoaded == true) {
                Log.d(TAG, "showAdd: ")
                interstitialAd?.show()
            }else{
                adCallBack?.adClosed()
            }
        }

        @JvmStatic
        fun prepareAdd(context: Context) {
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
        }*/

    }
}