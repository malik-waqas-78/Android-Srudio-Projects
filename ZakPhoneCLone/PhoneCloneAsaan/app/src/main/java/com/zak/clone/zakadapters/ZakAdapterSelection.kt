package com.zak.clone.zakadapters


import android.content.Context
import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zak.clone.zakactivity.ZakDataSelectionActivity
import com.zak.clone.zakactivity.ZakShowItemsForSelection
import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.databinding.ZakRowSelectionDataBinding
import com.zak.clone.zakmodelclasses.ZAKFilesToShareModel

class ZakAdapterSelection(var context: Context, var list: ArrayList<ZAKFilesToShareModel>, var callBack:ZakDataSelectionActivity.SelectionCallBack) :
    RecyclerView.Adapter<ZakAdapterSelection.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZakAdapterSelection.ViewHolder {
        val binding =
            ZakRowSelectionDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ZakAdapterSelection.ViewHolder, position: Int) {
        with(holder) {
            var row = list[position]
            binding.tvFileType.text = row.fileType
            binding.tvSize.text = row.sizeDetails
            binding.tvTotaItems.text = row.itemsCountDetails
            Glide.with(context).load(row.icon).into(binding.imageView5)
            binding.cbSelectalliems.setOnCheckedChangeListener(null)
            binding.cbSelectalliems.isChecked=row.isSelected
            /*binding.tvSelected.text=row.selectionDetails
            binding.tvSelectedTotal.text=row.selectionDetailsTotal*/
            binding.cbSelectalliems.setOnCheckedChangeListener { buttonView, isChecked ->
                callBack.itemSelectionChanged(row.fileType,isChecked)
            }

            val adapter=ZakLimitedFilesAdapter(context,row.innerList,object:ZakShowItemsForSelection.ShowItemsCallBack{
                override fun itemSelectionChanged(fileType: String, isChecked: Boolean, _row: Any) {
                    callBack.itemSelectionChanged(
                        ZakAdapterGeneralShowItems.FILE_TYPE,
                        isChecked
                    )
                }

            },row.fileType)
        }
    }

    override fun getItemCount(): Int {
     return list.size
    }

    inner class ViewHolder(val binding: ZakRowSelectionDataBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.viewClicked.setOnClickListener {
               if(adapterPosition!=RecyclerView.NO_POSITION){
                   if(list[adapterPosition].fileType==ZakMyConstants.FILE_TYPE_VIDEOS||
                       list[adapterPosition].fileType==ZakMyConstants.FILE_TYPE_PICS||
                       list[adapterPosition].fileType==ZakMyConstants.FILE_TYPE_AUDIOS||
                       list[adapterPosition].fileType==ZakMyConstants.FILE_TYPE_DOCS){
                       callBack.openItem(list[adapterPosition].fileType,list[adapterPosition].isSelected)
                   }else{
                       callBack.openItem(list[adapterPosition].fileType,list[adapterPosition].isSelected)

                   }
               }
            }
        }
    }
}