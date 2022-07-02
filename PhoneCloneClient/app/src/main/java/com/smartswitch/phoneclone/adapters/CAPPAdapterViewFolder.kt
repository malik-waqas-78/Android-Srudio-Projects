package com.smartswitch.phoneclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartswitch.phoneclone.activities.CAPPShowItemsForSelection
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.databinding.RowFolderViewCappBinding
import com.smartswitch.phoneclone.modelclasses.CAPPFileSharingModel
import com.smartswitch.phoneclone.modelclasses.CAPPMediaModelClass

class CAPPAdapterViewFolder(

) : RecyclerView.Adapter<CAPPAdapterViewFolder.ViewHolder>() {
    lateinit var listOfKey: Array<String>
    lateinit var map: HashMap<String, ArrayList<*>>
    lateinit var context: Context
    lateinit var callBack: CAPPShowItemsForSelection.ShowItemsCallBack

    constructor(
        context: Context,
        map: HashMap<String, ArrayList<CAPPMediaModelClass>>,
        callBack: CAPPShowItemsForSelection.ShowItemsCallBack
    ) : this() {
        listOfKey = map.keys.toTypedArray()
        this.map = map as HashMap<String, ArrayList<*>>
        this.context = context
        this.callBack = callBack
    }

    constructor(
        map: HashMap<String, ArrayList<CAPPFileSharingModel>>, context: Context,
        callBack: CAPPShowItemsForSelection.ShowItemsCallBack
    ) : this() {
        listOfKey = map.keys.toTypedArray()
        this.map = map as HashMap<String, ArrayList<*>>
        this.context = context
        this.callBack = callBack
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CAPPAdapterViewFolder.ViewHolder {
        val binding =
            RowFolderViewCappBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CAPPAdapterViewFolder.ViewHolder, position: Int) {
        with(holder) {
            binding.tvFolderName.text = listOfKey[position]

            val adapter = CAPPAdapterGeneralShowItems(
                context,
                map[listOfKey[position]]!!,
                object : CAPPShowItemsForSelection.ShowItemsCallBack {
                    override fun itemSelectionChanged(
                        fileType: String,
                        isChecked: Boolean,
                        row: Any
                    ) {
                        callBack.itemSelectionChanged(
                            CAPPAdapterGeneralShowItems.FILE_TYPE,
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
        if (a[0] is CAPPMediaModelClass) {
            var arr = a as ArrayList<CAPPMediaModelClass>
            when (CAPPAdapterGeneralShowItems.FILE_TYPE) {
                CAPPMConstants.FILE_TYPE_VIDEOS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            CAPPAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
                CAPPMConstants.FILE_TYPE_PICS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            CAPPAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
            }

        } else if (a[0] is CAPPFileSharingModel) {
            var arr = a as ArrayList<CAPPFileSharingModel>
            when (CAPPAdapterGeneralShowItems.FILE_TYPE) {
                CAPPMConstants.FILE_TYPE_AUDIOS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            CAPPAdapterGeneralShowItems.FILE_TYPE,
                            isChecked,
                            f
                        )
                    }
                }
                CAPPMConstants.FILE_TYPE_DOCS -> {
                    for (f in arr) {
                        f.isSelected = isChecked
                        callBack.itemSelectionChanged(
                            CAPPAdapterGeneralShowItems.FILE_TYPE,
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
        if (a[0] is CAPPMediaModelClass) {
            var arr = a as ArrayList<CAPPMediaModelClass>
            for (f in arr) {
                if (!f.isSelected) {
                    return false
                }
            }
            return true
        } else if (a[0] is CAPPFileSharingModel) {
            var arr = a as ArrayList<CAPPFileSharingModel>
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

    class ViewHolder(val binding: RowFolderViewCappBinding) : RecyclerView.ViewHolder(binding.root) {
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