package com.ppt.walkie.actvities

import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppt.walkie.BuildConfig
import com.ppt.walkie.R
import com.ppt.walkie.adapters.SettingsAdapterOKRA
import com.ppt.walkie.callbacks.SettingsCallBacksOKRA
import com.ppt.walkie.databinding.ActivitySettingsOkraBinding


import com.ppt.walkie.services.MyServiceOKRA
import com.ppt.walkie.utils.SettingsModelClassOKRA
import com.ppt.walkie.utils.SharePrefHelperOKRA
import com.walkie.talkie.ads.BannerAdHelperOKRA

import java.util.*
import kotlin.collections.ArrayList

class SettingsActivityOKRA : AppCompatActivity(),SettingsCallBacksOKRA {
    lateinit var binding: ActivitySettingsOkraBinding
    var list=ArrayList<SettingsModelClassOKRA>()

    var mSharePrefHelper:SharePrefHelperOKRA?=null
    var mService: MyServiceOKRA?=null
    private var mBound: Boolean = false

    /*private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MyServiceOKRA.MyServiceBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding= ActivitySettingsOkraBinding.inflate(layoutInflater)
        setContentView(binding.root)


        BannerAdHelperOKRA.loadMediatedAdmobBanner(this,binding.topBanner)
        mSharePrefHelper=SharePrefHelperOKRA(this@SettingsActivityOKRA)
        setUpListData()

        var adapter=SettingsAdapterOKRA(this@SettingsActivityOKRA,list,this)
        var layoutManager=LinearLayoutManager(this@SettingsActivityOKRA,LinearLayoutManager.VERTICAL,false)
        binding.rvSettings.layoutManager=layoutManager
        binding.rvSettings.adapter=adapter

        binding.btnHome.setOnClickListener {
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
      /*  Intent(this, MyServiceOKRA::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }*/
    }

    private fun setUpListData() {
        list.clear()
        list.add(SettingsModelClassOKRA(getDrawable(R.drawable.ic_change_name_icon),getString(R.string.receive_calls_in_Background),mSharePrefHelper?.canReceiveCallsInBackground()==true,getString(R.string.description_service)))
        list.add(SettingsModelClassOKRA(getDrawable(R.drawable.ic_change_name_icon),getString(R.string.change_user_name),false))
        list.add(SettingsModelClassOKRA(getDrawable(R.drawable.ic_rate_us),getString(R.string.rate_us),false))
        list.add(SettingsModelClassOKRA(getDrawable(R.drawable.ic_share_icon),getString(R.string.share),false))
    }

    override fun stopMyService() {
        stopService(Intent(this@SettingsActivityOKRA,MyServiceOKRA::class.java))
    }

    override fun itemClicked(item: SettingsModelClassOKRA) {
        if(item.title==getString(R.string.change_user_name)){

            startActivity(Intent(this@SettingsActivityOKRA,GetUserNameOKRA::class.java))
        }else if(item.title==getString(R.string.share)){
            shareMyApp()
        }else if(item.title==getString(R.string.rate_us)){
            rateApp()
        }
    }

    private fun shareMyApp() {
        val urlToShare="${applicationContext.resources.getString(R.string.play_store_link)}${BuildConfig.APPLICATION_ID}"
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "${applicationContext.resources.getString(R.string.share_message)} $urlToShare"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    private fun rateIntentForUrl(url: String): Intent? {
        var intent: Intent? = null
        intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                url + Objects.requireNonNull(this@SettingsActivityOKRA).packageName
            )
        )
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
}