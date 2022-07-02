package com.photo.recovery.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.photo.recovery.ads.BannerAdHelperAAT
import com.photo.recovery.ads.isAppInstalledFromPlay
//import com.datarecovery.recyclebindatarecovery.ads.BannerAdHelperAAT
//import com.datarecovery.recyclebindatarecovery.ads.isAppInstalledFromPlay
import com.photo.recovery.databinding.ActivityShowImageAatBinding


class ShowImageAAT : AppCompatActivity() {

    lateinit var binding:ActivityShowImageAatBinding
    companion object{
        var position:Int=0
        var open=false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityShowImageAatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if(isAppInstalledFromPlay(this@ShowImageAAT)){
           // loadFbBannerAd()
           // loadAdmobBannerAd()
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
//            BannerAdHelperAAT.loadNormalAdmobBanner(this,binding.bottomBanner)
        }

        open=true
        supportActionBar?.title=""
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun changeName(name:String){
        supportActionBar?.title=name
    }


}