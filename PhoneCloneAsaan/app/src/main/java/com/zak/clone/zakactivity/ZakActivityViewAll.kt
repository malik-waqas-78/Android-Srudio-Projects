package com.zak.clone.zakactivity

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.GridLayoutManager
import com.zak.clone.zakadapters.ZakAdapterGeneralShowItems
import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.databinding.ZakActivitySeeMoreBinding
import com.zak.clone.zakmodelclasses.ZakFileSharingModel
import com.zak.clone.zakmodelclasses.ZakMediaModelClass

class ZakActivityViewAll : AppCompatActivity() {
    lateinit var binding: ZakActivitySeeMoreBinding
    var arrayList = ArrayList<Any>()
    var FILE_TYPE: String = ""


    var HSAdapter: ZakAdapterGeneralShowItems? = null

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

    var isSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ZakActivitySeeMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FILE_TYPE = intent.getStringExtra(ZakMyConstants.FILE_TYPE)
        isSelected = intent.getBooleanExtra(ZakMyConstants.IS_SELECTED, false)

        ZakAdapterGeneralShowItems.FILE_TYPE = FILE_TYPE

       binding.tvFileType.text=FILE_TYPE

        binding.btnBack.setOnClickListener {
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
                ZakMyConstants.FILE_TYPE_DOCS -> {
                    for(item in ZakDataSelectionActivity.docsList){
                        item.isSelected=isSelected
                    }
                }
                ZakMyConstants.FILE_TYPE_AUDIOS -> {
                    for(item in ZakDataSelectionActivity.audiosList){
                        item.isSelected=isSelected
                    }
                }
                ZakMyConstants.FILE_TYPE_VIDEOS -> {
                    for(item in ZakDataSelectionActivity.videosList){
                        item.isSelected=isSelected
                    }
                }
                ZakMyConstants.FILE_TYPE_PICS -> {
                    for(item in ZakDataSelectionActivity.imagesList){
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
                ZakMyConstants.FILE_TYPE_PICS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = ZakAdapterGeneralShowItems(
                        this@ZakActivityViewAll,
                        ZakDataSelectionActivity.imagesList,
                        object : ZakShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(ZakDataSelectionActivity.imagesList.size == ZakDataSelectionActivity.selectedImagesList.size)
                            }

                        })
                }
                ZakMyConstants.FILE_TYPE_VIDEOS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = ZakAdapterGeneralShowItems(
                        this@ZakActivityViewAll,
                        ZakDataSelectionActivity.videosList,
                        object : ZakShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(ZakDataSelectionActivity.videosList.size == ZakDataSelectionActivity.selectedVideosList.size)
                            }


                        })
                }
                ZakMyConstants.FILE_TYPE_AUDIOS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = ZakAdapterGeneralShowItems(
                        this@ZakActivityViewAll,
                        ZakDataSelectionActivity.audiosList,
                        object : ZakShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(ZakDataSelectionActivity.audiosList.size == ZakDataSelectionActivity.selectedAudiosList.size)
                            }

                        })
                }
                ZakMyConstants.FILE_TYPE_DOCS -> {
                    setCheckBoxState(isSelected)
                    HSAdapter = ZakAdapterGeneralShowItems(
                        this@ZakActivityViewAll,
                        ZakDataSelectionActivity.docsList,
                        object : ZakShowItemsForSelection.ShowItemsCallBack {
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
                                setCheckBoxState(ZakDataSelectionActivity.docsList.size == ZakDataSelectionActivity.selectedDocsList.size)
                            }

                        })


                }
            }
            binding.cbSelectAll.setOnCheckedChangeListener(checkBoxListener)


            binding.rvFiles.layoutManager = GridLayoutManager(this@ZakActivityViewAll, 3)
            binding.rvFiles.adapter = HSAdapter
        }
    }
}