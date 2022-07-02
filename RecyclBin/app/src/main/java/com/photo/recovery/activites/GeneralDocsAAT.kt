package com.photo.recovery.activites

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible

import androidx.recyclerview.widget.GridLayoutManager
import com.photo.recovery.R
import com.photo.recovery.adapters.DocsAdapter
import com.photo.recovery.ads.BannerAdHelperAAT
import com.photo.recovery.ads.InterAdHelperAAT
import com.photo.recovery.ads.NativeAdHelperAAT
import com.photo.recovery.ads.isAppInstalledFromPlay
//import com.datarecovery.recyclebindatarecovery.ads.BannerAdHelperAAT
//import com.datarecovery.recyclebindatarecovery.ads.InterAdHelper
//import com.datarecovery.recyclebindatarecovery.ads.NativeAdHelperAAT
//import com.datarecovery.recyclebindatarecovery.ads.isAppInstalledFromPlay
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.databinding.ActivityGeneralMediaAatBinding


import com.photo.recovery.dialogues.MyDialoguesAAT
import com.photo.recovery.models.AllFilesModelClassAAT
import com.photo.recovery.utils.LoadTrashFilesAAT
import com.photo.recovery.utils.RecoverFilesAAT

import java.io.File

class GeneralDocsAAT : AppCompatActivity(), SelectionCallBackAAT {
    var fileType: String? = ""
    var docsFile = ArrayList<AllFilesModelClassAAT>()
    var mAdapterAAT: DocsAdapter? = null
    var recover = false
    val checkboxListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            mAdapterAAT?.selectedList?.clear()
            mAdapterAAT?.files?.let { mAdapterAAT?.selectedList?.addAll(it) }
            mState = true
        } else {
            mAdapterAAT?.selectedList?.clear()
            mState = false
        }
        invalidateOptionsMenu()
        mAdapterAAT?.notifyDataSetChanged()

    }

    lateinit var binding: ActivityGeneralMediaAatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgColor)

        binding = ActivityGeneralMediaAatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (isAppInstalledFromPlay(this@GeneralDocsAAT)) {
            // loadFbBannerAd()
            //loadAdmobBannerAd()
            BannerAdHelperAAT.loadMediatedAdmobBanner(this, binding.topBanner)
        }
//        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        toggleCheckBox(false)
        binding.viewDocBtn.visibility = View.VISIBLE
        binding.viewBtn.visibility = View.GONE

        if (intent.hasExtra(MyConstantsAAT.FILE_TYPE_KEY)) {
            fileType = intent.getStringExtra(MyConstantsAAT.FILE_TYPE_KEY)
        } else {
            fileType = ""
        }


        /*fileType = if (intent.hasExtra(MyConstantsAAT.FILE_TYPE_KEY)) ({
            intent.getStringExtra(MyConstantsAAT.FILE_TYPE_KEY)
        }) else {
            ""
        }*/

        binding.recoverFiles.setOnClickListener {


            val filesToRecover =
                mAdapterAAT?.selectedList?.let { ArrayList<AllFilesModelClassAAT>(it) }

            if (!filesToRecover.isNullOrEmpty()) {
                binding.viewMain.visibility = View.GONE
                binding.viewBtn.visibility = View.GONE
                binding.viewProgress.visibility = View.VISIBLE
                binding.tvProgressText.text = getString(R.string.recovering_files)
                //show alert dialogue perogress bar

                RecoverFilesAAT(
                    this@GeneralDocsAAT,
                    filesToRecover,
                    object : RecoverFilesAAT.RecoverFilesCallBack {
                        override fun filesRecoveryDone() {

                            binding.viewMain.visibility = View.VISIBLE
                            binding.viewBtn.visibility = View.GONE
                            binding.viewProgress.visibility = View.GONE
                            binding.tvProgressText.text = getString(R.string.scanning_files)
                            mAdapterAAT?.selectedList?.let { it1 ->
                                mAdapterAAT?.files?.removeAll(
                                    it1
                                )
                            }
                            mAdapterAAT?.selectedList?.let { it1 ->
                                DocsActivityAAT?.pdf?.removeAll(
                                    it1
                                )
                            }
                            mAdapterAAT?.selectedList?.let { it1 ->
                                DocsActivityAAT?.word?.removeAll(
                                    it1
                                )
                            }
                            mAdapterAAT?.selectedList?.let { it1 ->
                                DocsActivityAAT?.other?.removeAll(
                                    it1
                                )
                            }
                            mAdapterAAT?.selectedList?.let { it1 -> docsFile?.removeAll(it1) }
                            mAdapterAAT?.selectedList?.let { it1 ->
                                HomeScreenAAT?.docFiles.removeAll(
                                    it1
                                )
                            }


                            MyDialoguesAAT.showRecoveryCompleted(
                                this@GeneralDocsAAT,
                                mAdapterAAT?.selectedList?.size.toString()
                            )
                            mAdapterAAT?.selectedList?.clear()
                            mState = false
                            invalidateOptionsMenu()
                            mAdapterAAT?.notifyDataSetChanged()
                            if (mAdapterAAT?.files?.isEmpty() == true) {
                                binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                binding.btnDelete.visibility = View.GONE
                                binding.rvFiles.visibility = View.GONE
                                binding.cbSelectAll.visibility = View.GONE
                            }
                        }

                    }).recoverFiles()
            }


        }
        binding.btnDocScan.setOnClickListener {

            InterAdHelperAAT.adCallBack = object : InterAdHelperAAT.Companion.AdLoadCallBack {
                override fun adClosed() {
                    NativeAdHelperAAT.showAdmobNativeAd(this@GeneralDocsAAT, binding.adFrame)
                    binding.tvProgressText.text = resources.getString(R.string.scanning_files)

                    binding.animationViewa.setAnimation(R.raw.lottienormalscan)
                    binding.animationViewa.playAnimation()
                    setupLayout()
                }
            }
            if(InterAdHelperAAT.mediatedInterstitialAdGoogle!=null) {
                InterAdHelperAAT.showMediatedAdmobIntersitial(this@GeneralDocsAAT)
            }else{
                  NativeAdHelperAAT.showAdmobNativeAd(this@GeneralDocsAAT, binding.adFrame)
                    binding.tvProgressText.text = resources.getString(R.string.scanning_files)

                    binding.animationViewa.setAnimation(R.raw.lottienormalscan)
                    binding.animationViewa.playAnimation()
                    setupLayout()
            }

        }

        binding.btnDelete.setOnClickListener(View.OnClickListener {
            if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
                MyDialoguesAAT.showDeleteWarning(this@GeneralDocsAAT,
                    object : MyDialoguesAAT.Companion.DeletionCallBack {
                        override fun deleteFiles() {
                            object : AsyncTask<Void, Void, Void?>() {
                                override fun onPreExecute() {
                                    super.onPreExecute()
                                    binding.viewMain.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE
                                    binding.viewProgress.visibility = View.VISIBLE
                                    binding.tvProgressText.text = getString(R.string.deleting_files)
                                }

                                override fun doInBackground(vararg params: Void?): Void? {
                                    mAdapterAAT?.selectedList?.forEach {
                                        if (File(it.filePath).delete()) {
                                            when (it.subType) {
                                                MyConstantsAAT.PDF_FILE_TYPE -> {
                                                    docsFile.remove(it)
                                                    HomeScreenAAT.docFiles.remove(it)
                                                    DocsActivityAAT.pdf.remove(it)
                                                }
                                                MyConstantsAAT.WORD_FILE_TYPE -> {
                                                    docsFile.remove(it)
                                                    HomeScreenAAT.docFiles.remove(it)
                                                    DocsActivityAAT.word.remove(it)
                                                }
                                                MyConstantsAAT.OTHER_FILE_TYPE -> {
                                                    docsFile.remove(it)
                                                    HomeScreenAAT.docFiles.remove(it)
                                                    DocsActivityAAT.other.remove(it)
                                                }
                                            }
                                        }

                                    }
                                    return null
                                }

                                override fun onPostExecute(result: Void?) {
                                    super.onPostExecute(result)
                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewBtn.visibility = View.GONE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.tvProgressText.text = getString(R.string.scanning_files)

                                    mAdapterAAT?.selectedList?.clear()
                                    mAdapterAAT?.notifyDataSetChanged()

                                    mState = false
                                    invalidateOptionsMenu()

                                    if (docsFile.isEmpty()) {
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.btnDelete.visibility = View.GONE
                                    }
                                }

                            }.execute()


                        }

                        override fun dismiss() {
                            binding.viewMain.visibility = View.VISIBLE
                            binding.viewBtn.visibility = View.GONE
                            binding.viewProgress.visibility = View.GONE
                            binding.tvProgressText.text = getString(R.string.scanning_files)

                            mAdapterAAT?.selectedList?.clear()
                            mAdapterAAT?.notifyDataSetChanged()

                            mState = false
                            invalidateOptionsMenu()

                            if (docsFile.isEmpty()) {
                                binding.rvFiles.visibility = View.GONE
                                binding.cbSelectAll.visibility = View.GONE
                                binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                binding.btnDelete.visibility = View.GONE
                            }
                        }

                    })
            }
        })

        binding.btnRecoveredDocFiles.setOnClickListener {

            NativeAdHelperAAT.showAdmobNativeAd(this@GeneralDocsAAT, binding.adFrame)
            binding.tvProgressText.text = resources.getString(R.string.scanning_files)

            binding.animationViewa.setAnimation(R.raw.lottienormalscan)
            binding.animationViewa.playAnimation()
            recoverySetUp()
        }

        when (fileType) {
            MyConstantsAAT.PDF_FILE_TYPE -> {
                //binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.pdf_files)

                binding.toolbarTitle.text =
                    applicationContext.resources.getString(R.string.pdf_files)
//                supportActionBar?.setBackgroundDrawable(
//                    applicationContext.resources.getDrawable(
//                        R.drawable.tb_docs,
//                        null
//                    )
//                )
//                val window: Window = window
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = resources.getColor(R.color.pdf_sc)

//                binding.clDocBack.setBackgroundColor(resources.getColor(R.color.pdf_red))
//                binding.btnDocScan.background =
//                    ContextCompat.getDrawable(this@GeneralDocsAAT, R.drawable.ic_pdf_shape)
//                binding.btnDocScan.setImageResource(R.drawable.ic_scan_pdf)
//
//                binding.btnRecoveredDocFiles.background =
//                    ContextCompat.getDrawable(this@GeneralDocsAAT, R.drawable.ic_pdf_shape)
//                binding.btnRecoveredDocFiles.setImageResource(R.drawable.ic_recover_pdf)
//                binding.recoverFiles.setBackgroundColor(resources.getColor(R.color.pdf_sc))


            }


            MyConstantsAAT.WORD_FILE_TYPE -> {
                // binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.word_files)

                binding.toolbarTitle.text =
                    applicationContext.resources.getString(R.string.word_files)
//                supportActionBar?.setBackgroundDrawable(
//                    applicationContext.resources.getDrawable(
//                        R.drawable.tb_word,
//                        null
//                    )
//                )
//                val window: Window = window
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = resources.getColor(R.color.word_sc)

//                binding.clDocBack.setBackgroundColor(resources.getColor(R.color.word_blue))
//                binding.btnDocScan.background =
//                    ContextCompat.getDrawable(this@GeneralDocsAAT, R.drawable.ic_wrod_shape)
//                binding.btnDocScan.setImageResource(R.drawable.ic_scan_wrod)
//
//                binding.btnRecoveredDocFiles.background =
//                    ContextCompat.getDrawable(this@GeneralDocsAAT, R.drawable.ic_wrod_shape)
//                binding.btnRecoveredDocFiles.setImageResource(R.drawable.ic_recover_word)
//                binding.recoverFiles.setBackgroundColor(resources.getColor(R.color.word_sc))

            }
            MyConstantsAAT.OTHER_FILE_TYPE -> {
                // binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.other_files)
                binding.toolbarTitle.text =
                    applicationContext.resources.getString(R.string.other_files)
//                supportActionBar?.setBackgroundDrawable(
//                    applicationContext.resources.getDrawable(
//                        R.drawable.tb_other,
//                        null
//                    )
//
//                )
//                val window: Window = window
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = resources.getColor(R.color.others_sc)


//                binding.clDocBack.setBackgroundColor(resources.getColor(R.color.other_sky_blue))
//                binding.btnDocScan.background =
//                    ContextCompat.getDrawable(this@GeneralDocsAAT, R.drawable.ic_other_shape)
//                binding.btnDocScan.setImageResource(R.drawable.ic_scan_other)
//
//                binding.btnRecoveredDocFiles.background =
//                    ContextCompat.getDrawable(this@GeneralDocsAAT, R.drawable.ic_other_shape)
//                binding.btnRecoveredDocFiles.setImageResource(R.drawable.ic_recover_other)
//                binding.recoverFiles.setBackgroundColor(resources.getColor(R.color.others_sc))
            }
        }
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        InterAdHelperAAT.adCallBack = null
        super.onDestroy()
    }

    override fun onBackPressed() {

        if (binding.viewMain.isVisible) {
            binding.viewBtn.visibility = View.GONE
            binding.viewDocBtn.visibility = View.VISIBLE
            binding.viewMain.visibility = View.GONE
            binding.viewProgress.visibility = View.GONE
            mAdapterAAT = null
            //deepScanAdapter = null
            mState = false
            if (mState) {
                binding.btnDelete.visibility = View.VISIBLE
                binding.recoverFiles.visibility = View.VISIBLE
            } else {
                binding.btnDelete.visibility = View.GONE
                binding.recoverFiles.visibility = View.GONE
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun setupLayout() {
        binding.viewBtn.visibility = View.GONE
        binding.viewDocBtn.visibility = View.GONE
        binding.viewMain.visibility = View.GONE
        binding.viewProgress.visibility = View.VISIBLE
        recover = false
        docsFile.clear()
        when (fileType) {
            MyConstantsAAT.PDF_FILE_TYPE -> {

                //binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.pdf_files)
                android.os.Handler().postDelayed({
                    docsFile.addAll(DocsActivityAAT.pdf)

                    setupAdapter()
                }, 4000)

            }


            MyConstantsAAT.WORD_FILE_TYPE -> {
                // binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.word_files)
                android.os.Handler().postDelayed({
                    docsFile.addAll(DocsActivityAAT.word)
                    setupAdapter()
                }, 4000)

            }
            MyConstantsAAT.OTHER_FILE_TYPE -> {
                // binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.other_files)
                android.os.Handler().postDelayed({
                    docsFile.addAll(DocsActivityAAT.other)
                    setupAdapter()
                }, 4000)
            }
        }


    }

    fun recoverySetUp() {
        binding.viewBtn.visibility = View.GONE
        binding.viewDocBtn.visibility = View.GONE
        binding.viewMain.visibility = View.GONE
        binding.viewProgress.visibility = View.VISIBLE
        recover = true
        docsFile.clear()
        when (fileType) {
            MyConstantsAAT.PDF_FILE_TYPE -> {
                //binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.pdf_files)

                android.os.Handler().postDelayed({
                    LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                        override fun trashLoaded(files: ArrayList<File>) {

                            docsFile.clear()
                            files.forEach {
                                docsFile.add(
                                    AllFilesModelClassAAT(
                                        this@GeneralDocsAAT,
                                        it.absolutePath,
                                        it.name,
                                        it.lastModified(),
                                        MyConstantsAAT.DOC_FILE_TYPE,
                                        MyConstantsAAT.PDF_FILE_TYPE,
                                        false
                                    )
                                )
                            }

                            setupAdapter()
                        }

                    }).loadAllDocRecoverFiles(MyConstantsAAT.PDF_FILE_TYPE)
                }, 4000)


            }


            MyConstantsAAT.WORD_FILE_TYPE -> {
                // binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.word_files)


                android.os.Handler().postDelayed({
                    LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                        override fun trashLoaded(files: ArrayList<File>) {

                            docsFile.clear()
                            files.forEach {
                                docsFile.add(
                                    AllFilesModelClassAAT(
                                        this@GeneralDocsAAT,
                                        it.absolutePath,
                                        it.name,
                                        it.lastModified(),
                                        MyConstantsAAT.DOC_FILE_TYPE,
                                        MyConstantsAAT.WORD_FILE_TYPE,
                                        false
                                    )
                                )
                            }

                            setupAdapter()
                        }

                    }).loadAllDocRecoverFiles(MyConstantsAAT.WORD_FILE_TYPE)
                }, 4000)


            }

            MyConstantsAAT.OTHER_FILE_TYPE -> {
                // binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.other_files)
                android.os.Handler().postDelayed({
                    LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                        override fun trashLoaded(files: ArrayList<File>) {

                            docsFile.clear()
                            files.forEach {
                                docsFile.add(
                                    AllFilesModelClassAAT(
                                        this@GeneralDocsAAT,
                                        it.absolutePath,
                                        it.name,
                                        it.lastModified(),
                                        MyConstantsAAT.DOC_FILE_TYPE,
                                        MyConstantsAAT.OTHER_FILE_TYPE,
                                        false
                                    )
                                )
                            }

                            setupAdapter()
                        }

                    }).loadAllDocRecoverFiles(MyConstantsAAT.OTHER_FILE_TYPE)
                }, 4000)


            }
        }


    }

    fun setupAdapter() {
        //set adapter and recycle view
        binding.rvFiles.layoutManager = GridLayoutManager(this, 1)
        mAdapterAAT = DocsAdapter(this@GeneralDocsAAT, docsFile)
        binding.rvFiles.adapter = mAdapterAAT

        binding.viewBtn.visibility = View.GONE
        binding.viewDocBtn.visibility = View.GONE
        binding.viewMain.visibility = View.VISIBLE
        binding.viewProgress.visibility = View.GONE

        if (docsFile.isEmpty()) {
//            binding.viewMain.visibility=View.GONE

            binding.tvNoDeletedFiles.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.GONE
            binding.rvFiles.visibility = View.GONE
            binding.cbSelectAll.visibility = View.GONE
        } else {

            binding.tvNoDeletedFiles.visibility = View.GONE

            binding.rvFiles.visibility = View.VISIBLE
            binding.cbSelectAll.visibility = View.VISIBLE
        }
    }


    var mState = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu_aat, menu)
        menu?.findItem(R.id.mi_delete)?.isVisible = mState
        menu?.findItem(R.id.mi_share)?.isVisible = mState
        if (mState && !recover) {
            binding.recoverFiles.visibility = View.VISIBLE
        } else {
            binding.recoverFiles.visibility = View.GONE
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_delete -> {
//                if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
//                    MyDialoguesAAT.showDeleteWarning(this@GeneralDocsAAT,
//                        object : MyDialoguesAAT.Companion.DeletionCallBack {
//                            override fun deleteFiles() {
//                                object : AsyncTask<Void,Void,Void?>() {
//                                    override fun onPreExecute() {
//                                        super.onPreExecute()
//                                        binding.viewMain.visibility = View.GONE
//                                        binding.viewBtn.visibility = View.GONE
//                                        binding.viewProgress.visibility = View.VISIBLE
//                                        binding.tvProgressText.text =getString(R.string.deleting_files)
//                                    }
//                                    override fun doInBackground(vararg params: Void?): Void? {
//                                        mAdapterAAT?.selectedList?.forEach {
//                                            if (File(it.filePath).delete()) {
//                                                when (it.subType) {
//                                                    MyConstantsAAT.PDF_FILE_TYPE -> {
//                                                        docsFile.remove(it)
//                                                        HomeScreenAAT.docFiles.remove(it)
//                                                        DocsActivityAAT.pdf.remove(it)
//                                                    }
//                                                    MyConstantsAAT.WORD_FILE_TYPE -> {
//                                                        docsFile.remove(it)
//                                                        HomeScreenAAT.docFiles.remove(it)
//                                                        DocsActivityAAT.word.remove(it)
//                                                    }
//                                                    MyConstantsAAT.OTHER_FILE_TYPE -> {
//                                                        docsFile.remove(it)
//                                                        HomeScreenAAT.docFiles.remove(it)
//                                                        DocsActivityAAT.other.remove(it)
//                                                    }
//                                                }
//                                            }
//
//                                        }
//                                        return null
//                                    }
//
//                                    override fun onPostExecute(result: Void?) {
//                                        super.onPostExecute(result)
//                                        binding.viewMain.visibility = View.VISIBLE
//                                        binding.viewBtn.visibility = View.GONE
//                                        binding.viewProgress.visibility = View.GONE
//                                        binding.tvProgressText.text = getString(R.string.scanning_files)
//
//                                        mAdapterAAT?.selectedList?.clear()
//                                        mAdapterAAT?.notifyDataSetChanged()
//
//                                        mState = false
//                                        invalidateOptionsMenu()
//
//                                        if (docsFile.isEmpty()) {
//                                            binding.rvFiles.visibility = View.GONE
//                                            binding.cbSelectAll.visibility = View.GONE
//                                            binding.tvNoDeletedFiles.visibility = View.VISIBLE
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
        if (mState) {
            binding.btnDelete.visibility = View.VISIBLE
            binding.recoverFiles.visibility = View.VISIBLE
        } else {
            binding.btnDelete.visibility = View.GONE
            binding.recoverFiles.visibility = View.GONE
        }
        toggleCheckBox(mAdapterAAT?.files?.size == size)

    }

    fun toggleCheckBox(state: Boolean) {
        binding.cbSelectAll.setOnCheckedChangeListener(null)
        binding.cbSelectAll.isChecked = state
        binding.cbSelectAll.setOnCheckedChangeListener(checkboxListener)
    }
    /*  fun loadFbBannerAd() {

          val adView = AdView(
              this@GeneralDocs,
              this@GeneralDocs.resources.getString(R.string.fb_banner_ad2),
              AdSize.BANNER_HEIGHT_50
          )
          val s = this@GeneralDocs.resources.getString(R.string.fb_banner_ad2)
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