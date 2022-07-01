package com.phoneclone.data.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phoneclone.data.activity.SelectionActivity
import com.phoneclone.data.databinding.RowSelectionDataBinding
import com.phoneclone.data.modelclasses.FilesToShareModel

class AdapterSelection(var context: Context, var list: ArrayList<FilesToShareModel>, var callBack:SelectionActivity.SelectionCallBack) :
    RecyclerView.Adapter<AdapterSelection.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSelection.ViewHolder {
        val binding =
            RowSelectionDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterSelection.ViewHolder, position: Int) {
        with(holder) {
            var row = list[position]
            binding.tvFileType.text = row.fileType
            binding.tvSize.text = row.sizeDetails
            binding.tvTotaItems.text = row.itemsCountDetails
            Glide.with(context).load(row.icon).into(binding.imageView5)
            binding.cbSelectalliems.setOnCheckedChangeListener(null)
            binding.cbSelectalliems.isChecked=row.isSelected
            binding.tvSelected.text=row.selectionDetails
            binding.tvSelectedTotal.text=row.selectionDetailsTotal
            binding.cbSelectalliems.setOnCheckedChangeListener { buttonView, isChecked ->
                callBack.itemSelectionChanged(row.fileType,isChecked)
            }
        }
    }

    override fun getItemCount(): Int {
     return list.size
    }

    inner class ViewHolder(val binding: RowSelectionDataBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.viewClicked.setOnClickListener {
                callBack.openItem(list[adapterPosition].fileType,list[adapterPosition].isSelected)
            }
        }
    }
}