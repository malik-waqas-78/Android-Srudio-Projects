package com.photo.recovery.activites

import android.Manifest.permission
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.photo.recovery.R
import com.photo.recovery.ads.*
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.constants.MyConstantsAAT.Companion.PERMISSIONS_REQUSTCODE2
import com.photo.recovery.databinding.ActivitySplachScreenAatBinding
import com.photo.recovery.databinding.DbGeneralAatBinding
import com.photo.recovery.dialogue.MyDialogueAAT
import com.photo.recovery.permissions.MyPermissionsAAT
import com.photo.recovery.utils.SharePrefHelperAAT
import java.util.*


class SplashScreenAAT : AppCompatActivity() {

    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

    lateinit var binding: ActivitySplachScreenAatBinding

    var mGetContent = registerForActivityResult(
        StartActivityForResult()
    ) {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                startHomeActivity()
            } else {
                Toast.makeText(
                    this@SplashScreenAAT,
                    "Allow permission for storage access!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivitySplachScreenAatBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        Realm.init(this)

        if (isAppInstalledFromPlay(this@SplashScreenAAT)) {
            //loadFbBannerAd()
//            NativeAdHelper.loadFbNativeAd(this@SplashScreen)
            InterAdHelperAAT.loadMediatedAdmobInterstitial(this@SplashScreenAAT)
            InterAdHelperAAT.loadNormalAdmobInterstitial(this)
            NativeAdHelperAAT.loadAdmobNativeAd(this@SplashScreenAAT)
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
//            BannerAdHelperAAT.loadNormalAdmobBanner(this,binding.bottomBanner)
        }

    }

    override fun onPostResume() {
        super.onPostResume()
        /*if(MyPermissions.hasStoragePermission(this@SplashScreen)){
          CheckObserver().execute()
        }*/
        Handler().postDelayed({
           runOnUiThread {
               binding.btnStart.visibility = View.VISIBLE
               binding.pbarSplash.visibility = View.INVISIBLE
           }
        }, 4000)
        if (SharePrefHelperAAT(this).isFirstTime()) {
            startActivity(Intent(this@SplashScreenAAT, MyIntroActivityAAT::class.java))
        }
        //MediationTestSuite.launch(this@SplashScreen)
        // MyConstants.rootDir=Environment.getExternalStorageDirectory().absolutePath

        binding.btnStart.setOnClickListener {


            if (checkPermission()) {
                startHomeActivity()
            } else {
                permisssionDialog()
            }

//            if (MyPermissionsAAT.hasStoragePermission(this@SplashScreenAAT)) {
//                if (!isNotificationServiceEnabled()) {
//                    if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
//                        buildNotificationServiceAlertDialog()
//                    }
//                } else {
//                    startHomeActivity()
//                }
//            } else {
//                //do nothing
//                MyPermissionsAAT.showStorageExplanation(this@SplashScreenAAT,
//                    object : MyPermissionsAAT.Companion.ExplanationCallBack {
//                        override fun requestPermission() {
//                            MyPermissionsAAT.requestStoragePermission(this@SplashScreenAAT)
//                        }
//
//                        override fun denyPermission() {
//
//                        }
//
//                    })
//            }
        }

        binding.btnPrivacyPolicy.setOnClickListener {
            MyDialogueAAT.showPrivacyDialog(this@SplashScreenAAT)
        }
    }

    private fun checkPermission(): Boolean {
        var check = false
        if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            check = Environment.isExternalStorageManager()
        } else if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var result = 0
            result = checkSelfPermission(permission.WRITE_EXTERNAL_STORAGE)
            var result1 = 0
            result1 = checkSelfPermission(permission.READ_EXTERNAL_STORAGE)
            check =
                result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
        return check
    }

    private fun requestPermission() {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", applicationContext.packageName))
                // startActivityForResult(intent, 2296);
                mGetContent.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                // startActivityForResult(intent, 2296);
                mGetContent.launch(intent)
            }
        } else {
            //below android 11
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        permission.WRITE_EXTERNAL_STORAGE,
                        permission.READ_EXTERNAL_STORAGE
                    ), PERMISSIONS_REQUSTCODE2
                )
            } else {
                startHomeActivity()
            }
        }
    }

    fun permisssionDialog() {
        val alertDialog = AlertDialog.Builder(this@SplashScreenAAT)
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View = LayoutInflater.from(this@SplashScreenAAT)
            .inflate(R.layout.db_general_aat, viewGroup, false)
        alertDialog.setView(dialogView)
        val dialog = alertDialog.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val titleText = """Storage Permission Required.""";
        val msgText = "This App needs Storage Permission to recover and show Images/Videos/Audios/Documents etc. Please allow storage permission to continue...";


        val title:TextView=dialogView.findViewById<Button>(R.id.tv_title)

        title.setText(titleText)


        val msg:TextView=dialogView.findViewById<Button>(R.id.tv_msg)
        msg.setText(msgText)

        val btnAllow = dialogView.findViewById<Button>(R.id.btn_positive)
        val btnDeny = dialogView.findViewById<Button>(R.id.btn_negative)

        btnAllow.setText("Allow")
        btnDeny.setText("Deny")


        btnAllow.setOnClickListener {
            requestPermission()
            dialog.dismiss()
        }
        btnDeny.setOnClickListener { dialog.dismiss() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startHomeActivity()
        } else if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED && VERSION.SDK_INT >= 23) {
            if (!shouldShowRequestPermissionRationale(permission.WRITE_EXTERNAL_STORAGE)) {
                val alertDialog = AlertDialog.Builder(this@SplashScreenAAT)
                val viewGroup = findViewById<ViewGroup>(android.R.id.content)
                val dialogView: View = LayoutInflater.from(this@SplashScreenAAT)
                    .inflate(R.layout.db_general_aat, viewGroup, false)
                alertDialog.setView(dialogView)
                val buttonAllowRel =
                    dialogView.findViewById<View>(R.id.btn_positive) as Button
                val buttonCancelRel =
                    dialogView.findViewById<View>(R.id.btn_negative) as Button
                val dialog = alertDialog.create()
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()

                buttonAllowRel.setText("Allow")
                buttonCancelRel.setText("Cancel")

                val titleText = """Storage Permission Required.""";
                val msgText = "This App needs Storage Permission to recover and show Images/Videos/Audios/Documents etc. Please allow storage permission from Settings to continue...";


                val title:TextView=dialogView.findViewById<Button>(R.id.tv_title)

                title.setText(titleText)


                val msg:TextView=dialogView.findViewById<Button>(R.id.tv_msg)
                msg.setText(msgText)


                buttonAllowRel.setOnClickListener {
                    dialog.dismiss()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        "package",
                        packageName, null
                    )
                    intent.data = uri
                    mGetContent.launch(intent)
                }
                buttonCancelRel.setOnClickListener { dialog.dismiss() }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            return
        }
    }


    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onBackPressed() {
        InterAdHelperAAT.showMediatedAdmobIntersitial(this)
        exitorcontinue()
    }

    fun exitorcontinue() {
        val dialog = Dialog(this@SplashScreenAAT)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dbBinding = DbGeneralAatBinding.inflate(LayoutInflater.from(this@SplashScreenAAT))
        dialog.setContentView(dbBinding.root)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dbBinding.btnPositive.text = "Exit"
        dbBinding.btnNegative.text = "Cancel"
        dbBinding.tvTitle.visibility = View.GONE
        dbBinding.tvMsg.text = "Are you sure you want to exit?"
        val positive: Button = dbBinding.btnPositive
        positive.setOnClickListener {
            dialog.cancel()
            finishAffinity()
        }
        val negative: Button = dbBinding.btnNegative
        negative.setOnClickListener {
            if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
                dialog.cancel()
            }
        }

        val rateUs: Button = dialog.findViewById(R.id.btn_rate_us)
        rateUs.visibility = View.VISIBLE
        rateUs.setOnClickListener {
            rateApp()
            if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
                dialog.cancel()
            }
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.cancel()
        }
        dbBinding.adFrame.visibility = View.VISIBLE
        NativeAdHelperAAT.showAdmobNativeAd(this, dbBinding.adFrame)
        if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
            dialog.show()
        }
    }

    private fun rateIntentForUrl(url: String): Intent? {
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    url + Objects.requireNonNull(this@SplashScreenAAT).packageName
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

    fun rateApp() {
        try {
            val rateIntent = rateIntentForUrl("market://details?id=")
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=")
            startActivity(rateIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyConstantsAAT.PERMISSION_SETTINGS_STORAGE) {
            if (!MyPermissionsAAT.hasStoragePermission(this@SplashScreenAAT)) {
                if (!isNotificationServiceEnabled()) {

                    if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
                        buildNotificationServiceAlertDialog()
                    }
                }
            } else {
                if (!isNotificationServiceEnabled()) {

                    if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
                        buildNotificationServiceAlertDialog()
                    }
                } else {
                    startHomeActivity()
                }
            }
        } else if (requestCode == MyConstantsAAT.NOTIFICATION_SETTINGS) {
            if (isNotificationServiceEnabled()) {
                if (MyPermissionsAAT.hasStoragePermission(this@SplashScreenAAT)) {
                    startHomeActivity()
                }
            }
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (permissions.isNotEmpty() && grantResults.isNotEmpty()) {
//            if (requestCode == MyConstantsAAT.STORAGE_PERMISSION_REQUEST_CODE) {
//                if (!MyPermissionsAAT.hasStoragePermission(this@SplashScreenAAT)) {
//                    if (Build.VERSION.SDK_INT >= 23) {
//                        var showRational = shouldShowRequestPermissionRationale(permissions[0])
//                        if (!showRational) {
//
//                            MyPermissionsAAT.showStorageRational(this@SplashScreenAAT, object :
//                                MyPermissionsAAT.Companion.RationalCallback {
//                                override fun openSettings() {
//                                    val intent =
//                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                    val uri: Uri = Uri.fromParts("package", packageName, null)
//                                    intent.data = uri
//                                    startActivityForResult(
//                                        intent,
//                                        MyConstantsAAT.PERMISSION_SETTINGS_STORAGE
//                                    )
//                                }
//
//                                override fun dismissed() {
//                                    if (!isNotificationServiceEnabled()) {
//
//                                        if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
//                                            buildNotificationServiceAlertDialog()
//                                        }
//                                    }
//                                }
//
//                            })
//                        } else {
//                            if (!isNotificationServiceEnabled()) {
//
//                                if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
//                                    buildNotificationServiceAlertDialog()
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    if (!isNotificationServiceEnabled()) {
//
//                        if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
//                            buildNotificationServiceAlertDialog()
//                        }
//                    } else {
//                        startHomeActivity()
//                    }
//                }
//            }
//        }
//    }

    fun startHomeActivity() {

        if (!isNotificationServiceEnabled()) {
            if (!this@SplashScreenAAT.isFinishing && !this@SplashScreenAAT.isDestroyed) {
                buildNotificationServiceAlertDialog()
            }
        }else{
            startActivity(Intent(this@SplashScreenAAT, HomeScreenAAT::class.java))
            finish()
        }



    }

    private fun buildNotificationServiceAlertDialog() {
        val alertDialogBuilder = Dialog(this@SplashScreenAAT)
        val binding = DbGeneralAatBinding.inflate(LayoutInflater.from(this@SplashScreenAAT))
        alertDialogBuilder.setContentView(binding.root)
        alertDialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialogBuilder.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        binding.tvTitle.setText(R.string.notification_listener_service)
        binding.tvMsg.setText(R.string.notification_listener_service_explanation)

        binding.btnPositive.text = "Allow"
        binding.btnNegative.text = "No"

        binding.btnPositive.setOnClickListener {
            startActivityForResult(
                Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS),
                MyConstantsAAT.PERMISSION_SETTINGS_STORAGE
            )
            alertDialogBuilder?.dismiss()
        }
        binding.btnNegative.setOnClickListener {
            startActivity(Intent(this@SplashScreenAAT, HomeScreenAAT::class.java))
            finish()
            alertDialogBuilder?.cancel()
        }
        alertDialogBuilder?.show()
    }






}