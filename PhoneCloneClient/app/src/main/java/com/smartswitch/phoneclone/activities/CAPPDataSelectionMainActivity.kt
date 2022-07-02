package com.smartswitch.phoneclone.activities


//import com.switchphone.transferdata.ads.CappIntersitialAdHelper

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.adapters.CAPPAdapterSelectionMainActivity
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.databinding.ActivitySelectionCappBinding
import com.smartswitch.phoneclone.datautils.*
import com.smartswitch.phoneclone.interfaces.CAPPUtilsCallBacks
import com.smartswitch.phoneclone.modelclasses.*
import com.smartswitch.phoneclone.utills.CAPPMPermissions
import com.smartswitch.phoneclone.utills.CAPPMemoryStatus
import com.smartswitch.phoneclone.widgets.FitChartValue
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*


class CAPPDataSelectionMainActivity : AppCompatActivity(), CAPPUtilsCallBacks {
    lateinit var binding: ActivitySelectionCappBinding

    var totalMemory:Double=0.0
    var availableMemory:Double=0.0

    var usedMemorySpace:Double=0.0

    var usedPercentage:Int= 0

    companion object {
        var checkbtnVisibility:Int=0
        var apksList = ArrayList<CAPPAppsModel>()
        var calendarEventsList = ArrayList<CAPPCalendarEventsModel>()
        var contactsList = ArrayList<CAPPContactsModel>()
        var videosList = ArrayList<CAPPMediaModelClass>()
        var imagesList = ArrayList<CAPPMediaModelClass>()
        var docsList = ArrayList<CAPPFileSharingModel>()
        var audiosList = ArrayList<CAPPFileSharingModel>()


        var selectedApksList = ArrayList<CAPPAppsModel>()
        var selectedCalendarEventsList = ArrayList<CAPPCalendarEventsModel>()
        var selectedContactsList = ArrayList<CAPPContactsModel>()
        var selectedVideosList = ArrayList<CAPPMediaModelClass>()
        var selectedImagesList = ArrayList<CAPPMediaModelClass>()
        var selectedDocsList = ArrayList<CAPPFileSharingModel>()
        var selectedAudiosList = ArrayList<CAPPFileSharingModel>()

    }

    var checkChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkbtnVisibility++
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
               checkbtnVisibility--
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

                values.clear()



                checkbtnvisible()
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


//            checkbtnvisible()
            HSAdapter?.notifyDataSetChanged()
        }


    var list = ArrayList<CAPPFilesToShareModel>()

    var HSContactsHandler: CAPPContactsUtils? = null
    var HSImagesHandler: CAPPImagesUtils? = null
    var HSVideosHandler: CAPPVideosUtils? = null
    var calendarEventsUtils: CAPPCalendarEventsUtils? = null
    var apksHandler: CAPPAppsUtils? = null
    var docsHandler: CAPPDocumentsUtils? = null
    var HSAudiosHanlder: CAPPAudiosUtils? = null
    var HSAdapter: CAPPAdapterSelectionMainActivity? = null
    var toolbar:Toolbar?=null
    val values: MutableCollection<FitChartValue> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionCappBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        CappIntersitialAdHelper.loadAdmobBanner(
//            this,
//            findViewById(R.id.top_banner),
//            resources.getString(R.string.admob_banner)
//        )

        totalMemory=CAPPMemoryStatus.getTotalExternalMemorySize()
        availableMemory=CAPPMemoryStatus.getAvailableExternalMemorySize()
        usedMemorySpace=totalMemory-availableMemory
        usedPercentage= (((usedMemorySpace/totalMemory))*100).toInt()

        binding.percentageSelected.setText("" +usedPercentage+" %")




        toolbar=findViewById(R.id.toolbar)
        toolbar?.setNavigationIcon(R.drawable.ic_group_back_white)

        toolbar?.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        binding.pieChart.minValue=0f
        binding.pieChart.maxValue=100f




        /* loadFbBannerAdd()
         HSNativeAdHelper.showAd(this@HSDataSelectionActivity,binding.nativeAdContainer)*/
        CAPPMConstants.FILES_TO_SHARE.clear()

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
        if (CAPPMPermissions.hasStoragePermission(this@CAPPDataSelectionMainActivity)) {
            startLoadingData()
        } else {
            CAPPMPermissions.showStorageExplanation(this@CAPPDataSelectionMainActivity,
                object : CAPPMPermissions.ExplanationCallBack {
                    override fun requestPermission() {

                        CAPPMPermissions.requestStoragePermission(this@CAPPDataSelectionMainActivity)

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

            values.clear()

            checkbtnvisible()
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
        binding.viewProgress.visibility = View.VISIBLE
        binding.viewMain.visibility = View.GONE

        if (CAPPMPermissions.hasCalendarPermission(this@CAPPDataSelectionMainActivity)) {
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
        if (grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == CAPPMConstants.SPRC) {
            if (CAPPMPermissions.hasStoragePermission(this@CAPPDataSelectionMainActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        CAPPMPermissions.showStorageRational(this@CAPPDataSelectionMainActivity,
                            object : CAPPMPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        CAPPMConstants.SPRC
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
        } else if (grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == CAPPMConstants.CONTACTS_PERMISSIONS_REQUEST_CODE) {
            if (CAPPMPermissions.hasContactsPermission(this@CAPPDataSelectionMainActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        CAPPMPermissions.showContactsRational(this@CAPPDataSelectionMainActivity,
                            object : CAPPMPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        CAPPMConstants.SPRC
                                    )
                                }

                                override fun dismissed() {

                                }

                            })
                    }
                }
            }
        } else if (grantResults.isNotEmpty() && permissions.isNotEmpty() && requestCode == CAPPMConstants.CALEDAR_PERMISSION_REQUEST_CODE) {
            if (CAPPMPermissions.hasCalendarPermission(this@CAPPDataSelectionMainActivity)) {
                startLoadingData()
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    val showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        CAPPMPermissions.showCalendarRational(this@CAPPDataSelectionMainActivity,
                            object : CAPPMPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        CAPPMConstants.SPRC
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
        calendarEventsUtils = CAPPCalendarEventsUtils(this@CAPPDataSelectionMainActivity, null)
        HSContactsHandler = CAPPContactsUtils(this@CAPPDataSelectionMainActivity, null)
        HSImagesHandler = CAPPImagesUtils(this@CAPPDataSelectionMainActivity, null)
        HSVideosHandler = CAPPVideosUtils(this@CAPPDataSelectionMainActivity, null)
        apksHandler = CAPPAppsUtils(this@CAPPDataSelectionMainActivity, null)
        docsHandler = CAPPDocumentsUtils(this@CAPPDataSelectionMainActivity)
        HSAudiosHanlder = CAPPAudiosUtils(this@CAPPDataSelectionMainActivity)
    }

    override fun doneLoadingContacts() {
        contactsList = ArrayList(CAPPContactsUtils.contactsList)

        if (CAPPMPermissions.hasStoragePermission(this@CAPPDataSelectionMainActivity)) {
            HSImagesHandler?.loadData()
        } else {
            doneLoadingPics()
        }
    }

    override fun doneLoadingCallLogs() {

    }

    override fun doneLoadingCalendarsEvents() {
        calendarEventsList = ArrayList(CAPPCalendarEventsUtils.calendarEventsList)

        if (CAPPMPermissions.hasContactsPermission(this@CAPPDataSelectionMainActivity)) {
            HSContactsHandler?.loadData()
        } else {
            doneLoadingContacts()
        }


    }

    fun openSettingsAllFilesAccess(activity: AppCompatActivity) {
        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        activity.startActivity(intent)
    }

    override fun doneLoadingPics() {
        imagesList = ArrayList(CAPPImagesUtils.imagesList)
        if (CAPPMPermissions.hasStoragePermission(this@CAPPDataSelectionMainActivity)) {
            HSVideosHandler?.loadData()
        } else {
            doneLoadingVideos()
        }

    }

    override fun doneLoadingVideos() {
        videosList = ArrayList(CAPPVideosUtils.videosList)
        if (CAPPMPermissions.hasStoragePermission(this@CAPPDataSelectionMainActivity)) {
            HSAudiosHanlder?.loadData()
        } else {
            doneLoadingAudios()
        }

    }

    override fun doneLoadingAudios() {
        audiosList = ArrayList(CAPPAudiosUtils.audiosList)
        if (CAPPMPermissions.hasStoragePermission(this@CAPPDataSelectionMainActivity)) {
            docsHandler?.loadData()
        } else {
            doneLoadingDocuments()
        }

    }

    override fun doneLoadingDocuments() {
        docsList = ArrayList(CAPPDocumentsUtils.docsList)
        apksHandler?.loadData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CAPPContactsUtils.contactsList.clear()
        CAPPAppsUtils.apksList.clear()
        CAPPCalendarEventsUtils.calendarEventsList.clear()
        CAPPAppsUtils.apksList.clear()
        CAPPImagesUtils.imagesList.clear()
        CAPPVideosUtils.videosList.clear()
        CAPPAudiosUtils.audiosList.clear()
        CAPPDocumentsUtils.docsList.clear()

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
        CAPPMConstants.FILES_TO_SHARE.clear()
    }

    override fun doneLoadingApks() {
        apksList = ArrayList(CAPPAppsUtils.apksList)
        //data has been loaded do somehing with it
        binding.viewProgress.visibility = View.GONE
        binding.viewMain.visibility = View.VISIBLE

        prepareSelectionDetails()

        TaskCreatePieChart().execute()


        HSAdapter = CAPPAdapterSelectionMainActivity(this, list, object : SelectionCallBack {
            override fun itemSelectionChanged(fileType: String, isChecked: Boolean) {

                when (fileType) {
                    CAPPMConstants.FILE_TYPE_CONTACTS -> {
                        list[0].isSelected = isChecked
                        if (isChecked) {
                            checkbtnVisibility++
                            checkbtnvisible()
                            selectedContactsList.clear()
                            selectedContactsList.addAll(contactsList)

                        } else {
                            selectedContactsList.clear()
                            checkbtnVisibility--
                            checkbtnvisible()

                        }
                    }
                    CAPPMConstants.FILE_TYPE_PICS -> {
                        list[1].isSelected = isChecked
                        if (isChecked) {
                            checkbtnVisibility++
                            checkbtnvisible()
                            selectedImagesList.clear()
                            selectedImagesList.addAll(imagesList)

                        } else {
                            selectedImagesList.clear()
                            checkbtnVisibility--
                            checkbtnvisible()
                        }
                    }
                    CAPPMConstants.FILE_TYPE_CALENDAR -> {
                        list[2].isSelected = isChecked
                        if (isChecked) {
                            checkbtnVisibility++
                            checkbtnvisible()
                            selectedCalendarEventsList.clear()
                            selectedCalendarEventsList.addAll(calendarEventsList)

                        } else {
                            selectedCalendarEventsList.clear()
                            checkbtnVisibility--
                            checkbtnvisible()
                        }
                    }
                    CAPPMConstants.FILE_TYPE_VIDEOS -> {
                        list[3].isSelected = isChecked
                        if (isChecked) {
                            checkbtnVisibility++
                            checkbtnvisible()
                            selectedVideosList.clear()
                            selectedVideosList.addAll(videosList)

                        } else {
                            selectedVideosList.clear()
                            checkbtnVisibility--
                            checkbtnvisible()
                        }
                    }
                    CAPPMConstants.FILE_TYPE_APPS -> {
                        list[4].isSelected = isChecked
                        if (isChecked) {
                            checkbtnVisibility++
                            checkbtnvisible()
                            selectedApksList.clear()
                            selectedApksList.addAll(apksList)

                        } else {
                            selectedApksList.clear()
                            checkbtnVisibility--
                            checkbtnvisible()
                        }
                    }
                    CAPPMConstants.FILE_TYPE_AUDIOS -> {
                        list[5].isSelected = isChecked
                        if (isChecked) {
                            checkbtnVisibility++
                            checkbtnvisible()
                            selectedAudiosList.clear()
                            selectedAudiosList.addAll(audiosList)

                        } else {
                            selectedAudiosList.clear()
                            checkbtnVisibility--
                            checkbtnvisible()
                        }
                    }
                    CAPPMConstants.FILE_TYPE_DOCS -> {
                        list[6].isSelected = isChecked
                        if (isChecked) {
                            checkbtnVisibility++
                            selectedDocsList.clear()
                            selectedDocsList.addAll(docsList)
                            checkbtnvisible()

                        } else {
                            selectedDocsList.clear()
                            checkbtnVisibility--
                            checkbtnvisible()
                        }
                    }
                }


                manageSelectAllSelection()
                HSAdapter?.notifyDataSetChanged()

            }

            override fun openItem(fileType: String, isSelected: Boolean) {

                if (fileType == CAPPMConstants.FILE_TYPE_CONTACTS &&
                    CAPPMPermissions.hasContactsPermission(this@CAPPDataSelectionMainActivity) &&
                    contactsList.isNotEmpty()
                ) {
                    openActivity(isSelected, fileType)
                    return
                } else if (fileType == CAPPMConstants.FILE_TYPE_CONTACTS) {
                    CAPPMPermissions.showContactsExplanation(this@CAPPDataSelectionMainActivity,
                        object : CAPPMPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                CAPPMPermissions.requestContactsPermission(this@CAPPDataSelectionMainActivity)
                                return
                            }

                            override fun denyPermission() {
                                return
                            }

                        })
                    return
                }

                if (fileType == CAPPMConstants.FILE_TYPE_CALENDAR &&
                    CAPPMPermissions.hasCalendarPermission(this@CAPPDataSelectionMainActivity)
                ) {
                    return
                } else if (fileType == CAPPMConstants.FILE_TYPE_CALENDAR) {
                    CAPPMPermissions.showCalendarExplanation(this@CAPPDataSelectionMainActivity,
                        object : CAPPMPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                CAPPMPermissions.requestCalendarPermission(this@CAPPDataSelectionMainActivity)
                                return
                            }

                            override fun denyPermission() {
                                return
                            }

                        })
                    return
                }

                if (fileType == CAPPMConstants.FILE_TYPE_APPS) {
                    openActivity(isSelected, fileType)
                } else {
                    openViewAll(isSelected, fileType)
                }

            }

        })

        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
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
    var videosPercentUsed:Float=0f
    var imagesPercentUsed:Float=0f
    var docsPercentUsed:Float=0f
    var apksPercentUsed:Float=0f
    var contactPercentUsed:Float=0f
    var calenderPercentUsed:Float=0f
    var audioPercentUsed:Float=0f
    var totalPercentageused:Float=0f
    inner class TaskCreatePieChart : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
            values.clear()

             videosPercentUsed= ((videosSize / usedMemorySpace) * 100).toFloat()
             imagesPercentUsed= ((imagesSize / usedMemorySpace) * 100).toFloat()
             docsPercentUsed= ((docsSize / usedMemorySpace) * 100).toFloat()
             apksPercentUsed= ((apksSize / usedMemorySpace) * 100).toFloat()
             contactPercentUsed= ((contactsSize / usedMemorySpace) * 100).toFloat()
             calenderPercentUsed= ((calenderSize / usedMemorySpace) * 100).toFloat()
             audioPercentUsed= ((audioSize / usedMemorySpace) * 100).toFloat()

             totalPercentageused=videosPercentUsed+imagesPercentUsed+docsPercentUsed+apksPercentUsed+contactPercentUsed+calenderPercentUsed+audioPercentUsed

        }
        override fun doInBackground(vararg p0: Void?): Void? {
           createAndAddValuestoPieChart()
            return null
        }
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            binding.pieChart.setValues(values)
        }
    }

    private fun createAndAddValuestoPieChart() {


        if(usedMemorySpace!=0.0) {
            values.add(
                FitChartValue(
                    videosPercentUsed,
                    resources.getColor(R.color.videoscolor)
                )
            )
            values.add(
                FitChartValue(
                    imagesPercentUsed,
                    resources.getColor(R.color.photoscolor)
                )
            )
            values.add(
                FitChartValue(
                    docsPercentUsed,
                    resources.getColor(R.color.docscolor)
                )
            )
            values.add(
                FitChartValue(
                    apksPercentUsed,
                    resources.getColor(R.color.appscolor)
                )
            )
            values.add(
                FitChartValue(
                    contactPercentUsed,
                    resources.getColor(R.color.contactscolor)
                )
            )
            values.add(
                FitChartValue(
                    calenderPercentUsed,
                    resources.getColor(R.color.calendercolor)
                )
            )
            values.add(
                FitChartValue(
                    audioPercentUsed,
                    resources.getColor(R.color.audiocolor)
                )
            )

            values.add(
                FitChartValue(
                    usedPercentage-totalPercentageused,
                    resources.getColor(R.color.other)
                )
            )

        }

    }

    private fun checkbtnvisible() {
       if(checkbtnVisibility==0){
           binding.btnDone.visibility=View.GONE
       }else{
           binding.btnDone.visibility=View.VISIBLE
       }
    }


    fun openActivity(isSelected: Boolean, fileType: String) {
        val intent = Intent(this@CAPPDataSelectionMainActivity, CAPPShowItemsForSelection::class.java)
        intent.putExtra(CAPPMConstants.FILE_TYPE, fileType)
        intent.putExtra(CAPPMConstants.IS_SELECTED, isSelected)
        startActivity(intent)
    }

    fun openViewAll(isSelected: Boolean, fileType: String) {
        val intent = Intent(this@CAPPDataSelectionMainActivity, CAPPActivityViewAll::class.java)
        intent.putExtra(CAPPMConstants.FILE_TYPE, fileType)
        intent.putExtra(CAPPMConstants.IS_SELECTED, isSelected)
        startActivity(intent)
    }

    private fun prepareTransferDetails() {

        populateFileSharingList()

        //       binding.tvDetails.text = """Sending to ${HSMyConstants.OPONENT_NAME}"""

    }

    private fun populateFileSharingList() {
        CAPPMConstants.FILES_TO_SHARE.clear()
        if (CAPPDataSelectionMainActivity.selectedCalendarEventsList.isNotEmpty()) {
            CalendarFilePopulatingTask(object : AsyncTaskCallBack {
                override fun taskCompleted() {
                    val pathToSave = "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${CAPPMConstants.EventsFileName}"
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + pathToSave
                    val file =
                        CAPPFileSharingModel(path, CAPPMConstants.FILE_TYPE_CALENDAR, pathToSave)
                    CAPPMConstants.FILES_TO_SHARE.add(file)

                    populateContactsFile()
                }

            }).execute()
        } else {
            populateContactsFile()
        }

    }

    private fun populateContactsFile() {
        if (CAPPDataSelectionMainActivity.selectedContactsList.isNotEmpty()) {
            ContactsFilePopulatingTask(object : AsyncTaskCallBack {
                override fun taskCompleted() {
                    val pathToSave = "/${
                        applicationContext.resources.getString(R.string.app_name)
                    }/${CAPPMConstants.ContactsFileName}"
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + pathToSave
                    val file =
                        CAPPFileSharingModel(path, CAPPMConstants.FILE_TYPE_CONTACTS, pathToSave)
                    CAPPMConstants.FILES_TO_SHARE.add(file)
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
                val intent = Intent(
                    this@CAPPDataSelectionMainActivity,
                    CAPPWifiP2PJoinGroupAndSend::class.java
                )
                startActivityForResult(intent, CAPPMConstants.SELCTION_ACTIVITY)
            }

        }).execute()
    }
    var contactsSize:Long=0L
    var imagesSize:Long=0L
    var calenderSize:Long=0L
    var videosSize:Long=0L
    var apksSize:Long=0L
    var audioSize:Long=0L
    var docsSize:Long=0L
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == CAPPMConstants.SELCTION_ACTIVITY) {
            binding.viewMain.visibility = View.VISIBLE
            binding.viewProgress.visibility = View.GONE
            finish()
        } else if (resultCode == CAPPMConstants.RESTART_JOIN) {
            startActivityForResult(
                Intent(this@CAPPDataSelectionMainActivity, CAPPWifiP2PJoinGroupAndSend::class.java),
                CAPPMConstants.FINISH_ACTIVITY_CODE
            )
        } else if (requestCode == CAPPMConstants.SPRC) {
            if (CAPPMPermissions.hasStoragePermission(this@CAPPDataSelectionMainActivity)) {
                startLoadingData()
            } else {
                finish()
            }
        } else if (requestCode == CAPPMConstants.CALEDAR_PERMISSION_REQUEST_CODE) {
            if (CAPPMPermissions.hasCalendarPermission(this@CAPPDataSelectionMainActivity)) {
                startLoadingData()
            }
        } else if (requestCode == CAPPMConstants.CONTACTS_PERMISSIONS_REQUEST_CODE) {
            if (CAPPMPermissions.hasContactsPermission(this@CAPPDataSelectionMainActivity)) {
                startLoadingData()
            }
        } else if (requestCode == CAPPMConstants.ManageAllFilePermissionRequestCode) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    startLoadingData()
                } else {
                    finish()
                }
            }

        }
    }
    var used:Double=0.0
    private fun prepareSelectionDetails() {



        list.clear()
        list.add(
            CAPPFilesToShareModel(
                this@CAPPDataSelectionMainActivity,
                CAPPMConstants.FILE_TYPE_CONTACTS,
                contactsList.size,
                0

            )
        )
        contactsSize=0L
        used+=contactsSize
        list.add(
            CAPPFilesToShareModel(
                this@CAPPDataSelectionMainActivity,
                CAPPMConstants.FILE_TYPE_PICS,
                imagesList.size,
                HSImagesHandler?.getJustSize()
            )
        )
        imagesSize= HSImagesHandler?.getJustSize()!!
        used+=imagesSize
        list.add(
            CAPPFilesToShareModel(
                this@CAPPDataSelectionMainActivity,
                CAPPMConstants.FILE_TYPE_CALENDAR,
                calendarEventsList.size,
                0
            )
        )
        calenderSize=0L
        used+=calenderSize
        list.add(
            CAPPFilesToShareModel(
                this@CAPPDataSelectionMainActivity,
                CAPPMConstants.FILE_TYPE_VIDEOS,
                videosList.size,
                HSVideosHandler?.getJustSize()
            )
        )
        videosSize=HSVideosHandler?.getJustSize()!!
        used+=videosSize
        list.add(
            CAPPFilesToShareModel(
                this@CAPPDataSelectionMainActivity,
                CAPPMConstants.FILE_TYPE_APPS,
                apksList.size,
                apksHandler?.getJustSzie()
            )
        )
        apksSize=apksHandler?.getJustSzie()!!
        used+=apksSize
        list.add(
            CAPPFilesToShareModel(
                this@CAPPDataSelectionMainActivity,
                CAPPMConstants.FILE_TYPE_AUDIOS,
                audiosList.size,
                HSAudiosHanlder?.getJustSize()
            )
        )
        audioSize=HSAudiosHanlder?.getJustSize()!!
        used+=audioSize
        list.add(
            CAPPFilesToShareModel(
                this@CAPPDataSelectionMainActivity,
                CAPPMConstants.FILE_TYPE_DOCS,
                docsList.size,
                docsHandler?.getJustSize()
            )
        )
        docsSize=docsHandler?.getJustSize()!!
        used+=docsSize
        for (i in docsList) {
            if (list[6].innerList.size < 3) {
                list[6].innerList.add(i)
            } else {
                break
            }
        }
        for (i in audiosList) {
            if (list[5].innerList.size < 3) {
                list[5].innerList.add(i)
            } else {
                break
            }
        }
        for (i in videosList) {
            if (list[3].innerList.size < 3) {
                list[3].innerList.add(i)
            } else {
                break
            }
        }
        list[1].selectionDetailsTotal = """/${imagesList.size})"""
        for (i in imagesList) {
            if (list[1].innerList.size < 3) {
                list[1].innerList.add(i)
            } else {
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
                    }/${CAPPMConstants.EventsFileName}"
                )
                fileWriter = FileWriter(file)
                fileWriter.append(CSV_HEADER)
                fileWriter.append('\n')

                for (event in CAPPDataSelectionMainActivity.selectedCalendarEventsList) {
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
                    }/${CAPPMConstants.ContactsFileName}"
                )
                fileWriter = FileWriter(file)
                fileWriter.append(CSV_HEADER)
                fileWriter.append('\n')

                for (contact in CAPPDataSelectionMainActivity.selectedContactsList) {
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
                    fileWriter?.flush()
                    fileWriter?.close()
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
            if (CAPPDataSelectionMainActivity.selectedApksList.isNotEmpty()) {
                val list = ArrayList(selectedApksList)
                list.forEach {
                    val f = CAPPFileSharingModel(
                        it.srcDir,
                        CAPPMConstants.FILE_TYPE_APPS,
                        fileName = it.apkName,
                        pathToSave = ""
                    )
                    CAPPMConstants.FILES_TO_SHARE.add(f)
                }
            }
            if (CAPPDataSelectionMainActivity.selectedVideosList.isNotEmpty()) {
                val list = ArrayList(selectedVideosList)
                list.forEach {
                    CAPPMConstants.FILES_TO_SHARE.add(
                        CAPPFileSharingModel(
                            it.path,
                            CAPPMConstants.FILE_TYPE_VIDEOS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedImagesList.isNotEmpty()) {
                val list = ArrayList(selectedImagesList)
                list.forEach {
                    CAPPMConstants.FILES_TO_SHARE.add(
                        CAPPFileSharingModel(
                            it.path,
                            CAPPMConstants.FILE_TYPE_PICS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedAudiosList.isNotEmpty()) {
                val list = ArrayList(selectedAudiosList)
                list.forEach {
                    CAPPMConstants.FILES_TO_SHARE.add(
                        CAPPFileSharingModel(
                            it.filePath,
                            CAPPMConstants.FILE_TYPE_AUDIOS,
                            it.getActualPath()
                        )
                    )
                }
            }
            if (selectedDocsList.isNotEmpty()) {

                val list = ArrayList(selectedDocsList)
                list.forEach {
                    CAPPMConstants.FILES_TO_SHARE.add(
                        CAPPFileSharingModel(
                            it.filePath,
                            CAPPMConstants.FILE_TYPE_DOCS,
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