package com.data.usage.manager.adds


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.data.usage.manager.constants.Constants.get.TAG
import com.facebook.ads.*
import com.data.usage.manager.R;

class NativeAdds {

    companion object {
        var mynativeAds: NativeAd? = null
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
                   /* if (mynativeAds == null || mynativeAds != ad) {
                        return;
                    }*/
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

   fun showAd(context: Context,nativeAdLayout: NativeAdLayout,nativeAd: NativeAd) {
      /*  if(nativeAds==null){
           // prepareAdd(context)
            return
        }*/
       if(nativeAd==null||!nativeAd.isAdLoaded){
           nativeAdLayout.visibility=View.GONE
           return
       }
       nativeAd?.unregisterView()

        // Add the Ad view into the ad container.
        //nativeAdLayout = findViewById(R.id.native_ad_container)
        val inflater = LayoutInflater.from(context)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        val adView =
            inflater.inflate(R.layout.fb_native_layout, nativeAdLayout, false) as LinearLayout
        nativeAdLayout.addView(adView)

        // Add the AdOptionsView
        val adChoicesContainer: LinearLayout = adView.findViewById(R.id.ad_choices_container)
        val adOptionsView = AdOptionsView(context, nativeAd, nativeAdLayout)
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
        nativeAdTitle.text = nativeAd?.advertiserName
        nativeAdBody.text = nativeAd?.adBodyText
        nativeAdSocialContext.text = nativeAd?.adSocialContext
       nativeAdCallToAction.visibility = if (nativeAd?.hasCallToAction() == true) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction.setText(nativeAd?.adCallToAction)
        sponsoredLabel.text = nativeAd?.sponsoredTranslation

        // Create a list of clickable views
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
       nativeAd?.registerViewForInteraction(
            adView, nativeAdMedia, nativeAdIcon, clickableViews
        )
       Log.d(TAG, "showAd: ")
    }
}

}