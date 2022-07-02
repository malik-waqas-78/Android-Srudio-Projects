package  com.screen.mirror.Utils


import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*


import com.facebook.ads.*
import com.facebook.ads.AdError
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.locker.applock.R


import java.util.*
import kotlin.collections.ArrayList


class NativeAdHelper_OKRA {
    
    companion object {

        var googleNativeAd:com.google.android.gms.ads.nativead.NativeAd?=null
        @JvmStatic
        private fun prepareAdMobNativeAdView(nativeAd: com.google.android.gms.ads.nativead.NativeAd, googleAdView: NativeAdView) {
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
        @JvmStatic
        public fun loadAdmobNativeAd(context: Context) {

            if(!InterAdHelper_OKRA.isAppInstalledFromPlay(context))
                return

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

                    Log.d("addloadfailederror", loadAdError.toString())
                    val error =
                        """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
                }
            }).build()

            adLoader.loadAd(AdRequest.Builder().build())
        }
        @JvmStatic
        fun showAdmobNativeAd(context: Context,frameLayout: FrameLayout){

            val adView = LayoutInflater.from(context)
                .inflate(R.layout.admob_nativead, null) as NativeAdView
           if(googleNativeAd ==null){
               frameLayout.visibility=View.GONE
           }else{
               prepareAdMobNativeAdView(googleNativeAd!!, adView)
           }
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
        }

    }

}