package com.zak.clone.zakactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zak.clone.R
import com.zak.clone.zakadapters.ZakAdapterGeneralShowItems
import com.zak.clone.zakadapters.ZakAdapterViewFolder
import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.databinding.ZakActivityShowItemsForSelectionBinding
import com.zak.clone.zakmodelclasses.ZakAppsModel
import com.zak.clone.zakmodelclasses.ZakContactsModel
import com.zak.clone.zakmodelclasses.ZakFileSharingModel
import com.zak.clone.zakmodelclasses.ZakMediaModelClass

class ZakShowItemsForSelection : AppCompatActivity() {

    lateinit var binding: ZakActivityShowItemsForSelectionBinding
    lateinit var FILE_TYPE: String
    var HSAdapter: ZakAdapterGeneralShowItems? = null

    var folderHSAdapterViewFolder: ZakAdapterViewFolder? = null
    var picAndVideosFolders = HashMap<String, ArrayList<ZakMediaModelClass>>()
    var audioAndDocFolders = HashMap<String, ArrayList<ZakFileSharingModel>>()

    val checkBoxListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            when (FILE_TYPE) {
                ZakMyConstants.FILE_TYPE_CALENDAR -> {
                    ZakDataSelectionActivity.calendarEventsList.forEach {
                        it.isSelected = true
                    }
                    ZakDataSelectionActivity.selectedCalendarEventsList.clear()
                    ZakDataSelectionActivity.selectedCalendarEventsList.addAll(ZakDataSelectionActivity.calendarEventsList)
                }
                ZakMyConstants.FILE_TYPE_APPS -> {
                    ZakDataSelectionActivity.apksList.forEach {
                        it.isSelected = true
                    }
                    ZakDataSelectionActivity.selectedApksList.clear()
                    ZakDataSelectionActivity.selectedApksList.addAll(ZakDataSelectionActivity.apksList)
                }
                ZakMyConstants.FILE_TYPE_VIDEOS -> {
                    ZakDataSelectionActivity.videosList.forEach {
                        it.isSelected = true
                    }
                    ZakDataSelectionActivity.selectedVideosList.clear()
                    ZakDataSelectionActivity.selectedVideosList.addAll(ZakDataSelectionActivity.videosList)
                }
                ZakMyConstants.FILE_TYPE_PICS -> {
                    ZakDataSelectionActivity.imagesList.forEach {
                        it.isSelected = true
                    }
                    ZakDataSelectionActivity.selectedImagesList.clear()
                    ZakDataSelectionActivity.selectedImagesList.addAll(ZakDataSelectionActivity.imagesList)
                }
                ZakMyConstants.FILE_TYPE_CONTACTS -> {
                    ZakDataSelectionActivity.contactsList.forEach {
                        it.isSelected = true
                    }
                    ZakDataSelectionActivity.selectedContactsList.clear()
                    ZakDataSelectionActivity.selectedContactsList.addAll(ZakDataSelectionActivity.contactsList)
                }
                ZakMyConstants.FILE_TYPE_DOCS -> {
                    ZakDataSelectionActivity.docsList.forEach {
                        it.isSelected = true
                    }
                    ZakDataSelectionActivity.selectedDocsList.clear()
                    ZakDataSelectionActivity.selectedDocsList.addAll(ZakDataSelectionActivity.docsList)
                }
                ZakMyConstants.FILE_TYPE_AUDIOS -> {
                    ZakDataSelectionActivity.audiosList.forEach {
                        it.isSelected = true
                    }
                    ZakDataSelectionActivity.selectedAudiosList.clear()
                    ZakDataSelectionActivity.selectedAudiosList.addAll(ZakDataSelectionActivity.audiosList)
                }
            }
        } else {
            when (FILE_TYPE) {
                ZakMyConstants.FILE_TYPE_CALENDAR -> {
                    ZakDataSelectionActivity.calendarEventsList.forEach {
                        it.isSelected = false
                    }
                    ZakDataSelectionActivity.selectedCalendarEventsList.clear()

                }
                ZakMyConstants.FILE_TYPE_APPS -> {
                    ZakDataSelectionActivity.apksList.forEach {
                        it.isSelected = false
                    }
                    ZakDataSelectionActivity.selectedApksList.clear()

                }
                ZakMyConstants.FILE_TYPE_VIDEOS -> {
                    ZakDataSelectionActivity.videosList.forEach {
                        it.isSelected = false
                    }
                    ZakDataSelectionActivity.selectedVideosList.clear()

                }
                ZakMyConstants.FILE_TYPE_PICS -> {
                    ZakDataSelectionActivity.imagesList.forEach {
                        it.isSelected = false
                    }
                    ZakDataSelectionActivity.selectedImagesList.clear()

                }
                ZakMyConstants.FILE_TYPE_CONTACTS -> {
                    ZakDataSelectionActivity.contactsList.forEach {
                        it.isSelected = false
                    }
                    ZakDataSelectionActivity.selectedContactsList.clear()

                }
                ZakMyConstants.FILE_TYPE_AUDIOS -> {
                    ZakDataSelectionActivity.audiosList.forEach {
                        it.isSelected = false
                    }
                    ZakDataSelectionActivity.selectedAudiosList.clear()
                }
                ZakMyConstants.FILE_TYPE_DOCS -> {
                    ZakDataSelectionActivity.docsList.forEach {
                        it.isSelected = false
                    }
                    ZakDataSelectionActivity.selectedDocsList.clear()
                }
            }
        }
        HSAdapter?.notifyDataSetChanged()
    }
    val isSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ZakActivityShowItemsForSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        loadFbBannerAdd()*/

        if (intent.hasExtra(ZakMyConstants.FILE_TYPE)) {
            val fileType = intent.getStringExtra(ZakMyConstants.FILE_TYPE)
            val isSelected = intent.getBooleanExtra(ZakMyConstants.IS_SELECTED, false)
            binding.cbSelectAllGivenItems.setOnCheckedChangeListener(null)
            binding.cbSelectAllGivenItems.isChecked = isSelected
            binding.cbSelectAllGivenItems.setOnCheckedChangeListener(checkBoxListener)
            FILE_TYPE = fileType
            ZakAdapterGeneralShowItems.FILE_TYPE = fileType

            //   loadFiles()
            ldView()
            binding.btnDone.setOnClickListener {
                finish()
            }
            binding.cbSelectAllGivenItems.setOnCheckedChangeListener(checkBoxListener)
        }

    }

    private fun loadFiles() {
        when (FILE_TYPE) {
            ZakMyConstants.FILE_TYPE_APPS -> {
                performAppSelection(isSelected)
                binding.rvGeneral.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                HSAdapter = ZakAdapterGeneralShowItems(
                    this,
                    ZakDataSelectionActivity.apksList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as ZakAppsModel
                            if (isChecked) {
                                ZakDataSelectionActivity.selectedApksList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                ZakDataSelectionActivity.selectedApksList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageAppsSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            ZakMyConstants.FILE_TYPE_VIDEOS -> {
                performVideosSelection(isSelected)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 3)
                HSAdapter = ZakAdapterGeneralShowItems(
                    this,
                    ZakDataSelectionActivity.videosList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as ZakMediaModelClass
                            if (isChecked) {
                                ZakDataSelectionActivity.selectedVideosList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                ZakDataSelectionActivity.selectedVideosList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageVideosSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            ZakMyConstants.FILE_TYPE_PICS -> {
                performImagesSelection(isSelected)
                binding.rvGeneral.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                HSAdapter = ZakAdapterGeneralShowItems(
                    this,
                    ZakDataSelectionActivity.imagesList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as ZakMediaModelClass
                            if (isChecked) {
                                ZakDataSelectionActivity.selectedImagesList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                ZakDataSelectionActivity.selectedImagesList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageImagesSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            ZakMyConstants.FILE_TYPE_CONTACTS -> {
                performContactsSelection(isSelected)
                ZakContactsModel.icon =
                    applicationContext.resources.getDrawable(R.drawable.ic_contacts, null)
                binding.rvGeneral.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                HSAdapter = ZakAdapterGeneralShowItems(
                    this,
                    ZakDataSelectionActivity.contactsList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as ZakContactsModel
                            if (isChecked) {
                                ZakDataSelectionActivity.selectedContactsList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                ZakDataSelectionActivity.selectedContactsList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageContactsSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            ZakMyConstants.FILE_TYPE_AUDIOS -> {
                performAudiosSelection(isSelected)
//                    binding.rvGeneral.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 3)

                HSAdapter = ZakAdapterGeneralShowItems(
                    this,
                    ZakDataSelectionActivity.audiosList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as ZakFileSharingModel
                            if (isChecked) {
                                ZakDataSelectionActivity.selectedAudiosList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                ZakDataSelectionActivity.selectedAudiosList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageAudiosSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            ZakMyConstants.FILE_TYPE_DOCS -> {
                performDocsSelection(isSelected)
                binding.rvGeneral.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                HSAdapter = ZakAdapterGeneralShowItems(
                    this,
                    ZakDataSelectionActivity.docsList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as ZakFileSharingModel
                            if (isChecked) {
                                ZakDataSelectionActivity.selectedDocsList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                ZakDataSelectionActivity.selectedDocsList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageDocsSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
        }
    }

    private fun ldView() {
        binding.rvGeneral.visibility = View.GONE
        binding.cbSelectAllGivenItems.visibility = View.GONE
        binding.pbarLoad.visibility = View.VISIBLE
        picAndVideosFolders.clear()
        audioAndDocFolders.clear()
        if (folderHSAdapterViewFolder == null) {
            when (FILE_TYPE) {
                ZakMyConstants.FILE_TYPE_VIDEOS -> {
                    for (i in ZakDataSelectionActivity.videosList) {
                        if (picAndVideosFolders.containsKey(i.getParentName())) {
                            var arr =
                                picAndVideosFolders[i.getParentName()] as ArrayList<ZakMediaModelClass>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<ZakMediaModelClass>()
                            arr.add(i)
                            picAndVideosFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                ZakMyConstants.FILE_TYPE_PICS -> {
                    for (i in ZakDataSelectionActivity.imagesList) {
                        if (picAndVideosFolders.containsKey(i.getParentName())) {
                            var arr =
                                picAndVideosFolders[i.getParentName()] as ArrayList<ZakMediaModelClass>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<ZakMediaModelClass>()
                            arr.add(i)
                            picAndVideosFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                ZakMyConstants.FILE_TYPE_AUDIOS -> {
                    for (i in ZakDataSelectionActivity.audiosList) {
                        if (audioAndDocFolders.containsKey(i.getParentName())) {
                            var arr =
                                audioAndDocFolders[i.getParentName()] as ArrayList<ZakFileSharingModel>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<ZakFileSharingModel>()
                            arr.add(i)
                            audioAndDocFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                ZakMyConstants.FILE_TYPE_DOCS -> {
                    for (i in ZakDataSelectionActivity.docsList) {
                        if (audioAndDocFolders.containsKey(i.getParentName())) {
                            var arr =
                                audioAndDocFolders[i.getParentName()] as ArrayList<ZakFileSharingModel>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<ZakFileSharingModel>()
                            arr.add(i)
                            audioAndDocFolders.set(i.getParentName(), arr)
                        }
                    }
                }
            }


            when (FILE_TYPE) {
                ZakMyConstants.FILE_TYPE_APPS -> {
                    performAppSelection(isSelected)
                    binding.rvGeneral.layoutManager = GridLayoutManager(this, 4)
                    HSAdapter = ZakAdapterGeneralShowItems(
                        this,
                        ZakDataSelectionActivity.apksList,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as ZakAppsModel
                                if (isChecked) {
                                    ZakDataSelectionActivity.selectedApksList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    ZakDataSelectionActivity.selectedApksList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageAppsSelection()
                            }


                        })
                    binding.rvGeneral.adapter = HSAdapter
                    folderHSAdapterViewFolder?.notifyDataSetChanged()
                }
                ZakMyConstants.FILE_TYPE_CONTACTS -> {
                    performContactsSelection(isSelected)
                    ZakContactsModel.icon =
                        applicationContext.resources.getDrawable(R.drawable.ic_contacts, null)
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    HSAdapter = ZakAdapterGeneralShowItems(
                        this,
                        ZakDataSelectionActivity.contactsList,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as ZakContactsModel
                                if (isChecked) {
                                    ZakDataSelectionActivity.selectedContactsList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    ZakDataSelectionActivity.selectedContactsList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageContactsSelection()
                            }


                        })
                    binding.rvGeneral.adapter = HSAdapter
                    folderHSAdapterViewFolder?.notifyDataSetChanged()
                }
                ZakMyConstants.FILE_TYPE_VIDEOS -> {

                    folderHSAdapterViewFolder = ZakAdapterViewFolder(
                        this,
                        picAndVideosFolders,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as ZakMediaModelClass
                                if (isChecked) {
                                    ZakDataSelectionActivity.selectedVideosList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    ZakDataSelectionActivity.selectedVideosList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageVideosSelection()
                            }


                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderHSAdapterViewFolder
                    folderHSAdapterViewFolder?.notifyDataSetChanged()
                }
                ZakMyConstants.FILE_TYPE_PICS -> {
                    performImagesSelection(isSelected)
                    folderHSAdapterViewFolder = ZakAdapterViewFolder(
                        this,
                        picAndVideosFolders,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as ZakMediaModelClass
                                if (isChecked) {
                                    ZakDataSelectionActivity.selectedImagesList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    ZakDataSelectionActivity.selectedImagesList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageImagesSelection()
                            }


                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderHSAdapterViewFolder
                    folderHSAdapterViewFolder?.notifyDataSetChanged()

                }
                ZakMyConstants.FILE_TYPE_AUDIOS -> {
                    performAudiosSelection(isSelected)
//                    binding.rvGeneral.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                    folderHSAdapterViewFolder = ZakAdapterViewFolder(
                        audioAndDocFolders,
                        context = this,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as ZakFileSharingModel
                                if (isChecked) {
                                    ZakDataSelectionActivity.selectedAudiosList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    ZakDataSelectionActivity.selectedAudiosList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageAudiosSelection()
                            }


                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderHSAdapterViewFolder
                    folderHSAdapterViewFolder?.notifyDataSetChanged()

                }
                ZakMyConstants.FILE_TYPE_DOCS -> {
                    performDocsSelection(isSelected)
                    folderHSAdapterViewFolder = ZakAdapterViewFolder(
                        audioAndDocFolders,
                        context = this,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as ZakFileSharingModel
                                if (isChecked) {
                                    ZakDataSelectionActivity.selectedDocsList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    ZakDataSelectionActivity.selectedDocsList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageDocsSelection()
                            }

                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderHSAdapterViewFolder
                    folderHSAdapterViewFolder?.notifyDataSetChanged()
                }
            }
        }

        binding.rvGeneral.visibility = View.VISIBLE
        binding.cbSelectAllGivenItems.visibility = View.VISIBLE
        binding.pbarLoad.visibility = View.GONE


    }

    private fun performAudiosSelection(selected: Boolean) {
        if (selected && ZakDataSelectionActivity.selectedAudiosList.size != 0) {
            ZakDataSelectionActivity.audiosList.forEach {
                it.isSelected = true
            }
            ZakDataSelectionActivity.selectedAudiosList.clear()
            ZakDataSelectionActivity.selectedAudiosList.addAll(ZakDataSelectionActivity.audiosList)
        } else if (ZakDataSelectionActivity.selectedAudiosList.size == 0) {
            ZakDataSelectionActivity.audiosList.forEach {
                it.isSelected = false
            }
            ZakDataSelectionActivity.selectedAudiosList.clear()
        }
    }

    private fun performDocsSelection(selected: Boolean) {
        if (selected && ZakDataSelectionActivity.selectedDocsList.size != 0) {
            ZakDataSelectionActivity.docsList.forEach {
                it.isSelected = true
            }
            ZakDataSelectionActivity.selectedDocsList.clear()
            ZakDataSelectionActivity.selectedDocsList.addAll(ZakDataSelectionActivity.docsList)
        } else if (ZakDataSelectionActivity.selectedDocsList.size == 0) {
            ZakDataSelectionActivity.docsList.forEach {
                it.isSelected = false
            }
            ZakDataSelectionActivity.selectedDocsList.clear()
        }
    }

    private fun performAppSelection(selected: Boolean) {
        if (selected && ZakDataSelectionActivity.selectedApksList.size != 0) {
            ZakDataSelectionActivity.apksList.forEach {
                it.isSelected = true
            }
            ZakDataSelectionActivity.selectedApksList.clear()
            ZakDataSelectionActivity.selectedApksList.addAll(ZakDataSelectionActivity.apksList)
        } else if (ZakDataSelectionActivity.selectedApksList.size == 0) {
            ZakDataSelectionActivity.apksList.forEach {
                it.isSelected = false
            }
            ZakDataSelectionActivity.selectedApksList.clear()
        }
    }

    private fun performContactsSelection(selected: Boolean) {
        if (selected && ZakDataSelectionActivity.selectedContactsList.size != 0) {
            ZakDataSelectionActivity.contactsList.forEach {
                it.isSelected = true
            }
            ZakDataSelectionActivity.selectedContactsList.clear()
            ZakDataSelectionActivity.selectedContactsList.addAll(ZakDataSelectionActivity.contactsList)
        } else if (ZakDataSelectionActivity.selectedContactsList.size == 0) {
            ZakDataSelectionActivity.contactsList.forEach {
                it.isSelected = false
            }
            ZakDataSelectionActivity.selectedContactsList.clear()
        }
    }

    private fun performVideosSelection(selected: Boolean) {
        if (selected && ZakDataSelectionActivity.selectedVideosList.size != 0) {
            ZakDataSelectionActivity.videosList.forEach {
                it.isSelected = true
            }
            ZakDataSelectionActivity.selectedVideosList.clear()
            ZakDataSelectionActivity.selectedVideosList.addAll(ZakDataSelectionActivity.videosList)
        } else if (ZakDataSelectionActivity.selectedVideosList.size == 0) {
            ZakDataSelectionActivity.videosList.forEach {
                it.isSelected = false
            }
            ZakDataSelectionActivity.selectedVideosList.clear()
        }
    }

    private fun performImagesSelection(selected: Boolean) {
        if (selected && ZakDataSelectionActivity.selectedImagesList.size != 0) {
            ZakDataSelectionActivity.imagesList.forEach {
                it.isSelected = true
            }
            ZakDataSelectionActivity.selectedImagesList.clear()
            ZakDataSelectionActivity.selectedImagesList.addAll(ZakDataSelectionActivity.imagesList)
        } else if (ZakDataSelectionActivity.selectedImagesList.size == 0) {
            ZakDataSelectionActivity.imagesList.forEach {
                it.isSelected = false
            }
            ZakDataSelectionActivity.selectedImagesList.clear()
        }
    }


    interface ShowItemsCallBack {
        fun itemSelectionChanged(fileType: String, isChecked: Boolean, row: Any)
    }

    fun setCheckBoxState(isChecked: Boolean) {
        binding.cbSelectAllGivenItems.setOnCheckedChangeListener(null)
        binding.cbSelectAllGivenItems.isChecked = isChecked
        binding.cbSelectAllGivenItems.setOnCheckedChangeListener(checkBoxListener)
    }

    fun manageAppsSelection() {
        if (ZakDataSelectionActivity.apksList.size == ZakDataSelectionActivity.selectedApksList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageAudiosSelection() {
        if (ZakDataSelectionActivity.audiosList.size == ZakDataSelectionActivity.selectedAudiosList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageDocsSelection() {
        if (ZakDataSelectionActivity.docsList.size == ZakDataSelectionActivity.selectedDocsList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageImagesSelection() {
        if (ZakDataSelectionActivity.imagesList.size == ZakDataSelectionActivity.selectedImagesList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageContactsSelection() {
        if (ZakDataSelectionActivity.contactsList.size == ZakDataSelectionActivity.selectedContactsList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageVideosSelection() {
        if (ZakDataSelectionActivity.videosList.size == ZakDataSelectionActivity.selectedVideosList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }
/*
    fun loadFbBannerAdd() {

        val adView = AdView(
            this@HSShowItemsForSelection,
            this@HSShowItemsForSelection.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {

            }

            override fun onAdLoaded(ad: Ad) {
                // Ad loaded callback
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback

            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
            }
        }


        adView?.loadAd(adView?.buildLoadAdConfig()?.withAdListener(adListener)?.build())
        findViewById<RelativeLayout>(R.id.top_banner).addView(adView)
    }*/
}