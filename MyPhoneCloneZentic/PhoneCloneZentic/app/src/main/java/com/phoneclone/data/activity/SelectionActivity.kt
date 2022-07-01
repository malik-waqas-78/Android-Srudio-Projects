package com.phoneclone.data.activity

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
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.facebook.ads.*
import com.phoneclone.data.R
import com.phoneclone.data.adapters.AdapterSelection
import com.phoneclone.data.ads.NativeAdHelper
import com.phoneclone.data.ads.isAppInstalledFromPlay
import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.databinding.ActivitySelectionBinding
import com.phoneclone.data.datautils.*
import com.phoneclone.data.interfaces.UtilsCallBacks
import com.phoneclone.data.modelclasses.*
import com.phoneclone.data.utills.MyPermissions
import java.io.File
import java.io.FileWriter
import java.io.IOException

class  SelectionActivity : AppCompatActivity(), UtilsCallBacks {
    lateinit var binding: ActivitySelectionBinding

    companion object {

        var apksList = ArrayList<AppsModel>()
        var calendarEventsList = ArrayList<CalendarEventsModel>()
        var contactsList = ArrayList<ContactsModel>()
        var videosList = ArrayList<MediaModelClass>()
        var imagesList = ArrayList<MediaModelClass>()
        var docsList = ArrayList<FileSharingModel>()
        var audiosList = ArrayList<FileSharingModel>()


        var selectedApksList = ArrayList<AppsModel>()
        var selectedCalendarEventsList = ArrayList<CalendarEventsModel>()
        var selectedContactsList = ArrayList<ContactsModel>()
        var selectedVideosList = ArrayList<MediaModelClass>()
        var selectedImagesList = ArrayList<MediaModelClass>()
        var selectedDocsList = ArrayList<FileSharingModel>()
        var selectedAudiosList = ArrayList<FileSharingModel>()

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
//            list[0].selectionDetails="""(${selectedContactsList.size}/${contactsList.size})"""
                list[0].selectionDetails = """(${selectedContactsList.size}"""
                list[0].selectionDetailsTotal = """/${contactsList.size})"""

//            list[1].selectionDetails="""(${selectedImagesList.size}/${imagesList.size})"""
                list[1].selectionDetails = """(${selectedImagesList.size}"""
                list[1].selectionDetailsTotal = """/${imagesList.size})"""


//            list[2].selectionDetails="""(${selectedCalendarEventsList.size}/${calendarEventsList.size})"""
                list[2].selectionDetails = """(${selectedCalendarEventsList.size}"""
                list[2].selectionDetailsTotal = """/${calendarEventsList.size})"""


//            list[3].selectionDetails="""(${selectedVideosList.size}/${videosList.size})"""
                list[3].selectionDetails = """(${selectedVideosList.size}"""
                list[3].selectionDetailsTotal = """/${videosList.size})"""

//            list[4].selectionDetails="""(${selectedApksList.size}/${apksList.size})"""
                list[4].selectionDetails = """(${selectedApksList.size}"""
                list[4].selectionDetailsTotal = """/${apksList.size})"""

//            list[5].selectionDetails="""(${selectedAudiosList.size}/${audiosList.size})"""
                list[5].selectionDetails = """(${selectedAudiosList.size}"""
                list[5].selectionDetailsTotal = """/${audiosList.size})"""

//            list[6].selectionDetails="""(${selectedDocsList.size}/${docsList.size})"""
                list[6].selectionDetails = """(${selectedDocsList.size}"""
                list[6].selectionDetailsTotal = """/${docsList.size})"""


//                list[0].selectionDetails="""(${selectedContactsList.size}/${contactsList.size})"""
//                list[1].selectionDetails="""(${selectedImagesList.size}/${imagesList.size})"""
//                list[2].selectionDetails="""(${selectedCalendarEventsList.size}/${calendarEventsList.size})"""
//                list[3].selectionDetails="""(${selectedVideosList.size}/${videosList.size})"""
//                list[4].selectionDetails="""(${selectedApksList.size}/${apksList.size})"""
//                list[5].selectionDetails="""(${selectedAudiosList.size}/${audiosList.size})"""
//                list[6].selectionDetails="""(${selectedDocsList.size}/${docsList.size})"""
            }
            adapter?.notifyDataSetChanged()
        }


    var list = ArrayList<FilesToShareModel>()

    var contactsHandler: ContactsUtils? = null
    var imagesHandler: ImagesUtils? = null
    var videosHandler: VideosUtils? = null
    var calendarEventsUtils: CalendarEventsUtils? = null
    var apksHandler: AppsUtils? = null
    var docsHandler: DocumentsUtils? = null
    var audiosHanlder: AudiosUtils? = null
    var adapter: AdapterSelection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isAppInstalledFromPlay(this)){
            loadFbBannerAdd()
        }
        NativeAdHelper.showAdmobNativeAd(this@SelectionActivity,binding.adFrame)
        MyConstants.FILES_TO_SHARE.clear()

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
        if (MyPermissions.hasStoragePermission(this@SelectionActivity)) {
            startLoadingData()
        } else {
            MyPermissions.showStorageExplanation(this@SelectionActivity,
                object : MyPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        MyPermissions.requestStoragePermission(this@SelectionActivity)
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
//            list[0].selectionDetails="""(${selectedContactsList.size}/${contactsList.size})"""
            list[0].selectionDetails = """(${selectedContactsList.size}"""
            list[0].selectionDetailsTotal = """/${contactsList.size})"""

//            list[1].selectionDetails="""(${selectedImagesList.size}/${imagesList.size})"""
            list[1].selectionDetails = """(${selectedImagesList.size}"""
            list[1].selectionDetailsTotal = """/${imagesList.size})"""


//            list[2].selectionDetails="""(${selectedCalendarEventsList.size}/${calendarEventsList.size})"""
            list[2].selectionDetails = """(${selectedCalendarEventsList.size}"""
            list[2].selectionDetailsTotal = """/${calendarEventsList.size})"""


//            list[3].selectionDetails="""(${selectedVideosList.size}/${videosList.size})"""
            list[3].selectionDetails = """(${selectedVideosList.size}"""
            list[3].selectionDetailsTotal = """/${videosList.size})"""

//            list[4].selectionDetails="""(${selectedApksList.size}/${apksList.size})"""
            list[4].selectionDetails = """(${selectedApksList.size}"""
            list[4].selectionDetailsTotal = """/${apksList.size})"""

//            list[5].selectionDetails="""(${selectedAudiosList.size}/${audiosList.size})"""
            list[5].selectionDetails = """(${selectedAudiosList.size}"""
            list[5].selectionDetailsTotal = """/${audiosList.size})"""

//            list[6].selectionDetails="""(${selectedDocsList.size}/${docsList.size})"""
            list[6].selectionDetails = """(${selectedDocsList.size}"""
            list[6].selectionDetailsTotal = """/${docsList.size})"""


//            list[0].selectionDetails="""(${selectedContactsList.size}/${contactsList.size})"""
//            list[1].selectionDetails="""(${selectedImagesList.size}/${imagesList.size})"""
//            list[2].selectionDetails="""(${selectedCalendarEventsList.size}/${calendarEventsList.size})"""
//            list[3].selectionDetails="""(${selectedVideosList.size}/${videosList.size})"""
//            list[4].selectionDetails="""(${selectedApksList.size}/${apksList.size})"""
//            list[5].selectionDetails="""(${selectedAudiosList.size}/${audiosList.size})"""
//            list[6].selectionDetails="""(${selectedDocsList.size}/${docsList.size})"""
        }


        adapter?.notifyDataSetChanged()
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

        if (MyPermissions.hasCalendarPermission(this@SelectionActivity)) {
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
        if (grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == MyConstants.SPRC) {
            if (MyPermissions.hasStoragePermission(this@SelectionActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        MyPermissions.showStorageRational(this@SelectionActivity,
                            object : MyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        MyConstants.SPRC
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
        }else if(grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == MyConstants.CONTACTS_PERMISSIONS_REQUEST_CODE){
            if (MyPermissions.hasContactsPermission(this@SelectionActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        MyPermissions.showContactsRational(this@SelectionActivity,
                            object : MyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        MyConstants.SPRC
                                    )
                                }

                                override fun dismissed() {

                                }

                            })
                    }
                }
            }
        }else if(grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == MyConstants.CALEDAR_PERMISSION_REQUEST_CODE){
            if (MyPermissions.hasCalendarPermission(this@SelectionActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        MyPermissions.showCalendarRational(this@SelectionActivity,
                            object : MyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        MyConstants.SPRC
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
        calendarEventsUtils = CalendarEventsUtils(this@SelectionActivity, null)
        contactsHandler = ContactsUtils(this@SelectionActivity, null)
        imagesHandler = ImagesUtils(this@SelectionActivity, null)
        videosHandler = VideosUtils(this@SelectionActivity, null)
        apksHandler = AppsUtils(this@SelectionActivity, null)
        docsHandler = DocumentsUtils(this@SelectionActivity)
        audiosHanlder = AudiosUtils(this@SelectionActivity)
    }

    override fun doneLoadingContacts() {
        contactsList = ArrayList(ContactsUtils.contactsList)

        if (MyPermissions.hasStoragePermission(this@SelectionActivity)) {
            imagesHandler?.loadData()
        } else {
            doneLoadingPics()
        }
    }

    override fun doneLoadingCallLogs() {

    }

    override fun doneLoadingCalendarsEvents() {
        calendarEventsList = ArrayList(CalendarEventsUtils.calendarEventsList)

        if (MyPermissions.hasContactsPermission(this@SelectionActivity)) {
            contactsHandler?.loadData()
        } else {
            doneLoadingContacts()
        }


    }

    override fun doneLoadingPics() {
        imagesList = ArrayList(ImagesUtils.imagesList)
        if (MyPermissions.hasStoragePermission(this@SelectionActivity)) {
            videosHandler?.loadData()
        } else {
            doneLoadingVideos()
        }

    }

    override fun doneLoadingVideos() {
        videosList = ArrayList(VideosUtils.videosList)
        if (MyPermissions.hasStoragePermission(this@SelectionActivity)) {
            audiosHanlder?.loadData()
        } else {
            doneLoadingAudios()
        }

    }

    override fun doneLoadingAudios() {
        audiosList = ArrayList(AudiosUtils.audiosList)
        if (MyPermissions.hasStoragePermission(this@SelectionActivity)) {
            docsHandler?.loadData()
        } else {
            doneLoadingDocuments()
        }

    }

    override fun doneLoadingDocuments() {
        docsList = ArrayList(DocumentsUtils.docsList)
        apksHandler?.loadData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ContactsUtils.contactsList.clear()
        AppsUtils.apksList.clear()
        CalendarEventsUtils.calendarEventsList.clear()
        AppsUtils.apksList.clear()
        ImagesUtils.imagesList.clear()
        VideosUtils.videosList.clear()
        AudiosUtils.audiosList.clear()
        DocumentsUtils.docsList.clear()

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
        MyConstants.FILES_TO_SHARE.clear()
    }

    override fun doneLoadingApks() {
        apksList = ArrayList(AppsUtils.apksList)
        //data has been loaded do somehing with it
        binding.viewProgress.visibility = View.GONE
        binding.viewMain.visibility = View.VISIBLE

        prepareSelectionDetails()

        adapter = AdapterSelection(this, list, object : SelectionCallBack {
            override fun itemSelectionChanged(fileType: String, isChecked: Boolean) {

                when (fileType) {
                    MyConstants.FILE_TYPE_CONTACTS -> {
                        list[0].isSelected = isChecked
                        if (isChecked) {
                            selectedContactsList.clear()
                            selectedContactsList.addAll(contactsList)
                        } else {
                            selectedContactsList.clear()
                        }
                    }
                    MyConstants.FILE_TYPE_PICS -> {
                        list[1].isSelected = isChecked
                        if (isChecked) {
                            selectedImagesList.clear()
                            selectedImagesList.addAll(imagesList)
                        } else {
                            selectedImagesList.clear()
                        }
                    }
                    MyConstants.FILE_TYPE_CALENDAR -> {
                        list[2].isSelected = isChecked
                        if (isChecked) {
                            selectedCalendarEventsList.clear()
                            selectedCalendarEventsList.addAll(calendarEventsList)
                        } else {
                            selectedCalendarEventsList.clear()
                        }
                    }
                    MyConstants.FILE_TYPE_VIDEOS -> {
                        list[3].isSelected = isChecked
                        if (isChecked) {
                            selectedVideosList.clear()
                            selectedVideosList.addAll(videosList)
                        } else {
                            selectedVideosList.clear()
                        }
                    }
                    MyConstants.FILE_TYPE_APPS -> {
                        list[4].isSelected = isChecked
                        if (isChecked) {
                            selectedApksList.clear()
                            selectedApksList.addAll(apksList)
                        } else {
                            selectedApksList.clear()
                        }
                    }
                    MyConstants.FILE_TYPE_AUDIOS -> {
                        list[5].isSelected = isChecked
                        if (isChecked) {
                            selectedAudiosList.clear()
                            selectedAudiosList.addAll(audiosList)
                        } else {
                            selectedAudiosList.clear()
                        }
                    }
                    MyConstants.FILE_TYPE_DOCS -> {
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
                adapter?.notifyDataSetChanged()

            }

            override fun openItem(fileType: String, isSelected: Boolean) {

                if (fileType == MyConstants.FILE_TYPE_CONTACTS &&
                    MyPermissions.hasContactsPermission(this@SelectionActivity) &&
                    contactsList.isNotEmpty()
                ) {
                    openActivity(isSelected,fileType)
                    return
                } else if(fileType == MyConstants.FILE_TYPE_CONTACTS) {
                    MyPermissions.showContactsExplanation(this@SelectionActivity,
                        object : MyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                MyPermissions.requestContactsPermission(this@SelectionActivity)
                                return
                            }

                            override fun denyPermission() {
                                return
                            }

                        })
                    return
                }

                if (fileType == MyConstants.FILE_TYPE_CALENDAR&&
                    MyPermissions.hasCalendarPermission(this@SelectionActivity)) {
                    return
                }else  if (fileType == MyConstants.FILE_TYPE_CALENDAR){
                    MyPermissions.showCalendarExplanation(this@SelectionActivity,object:MyPermissions.ExplanationCallBack{
                        override fun requestPermission() {
                            MyPermissions.requestCalendarPermission(this@SelectionActivity)
                            return
                        }

                        override fun denyPermission() {
                            return
                        }

                    })
                    return
                }

                openActivity(isSelected,fileType)

            }

        })

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
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
        val intent = Intent(this@SelectionActivity, ShowItemsForSelection::class.java)
        intent.putExtra(MyConstants.FILE_TYPE, fileType)
        intent.putExtra(MyConstants.IS_SELECTED, isSelected)
        startActivity(intent)
    }

    private fun prepareTransferDetails() {

        populateFileSharingList()

        //       binding.tvDetails.text = """Sending to ${MyConstants.OPONENT_NAME}"""

    }

    private fun populateFileSharingList() {
        MyConstants.FILES_TO_SHARE.clear()
        if (SelectionActivity.selectedCalendarEventsList.isNotEmpty()) {
            CalendarFilePopulatingTask(object : AsyncTaskCallBack {
                override fun taskCompleted() {
                    val pathToSave = "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${MyConstants.EventsFileName}"
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + pathToSave
                    val file =
                        FileSharingModel(path, MyConstants.FILE_TYPE_CALENDAR, pathToSave)
                    MyConstants.FILES_TO_SHARE.add(file)

                    populateContactsFile()
                }

            }).execute()
        } else {
            populateContactsFile()
        }

    }

    private fun populateContactsFile() {
        if (SelectionActivity.selectedContactsList.isNotEmpty()) {
            ContactsFilePopulatingTask(object : AsyncTaskCallBack {
                override fun taskCompleted() {
                    val pathToSave = "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${MyConstants.ContactsFileName}"
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + pathToSave
                    val file =
                        FileSharingModel(path, MyConstants.FILE_TYPE_CONTACTS, pathToSave)
                    MyConstants.FILES_TO_SHARE.add(file)
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
                val intent = Intent(this@SelectionActivity, WifiP2PJoinGroupAndSend::class.java)
                startActivityForResult(intent, MyConstants.SELCTION_ACTIVITY)
            }

        }).execute()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == MyConstants.SELCTION_ACTIVITY) {
            binding.viewMain.visibility = View.VISIBLE
            binding.viewProgress.visibility = View.GONE
            finish()
        }else if (resultCode == MyConstants.RESTART_JOIN) {
            startActivityForResult(
                Intent(this@SelectionActivity, WifiP2PJoinGroupAndSend::class.java),
                MyConstants.FINISH_ACTIVITY_CODE
            )
        }  else if (requestCode == MyConstants.SPRC) {
            if (MyPermissions.hasStoragePermission(this@SelectionActivity)) {
                startLoadingData()
            } else {
                finish()
            }
        }else if(requestCode==MyConstants.CALEDAR_PERMISSION_REQUEST_CODE){
            if (MyPermissions.hasCalendarPermission(this@SelectionActivity)) {
                startLoadingData()
            }
        }else if(requestCode==MyConstants.CONTACTS_PERMISSIONS_REQUEST_CODE){
            if(MyPermissions.hasContactsPermission(this@SelectionActivity)){
                startLoadingData()
            }
        }
    }

    private fun prepareSelectionDetails() {
        list.clear()
        list.add(
            FilesToShareModel(
                this@SelectionActivity,
                MyConstants.FILE_TYPE_CONTACTS,
                contactsList.size,
                0
            )
        )
        list.add(
            FilesToShareModel(
                this@SelectionActivity,
                MyConstants.FILE_TYPE_PICS,
                imagesList.size,
                imagesHandler?.getJustSize()
            )
        )
        list.add(
            FilesToShareModel(
                this@SelectionActivity,
                MyConstants.FILE_TYPE_CALENDAR,
                calendarEventsList.size,
                0
            )
        )
        list.add(
            FilesToShareModel(
                this@SelectionActivity,
                MyConstants.FILE_TYPE_VIDEOS,
                videosList.size,
                videosHandler?.getJustSize()
            )
        )
        list.add(
            FilesToShareModel(
                this@SelectionActivity,
                MyConstants.FILE_TYPE_APPS,
                apksList.size,
                apksHandler?.getJustSzie()
            )
        )
        list.add(
            FilesToShareModel(
                this@SelectionActivity,
                MyConstants.FILE_TYPE_AUDIOS,
                audiosList.size,
                audiosHanlder?.getJustSize()
            )
        )
        list.add(
            FilesToShareModel(
                this@SelectionActivity,
                MyConstants.FILE_TYPE_DOCS,
                docsList.size,
                docsHandler?.getJustSize()
            )
        )
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
                    }/${MyConstants.EventsFileName}"
                )
                fileWriter = FileWriter(file)
                fileWriter.append(CSV_HEADER)
                fileWriter.append('\n')

                for (event in SelectionActivity.selectedCalendarEventsList) {
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
                    }/${MyConstants.ContactsFileName}"
                )
                fileWriter = FileWriter(file)
                fileWriter.append(CSV_HEADER)
                fileWriter.append('\n')

                for (contact in SelectionActivity.selectedContactsList) {
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
            if (SelectionActivity.selectedApksList.isNotEmpty()) {
                val list = ArrayList(selectedApksList)
                list.forEach {
                    val f = FileSharingModel(
                        it.srcDir,
                        MyConstants.FILE_TYPE_APPS,
                        fileName = it.apkName,
                        pathToSave = ""
                    )
                    MyConstants.FILES_TO_SHARE.add(f)
                }
            }
            if (SelectionActivity.selectedVideosList.isNotEmpty()) {
                val list = ArrayList(selectedVideosList)
                list.forEach {
                    MyConstants.FILES_TO_SHARE.add(
                        FileSharingModel(
                            it.path,
                            MyConstants.FILE_TYPE_VIDEOS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedImagesList.isNotEmpty()) {
                val list = ArrayList(selectedImagesList)
                list.forEach {
                    MyConstants.FILES_TO_SHARE.add(
                        FileSharingModel(
                            it.path,
                            MyConstants.FILE_TYPE_PICS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedAudiosList.isNotEmpty()) {
                val list = ArrayList(selectedAudiosList)
                list.forEach {
                    MyConstants.FILES_TO_SHARE.add(
                        FileSharingModel(
                            it.filePath,
                            MyConstants.FILE_TYPE_AUDIOS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedDocsList.isNotEmpty()) {

                val list = ArrayList(selectedDocsList)
                list.forEach {
                    MyConstants.FILES_TO_SHARE.add(
                        FileSharingModel(
                            it.filePath,
                            MyConstants.FILE_TYPE_DOCS,
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
    fun loadFbBannerAdd() {

        val adView = AdView(
            this@SelectionActivity,
            this@SelectionActivity.resources.getString(R.string.banner_add),
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