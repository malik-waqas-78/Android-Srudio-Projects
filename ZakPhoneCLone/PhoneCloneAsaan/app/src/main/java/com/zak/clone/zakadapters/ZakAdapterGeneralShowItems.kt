package com.zak.clone.zakadapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zak.clone.R
import com.zak.clone.databinding.*
import com.zak.clone.zakactivity.ZakShowItemsForSelection
import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.zakmodelclasses.ZakAppsModel
import com.zak.clone.zakmodelclasses.ZakContactsModel
import com.zak.clone.zakmodelclasses.ZakFileSharingModel
import com.zak.clone.zakmodelclasses.ZakMediaModelClass


class ZakAdapterGeneralShowItems(var context: Context, var list: ArrayList<*>, var callBack: ZakShowItemsForSelection.ShowItemsCallBack) : RecyclerView.Adapter<ZakAdapterGeneralShowItems.ViewHolder>() {

    companion object {
        var FILE_TYPE = ""
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ZakAdapterGeneralShowItems.ViewHolder {
        when (FILE_TYPE) {
            ZakMyConstants.FILE_TYPE_CONTACTS -> {
                return ViewHolder(ZakRowContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            ZakMyConstants.FILE_TYPE_PICS -> {
                return ViewHolder(ZakRowVideosPicsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            ZakMyConstants.FILE_TYPE_VIDEOS -> {
                return ViewHolder(ZakRowVideosPicsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            ZakMyConstants.FILE_TYPE_APPS -> {
                return ViewHolder(ZakRowAppsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            ZakMyConstants.FILE_TYPE_AUDIOS -> {
                return ViewHolder(ZakRowGenericSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            ZakMyConstants.FILE_TYPE_DOCS -> {
                return ViewHolder(ZakRowGenericSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            }
            else -> {
                return ViewHolder(ZakRowContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

    }


    override fun onBindViewHolder(holder: ZakAdapterGeneralShowItems.ViewHolder, position: Int) {
        with(holder) {
            when (FILE_TYPE) {
                ZakMyConstants.FILE_TYPE_CONTACTS -> {
                    val row = list[position] as ZakContactsModel
                    val _binding = binding as ZakRowContactsBinding
                    _binding.tvContactName.text = row.name
                    _binding.tvPhonenumber.text = row.phoneNumber
                    Glide.with(context).load(ZakContactsModel.icon).into(_binding.ivIcon)
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
                ZakMyConstants.FILE_TYPE_PICS -> {
                    val row = list[position] as ZakMediaModelClass
                    val _binding = binding as ZakRowVideosPicsBinding
                    _binding.tvSize.text = row.sizeInFormat
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
                ZakMyConstants.FILE_TYPE_VIDEOS -> {
                    val row = list[position] as ZakMediaModelClass
                    val _binding = binding as ZakRowVideosPicsBinding
                    _binding.tvSize.text = row.sizeInFormat
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
                ZakMyConstants.FILE_TYPE_APPS -> {
                    val row = list[position] as ZakAppsModel
                    val _binding = binding as ZakRowAppsBinding
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
                ZakMyConstants.FILE_TYPE_AUDIOS -> {
                    val row = list[position] as ZakFileSharingModel
                    val _binding = binding as ZakRowGenericSelectionBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.ic_audio)
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
                ZakMyConstants.FILE_TYPE_DOCS -> {
                    val row = list[position] as ZakFileSharingModel
                    val _binding = binding as ZakRowGenericSelectionBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.ic_doc)
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
                    val row = list[position] as ZakContactsModel
                    val _binding = binding as ZakRowContactsBinding
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

        constructor(_binding: ZakRowContactsBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: ZakRowAppsBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: ZakRowVideosPicsBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: ZakRowGenericSelectionBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: ZakRowDocsBinding) : this(_binding.root) {
            binding = _binding
        }
    }

}