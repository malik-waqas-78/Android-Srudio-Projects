package com.photo.recovery.activites

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.photo.recovery.R
import com.photo.recovery.adapters.RecentChatsAdapterAAT
import com.photo.recovery.ads.BannerAdHelperAAT
import com.photo.recovery.ads.isAppInstalledFromPlay
//import com.datarecovery.recyclebindatarecovery.ads.BannerAdHelperAAT
//import com.datarecovery.recyclebindatarecovery.ads.isAppInstalledFromPlay
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.databinding.ActivityGeneralScreenAatBinding
import com.photo.recovery.dialogues.MyDialoguesAAT
import com.photo.recovery.models.ChatProfileModelClassAAT
import com.photo.recovery.models.ChatsModelClassAAT
import com.photo.recovery.models.RecentChatsAAT
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.Sort

class ProfilesActivityAAT : AppCompatActivity(), SelectionCallBackAAT {

    lateinit var binding: ActivityGeneralScreenAatBinding

    var mAdapterAAT: RecentChatsAdapterAAT? = null

    var realmProfiles: Realm? = null
    var chats = ArrayList<RecentChatsAAT>()
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
        binding = ActivityGeneralScreenAatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isAppInstalledFromPlay(this@ProfilesActivityAAT)) {
            //loadFbBannerAd()
            //loadAdmobBannerAd()
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
//            BannerAdHelperAAT.loadNormalAdmobBanner(this,binding.bottomBanner)
        }
        binding.toolbar.setBackgroundResource(R.drawable.tb_chats)
            binding.toolbar.setNavigationIcon(R.drawable.ic_group_back_white)

            binding.toolbar.setNavigationOnClickListener {
                finish()
            }

            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.purple)

            binding.first.setOnClickListener {
                val intent = Intent(this@ProfilesActivityAAT, GeneralProfileAAT::class.java)
                intent.putExtra(MyConstantsAAT.CHAT_TYPE_KEY, MyConstantsAAT.TYPE_MESSENGER)
                startActivity(intent)
            }
            binding.third.setOnClickListener {
                val intent = Intent(this@ProfilesActivityAAT, GeneralProfileAAT::class.java)
                intent.putExtra(MyConstantsAAT.CHAT_TYPE_KEY, MyConstantsAAT.TYPE_MAIL)
                startActivity(intent)
            }
            binding.second.setOnClickListener {
                val intent = Intent(this@ProfilesActivityAAT, GeneralProfileAAT::class.java)
                intent.putExtra(MyConstantsAAT.CHAT_TYPE_KEY, MyConstantsAAT.TYPE_WHATSAPP)
                startActivity(intent)
            }

            binding.btnDelete.setOnClickListener(View.OnClickListener {
                initRealms()
                if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
                    MyDialoguesAAT.showDeleteWarning(this@ProfilesActivityAAT,
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
                                    chats.remove(it)
                                }
                                mAdapterAAT?.selectedList?.clear()
                                if (chats.isEmpty()) {
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewMain.visibility = View.GONE
                                    binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                    binding.btnDelete.visibility=View.GONE
                                }
                                mAdapterAAT?.notifyDataSetChanged()


                                mState = false
                                if(mState) {
                                    binding.btnDelete.visibility = View.VISIBLE

                                }else {
                                    binding.btnDelete.visibility = View.GONE

                                }
                            }

                            override fun dismiss() {
                                mAdapterAAT?.selectedList?.clear()
                                if (chats.isEmpty()) {
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewMain.visibility = View.GONE
                                    binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                    binding.btnDelete.visibility=View.GONE
                                }
                                mAdapterAAT?.notifyDataSetChanged()


                                mState = false
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

            setupLayout()

            initRealm()

            loadData()

            updateView()
        }

        private fun updateView() {
            if (chats.isNotEmpty()) {
                binding.rvRecentDevices.visibility = View.VISIBLE
                binding.tvNoDeletedFiles.visibility = View.GONE


                mAdapterAAT = RecentChatsAdapterAAT(this, chats)
                binding.rvRecentDevices.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvRecentDevices.adapter = mAdapterAAT

            } else {
                binding.rvRecentDevices.visibility = View.GONE
                binding.tvNoDeletedFiles.visibility = View.VISIBLE
                binding.btnDelete.visibility=View.GONE
            }
            binding.tvTotalFilesDetails.text =
                "${applicationContext.getString(R.string.total_chats)} (${chats.size})"
            binding.tvChatsDetails.text =
                "${applicationContext.getString(R.string.messenger)} (${RecentChatsAAT.mcCount})"
            binding.tvDocsDetails.text =
                "${applicationContext.getString(R.string.whatsapp)} (${RecentChatsAAT.wcCount})"
            binding.tvMediaDetails.text =
                "${applicationContext.getString(R.string.mails)} (${RecentChatsAAT.gmcCount})"
        }

        private fun initRealm() {
            val realmProfile = RealmConfiguration.Builder()
                .name("profiles")
                .deleteRealmIfMigrationNeeded()
                .build()
            realmProfiles = Realm.getInstance(realmProfile)
        }

        private fun loadData() {

            chats.clear()

            RecentChatsAAT.wcCount = 0
            RecentChatsAAT.gmcCount = 0
            RecentChatsAAT.mcCount = 0

            val profileAATS: RealmResults<ChatProfileModelClassAAT>? =
                realmProfiles?.where(ChatProfileModelClassAAT::class.java)
                    ?.sort("timeInMillis", Sort.DESCENDING)?.findAll()

            if (profileAATS != null && profileAATS.isNotEmpty()) {

                for (chat in profileAATS) {
                    val mChat = RecentChatsAAT(
                        this@ProfilesActivityAAT,
                        chat.senderName,
                        chat.lastMsgDate,
                        chat.messengerType
                    )
                    chats.add(mChat)
                }

            }
            binding.viewMain.visibility = View.VISIBLE
            binding.viewProgress.visibility = View.GONE

        }

        private fun setupLayout() {


            binding.viewMain.visibility = View.GONE
            binding.viewProgress.visibility = View.VISIBLE
            binding.tvChatsDetails.visibility = View.VISIBLE

            binding.linearName.visibility=View.GONE

//            binding.ivChats.visibility = View.VISIBLE
//
//
            binding.imgChat.setImageResource(R.drawable.fb)
            binding.imgDocs.setImageResource(R.drawable.wa)
            binding.imgMedia.setImageResource(R.drawable.msg)
//
            binding.constfirst.background =
                applicationContext.resources.getDrawable(R.drawable.main_icons_back, null)
            binding.constsecond.background =
                applicationContext.resources.getDrawable(R.drawable.main_icons_back, null)
            binding.constthird.background =
                applicationContext.resources.getDrawable(R.drawable.main_icons_back, null)

            binding.tvChat.setTextColor(applicationContext.resources.getColor(R.color.black))
            binding.tvMedia.setTextColor(applicationContext.resources.getColor(R.color.black))
            binding.tvDocs.setTextColor(applicationContext.resources.getColor(R.color.black))

            binding.toolbarTitle.text = "Chats"
            binding.tvAllFiles.text = applicationContext.resources.getString(R.string.deleted_chats)

            binding.tvChat.text = applicationContext.resources.getString(R.string.messenger)
            binding.tvDocs.text = applicationContext.resources.getString(R.string.whatsapp)
            binding.tvMedia.text = applicationContext.resources.getString(R.string.mails)

            binding.tvTotalFilesDetails.text =
                applicationContext.resources.getString(R.string.total_chats)
            binding.tvChatsDetails.text = applicationContext.resources.getString(R.string.messenger)
            binding.tvDocsDetails.text = applicationContext.resources.getString(R.string.whatsapp)
            binding.tvMediaDetails.text = applicationContext.resources.getString(R.string.mails)

            binding.rvRecentDevices.visibility = View.GONE
            binding.tvNoDeletedFiles.visibility = View.VISIBLE
            binding.btnDelete.visibility=View.GONE

            binding.searchView.setOnQueryTextListener(queryTextChangeListener)
        }

        var mState = false
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.my_menu_aat, menu)
            menu?.findItem(R.id.mi_delete)?.isVisible = mState
            menu?.findItem(R.id.mi_share)?.isVisible = false
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.mi_delete -> {
//                    initRealms()
//                    if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
//                        MyDialoguesAAT.showDeleteWarning(this@ProfilesActivityAAT,
//                            object : MyDialoguesAAT.Companion.DeletionCallBack {
//                                override fun deleteFiles() {
//                                    mAdapterAAT?.selectedList?.forEach {
//
//                                        var realmResults: RealmResults<*>? = null
//                                        realmResults =
//                                            chatTable.where(ChatsModelClassAAT::class.java)
//                                                .equalTo("senderName", it.senderName).findAll()
//                                        chatTable.beginTransaction()
//                                        realmResults.deleteAllFromRealm()
//                                        chatTable.commitTransaction()
//                                        realmResults =
//                                            profileTable.where(ChatProfileModelClassAAT::class.java)
//                                                .equalTo("senderName", it.senderName).findAll()
//                                        profileTable.beginTransaction()
//                                        realmResults.deleteAllFromRealm()
//                                        profileTable.commitTransaction()
//                                        chats.remove(it)
//                                    }
//                                    mAdapterAAT?.selectedList?.clear()
//                                    if (chats.isEmpty()) {
//                                        binding.viewProgress.visibility = View.GONE
//                                        binding.viewMain.visibility = View.GONE
//                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
//                                    }
//                                    mAdapterAAT?.notifyDataSetChanged()
//
//
//                                    mState = false
//                                    invalidateOptionsMenu()
//
//                                }
//
//                                override fun dismiss() {
//
//                                }
//
//                            })
//                    }
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
            mState = size > 0
            if(mState) {
                binding.btnDelete.visibility = View.VISIBLE

            }else {
                binding.btnDelete.visibility = View.GONE

            }
        }
/*    fun loadFbBannerAd() {

        val adView = AdView(
            this@ProfilesActivity,
            this@ProfilesActivity.resources.getString(R.string.fb_banner_ad2),
            AdSize.BANNER_HEIGHT_50
        )
        val s = this@ProfilesActivity.resources.getString(R.string.fb_banner_ad2)
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
