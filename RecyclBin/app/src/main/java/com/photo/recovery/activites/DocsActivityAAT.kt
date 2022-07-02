package com.photo.recovery.activites

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.photo.recovery.R
import com.photo.recovery.adapters.RecentsAdapterAAT
import com.photo.recovery.ads.BannerAdHelperAAT
import com.photo.recovery.ads.isAppInstalledFromPlay
//import com.datarecovery.recyclebindatarecovery.ads.BannerAdHelperAAT
//import com.datarecovery.recyclebindatarecovery.ads.isAppInstalledFromPlay

import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.databinding.ActivityGeneralScreenAatBinding

import com.photo.recovery.dialogues.MyDialoguesAAT
import com.photo.recovery.models.AllFilesModelClassAAT
//import com.facebook.ads.*
import java.io.File


class DocsActivityAAT : AppCompatActivity(), SelectionCallBackAAT {

    companion object {
        var pdf = ArrayList<AllFilesModelClassAAT>()
        var word = ArrayList<AllFilesModelClassAAT>()
        var other = ArrayList<AllFilesModelClassAAT>()
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

    var mAdapterAAT: RecentsAdapterAAT? = null


    lateinit var binding: ActivityGeneralScreenAatBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityGeneralScreenAatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isAppInstalledFromPlay(this@DocsActivityAAT)){
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
//            BannerAdHelperAAT.loadNormalAdmobBanner(this,binding.bottomBanner)
        }


        binding.toolbar.setBackgroundResource(R.drawable.tb_docs)
        binding.toolbar.setNavigationIcon(R.drawable.ic_group_back_white)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.pink1)

        //imitates pdf
        binding.first.setOnClickListener {
            val intent = Intent(this@DocsActivityAAT, GeneralDocsAAT::class.java)
            intent.putExtra(MyConstantsAAT.FILE_TYPE_KEY, MyConstantsAAT.PDF_FILE_TYPE)
            startActivity(intent)
        }

        //imitates other
        binding.third.setOnClickListener {
            val intent = Intent(this@DocsActivityAAT, GeneralDocsAAT::class.java)
            intent.putExtra(MyConstantsAAT.FILE_TYPE_KEY, MyConstantsAAT.OTHER_FILE_TYPE)
            startActivity(intent)
        }

        //imitates word
        binding.second.setOnClickListener {
            val intent = Intent(this@DocsActivityAAT, GeneralDocsAAT::class.java)
            intent.putExtra(MyConstantsAAT.FILE_TYPE_KEY, MyConstantsAAT.WORD_FILE_TYPE)
            startActivity(intent)
        }
        binding.btnDelete.setOnClickListener(View.OnClickListener {
            if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
                MyDialoguesAAT.showDeleteWarning(this@DocsActivityAAT,
                    object : MyDialoguesAAT.Companion.DeletionCallBack {
                        override fun deleteFiles() {

                            object : AsyncTask<Void, Void, Void?>() {

                                override fun onPreExecute() {
                                    super.onPreExecute()
                                    binding.viewMain.visibility = View.GONE
                                    binding.viewProgress.visibility = View.VISIBLE
                                }

                                override fun doInBackground(vararg params: Void?): Void? {
                                    mAdapterAAT?.selectedList?.forEach {
                                        if (File(it.filePath).delete()) {
                                            HomeScreenAAT.docFiles.remove(it)
                                            when (it.subType) {
                                                MyConstantsAAT.PDF_FILE_TYPE -> {
                                                    pdf.remove(it)
                                                    HomeScreenAAT.docFiles.remove(it)
                                                }
                                                MyConstantsAAT.WORD_FILE_TYPE -> {
                                                    word.remove(it)
                                                    HomeScreenAAT.docFiles.remove(it)
                                                }
                                                MyConstantsAAT.OTHER_FILE_TYPE -> {
                                                    other.remove(it)
                                                    HomeScreenAAT.docFiles.remove(it)
                                                }
                                            }
                                        }
                                    }
                                    return null
                                }

                                override fun onPostExecute(result: Void?) {
                                    super.onPostExecute(result)
                                    mAdapterAAT?.selectedList?.clear()
                                    mAdapterAAT?.notifyDataSetChanged()

                                    mState = false
                                    invalidateOptionsMenu()

                                    if (HomeScreenAAT.docFiles.isEmpty()) {
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.rvRecentDevices.visibility = View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                    }
                                }

                            }.execute()


                        }

                        override fun dismiss() {
                            mAdapterAAT?.selectedList?.clear()
                            mAdapterAAT?.notifyDataSetChanged()

                            mState = false
                            invalidateOptionsMenu()

                            if (HomeScreenAAT.docFiles.isEmpty()) {
                                binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                binding.rvRecentDevices.visibility = View.GONE
                                binding.btnDelete.visibility=View.GONE
                            }
                        }

                    })
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setupLayout()

        populateMediaFiles()
    }

    override fun onDestroy() {
        super.onDestroy()
        pdf.clear()
        word.clear()
        other.clear()
    }

    private fun populateMediaFiles() {

        object : AsyncTask<Void, Void, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                pdf.clear()
                word.clear()
                other.clear()

                for (i in HomeScreenAAT.docFiles) {
                    when (i.subType) {
                        MyConstantsAAT.PDF_FILE_TYPE -> {
                            pdf.add(i)
                        }
                        MyConstantsAAT.WORD_FILE_TYPE -> {
                            word.add(i)
                        }
                        MyConstantsAAT.OTHER_FILE_TYPE -> {
                            other.add(i)
                        }
                    }
                }
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                binding.viewMain.visibility = View.VISIBLE
                binding.viewProgress.visibility = View.GONE
                updateView()
            }

        }.execute()

    }

    private fun updateView() {
        if (HomeScreenAAT.docFiles.isNotEmpty()) {
            binding.rvRecentDevices.visibility = View.VISIBLE
            binding.tvNoDeletedFiles.visibility = View.GONE


            mAdapterAAT = RecentsAdapterAAT(this, HomeScreenAAT.docFiles)
            binding.rvRecentDevices.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvRecentDevices.adapter = mAdapterAAT

            binding.tvTotalFilesDetails.text =
                "${applicationContext.getString(R.string.total_files)} (${HomeScreenAAT.docFiles.size})"
            binding.tvChatsDetails.text =
                "${applicationContext.getString(R.string.pdf)} (${pdf.size})"
            binding.tvDocsDetails.text =
                "${applicationContext.getString(R.string.word)} (${word.size})"
            binding.tvMediaDetails.text =
                "${applicationContext.getString(R.string.other)} (${other.size})"

        } else {
            binding.rvRecentDevices.visibility = View.GONE
            binding.tvNoDeletedFiles.visibility = View.VISIBLE
            binding.btnDelete.visibility=View.GONE

            binding.tvTotalFilesDetails.text =
                "${applicationContext.getString(R.string.total_files)} (${HomeScreenAAT.docFiles.size})"
            binding.tvChatsDetails.text =
                "${applicationContext.getString(R.string.pdf)} (${pdf.size})"
            binding.tvDocsDetails.text =
                "${applicationContext.getString(R.string.word)} (${word.size})"
            binding.tvMediaDetails.text =
                "${applicationContext.getString(R.string.other)} (${other.size})"
        }
    }

    private fun setupLayout() {
        binding.viewMain.visibility = View.GONE
        binding.viewProgress.visibility = View.VISIBLE
        binding.tvChatsDetails.visibility = View.VISIBLE
//        binding.ivChats.visibility = View.VISIBLE

        binding.linearName.visibility=View.GONE

        binding.imgChat.setImageResource(R.drawable.pdf)
        binding.imgDocs.setImageResource(R.drawable.wdf)
        binding.imgMedia.setImageResource(R.drawable.fdf)

        binding.constfirst.background =
            applicationContext.resources.getDrawable(R.drawable.main_icons_back, null)
        binding.constsecond.background =
            applicationContext.resources.getDrawable(R.drawable.main_icons_back, null)
        binding.constthird.background =
            applicationContext.resources.getDrawable(R.drawable.main_icons_back, null)


        binding.tvChat.setTextColor(applicationContext.resources.getColor(R.color.black))
        binding.tvMedia.setTextColor(applicationContext.resources.getColor(R.color.black))
        binding.tvDocs.setTextColor(applicationContext.resources.getColor(R.color.black))

        binding.tvAllFiles.text = applicationContext.resources.getString(R.string.deleted_docs)

        binding.toolbarTitle.text = "Docs"
        binding.tvChat.text = applicationContext.resources.getString(R.string.pdf)
        binding.tvDocs.text = applicationContext.resources.getString(R.string.word)
        binding.tvMedia.text = applicationContext.resources.getString(R.string.other)

        binding.tvChatsDetails.text = applicationContext.resources.getString(R.string.pdf)
        binding.tvDocsDetails.text = applicationContext.resources.getString(R.string.word)
        binding.tvMediaDetails.text = applicationContext.resources.getString(R.string.other)

        binding.rvRecentDevices.visibility = View.GONE
        binding.tvNoDeletedFiles.visibility = View.VISIBLE
        binding.btnDelete.visibility=View.GONE

        binding.searchView.setOnQueryTextListener(queryTextChangeListener)

    }

    var mState = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu_aat, menu)
        menu?.findItem(R.id.mi_delete)?.isVisible = mState
        menu?.findItem(R.id.mi_share)?.isVisible = mState
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_delete -> {
//                if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
//                    MyDialoguesAAT.showDeleteWarning(this@DocsActivityAAT,
//                        object : MyDialoguesAAT.Companion.DeletionCallBack {
//                            override fun deleteFiles() {
//
//                                object : AsyncTask<Void, Void, Void?>() {
//
//                                    override fun onPreExecute() {
//                                        super.onPreExecute()
//                                        binding.viewMain.visibility = View.GONE
//                                        binding.viewProgress.visibility = View.VISIBLE
//                                    }
//
//                                    override fun doInBackground(vararg params: Void?): Void? {
//                                        mAdapterAAT?.selectedList?.forEach {
//                                            if (File(it.filePath).delete()) {
//                                                HomeScreenAAT.docFiles.remove(it)
//                                                when (it.subType) {
//                                                    MyConstantsAAT.PDF_FILE_TYPE -> {
//                                                        pdf.remove(it)
//                                                        HomeScreenAAT.docFiles.remove(it)
//                                                    }
//                                                    MyConstantsAAT.WORD_FILE_TYPE -> {
//                                                        word.remove(it)
//                                                        HomeScreenAAT.docFiles.remove(it)
//                                                    }
//                                                    MyConstantsAAT.OTHER_FILE_TYPE -> {
//                                                        other.remove(it)
//                                                        HomeScreenAAT.docFiles.remove(it)
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        return null
//                                    }
//
//                                    override fun onPostExecute(result: Void?) {
//                                        super.onPostExecute(result)
//                                        mAdapterAAT?.selectedList?.clear()
//                                        mAdapterAAT?.notifyDataSetChanged()
//
//                                        mState = false
//                                        invalidateOptionsMenu()
//
//                                        if (HomeScreenAAT.docFiles.isEmpty()) {
//                                            binding.tvNoDeletedFiles.visibility = View.VISIBLE
//                                            binding.rvRecentDevices.visibility = View.GONE
//                                        }
//                                    }
//
//                                }.execute()
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
            R.id.mi_share -> {


                val intent = Intent()
                intent.action = Intent.ACTION_SEND_MULTIPLE
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.")
                intent.type = "*/*" /* This example is sharing jpeg images. */
                val files = java.util.ArrayList<Uri>()
                mAdapterAAT?.selectedList?.forEach { it ->
                    val file = File(it.filePath)
                    val uri = FileProvider.getUriForFile(
                        applicationContext,
                        applicationContext.resources.getString(R.string.authority),
                        file
                    )
                    files.add(uri)
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
                startActivity(intent)
                mAdapterAAT?.selectedList?.clear()
                mState = false
                invalidateOptionsMenu()
                mAdapterAAT?.notifyDataSetChanged()
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

  /*  fun loadFbBannerAd() {

        val adView = AdView(
            this@DocsActivity,
            this@DocsActivity.resources.getString(R.string.fb_banner_ad2),
            AdSize.BANNER_HEIGHT_50
        )
        val s = this@DocsActivity.resources.getString(R.string.fb_banner_ad2)
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