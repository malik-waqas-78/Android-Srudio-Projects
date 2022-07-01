package com.phoneclone.data.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.ads.*
import com.phoneclone.data.R
import com.phoneclone.data.adapters.AdapterGeneralShowItems
import com.phoneclone.data.adapters.AdapterViewFolder
import com.phoneclone.data.ads.isAppInstalledFromPlay
import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.databinding.ActivityShowItemsForSelectionBinding
import com.phoneclone.data.modelclasses.AppsModel
import com.phoneclone.data.modelclasses.ContactsModel
import com.phoneclone.data.modelclasses.FileSharingModel
import com.phoneclone.data.modelclasses.MediaModelClass

class ShowItemsForSelection : AppCompatActivity() {

    lateinit var binding: ActivityShowItemsForSelectionBinding
    lateinit var FILE_TYPE: String
    var adapter: AdapterGeneralShowItems? = null

    var folderAdapterViewFolder: AdapterViewFolder? = null
    var picAndVideosFolders = HashMap<String, ArrayList<MediaModelClass>>()
    var audioAndDocFolders = HashMap<String, ArrayList<FileSharingModel>>()

    val checkBoxListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            when (FILE_TYPE) {
                MyConstants.FILE_TYPE_CALENDAR -> {
                    SelectionActivity.calendarEventsList.forEach {
                        it.isSelected = true
                    }
                    SelectionActivity.selectedCalendarEventsList.clear()
                    SelectionActivity.selectedCalendarEventsList.addAll(SelectionActivity.calendarEventsList)
                }
                MyConstants.FILE_TYPE_APPS -> {
                    SelectionActivity.apksList.forEach {
                        it.isSelected = true
                    }
                    SelectionActivity.selectedApksList.clear()
                    SelectionActivity.selectedApksList.addAll(SelectionActivity.apksList)
                }
                MyConstants.FILE_TYPE_VIDEOS -> {
                    SelectionActivity.videosList.forEach {
                        it.isSelected = true
                    }
                    SelectionActivity.selectedVideosList.clear()
                    SelectionActivity.selectedVideosList.addAll(SelectionActivity.videosList)
                }
                MyConstants.FILE_TYPE_PICS -> {
                    SelectionActivity.imagesList.forEach {
                        it.isSelected = true
                    }
                    SelectionActivity.selectedImagesList.clear()
                    SelectionActivity.selectedImagesList.addAll(SelectionActivity.imagesList)
                }
                MyConstants.FILE_TYPE_CONTACTS -> {
                    SelectionActivity.contactsList.forEach {
                        it.isSelected = true
                    }
                    SelectionActivity.selectedContactsList.clear()
                    SelectionActivity.selectedContactsList.addAll(SelectionActivity.contactsList)
                }
                MyConstants.FILE_TYPE_DOCS -> {
                    SelectionActivity.docsList.forEach {
                        it.isSelected = true
                    }
                    SelectionActivity.selectedDocsList.clear()
                    SelectionActivity.selectedDocsList.addAll(SelectionActivity.docsList)
                }
                MyConstants.FILE_TYPE_AUDIOS -> {
                    SelectionActivity.audiosList.forEach {
                        it.isSelected = true
                    }
                    SelectionActivity.selectedAudiosList.clear()
                    SelectionActivity.selectedAudiosList.addAll(SelectionActivity.audiosList)
                }
            }
        } else {
            when (FILE_TYPE) {
                MyConstants.FILE_TYPE_CALENDAR -> {
                    SelectionActivity.calendarEventsList.forEach {
                        it.isSelected = false
                    }
                    SelectionActivity.selectedCalendarEventsList.clear()

                }
                MyConstants.FILE_TYPE_APPS -> {
                    SelectionActivity.apksList.forEach {
                        it.isSelected = false
                    }
                    SelectionActivity.selectedApksList.clear()

                }
                MyConstants.FILE_TYPE_VIDEOS -> {
                    SelectionActivity.videosList.forEach {
                        it.isSelected = false
                    }
                    SelectionActivity.selectedVideosList.clear()

                }
                MyConstants.FILE_TYPE_PICS -> {
                    SelectionActivity.imagesList.forEach {
                        it.isSelected = false
                    }
                    SelectionActivity.selectedImagesList.clear()

                }
                MyConstants.FILE_TYPE_CONTACTS -> {
                    SelectionActivity.contactsList.forEach {
                        it.isSelected = false
                    }
                    SelectionActivity.selectedContactsList.clear()

                }
                MyConstants.FILE_TYPE_AUDIOS -> {
                    SelectionActivity.audiosList.forEach {
                        it.isSelected = false
                    }
                    SelectionActivity.selectedAudiosList.clear()
                }
                MyConstants.FILE_TYPE_DOCS -> {
                    SelectionActivity.docsList.forEach {
                        it.isSelected = false
                    }
                    SelectionActivity.selectedDocsList.clear()
                }
            }
        }
        adapter?.notifyDataSetChanged()
    }
    val isSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowItemsForSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isAppInstalledFromPlay(this)){
            loadFbBannerAdd()

        }

        if (intent.hasExtra(MyConstants.FILE_TYPE)) {
            val fileType = intent.getStringExtra(MyConstants.FILE_TYPE)
            val isSelected = intent.getBooleanExtra(MyConstants.IS_SELECTED, false)
            binding.cbSelectAllGivenItems.setOnCheckedChangeListener(null)
            binding.cbSelectAllGivenItems.isChecked = isSelected
            binding.cbSelectAllGivenItems.setOnCheckedChangeListener(checkBoxListener)
            FILE_TYPE = fileType
            AdapterGeneralShowItems.FILE_TYPE = fileType

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
            MyConstants.FILE_TYPE_APPS -> {
                performAppSelection(isSelected)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 4)
                adapter = AdapterGeneralShowItems(
                    this,
                    SelectionActivity.apksList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as AppsModel
                            if (isChecked) {
                                SelectionActivity.selectedApksList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                SelectionActivity.selectedApksList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageAppsSelection()
                        }

                    })
                binding.rvGeneral.adapter = adapter
            }
            MyConstants.FILE_TYPE_VIDEOS -> {
                performVideosSelection(isSelected)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 3)
                adapter = AdapterGeneralShowItems(
                    this,
                    SelectionActivity.videosList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as MediaModelClass
                            if (isChecked) {
                                SelectionActivity.selectedVideosList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                SelectionActivity.selectedVideosList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageVideosSelection()
                        }

                    })
                binding.rvGeneral.adapter = adapter
            }
            MyConstants.FILE_TYPE_PICS -> {
                performImagesSelection(isSelected)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 3)
                adapter = AdapterGeneralShowItems(
                    this,
                    SelectionActivity.imagesList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as MediaModelClass
                            if (isChecked) {
                                SelectionActivity.selectedImagesList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                SelectionActivity.selectedImagesList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageImagesSelection()
                        }

                    })
                binding.rvGeneral.adapter = adapter
            }
            MyConstants.FILE_TYPE_CONTACTS -> {
                performContactsSelection(isSelected)
                ContactsModel.icon =
                    applicationContext.resources.getDrawable(R.drawable.ic_contact, null)
                binding.rvGeneral.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                adapter = AdapterGeneralShowItems(
                    this,
                    SelectionActivity.contactsList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as ContactsModel
                            if (isChecked) {
                                SelectionActivity.selectedContactsList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                SelectionActivity.selectedContactsList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageContactsSelection()
                        }

                    })
                binding.rvGeneral.adapter = adapter
            }
            MyConstants.FILE_TYPE_AUDIOS -> {
                performAudiosSelection(isSelected)
//                    binding.rvGeneral.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 3)

                adapter = AdapterGeneralShowItems(
                    this,
                    SelectionActivity.audiosList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as FileSharingModel
                            if (isChecked) {
                                SelectionActivity.selectedAudiosList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                SelectionActivity.selectedAudiosList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageAudiosSelection()
                        }

                    })
                binding.rvGeneral.adapter = adapter
            }
            MyConstants.FILE_TYPE_DOCS -> {
                performDocsSelection(isSelected)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 3)
                adapter = AdapterGeneralShowItems(
                    this,
                    SelectionActivity.docsList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as FileSharingModel
                            if (isChecked) {
                                SelectionActivity.selectedDocsList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                SelectionActivity.selectedDocsList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageDocsSelection()
                        }

                    })
                binding.rvGeneral.adapter = adapter
            }
        }
    }

    private fun ldView() {
        binding.rvGeneral.visibility = View.GONE
        binding.cbSelectAllGivenItems.visibility = View.GONE
        binding.pbarLoad.visibility = View.VISIBLE
        picAndVideosFolders.clear()
        audioAndDocFolders.clear()
        if (folderAdapterViewFolder == null) {
            when (FILE_TYPE) {
                MyConstants.FILE_TYPE_VIDEOS -> {
                    for (i in SelectionActivity.videosList) {
                        if (picAndVideosFolders.containsKey(i.getParentName())) {
                            var arr =
                                picAndVideosFolders[i.getParentName()] as ArrayList<MediaModelClass>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<MediaModelClass>()
                            arr.add(i)
                            picAndVideosFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                MyConstants.FILE_TYPE_PICS -> {
                    for (i in SelectionActivity.imagesList) {
                        if (picAndVideosFolders.containsKey(i.getParentName())) {
                            var arr =
                                picAndVideosFolders[i.getParentName()] as ArrayList<MediaModelClass>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<MediaModelClass>()
                            arr.add(i)
                            picAndVideosFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                MyConstants.FILE_TYPE_AUDIOS -> {
                    for (i in SelectionActivity.audiosList) {
                        if (audioAndDocFolders.containsKey(i.getParentName())) {
                            var arr =
                                audioAndDocFolders[i.getParentName()] as ArrayList<FileSharingModel>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<FileSharingModel>()
                            arr.add(i)
                            audioAndDocFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                MyConstants.FILE_TYPE_DOCS -> {
                    for (i in SelectionActivity.docsList) {
                        if (audioAndDocFolders.containsKey(i.getParentName())) {
                            var arr =
                                audioAndDocFolders[i.getParentName()] as ArrayList<FileSharingModel>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<FileSharingModel>()
                            arr.add(i)
                            audioAndDocFolders.set(i.getParentName(), arr)
                        }
                    }
                }
            }


            when (FILE_TYPE) {
                MyConstants.FILE_TYPE_APPS -> {
                    performAppSelection(isSelected)
                    binding.rvGeneral.layoutManager = GridLayoutManager(this, 4)
                    adapter = AdapterGeneralShowItems(
                        this,
                        SelectionActivity.apksList,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as AppsModel
                                if (isChecked) {
                                    SelectionActivity.selectedApksList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    SelectionActivity.selectedApksList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageAppsSelection()
                            }


                        })
                    binding.rvGeneral.adapter = adapter
                    folderAdapterViewFolder?.notifyDataSetChanged()
                }
                MyConstants.FILE_TYPE_CONTACTS -> {
                    performContactsSelection(isSelected)
                    ContactsModel.icon =
                        applicationContext.resources.getDrawable(R.drawable.ic_contact, null)
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    adapter = AdapterGeneralShowItems(
                        this,
                        SelectionActivity.contactsList,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as ContactsModel
                                if (isChecked) {
                                    SelectionActivity.selectedContactsList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    SelectionActivity.selectedContactsList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageContactsSelection()
                            }


                        })
                    binding.rvGeneral.adapter = adapter
                    folderAdapterViewFolder?.notifyDataSetChanged()
                }
                MyConstants.FILE_TYPE_VIDEOS -> {

                    folderAdapterViewFolder = AdapterViewFolder(
                        this,
                        picAndVideosFolders,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as MediaModelClass
                                if (isChecked) {
                                    SelectionActivity.selectedVideosList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    SelectionActivity.selectedVideosList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageVideosSelection()
                            }


                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderAdapterViewFolder
                    folderAdapterViewFolder?.notifyDataSetChanged()
                }
                MyConstants.FILE_TYPE_PICS -> {
                    performImagesSelection(isSelected)
                    folderAdapterViewFolder = AdapterViewFolder(
                        this,
                        picAndVideosFolders,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as MediaModelClass
                                if (isChecked) {
                                    SelectionActivity.selectedImagesList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    SelectionActivity.selectedImagesList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageImagesSelection()
                            }


                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderAdapterViewFolder
                    folderAdapterViewFolder?.notifyDataSetChanged()

                }
                MyConstants.FILE_TYPE_AUDIOS -> {
                    performAudiosSelection(isSelected)
//                    binding.rvGeneral.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                    folderAdapterViewFolder = AdapterViewFolder(
                        audioAndDocFolders,
                        context = this,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as FileSharingModel
                                if (isChecked) {
                                    SelectionActivity.selectedAudiosList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    SelectionActivity.selectedAudiosList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageAudiosSelection()
                            }


                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderAdapterViewFolder
                    folderAdapterViewFolder?.notifyDataSetChanged()

                }
                MyConstants.FILE_TYPE_DOCS -> {
                    performDocsSelection(isSelected)
                    folderAdapterViewFolder = AdapterViewFolder(
                        audioAndDocFolders,
                        context = this,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as FileSharingModel
                                if (isChecked) {
                                    SelectionActivity.selectedDocsList.add(row)
                                } else {
                                    row.isSelected = !row.isSelected
                                    SelectionActivity.selectedDocsList.remove(row)
                                    row.isSelected = !row.isSelected
                                }
                                manageDocsSelection()
                            }

                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderAdapterViewFolder
                    folderAdapterViewFolder?.notifyDataSetChanged()
                }
            }
        }

        binding.rvGeneral.visibility = View.VISIBLE
        binding.cbSelectAllGivenItems.visibility = View.VISIBLE
        binding.pbarLoad.visibility = View.GONE


    }

    private fun performAudiosSelection(selected: Boolean) {
        if (selected && SelectionActivity.selectedAudiosList.size != 0) {
            SelectionActivity.audiosList.forEach {
                it.isSelected = true
            }
            SelectionActivity.selectedAudiosList.clear()
            SelectionActivity.selectedAudiosList.addAll(SelectionActivity.audiosList)
        } else if (SelectionActivity.selectedAudiosList.size == 0) {
            SelectionActivity.audiosList.forEach {
                it.isSelected = false
            }
            SelectionActivity.selectedAudiosList.clear()
        }
    }

    private fun performDocsSelection(selected: Boolean) {
        if (selected && SelectionActivity.selectedDocsList.size != 0) {
            SelectionActivity.docsList.forEach {
                it.isSelected = true
            }
            SelectionActivity.selectedDocsList.clear()
            SelectionActivity.selectedDocsList.addAll(SelectionActivity.docsList)
        } else if (SelectionActivity.selectedDocsList.size == 0) {
            SelectionActivity.docsList.forEach {
                it.isSelected = false
            }
            SelectionActivity.selectedDocsList.clear()
        }
    }

    private fun performAppSelection(selected: Boolean) {
        if (selected && SelectionActivity.selectedApksList.size != 0) {
            SelectionActivity.apksList.forEach {
                it.isSelected = true
            }
            SelectionActivity.selectedApksList.clear()
            SelectionActivity.selectedApksList.addAll(SelectionActivity.apksList)
        } else if (SelectionActivity.selectedApksList.size == 0) {
            SelectionActivity.apksList.forEach {
                it.isSelected = false
            }
            SelectionActivity.selectedApksList.clear()
        }
    }

    private fun performContactsSelection(selected: Boolean) {
        if (selected && SelectionActivity.selectedContactsList.size != 0) {
            SelectionActivity.contactsList.forEach {
                it.isSelected = true
            }
            SelectionActivity.selectedContactsList.clear()
            SelectionActivity.selectedContactsList.addAll(SelectionActivity.contactsList)
        } else if (SelectionActivity.selectedContactsList.size == 0) {
            SelectionActivity.contactsList.forEach {
                it.isSelected = false
            }
            SelectionActivity.selectedContactsList.clear()
        }
    }

    private fun performVideosSelection(selected: Boolean) {
        if (selected && SelectionActivity.selectedVideosList.size != 0) {
            SelectionActivity.videosList.forEach {
                it.isSelected = true
            }
            SelectionActivity.selectedVideosList.clear()
            SelectionActivity.selectedVideosList.addAll(SelectionActivity.videosList)
        } else if (SelectionActivity.selectedVideosList.size == 0) {
            SelectionActivity.videosList.forEach {
                it.isSelected = false
            }
            SelectionActivity.selectedVideosList.clear()
        }
    }

    private fun performImagesSelection(selected: Boolean) {
        if (selected && SelectionActivity.selectedImagesList.size != 0) {
            SelectionActivity.imagesList.forEach {
                it.isSelected = true
            }
            SelectionActivity.selectedImagesList.clear()
            SelectionActivity.selectedImagesList.addAll(SelectionActivity.imagesList)
        } else if (SelectionActivity.selectedImagesList.size == 0) {
            SelectionActivity.imagesList.forEach {
                it.isSelected = false
            }
            SelectionActivity.selectedImagesList.clear()
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
        if (SelectionActivity.apksList.size == SelectionActivity.selectedApksList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageAudiosSelection() {
        if (SelectionActivity.audiosList.size == SelectionActivity.selectedAudiosList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageDocsSelection() {
        if (SelectionActivity.docsList.size == SelectionActivity.selectedDocsList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageImagesSelection() {
        if (SelectionActivity.imagesList.size == SelectionActivity.selectedImagesList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageContactsSelection() {
        if (SelectionActivity.contactsList.size == SelectionActivity.selectedContactsList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageVideosSelection() {
        if (SelectionActivity.videosList.size == SelectionActivity.selectedVideosList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun loadFbBannerAdd() {

        val adView = AdView(
            this@ShowItemsForSelection,
            this@ShowItemsForSelection.resources.getString(R.string.banner_add),
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
    }
}