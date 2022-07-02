package com.smartswitch.phoneclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.activities.CAPPDataSelectionMainActivity.Companion.checkbtnVisibility
import com.smartswitch.phoneclone.adapters.CAPPAdapterGeneralShowItems
import com.smartswitch.phoneclone.adapters.CAPPAdapterViewFolder
//import com.switchphone.transferdata.ads.AATIntersitialAdHelper
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.databinding.ActivityShowItemsForSelectionCappBinding
import com.smartswitch.phoneclone.modelclasses.CAPPAppsModel
import com.smartswitch.phoneclone.modelclasses.CAPPContactsModel
import com.smartswitch.phoneclone.modelclasses.CAPPFileSharingModel
import com.smartswitch.phoneclone.modelclasses.CAPPMediaModelClass
import java.util.*

class CAPPShowItemsForSelection : AppCompatActivity() {

//    lateinit var binding: ActivityShowItemsForSelectionBinding
    lateinit var binding: ActivityShowItemsForSelectionCappBinding
    lateinit var FILE_TYPE: String
    var HSAdapter: CAPPAdapterGeneralShowItems? = null

    var folderHSAdapterViewFolder: CAPPAdapterViewFolder? = null
    var picAndVideosFolders = HashMap<String, ArrayList<CAPPMediaModelClass>>()
    var audioAndDocFolders = HashMap<String, ArrayList<CAPPFileSharingModel>>()

    val checkBoxListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            when (FILE_TYPE) {
                CAPPMConstants.FILE_TYPE_CALENDAR -> {
                    CAPPDataSelectionMainActivity.calendarEventsList.forEach {
                        it.isSelected = true
                    }
                    CAPPDataSelectionMainActivity.selectedCalendarEventsList.clear()
                    CAPPDataSelectionMainActivity.selectedCalendarEventsList.addAll(CAPPDataSelectionMainActivity.calendarEventsList)
                }
                CAPPMConstants.FILE_TYPE_APPS -> {
                    CAPPDataSelectionMainActivity.apksList.forEach {
                        it.isSelected = true
                    }
                    CAPPDataSelectionMainActivity.selectedApksList.clear()
                    CAPPDataSelectionMainActivity.selectedApksList.addAll(CAPPDataSelectionMainActivity.apksList)
                }
                CAPPMConstants.FILE_TYPE_VIDEOS -> {
                    CAPPDataSelectionMainActivity.videosList.forEach {
                        it.isSelected = true
                    }
                    CAPPDataSelectionMainActivity.selectedVideosList.clear()
                    CAPPDataSelectionMainActivity.selectedVideosList.addAll(CAPPDataSelectionMainActivity.videosList)
                }
                CAPPMConstants.FILE_TYPE_PICS -> {
                    CAPPDataSelectionMainActivity.imagesList.forEach {
                        it.isSelected = true
                    }
                    CAPPDataSelectionMainActivity.selectedImagesList.clear()
                    CAPPDataSelectionMainActivity.selectedImagesList.addAll(CAPPDataSelectionMainActivity.imagesList)
                }
                CAPPMConstants.FILE_TYPE_CONTACTS -> {
                    CAPPDataSelectionMainActivity.contactsList.forEach {
                        it.isSelected = true
                    }
                    CAPPDataSelectionMainActivity.selectedContactsList.clear()
                    CAPPDataSelectionMainActivity.selectedContactsList.addAll(CAPPDataSelectionMainActivity.contactsList)
                }
                CAPPMConstants.FILE_TYPE_DOCS -> {
                    CAPPDataSelectionMainActivity.docsList.forEach {
                        it.isSelected = true
                    }
                    CAPPDataSelectionMainActivity.selectedDocsList.clear()
                    CAPPDataSelectionMainActivity.selectedDocsList.addAll(CAPPDataSelectionMainActivity.docsList)
                }
                CAPPMConstants.FILE_TYPE_AUDIOS -> {
                    CAPPDataSelectionMainActivity.audiosList.forEach {
                        it.isSelected = true
                    }
                    CAPPDataSelectionMainActivity.selectedAudiosList.clear()
                    CAPPDataSelectionMainActivity.selectedAudiosList.addAll(CAPPDataSelectionMainActivity.audiosList)
                }
            }
        } else {
            when (FILE_TYPE) {
                CAPPMConstants.FILE_TYPE_CALENDAR -> {
                    CAPPDataSelectionMainActivity.calendarEventsList.forEach {
                        it.isSelected = false
                    }
                    CAPPDataSelectionMainActivity.selectedCalendarEventsList.clear()

                }
                CAPPMConstants.FILE_TYPE_APPS -> {
                    CAPPDataSelectionMainActivity.apksList.forEach {
                        it.isSelected = false
                    }
                    CAPPDataSelectionMainActivity.selectedApksList.clear()

                }
                CAPPMConstants.FILE_TYPE_VIDEOS -> {
                    CAPPDataSelectionMainActivity.videosList.forEach {
                        it.isSelected = false
                    }
                    CAPPDataSelectionMainActivity.selectedVideosList.clear()

                }
                CAPPMConstants.FILE_TYPE_PICS -> {
                    CAPPDataSelectionMainActivity.imagesList.forEach {
                        it.isSelected = false
                    }
                    CAPPDataSelectionMainActivity.selectedImagesList.clear()

                }
                CAPPMConstants.FILE_TYPE_CONTACTS -> {
                    CAPPDataSelectionMainActivity.contactsList.forEach {
                        it.isSelected = false
                    }
                    CAPPDataSelectionMainActivity.selectedContactsList.clear()

                }
                CAPPMConstants.FILE_TYPE_AUDIOS -> {
                    CAPPDataSelectionMainActivity.audiosList.forEach {
                        it.isSelected = false
                    }
                    CAPPDataSelectionMainActivity.selectedAudiosList.clear()
                }
                CAPPMConstants.FILE_TYPE_DOCS -> {
                    CAPPDataSelectionMainActivity.docsList.forEach {
                        it.isSelected = false
                    }
                    CAPPDataSelectionMainActivity.selectedDocsList.clear()
                }
            }
        }
        HSAdapter?.notifyDataSetChanged()
    }
    val isSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowItemsForSelectionCappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_group_back_white)
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

/*



        loadFbBannerAdd()*/
//
//        AATIntersitialAdHelper.loadAdmobBanner(
//            this,
//            findViewById(R.id.top_banner),
//            resources.getString(R.string.admob_banner)
//        )


        if (intent.hasExtra(CAPPMConstants.FILE_TYPE)) {
            val fileType = intent.getStringExtra(CAPPMConstants.FILE_TYPE)
            val isSelected = intent.getBooleanExtra(CAPPMConstants.IS_SELECTED, false)
            binding.cbSelectAllGivenItems.setOnCheckedChangeListener(null)
            binding.cbSelectAllGivenItems.isChecked = isSelected
            binding.cbSelectAllGivenItems.setOnCheckedChangeListener(checkBoxListener)
            if (fileType != null) {
                FILE_TYPE = fileType
            }
            CAPPAdapterGeneralShowItems.FILE_TYPE = fileType!!

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
            CAPPMConstants.FILE_TYPE_APPS -> {
                performAppSelection(isSelected)
                binding.rvGeneral.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                HSAdapter = CAPPAdapterGeneralShowItems(
                    this,
                    CAPPDataSelectionMainActivity.apksList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as CAPPAppsModel
                            if (isChecked) {
                                CAPPDataSelectionMainActivity.selectedApksList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                CAPPDataSelectionMainActivity.selectedApksList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageAppsSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            CAPPMConstants.FILE_TYPE_VIDEOS -> {
                performVideosSelection(isSelected)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 1)
                HSAdapter = CAPPAdapterGeneralShowItems(
                    this,
                    CAPPDataSelectionMainActivity.videosList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as CAPPMediaModelClass
                            if (isChecked) {
                                CAPPDataSelectionMainActivity.selectedVideosList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                CAPPDataSelectionMainActivity.selectedVideosList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageVideosSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            CAPPMConstants.FILE_TYPE_PICS -> {
                performImagesSelection(isSelected)
                binding.rvGeneral.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                HSAdapter = CAPPAdapterGeneralShowItems(
                    this,
                    CAPPDataSelectionMainActivity.imagesList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as CAPPMediaModelClass
                            if (isChecked) {
                                CAPPDataSelectionMainActivity.selectedImagesList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                CAPPDataSelectionMainActivity.selectedImagesList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageImagesSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            CAPPMConstants.FILE_TYPE_CONTACTS -> {
                performContactsSelection(isSelected)
                CAPPContactsModel.icon =
                    applicationContext.resources.getDrawable(R.drawable.msg, null)
                binding.rvGeneral.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                HSAdapter = CAPPAdapterGeneralShowItems(
                    this,
                    CAPPDataSelectionMainActivity.contactsList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as CAPPContactsModel
                            if (isChecked) {
                                CAPPDataSelectionMainActivity.selectedContactsList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                CAPPDataSelectionMainActivity.selectedContactsList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageContactsSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            CAPPMConstants.FILE_TYPE_AUDIOS -> {
                performAudiosSelection(isSelected)
//                    binding.rvGeneral.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvGeneral.layoutManager = GridLayoutManager(this, 1)

                HSAdapter = CAPPAdapterGeneralShowItems(
                    this,
                    CAPPDataSelectionMainActivity.audiosList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as CAPPFileSharingModel
                            if (isChecked) {
                                CAPPDataSelectionMainActivity.selectedAudiosList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                CAPPDataSelectionMainActivity.selectedAudiosList.remove(row)
                                row.isSelected = !row.isSelected
                            }
                            manageAudiosSelection()
                        }

                    })
                binding.rvGeneral.adapter = HSAdapter
            }
            CAPPMConstants.FILE_TYPE_DOCS -> {
                performDocsSelection(isSelected)
                binding.rvGeneral.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                HSAdapter = CAPPAdapterGeneralShowItems(
                    this,
                    CAPPDataSelectionMainActivity.docsList,
                    object : ShowItemsCallBack {
                        override fun itemSelectionChanged(
                            fileType: String,
                            isChecked: Boolean,
                            _row: Any
                        ) {
                            val row = _row as CAPPFileSharingModel
                            if (isChecked) {
                                CAPPDataSelectionMainActivity.selectedDocsList.add(row)
                            } else {
                                row.isSelected = !row.isSelected
                                CAPPDataSelectionMainActivity.selectedDocsList.remove(row)
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
                CAPPMConstants.FILE_TYPE_VIDEOS -> {
                    for (i in CAPPDataSelectionMainActivity.videosList) {
                        if (picAndVideosFolders.containsKey(i.getParentName())) {
                            var arr =
                                picAndVideosFolders[i.getParentName()] as ArrayList<CAPPMediaModelClass>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<CAPPMediaModelClass>()
                            arr.add(i)
                            picAndVideosFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                CAPPMConstants.FILE_TYPE_PICS -> {
                    for (i in CAPPDataSelectionMainActivity.imagesList) {
                        if (picAndVideosFolders.containsKey(i.getParentName())) {
                            var arr =
                                picAndVideosFolders[i.getParentName()] as ArrayList<CAPPMediaModelClass>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<CAPPMediaModelClass>()
                            arr.add(i)
                            picAndVideosFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                CAPPMConstants.FILE_TYPE_AUDIOS -> {
                    for (i in CAPPDataSelectionMainActivity.audiosList) {
                        if (audioAndDocFolders.containsKey(i.getParentName())) {
                            var arr =
                                audioAndDocFolders[i.getParentName()] as ArrayList<CAPPFileSharingModel>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<CAPPFileSharingModel>()
                            arr.add(i)
                            audioAndDocFolders.set(i.getParentName(), arr)
                        }
                    }
                }
                CAPPMConstants.FILE_TYPE_DOCS -> {
                    for (i in CAPPDataSelectionMainActivity.docsList) {
                        if (audioAndDocFolders.containsKey(i.getParentName())) {
                            var arr =
                                audioAndDocFolders[i.getParentName()] as ArrayList<CAPPFileSharingModel>
                            arr.add(i)
                        } else {
                            val arr = ArrayList<CAPPFileSharingModel>()
                            arr.add(i)
                            audioAndDocFolders.set(i.getParentName(), arr)
                        }
                    }
                }
            }


            when (FILE_TYPE) {
                CAPPMConstants.FILE_TYPE_APPS -> {
                    performAppSelection(isSelected)
                    binding.rvGeneral.layoutManager = GridLayoutManager(this, 1)
                    HSAdapter = CAPPAdapterGeneralShowItems(
                        this,
                        CAPPDataSelectionMainActivity.apksList,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as CAPPAppsModel
                                if (isChecked) {
                                    CAPPDataSelectionMainActivity.selectedApksList.add(row)
                                    checkbtnVisibility++
                                } else {
                                    row.isSelected = !row.isSelected
                                    CAPPDataSelectionMainActivity.selectedApksList.remove(row)
                                    row.isSelected = !row.isSelected
                                    checkbtnVisibility--
                                }
                                manageAppsSelection()
                            }


                        })
                    binding.rvGeneral.adapter = HSAdapter
                    folderHSAdapterViewFolder?.notifyDataSetChanged()
                }
                CAPPMConstants.FILE_TYPE_CONTACTS -> {
                    performContactsSelection(isSelected)
                    CAPPContactsModel.icon =
                        applicationContext.resources.getDrawable(R.drawable.msg, null)
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    HSAdapter = CAPPAdapterGeneralShowItems(
                        this,
                        CAPPDataSelectionMainActivity.contactsList,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as CAPPContactsModel
                                if (isChecked) {
                                    CAPPDataSelectionMainActivity.selectedContactsList.add(row)
                                    checkbtnVisibility++
                                } else {
                                    row.isSelected = !row.isSelected
                                    CAPPDataSelectionMainActivity.selectedContactsList.remove(row)
                                    row.isSelected = !row.isSelected
                                    checkbtnVisibility--
                                }
                                manageContactsSelection()
                            }


                        })
                    binding.rvGeneral.adapter = HSAdapter
                    folderHSAdapterViewFolder?.notifyDataSetChanged()
                }
                CAPPMConstants.FILE_TYPE_VIDEOS -> {

                    folderHSAdapterViewFolder = CAPPAdapterViewFolder(
                        this,
                        picAndVideosFolders,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as CAPPMediaModelClass
                                if (isChecked) {
                                    CAPPDataSelectionMainActivity.selectedVideosList.add(row)
                                    checkbtnVisibility++
                                } else {
                                    row.isSelected = !row.isSelected
                                    CAPPDataSelectionMainActivity.selectedVideosList.remove(row)
                                    row.isSelected = !row.isSelected
                                    checkbtnVisibility--
                                }
                                manageVideosSelection()
                            }


                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderHSAdapterViewFolder
                    folderHSAdapterViewFolder?.notifyDataSetChanged()
                }
                CAPPMConstants.FILE_TYPE_PICS -> {
                    performImagesSelection(isSelected)
                    folderHSAdapterViewFolder = CAPPAdapterViewFolder(
                        this,
                        picAndVideosFolders,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as CAPPMediaModelClass
                                if (isChecked) {
                                    CAPPDataSelectionMainActivity.selectedImagesList.add(row)
                                    checkbtnVisibility++
                                } else {
                                    row.isSelected = !row.isSelected
                                    CAPPDataSelectionMainActivity.selectedImagesList.remove(row)
                                    row.isSelected = !row.isSelected
                                    checkbtnVisibility--
                                }
                                manageImagesSelection()
                            }


                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderHSAdapterViewFolder
                    folderHSAdapterViewFolder?.notifyDataSetChanged()

                }
                CAPPMConstants.FILE_TYPE_AUDIOS -> {
                    performAudiosSelection(isSelected)
//                    binding.rvGeneral.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                    folderHSAdapterViewFolder = CAPPAdapterViewFolder(
                        audioAndDocFolders,
                        context = this,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as CAPPFileSharingModel
                                if (isChecked) {
                                    CAPPDataSelectionMainActivity.selectedAudiosList.add(row)
                                    checkbtnVisibility++
                                } else {
                                    row.isSelected = !row.isSelected
                                    CAPPDataSelectionMainActivity.selectedAudiosList.remove(row)
                                    row.isSelected = !row.isSelected
                                    checkbtnVisibility--
                                }
                                manageAudiosSelection()
                            }


                        })
                    binding.rvGeneral.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rvGeneral.adapter = folderHSAdapterViewFolder
                    folderHSAdapterViewFolder?.notifyDataSetChanged()

                }
                CAPPMConstants.FILE_TYPE_DOCS -> {
                    performDocsSelection(isSelected)
                    folderHSAdapterViewFolder = CAPPAdapterViewFolder(
                        audioAndDocFolders,
                        context = this,
                        object : ShowItemsCallBack {
                            override fun itemSelectionChanged(
                                fileType: String,
                                isChecked: Boolean,
                                _row: Any
                            ) {
                                val row = _row as CAPPFileSharingModel
                                if (isChecked) {
                                    CAPPDataSelectionMainActivity.selectedDocsList.add(row)
                                    checkbtnVisibility++
                                } else {
                                    row.isSelected = !row.isSelected
                                    CAPPDataSelectionMainActivity.selectedDocsList.remove(row)
                                    row.isSelected = !row.isSelected
                                    checkbtnVisibility--
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
        if (selected && CAPPDataSelectionMainActivity.selectedAudiosList.size != 0) {
            CAPPDataSelectionMainActivity.audiosList.forEach {
                it.isSelected = true
            }
            CAPPDataSelectionMainActivity.selectedAudiosList.clear()
            CAPPDataSelectionMainActivity.selectedAudiosList.addAll(CAPPDataSelectionMainActivity.audiosList)
        } else if (CAPPDataSelectionMainActivity.selectedAudiosList.size == 0) {
            CAPPDataSelectionMainActivity.audiosList.forEach {
                it.isSelected = false
            }
            CAPPDataSelectionMainActivity.selectedAudiosList.clear()
        }
    }

    private fun performDocsSelection(selected: Boolean) {
        if (selected && CAPPDataSelectionMainActivity.selectedDocsList.size != 0) {
            CAPPDataSelectionMainActivity.docsList.forEach {
                it.isSelected = true
            }
            CAPPDataSelectionMainActivity.selectedDocsList.clear()
            CAPPDataSelectionMainActivity.selectedDocsList.addAll(CAPPDataSelectionMainActivity.docsList)
        } else if (CAPPDataSelectionMainActivity.selectedDocsList.size == 0) {
            CAPPDataSelectionMainActivity.docsList.forEach {
                it.isSelected = false
            }
            CAPPDataSelectionMainActivity.selectedDocsList.clear()
        }
    }

    private fun performAppSelection(selected: Boolean) {
        if (selected && CAPPDataSelectionMainActivity.selectedApksList.size != 0) {
            CAPPDataSelectionMainActivity.apksList.forEach {
                it.isSelected = true
            }
            CAPPDataSelectionMainActivity.selectedApksList.clear()
            CAPPDataSelectionMainActivity.selectedApksList.addAll(CAPPDataSelectionMainActivity.apksList)
        } else if (CAPPDataSelectionMainActivity.selectedApksList.size == 0) {
            CAPPDataSelectionMainActivity.apksList.forEach {
                it.isSelected = false
            }
            CAPPDataSelectionMainActivity.selectedApksList.clear()
        }
    }

    private fun performContactsSelection(selected: Boolean) {
        if (selected && CAPPDataSelectionMainActivity.selectedContactsList.size != 0) {
            CAPPDataSelectionMainActivity.contactsList.forEach {
                it.isSelected = true
            }
            CAPPDataSelectionMainActivity.selectedContactsList.clear()
            CAPPDataSelectionMainActivity.selectedContactsList.addAll(CAPPDataSelectionMainActivity.contactsList)
        } else if (CAPPDataSelectionMainActivity.selectedContactsList.size == 0) {
            CAPPDataSelectionMainActivity.contactsList.forEach {
                it.isSelected = false
            }
            CAPPDataSelectionMainActivity.selectedContactsList.clear()
        }
    }

    private fun performVideosSelection(selected: Boolean) {
        if (selected && CAPPDataSelectionMainActivity.selectedVideosList.size != 0) {
            CAPPDataSelectionMainActivity.videosList.forEach {
                it.isSelected = true
            }
            CAPPDataSelectionMainActivity.selectedVideosList.clear()
            CAPPDataSelectionMainActivity.selectedVideosList.addAll(CAPPDataSelectionMainActivity.videosList)
        } else if (CAPPDataSelectionMainActivity.selectedVideosList.size == 0) {
            CAPPDataSelectionMainActivity.videosList.forEach {
                it.isSelected = false
            }
            CAPPDataSelectionMainActivity.selectedVideosList.clear()
        }
    }

    private fun performImagesSelection(selected: Boolean) {
        if (selected && CAPPDataSelectionMainActivity.selectedImagesList.size != 0) {
            CAPPDataSelectionMainActivity.imagesList.forEach {
                it.isSelected = true
            }
            CAPPDataSelectionMainActivity.selectedImagesList.clear()
            CAPPDataSelectionMainActivity.selectedImagesList.addAll(CAPPDataSelectionMainActivity.imagesList)
        } else if (CAPPDataSelectionMainActivity.selectedImagesList.size == 0) {
            CAPPDataSelectionMainActivity.imagesList.forEach {
                it.isSelected = false
            }
            CAPPDataSelectionMainActivity.selectedImagesList.clear()
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
        if (CAPPDataSelectionMainActivity.apksList.size == CAPPDataSelectionMainActivity.selectedApksList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageAudiosSelection() {
        if (CAPPDataSelectionMainActivity.audiosList.size == CAPPDataSelectionMainActivity.selectedAudiosList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageDocsSelection() {
        if (CAPPDataSelectionMainActivity.docsList.size == CAPPDataSelectionMainActivity.selectedDocsList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageImagesSelection() {
        if (CAPPDataSelectionMainActivity.imagesList.size == CAPPDataSelectionMainActivity.selectedImagesList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageContactsSelection() {
        if (CAPPDataSelectionMainActivity.contactsList.size == CAPPDataSelectionMainActivity.selectedContactsList.size) {
            setCheckBoxState(true)
        } else {
            setCheckBoxState(false)
        }
    }

    fun manageVideosSelection() {
        if (CAPPDataSelectionMainActivity.videosList.size == CAPPDataSelectionMainActivity.selectedVideosList.size) {
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