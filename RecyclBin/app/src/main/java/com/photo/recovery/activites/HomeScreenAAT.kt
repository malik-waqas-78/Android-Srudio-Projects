package com.photo.recovery.activites

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.photo.recovery.R
import com.photo.recovery.adapters.RecentsAdapterAAT
import com.photo.recovery.ads.*
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.databinding.ActivityGeneralScreenAatBinding
import com.photo.recovery.databinding.DbGeneralAatBinding
import com.photo.recovery.dialogues.MyDialoguesAAT
import com.photo.recovery.models.AllFilesModelClassAAT
//import com.facebook.ads.Ad
//import com.facebook.ads.AdError
//import com.facebook.ads.AdSize
//import com.facebook.ads.AdView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class HomeScreenAAT : AppCompatActivity(), SelectionCallBackAAT {
    private lateinit var binding: ActivityGeneralScreenAatBinding

    companion object {
        val mediaFiles = ArrayList<AllFilesModelClassAAT>()
        val docFiles = ArrayList<AllFilesModelClassAAT>()
    }

    val allFiles = ArrayList<AllFilesModelClassAAT>()

    var mAdapterAAT: RecentsAdapterAAT? = null

    var queryTextChangeListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            mAdapterAAT?.filter?.filter(newText)
            return false
        }

    }

    private var fileLoadingTask: LoadRecycleBinFiles? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgColor)

        binding = ActivityGeneralScreenAatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (isAppInstalledFromPlay(this@HomeScreenAAT)) {
//            loadFbBannerAd()
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
//            BannerAdHelperAAT.loadNormalAdmobBanner(this,binding.bottomBanner)
        }
//        setSupportActionBar(binding.toolbar)
//        binding.toolbar.navigationIcon = null
        //load files in recycle bin


//        com.google.android.ads.mediationtestsuite.MediationTestSuite.launch(this@HomeScreen)

//        val window: Window = window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = resources.getColor(R.color.home_status_color)


//        binding.zero.visibility = View.VISIBLE
        binding.searchView.setOnQueryTextListener(queryTextChangeListener)
        binding.tvChatsDetails.visibility = View.GONE

//        binding.zero.setOnClickListener {
//
//        }

        binding.first.setOnClickListener {
            startActivity(Intent(this@HomeScreenAAT, ProfilesActivityAAT::class.java))
        }

        binding.third.setOnClickListener {
            startActivity(Intent(this@HomeScreenAAT, MediaActivityAAT::class.java))
        }

        binding.second.setOnClickListener {
            startActivity(Intent(this@HomeScreenAAT, DocsActivityAAT::class.java))
        }


        /*binding.btnDeepScan.setOnClickListener {
            startActivity(Intent(this@HomeScreen,DataRecoveryActivity::class.java))
        }*/
        binding.btnDelete.setOnClickListener(View.OnClickListener {
            if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
                binding.viewMain.visibility = View.GONE
                binding.viewProgress.visibility = View.VISIBLE
                MyDialoguesAAT.showDeleteWarning(this@HomeScreenAAT,
                    object : MyDialoguesAAT.Companion.DeletionCallBack {
                        override fun deleteFiles() {
                            object : AsyncTask<Void, Void, Void?>() {
                                override fun doInBackground(vararg params: Void?): Void? {
                                    mAdapterAAT?.selectedList?.forEach {
                                        if (File(it.filePath).delete()) {
                                            allFiles.remove(it)
                                            if (it.fileType == MyConstantsAAT.DOC_FILE_TYPE) {
                                                docFiles.remove(it)
                                            } else {
                                                mediaFiles.remove(it)
                                            }
                                        }
                                    }
                                    return null
                                }

                                override fun onPostExecute(result: Void?) {
                                    super.onPostExecute(result)
                                    mAdapterAAT?.selectedList?.clear()
                                    mAdapterAAT?.notifyItemRangeChanged(0, allFiles.size)
                                    mAdapterAAT?.notifyDataSetChanged()

                                    if (allFiles.isEmpty()) {
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvRecentDevices.visibility = View.GONE
                                    }

                                    mState = false
                                    if(mState) {
                                        binding.btnDelete.visibility = View.VISIBLE

                                    }else {
                                        binding.btnDelete.visibility = View.GONE

                                    }
//                                    binding.viewMain.visibility = View.GONE
//                                    binding.viewProgress.visibility = View.VISIBLE
                                }
                            }.execute()


                        }

                        override fun dismiss() {

                                mAdapterAAT?.selectedList?.clear()
                                mAdapterAAT?.notifyItemRangeChanged(0, allFiles.size)
                                mAdapterAAT?.notifyDataSetChanged()

                                if (allFiles.isEmpty()) {
                                    binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                    binding.btnDelete.visibility=View.GONE
                                    binding.rvRecentDevices.visibility = View.GONE
                                }

                                mState = false
                            if(mState) {
                                binding.btnDelete.visibility = View.VISIBLE

                            }else {
                                binding.btnDelete.visibility = View.GONE

                            }
                                binding.viewMain.visibility = View.VISIBLE
                                binding.viewProgress.visibility = View.GONE
                            }


                    })
            }
        })

    }

    override fun onResume() {
        super.onResume()
        fileLoadingTask = LoadRecycleBinFiles()
        fileLoadingTask?.execute()
    }


    override fun onBackPressed() {
       InterAdHelperAAT.showMediatedAdmobIntersitial(this@HomeScreenAAT)
        exitorcontinue()

    }

    override fun onDestroy() {
        super.onDestroy()
        fileLoadingTask?.cancel(true)
        mediaFiles.clear()
        docFiles.clear()
    }

    var mState = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu_aat, menu)
        menu?.findItem(R.id.mi_delete)?.isVisible = mState
        menu?.findItem(R.id.mi_share)?.isVisible = mState
        menu?.findItem(R.id.mi_guide)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_delete -> {
//                if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
////                    binding.viewMain.visibility = View.GONE
////                    binding.viewProgress.visibility = View.VISIBLE
//                    MyDialoguesAAT.showDeleteWarning(this@HomeScreenAAT,
//                        object : MyDialoguesAAT.Companion.DeletionCallBack {
//                            override fun deleteFiles() {
//                                object : AsyncTask<Void, Void, Void?>() {
//                                    override fun doInBackground(vararg params: Void?): Void? {
//                                        mAdapterAAT?.selectedList?.forEach {
//                                            if (File(it.filePath).delete()) {
//                                                allFiles.remove(it)
//                                                if (it.fileType == MyConstantsAAT.DOC_FILE_TYPE) {
//                                                    docFiles.remove(it)
//                                                } else {
//                                                    mediaFiles.remove(it)
//                                                }
//                                            }
//                                        }
//                                        return null
//                                    }
//
//                                    override fun onPostExecute(result: Void?) {
//                                        super.onPostExecute(result)
//                                        mAdapterAAT?.selectedList?.clear()
//                                        mAdapterAAT?.notifyItemRangeChanged(0, allFiles.size)
//                                        mAdapterAAT?.notifyDataSetChanged()
//
//                                        if (allFiles.isEmpty()) {
//                                            binding.tvNoDeletedFiles.visibility = View.VISIBLE
//                                            binding.rvRecentDevices.visibility = View.GONE
//                                        }
//
//                                        mState = false
//                                        invalidateOptionsMenu()
////                                        binding.viewMain.visibility = View.GONE
////                                        binding.viewProgress.visibility = View.VISIBLE
//                                    }
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
                if(mState) {
                    binding.btnDelete.visibility = View.VISIBLE

                }else {
                    binding.btnDelete.visibility = View.GONE

                }
                mAdapterAAT?.notifyDataSetChanged()
                return true
            }
            R.id.mi_guide -> {
                startActivity(Intent(this@HomeScreenAAT, MyIntroActivityAAT::class.java))
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    inner class LoadRecycleBinFiles : AsyncTask<Void, Void, Void>() {


        override fun onPreExecute() {
            super.onPreExecute()
            docFiles.clear()
            mediaFiles.clear()
            allFiles.clear()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val file = File(applicationContext?.filesDir?.absolutePath + "/recycle Bin")
            loadRecycledFiles(file)
            for (i in allFiles) {
                if (i.fileType == MyConstantsAAT.DOC_FILE_TYPE) {
                    docFiles.add(i)
                } else {
                    mediaFiles.add(i)
                }
            }
            return null
        }

        private fun loadRecycledFiles(rootFile: File) {
            if (rootFile.exists()) {
                val listFiles = rootFile.listFiles()
                if (listFiles != null) {
                    for (file in listFiles) {
                        if (file.isDirectory) {
                            loadRecycledFiles(file)
                        } else {

                            if (file?.absolutePath?.contains(MyConstantsAAT.MEDIA_FOLDER_NAME) == true) {
                                if (file?.absolutePath?.contains(MyConstantsAAT.IMAGE_FOLDER_NAME) == true) {
                                    allFiles.add(
                                        AllFilesModelClassAAT(
                                            this@HomeScreenAAT,
                                            file.absolutePath,
                                            file.name,
                                            file.lastModified(),
                                            MyConstantsAAT.MEDIA_FILE_TYPE,
                                            subType = MyConstantsAAT.IMAGE_FILE_TYPE
                                        )
                                    )
                                } else if (file?.absolutePath?.contains(MyConstantsAAT.AUDIOS_FOLDER_NAME) == true) {
                                    allFiles.add(
                                        AllFilesModelClassAAT(
                                            this@HomeScreenAAT,
                                            file.absolutePath,
                                            file.name,
                                            file.lastModified(),
                                            MyConstantsAAT.MEDIA_FILE_TYPE,
                                            subType = MyConstantsAAT.AUDIO_FILE_TYPE
                                        )
                                    )
                                } else if (file?.absolutePath?.contains(MyConstantsAAT.VIDEOS_FOLDER_NAME) == true) {
                                    allFiles.add(
                                        AllFilesModelClassAAT(
                                            this@HomeScreenAAT,
                                            file.absolutePath,
                                            file.name,
                                            file.lastModified(),
                                            MyConstantsAAT.MEDIA_FILE_TYPE,
                                            subType = MyConstantsAAT.VIDEO_FILE_TYPE
                                        )
                                    )
                                }

                            } else if (file?.parentFile?.absolutePath?.contains(MyConstantsAAT.DOC_FOLDER_NAME) == true) {
                                if (file.extension.contains("pdf")) {
                                    allFiles.add(
                                        AllFilesModelClassAAT(
                                            this@HomeScreenAAT,
                                            file.absolutePath,
                                            file.name,
                                            file.lastModified(),
                                            MyConstantsAAT.DOC_FILE_TYPE,
                                            subType = MyConstantsAAT.PDF_FILE_TYPE
                                        )
                                    )
                                } else if (file.extension.contains("doc")) {
                                    allFiles.add(
                                        AllFilesModelClassAAT(
                                            this@HomeScreenAAT,
                                            file.absolutePath,
                                            file.name,
                                            file.lastModified(),
                                            MyConstantsAAT.DOC_FILE_TYPE,
                                            subType = MyConstantsAAT.WORD_FILE_TYPE
                                        )
                                    )
                                } else {
                                    allFiles.add(
                                        AllFilesModelClassAAT(
                                            this@HomeScreenAAT,
                                            file.absolutePath,
                                            file.name,
                                            file.lastModified(),
                                            MyConstantsAAT.DOC_FILE_TYPE,
                                            subType = MyConstantsAAT.OTHER_FILE_TYPE
                                        )
                                    )
                                }
                            }

                        }
                    }
                }
                for (i in 0 until allFiles.size - 1) {
                    for (j in i until allFiles.size) {
                        if (allFiles[i].date < allFiles[j].date) {
                            val temp = allFiles[i]
                            allFiles[i] = allFiles[j]
                            allFiles[j] = temp
                        }
                    }
                }
            } else {
                rootFile.mkdirs()
            }

        }


        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            binding.viewMain.visibility = View.VISIBLE
            binding.viewProgress.visibility = View.GONE
            //show files to user
            updateUI()
        }

    }

    fun exitorcontinue() {
        val dialog = Dialog(this@HomeScreenAAT)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dbBinding = DbGeneralAatBinding.inflate(LayoutInflater.from(this@HomeScreenAAT))
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
            fileLoadingTask?.cancel(true)
            finishAffinity()
        }
        val negative: Button = dbBinding.btnNegative
        negative.setOnClickListener {
            if (!this@HomeScreenAAT.isFinishing && !this@HomeScreenAAT.isDestroyed) {
                dialog.cancel()
            }
        }

        val rateUs: Button = dialog.findViewById(R.id.btn_rate_us)
        rateUs.visibility = View.VISIBLE
        rateUs.setOnClickListener {
            rateApp()
            if (!this@HomeScreenAAT.isFinishing && !this@HomeScreenAAT.isDestroyed) {
                dialog.cancel()
            }
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.cancel()
        }
        dbBinding.adFrame.visibility = View.VISIBLE
        NativeAdHelperAAT.showAdmobNativeAd(this, dbBinding.adFrame)
        if (!this@HomeScreenAAT.isFinishing && !this@HomeScreenAAT.isDestroyed) {
            dialog.show()
        }
    }

    private fun rateIntentForUrl(url: String): Intent? {
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    url + Objects.requireNonNull(this@HomeScreenAAT).packageName
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

    fun updateUI() {
        if (allFiles.isNotEmpty()) {
            binding.rvRecentDevices.visibility = View.VISIBLE
            binding.tvNoDeletedFiles.visibility = View.GONE


            mAdapterAAT = RecentsAdapterAAT(this, allFiles)
            binding.rvRecentDevices.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvRecentDevices.adapter = mAdapterAAT

            binding.tvTotalFilesDetails.text =
                "${applicationContext.getString(R.string.total_files)} (${allFiles.size})"
            binding.tvDocsDetails.text =
                "${applicationContext.getString(R.string.docs)} (${docFiles.size})"
            binding.tvMediaDetails.text =
                "${applicationContext.getString(R.string.media)} (${mediaFiles.size})"
        } else {
            binding.rvRecentDevices.visibility = View.GONE
            binding.tvNoDeletedFiles.visibility = View.VISIBLE
            binding.btnDelete.visibility=View.GONE

            binding.tvTotalFilesDetails.text =
                "${applicationContext.getString(R.string.total_files)} (${allFiles.size})"
            binding.tvDocsDetails.text =
                "${applicationContext.getString(R.string.docs)} (${docFiles.size})"
            binding.tvMediaDetails.text =
                "${applicationContext.getString(R.string.media)} (${mediaFiles.size})"
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

//    fun loadFbBannerAd() {
//
//        val adView = AdView(
//            this@HomeScreenAAT,
//            this@HomeScreenAAT.resources.getString(R.string.fb_banner_ad),
//            AdSize.BANNER_HEIGHT_50
//        )
//        val s = this@HomeScreenAAT.resources.getString(R.string.fb_banner_ad2)
//        val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {
//
//            override fun onError(ad: Ad, adError: AdError) {
//                Log.d(MyConstantsAAT.TAG, "onError: ")
//                Log.d(
//                    MyConstantsAAT.TAG,
//                    "onError  Banner: " + ad!!.placementId + "\n" + adError!!.errorMessage
//                )
//            }
//
//            override fun onAdLoaded(ad: Ad) {
//                // Ad loaded callback
//                Log.d(MyConstantsAAT.TAG, "onAdLoaded: Banner=" + ad!!.placementId)
//            }
//
//            override fun onAdClicked(ad: Ad) {
//                Log.d(MyConstantsAAT.TAG, "onAdClicked: ")
//            }
//
//            override fun onLoggingImpression(ad: Ad) {
//                // Ad impression logged callback
//                Log.d(MyConstantsAAT.TAG, "onLoggingImpression: ")
//            }
//        }
//
//
//        adView?.loadAd(adView?.buildLoadAdConfig()?.withAdListener(adListener)?.build())
//        binding.bottomBanner.addView(adView)
//    }






}