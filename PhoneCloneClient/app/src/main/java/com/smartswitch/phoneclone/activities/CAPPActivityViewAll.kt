package com.smartswitch.phoneclone.activities

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.GridLayoutManager
import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.activities.CAPPDataSelectionMainActivity.Companion.checkbtnVisibility
import com.smartswitch.phoneclone.adapters.CAPPAdapterGeneralShowItems
//import com.switchphone.transferdata.ads.AATIntersitialAdHelper
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.databinding.ActivitySeeMoreCappBinding
import com.smartswitch.phoneclone.modelclasses.CAPPFileSharingModel
import com.smartswitch.phoneclone.modelclasses.CAPPMediaModelClass
import java.util.*

class CAPPActivityViewAll : AppCompatActivity() {
    lateinit var binding: ActivitySeeMoreCappBinding
    var arrayList = ArrayList<Any>()
    var FILE_TYPE: String = ""


    var HSAdapter: CAPPAdapterGeneralShowItems? = null

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

    var isSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeMoreCappBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        AATIntersitialAdHelper.loadAdmobBanner(
//            this,
//            findViewById(R.id.top_banner),
//            resources.getString(R.string.admob_banner)
//        )

        
        FILE_TYPE = intent.getStringExtra(CAPPMConstants.FILE_TYPE).toString()
        isSelected = intent.getBooleanExtra(CAPPMConstants.IS_SELECTED, false)

        CAPPAdapterGeneralShowItems.FILE_TYPE = FILE_TYPE




        binding.toolbar.setNavigationIcon(R.drawable.ic_group_back_white)
        binding.toolbarTitle.text=FILE_TYPE
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        binding.btnDone.setOnClickListener {
            finish()
        }

        PopulateArrayLists().execute()

    }


    private fun setCheckBoxState(b: Boolean) {
        binding.cbSelectAll.setOnCheckedChangeListener(null)
        binding.cbSelectAll.isChecked = b
        binding.cbSelectAll.setOnCheckedChangeListener(checkBoxListener)

    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    inner class PopulateArrayLists : AsyncTask<Void, Void, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            binding.viewMain.visibility = View.GONE
            binding.viewProgress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): Void? {
            when (FILE_TYPE) {
                CAPPMConstants.FILE_TYPE_DOCS -> {
                    for(item in CAPPDataSelectionMainActivity.docsList){
                        item.isSelected=isSelected
                    }
                }
                CAPPMConstants.FILE_TYPE_AUDIOS -> {
                    for(item in CAPPDataSelectionMainActivity.audiosList){
                        item.isSelected=isSelected
                    }
                }
                CAPPMConstants.FILE_TYPE_VIDEOS -> {
                    for(item in CAPPDataSelectionMainActivity.videosList){
                        item.isSelected=isSelected
                    }
                }
                CAPPMConstants.FILE_TYPE_PICS -> {
                    for(item in CAPPDataSelectionMainActivity.imagesList){
                        item.isSelected=isSelected
                    }
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            binding.viewMain.visibility = View.VISIBLE
            binding.viewProgress.visibility = View.GONE

            when (FILE_TYPE) {
                CAPPMConstants.FILE_TYPE_PICS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = CAPPAdapterGeneralShowItems(this@CAPPActivityViewAll, CAPPDataSelectionMainActivity.imagesList, object : CAPPShowItemsForSelection.ShowItemsCallBack {
                            override fun itemSelectionChanged(fileType: String, isChecked: Boolean, _row: Any) {
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
                                setCheckBoxState(CAPPDataSelectionMainActivity.imagesList.size == CAPPDataSelectionMainActivity.selectedImagesList.size)
                            }

                        })
                }
                CAPPMConstants.FILE_TYPE_VIDEOS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = CAPPAdapterGeneralShowItems(
                        this@CAPPActivityViewAll,
                        CAPPDataSelectionMainActivity.videosList,
                        object : CAPPShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(CAPPDataSelectionMainActivity.videosList.size == CAPPDataSelectionMainActivity.selectedVideosList.size)
                            }


                        })
                }
                CAPPMConstants.FILE_TYPE_AUDIOS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = CAPPAdapterGeneralShowItems(
                        this@CAPPActivityViewAll,
                        CAPPDataSelectionMainActivity.audiosList,
                        object : CAPPShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(CAPPDataSelectionMainActivity.audiosList.size == CAPPDataSelectionMainActivity.selectedAudiosList.size)
                            }

                        })
                }
                CAPPMConstants.FILE_TYPE_DOCS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = CAPPAdapterGeneralShowItems(
                        this@CAPPActivityViewAll,
                        CAPPDataSelectionMainActivity.docsList,
                        object : CAPPShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(CAPPDataSelectionMainActivity.docsList.size == CAPPDataSelectionMainActivity.selectedDocsList.size)
                            }

                        })


                }
            }
            binding.cbSelectAll.setOnCheckedChangeListener(checkBoxListener)


            binding.rvFiles.layoutManager = GridLayoutManager(this@CAPPActivityViewAll, 1)
            binding.rvFiles.adapter = HSAdapter
        }
    }
}