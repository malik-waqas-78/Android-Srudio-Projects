package com.ppt.walkie.actvities


import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.common.internal.Constants
import com.ppt.walkie.BuildConfig
import com.ppt.walkie.R
import com.ppt.walkie.R.drawable.*
import com.ppt.walkie.databinding.ActivityUserNameOkraBinding
import com.ppt.walkie.services.MyServiceOKRA
import com.ppt.walkie.utils.ConstantsOKRA
import com.ppt.walkie.utils.ConstantsOKRA.TAG
import com.ppt.walkie.utils.SharePrefHelperOKRA
import com.ppt.walkie.utils.TempDelClass
import com.walkie.talkie.ads.BannerAdHelperOKRA
import com.walkie.talkie.ads.InterAdHelperOKRA
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class GetUserNameOKRA : AppCompatActivity() {
    lateinit var binding: ActivityUserNameOkraBinding
    private lateinit var sharedPreferences: SharePrefHelperOKRA

    var path:String?=null

    val avatars = ArrayList<ImageView>()
    var outputStream: FileOutputStream? = null
    var f: File? = null

    var mGetContent = registerForActivityResult(
        GetContent()
    ) { uri ->
        if (uri != null) {
            var f: File? = null
            try {
                f = saveImage(
                    MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        uri
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val uri2 = FileProvider.getUriForFile(
                applicationContext, BuildConfig.APPLICATION_ID.toString() + ".fileprovider",
                f!!
            )
            val uri = uri2.toString()
            sharedPreferences.setImagePath(uri)

            binding.ivPickedAvater.setImageURI(Uri.parse(sharedPreferences.getImagePath()))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        binding= ActivityUserNameOkraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BannerAdHelperOKRA.loadMediatedAdmobBanner(this,binding.topBanner)
        binding.btnHome.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        sharedPreferences = SharePrefHelperOKRA(this@GetUserNameOKRA)

        if(sharedPreferences.getName().equals("Enter Name")){
            binding.etName.setHint(sharedPreferences.getName())
        }else {
            binding.etName.setText(sharedPreferences.getName())
        }

        path=sharedPreferences.getImagePath()

        if(path!=null){
            binding.ivPickedAvater.setImageURI(Uri.parse(path))
        }

//        binding.etName.hint = SharePrefHelperHS.name

//        avatars.add(binding.avatar1)
//        avatars.add(binding.avatar2)
//        avatars.add(binding.avatar3)
//        avatars.add(binding.avatar4)
//        avatars.add(binding.avatar5)
//        avatars.add(binding.avatar6)

          binding.ivPickImage.setOnClickListener(View.OnClickListener {
              mGetContent.launch("image/*")
          })

//        setUpSelectedAvatar(0)


//        for (index in avatars.indices) {
//            avatars[index].setOnClickListener {
//                setUpSelectedAvatar(0)
//                sharedPreferences.setAvatar(0)
//                binding.ivPickedAvater.setImageDrawable(avatars[0].drawable)
//            }
//        }

        binding.btnStart.setOnClickListener {

            if (binding.etName.text.isNotEmpty() && binding.etName.text.toString() != SharePrefHelperOKRA.name) {
                sharedPreferences?.setName(binding.etName.text.toString())
            }

            if (SharePrefHelperOKRA.name != "Enter Name") {
                sharedPreferences?.setIsFirstTime(false)
                if (sharedPreferences.canReceiveCallsInBackground() && !sharedPreferences.isFirstTime()) {
                    val intent = Intent(this@GetUserNameOKRA, MyServiceOKRA::class.java)
                    ContextCompat.startForegroundService(this@GetUserNameOKRA, intent)
                }

                startActivity(Intent(this@GetUserNameOKRA, ActivityHomeOKRA::class.java))

                finish()
            } else {
                binding.etName.error = "Please Enter Your Name First"
            }

        }

    }

    fun saveImage(bitmap: Bitmap): File? {
        f = null
        val filepath = filesDir
        val dir = File(filepath.absolutePath + "/temp3/")
        if (!dir.exists()) {
            dir.mkdir()
        }
        if (TempDelClass(this@GetUserNameOKRA).get_Documents_Images().size != 0) {
            val images: java.util.ArrayList<File> =
                TempDelClass(this@GetUserNameOKRA).get_Documents_Images()
            for (f in images) {
                f.delete()
            }
        }


        val file = File(dir,  System.currentTimeMillis().toString()+".jpg")
        try {
            outputStream = FileOutputStream(file)
            f = file
        } catch (e: FileNotFoundException) {
            f = null
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        try {
            outputStream!!.flush()
            outputStream!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        MediaScannerConnection.scanFile(
            this@GetUserNameOKRA,
            arrayOf(file.path),
            arrayOf("image/jpg"),
            null
        )
        return f
    }
//    private fun setUpSelectedAvatar(selectedAvatar: Int) {
//        avatars[selectedAvatar].background = getDrawable(circle_grey_aat)
//        for (index in avatars.indices) {
//            if (index != selectedAvatar) {
//                avatars[index].background = getDrawable(circular_shape_aat)
//            }
//        }
//    }


    fun exitorcontinue() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.db_exitdialogue_okra)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val positive: Button = dialog.findViewById(R.id.btn_yes)
        positive.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }
        val negative: Button = dialog.findViewById(R.id.btn_exit)
        negative.setOnClickListener {
            dialog.dismiss()
        }
        val rateUs: ImageView = dialog.findViewById(R.id.btn_rate_us)
        rateUs.setOnClickListener {
            rateApp()
            dialog.dismiss()
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun rateIntentForUrl(url: String): Intent? {
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    url + Objects.requireNonNull(this@GetUserNameOKRA).packageName
                )
            )
        }
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = if (Build.VERSION.SDK_INT >= 21) {
            flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        } else {
            flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
        }
        intent!!.addFlags(flags)
        return intent
    }

    private fun rateApp() {
        try {
            val rateIntent = rateIntentForUrl("market://details?id=")
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=")
            startActivity(rateIntent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


//     fun loadFbBannerAd() {
//
//         val adView = AdView(
//             this@GetUserNameOKRA,
//             this@GetUserNameOKRA.resources.getString(R.string.fb_banner_ad2),
//             AdSize.BANNER_HEIGHT_50
//         )
//
//         val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {
//
//             override fun onError(ad: Ad, adError: AdError) {
//                 Log.d(ConstantsOKRA.TAG, "onError: ")
//             }
//
//             override fun onAdLoaded(ad: Ad) {
//                 // Ad loaded callback
//                 Log.d(TAG, "onAdLoaded: ")
//             }
//
//             override fun onAdClicked(ad: Ad) {
//                 Log.d(TAG, "onAdClicked: ")
//             }
//
//             override fun onLoggingImpression(ad: Ad) {
//                 // Ad impression logged callback
//                 Log.d(TAG, "onLoggingImpression: ")
//             }
//         }
//
//
//         adView?.loadAd(adView?.buildLoadAdConfig()?.withAdListener(adListener)?.build())
//         binding.topBanner.addView(adView)
//     }
//
//     fun loadAdmobBannerAd() {
//
//         val mAdView = com.google.android.gms.ads.AdView(this@GetUserNameOKRA)
//         val adSize: com.google.android.gms.ads.AdSize =
//            InterAdHelperOKRA.getAdSize(this@GetUserNameOKRA)
//        mAdView.adSize = com.google.android.gms.ads.AdSize.BANNER//adSize
//
//        mAdView.adUnitId = resources.getString(R.string.admob_banner)
//
//        val adRequest = AdRequest.Builder().build()
//
//
//        binding.topBanner.addView(mAdView)
//
//        mAdView.loadAd(adRequest)
//
//        mAdView.adListener = object : AdListener() {
//            override fun onAdClosed() {
//                super.onAdClosed()
//                //Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdFailedToLoad(p0: LoadAdError?) {
//                super.onAdFailedToLoad(p0)
//                binding.topBanner.visibility = View.INVISIBLE
//                //  Toast.makeText(this@ChooseMobile,"msg : ${p0?.message} code : ${p0?.code}",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdOpened() {
//                super.onAdOpened()
//                //  Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdLoaded() {
//                super.onAdLoaded()
//                binding.topBanner.visibility = View.VISIBLE
//                //   Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdClicked() {
//                super.onAdClicked()
//                //   Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdImpression() {
//                super.onAdImpression()
//                //   Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }
}