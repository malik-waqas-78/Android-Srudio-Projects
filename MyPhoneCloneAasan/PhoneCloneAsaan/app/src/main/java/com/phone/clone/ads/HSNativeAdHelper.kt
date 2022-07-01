package com.phone.clone.ads


/*import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView*/


class HSNativeAdHelper {

    companion object {
       /* var mynativeAds: NativeAd? = null
        fun prepareAdd(context: Context) {
            mynativeAds = NativeAd(context, context.resources.getString(R.string.native_add))
            val nativeAdListener: NativeAdListener = object : NativeAdListener {
                override fun onMediaDownloaded(ad: Ad) {
                    // Native ad finished downloading all assets
                    Log.e(TAG, "Native ad finished downloading all assets.")
                }

                override fun onError(ad: Ad?, adError: AdError) {
                    // Native ad failed to load
                    Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage())
                }

                override fun onAdLoaded(ad: Ad) {
                    // Native ad is loaded and ready to be displayed
                    // Race condition, load() called again before last ad was displayed
                    Log.d(TAG, "Native ad is loaded and ready to be displayed!")
                    if (mynativeAds == null || mynativeAds != ad) {
                        return;
                    }
                    // Inflate Native Ad into Container
                   // inflateAd(context, nativeAd!!);

                }

                override fun onAdClicked(ad: Ad) {
                    // Native ad clicked
                    Log.d(TAG, "Native ad clicked!")
                }

                override fun onLoggingImpression(ad: Ad) {
                    // Native ad impression
                    Log.d(TAG, "Native ad impression logged!")
                }
            }

            // Request an ad

            // Request an ad
            mynativeAds?.loadAd(
                mynativeAds!!.buildLoadAdConfig()
                    .withAdListener(nativeAdListener)
                    .build()
            )
        }

   fun showAd(context: Context, nativeAdLayout: NativeAdLayout) {

       if(mynativeAds==null||mynativeAds?.isAdLoaded!=true){
           nativeAdLayout.visibility= View.GONE
           return
       }
       mynativeAds?.unregisterView()
        // Add the Ad view into the ad container.
        //nativeAdLayout = findViewById(R.id.native_ad_container)
        val inflater = LayoutInflater.from(context)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        val adView =
            inflater.inflate(R.layout.fb_native_layout, nativeAdLayout, false) as LinearLayout
        nativeAdLayout.addView(adView)

        // Add the AdOptionsView
        val adChoicesContainer: LinearLayout = adView.findViewById(R.id.ad_choices_container)
        val adOptionsView = AdOptionsView(context, mynativeAds, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        val nativeAdIcon: MediaView = adView.findViewById(R.id.native_ad_icon)
        val nativeAdTitle: TextView = adView.findViewById(R.id.native_ad_title)
        val nativeAdMedia: MediaView = adView.findViewById(R.id.native_ad_media)
        val nativeAdSocialContext: TextView = adView.findViewById(R.id.native_ad_social_context)
        val nativeAdBody: TextView = adView.findViewById(R.id.native_ad_body)
        val sponsoredLabel: TextView = adView.findViewById(R.id.native_ad_sponsored_label)
        val nativeAdCallToAction: Button = adView.findViewById(R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdTitle.text = mynativeAds?.advertiserName
        nativeAdBody.text = mynativeAds?.adBodyText
        nativeAdSocialContext.text = mynativeAds?.adSocialContext
        nativeAdCallToAction.setVisibility(if (mynativeAds?.hasCallToAction() == true) View.VISIBLE else View.INVISIBLE)
        nativeAdCallToAction.setText(mynativeAds?.adCallToAction)
        sponsoredLabel.text = mynativeAds?.sponsoredTranslation

        // Create a list of clickable views
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
       mynativeAds?.registerViewForInteraction(
           adView, nativeAdMedia, nativeAdIcon, clickableViews
       )
       Log.d(TAG, "showAd: ")
    }

        *//*public var googleNativeAd:com.google.android.gms.ads.nativead.NativeAd?=null
        private fun populateNativeAdView(nativeAd: com.google.android.gms.ads.nativead.NativeAd, googleAdView: NativeAdView) {
            // Set the media view.
            googleAdView.mediaView = googleAdView.findViewById<MediaView>(R.id.ad_media) as com.google.android.gms.ads.nativead.MediaView

            // Set other ad assets.
            googleAdView.headlineView = googleAdView.findViewById(R.id.ad_headline)
            googleAdView.bodyView = googleAdView.findViewById(R.id.ad_body)
            googleAdView.callToActionView = googleAdView.findViewById(R.id.ad_call_to_action)
            googleAdView.iconView = googleAdView.findViewById(R.id.ad_app_icon)
            googleAdView.priceView = googleAdView.findViewById(R.id.ad_price)
            googleAdView.starRatingView = googleAdView.findViewById(R.id.ad_stars)
            googleAdView.storeView = googleAdView.findViewById(R.id.ad_store)
            googleAdView.advertiserView = googleAdView.findViewById(R.id.ad_advertiser)

            // The headline and media content are guaranteed to be in every UnifiedNativeAd.
            (googleAdView.headlineView as TextView).text = nativeAd.headline
            googleAdView.mediaView.setMediaContent(nativeAd.mediaContent)

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.body == null) {
                googleAdView.bodyView.visibility = View.INVISIBLE
            } else {
                googleAdView.bodyView.visibility = View.VISIBLE
                (googleAdView.bodyView as TextView).text = nativeAd.body
            }

            if (nativeAd.callToAction == null) {
                googleAdView.callToActionView.visibility = View.INVISIBLE
            } else {
                googleAdView.callToActionView.visibility = View.VISIBLE
                (googleAdView.callToActionView as Button).text = nativeAd.callToAction
            }

            if (nativeAd.icon == null) {
                googleAdView.iconView.visibility = View.GONE
            } else {
                (googleAdView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon.drawable
                )
                googleAdView.iconView.visibility = View.VISIBLE
            }

            if (nativeAd.price == null) {
                googleAdView.priceView.visibility = View.INVISIBLE
            } else {
                googleAdView.priceView.visibility = View.VISIBLE
                (googleAdView.priceView as TextView).text = nativeAd.price
            }

            if (nativeAd.store == null) {
                googleAdView.storeView.visibility = View.INVISIBLE
            } else {
                googleAdView.storeView.visibility = View.VISIBLE
                (googleAdView.storeView as TextView).text = nativeAd.store
            }

            if (nativeAd.starRating == null) {
                googleAdView.starRatingView.visibility = View.INVISIBLE
            } else {
                (googleAdView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
                googleAdView.starRatingView.visibility = View.VISIBLE
            }

            if (nativeAd.advertiser == null) {
                googleAdView.advertiserView.visibility = View.INVISIBLE
            } else {
                (googleAdView.advertiserView as TextView).text = nativeAd.advertiser
                googleAdView.advertiserView.visibility = View.VISIBLE
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad.
            googleAdView.setNativeAd(nativeAd)

            // Get the video controller for the ad. One will always be provided, even if the ad doesn't
            // have a video asset.

        }

        public fun loadAd(context: Context) {

            val builder = AdLoader.Builder(context,context.resources.getString(R.string.admob_native_ad))

            builder.forNativeAd { unifiedNativeAd ->
                // OnUnifiedNativeAdLoadedListener implementation.
                // If this callback occurs after the activity is destroyed, you must call
                // destroy and return or you may get a memory leak.
                var activityDestroyed = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    activityDestroyed = (context as Activity).isDestroyed
                }
                if (activityDestroyed || (context as Activity).isFinishing || (context as Activity).isChangingConfigurations) {
                    unifiedNativeAd.destroy()
                    return@forNativeAd
                }
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                googleNativeAd?.destroy()
                googleNativeAd = unifiedNativeAd
            }

            val videoOptions = VideoOptions.Builder()
                .setStartMuted(false)
                .build()

            val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()

            builder.withNativeAdOptions(adOptions)

            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    val error =
                        """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
                }
            }).build()

            adLoader.loadAd(AdRequest.Builder().build())
        }

        fun showAdmobNativeAd(context: Context,frameLayout: FrameLayout){

            val adView = LayoutInflater.from(context)
                .inflate(R.layout.admob_nativead, null) as NativeAdView
           if(googleNativeAd==null){
               frameLayout.visibility=View.GONE
           }else{
               populateNativeAdView(googleNativeAd!!, adView)
           }
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
        }*/

    }

}