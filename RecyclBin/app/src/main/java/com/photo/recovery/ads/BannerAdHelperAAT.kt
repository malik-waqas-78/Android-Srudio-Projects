package com.photo.recovery.ads

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.RelativeLayout


import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.photo.recovery.R


class BannerAdHelperAAT {
    companion object{
        @JvmStatic
        fun loadNormalAdmobBanner(context: Context, view:RelativeLayout) {

            if(!isAppInstalledFromPlay(context))
                return


            val mAdView = com.google.android.gms.ads.AdView(context)
            val adSize: com.google.android.gms.ads.AdSize = getAdmobBannerAdSize(context as Activity)
            mAdView.adSize = adSize

            mAdView.adUnitId = context.resources.getString(R.string.admob_normal_banner)

            val adRequest = AdRequest.Builder().build()

            val adViewLayout =view
            adViewLayout.addView(mAdView)

            mAdView.loadAd(adRequest)

            mAdView.adListener = object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                }

                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    super.onAdFailedToLoad(p0)
                    adViewLayout.visibility = View.INVISIBLE
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adViewLayout.visibility = View.VISIBLE
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }
            }

        }
        @JvmStatic
        fun loadMediatedAdmobBanner(context: Context, view:RelativeLayout) {

            if(!isAppInstalledFromPlay(context))
                return


            val mAdView = com.google.android.gms.ads.AdView(context)
            val adSize: com.google.android.gms.ads.AdSize = getAdmobBannerAdSize(context as Activity)
            mAdView.adSize = adSize

            mAdView.adUnitId = context.resources.getString(R.string.admob_mediated_banner)

            val adRequest = AdRequest.Builder().build()

            val adViewLayout =view
            adViewLayout.addView(mAdView)

            mAdView.loadAd(adRequest)

            mAdView.adListener = object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                }

                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    super.onAdFailedToLoad(p0)
                    adViewLayout.visibility = View.INVISIBLE
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adViewLayout.visibility = View.VISIBLE
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }
            }

        }

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
    }
}