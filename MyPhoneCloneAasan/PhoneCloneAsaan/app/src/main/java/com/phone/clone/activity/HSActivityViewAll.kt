package com.phone.clone.activity

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.GridLayoutManager
import com.phone.clone.adapters.HSAdapterGeneralShowItems
import com.phone.clone.constants.HSMyConstants
import com.phone.clone.databinding.ActivitySeeMoreBinding
import com.phone.clone.modelclasses.HSFileSharingModel
import com.phone.clone.modelclasses.HSMediaModelClass

class HSActivityViewAll : AppCompatActivity() {
    lateinit var binding: ActivitySeeMoreBinding
    var arrayList = ArrayList<Any>()
    var FILE_TYPE: String = ""


    var HSAdapter: HSAdapterGeneralShowItems? = null

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

    var isSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FILE_TYPE = intent.getStringExtra(HSMyConstants.FILE_TYPE)
        isSelected = intent.getBooleanExtra(HSMyConstants.IS_SELECTED, false)

        HSAdapterGeneralShowItems.FILE_TYPE = FILE_TYPE

        setSupportActionBar(binding.tbToolbar)
        supportActionBar?.title = FILE_TYPE
        binding.tbToolbar.setNavigationOnClickListener {
            finish()
        }

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
                HSMyConstants.FILE_TYPE_DOCS -> {
                    for(item in HSDataSelectionActivity.docsList){
                        item.isSelected=isSelected
                    }
                }
                HSMyConstants.FILE_TYPE_AUDIOS -> {
                    for(item in HSDataSelectionActivity.audiosList){
                        item.isSelected=isSelected
                    }
                }
                HSMyConstants.FILE_TYPE_VIDEOS -> {
                    for(item in HSDataSelectionActivity.videosList){
                        item.isSelected=isSelected
                    }
                }
                HSMyConstants.FILE_TYPE_PICS -> {
                    for(item in HSDataSelectionActivity.imagesList){
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
                HSMyConstants.FILE_TYPE_PICS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = HSAdapterGeneralShowItems(
                        this@HSActivityViewAll,
                        HSDataSelectionActivity.imagesList,
                        object : HSShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(HSDataSelectionActivity.imagesList.size == HSDataSelectionActivity.selectedImagesList.size)
                            }

                        })
                }
                HSMyConstants.FILE_TYPE_VIDEOS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = HSAdapterGeneralShowItems(
                        this@HSActivityViewAll,
                        HSDataSelectionActivity.videosList,
                        object : HSShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(HSDataSelectionActivity.videosList.size == HSDataSelectionActivity.selectedVideosList.size)
                            }


                        })
                }
                HSMyConstants.FILE_TYPE_AUDIOS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = HSAdapterGeneralShowItems(
                        this@HSActivityViewAll,
                        HSDataSelectionActivity.audiosList,
                        object : HSShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(HSDataSelectionActivity.audiosList.size == HSDataSelectionActivity.selectedAudiosList.size)
                            }

                        })
                }
                HSMyConstants.FILE_TYPE_DOCS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = HSAdapterGeneralShowItems(
                        this@HSActivityViewAll,
                        HSDataSelectionActivity.docsList,
                        object : HSShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(HSDataSelectionActivity.docsList.size == HSDataSelectionActivity.selectedDocsList.size)
                            }

                        })


                }
            }
            binding.cbSelectAll.setOnCheckedChangeListener(checkBoxListener)


            binding.rvFiles.layoutManager = GridLayoutManager(this@HSActivityViewAll, 3)
            binding.rvFiles.adapter = HSAdapter
        }
    }
}