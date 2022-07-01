package com.phoneclone.data.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phoneclone.data.R
//import com.zentic.clonemyphone.R
import com.phoneclone.data.activity.ShowItemsForSelection
import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.databinding.*
import com.phoneclone.data.modelclasses.AppsModel
import com.phoneclone.data.modelclasses.ContactsModel
import com.phoneclone.data.modelclasses.FileSharingModel
import com.phoneclone.data.modelclasses.MediaModelClass


class AdapterGeneralShowItems(var context: Context, var list: ArrayList<*>, var callBack: ShowItemsForSelection.ShowItemsCallBack) : RecyclerView.Adapter<AdapterGeneralShowItems.ViewHolder>() {

    companion object {
        var FILE_TYPE = ""
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): AdapterGeneralShowItems.ViewHolder {
        when (FILE_TYPE) {
            MyConstants.FILE_TYPE_CONTACTS -> {
                return ViewHolder(RowContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            MyConstants.FILE_TYPE_PICS -> {
                return ViewHolder(RowVideosPicsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            MyConstants.FILE_TYPE_VIDEOS -> {
                return ViewHolder(RowVideosPicsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            MyConstants.FILE_TYPE_APPS -> {
                return ViewHolder(RowAppsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            MyConstants.FILE_TYPE_AUDIOS -> {
                return ViewHolder(RowGenericSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            MyConstants.FILE_TYPE_DOCS -> {
                return ViewHolder(RowGenericSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            }
            else -> {
                return ViewHolder(RowContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

    }


    override fun onBindViewHolder(holder: AdapterGeneralShowItems.ViewHolder, position: Int) {
        with(holder) {
            when (FILE_TYPE) {
                MyConstants.FILE_TYPE_CONTACTS -> {
                    val row = list[position] as ContactsModel
                    val _binding = binding as RowContactsBinding
                    _binding.tvContactName.text = row.name
                    _binding.tvPhonenumber.text = row.phoneNumber
                    Glide.with(context).load(ContactsModel.icon).into(_binding.ivIcon)
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
                MyConstants.FILE_TYPE_PICS -> {
                    val row = list[position] as MediaModelClass
                    val _binding = binding as RowVideosPicsBinding
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
                MyConstants.FILE_TYPE_VIDEOS -> {
                    val row = list[position] as MediaModelClass
                    val _binding = binding as RowVideosPicsBinding
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
                MyConstants.FILE_TYPE_APPS -> {
                    val row = list[position] as AppsModel
                    val _binding = binding as RowAppsBinding
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
                MyConstants.FILE_TYPE_AUDIOS -> {
                    val row = list[position] as FileSharingModel
                    val _binding = binding as RowGenericSelectionBinding
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
                MyConstants.FILE_TYPE_DOCS -> {
                    val row = list[position] as FileSharingModel
                    val _binding = binding as RowGenericSelectionBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.ic_docs)
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
                    val row = list[position] as ContactsModel
                    val _binding = binding as RowContactsBinding
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

        constructor(_binding: RowContactsBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowAppsBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowVideosPicsBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowGenericSelectionBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowDocsBinding) : this(_binding.root) {
            binding = _binding
        }
    }

}