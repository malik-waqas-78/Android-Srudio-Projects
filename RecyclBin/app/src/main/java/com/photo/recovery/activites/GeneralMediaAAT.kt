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
import com.photo.recovery.adapters.GeneralMediaAdapterAAT
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
import java.lang.Exception

class GeneralMediaAAT : AppCompatActivity(), SelectionCallBackAAT {

     var fileType:String?= ""
    var mediaFiles = ArrayList<AllFilesModelClassAAT>()
    var mAdapterAAT: GeneralMediaAdapterAAT? = null

    // var deepScanAdapter: ImagesAdapter? = null
    var recovery = false


    val checkboxListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            mAdapterAAT?.selectedList?.clear()
            mAdapterAAT?.files?.let { mAdapterAAT?.selectedList?.addAll(it) }
            mState = true
        } else {
            mAdapterAAT?.selectedList?.clear()
            mState = false
        }
        mAdapterAAT?.notifyDataSetChanged()
        invalidateOptionsMenu()

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
        if (isAppInstalledFromPlay(this@GeneralMediaAAT)) {
            //loadFbBannerAd()
            //loadAdmobBannerAd()
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
        }
//        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

//        val window: Window = window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = resources.getColor(R.color.black)
        toggleCheckBox(false)

        if (intent.hasExtra(MyConstantsAAT.FILE_TYPE_KEY)) {
            fileType = intent.getStringExtra(MyConstantsAAT.FILE_TYPE_KEY)
        } else {
            fileType = ""
        }

//        fileType = if (intent.hasExtra(MyConstantsAAT.FILE_TYPE_KEY)) ({
//            intent.getStringExtra(MyConstantsAAT.FILE_TYPE_KEY)
//        }) else {
//            ""
//        }

        binding.btnDelete.setOnClickListener(View.OnClickListener {
            if (mAdapterAAT?.selectedList?.isNotEmpty() == true) {
                MyDialoguesAAT.showDeleteWarning(this@GeneralMediaAAT,
                    object : MyDialoguesAAT.Companion.DeletionCallBack {
                        override fun deleteFiles() {

                            object : AsyncTask<Void, Void, Void?>() {
                                override fun onPreExecute() {
                                    super.onPreExecute()
                                    binding.viewMain.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE
                                    binding.viewProgress.visibility = View.VISIBLE
                                    binding.tvProgressText.text =
                                        getString(R.string.recovering_files)
                                }

                                override fun doInBackground(vararg params: Void?): Void? {
                                    val list = ArrayList(mAdapterAAT?.selectedList!!)
                                    list.forEach {
                                        if (File(it.filePath).delete()) {
                                            when (it.subType) {
                                                MyConstantsAAT.IMAGE_FILE_TYPE -> {
                                                    mediaFiles.remove(it)
                                                    MediaActivityAAT.images.remove(it)
                                                    HomeScreenAAT.mediaFiles.remove(it)
                                                }
                                                MyConstantsAAT.AUDIO_FILE_TYPE -> {
                                                    mAdapterAAT?.files?.remove(it)
                                                    mediaFiles?.remove(it)
                                                    MediaActivityAAT.images.remove(it)
                                                    HomeScreenAAT.mediaFiles.remove(it)
                                                }
                                                MyConstantsAAT.VIDEO_FILE_TYPE -> {
                                                    mAdapterAAT?.files?.remove(it)
                                                    mediaFiles?.remove(it)
                                                    MediaActivityAAT.images.remove(it)
                                                    HomeScreenAAT.mediaFiles.remove(it)
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
                                    binding.tvProgressText.text =
                                        getString(R.string.scanning_files)

                                    mAdapterAAT?.selectedList?.clear()
                                    when (fileType) {
                                        MyConstantsAAT.IMAGE_FILE_TYPE -> {
                                            mAdapterAAT?.files?.size?.let {
                                                mAdapterAAT?.notifyItemRangeChanged(
                                                    0,
                                                    it
                                                )
                                            }
                                            mAdapterAAT?.notifyDataSetChanged()

                                            if (mAdapterAAT?.files?.isEmpty() == true) {
                                                binding.tvNoDeletedFiles.visibility =
                                                    View.VISIBLE
                                                binding.btnDelete.visibility=View.GONE
                                                binding.recoverFiles.visibility=View.GONE
                                                binding.rvFiles.visibility = View.GONE
                                                binding.cbSelectAll.visibility = View.GONE
                                            }


                                        }
                                        MyConstantsAAT.AUDIO_FILE_TYPE -> {
                                            mAdapterAAT?.files?.size?.let {
                                                mAdapterAAT?.notifyItemRangeChanged(
                                                    0,
                                                    it
                                                )
                                            }
                                            mAdapterAAT?.notifyDataSetChanged()

                                            if (mAdapterAAT?.files?.isEmpty() == true) {
                                                binding.tvNoDeletedFiles.visibility =
                                                    View.VISIBLE
                                                binding.btnDelete.visibility=View.GONE
                                                binding.recoverFiles.visibility=View.GONE
                                                binding.rvFiles.visibility = View.GONE
                                                binding.cbSelectAll.visibility = View.GONE
                                            }

                                        }
                                        MyConstantsAAT.VIDEO_FILE_TYPE -> {
                                            mAdapterAAT?.files?.size?.let {
                                                mAdapterAAT?.notifyItemRangeChanged(
                                                    0,
                                                    it
                                                )
                                            }
                                            mAdapterAAT?.notifyDataSetChanged()

                                            if (mAdapterAAT?.files?.isEmpty() == true) {
                                                binding.tvNoDeletedFiles.visibility =
                                                    View.VISIBLE
                                                binding.btnDelete.visibility=View.GONE
                                                binding.recoverFiles.visibility=View.GONE
                                                binding.rvFiles.visibility = View.GONE
                                                binding.cbSelectAll.visibility = View.GONE
                                            }

                                        }
                                    }
                                    mState = false
                                    invalidateOptionsMenu()
                                    mAdapterAAT?.notifyDataSetChanged()
                                }

                            }.execute()


                        }

                        override fun dismiss() {
                            binding.viewMain.visibility = View.VISIBLE
                            binding.viewBtn.visibility = View.GONE
                            binding.viewProgress.visibility = View.GONE
                            binding.tvProgressText.text =
                                getString(R.string.scanning_files)

                            mAdapterAAT?.selectedList?.clear()
                            when (fileType) {
                                MyConstantsAAT.IMAGE_FILE_TYPE -> {
                                    mAdapterAAT?.files?.size?.let {
                                        mAdapterAAT?.notifyItemRangeChanged(
                                            0,
                                            it
                                        )
                                    }
                                    mAdapterAAT?.notifyDataSetChanged()

                                    if (mAdapterAAT?.files?.isEmpty() == true) {
                                        binding.tvNoDeletedFiles.visibility =
                                            View.VISIBLE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    }


                                }
                                MyConstantsAAT.AUDIO_FILE_TYPE -> {
                                    mAdapterAAT?.files?.size?.let {
                                        mAdapterAAT?.notifyItemRangeChanged(
                                            0,
                                            it
                                        )
                                    }
                                    mAdapterAAT?.notifyDataSetChanged()

                                    if (mAdapterAAT?.files?.isEmpty() == true) {
                                        binding.tvNoDeletedFiles.visibility =
                                            View.VISIBLE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    }

                                }
                                MyConstantsAAT.VIDEO_FILE_TYPE -> {
                                    mAdapterAAT?.files?.size?.let {
                                        mAdapterAAT?.notifyItemRangeChanged(
                                            0,
                                            it
                                        )
                                    }
                                    mAdapterAAT?.notifyDataSetChanged()

                                    if (mAdapterAAT?.files?.isEmpty() == true) {
                                        binding.tvNoDeletedFiles.visibility =
                                            View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    }

                                }
                            }
                            mState = false
                            invalidateOptionsMenu()
                            mAdapterAAT?.notifyDataSetChanged()
                        }

                    })
            }
        })

        binding.recoverFiles.setOnClickListener {
            val filesToRecover = mAdapterAAT?.selectedList?.let { ArrayList<AllFilesModelClassAAT>(it) }
            if (!filesToRecover.isNullOrEmpty()) {
                binding.viewMain.visibility = View.GONE
                binding.viewBtn.visibility = View.GONE
                binding.viewProgress.visibility = View.VISIBLE
                binding.tvProgressText.text = getString(R.string.recovering_files)
                //show alert dialogue perogress bar

                RecoverFilesAAT(
                    this@GeneralMediaAAT,
                    filesToRecover,
                    object : RecoverFilesAAT.RecoverFilesCallBack {
                        override fun filesRecoveryDone() {
                            binding.viewMain.visibility = View.VISIBLE
                            binding.viewBtn.visibility = View.GONE
                            binding.viewProgress.visibility = View.GONE
                            binding.tvProgressText.text = getString(R.string.scanning_files)
                            mAdapterAAT?.selectedList?.let { it1 -> mAdapterAAT?.files?.removeAll(it1) }
                            mAdapterAAT?.selectedList?.let { it1 ->
                                MediaActivityAAT?.images?.removeAll(
                                    it1
                                )
                            }
                            mAdapterAAT?.selectedList?.let { it1 ->
                                MediaActivityAAT?.videos?.removeAll(
                                    it1
                                )
                            }
                            mAdapterAAT?.selectedList?.let { it1 ->
                                MediaActivityAAT?.audios?.removeAll(
                                    it1
                                )
                            }
                            mAdapterAAT?.selectedList?.let { it1 -> mediaFiles?.removeAll(it1) }
                            mAdapterAAT?.selectedList?.let { it1 ->
                                HomeScreenAAT?.mediaFiles.removeAll(
                                    it1
                                )
                            }

                            MyDialoguesAAT.showRecoveryCompleted(
                                this@GeneralMediaAAT,
                                mAdapterAAT?.selectedList?.size.toString()
                            )

                            mAdapterAAT?.selectedList?.clear()
                            mState = false
                            invalidateOptionsMenu()
                            mAdapterAAT?.notifyDataSetChanged()
                            if (mAdapterAAT?.files?.isEmpty() == true) {
                                binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                binding.recoverFiles.visibility=View.GONE
                                binding.btnDelete.visibility=View.GONE
                                binding.rvFiles.visibility = View.GONE
                                binding.cbSelectAll.visibility = View.GONE
                            }
                        }

                    }).recoverFiles()
            }

        }


    }

    override fun onResume() {
        super.onResume()
        if (InterAdHelperAAT.adCallBack == null) {
            if (!ShowImageAAT.open) {
                setupLayout()
            } else {
                ShowImageAAT.open = false
            }
        } else {
            InterAdHelperAAT.adCallBack = null
        }
    }

    override fun onBackPressed() {
        if (binding.viewMain.isVisible) {
            binding.viewBtn.visibility = View.VISIBLE
            binding.viewMain.visibility = View.GONE
            binding.viewProgress.visibility = View.GONE
            mAdapterAAT = null
            //deepScanAdapter = null
            mState = false
            if(mState) {
                binding.btnDelete.visibility = View.VISIBLE
                binding.recoverFiles.visibility = View.VISIBLE
            }else {
                binding.btnDelete.visibility = View.GONE
                binding.recoverFiles.visibility = View.GONE
            }
        } else {
            ShowImageAAT.open=false
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        InterAdHelperAAT.adCallBack=null
        super.onDestroy()
    }

    private fun setupLayout() {




        if( binding.viewMain.visibility == View.VISIBLE){

        }else{
            mediaFiles.clear()
            when (fileType) {
                MyConstantsAAT.IMAGE_FILE_TYPE -> {
                    //   binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.images_files)
                    mediaFiles.addAll(MediaActivityAAT.images)
                    binding.toolbarTitle.text =
                        applicationContext.resources.getString(R.string.images_files)
//                supportActionBar?.setBackgroundDrawable(
//                    applicationContext.resources.getDrawable(
//                        R.drawable.tb_images,
//                        null
//                    )
//                )

//                val window: Window = window
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = resources.getColor(R.color.image_sc)

//                binding.clBack.setBackgroundColor(resources.getColor(R.color.purple))
//                binding.btnScan.background =
//                    ContextCompat.getDrawable(this@GeneralMediaAAT, R.drawable.ic_shape_image)
//                binding.btnScan.setImageResource(R.drawable.ic_image_scan)
//                binding.btnDeepScan.background =
//                    ContextCompat.getDrawable(this@GeneralMediaAAT, R.drawable.ic_shape_image)
//                binding.btnDeepScan.setImageResource(R.drawable.ic_deep_scan_image)
//                binding.btnRecoveredFiles.background =
//                    ContextCompat.getDrawable(
//                        this@GeneralMediaAAT,
//                        R.drawable.ic_image_recover_back_shape
//                    )
//                binding.btnRecoveredFiles.setImageResource(R.drawable.ic_image_recover)
//                binding.recoverFiles.setBackgroundColor(resources.getColor(R.color.image_sc))

                    binding.btnDeepScan.setOnClickListener {


                    InterAdHelperAAT.adCallBack=object:InterAdHelperAAT.Companion.AdLoadCallBack{
                        override fun adClosed() {

                        binding.tvProgressText.text=resources.getString(R.string.deeplyscanning)

                        binding.animationViewa.setAnimation(R.raw.lottiedeepscan)

                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewMain.visibility = View.GONE
                        binding.viewBtn.visibility = View.GONE

                            NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)


                        recovery = false

                        android.os.Handler().postDelayed({
                            LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                                override fun trashLoaded(files: ArrayList<File>) {


                                    if (!this@GeneralMediaAAT.isDestroyed || !this@GeneralMediaAAT.isFinishing) {

                                        ShowImageAAT.open = true
                                    }

                                    binding.rvFiles.layoutManager =
                                        GridLayoutManager(this@GeneralMediaAAT, 3)
                                    val imageFiles = ArrayList<AllFilesModelClassAAT>()
                                    files.forEach {
                                        imageFiles.add(
                                            AllFilesModelClassAAT(
                                                this@GeneralMediaAAT,
                                                it.absolutePath,
                                                it.name,
                                                it.lastModified(),
                                                MyConstantsAAT.MEDIA_FILE_TYPE,
                                                MyConstantsAAT.IMAGE_FILE_TYPE,
                                                false
                                            )
                                        )
                                    }
                                    mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, imageFiles)
                                    binding.rvFiles.adapter = mAdapterAAT

                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE

                                    if (files.isEmpty()) {
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    } else {
//                                            binding.recoverFiles.visibility=View.VISIBLE
                                        binding.tvNoDeletedFiles.visibility = View.GONE

                                        binding.rvFiles.visibility = View.VISIBLE
                                        binding.cbSelectAll.visibility = View.VISIBLE
                                    }
                                }

                            }).loadImageFiles(false, MyConstantsAAT.IMAGE_FILE_TYPE)
                        }, 4000)
                       }

                    }
                    if(InterAdHelperAAT.mediatedInterstitialAdGoogle!=null) {
                        InterAdHelperAAT.showMediatedAdmobIntersitial(this@GeneralMediaAAT)
                    }else{
                        binding.tvProgressText.text=resources.getString(R.string.deeplyscanning)

                        binding.animationViewa.setAnimation(R.raw.lottiedeepscan)

                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewMain.visibility = View.GONE
                        binding.viewBtn.visibility = View.GONE

                        NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)


                        recovery = false

                        android.os.Handler().postDelayed({
                            LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                                override fun trashLoaded(files: ArrayList<File>) {


                                    if (!this@GeneralMediaAAT.isDestroyed || !this@GeneralMediaAAT.isFinishing) {

                                        ShowImageAAT.open = true
                                    }

                                    binding.rvFiles.layoutManager =
                                        GridLayoutManager(this@GeneralMediaAAT, 3)
                                    val imageFiles = ArrayList<AllFilesModelClassAAT>()
                                    files.forEach {
                                        imageFiles.add(
                                            AllFilesModelClassAAT(
                                                this@GeneralMediaAAT,
                                                it.absolutePath,
                                                it.name,
                                                it.lastModified(),
                                                MyConstantsAAT.MEDIA_FILE_TYPE,
                                                MyConstantsAAT.IMAGE_FILE_TYPE,
                                                false
                                            )
                                        )
                                    }
                                    mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, imageFiles)
                                    binding.rvFiles.adapter = mAdapterAAT

                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE

                                    if (files.isEmpty()) {
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    } else {
//                                            binding.recoverFiles.visibility=View.VISIBLE
                                        binding.tvNoDeletedFiles.visibility = View.GONE

                                        binding.rvFiles.visibility = View.VISIBLE
                                        binding.cbSelectAll.visibility = View.VISIBLE
                                    }
                                }

                            }).loadImageFiles(false, MyConstantsAAT.IMAGE_FILE_TYPE)
                        }, 4000)
                    }



                    }
                    binding.btnScan.setOnClickListener {
                        //set adapter and recycle view



                    NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        binding.viewMain.visibility = View.GONE
                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewBtn.visibility = View.GONE

                        recovery = false

                        android.os.Handler().postDelayed({
                            binding.rvFiles.layoutManager = GridLayoutManager(this, 3)
                            mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, mediaFiles)
                            binding.rvFiles.adapter = mAdapterAAT

                            binding.viewMain.visibility = View.VISIBLE
                            binding.viewProgress.visibility = View.GONE
                            binding.viewBtn.visibility = View.GONE

                            if (mediaFiles.isEmpty()) {
                                binding.recoverFiles.visibility=View.GONE
                                binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                binding.recoverFiles.visibility=View.GONE
                                binding.btnDelete.visibility=View.GONE
                                binding.rvFiles.visibility = View.GONE
                                binding.cbSelectAll.visibility = View.GONE
                            } else {
//                            binding.recoverFiles.visibility=View.VISIBLE
                                binding.tvNoDeletedFiles.visibility = View.GONE

                                binding.rvFiles.visibility = View.VISIBLE
                                binding.cbSelectAll.visibility = View.VISIBLE
                            }
                        }, 4000)


                    }
                    binding.btnRecoveredFiles.setOnClickListener {

//                    NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        binding.tvProgressText.text=resources.getString(R.string.scanning_files)

                        binding.animationViewa.setAnimation(R.raw.lottienormalscan)

                        recovery = true
                        binding.viewMain.visibility = View.GONE
                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewBtn.visibility = View.GONE
                        android.os.Handler().postDelayed({
                            LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                                override fun trashLoaded(files: ArrayList<File>) {

                                    binding.rvFiles.layoutManager =
                                        GridLayoutManager(this@GeneralMediaAAT, 3)
                                    val imageFiles = ArrayList<AllFilesModelClassAAT>()
                                    files.forEach {
                                        imageFiles.add(
                                            AllFilesModelClassAAT(
                                                this@GeneralMediaAAT,
                                                it.absolutePath,
                                                it.name,
                                                it.lastModified(),
                                                MyConstantsAAT.MEDIA_FILE_TYPE,
                                                MyConstantsAAT.IMAGE_FILE_TYPE,
                                                false
                                            )
                                        )
                                    }
                                    mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, imageFiles)
                                    binding.rvFiles.adapter = mAdapterAAT

                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE

                                    if (files.isEmpty()) {
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    } else {
//                                    binding.recoverFiles.visibility=View.VISIBLE
                                        binding.tvNoDeletedFiles.visibility = View.GONE

                                        binding.rvFiles.visibility = View.VISIBLE
                                        binding.cbSelectAll.visibility = View.VISIBLE
                                    }
                                }

                            }).loadImageFiles(true, MyConstantsAAT.IMAGE_FILE_TYPE)
                        }, 4000)
                    }

                }
                MyConstantsAAT.VIDEO_FILE_TYPE -> {
                    //  binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.videos_files)
                    mediaFiles.addAll(MediaActivityAAT.videos)
                    binding.toolbarTitle.text =
                        applicationContext.resources.getString(R.string.videos_files)
//                supportActionBar?.setBackgroundDrawable(
//                    applicationContext.resources.getDrawable(
//                        R.drawable.tb_video,
//                        null
//                    )
//                )

//                val window: Window = window
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = resources.getColor(R.color.videos_sc)
//                binding.clBack.setBackgroundColor(resources.getColor(R.color.videos_sc))
//
//                binding.btnScan.background =
//                    ContextCompat.getDrawable(this@GeneralMediaAAT, R.drawable.ic_shape_video)
//                binding.btnScan.setImageResource(R.drawable.ic_video_scan)
//                binding.btnDeepScan.background =
//                    ContextCompat.getDrawable(this@GeneralMediaAAT, R.drawable.ic_shape_video)
//                binding.btnDeepScan.setImageResource(R.drawable.ic_deep_scan_video)
//                binding.btnRecoveredFiles.background =
//                    ContextCompat.getDrawable(
//                        this@GeneralMediaAAT,
//                        R.drawable.ic_video_recover_back_shape
//                    )
//                binding.btnRecoveredFiles.setImageResource(R.drawable.ic_video_recover)
//
//                binding.recoverFiles.setBackgroundColor(resources.getColor(R.color.videos_sc))

                    binding.btnDeepScan.setOnClickListener {

                    InterAdHelperAAT.adCallBack=object : InterAdHelperAAT.Companion.AdLoadCallBack{
                        override fun adClosed() {
                            NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        binding.tvProgressText.text=resources.getString(R.string.deeplyscanning)

                        binding.animationViewa.setAnimation(R.raw.lottiedeepscan)


                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewMain.visibility = View.GONE
                        binding.viewBtn.visibility = View.GONE
                        recovery = false

                        android.os.Handler().postDelayed({
                            LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                                override fun trashLoaded(files: ArrayList<File>) {

                                    if (!this@GeneralMediaAAT.isDestroyed || !this@GeneralMediaAAT.isFinishing) {

                                        ShowImageAAT.open = true
                                    }

                                    binding.rvFiles.layoutManager =
                                        GridLayoutManager(this@GeneralMediaAAT, 3)
                                    val videofiles = ArrayList<AllFilesModelClassAAT>()
                                    files.forEach {
                                        videofiles.add(
                                            AllFilesModelClassAAT(
                                                this@GeneralMediaAAT,
                                                it.absolutePath,
                                                it.name,
                                                it.lastModified(),
                                                MyConstantsAAT.MEDIA_FILE_TYPE,
                                                MyConstantsAAT.VIDEO_FILE_TYPE,
                                                false
                                            )
                                        )
                                    }
                                    mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, videofiles)

                                    binding.rvFiles.adapter = mAdapterAAT

                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE

                                    if (files.isEmpty()) {
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    } else {
//                                            binding.recoverFiles.visibility=View.VISIBLE
                                        binding.tvNoDeletedFiles.visibility = View.GONE

                                        binding.rvFiles.visibility = View.VISIBLE
                                        binding.cbSelectAll.visibility = View.VISIBLE
                                    }
                                }

                            }).loadVideosFiles(false, MyConstantsAAT.VIDEO_FILE_TYPE)
                        }, 4000)
                        }

                    }
                    if(InterAdHelperAAT.mediatedInterstitialAdGoogle!=null) {
                        InterAdHelperAAT.showMediatedAdmobIntersitial(this@GeneralMediaAAT)
                    }else{
                        NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        binding.tvProgressText.text=resources.getString(R.string.deeplyscanning)

                        binding.animationViewa.setAnimation(R.raw.lottiedeepscan)


                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewMain.visibility = View.GONE
                        binding.viewBtn.visibility = View.GONE
                        recovery = false

                        android.os.Handler().postDelayed({
                            LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                                override fun trashLoaded(files: ArrayList<File>) {

                                    if (!this@GeneralMediaAAT.isDestroyed || !this@GeneralMediaAAT.isFinishing) {

                                        ShowImageAAT.open = true
                                    }

                                    binding.rvFiles.layoutManager =
                                        GridLayoutManager(this@GeneralMediaAAT, 3)
                                    val videofiles = ArrayList<AllFilesModelClassAAT>()
                                    files.forEach {
                                        videofiles.add(
                                            AllFilesModelClassAAT(
                                                this@GeneralMediaAAT,
                                                it.absolutePath,
                                                it.name,
                                                it.lastModified(),
                                                MyConstantsAAT.MEDIA_FILE_TYPE,
                                                MyConstantsAAT.VIDEO_FILE_TYPE,
                                                false
                                            )
                                        )
                                    }
                                    mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, videofiles)

                                    binding.rvFiles.adapter = mAdapterAAT

                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE

                                    if (files.isEmpty()) {
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    } else {
//                                            binding.recoverFiles.visibility=View.VISIBLE
                                        binding.tvNoDeletedFiles.visibility = View.GONE

                                        binding.rvFiles.visibility = View.VISIBLE
                                        binding.cbSelectAll.visibility = View.VISIBLE
                                    }
                                }

                            }).loadVideosFiles(false, MyConstantsAAT.VIDEO_FILE_TYPE)
                        }, 4000)
                    }




                    }
                    binding.btnScan.setOnClickListener {


                    NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        //set adapter and recycle view
                        recovery = false
                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewBtn.visibility = View.GONE
                        binding.viewMain.visibility = View.GONE

                        android.os.Handler().postDelayed({
                            binding.rvFiles.layoutManager = GridLayoutManager(this, 3)
                            mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, mediaFiles)
                            binding.rvFiles.adapter = mAdapterAAT

                            binding.viewMain.visibility = View.VISIBLE
                            binding.viewProgress.visibility = View.GONE
                            binding.viewBtn.visibility = View.GONE

                            if (mediaFiles.isEmpty()) {
                                binding.recoverFiles.visibility=View.GONE
                                binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                binding.recoverFiles.visibility=View.GONE
                                binding.btnDelete.visibility=View.GONE
                                binding.rvFiles.visibility = View.GONE
                                binding.cbSelectAll.visibility = View.GONE
                            } else {
//                            binding.recoverFiles.visibility=View.VISIBLE
                                binding.tvNoDeletedFiles.visibility = View.GONE

                                binding.rvFiles.visibility = View.VISIBLE
                                binding.cbSelectAll.visibility = View.VISIBLE
                            }
                        }, 4000)


                    }
                    binding.btnRecoveredFiles.setOnClickListener {

//                    NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        binding.tvProgressText.text=resources.getString(R.string.scanning_files)

                        binding.animationViewa.setAnimation(R.raw.lottienormalscan)

                        recovery = true
                        binding.viewMain.visibility = View.GONE
                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewBtn.visibility = View.GONE
                        android.os.Handler().postDelayed({
                            LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                                override fun trashLoaded(files: ArrayList<File>) {

                                    binding.rvFiles.layoutManager =
                                        GridLayoutManager(this@GeneralMediaAAT, 3)
                                    val videofiles = ArrayList<AllFilesModelClassAAT>()
                                    files.forEach {
                                        videofiles.add(
                                            AllFilesModelClassAAT(
                                                this@GeneralMediaAAT,
                                                it.absolutePath,
                                                it.name,
                                                it.lastModified(),
                                                MyConstantsAAT.MEDIA_FILE_TYPE,
                                                MyConstantsAAT.VIDEO_FILE_TYPE,
                                                false
                                            )
                                        )
                                    }
                                    mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, videofiles)
                                    binding.rvFiles.adapter = mAdapterAAT

                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE

                                    if (files.isEmpty()) {
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    } else {

//                                    binding.recoverFiles.visibility=View.VISIBLE
                                        binding.tvNoDeletedFiles.visibility = View.GONE

                                        binding.rvFiles.visibility = View.VISIBLE
                                        binding.cbSelectAll.visibility = View.VISIBLE
                                    }
                                }

                            }).loadImageFiles(true, MyConstantsAAT.VIDEO_FILE_TYPE)
                        }, 4000)
                    }
                }
                MyConstantsAAT.AUDIO_FILE_TYPE -> {
                    //   binding.tvFilesTitle.text=applicationContext.resources.getString(R.string.audios_files)
                    mediaFiles.addAll(MediaActivityAAT.audios)
                    binding.toolbarTitle.text =
                        applicationContext.resources.getString(R.string.audios_files)
//                supportActionBar?.setBackgroundDrawable(
//                    applicationContext.resources.getDrawable(
//                        R.drawable.tb_audio,
//                        null
//                    )
//                )

//                val window: Window = window
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = resources.getColor(R.color.bgColor)

//                binding.clBack.setBackgroundColor(resources.getColor(R.color.audio_color))
//                binding.btnScan.background =
//                    ContextCompat.getDrawable(this@GeneralMediaAAT, R.drawable.ic_shape_audio)
//                binding.btnScan.setImageResource(R.drawable.ic_audio_scan)
//                binding.btnDeepScan.background =
//                    ContextCompat.getDrawable(this@GeneralMediaAAT, R.drawable.ic_shape_audio)
//                binding.btnDeepScan.setImageResource(R.drawable.ic_deep_scan_audio)
//                binding.btnRecoveredFiles.background =
//                    ContextCompat.getDrawable(
//                        this@GeneralMediaAAT,
//                        R.drawable.ic_audio_recover_back_shape
//                    )
//                binding.btnRecoveredFiles.setImageResource(R.drawable.ic_audio_recover)
//                binding.recoverFiles.setBackgroundColor(resources.getColor(R.color.audio_color))
                    binding.btnDeepScan.setOnClickListener {

                    InterAdHelperAAT.adCallBack=object:InterAdHelperAAT.Companion.AdLoadCallBack{
                        override fun adClosed() {
                            NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        binding.tvProgressText.text=resources.getString(R.string.deeplyscanning)

                        binding.animationViewa.setAnimation(R.raw.lottiedeepscan)

                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewMain.visibility = View.GONE
                        binding.viewBtn.visibility = View.GONE
                        recovery = false
                        android.os.Handler().postDelayed({
                            LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                                override fun trashLoaded(files: ArrayList<File>) {

                                    if (!this@GeneralMediaAAT.isDestroyed || !this@GeneralMediaAAT.isFinishing) {
                                        ShowImageAAT.open = true

                                    }
                                    binding.rvFiles.layoutManager =
                                        GridLayoutManager(this@GeneralMediaAAT, 3)
                                    val audioFiles = ArrayList<AllFilesModelClassAAT>()
                                    files.forEach {
                                        audioFiles.add(
                                            AllFilesModelClassAAT(
                                                this@GeneralMediaAAT,
                                                it.absolutePath,
                                                it.name,
                                                it.lastModified(),
                                                MyConstantsAAT.MEDIA_FILE_TYPE,
                                                MyConstantsAAT.AUDIO_FILE_TYPE,
                                                false
                                            )
                                        )
                                    }
                                    mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, audioFiles)
                                    binding.rvFiles.adapter = mAdapterAAT

                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE

                                    if (files.isEmpty()) {
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    } else {
//                                            binding.recoverFiles.visibility=View.VISIBLE
                                        binding.tvNoDeletedFiles.visibility = View.GONE

                                        binding.rvFiles.visibility = View.VISIBLE
                                        binding.cbSelectAll.visibility = View.VISIBLE
                                    }
                                }

                            }).loadAudiosFiles(false, MyConstantsAAT.AUDIO_FILE_TYPE)
                        }, 4000)
                        }

                    }
                    if(InterAdHelperAAT.mediatedInterstitialAdGoogle!=null) {
                        InterAdHelperAAT.showMediatedAdmobIntersitial(this@GeneralMediaAAT)
                    }else{
                        NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        binding.tvProgressText.text=resources.getString(R.string.deeplyscanning)

                        binding.animationViewa.setAnimation(R.raw.lottiedeepscan)

                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewMain.visibility = View.GONE
                        binding.viewBtn.visibility = View.GONE
                        recovery = false
                        android.os.Handler().postDelayed({
                            LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                                override fun trashLoaded(files: ArrayList<File>) {

                                    if (!this@GeneralMediaAAT.isDestroyed || !this@GeneralMediaAAT.isFinishing) {
                                        ShowImageAAT.open = true

                                    }
                                    binding.rvFiles.layoutManager =
                                        GridLayoutManager(this@GeneralMediaAAT, 3)
                                    val audioFiles = ArrayList<AllFilesModelClassAAT>()
                                    files.forEach {
                                        audioFiles.add(
                                            AllFilesModelClassAAT(
                                                this@GeneralMediaAAT,
                                                it.absolutePath,
                                                it.name,
                                                it.lastModified(),
                                                MyConstantsAAT.MEDIA_FILE_TYPE,
                                                MyConstantsAAT.AUDIO_FILE_TYPE,
                                                false
                                            )
                                        )
                                    }
                                    mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, audioFiles)
                                    binding.rvFiles.adapter = mAdapterAAT

                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE

                                    if (files.isEmpty()) {
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    } else {
//                                            binding.recoverFiles.visibility=View.VISIBLE
                                        binding.tvNoDeletedFiles.visibility = View.GONE

                                        binding.rvFiles.visibility = View.VISIBLE
                                        binding.cbSelectAll.visibility = View.VISIBLE
                                    }
                                }

                            }).loadAudiosFiles(false, MyConstantsAAT.AUDIO_FILE_TYPE)
                        }, 4000)
                    }


                    }
                    binding.btnScan.setOnClickListener {


                    NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        //set adapter and recycle view
                        recovery = false
                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewBtn.visibility = View.GONE
                        binding.viewMain.visibility = View.GONE
                        android.os.Handler().postDelayed({
                            binding.rvFiles.layoutManager = GridLayoutManager(this, 3)
                            mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, mediaFiles)
                            binding.rvFiles.adapter = mAdapterAAT

                            binding.viewMain.visibility = View.VISIBLE

                            binding.viewBtn.visibility = View.GONE

                            if (mediaFiles.isEmpty()) {
                                binding.recoverFiles.visibility=View.GONE
                                binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                binding.btnDelete.visibility=View.GONE
                                binding.recoverFiles.visibility=View.GONE
                                binding.rvFiles.visibility = View.GONE
                                binding.cbSelectAll.visibility = View.GONE
                            } else {
//                            binding.recoverFiles.visibility=View.VISIBLE

//                            binding.recoverFiles.visibility=View.VISIBLE
                                binding.rvFiles.visibility = View.VISIBLE
                                binding.cbSelectAll.visibility = View.VISIBLE
                            }
                        }, 4000)


                    }
                    binding.btnRecoveredFiles.setOnClickListener {

//                    NativeAdHelperAAT.showAdmobNativeAd(this@GeneralMediaAAT, binding.adFrame)

                        binding.tvProgressText.text=resources.getString(R.string.scanning_files)

                        binding.animationViewa.setAnimation(R.raw.lottienormalscan)

                        recovery = true
                        binding.viewMain.visibility = View.GONE
                        binding.viewProgress.visibility = View.VISIBLE
                        binding.viewBtn.visibility = View.GONE
                        android.os.Handler().postDelayed({
                            LoadTrashFilesAAT(object : LoadTrashFilesAAT.TrashCallBack {
                                override fun trashLoaded(files: ArrayList<File>) {

                                    binding.rvFiles.layoutManager =
                                        GridLayoutManager(this@GeneralMediaAAT, 3)
                                    val audioFile = ArrayList<AllFilesModelClassAAT>()
                                    files.forEach {
                                        audioFile.add(
                                            AllFilesModelClassAAT(
                                                this@GeneralMediaAAT,
                                                it.absolutePath,
                                                it.name,
                                                it.lastModified(),
                                                MyConstantsAAT.MEDIA_FILE_TYPE,
                                                MyConstantsAAT.AUDIO_FILE_TYPE,
                                                false
                                            )
                                        )
                                    }
                                    mAdapterAAT = GeneralMediaAdapterAAT(this@GeneralMediaAAT, audioFile)
                                    binding.rvFiles.adapter = mAdapterAAT

                                    binding.viewMain.visibility = View.VISIBLE
                                    binding.viewProgress.visibility = View.GONE
                                    binding.viewBtn.visibility = View.GONE

                                    if (files.isEmpty()) {
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.tvNoDeletedFiles.visibility = View.VISIBLE
                                        binding.recoverFiles.visibility=View.GONE
                                        binding.btnDelete.visibility=View.GONE
                                        binding.rvFiles.visibility = View.GONE
                                        binding.cbSelectAll.visibility = View.GONE
                                    } else {
//                                    binding.recoverFiles.visibility=View.VISIBLE
                                        binding.tvNoDeletedFiles.visibility = View.GONE

                                        binding.rvFiles.visibility = View.VISIBLE
                                        binding.cbSelectAll.visibility = View.VISIBLE
                                    }
                                }

                            }).loadImageFiles(true, MyConstantsAAT.AUDIO_FILE_TYPE)
                        }, 4000)
                    }
                }
            }
            binding.viewProgress.visibility = View.GONE
            binding.viewBtn.visibility = View.VISIBLE
        }





    }


    var mState = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu_aat, menu)
        menu?.findItem(R.id.mi_delete)?.isVisible = mState
        menu?.findItem(R.id.mi_share)?.isVisible = mState
        if (mState && !recovery) {
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
//                    MyDialoguesAAT.showDeleteWarning(this@GeneralMediaAAT,
//                        object : MyDialoguesAAT.Companion.DeletionCallBack {
//                            override fun deleteFiles() {
//
//                                object : AsyncTask<Void, Void, Void?>() {
//                                    override fun onPreExecute() {
//                                        super.onPreExecute()
//                                        binding.viewMain.visibility = View.GONE
//                                        binding.viewBtn.visibility = View.GONE
//                                        binding.viewProgress.visibility = View.VISIBLE
//                                        binding.tvProgressText.text =
//                                            getString(R.string.recovering_files)
//                                    }
//
//                                    override fun doInBackground(vararg params: Void?): Void? {
//                                        val list = ArrayList(mAdapterAAT?.selectedList!!)
//                                        list.forEach {
//                                            if (File(it.filePath).delete()) {
//                                                when (it.subType) {
//                                                    MyConstantsAAT.IMAGE_FILE_TYPE -> {
//                                                        mediaFiles.remove(it)
//                                                        MediaActivityAAT.images.remove(it)
//                                                        HomeScreenAAT.mediaFiles.remove(it)
//                                                    }
//                                                    MyConstantsAAT.AUDIO_FILE_TYPE -> {
//                                                        mAdapterAAT?.files?.remove(it)
//                                                        mediaFiles?.remove(it)
//                                                        MediaActivityAAT.images.remove(it)
//                                                        HomeScreenAAT.mediaFiles.remove(it)
//                                                    }
//                                                    MyConstantsAAT.VIDEO_FILE_TYPE -> {
//                                                        mAdapterAAT?.files?.remove(it)
//                                                        mediaFiles?.remove(it)
//                                                        MediaActivityAAT.images.remove(it)
//                                                        HomeScreenAAT.mediaFiles.remove(it)
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
//
//                                        binding.viewMain.visibility = View.VISIBLE
//                                        binding.viewBtn.visibility = View.GONE
//                                        binding.viewProgress.visibility = View.GONE
//                                        binding.tvProgressText.text =
//                                            getString(R.string.scanning_files)
//
//                                        mAdapterAAT?.selectedList?.clear()
//                                        when (fileType) {
//                                            MyConstantsAAT.IMAGE_FILE_TYPE -> {
//                                                mAdapterAAT?.files?.size?.let {
//                                                    mAdapterAAT?.notifyItemRangeChanged(
//                                                        0,
//                                                        it
//                                                    )
//                                                }
//                                                mAdapterAAT?.notifyDataSetChanged()
//
//                                                if (mAdapterAAT?.files?.isEmpty() == true) {
//                                                    binding.tvNoDeletedFiles.visibility =
//                                                        View.VISIBLE
//                                                    binding.rvFiles.visibility = View.GONE
//                                                    binding.cbSelectAll.visibility = View.GONE
//                                                }
//
//
//                                            }
//                                            MyConstantsAAT.AUDIO_FILE_TYPE -> {
//                                                mAdapterAAT?.files?.size?.let {
//                                                    mAdapterAAT?.notifyItemRangeChanged(
//                                                        0,
//                                                        it
//                                                    )
//                                                }
//                                                mAdapterAAT?.notifyDataSetChanged()
//
//                                                if (mAdapterAAT?.files?.isEmpty() == true) {
//                                                    binding.tvNoDeletedFiles.visibility =
//                                                        View.VISIBLE
//                                                    binding.rvFiles.visibility = View.GONE
//                                                    binding.cbSelectAll.visibility = View.GONE
//                                                }
//
//                                            }
//                                            MyConstantsAAT.VIDEO_FILE_TYPE -> {
//                                                mAdapterAAT?.files?.size?.let {
//                                                    mAdapterAAT?.notifyItemRangeChanged(
//                                                        0,
//                                                        it
//                                                    )
//                                                }
//                                                mAdapterAAT?.notifyDataSetChanged()
//
//                                                if (mAdapterAAT?.files?.isEmpty() == true) {
//                                                    binding.tvNoDeletedFiles.visibility =
//                                                        View.VISIBLE
//                                                    binding.rvFiles.visibility = View.GONE
//                                                    binding.cbSelectAll.visibility = View.GONE
//                                                }
//
//                                            }
//                                        }
//                                        mState = false
//                                        invalidateOptionsMenu()
//                                        mAdapterAAT?.notifyDataSetChanged()
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
                val list = ArrayList(mAdapterAAT?.selectedList!!)
                list?.forEach { it ->
                    val file = File(it.filePath)
                    try {
                        val uri = FileProvider.getUriForFile(
                            applicationContext,
                            applicationContext.resources.getString(R.string.authority),
                            file
                        )
                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
                        startActivity(intent)
                        mAdapterAAT?.selectedList?.clear()
                        mState = false
                        invalidateOptionsMenu()
                        mAdapterAAT?.notifyDataSetChanged()

                        files.add(uri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
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
            binding.recoverFiles.visibility = View.VISIBLE
        }else {
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

    /*fun loadFbBannerAd() {

        val adView = AdView(
            this@GeneralMedia,
            this@GeneralMedia.resources.getString(R.string.fb_banner_ad),
            AdSize.BANNER_HEIGHT_50
        )
        val s = this@GeneralMedia.resources.getString(R.string.fb_banner_ad2)
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