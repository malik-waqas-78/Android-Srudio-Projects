package com.photo.recovery.activites

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.photo.recovery.R
import com.photo.recovery.adapters.ChatsAdapterAAT
import com.photo.recovery.ads.BannerAdHelperAAT
import com.photo.recovery.ads.isAppInstalledFromPlay
//import com.datarecovery.recyclebindatarecovery.ads.BannerAdHelperAAT
//import com.datarecovery.recyclebindatarecovery.ads.isAppInstalledFromPlay
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.databinding.ActivityGeneralChatsAatBinding

import com.photo.recovery.dialogues.MyDialoguesAAT
import com.photo.recovery.models.ChatsAAT
import com.photo.recovery.models.ChatsModelClassAAT
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.Sort

class GeneralChatsAAT : AppCompatActivity(), SelectionCallBackAAT {

    var profileName:String? = ""
    var realmChats: Realm? = null
    lateinit var binding: ActivityGeneralChatsAatBinding
    var chats = ArrayList<ChatsAAT>()

    var chatsAdapterAAT: ChatsAdapterAAT? = null

    var queryTextChangeListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            chatsAdapterAAT?.filter?.filter(newText)
            return false
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralChatsAatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isAppInstalledFromPlay(this@GeneralChatsAAT)){
//            loadFbBannerAd()
//            loadAdmobBannerAd()
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
//            BannerAdHelperAAT.loadNormalAdmobBanner(this,binding.bottomBanner)
        }



        binding.tbGeneralChats.setNavigationIcon(R.drawable.ic_group_back_white)
        binding.tbGeneralChats.setNavigationOnClickListener {
            finish()
        }
        if (intent.hasExtra(MyConstantsAAT.PROFILE_NAME_KEY)) {
            profileName = intent.getStringExtra(MyConstantsAAT.PROFILE_NAME_KEY)
        }

        binding.btnDelete.setOnClickListener(View.OnClickListener {
            if (chatsAdapterAAT?.selectedList?.isNotEmpty() == true) {
                MyDialoguesAAT.showDeleteWarning(this@GeneralChatsAAT,
                    object : MyDialoguesAAT.Companion.DeletionCallBack {
                        override fun deleteFiles() {
                            chatsAdapterAAT?.selectedList?.forEach {

                                var realmResults: RealmResults<*>? = null
                                realmResults =
                                    realmChats?.where(ChatsModelClassAAT::class.java)
                                        ?.equalTo("id", it.id)?.findAll()
                                realmChats?.beginTransaction()
                                realmResults?.deleteAllFromRealm()
                                realmChats?.commitTransaction()
                                chats.remove(it)
                            }
                            chatsAdapterAAT?.selectedList?.clear()

                            if (chats.isEmpty()) {
                                binding.viewMain.visibility = View.GONE
                                binding.viewEmpty.visibility = View.VISIBLE
                            }
                            chatsAdapterAAT?.notifyDataSetChanged()


                            mState = false
                            invalidateOptionsMenu()


                        }

                        override fun dismiss() {
                            chatsAdapterAAT?.selectedList?.clear()

                            if (chats.isEmpty()) {
                                binding.viewMain.visibility = View.GONE
                                binding.viewEmpty.visibility = View.VISIBLE
                            }
                            chatsAdapterAAT?.notifyDataSetChanged()


                            mState = false
                            invalidateOptionsMenu()
                        }

                    })
            }
        })


    }

    override fun onResume() {
        super.onResume()
        initRealm()

        getMessagesList()

        setupLayout()
    }

    private fun setupLayout() {

        when (GeneralProfileAAT.MessengerType) {
            MyConstantsAAT.TYPE_WHATSAPP -> {
                GeneralProfileAAT.color = R.color.appcolorgreen
                binding.toolbarTitle.text = profileName
                binding.tbGeneralChats.setBackgroundColor(resources.getColor(R.color.appcolorgreen))
                val window: Window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = resources.getColor(R.color.appcolorgreen)
            }
            MyConstantsAAT.TYPE_MAIL -> {
                GeneralProfileAAT.color = R.color.pink1
                binding.toolbarTitle.text  = profileName
                binding.tbGeneralChats.setBackgroundColor(resources.getColor(R.color.pink1))
                val window: Window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = resources.getColor(R.color.pink1)
            }
            MyConstantsAAT.TYPE_MESSENGER -> {
                GeneralProfileAAT.color = R.color.purple
                binding.toolbarTitle.text  = profileName
                binding.tbGeneralChats.setBackgroundColor(resources.getColor(R.color.purple))
                val window: Window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = resources.getColor(R.color.purple)
            }
        }
        binding.searchView.setOnQueryTextListener(queryTextChangeListener)
        binding.rvMessages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        chatsAdapterAAT = ChatsAdapterAAT(this, chats)
        binding.rvMessages.adapter = chatsAdapterAAT
    }

    private fun getMessagesList() {

        chats.clear()

        realmChats?.where(ChatsModelClassAAT::class.java)
            ?.sort("timeInMillis", Sort.DESCENDING)
            ?.equalTo("messengerType", GeneralProfileAAT.MessengerType)
            ?.equalTo("senderName", profileName)?.findAll()?.forEach {
                chats.add(
                    ChatsAAT(
                        it.timeInMillis,
                        it.senderName,
                        it.textMsg,
                        it.icon,
                        it.date,
                        it.messengerType
                    )
                )
            }

        if (chats.isNotEmpty()) {
            binding.viewMain.visibility = View.VISIBLE
            binding.viewEmpty.visibility = View.GONE
        } else {
            binding.viewMain.visibility = View.GONE
            binding.viewEmpty.visibility = View.VISIBLE
        }


    }

    private fun initRealm() {
        val realmProfile = RealmConfiguration.Builder()
            .name("chats")
            .deleteRealmIfMigrationNeeded()
            .build()
        realmChats = Realm.getInstance(realmProfile)
    }

    var mState = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu_aat, menu)
        menu?.findItem(R.id.mi_delete)?.isVisible = mState
        menu?.findItem(R.id.mi_share)?.isVisible=mState
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_delete -> {

//                if (chatsAdapterAAT?.selectedList?.isNotEmpty() == true) {
//                    MyDialoguesAAT.showDeleteWarning(this@GeneralChatsAAT,
//                        object : MyDialoguesAAT.Companion.DeletionCallBack {
//                            override fun deleteFiles() {
//                                chatsAdapterAAT?.selectedList?.forEach {
//
//                                    var realmResults: RealmResults<*>? = null
//                                    realmResults =
//                                        realmChats?.where(ChatsModelClassAAT::class.java)
//                                            ?.equalTo("id", it.id)?.findAll()
//                                    realmChats?.beginTransaction()
//                                    realmResults?.deleteAllFromRealm()
//                                    realmChats?.commitTransaction()
//                                    chats.remove(it)
//                                }
//                                chatsAdapterAAT?.selectedList?.clear()
//
//                                if (chats.isEmpty()) {
//                                    binding.viewMain.visibility = View.GONE
//                                    binding.viewEmpty.visibility = View.VISIBLE
//                                }
//                                chatsAdapterAAT?.notifyDataSetChanged()
//
//
//                                mState = false
//                                invalidateOptionsMenu()
//
//
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
            R.id.mi_share->{
                var msg=""
                chatsAdapterAAT?.selectedList?.forEach {
                    msg+=it.textMsg+"\n"
                }

                val intent = Intent()
                intent.action = Intent.ACTION_SEND_MULTIPLE
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some Text msgs.")
                intent.type = "text/*" /* This example is sharing jpeg images. */
                intent.putExtra(Intent.EXTRA_TEXT, msg)
                startActivity(intent)
                chatsAdapterAAT?.selectedList?.clear()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    override fun selectionChanged(size: Int) {
        mState = size > 0
         if(mState) {
            binding.btnDelete.visibility = View.VISIBLE
        }else {
            binding.btnDelete.visibility = View.GONE
        }
    }

   /* fun loadFbBannerAd() {

        val adView = AdView(
            this@GeneralChats,
            this@GeneralChats.resources.getString(R.string.fb_banner_ad2),
            AdSize.BANNER_HEIGHT_50
        )
        val s = this@GeneralChats.resources.getString(R.string.fb_banner_ad2)
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