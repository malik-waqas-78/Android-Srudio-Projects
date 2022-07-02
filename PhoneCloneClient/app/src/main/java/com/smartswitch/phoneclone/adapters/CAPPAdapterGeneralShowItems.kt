package com.smartswitch.phoneclone.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartswitch.phoneclone.R
//import com.zentic.clonemyphone.R
import com.smartswitch.phoneclone.activities.CAPPShowItemsForSelection
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.databinding.*
import com.smartswitch.phoneclone.modelclasses.CAPPAppsModel
import com.smartswitch.phoneclone.modelclasses.CAPPContactsModel
import com.smartswitch.phoneclone.modelclasses.CAPPFileSharingModel
import com.smartswitch.phoneclone.modelclasses.CAPPMediaModelClass
import java.util.*


class CAPPAdapterGeneralShowItems(var context: Context, var list: ArrayList<*>, var callBack: CAPPShowItemsForSelection.ShowItemsCallBack) : RecyclerView.Adapter<CAPPAdapterGeneralShowItems.ViewHolder>() {

    companion object {
        var FILE_TYPE = ""
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): CAPPAdapterGeneralShowItems.ViewHolder {
        when (FILE_TYPE) {
            CAPPMConstants.FILE_TYPE_CONTACTS -> {
                return ViewHolder(RowContactsCappBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            CAPPMConstants.FILE_TYPE_PICS -> {
                return ViewHolder(RowVideosPicsCappBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            CAPPMConstants.FILE_TYPE_VIDEOS -> {
                return ViewHolder(RowVideosPicsCappBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            CAPPMConstants.FILE_TYPE_APPS -> {
                return ViewHolder(RowAppsCappBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            CAPPMConstants.FILE_TYPE_AUDIOS -> {
                return ViewHolder(RowGenericSelectionCappBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            CAPPMConstants.FILE_TYPE_DOCS -> {
                return ViewHolder(RowGenericSelectionCappBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            }
            else -> {
                return ViewHolder(RowContactsCappBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

    }


    override fun onBindViewHolder(holder: CAPPAdapterGeneralShowItems.ViewHolder, position: Int) {
        with(holder) {
            when (FILE_TYPE) {
                CAPPMConstants.FILE_TYPE_CONTACTS -> {
                    val row = list[position] as CAPPContactsModel
                    val _binding = binding as RowContactsCappBinding
                    _binding.tvContactName.text = row.name
                    _binding.tvPhonenumber.text = row.phoneNumber
                    Glide.with(context).load(CAPPContactsModel.icon).into(_binding.ivIcon)
                    _binding.cbSelection.setOnCheckedChangeListener(null)
                    _binding.cbSelection.isChecked = row.isSelected
                    _binding.cbSelection.setOnCheckedChangeListener { _, isChecked ->
                        row.isSelected = isChecked
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, isChecked, row)
                    }

                    _binding.root.setOnClickListener {
                        row.isSelected = !row.isSelected
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, row.isSelected, row)
                    }
                }
                CAPPMConstants.FILE_TYPE_PICS -> {
                    val row = list[position] as CAPPMediaModelClass
                    val _binding = binding as RowVideosPicsCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.imagevideoname.text=row.displayName
                    Glide.with(context).load(row.path).into(_binding.ivIcon)
                    _binding.cbSelection.setOnCheckedChangeListener(null)
                    _binding.cbSelection.isChecked = row.isSelected
                    _binding.cbSelection.setOnCheckedChangeListener { _, isChecked ->
                        row.isSelected = isChecked
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, isChecked, row)
                    }
                    _binding.root.setOnClickListener {
                        row.isSelected = !row.isSelected
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, row.isSelected, row)
                    }
                }
                CAPPMConstants.FILE_TYPE_VIDEOS -> {
                    val row = list[position] as CAPPMediaModelClass
                    val _binding = binding as RowVideosPicsCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.imagevideoname.text=row.displayName
                    _binding.ivVideoPlayButton.visibility = View.VISIBLE
                    Glide.with(context).load(row.path).into(_binding.ivIcon)
                    _binding.cbSelection.setOnCheckedChangeListener(null)
                    _binding.cbSelection.isChecked = row.isSelected
                    _binding.cbSelection.setOnCheckedChangeListener { _, isChecked ->
                        row.isSelected = isChecked
                        callBack.itemSelectionChanged(FILE_TYPE, isChecked, row)
                        notifyItemChanged(position)
                    }
                    _binding.root.setOnClickListener {
                        row.isSelected = !row.isSelected
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, row.isSelected, row)
                    }
                }
                CAPPMConstants.FILE_TYPE_APPS -> {
                    val row = list[position] as CAPPAppsModel
                    val _binding = binding as RowAppsCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.apkName
                    Glide.with(context).load(row.icon).into(_binding.ivIcon)
                    _binding.cbSelection.setOnCheckedChangeListener(null)
                    _binding.cbSelection.isChecked = row.isSelected
                    _binding.cbSelection.setOnCheckedChangeListener { _, isChecked ->
                        row.isSelected = isChecked
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, isChecked, row)
                    }
                    _binding.root.setOnClickListener {
                        row.isSelected = !row.isSelected
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, row.isSelected, row)
                    }
                }
                CAPPMConstants.FILE_TYPE_AUDIOS -> {
                    val row = list[position] as CAPPFileSharingModel
                    val _binding = binding as RowGenericSelectionCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.audio)
                    _binding.tvName.isSelected = true
                    _binding.cbSelection.setOnCheckedChangeListener(null)
                    _binding.cbSelection.isChecked = row.isSelected
                    _binding.cbSelection.setOnCheckedChangeListener { _, isChecked ->
                        row.isSelected = isChecked
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, isChecked, row)
                    }
                    _binding.root.setOnClickListener {
                        row.isSelected = !row.isSelected
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, row.isSelected, row)
                    }
                }
                CAPPMConstants.FILE_TYPE_DOCS -> {
                    val row = list[position] as CAPPFileSharingModel
                    val _binding = binding as RowGenericSelectionCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.docs)
                    _binding.tvName.isSelected = true
                    _binding.cbSelection.setOnCheckedChangeListener(null)
                    _binding.cbSelection.isChecked = row.isSelected
                    _binding.cbSelection.setOnCheckedChangeListener { _, isChecked ->
                        row.isSelected = isChecked
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, isChecked, row)
                    }
                    _binding.root.setOnClickListener {
                        row.isSelected = !row.isSelected
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, row.isSelected, row)
                    }
                }
                else -> {
                    val row = list[position] as CAPPContactsModel
                    val _binding = binding as RowContactsCappBinding
                    _binding.tvContactName.text = row.name
                    _binding.tvPhonenumber.text = row.phoneNumber
                    _binding.cbSelection.setOnCheckedChangeListener(null)
                    _binding.cbSelection.isChecked = row.isSelected
                    _binding.cbSelection.setOnCheckedChangeListener { _, isChecked ->
                        row.isSelected = isChecked
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, isChecked, row)
                    }
                    _binding.root.setOnClickListener {
                        row.isSelected = !row.isSelected
                        notifyItemChanged(position)
                        callBack.itemSelectionChanged(FILE_TYPE, row.isSelected, row)
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var binding: Any

        constructor(_binding: RowContactsCappBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowAppsCappBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowVideosPicsCappBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowGenericSelectionCappBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowDocsCappBinding) : this(_binding.root) {
            binding = _binding
        }
    }

}