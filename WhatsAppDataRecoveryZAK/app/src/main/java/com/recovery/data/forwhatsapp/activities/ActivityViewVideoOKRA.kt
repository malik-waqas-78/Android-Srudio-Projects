package com.recovery.data.forwhatsapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
//import com.facebook.ads.*

import com.recovery.data.forwhatsapp.R
import java.io.File
import java.util.*

class ActivityViewVideoOKRA : AppCompatActivity() {
    var videoView: VideoView? = null

    var path: String? = null
    var name:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_videos_okra)
        var videoname:TextView?
        videoView = findViewById<VideoView>(R.id.videoView)
        videoname=findViewById<TextView>(R.id.videoname)
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
//        loadFbBannerAdd()

//        AATAdsManager.displayInterstitialFacebook()
        findViewById<ImageView>(R.id.iv_share_video).setOnClickListener {
            share()
        }
        val intent = intent
        path = intent.getStringExtra("url")
        name=intent.getStringExtra("videoname")
        videoname.setText(name)
        videoname.setSelected(true)
        playVideo()
        /* loadBannerAdd()
         admobBanner()*/
    }

    fun playVideo() {
        val m = MediaController(this)
        videoView?.setMediaController(m)
        val u = Uri.parse(path)
        videoView?.setVideoURI(u)
        videoView?.start()
    }

    override fun onBackPressed() {
        stopvideo()
        super.onBackPressed()
    }

    fun stopvideo() {
        if (videoView?.isPlaying() == true) {
            videoView?.suspend()
        }
    }

    /*   fun loadBannerAdd() {
           val adView = AdView(this, this.resources.getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50)
           val adListener: AdListener = object : AdListener {
               override fun onError(ad: Ad, adError: AdError) {}
               override fun onAdLoaded(ad: Ad) {}
               override fun onAdClicked(ad: Ad) {
                   adView.loadAd(adView.buildLoadAdConfig().withAdListener(this).build())
               }

               override fun onLoggingImpression(ad: Ad) {}
           }
           adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build())
           val relativeLayout = findViewById<RelativeLayout>(R.id.banner)
           relativeLayout.addView(adView)
       }

       fun admobBanner() {


           val adView = com.google.android.gms.ads.AdView(this)
           val adSize = IntersitialAdds.getAdSize(this)
           adView.adSize = adSize
           adView.adUnitId = resources.getString(R.string.admob_banner)
           val adRequest = AdRequest.Builder().build()

           val adViewLayout = findViewById<View>(R.id.mybanner) as RelativeLayout
           adViewLayout.addView(adView)


           adView.adListener = object : com.google.android.gms.ads.AdListener() {
               override fun onAdClosed() {
                   super.onAdClosed()
               }

               override fun onAdFailedToLoad(errorCode: Int) {
                   super.onAdFailedToLoad(errorCode)
                   //adViewLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
   //                adViewLayout.visibility = View.INVISIBLE
               }

               override fun onAdLeftApplication() {
                   super.onAdLeftApplication()
               }

               override fun onAdOpened() {
                   super.onAdOpened()
               }

               override fun onAdLoaded() {
                   super.onAdLoaded()
   //                adViewLayout.visibility = View.VISIBLE
               }
           }

           adView.loadAd(adRequest)

       }*/

    fun share() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND_MULTIPLE
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.")
        val file = File(path)
        if (file.name.endsWith("gif")) {
            intent.type = "gif/*" /* This example is sharing jpeg images. */
        } else {
            intent.type = "video/*" /* This example is sharing jpeg images. */
        }
        val uri = FileProvider.getUriForFile(getApplicationContext(), resources.getString(R.string.authority), file)
        val files = ArrayList<Uri>()
        files.add(uri)
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
        startActivity(intent)
    }


//    fun loadFbBannerAdd() {
//        val adView = AdView(this, resources.getString(R.string.fbbannerad), AdSize.BANNER_HEIGHT_50)
//        val adListener: AdListener = object : AdListener {
//            override fun onError(ad: Ad, adError: AdError) {
//                Log.d("TAG", "onError: " + adError.errorMessage)
//            }
//
//            override fun onAdLoaded(ad: Ad) {}
//            override fun onAdClicked(ad: Ad) {}
//            override fun onLoggingImpression(ad: Ad) {}
//        }
//        val relativeLayout = findViewById<RelativeLayout>(R.id.top_banner)
//        relativeLayout.addView(adView)
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build())
//    }

}