package com.photo.recovery.activites

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.photo.recovery.R
import com.photo.recovery.adapters.ProfilesAdapterAAT
import com.photo.recovery.ads.BannerAdHelperAAT
import com.photo.recovery.ads.InterAdHelperAAT
import com.photo.recovery.ads.isAppInstalledFromPlay
//import com.datarecovery.recyclebindatarecovery.ads.BannerAdHelperAAT
//import com.datarecovery.recyclebindatarecovery.ads.InterAdHelper
//import com.datarecovery.recyclebindatarecovery.ads.isAppInstalledFromPlay
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.databinding.ActivityGeneralProfilesAatBinding
import com.photo.recovery.dialogues.MyDialoguesAAT
import com.photo.recovery.models.ChatProfileModelClassAAT
import com.photo.recovery.models.ChatsAAT
import com.photo.recovery.models.ChatsModelClassAAT
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.Sort

class GeneralProfileAAT : AppCompatActivity(), SelectionCallBackAAT {

    lateinit var binding: ActivityGeneralProfilesAatBinding
    var mAdapterAAT: ProfilesAdapterAAT? = null
    var profile = ArrayList<ChatsAAT>()
    var realmProfiles: Realm? = null


    companion object {
        var color: Int = R.color.appcolorgreen
        var MessengerType:String? = ""
    }

    var queryTextChangeListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            mAdapterAAT?.filter?.filter(newText)
            return false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralProfilesAatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(isAppInstalledFromPlay(this@GeneralProfileAAT)){
            //loadFbBannerAd()
            //loadAdmobBannerAd()
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
//            BannerAdHelperAAT.loadNormalAdmobBanner(this,binding.bottomBanner)
        }

        if(intent.hasExtra(MyConstantsAAT.CHAT_TYPE_KEY)){
            if(intent.getStringExtra(MyConstantsAAT.CHAT_TYPE_KEY)==MyConstantsAAT.TYPE_WHATSAPP){
                InterAdHelperAAT.showMediatedAdmobIntersitial(this)
            }
        }


        binding.tbGeneralProfiles.setNavigationIcon(R.drawable.ic_group_back_white)
        binding.tbGeneralProfiles.setNavigationOnClickListener {
            finish()
        }

        binding.btnDelete.setOnClickListener(View.OnClickListener {
            initRealms()
            if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
                MyDialoguesAAT.showDeleteWarning(this@GeneralProfileAAT,
                    object : MyDialoguesAAT.Companion.DeletionCallBack {
                        override fun deleteFiles() {
                            mAdapterAAT?.selectedList?.forEach {

                                var realmResults: RealmResults<*>? = null
                                realmResults =
                                    chatTable.where(ChatsModelClassAAT::class.java)
                                        .equalTo("senderName", it.senderName).findAll()
                                chatTable.beginTransaction()
                                realmResults.deleteAllFromRealm()
                                chatTable.commitTransaction()
                                realmResults =
                                    profileTable.where(ChatProfileModelClassAAT::class.java)
                                        .equalTo("senderName", it.senderName).findAll()
                                profileTable.beginTransaction()
                                realmResults.deleteAllFromRealm()
                                profileTable.commitTransaction()
                                profile.remove(it)
                            }
                            mAdapterAAT?.selectedList?.clear()

                            if(profile.isEmpty()){
                                binding.viewProgress.visibility = View.GONE
                                binding.viewMain.visibility = View.GONE
                                binding.viewEmpty.visibility = View.VISIBLE
                            }
                            mAdapterAAT?.notifyDataSetChanged()
                            mState=false
                            if(mState) {
                                binding.btnDelete.visibility = View.VISIBLE

                            }else {
                                binding.btnDelete.visibility = View.GONE

                            }
                        }

                        override fun dismiss() {
                            mAdapterAAT?.selectedList?.clear()

                            if(profile.isEmpty()){
                                binding.viewProgress.visibility = View.GONE
                                binding.viewMain.visibility = View.GONE
                                binding.viewEmpty.visibility = View.VISIBLE
                            }
                            mAdapterAAT?.notifyDataSetChanged()
                            mState=false
                            if(mState) {
                                binding.btnDelete.visibility = View.VISIBLE

                            }else {
                                binding.btnDelete.visibility = View.GONE

                            }
                        }

                    })
            }
        })




    }

    override fun onResume() {
        super.onResume()
        initRealm()
        profile.clear()
        if (intent.hasExtra(MyConstantsAAT.CHAT_TYPE_KEY)) {
            MessengerType = intent.getStringExtra(MyConstantsAAT.CHAT_TYPE_KEY)
            when (intent.getStringExtra(MyConstantsAAT.CHAT_TYPE_KEY)) {
                MyConstantsAAT.TYPE_WHATSAPP -> {
                    color = R.color.appcolorgreen
                    binding.toolbarTitle.text =
                        applicationContext.resources.getString(R.string.whatsapp)
                    binding.tbGeneralProfiles.setBackgroundColor(resources.getColor(R.color.appcolorgreen))
//                    supportActionBar?.setBackgroundDrawable(
//                        applicationContext.resources.getDrawable(
//                            R.drawable.tb_back_whatsapp,
//                            null
//                        )
//                    )

                    val window: Window = window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = resources.getColor(R.color.appcolorgreen)

                    realmProfiles?.where(ChatProfileModelClassAAT::class.java)
                        ?.sort("timeInMillis", Sort.DESCENDING)
                        ?.equalTo("messengerType", MyConstantsAAT.TYPE_WHATSAPP)?.findAll()?.forEach {
                            profile.add(
                                ChatsAAT(
                                    it.timeInMillis.toString(),
                                    it.senderName,
                                    it.lastMsg,
                                    it.icon,
                                    it.lastMsgDate,
                                    MyConstantsAAT.TYPE_WHATSAPP
                                )
                            )
                        }
                }
                MyConstantsAAT.TYPE_MAIL -> {
                    color = R.color.pink1
                    binding.toolbarTitle.text = applicationContext.resources.getString(R.string.mails)
//                    supportActionBar?.setBackgroundDrawable(
//                        applicationContext.resources.getDrawable(
//                            R.drawable.tb_back_mail,
//                            null
//                        )
//                    )
                    binding.tbGeneralProfiles.setBackgroundColor(resources.getColor(R.color.pink1))

                    val window: Window = window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = resources.getColor(R.color.pink1)

                    realmProfiles?.where(ChatProfileModelClassAAT::class.java)
                        ?.sort("timeInMillis", Sort.DESCENDING)
                        ?.equalTo("messengerType", MyConstantsAAT.TYPE_MAIL)?.findAll()?.forEach {
                            profile.add(
                                ChatsAAT(
                                    it.timeInMillis.toString(),
                                    it.senderName,
                                    it.lastMsg,
                                    it.icon,
                                    it.lastMsgDate,
                                    MyConstantsAAT.TYPE_MAIL
                                )
                            )
                        }
                }
                MyConstantsAAT.TYPE_MESSENGER -> {
                    color = R.color.purple
                    binding.toolbarTitle.text =
                        applicationContext.resources.getString(R.string.messenger)
//                    supportActionBar?.setBackgroundDrawable(
//                        applicationContext.resources.getDrawable(
//                            R.drawable.tb_back_mess,
//                            null
//                        )
//                    )
                    binding.tbGeneralProfiles.setBackgroundColor(resources.getColor(R.color.purple))

                    val window: Window = window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = resources.getColor(R.color.purple)

                    realmProfiles?.where(ChatProfileModelClassAAT::class.java)
                        ?.sort("timeInMillis", Sort.DESCENDING)
                        ?.equalTo("messengerType", MyConstantsAAT.TYPE_MESSENGER)?.findAll()?.forEach {
                            profile.add(
                                ChatsAAT(
                                    it.timeInMillis.toString(),
                                    it.senderName,
                                    it.lastMsg,
                                    it.icon,
                                    it.lastMsgDate,
                                    MyConstantsAAT.TYPE_MESSENGER
                                )
                            )
                        }
                }
            }
            if (profile.isNotEmpty()) {
                binding.searchView.setOnQueryTextListener(queryTextChangeListener)

                binding.viewProgress.visibility = View.GONE
                binding.viewMain.visibility = View.VISIBLE
                binding.viewEmpty.visibility = View.GONE

                mAdapterAAT = ProfilesAdapterAAT(this, profile)
                binding.rvProfiles.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvProfiles.adapter = mAdapterAAT


            } else {
                binding.viewProgress.visibility = View.GONE
                binding.viewMain.visibility = View.GONE
                binding.viewEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun initRealm() {
        val realmProfile = RealmConfiguration.Builder()
            .name("profiles")
            .deleteRealmIfMigrationNeeded()
            .build()
        realmProfiles = Realm.getInstance(realmProfile)
    }

    var mState=false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu_aat, menu)
        menu?.findItem(R.id.mi_delete)?.isVisible = mState
        menu?.findItem(R.id.mi_share)?.isVisible=false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_delete -> {
//                initRealms()
//                if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
//                    MyDialoguesAAT.showDeleteWarning(this@GeneralProfileAAT,
//                        object : MyDialoguesAAT.Companion.DeletionCallBack {
//                            override fun deleteFiles() {
//                                mAdapterAAT?.selectedList?.forEach {
//
//                                    var realmResults: RealmResults<*>? = null
//                                    realmResults =
//                                        chatTable.where(ChatsModelClassAAT::class.java)
//                                            .equalTo("senderName", it.senderName).findAll()
//                                    chatTable.beginTransaction()
//                                    realmResults.deleteAllFromRealm()
//                                    chatTable.commitTransaction()
//                                    realmResults =
//                                        profileTable.where(ChatProfileModelClassAAT::class.java)
//                                            .equalTo("senderName", it.senderName).findAll()
//                                    profileTable.beginTransaction()
//                                    realmResults.deleteAllFromRealm()
//                                    profileTable.commitTransaction()
//                                    profile.remove(it)
//                                }
//                                mAdapterAAT?.selectedList?.clear()
//
//                                if(profile.isEmpty()){
//                                    binding.viewProgress.visibility = View.GONE
//                                    binding.viewMain.visibility = View.GONE
//                                    binding.viewEmpty.visibility = View.VISIBLE
//                                }
//                                mAdapterAAT?.notifyDataSetChanged()
//                                mState=false
//                                invalidateOptionsMenu()
//                            }
//
//                            override fun dismiss() {
//
//                            }
//
//                        })
//                }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    lateinit var profileTable: Realm
    lateinit var chatTable: Realm
    private fun initRealms() {
        val realmProfile = RealmConfiguration.Builder()
            .name("profiles")
            .build()
        profileTable = Realm.getInstance(realmProfile)
        val realmChat = RealmConfiguration.Builder()
            .name("chats")
            .build()
        chatTable = Realm.getInstance(realmChat)
    }

    override fun selectionChanged(size: Int) {
        mState= size > 0
        if(mState) {
            binding.btnDelete.visibility = View.VISIBLE

        }else {
            binding.btnDelete.visibility = View.GONE

        }
    }
/*    fun loadFbBannerAd() {

        val adView = AdView(
            this@GeneralProfile,
            this@GeneralProfile.resources.getString(R.string.fb_banner_ad2),
            AdSize.BANNER_HEIGHT_50
        )
        val s = this@GeneralProfile.resources.getString(R.string.fb_banner_ad2)
        val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {

            override fun onError(ad: Ad, adError: AdError) {
                Log.d(MyConstants.TAG, "onError: ")
                Log.d(
                    MyConstants.TAG,
                    "onError  Banner: " + ad!!.placementId + "\n" + adError!!.errorMessage
                )
            }

            override fun onAdLoaded(ad: Ad) {
                // Ad loaded callback
                Log.d(MyConstants.TAG, "onAdLoaded: Banner=" + ad!!.placementId)
            }

            override fun onAdClicked(ad: Ad) {
                Log.d(MyConstants.TAG, "onAdClicked: ")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d(MyConstants.TAG, "onLoggingImpression: ")
            }
        }


        adView?.loadAd(adView?.buildLoadAdConfig()?.withAdListener(adListener)?.build())
        binding.topBanner.addView(adView)
    }*/

}