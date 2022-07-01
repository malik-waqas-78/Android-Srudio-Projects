package com.phone.clone.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phone.clone.activity.HSDataSelectionActivity
import com.phone.clone.activity.HSShowItemsForSelection
import com.phone.clone.constants.HSMyConstants
import com.phone.clone.databinding.RowSelectionDataBinding
import com.phone.clone.modelclasses.HSFilesToShareModel

class HSAdapterSelection(var context: Context, var list: ArrayList<HSFilesToShareModel>, var callBack:HSDataSelectionActivity.SelectionCallBack) :
    RecyclerView.Adapter<HSAdapterSelection.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HSAdapterSelection.ViewHolder {
        val binding =
            RowSelectionDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HSAdapterSelection.ViewHolder, position: Int) {
        with(holder) {
            var row = list[position]
            binding.tvFileType.text = row.fileType
            binding.tvSize.text = row.sizeDetails
            binding.tvTotaItems.text = row.itemsCountDetails
            Glide.with(context).load(row.icon).into(binding.imageView5)
            binding.cbSelectalliems.setOnCheckedChangeListener(null)
            binding.cbSelectalliems.isChecked=row.isSelected
            binding.clExpend.visibility=View.GONE
            /*binding.tvSelected.text=row.selectionDetails
            binding.tvSelectedTotal.text=row.selectionDetailsTotal*/
            binding.cbSelectalliems.setOnCheckedChangeListener { buttonView, isChecked ->
                callBack.itemSelectionChanged(row.fileType,isChecked)
            }

            val adapter=HSLimitedFilesAdapter(context,row.innerList,object:HSShowItemsForSelection.ShowItemsCallBack{
                override fun itemSelectionChanged(fileType: String, isChecked: Boolean, _row: Any) {
                    callBack.itemSelectionChanged(
                        HSAdapterGeneralShowItems.FILE_TYPE,
                        isChecked
                    )
                }

            },row.fileType)
            binding.rvLimitedFiles.layoutManager=object:LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false){
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }

            binding.rvLimitedFiles.adapter=adapter
        }
    }

    override fun getItemCount(): Int {
     return list.size
    }

    inner class ViewHolder(val binding: RowSelectionDataBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.viewClicked.setOnClickListener {
               if(adapterPosition!=RecyclerView.NO_POSITION){
                   if(list[adapterPosition].fileType==HSMyConstants.FILE_TYPE_VIDEOS||
                       list[adapterPosition].fileType==HSMyConstants.FILE_TYPE_PICS||
                       list[adapterPosition].fileType==HSMyConstants.FILE_TYPE_AUDIOS||
                       list[adapterPosition].fileType==HSMyConstants.FILE_TYPE_DOCS){
                       if(binding.clExpend.visibility==View.GONE){
                           binding.clExpend.visibility= View.VISIBLE
                       }else{
                           binding.clExpend.visibility=View.GONE
                       }
                   }else{
                       callBack.openItem(list[adapterPosition].fileType,list[adapterPosition].isSelected)

                   }
               }
            }
            binding.btnSeeAll.setOnClickListener {
                if(adapterPosition!=RecyclerView.NO_POSITION){
                    callBack.openItem(list[adapterPosition].fileType,list[adapterPosition].isSelected)
                }
            }
        }
    }
}