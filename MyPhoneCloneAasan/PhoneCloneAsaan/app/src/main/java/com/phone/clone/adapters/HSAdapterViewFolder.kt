package com.phone.clone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phone.clone.activity.HSShowItemsForSelection
import com.phone.clone.constants.HSMyConstants
import com.phone.clone.databinding.RowFolderViewBinding
import com.phone.clone.modelclasses.HSFileSharingModel
import com.phone.clone.modelclasses.HSMediaModelClass

class HSAdapterViewFolder(

) : RecyclerView.Adapter<HSAdapterViewFolder.ViewHolder>() {
    lateinit var listOfKey: Array<String>
    lateinit var map: HashMap<String, ArrayList<*>>
    lateinit var context: Context
    lateinit var callBack: HSShowItemsForSelection.ShowItemsCallBack

    constructor(
        context: Context,
        map: HashMap<String, ArrayList<HSMediaModelClass>>,
        callBack: HSShowItemsForSelection.ShowItemsCallBack
    ) : this() {
        listOfKey = map.keys.toTypedArray()
        this.map = map as HashMap<String, ArrayList<*>>
        this.context = context
        this.callBack = callBack
    }

    constructor(
        map: HashMap<String, ArrayList<HSFileSharingModel>>, context: Context,
        callBack: HSShowItemsForSelection.ShowItemsCallBack
    ) : this() {
        listOfKey = map.keys.toTypedArray()
        this.map = map as HashMap<String, ArrayList<*>>
        this.context = context
        this.callBack = callBack
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HSAdapterViewFolder.ViewHolder {
        val binding =
            RowFolderViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HSAdapterViewFolder.ViewHolder, position: Int) {
        with(holder) {
            binding.tvFolderName.text = listOfKey[position]

            val adapter = HSAdapterGeneralShowItems(
                context,
                map[listOfKey[position]]!!,
                object : HSShowItemsForSelection.ShowItemsCallBack {
                    override fun itemSelectionChanged(
                        fileType: String,
                        isChecked: Boolean,
                        row: Any
                    ) {
                        callBack.itemSelectionChanged(
                            HSAdapterGeneralShowItems.FILE_TYPE,
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
        if (a[0] is HSMediaModelClass) {
            var arr = a as ArrayList<HSMediaModelClass>
            when (HSAdapterGeneralShowItems.FILE_TYPE) {
                HSMyConstants.FILE_TYPE_VIDEOS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            HSAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
                HSMyConstants.FILE_TYPE_PICS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            HSAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
            }

        } else if (a[0] is HSFileSharingModel) {
            var arr = a as ArrayList<HSFileSharingModel>
            when (HSAdapterGeneralShowItems.FILE_TYPE) {
                HSMyConstants.FILE_TYPE_AUDIOS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            HSAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
                HSMyConstants.FILE_TYPE_DOCS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            HSAdapterGeneralShowItems.FILE_TYPE,
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
        if (a[0] is HSMediaModelClass) {
            var arr = a as ArrayList<HSMediaModelClass>
            for (f in arr) {
                if (!f.isSelected) {
                    return false
                }
            }
            return true
        } else if (a[0] is HSFileSharingModel) {
            var arr = a as ArrayList<HSFileSharingModel>
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

    class ViewHolder(val binding: RowFolderViewBinding) : RecyclerView.ViewHolder(binding.root) {
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