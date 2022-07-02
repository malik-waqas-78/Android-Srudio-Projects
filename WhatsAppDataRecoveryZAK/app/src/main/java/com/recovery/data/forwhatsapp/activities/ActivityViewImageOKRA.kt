package com.recovery.data.forwhatsapp.activities

//import com.facebook.ads.*


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.recovery.data.forwhatsapp.R
import java.io.File
import java.util.*


class ActivityViewImageOKRA : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_images_okra)

//        loadFbBannerAdd()
//        AATAdsManager.displayInterstitialFacebook()
        val imageView: ImageView = findViewById<ImageView>(R.id.image)
        val shareCard: CardView=findViewById<CardView>(R.id.cardShare)
        val deleteCard: CardView=findViewById<CardView>(R.id.cardDelete)
        val name: TextView=findViewById<TextView>(R.id.imgname)
        val intent = intent
        val url = intent.getStringExtra("url")
        val imangename=intent.getStringExtra("imgname")
        Glide.with(this@ActivityViewImageOKRA).load(url).into(imageView)
        name.setText(imangename)
        name.setSelected(true)
        findViewById<ImageView>(R.id.iv_back2).setOnClickListener {
            finish()
        }
        shareCard.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND_MULTIPLE
            intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.")
            intent.type = "image/*" /* This example is sharing jpeg images. */
            val file = File(url)
            val uri = FileProvider.getUriForFile(applicationContext, resources.getString(R.string.authority), file)
            val files = ArrayList<Uri>()
            files.add(uri)
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
            startActivity(intent)
        }
        deleteCard.setOnClickListener {
            deleteWarningDialog(url)
        }
        findViewById<ImageView>(R.id.iv_share).setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_SEND_MULTIPLE
            intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.")
            intent.type = "image/*" /* This example is sharing jpeg images. */
            val file = File(url)
            val uri = FileProvider.getUriForFile(applicationContext, resources.getString(R.string.authority), file)
            val files = ArrayList<Uri>()
            files.add(uri)
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
            startActivity(intent)
        }

      /*  loadBannerAdd()
        admobBanner()*/
    }
    private fun deleteWarningDialog(url: String?) {

        val dialog = Dialog(this@ActivityViewImageOKRA)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_warning_layout_okra)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawable(getDrawable(android.R.color.transparent))
        dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        val btn_yes = dialog.findViewById<Button>(R.id.btn_yes)
        val no = dialog.findViewById<Button>(R.id.btn_no)
        no.setOnClickListener { view: View? -> dialog.dismiss() }
        btn_yes.setOnClickListener { view: View? ->
            val file = File(url)
            if (file.exists()) {
                file.delete()
                System.gc()
            }
            finish()
        }
        dialog.show()
    }
    override fun onResume() {
        super.onResume()
    }
    /*fun loadBannerAdd() {
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