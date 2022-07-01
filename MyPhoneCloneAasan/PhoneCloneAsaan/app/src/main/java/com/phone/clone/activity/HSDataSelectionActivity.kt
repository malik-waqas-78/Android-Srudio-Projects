package com.phone.clone.activity

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
import androidx.recyclerview.widget.LinearLayoutManager


import com.phone.clone.R
import com.phone.clone.adapters.HSAdapterSelection

import com.phone.clone.constants.HSMyConstants
import com.phone.clone.databinding.ActivitySelectionBinding
import com.phone.clone.datautils.*
import com.phone.clone.interfaces.HSUtilsCallBacks
import com.phone.clone.modelclasses.*
import com.phone.clone.utills.HSMyPermissions
import java.io.File
import java.io.FileWriter
import java.io.IOException

class HSDataSelectionActivity : AppCompatActivity(), HSUtilsCallBacks {
    lateinit var binding: ActivitySelectionBinding

    companion object {

        var apksList = ArrayList<HSAppsModel>()
        var calendarEventsList = ArrayList<HSCalendarEventsModel>()
        var contactsList = ArrayList<HSContactsModel>()
        var videosList = ArrayList<HSMediaModelClass>()
        var imagesList = ArrayList<HSMediaModelClass>()
        var docsList = ArrayList<HSFileSharingModel>()
        var audiosList = ArrayList<HSFileSharingModel>()


        var selectedApksList = ArrayList<HSAppsModel>()
        var selectedCalendarEventsList = ArrayList<HSCalendarEventsModel>()
        var selectedContactsList = ArrayList<HSContactsModel>()
        var selectedVideosList = ArrayList<HSMediaModelClass>()
        var selectedImagesList = ArrayList<HSMediaModelClass>()
        var selectedDocsList = ArrayList<HSFileSharingModel>()
        var selectedAudiosList = ArrayList<HSFileSharingModel>()

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


    var list = ArrayList<HSFilesToShareModel>()

    var HSContactsHandler: HSContactsUtils? = null
    var HSImagesHandler: HSImagesUtils? = null
    var HSVideosHandler: HSVideosUtils? = null
    var calendarEventsUtils: HSCalendarEventsUtils? = null
    var apksHandler: HSAppsUtils? = null
    var docsHandler: HSDocumentsUtils? = null
    var HSAudiosHanlder: HSAudiosUtils? = null
    var HSAdapter: HSAdapterSelection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /* loadFbBannerAdd()
        HSNativeAdHelper.showAd(this@HSDataSelectionActivity,binding.nativeAdContainer)*/
        HSMyConstants.FILES_TO_SHARE.clear()

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
        if (HSMyPermissions.hasStoragePermission(this@HSDataSelectionActivity)) {
            startLoadingData()
        } else {
            HSMyPermissions.showStorageExplanation(this@HSDataSelectionActivity,
                object : HSMyPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        HSMyPermissions.requestStoragePermission(this@HSDataSelectionActivity)
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

        if (HSMyPermissions.hasCalendarPermission(this@HSDataSelectionActivity)) {
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
        if (grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == HSMyConstants.SPRC) {
            if (HSMyPermissions.hasStoragePermission(this@HSDataSelectionActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        HSMyPermissions.showStorageRational(this@HSDataSelectionActivity,
                            object : HSMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        HSMyConstants.SPRC
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
        }else if(grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == HSMyConstants.CONTACTS_PERMISSIONS_REQUEST_CODE){
            if (HSMyPermissions.hasContactsPermission(this@HSDataSelectionActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        HSMyPermissions.showContactsRational(this@HSDataSelectionActivity,
                            object : HSMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        HSMyConstants.SPRC
                                    )
                                }

                                override fun dismissed() {

                                }

                            })
                    }
                }
            }
        }else if(grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == HSMyConstants.CALEDAR_PERMISSION_REQUEST_CODE){
            if (HSMyPermissions.hasCalendarPermission(this@HSDataSelectionActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        HSMyPermissions.showCalendarRational(this@HSDataSelectionActivity,
                            object : HSMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        HSMyConstants.SPRC
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
        calendarEventsUtils = HSCalendarEventsUtils(this@HSDataSelectionActivity, null)
        HSContactsHandler = HSContactsUtils(this@HSDataSelectionActivity, null)
        HSImagesHandler = HSImagesUtils(this@HSDataSelectionActivity, null)
        HSVideosHandler = HSVideosUtils(this@HSDataSelectionActivity, null)
        apksHandler = HSAppsUtils(this@HSDataSelectionActivity, null)
        docsHandler = HSDocumentsUtils(this@HSDataSelectionActivity)
        HSAudiosHanlder = HSAudiosUtils(this@HSDataSelectionActivity)
    }

    override fun doneLoadingContacts() {
        contactsList = ArrayList(HSContactsUtils.contactsList)

        if (HSMyPermissions.hasStoragePermission(this@HSDataSelectionActivity)) {
            HSImagesHandler?.loadData()
        } else {
            doneLoadingPics()
        }
    }

    override fun doneLoadingCallLogs() {

    }

    override fun doneLoadingCalendarsEvents() {
        calendarEventsList = ArrayList(HSCalendarEventsUtils.calendarEventsList)

        if (HSMyPermissions.hasContactsPermission(this@HSDataSelectionActivity)) {
            HSContactsHandler?.loadData()
        } else {
            doneLoadingContacts()
        }


    }

    override fun doneLoadingPics() {
        imagesList = ArrayList(HSImagesUtils.imagesList)
        if (HSMyPermissions.hasStoragePermission(this@HSDataSelectionActivity)) {
            HSVideosHandler?.loadData()
        } else {
            doneLoadingVideos()
        }

    }

    override fun doneLoadingVideos() {
        videosList = ArrayList(HSVideosUtils.videosList)
        if (HSMyPermissions.hasStoragePermission(this@HSDataSelectionActivity)) {
            HSAudiosHanlder?.loadData()
        } else {
            doneLoadingAudios()
        }

    }

    override fun doneLoadingAudios() {
        audiosList = ArrayList(HSAudiosUtils.audiosList)
        if (HSMyPermissions.hasStoragePermission(this@HSDataSelectionActivity)) {
            docsHandler?.loadData()
        } else {
            doneLoadingDocuments()
        }

    }

    override fun doneLoadingDocuments() {
        docsList = ArrayList(HSDocumentsUtils.docsList)
        apksHandler?.loadData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        HSContactsUtils.contactsList.clear()
        HSAppsUtils.apksList.clear()
        HSCalendarEventsUtils.calendarEventsList.clear()
        HSAppsUtils.apksList.clear()
        HSImagesUtils.imagesList.clear()
        HSVideosUtils.videosList.clear()
        HSAudiosUtils.audiosList.clear()
        HSDocumentsUtils.docsList.clear()

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
        HSMyConstants.FILES_TO_SHARE.clear()
    }

    override fun doneLoadingApks() {
        apksList = ArrayList(HSAppsUtils.apksList)
        //data has been loaded do somehing with it
        binding.viewProgress.visibility = View.GONE
        binding.viewMain.visibility = View.VISIBLE

        prepareSelectionDetails()

        HSAdapter = HSAdapterSelection(this, list, object : SelectionCallBack {
            override fun itemSelectionChanged(fileType: String, isChecked: Boolean) {

                when (fileType) {
                    HSMyConstants.FILE_TYPE_CONTACTS -> {
                        list[0].isSelected = isChecked
                        if (isChecked) {
                            selectedContactsList.clear()
                            selectedContactsList.addAll(contactsList)
                        } else {
                            selectedContactsList.clear()
                        }
                    }
                    HSMyConstants.FILE_TYPE_PICS -> {
                        list[1].isSelected = isChecked
                        if (isChecked) {
                            selectedImagesList.clear()
                            selectedImagesList.addAll(imagesList)
                        } else {
                            selectedImagesList.clear()
                        }
                    }
                    HSMyConstants.FILE_TYPE_CALENDAR -> {
                        list[2].isSelected = isChecked
                        if (isChecked) {
                            selectedCalendarEventsList.clear()
                            selectedCalendarEventsList.addAll(calendarEventsList)
                        } else {
                            selectedCalendarEventsList.clear()
                        }
                    }
                    HSMyConstants.FILE_TYPE_VIDEOS -> {
                        list[3].isSelected = isChecked
                        if (isChecked) {
                            selectedVideosList.clear()
                            selectedVideosList.addAll(videosList)
                        } else {
                            selectedVideosList.clear()
                        }
                    }
                    HSMyConstants.FILE_TYPE_APPS -> {
                        list[4].isSelected = isChecked
                        if (isChecked) {
                            selectedApksList.clear()
                            selectedApksList.addAll(apksList)
                        } else {
                            selectedApksList.clear()
                        }
                    }
                    HSMyConstants.FILE_TYPE_AUDIOS -> {
                        list[5].isSelected = isChecked
                        if (isChecked) {
                            selectedAudiosList.clear()
                            selectedAudiosList.addAll(audiosList)
                        } else {
                            selectedAudiosList.clear()
                        }
                    }
                    HSMyConstants.FILE_TYPE_DOCS -> {
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

                if (fileType == HSMyConstants.FILE_TYPE_CONTACTS &&
                    HSMyPermissions.hasContactsPermission(this@HSDataSelectionActivity) &&
                    contactsList.isNotEmpty()
                ) {
                    openActivity(isSelected,fileType)
                    return
                } else if(fileType == HSMyConstants.FILE_TYPE_CONTACTS) {
                    HSMyPermissions.showContactsExplanation(this@HSDataSelectionActivity,
                        object : HSMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                HSMyPermissions.requestContactsPermission(this@HSDataSelectionActivity)
                                return
                            }

                            override fun denyPermission() {
                                return
                            }

                        })
                    return
                }

                if (fileType == HSMyConstants.FILE_TYPE_CALENDAR&&
                    HSMyPermissions.hasCalendarPermission(this@HSDataSelectionActivity)) {
                    return
                }else  if (fileType == HSMyConstants.FILE_TYPE_CALENDAR){
                    HSMyPermissions.showCalendarExplanation(this@HSDataSelectionActivity,object:HSMyPermissions.ExplanationCallBack{
                        override fun requestPermission() {
                            HSMyPermissions.requestCalendarPermission(this@HSDataSelectionActivity)
                            return
                        }

                        override fun denyPermission() {
                            return
                        }

                    })
                    return
                }

               if(fileType==HSMyConstants.FILE_TYPE_APPS){
                   openActivity(isSelected,fileType)
               }else{
                   openViewAll(isSelected,fileType)
               }

            }

        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
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
        val intent = Intent(this@HSDataSelectionActivity, HSShowItemsForSelection::class.java)
        intent.putExtra(HSMyConstants.FILE_TYPE, fileType)
        intent.putExtra(HSMyConstants.IS_SELECTED, isSelected)
        startActivity(intent)
    }
    fun openViewAll(isSelected: Boolean, fileType: String) {
        val intent = Intent(this@HSDataSelectionActivity, HSActivityViewAll::class.java)
        intent.putExtra(HSMyConstants.FILE_TYPE, fileType)
        intent.putExtra(HSMyConstants.IS_SELECTED, isSelected)
        startActivity(intent)
    }

    private fun prepareTransferDetails() {

        populateFileSharingList()

        //       binding.tvDetails.text = """Sending to ${HSMyConstants.OPONENT_NAME}"""

    }

    private fun populateFileSharingList() {
        HSMyConstants.FILES_TO_SHARE.clear()
        if (HSDataSelectionActivity.selectedCalendarEventsList.isNotEmpty()) {
            CalendarFilePopulatingTask(object : AsyncTaskCallBack {
                override fun taskCompleted() {
                    val pathToSave = "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${HSMyConstants.EventsFileName}"
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + pathToSave
                    val file =
                        HSFileSharingModel(path, HSMyConstants.FILE_TYPE_CALENDAR, pathToSave)
                    HSMyConstants.FILES_TO_SHARE.add(file)

                    populateContactsFile()
                }

            }).execute()
        } else {
            populateContactsFile()
        }

    }

    private fun populateContactsFile() {
        if (HSDataSelectionActivity.selectedContactsList.isNotEmpty()) {
            ContactsFilePopulatingTask(object : AsyncTaskCallBack {
                override fun taskCompleted() {
                    val pathToSave = "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${HSMyConstants.ContactsFileName}"
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + pathToSave
                    val file =
                        HSFileSharingModel(path, HSMyConstants.FILE_TYPE_CONTACTS, pathToSave)
                    HSMyConstants.FILES_TO_SHARE.add(file)
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
                val intent = Intent(this@HSDataSelectionActivity, HSHSWifiP2PJoinGroupAndSend::class.java)
                startActivityForResult(intent, HSMyConstants.SELCTION_ACTIVITY)
            }

        }).execute()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == HSMyConstants.SELCTION_ACTIVITY) {
            binding.viewMain.visibility = View.VISIBLE
            binding.viewProgress.visibility = View.GONE
            finish()
        }else if (resultCode == HSMyConstants.RESTART_JOIN) {
            startActivityForResult(
                Intent(this@HSDataSelectionActivity, HSHSWifiP2PJoinGroupAndSend::class.java),
                HSMyConstants.FINISH_ACTIVITY_CODE
            )
        }  else if (requestCode == HSMyConstants.SPRC) {
            if (HSMyPermissions.hasStoragePermission(this@HSDataSelectionActivity)) {
                startLoadingData()
            } else {
                finish()
            }
        }else if(requestCode==HSMyConstants.CALEDAR_PERMISSION_REQUEST_CODE){
            if (HSMyPermissions.hasCalendarPermission(this@HSDataSelectionActivity)) {
                startLoadingData()
            }
        }else if(requestCode==HSMyConstants.CONTACTS_PERMISSIONS_REQUEST_CODE){
            if(HSMyPermissions.hasContactsPermission(this@HSDataSelectionActivity)){
                startLoadingData()
            }
        }
    }

    private fun prepareSelectionDetails() {
        list.clear()
        list.add(
            HSFilesToShareModel(
                this@HSDataSelectionActivity,
                HSMyConstants.FILE_TYPE_CONTACTS,
                contactsList.size,
                0
            )
        )
        list.add(
            HSFilesToShareModel(
                this@HSDataSelectionActivity,
                HSMyConstants.FILE_TYPE_PICS,
                imagesList.size,
                HSImagesHandler?.getJustSize()
            )
        )
        list.add(
            HSFilesToShareModel(
                this@HSDataSelectionActivity,
                HSMyConstants.FILE_TYPE_CALENDAR,
                calendarEventsList.size,
                0
            )
        )
        list.add(
            HSFilesToShareModel(
                this@HSDataSelectionActivity,
                HSMyConstants.FILE_TYPE_VIDEOS,
                videosList.size,
                HSVideosHandler?.getJustSize()
            )
        )
        list.add(
            HSFilesToShareModel(
                this@HSDataSelectionActivity,
                HSMyConstants.FILE_TYPE_APPS,
                apksList.size,
                apksHandler?.getJustSzie()
            )
        )
        list.add(
            HSFilesToShareModel(
                this@HSDataSelectionActivity,
                HSMyConstants.FILE_TYPE_AUDIOS,
                audiosList.size,
                HSAudiosHanlder?.getJustSize()
            )
        )
        list.add(
            HSFilesToShareModel(
                this@HSDataSelectionActivity,
                HSMyConstants.FILE_TYPE_DOCS,
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
                    }/${HSMyConstants.EventsFileName}"
                )
                fileWriter = FileWriter(file)
                fileWriter.append(CSV_HEADER)
                fileWriter.append('\n')

                for (event in HSDataSelectionActivity.selectedCalendarEventsList) {
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
                    }/${HSMyConstants.ContactsFileName}"
                )
                fileWriter = FileWriter(file)
                fileWriter.append(CSV_HEADER)
                fileWriter.append('\n')

                for (contact in HSDataSelectionActivity.selectedContactsList) {
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
            if (HSDataSelectionActivity.selectedApksList.isNotEmpty()) {
                val list = ArrayList(selectedApksList)
                list.forEach {
                    val f = HSFileSharingModel(
                        it.srcDir,
                        HSMyConstants.FILE_TYPE_APPS,
                        fileName = it.apkName,
                        pathToSave = ""
                    )
                    HSMyConstants.FILES_TO_SHARE.add(f)
                }
            }
            if (HSDataSelectionActivity.selectedVideosList.isNotEmpty()) {
                val list = ArrayList(selectedVideosList)
                list.forEach {
                    HSMyConstants.FILES_TO_SHARE.add(
                        HSFileSharingModel(
                            it.path,
                            HSMyConstants.FILE_TYPE_VIDEOS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedImagesList.isNotEmpty()) {
                val list = ArrayList(selectedImagesList)
                list.forEach {
                    HSMyConstants.FILES_TO_SHARE.add(
                        HSFileSharingModel(
                            it.path,
                            HSMyConstants.FILE_TYPE_PICS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedAudiosList.isNotEmpty()) {
                val list = ArrayList(selectedAudiosList)
                list.forEach {
                    HSMyConstants.FILES_TO_SHARE.add(
                        HSFileSharingModel(
                            it.filePath,
                            HSMyConstants.FILE_TYPE_AUDIOS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedDocsList.isNotEmpty()) {

                val list = ArrayList(selectedDocsList)
                list.forEach {
                    HSMyConstants.FILES_TO_SHARE.add(
                        HSFileSharingModel(
                            it.filePath,
                            HSMyConstants.FILE_TYPE_DOCS,
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