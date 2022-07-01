package com.zak.clone.zakadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zak.clone.zakactivity.ZakShowItemsForSelection
import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.databinding.ZakRowFolderViewBinding
import com.zak.clone.zakmodelclasses.ZakFileSharingModel
import com.zak.clone.zakmodelclasses.ZakMediaModelClass

class ZakAdapterViewFolder(

) : RecyclerView.Adapter<ZakAdapterViewFolder.ViewHolder>() {
    lateinit var listOfKey: Array<String>
    lateinit var map: HashMap<String, ArrayList<*>>
    lateinit var context: Context
    lateinit var callBack: ZakShowItemsForSelection.ShowItemsCallBack

    constructor(
        context: Context,
        map: HashMap<String, ArrayList<ZakMediaModelClass>>,
        callBack: ZakShowItemsForSelection.ShowItemsCallBack
    ) : this() {
        listOfKey = map.keys.toTypedArray()
        this.map = map as HashMap<String, ArrayList<*>>
        this.context = context
        this.callBack = callBack
    }

    constructor(
        map: HashMap<String, ArrayList<ZakFileSharingModel>>, context: Context,
        callBack: ZakShowItemsForSelection.ShowItemsCallBack
    ) : this() {
        listOfKey = map.keys.toTypedArray()
        this.map = map as HashMap<String, ArrayList<*>>
        this.context = context
        this.callBack = callBack
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ZakAdapterViewFolder.ViewHolder {
        val binding =
            ZakRowFolderViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ZakAdapterViewFolder.ViewHolder, position: Int) {
        with(holder) {
            binding.tvFolderName.text = listOfKey[position]

            val adapter = ZakAdapterGeneralShowItems(
                context,
                map[listOfKey[position]]!!,
                object : ZakShowItemsForSelection.ShowItemsCallBack {
                    override fun itemSelectionChanged(
                        fileType: String,
                        isChecked: Boolean,
                        row: Any
                    ) {
                        callBack.itemSelectionChanged(
                            ZakAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            row
                        )
                        if (!isChecked) {
                            binding.cbSelectFolder.setOnCheckedChangeListener(null)
                            binding.cbSelectFolder.isChecked = false
                            binding.cbSelectFolder.setOnCheckedChangeListener { buttonView, isChecked ->
                                selectAllFiles(position, map[listOfKey[position]]!!, isChecked)
                            }
                        } else {
                            binding.cbSelectFolder.setOnCheckedChangeListener(null)
                            binding.cbSelectFolder.isChecked =
                                manageSelection(map[listOfKey[position]]!!)
                            binding.cbSelectFolder.setOnCheckedChangeListener { buttonView, isChecked ->
                                selectAllFiles(position, map[listOfKey[position]]!!, isChecked)
                            }
                        }

                    }
                })

            binding.cbSelectFolder.setOnCheckedChangeListener(null)
            binding.cbSelectFolder.isChecked = manageSelection(map[listOfKey[position]]!!)
            binding.cbSelectFolder.setOnCheckedChangeListener { buttonView, isChecked ->
                selectAllFiles(position, map[listOfKey[position]]!!, isChecked)
            }

            binding.rvFiles.layoutManager=GridLayoutManager(context,3)
            binding.rvFiles.adapter=adapter

        }
    }

    private fun selectAllFiles(position: Int, a: ArrayList<*>, isChecked: Boolean) {
        if (a[0] is ZakMediaModelClass) {
            var arr = a as ArrayList<ZakMediaModelClass>
            when (ZakAdapterGeneralShowItems.FILE_TYPE) {
                ZakMyConstants.FILE_TYPE_VIDEOS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            ZakAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
                ZakMyConstants.FILE_TYPE_PICS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            ZakAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
            }

        } else if (a[0] is ZakFileSharingModel) {
            var arr = a as ArrayList<ZakFileSharingModel>
            when (ZakAdapterGeneralShowItems.FILE_TYPE) {
                ZakMyConstants.FILE_TYPE_AUDIOS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            ZakAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
                ZakMyConstants.FILE_TYPE_DOCS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            ZakAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun manageSelection(a: ArrayList<*>): Boolean {
        if (a[0] is ZakMediaModelClass) {
            var arr = a as ArrayList<ZakMediaModelClass>
            for (f in arr) {
                if (!f.isSelected) {
                    return false
                }
            }
            return true
        } else if (a[0] is ZakFileSharingModel) {
            var arr = a as ArrayList<ZakFileSharingModel>
            for (f in arr) {
                if (!f.isSelected) {
                    return false
                }
            }
            return true
        }
        return false
    }

    override fun getItemCount(): Int {
        return map.size
    }

    class ViewHolder(val binding: ZakRowFolderViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewClick.setOnClickListener {

                binding.rvFiles.visibility = if(binding.rvFiles.isVisible){
                    View.GONE
                }else{
                    View.VISIBLE
                }
            }
        }
    }

}