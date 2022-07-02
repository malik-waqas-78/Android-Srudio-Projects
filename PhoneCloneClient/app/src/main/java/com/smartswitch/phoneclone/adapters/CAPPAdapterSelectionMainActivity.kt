package com.smartswitch.phoneclone.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartswitch.phoneclone.activities.CAPPDataSelectionMainActivity
import com.smartswitch.phoneclone.activities.CAPPShowItemsForSelection
import com.smartswitch.phoneclone.databinding.RowSelectionDataCappBinding
//import com.phone.clonedata.databinding.RowSelectionDataBinding
import com.smartswitch.phoneclone.modelclasses.CAPPFilesToShareModel
import java.util.*

class CAPPAdapterSelectionMainActivity(
    var context: Context,
    var list: ArrayList<CAPPFilesToShareModel>,
    var callBack: CAPPDataSelectionMainActivity.SelectionCallBack
) :
    RecyclerView.Adapter<CAPPAdapterSelectionMainActivity.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CAPPAdapterSelectionMainActivity.ViewHolder {
        val binding =
            RowSelectionDataCappBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CAPPAdapterSelectionMainActivity.ViewHolder, position: Int) {
        with(holder) {
            var row = list[position]
            binding.tvFileType.text = row.fileType

            if(!(row.selectionDetails.isEmpty())){
                if(!row.selectionDetails.equals("(0")) {
                    binding.tvSize.visibility = View.GONE
                    binding.tvTotaItems.text =
                        "Selected " + row.selectionDetails + row.selectionDetailsTotal + " items"
                }else{
                    binding.tvSize.text = row.sizeDetails
                    binding.tvTotaItems.text = row.itemsCountDetails
                }
            }else{
                binding.tvSize.text = row.sizeDetails
                binding.tvTotaItems.text = row.itemsCountDetails
            }
            Glide.with(context).load(row.icon).into(binding.imageView5)
            binding.cbSelectalliems.setOnCheckedChangeListener(null)
            binding.cbSelectalliems.isChecked = row.isSelected
            binding.clExpend.visibility = View.GONE
            /*binding.tvSelected.text=row.selectionDetails
            binding.tvSelectedTotal.text=row.selectionDetailsTotal*/
            binding.cbSelectalliems.setOnCheckedChangeListener { buttonView, isChecked ->
                callBack.itemSelectionChanged(row.fileType, isChecked)
            }

            val adapter = CAPPLimitedFilesAdapter(
                context,
                row.innerList,
                object : CAPPShowItemsForSelection.ShowItemsCallBack {
                    override fun itemSelectionChanged(
                        fileType: String,
                        isChecked: Boolean,
                        _row: Any
                    ) {
                        callBack.itemSelectionChanged(
                            CAPPAdapterGeneralShowItems.FILE_TYPE,
                            isChecked
                        )
                    }

                },
                row.fileType
            )
            binding.rvLimitedFiles.layoutManager =
                object : LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }
                }

            binding.rvLimitedFiles.adapter = adapter
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: RowSelectionDataCappBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewClicked.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {

                    /*if (list[adapterPosition].fileType == HSMyConstants.FILE_TYPE_VIDEOS ||
                        list[adapterPosition].fileType == HSMyConstants.FILE_TYPE_PICS ||
                        list[adapterPosition].fileType == HSMyConstants.FILE_TYPE_AUDIOS ||
                        list[adapterPosition].fileType == HSMyConstants.FILE_TYPE_DOCS
                    ) {
                        if (binding.clExpend.visibility == View.GONE) {
                            binding.clExpend.visibility = View.VISIBLE
                        } else {
                            binding.clExpend.visibility = View.GONE
                        }
                    } else {
                        callBack.openItem(
                            list[adapterPosition].fileType,
                            list[adapterPosition].isSelected
                        )

                    }*/


                    callBack.openItem(
                        list[adapterPosition].fileType,
                        list[adapterPosition].isSelected
                    )


                }
            }
            binding.btnSeeAll.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    callBack.openItem(
                        list[adapterPosition].fileType,
                        list[adapterPosition].isSelected
                    )
                }
            }
        }
    }
}