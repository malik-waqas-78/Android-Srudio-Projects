package com.zak.clone.zakactivity

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.CompoundButton
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager


import com.zak.clone.R
import com.zak.clone.databinding.ZakActivitySelectionBinding
import com.zak.clone.zakadapters.ZakAdapterSelection

import com.zak.clone.zakconstants.ZakMyConstants

import com.zak.clone.zakdatautils.*
import com.zak.clone.zakinterfaces.ZakUtilsCallBacks
import com.zak.clone.zakmodelclasses.*
import com.zak.clone.zakutills.ZakMyPermissions
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ZakDataSelectionActivity : AppCompatActivity(), ZakUtilsCallBacks {
    lateinit var binding: ZakActivitySelectionBinding

    companion object {

        var apksList = ArrayList<ZakAppsModel>()
        var calendarEventsList = ArrayList<ZakCalendarEventsModel>()
        var contactsList = ArrayList<ZakContactsModel>()
        var videosList = ArrayList<ZakMediaModelClass>()
        var imagesList = ArrayList<ZakMediaModelClass>()
        var docsList = ArrayList<ZakFileSharingModel>()
        var audiosList = ArrayList<ZakFileSharingModel>()


        var selectedApksList = ArrayList<ZakAppsModel>()
        var selectedCalendarEventsList = ArrayList<ZakCalendarEventsModel>()
        var selectedContactsList = ArrayList<ZakContactsModel>()
        var selectedVideosList = ArrayList<ZakMediaModelClass>()
        var selectedImagesList = ArrayList<ZakMediaModelClass>()
        var selectedDocsList = ArrayList<ZakFileSharingModel>()
        var selectedAudiosList = ArrayList<ZakFileSharingModel>()

    }

    var checkChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedApksList.clear()
                selectedApksList.addAll(apksList)

                selectedCalendarEventsList.clear()
                selectedCalendarEventsList.addAll(calendarEventsList)

                selectedContactsList.clear()
                selectedContactsList.addAll(contactsList)

                selectedImagesList.clear()
                selectedImagesList.addAll(imagesList)

                selectedVideosList.clear()
                selectedVideosList.addAll(videosList)

                selectedAudiosList.clear()
                selectedAudiosList.addAll(audiosList)

                selectedDocsList.clear()
                selectedDocsList.addAll(docsList)

                list?.forEach {
                    it.isSelected = true
                }
            } else {
                selectedApksList.clear()
                selectedCalendarEventsList.clear()
                selectedContactsList.clear()
                selectedImagesList.clear()
                selectedVideosList.clear()
                selectedAudiosList.clear()
                selectedDocsList.clear()
                list?.forEach {
                    it.isSelected = false
                }

            }
            if (list.isNotEmpty()) {

                list[0].selectionDetails = """(${selectedContactsList.size}"""
                list[0].selectionDetailsTotal = """/${contactsList.size})"""


                list[1].selectionDetails = """(${selectedImagesList.size}"""


                list[2].selectionDetails = """(${selectedCalendarEventsList.size}"""
                list[2].selectionDetailsTotal = """/${calendarEventsList.size})"""



                list[3].selectionDetails = """(${selectedVideosList.size}"""
                list[3].selectionDetailsTotal = """/${videosList.size})"""


                list[4].selectionDetails = """(${selectedApksList.size}"""
                list[4].selectionDetailsTotal = """/${apksList.size})"""


                list[5].selectionDetails = """(${selectedAudiosList.size}"""
                list[5].selectionDetailsTotal = """/${audiosList.size})"""

                list[6].selectionDetails = """(${selectedDocsList.size}"""
                list[6].selectionDetailsTotal = """/${docsList.size})"""

            }
            HSAdapter?.notifyDataSetChanged()
        }


    var list = ArrayList<ZAKFilesToShareModel>()

    var HSContactsHandler: ZakContactsUtils? = null
    var HSImagesHandler: ZakImagesUtils? = null
    var HSVideosHandler: ZAKVideosUtils? = null
    var calendarEventsUtils: ZakCalendarEventsUtils? = null
    var apksHandler: ZakAppsUtils? = null
    var docsHandler: ZakDocumentsUtils? = null
    var HSAudiosHanlder: ZakAudiosUtils? = null
    var HSAdapter: ZakAdapterSelection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ZakActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /* loadFbBannerAdd()
        HSNativeAdHelper.showAd(this@HSDataSelectionActivity,binding.nativeAdContainer)*/
        ZakMyConstants.FILES_TO_SHARE.clear()

        apksList.clear()
        selectedApksList.clear()

        audiosList.clear()
        selectedAudiosList.clear()

        docsList.clear()
        selectedDocsList.clear()

        imagesList.clear()
        selectedImagesList.clear()

        videosList.clear()
        selectedVideosList.clear()

        calendarEventsList.clear()
        selectedCalendarEventsList.clear()

        contactsList.clear()
        selectedContactsList.clear()

        //init handlers
        initHandlers()
        //load data
        if (ZakMyPermissions.hasStoragePermission(this@ZakDataSelectionActivity)) {
            startLoadingData()
        } else {
            ZakMyPermissions.showStorageExplanation(this@ZakDataSelectionActivity,
                object : ZakMyPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        ZakMyPermissions.requestStoragePermission(this@ZakDataSelectionActivity)
                    }

                    override fun denyPermission() {
                        //finish
                        finish()
                    }
                })
        }

    }

    override fun onResume() {
        super.onResume()
        manageSelectAllSelection()
    }

    private fun manageSelectAllSelection() {
        binding.checkBox.setOnCheckedChangeListener(null)
        binding.checkBox.isChecked = isAllFilesSelected()
        binding.checkBox.setOnCheckedChangeListener(checkChangeListener)

        if (list.isNotEmpty()) {

            list[0].selectionDetails = """(${selectedContactsList.size}"""
            list[0].selectionDetailsTotal = """/${contactsList.size})"""

            list[1].selectionDetails = """(${selectedImagesList.size}"""
            list[1].selectionDetailsTotal = """/${imagesList.size})"""



            list[2].selectionDetails = """(${selectedCalendarEventsList.size}"""
            list[2].selectionDetailsTotal = """/${calendarEventsList.size})"""


            list[3].selectionDetails = """(${selectedVideosList.size}"""
            list[3].selectionDetailsTotal = """/${videosList.size})"""


            list[4].selectionDetails = """(${selectedApksList.size}"""
            list[4].selectionDetailsTotal = """/${apksList.size})"""


            list[5].selectionDetails = """(${selectedAudiosList.size}"""
            list[5].selectionDetailsTotal = """/${audiosList.size})"""


            list[6].selectionDetails = """(${selectedDocsList.size}"""
            list[6].selectionDetailsTotal = """/${docsList.size})"""

        }


        HSAdapter?.notifyDataSetChanged()
    }

    private fun isAllFilesSelected(): Boolean {
        return isContactsSelected()
    }

    private fun isContactsSelected(): Boolean {
        return if (contactsList.size != 0) {
            if (contactsList.size == selectedContactsList.size) {
                if (list.isNotEmpty()) {
                    list[0].isSelected = true
                }
                isApksSelected() && true
            } else {
                if (list.isNotEmpty()) {
                    list[0].isSelected = false
                }
                isApksSelected() && false
            }
        } else {
            isApksSelected() && false
        }
    }

    private fun isApksSelected(): Boolean {
        return if (apksList.size != 0) {
            if (apksList.size == selectedApksList.size) {
                if (list.isNotEmpty()) {
                    list[4].isSelected = true
                }
                isCalendarSelected() && true
            } else {
                if (list.isNotEmpty()) {
                    list[4].isSelected = false
                }
                isCalendarSelected() && false
            }
        } else {
            isCalendarSelected() && false
        }
    }

    private fun isCalendarSelected(): Boolean {
        return if (calendarEventsList.size != 0) {
            if (calendarEventsList.size == selectedCalendarEventsList.size) {
                if (list.isNotEmpty()) {
                    list[2].isSelected = true
                }
                isVideosSelected() && true
            } else {
                if (list.isNotEmpty()) {
                    list[2].isSelected = false
                }
                isVideosSelected() && false
            }
        } else {
            isVideosSelected() && false
        }
    }

    private fun isVideosSelected(): Boolean {
        return if (videosList.size != 0) {
            if (videosList.size == selectedVideosList.size) {
                if (list.isNotEmpty()) {
                    list[3].isSelected = true
                }
                isImagesListSelected() && true
            } else {
                if (list.isNotEmpty()) {
                    list[3].isSelected = false
                }
                isImagesListSelected() && false
            }
        } else {
            isImagesListSelected() && false
        }
    }

    private fun isImagesListSelected(): Boolean {
        return if (imagesList.size != 0) {
            if (imagesList.size == selectedImagesList.size) {
                if (list.isNotEmpty()) {
                    list[1].isSelected = true
                }
                isAudiosListSelected() && true
            } else {
                if (list.isNotEmpty()) {
                    list[1].isSelected = false
                }
                isAudiosListSelected() && false
            }
        } else {
            isAudiosListSelected() && false
        }
    }

    private fun isAudiosListSelected(): Boolean {
        return if (audiosList.size != 0) {
            if (audiosList.size == selectedAudiosList.size) {
                if (list.isNotEmpty()) {
                    list[5].isSelected = true
                }
                isDocsListSelected() && true
            } else {
                if (list.isNotEmpty()) {
                    list[5].isSelected = false
                }
                isDocsListSelected() && false
            }
        } else {
            isDocsListSelected() && false
        }
    }

    private fun isDocsListSelected(): Boolean {
        return if (docsList.size != 0) {
            if (docsList.size == selectedDocsList.size) {
                if (list.isNotEmpty()) {
                    list[6].isSelected = true
                }
                true
            } else {
                if (list.isNotEmpty()) {
                    list[6].isSelected = false
                }
                false
            }
        } else {
            false
        }
    }


    private fun startLoadingData() {
        binding.viewProgress.visibility=View.VISIBLE
        binding.viewMain.visibility=View.GONE

        if (ZakMyPermissions.hasCalendarPermission(this@ZakDataSelectionActivity)) {
            calendarEventsUtils?.loadData()
        } else {
            doneLoadingCalendarsEvents()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == ZakMyConstants.SPRC) {
            if (ZakMyPermissions.hasStoragePermission(this@ZakDataSelectionActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        ZakMyPermissions.showStorageRational(this@ZakDataSelectionActivity,
                            object : ZakMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        ZakMyConstants.SPRC
                                    )
                                }

                                override fun dismissed() {
                                    finish()
                                }

                            })
                    } else {
                        finish()
                    }
                } else {
                    finish()
                }
            }
        }else if(grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == ZakMyConstants.CONTACTS_PERMISSIONS_REQUEST_CODE){
            if (ZakMyPermissions.hasContactsPermission(this@ZakDataSelectionActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        ZakMyPermissions.showContactsRational(this@ZakDataSelectionActivity,
                            object : ZakMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        ZakMyConstants.SPRC
                                    )
                                }

                                override fun dismissed() {

                                }

                            })
                    }
                }
            }
        }else if(grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == ZakMyConstants.CALEDAR_PERMISSION_REQUEST_CODE){
            if (ZakMyPermissions.hasCalendarPermission(this@ZakDataSelectionActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        ZakMyPermissions.showCalendarRational(this@ZakDataSelectionActivity,
                            object : ZakMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        ZakMyConstants.SPRC
                                    )
                                }

                                override fun dismissed() {

                                }

                            })
                    }
                }
            }
        }
    }


    private fun initHandlers() {
        calendarEventsUtils = ZakCalendarEventsUtils(this@ZakDataSelectionActivity, null)
        HSContactsHandler = ZakContactsUtils(this@ZakDataSelectionActivity, null)
        HSImagesHandler = ZakImagesUtils(this@ZakDataSelectionActivity, null)
        HSVideosHandler = ZAKVideosUtils(this@ZakDataSelectionActivity, null)
        apksHandler = ZakAppsUtils(this@ZakDataSelectionActivity, null)
        docsHandler = ZakDocumentsUtils(this@ZakDataSelectionActivity)
        HSAudiosHanlder = ZakAudiosUtils(this@ZakDataSelectionActivity)
    }

    override fun doneLoadingContacts() {
        contactsList = ArrayList(ZakContactsUtils.contactsList)

        if (ZakMyPermissions.hasStoragePermission(this@ZakDataSelectionActivity)) {
            HSImagesHandler?.loadData()
        } else {
            doneLoadingPics()
        }
    }

    override fun doneLoadingCallLogs() {

    }

    override fun doneLoadingCalendarsEvents() {
        calendarEventsList = ArrayList(ZakCalendarEventsUtils.calendarEventsList)

        if (ZakMyPermissions.hasContactsPermission(this@ZakDataSelectionActivity)) {
            HSContactsHandler?.loadData()
        } else {
            doneLoadingContacts()
        }


    }

    override fun doneLoadingPics() {
        imagesList = ArrayList(ZakImagesUtils.imagesList)
        if (ZakMyPermissions.hasStoragePermission(this@ZakDataSelectionActivity)) {
            HSVideosHandler?.loadData()
        } else {
            doneLoadingVideos()
        }

    }

    override fun doneLoadingVideos() {
        videosList = ArrayList(ZAKVideosUtils.videosList)
        if (ZakMyPermissions.hasStoragePermission(this@ZakDataSelectionActivity)) {
            HSAudiosHanlder?.loadData()
        } else {
            doneLoadingAudios()
        }

    }

    override fun doneLoadingAudios() {
        audiosList = ArrayList(ZakAudiosUtils.audiosList)
        if (ZakMyPermissions.hasStoragePermission(this@ZakDataSelectionActivity)) {
            docsHandler?.loadData()
        } else {
            doneLoadingDocuments()
        }

    }

    override fun doneLoadingDocuments() {
        docsList = ArrayList(ZakDocumentsUtils.docsList)
        apksHandler?.loadData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ZakContactsUtils.contactsList.clear()
        ZakAppsUtils.apksList.clear()
        ZakCalendarEventsUtils.calendarEventsList.clear()
        ZakAppsUtils.apksList.clear()
        ZakImagesUtils.imagesList.clear()
        ZAKVideosUtils.videosList.clear()
        ZakAudiosUtils.audiosList.clear()
        ZakDocumentsUtils.docsList.clear()

        selectedApksList.clear()
        selectedCalendarEventsList.clear()
        selectedContactsList.clear()
        selectedVideosList.clear()
        selectedImagesList.clear()
        selectedAudiosList.clear()
        selectedDocsList.clear()

        apksList.clear()
        calendarEventsList.clear()
        contactsList.clear()
        videosList.clear()
        imagesList.clear()
        docsList.clear()
        audiosList.clear()
        ZakMyConstants.FILES_TO_SHARE.clear()
    }

    override fun doneLoadingApks() {
        apksList = ArrayList(ZakAppsUtils.apksList)
        //data has been loaded do somehing with it
        binding.viewProgress.visibility = View.GONE
        binding.viewMain.visibility = View.VISIBLE

        prepareSelectionDetails()

        HSAdapter = ZakAdapterSelection(this, list, object : SelectionCallBack {
            override fun itemSelectionChanged(fileType: String, isChecked: Boolean) {

                when (fileType) {
                    ZakMyConstants.FILE_TYPE_CONTACTS -> {
                        list[0].isSelected = isChecked
                        if (isChecked) {
                            selectedContactsList.clear()
                            selectedContactsList.addAll(contactsList)
                        } else {
                            selectedContactsList.clear()
                        }
                    }
                    ZakMyConstants.FILE_TYPE_PICS -> {
                        list[1].isSelected = isChecked
                        if (isChecked) {
                            selectedImagesList.clear()
                            selectedImagesList.addAll(imagesList)
                        } else {
                            selectedImagesList.clear()
                        }
                    }
                    ZakMyConstants.FILE_TYPE_CALENDAR -> {
                        list[2].isSelected = isChecked
                        if (isChecked) {
                            selectedCalendarEventsList.clear()
                            selectedCalendarEventsList.addAll(calendarEventsList)
                        } else {
                            selectedCalendarEventsList.clear()
                        }
                    }
                    ZakMyConstants.FILE_TYPE_VIDEOS -> {
                        list[3].isSelected = isChecked
                        if (isChecked) {
                            selectedVideosList.clear()
                            selectedVideosList.addAll(videosList)
                        } else {
                            selectedVideosList.clear()
                        }
                    }
                    ZakMyConstants.FILE_TYPE_APPS -> {
                        list[4].isSelected = isChecked
                        if (isChecked) {
                            selectedApksList.clear()
                            selectedApksList.addAll(apksList)
                        } else {
                            selectedApksList.clear()
                        }
                    }
                    ZakMyConstants.FILE_TYPE_AUDIOS -> {
                        list[5].isSelected = isChecked
                        if (isChecked) {
                            selectedAudiosList.clear()
                            selectedAudiosList.addAll(audiosList)
                        } else {
                            selectedAudiosList.clear()
                        }
                    }
                    ZakMyConstants.FILE_TYPE_DOCS -> {
                        list[6].isSelected = isChecked
                        if (isChecked) {
                            selectedDocsList.clear()
                            selectedDocsList.addAll(docsList)
                        } else {
                            selectedDocsList.clear()
                        }
                    }
                }
                manageSelectAllSelection()
                HSAdapter?.notifyDataSetChanged()

            }

            override fun openItem(fileType: String, isSelected: Boolean) {

                if (fileType == ZakMyConstants.FILE_TYPE_CONTACTS &&
                    ZakMyPermissions.hasContactsPermission(this@ZakDataSelectionActivity) &&
                    contactsList.isNotEmpty()
                ) {
                    openActivity(isSelected,fileType)
                    return
                } else if(fileType == ZakMyConstants.FILE_TYPE_CONTACTS) {
                    ZakMyPermissions.showContactsExplanation(this@ZakDataSelectionActivity,
                        object : ZakMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                ZakMyPermissions.requestContactsPermission(this@ZakDataSelectionActivity)
                                return
                            }

                            override fun denyPermission() {
                                return
                            }

                        })
                    return
                }

                if (fileType == ZakMyConstants.FILE_TYPE_CALENDAR&&
                    ZakMyPermissions.hasCalendarPermission(this@ZakDataSelectionActivity)) {
                    return
                }else  if (fileType == ZakMyConstants.FILE_TYPE_CALENDAR){
                    ZakMyPermissions.showCalendarExplanation(this@ZakDataSelectionActivity,object:ZakMyPermissions.ExplanationCallBack{
                        override fun requestPermission() {
                            ZakMyPermissions.requestCalendarPermission(this@ZakDataSelectionActivity)
                            return
                        }

                        override fun denyPermission() {
                            return
                        }

                    })
                    return
                }

               if(fileType==ZakMyConstants.FILE_TYPE_APPS){
                   openActivity(isSelected,fileType)
               }else{
                   openViewAll(isSelected,fileType)
               }

            }

        })

        binding.recyclerView.layoutManager =GridLayoutManager(this,2)
            binding.recyclerView.adapter = HSAdapter
        binding.btnDone.setOnClickListener {

            //show dialogue
            if (selectedImagesList.isNotEmpty() || selectedVideosList.isNotEmpty() || selectedApksList.isNotEmpty() ||
                selectedContactsList.isNotEmpty() || selectedCalendarEventsList.isNotEmpty()
                || selectedDocsList.isNotEmpty() || selectedAudiosList.isNotEmpty()
            ) {
                binding.textView7.text =
                    applicationContext.resources.getString(R.string.preparing_data)
                binding.viewProgress.visibility = View.VISIBLE
                binding.viewMain.visibility = View.GONE
                prepareTransferDetails()
            }

        }
    }

    fun openActivity(isSelected: Boolean, fileType: String) {
        val intent = Intent(this@ZakDataSelectionActivity, ZakShowItemsForSelection::class.java)
        intent.putExtra(ZakMyConstants.FILE_TYPE, fileType)
        intent.putExtra(ZakMyConstants.IS_SELECTED, isSelected)
        startActivity(intent)
    }
    fun openViewAll(isSelected: Boolean, fileType: String) {
        val intent = Intent(this@ZakDataSelectionActivity, ZakActivityViewAll::class.java)
        intent.putExtra(ZakMyConstants.FILE_TYPE, fileType)
        intent.putExtra(ZakMyConstants.IS_SELECTED, isSelected)
        startActivity(intent)
    }

    private fun prepareTransferDetails() {

        populateFileSharingList()

        //       binding.tvDetails.text = """Sending to ${HSMyConstants.OPONENT_NAME}"""

    }

    private fun populateFileSharingList() {
        ZakMyConstants.FILES_TO_SHARE.clear()
        if (ZakDataSelectionActivity.selectedCalendarEventsList.isNotEmpty()) {
            CalendarFilePopulatingTask(object : AsyncTaskCallBack {
                override fun taskCompleted() {
                    val pathToSave = "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${ZakMyConstants.EventsFileName}"
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + pathToSave
                    val file =
                        ZakFileSharingModel(path, ZakMyConstants.FILE_TYPE_CALENDAR, pathToSave)
                    ZakMyConstants.FILES_TO_SHARE.add(file)

                    populateContactsFile()
                }

            }).execute()
        } else {
            populateContactsFile()
        }

    }

    private fun populateContactsFile() {
        if (ZakDataSelectionActivity.selectedContactsList.isNotEmpty()) {
            ContactsFilePopulatingTask(object : AsyncTaskCallBack {
                override fun taskCompleted() {
                    val pathToSave = "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${ZakMyConstants.ContactsFileName}"
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + pathToSave
                    val file =
                        ZakFileSharingModel(path, ZakMyConstants.FILE_TYPE_CONTACTS, pathToSave)
                    ZakMyConstants.FILES_TO_SHARE.add(file)
                    populateRemainingFiles()
                }
            }).execute()
        } else {
            populateRemainingFiles()
        }
    }

    private fun populateRemainingFiles() {
        OtherFilesPopulationTask(object : AsyncTaskCallBack {
            override fun taskCompleted() {
                val intent = Intent(this@ZakDataSelectionActivity, ZakWifiP2PJoinGroupAndSend::class.java)
                startActivityForResult(intent, ZakMyConstants.SELCTION_ACTIVITY)
            }

        }).execute()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ZakMyConstants.SELCTION_ACTIVITY) {
            binding.viewMain.visibility = View.VISIBLE
            binding.viewProgress.visibility = View.GONE
            finish()
        }else if (resultCode == ZakMyConstants.RESTART_JOIN) {
            startActivityForResult(
                Intent(this@ZakDataSelectionActivity, ZakWifiP2PJoinGroupAndSend::class.java),
                ZakMyConstants.FINISH_ACTIVITY_CODE
            )
        }  else if (requestCode == ZakMyConstants.SPRC) {
            if (ZakMyPermissions.hasStoragePermission(this@ZakDataSelectionActivity)) {
                startLoadingData()
            } else {
                finish()
            }
        }else if(requestCode==ZakMyConstants.CALEDAR_PERMISSION_REQUEST_CODE){
            if (ZakMyPermissions.hasCalendarPermission(this@ZakDataSelectionActivity)) {
                startLoadingData()
            }
        }else if(requestCode==ZakMyConstants.CONTACTS_PERMISSIONS_REQUEST_CODE){
            if(ZakMyPermissions.hasContactsPermission(this@ZakDataSelectionActivity)){
                startLoadingData()
            }
        }else{
            binding.viewMain.visibility = View.VISIBLE
            binding.viewProgress.visibility = View.GONE
        }
    }

    private fun prepareSelectionDetails() {
        list.clear()
        list.add(
            ZAKFilesToShareModel(
                this@ZakDataSelectionActivity,
                ZakMyConstants.FILE_TYPE_CONTACTS,
                contactsList.size,
                0
            )
        )
        list.add(
            ZAKFilesToShareModel(
                this@ZakDataSelectionActivity,
                ZakMyConstants.FILE_TYPE_PICS,
                imagesList.size,
                HSImagesHandler?.getJustSize()
            )
        )
        list.add(
            ZAKFilesToShareModel(
                this@ZakDataSelectionActivity,
                ZakMyConstants.FILE_TYPE_CALENDAR,
                calendarEventsList.size,
                0
            )
        )
        list.add(
            ZAKFilesToShareModel(
                this@ZakDataSelectionActivity,
                ZakMyConstants.FILE_TYPE_VIDEOS,
                videosList.size,
                HSVideosHandler?.getJustSize()
            )
        )
        list.add(
            ZAKFilesToShareModel(
                this@ZakDataSelectionActivity,
                ZakMyConstants.FILE_TYPE_APPS,
                apksList.size,
                apksHandler?.getJustSzie()
            )
        )
        list.add(
            ZAKFilesToShareModel(
                this@ZakDataSelectionActivity,
                ZakMyConstants.FILE_TYPE_AUDIOS,
                audiosList.size,
                HSAudiosHanlder?.getJustSize()
            )
        )
        list.add(
            ZAKFilesToShareModel(
                this@ZakDataSelectionActivity,
                ZakMyConstants.FILE_TYPE_DOCS,
                docsList.size,
                docsHandler?.getJustSize()
            )
        )
        for(i in docsList){
            if(list[6].innerList.size<3){
                list[6].innerList.add(i)
            }else{
                break
            }
        }
        for(i in audiosList){
            if(list[5].innerList.size<3){
                list[5].innerList.add(i)
            }else{
                break
            }
        }
        for(i in videosList){
            if(list[3].innerList.size<3){
                list[3].innerList.add(i)
            }else{
                break
            }
        }
        list[1].selectionDetailsTotal = """/${imagesList.size})"""
        for(i in imagesList){
            if(list[1].innerList.size<3){
                list[1].innerList.add(i)
            }else{
                break
            }
        }
    }


    interface SelectionCallBack {
        fun itemSelectionChanged(fileType: String, isChecked: Boolean)
        fun openItem(fileType: String, isSelected: Boolean)
    }


    inner class CalendarFilePopulatingTask(var callBack: AsyncTaskCallBack) :
        AsyncTask<Void, Void, Void>() {
        private val CSV_HEADER =
            "title,organizer,location,all_day,start,end,timezone,description,reminder,duration"

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: Void?): Void? {
            var fileWriter: FileWriter? = null
            try {
                val rootPath = Environment.getExternalStorageDirectory().absolutePath + "/${
                    applicationContext.resources.getString(R.string.app_name)
                }"
                val rootFile = File(rootPath)
                if (!rootFile.exists()) {
                    rootFile.mkdirs()
                }
                val file = File(
                    Environment.getExternalStorageDirectory().absolutePath + "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${ZakMyConstants.EventsFileName}"
                )
                fileWriter = FileWriter(file)
                fileWriter.append(CSV_HEADER)
                fileWriter.append('\n')

                for (event in ZakDataSelectionActivity.selectedCalendarEventsList) {
                    fileWriter.append(event.title)
                    fileWriter.append(',')
                    fileWriter.append(event.organizer)
                    fileWriter.append(',')
                    fileWriter.append(event.location)
                    fileWriter.append(',')
                    fileWriter.append(event.all_day)
                    fileWriter.append(',')
                    fileWriter.append(event.start)
                    fileWriter.append(',')
                    fileWriter.append(event.end)
                    fileWriter.append(',')
                    fileWriter.append(event.timezone)
                    fileWriter.append(',')
                    fileWriter.append(event.description)
                    fileWriter.append(',')
                    fileWriter.append(event.reminder)
                    fileWriter.append(',')
                    fileWriter.append(event.duration)
                    fileWriter.append('\n')
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fileWriter!!.flush()
                    fileWriter.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            callBack.taskCompleted()
        }

    }

    inner class ContactsFilePopulatingTask(var callBack: AsyncTaskCallBack) :
        AsyncTask<Void, Void, Void>() {
        private val CSV_HEADER = "name,phoneNumber"

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: Void?): Void? {
            var fileWriter: FileWriter? = null
            try {
                val rootPath = Environment.getExternalStorageDirectory().absolutePath + "/${
                    applicationContext.resources.getString(R.string.app_name)
                }"
                val rootFile = File(rootPath)
                if (!rootFile.exists()) {
                    rootFile.mkdirs()
                }
                val file = File(
                    Environment.getExternalStorageDirectory().absolutePath + "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${ZakMyConstants.ContactsFileName}"
                )
                fileWriter = FileWriter(file)
                fileWriter.append(CSV_HEADER)
                fileWriter.append('\n')

                for (contact in ZakDataSelectionActivity.selectedContactsList) {
                    fileWriter.append(contact.name)
                    fileWriter.append(',')
                    fileWriter.append(contact.phoneNumber)
                    fileWriter.append(',')
                    fileWriter.append('\n')
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fileWriter!!.flush()
                    fileWriter.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            callBack.taskCompleted()
        }

    }

    inner class OtherFilesPopulationTask(var callBack: AsyncTaskCallBack) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            if (ZakDataSelectionActivity.selectedApksList.isNotEmpty()) {
                val list = ArrayList(selectedApksList)
                list.forEach {
                    val f = ZakFileSharingModel(
                        it.srcDir,
                        ZakMyConstants.FILE_TYPE_APPS,
                        fileName = it.apkName,
                        pathToSave = ""
                    )
                    ZakMyConstants.FILES_TO_SHARE.add(f)
                }
            }
            if (ZakDataSelectionActivity.selectedVideosList.isNotEmpty()) {
                val list = ArrayList(selectedVideosList)
                list.forEach {
                    ZakMyConstants.FILES_TO_SHARE.add(
                        ZakFileSharingModel(
                            it.path,
                            ZakMyConstants.FILE_TYPE_VIDEOS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedImagesList.isNotEmpty()) {
                val list = ArrayList(selectedImagesList)
                list.forEach {
                    ZakMyConstants.FILES_TO_SHARE.add(
                        ZakFileSharingModel(
                            it.path,
                            ZakMyConstants.FILE_TYPE_PICS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedAudiosList.isNotEmpty()) {
                val list = ArrayList(selectedAudiosList)
                list.forEach {
                    ZakMyConstants.FILES_TO_SHARE.add(
                        ZakFileSharingModel(
                            it.filePath,
                            ZakMyConstants.FILE_TYPE_AUDIOS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedDocsList.isNotEmpty()) {

                val list = ArrayList(selectedDocsList)
                list.forEach {
                    ZakMyConstants.FILES_TO_SHARE.add(
                        ZakFileSharingModel(
                            it.filePath,
                            ZakMyConstants.FILE_TYPE_DOCS,
                            it.getActualPath()
                        )
                    )
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            callBack.taskCompleted()
        }

    }

    interface AsyncTaskCallBack {
        fun taskCompleted()
    }
/*    fun loadFbBannerAdd() {

        val adView = AdView(
            this@HSDataSelectionActivity,
            this@HSDataSelectionActivity.resources.getString(R.string.banner_add),
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