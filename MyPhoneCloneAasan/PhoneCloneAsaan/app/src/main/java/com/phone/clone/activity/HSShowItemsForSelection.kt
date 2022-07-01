package com.phone.clone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.phone.clone.R
import com.phone.clone.adapters.HSAdapterGeneralShowItems
import com.phone.clone.adapters.HSAdapterViewFolder
import com.phone.clone.constants.HSMyConstants
import com.phone.clone.databinding.ActivityShowItemsForSelectionBinding
import com.phone.clone.modelclasses.HSAppsModel
import com.phone.clone.modelclasses.HSContactsModel
import com.phone.clone.modelclasses.HSFileSharingModel
import com.phone.clone.modelclasses.HSMediaModelClass

class HSShowItemsForSelection : AppCompatActivity() {

    lateinit var binding: ActivityShowItemsForSelectionBinding
    lateinit var FILE_TYPE: String
    var HSAdapter: HSAdapterGeneralShowItems? = null

    var folderHSAdapterViewFolder: HSAdapterViewFolder? = null
    var picAndVideosFolders = HashMap<String, ArrayList<HSMediaModelClass>>()
    var audioAndDocFolders = HashMap<String, ArrayList<HSFileSharingModel>>()

    val checkBoxListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            when (FILE_TYPE) {
                HSMyConstants.FILE_TYPE_CALENDAR -> {
                    HSDataSelectionActivity.calendarEventsList.forEach {
                        it.isSelected = true
                    }
                    HSDataSelectionActivity.selectedCalendarEventsList.clear()
                    HSDataSelectionActivity.selectedCalendarEventsList.addAll(HSDataSelectionActivity.calendarEventsList)
                }
                HSMyConstants.FILE_TYPE_APPS -> {
                    HSDataSelectionActivity.apksList.forEach {
                        it.isSelected = true
                    }
                    HSDataSelectionActivity.selectedApksList.clear()
                    HSDataSelectionActivity.selectedApksList.addAll(HSDataSelectionActivity.apksList)
                }
                HSMyConstants.FILE_TYPE_VIDEOS -> {
                    HSDataSelectionActivity.videosList.forEach {
                        it.isSelected = true
                    }
                    HSDataSelectionActivity.selectedVideosList.clear()
                    HSDataSelectionActivity.selectedVideosList.addAll(HSDataSelectionActivity.videosList)
                }
                HSMyConstants.FILE_TYPE_PICS -> {
                    HSDataSelectionActivity.imagesList.forEach {
                        it.isSelected = true
                    }
                    HSDataSelectionActivity.selectedImagesList.clear()
                    HSDataSelectionActivity.selectedImagesList.addAll(HSDataSelectionActivity.imagesList)
                }
                HSMyConstants.FILE_TYPE_CONTACTS -> {
                    HSDataSelectionActivity.contactsList.forEach {
                        it.isSelected = true
                    }
                    HSDataSelectionActivity.selectedContactsList.clear()
                    HSDataSelectionActivity.selectedContactsList.addAll(HSDataSelectionActivity.contactsList)
                }
                HSMyConstants.FILE_TYPE_DOCS -> {
                    HSDataSelectionActivity.docsList.forEach {
                        it.isSelected = true
                    }
                    HSDataSelectionActivity.selectedDocsList.clear()
                    HSDataSelectionActivity.selectedDocsList.addAll(HSDataSelectionActivity.docsList)
                }
                HSMyConstants.FILE_TYPE_AUDIOS -> {
                    HSDataSelectionActivity.audiosList.forEach {
                        it.isSelected = true
                    }
                    HSDataSelectionActivity.selectedAudiosList.clear()
                    HSDataSelectionActivity.selectedAudiosList.addAll(HSDataSelectionActivity.audiosList)
                }
            }
        } else {
            when (FILE_TYPE) {
                HSMyConstants.FILE_TYPE_CALENDAR -> {
                    HSDataSelectionActivity.calendarEventsList.forEach {
                        it.isSelected = false
                    }
                    HSDataSelectionActivity.selectedCalendarEventsList.clear()

                }
                HSMyConstants.FILE_TYPE_APPS -> {
                    HSDataSelectionActivity.apksList.forEach {
                        it.isSelected = false
                    }
                    HSDataSelectionActivity.selectedApksList.clear()

                }
                HSMyConstants.FILE_TYPE_VIDEOS -> {
                    HSDataSelectionActivity.videosList.forEach {
                        it.isSelected = false
                    }
                    HSDataSelectionActivity.selectedVideosList.clear()

                }
                HSMyConstants.FILE_TYPE_PICS -> {
                    HSDataSelectionActivity.imagesList.forEach {
                        it.isSelected = false
                    }
                    HSDataSelectionActivity.selectedImagesList.clear()

                }
                HSMyConstants.FILE_TYPE_CONTACTS -> {
                    HSDataSelectionActivity.contactsList.forEach {
                        it.isSelected = false
                    }
                    HSDataSelectionActivity.selectedContactsList.clear()

                }
                HSMyConstants.FILE_TYPE_AUDIOS -> {
                    HSDataSelectionActivity.audiosList.forEach {
                        it.isSelected = false
                    }
                    HSDataSelectionActivity.selectedAudiosList.clear()
                }
                HSMyConstants.FILE_TYPE_DOCS -> {
                    HSDataSelectionActivity.docsList.forEach {
                        it.isSelected = false
                    }
                    HSDataSelectionActivity.selectedDocsList.clear()
                }
            }
        }
        HSAdapter?.notifyDataSetChanged()
    }
    val isSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowItemsForSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        loadFbBannerAdd()*/

        if (intent.hasExtra(HSMyConstants.FILE_TYPE)) {
            val fileType = intent.getStringExtra(HSMyConstants.FILE_TYPE)
            val isSelected = intent.getBooleanExtra(HSMyConstants.IS_SELECTED, false)
            binding.cbSelectAllGivenItems.setOnCheckedChangeListener(null)
            binding.cbSelectAllGivenItems.isChecked = isSelected
            binding.cbSelectAllGivenItems.setOnCheckedChangeListener(checkBoxListener)
            FILE_TYPE = fileType
            HSAdapterGeneralShowItems.FILE_TYPE = fileType

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
            HSMyConstants.FILE_TYPE_APPS -> {
                performAppSelection(isSelected)
                binding.rvGeneral.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                HSAdapter = HSAdapterGeneralShowItems(
                    this,
                    HSDataSelectionActivity.apksList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as HSAppsModel
                            if (isChecked) {
                                HSDataSelectionActivity.selectedApksList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                HSDataSelectionActivity.selectedApksList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageAppsSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            HSMyConstants.FILE_TYPE_VIDEOS -> {
                performVideosSelection(isSelected)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 3)
                HSAdapter = HSAdapterGeneralShowItems(
                    this,
                    HSDataSelectionActivity.videosList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as HSMediaModelClass
                            if (isChecked) {
                                HSDataSelectionActivity.selectedVideosList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                HSDataSelectionActivity.selectedVideosList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageVideosSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            HSMyConstants.FILE_TYPE_PICS -> {
                performImagesSelection(isSelected)
                binding.rvGeneral.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                HSAdapter = HSAdapterGeneralShowItems(
                    this,
                    HSDataSelectionActivity.imagesList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as HSMediaModelClass
                            if (isChecked) {
                                HSDataSelectionActivity.selectedImagesList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                HSDataSelectionActivity.selectedImagesList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageImagesSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            HSMyConstants.FILE_TYPE_CONTACTS -> {
                performContactsSelection(isSelected)
                HSContactsModel.icon =
                    applicationContext.resources.getDrawable(R.drawable.ic_contacts, null)
                binding.rvGeneral.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                HSAdapter = HSAdapterGeneralShowItems(
                    this,
                    HSDataSelectionActivity.contactsList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as HSContactsModel
                            if (isChecked) {
                                HSDataSelectionActivity.selectedContactsList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                HSDataSelectionActivity.selectedContactsList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageContactsSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            HSMyConstants.FILE_TYPE_AUDIOS -> {
                performAudiosSelection(isSelected)
//                    binding.rvGeneral.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 3)

                HSAdapter = HSAdapterGeneralShowItems(
                    this,
                    HSDataSelectionActivity.audiosList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as HSFileSharingModel
                            if (isChecked) {
                                HSDataSelectionActivity.selectedAudiosList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                HSDataSelectionActivity.selectedAudiosList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageAudiosSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            HSMyConstants.FILE_TYPE_DOCS -> {
                performDocsSelection(isSelected)
                binding.rvGeneral.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                HSAdapter = HSAdapterGeneralShowItems(
                    this,
                    HSDataSelectionActivity.docsList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as HSFileSharingModel
                            if (isChecked) {
                                HSDataSelectionActivity.selectedDocsList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                HSDataSelectionActivity.selectedDocsList.remove(row)
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
                HSMyConstants.FILE_TYPE_VIDEOS -> {
                    for (i in HSDataSelectionActivity.videosList) {
                        if (picAndVideosFolders.containsKey(i.getParentName())) {
                            var arr =
                                picAndVideosFolders[i.getParentName()] as ArrayList<HSMediaModelClass>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<HSMediaModelClass>()
                            arr.add(i)
                            picAndVideosFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                HSMyConstants.FILE_TYPE_PICS -> {
                    for (i in HSDataSelectionActivity.imagesList) {
                        if (picAndVideosFolders.containsKey(i.getParentName())) {
                            var arr =
                                picAndVideosFolders[i.getParentName()] as ArrayList<HSMediaModelClass>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<HSMediaModelClass>()
                            arr.add(i)
                            picAndVideosFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                HSMyConstants.FILE_TYPE_AUDIOS -> {
                    for (i in HSDataSelectionActivity.audiosList) {
                        if (audioAndDocFolders.containsKey(i.getParentName())) {
                            var arr =
                                audioAndDocFolders[i.getParentName()] as ArrayList<HSFileSharingModel>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<HSFileSharingModel>()
                            arr.add(i)
                            audioAndDocFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                HSMyConstants.FILE_TYPE_DOCS -> {
                    for (i in HSDataSelectionActivity.docsList) {
                        if (audioAndDocFolders.containsKey(i.getParentName())) {
                            var arr =
                                audioAndDocFolders[i.getParentName()] as ArrayList<HSFileSharingModel>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<HSFileSharingModel>()
                            arr.add(i)
                            audioAndDocFolders.set(i.getParentName(), arr)
                        }
                    }
                }
            }


            when (FILE_TYPE) {
                HSMyConstants.FILE_TYPE_APPS -> {
                    performAppSelection(isSelected)
                    binding.rvGeneral.layoutManager = GridLayoutManager(this, 4)
                    HSAdapter = HSAdapterGeneralShowItems(
                        this,
                        HSDataSelectionActivity.apksList,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as HSAppsModel
                                if (isChecked) {
                                    HSDataSelectionActivity.selectedApksList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    HSDataSelectionActivity.selectedApksList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageAppsSelection()
                            }


                        })
                    binding.rvGeneral.adapter = HSAdapter
                    folderHSAdapterViewFolder?.notifyDataSetChanged()
                }
                HSMyConstants.FILE_TYPE_CONTACTS -> {
                    performContactsSelection(isSelected)
                    HSContactsModel.icon =
                        applicationContext.resources.getDrawable(R.drawable.ic_contacts, null)
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    HSAdapter = HSAdapterGeneralShowItems(
                        this,
                        HSDataSelectionActivity.contactsList,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as HSContactsModel
                                if (isChecked) {
                                    HSDataSelectionActivity.selectedContactsList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    HSDataSelectionActivity.selectedContactsList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageContactsSelection()
                            }


                        })
                    binding.rvGeneral.adapter = HSAdapter
                    folderHSAdapterViewFolder?.notifyDataSetChanged()
                }
                HSMyConstants.FILE_TYPE_VIDEOS -> {

                    folderHSAdapterViewFolder = HSAdapterViewFolder(
                        this,
                        picAndVideosFolders,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as HSMediaModelClass
                                if (isChecked) {
                                    HSDataSelectionActivity.selectedVideosList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    HSDataSelectionActivity.selectedVideosList.remove(row)
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
                HSMyConstants.FILE_TYPE_PICS -> {
                    performImagesSelection(isSelected)
                    folderHSAdapterViewFolder = HSAdapterViewFolder(
                        this,
                        picAndVideosFolders,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as HSMediaModelClass
                                if (isChecked) {
                                    HSDataSelectionActivity.selectedImagesList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    HSDataSelectionActivity.selectedImagesList.remove(row)
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
                HSMyConstants.FILE_TYPE_AUDIOS -> {
                    performAudiosSelection(isSelected)
//                    binding.rvGeneral.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                    folderHSAdapterViewFolder = HSAdapterViewFolder(
                        audioAndDocFolders,
                        context = this,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as HSFileSharingModel
                                if (isChecked) {
                                    HSDataSelectionActivity.selectedAudiosList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    HSDataSelectionActivity.selectedAudiosList.remove(row)
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
                HSMyConstants.FILE_TYPE_DOCS -> {
                    performDocsSelection(isSelected)
                    folderHSAdapterViewFolder = HSAdapterViewFolder(
                        audioAndDocFolders,
                        context = this,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as HSFileSharingModel
                                if (isChecked) {
                                    HSDataSelectionActivity.selectedDocsList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    HSDataSelectionActivity.selectedDocsList.remove(row)
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
        if (selected && HSDataSelectionActivity.selectedAudiosList.size != 0) {
            HSDataSelectionActivity.audiosList.forEach {
                it.isSelected = true
            }
            HSDataSelectionActivity.selectedAudiosList.clear()
            HSDataSelectionActivity.selectedAudiosList.addAll(HSDataSelectionActivity.audiosList)
        } else if (HSDataSelectionActivity.selectedAudiosList.size == 0) {
            HSDataSelectionActivity.audiosList.forEach {
                it.isSelected = false
            }
            HSDataSelectionActivity.selectedAudiosList.clear()
        }
    }

    private fun performDocsSelection(selected: Boolean) {
        if (selected && HSDataSelectionActivity.selectedDocsList.size != 0) {
            HSDataSelectionActivity.docsList.forEach {
                it.isSelected = true
            }
            HSDataSelectionActivity.selectedDocsList.clear()
            HSDataSelectionActivity.selectedDocsList.addAll(HSDataSelectionActivity.docsList)
        } else if (HSDataSelectionActivity.selectedDocsList.size == 0) {
            HSDataSelectionActivity.docsList.forEach {
                it.isSelected = false
            }
            HSDataSelectionActivity.selectedDocsList.clear()
        }
    }

    private fun performAppSelection(selected: Boolean) {
        if (selected && HSDataSelectionActivity.selectedApksList.size != 0) {
            HSDataSelectionActivity.apksList.forEach {
                it.isSelected = true
            }
            HSDataSelectionActivity.selectedApksList.clear()
            HSDataSelectionActivity.selectedApksList.addAll(HSDataSelectionActivity.apksList)
        } else if (HSDataSelectionActivity.selectedApksList.size == 0) {
            HSDataSelectionActivity.apksList.forEach {
                it.isSelected = false
            }
            HSDataSelectionActivity.selectedApksList.clear()
        }
    }

    private fun performContactsSelection(selected: Boolean) {
        if (selected && HSDataSelectionActivity.selectedContactsList.size != 0) {
            HSDataSelectionActivity.contactsList.forEach {
                it.isSelected = true
            }
            HSDataSelectionActivity.selectedContactsList.clear()
            HSDataSelectionActivity.selectedContactsList.addAll(HSDataSelectionActivity.contactsList)
        } else if (HSDataSelectionActivity.selectedContactsList.size == 0) {
            HSDataSelectionActivity.contactsList.forEach {
                it.isSelected = false
            }
            HSDataSelectionActivity.selectedContactsList.clear()
        }
    }

    private fun performVideosSelection(selected: Boolean) {
        if (selected && HSDataSelectionActivity.selectedVideosList.size != 0) {
            HSDataSelectionActivity.videosList.forEach {
                it.isSelected = true
            }
            HSDataSelectionActivity.selectedVideosList.clear()
            HSDataSelectionActivity.selectedVideosList.addAll(HSDataSelectionActivity.videosList)
        } else if (HSDataSelectionActivity.selectedVideosList.size == 0) {
            HSDataSelectionActivity.videosList.forEach {
                it.isSelected = false
            }
            HSDataSelectionActivity.selectedVideosList.clear()
        }
    }

    private fun performImagesSelection(selected: Boolean) {
        if (selected && HSDataSelectionActivity.selectedImagesList.size != 0) {
            HSDataSelectionActivity.imagesList.forEach {
                it.isSelected = true
            }
            HSDataSelectionActivity.selectedImagesList.clear()
            HSDataSelectionActivity.selectedImagesList.addAll(HSDataSelectionActivity.imagesList)
        } else if (HSDataSelectionActivity.selectedImagesList.size == 0) {
            HSDataSelectionActivity.imagesList.forEach {
                it.isSelected = false
            }
            HSDataSelectionActivity.selectedImagesList.clear()
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
        if (HSDataSelectionActivity.apksList.size == HSDataSelectionActivity.selectedApksList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageAudiosSelection() {
        if (HSDataSelectionActivity.audiosList.size == HSDataSelectionActivity.selectedAudiosList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageDocsSelection() {
        if (HSDataSelectionActivity.docsList.size == HSDataSelectionActivity.selectedDocsList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageImagesSelection() {
        if (HSDataSelectionActivity.imagesList.size == HSDataSelectionActivity.selectedImagesList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageContactsSelection() {
        if (HSDataSelectionActivity.contactsList.size == HSDataSelectionActivity.selectedContactsList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageVideosSelection() {
        if (HSDataSelectionActivity.videosList.size == HSDataSelectionActivity.selectedVideosList.size) {
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