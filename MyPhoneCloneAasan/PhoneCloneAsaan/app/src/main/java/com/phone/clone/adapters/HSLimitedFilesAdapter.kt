package com.phone.clone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phone.clone.R
import com.phone.clone.activity.HSShowItemsForSelection
import com.phone.clone.constants.HSMyConstants
import com.phone.clone.databinding.*
import com.phone.clone.modelclasses.HSFileSharingModel
import com.phone.clone.modelclasses.HSMediaModelClass
import com.bumptech.glide.Glide

class HSLimitedFilesAdapter (var context: Context, var list: ArrayList<*>, var callBack: HSShowItemsForSelection.ShowItemsCallBack, var FILE_TYPE:String) : RecyclerView.Adapter<HSLimitedFilesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HSLimitedFilesAdapter.ViewHolder {
        when (FILE_TYPE) {
            HSMyConstants.FILE_TYPE_PICS -> {
                return ViewHolder(
                    RowPvUnscolBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            HSMyConstants.FILE_TYPE_VIDEOS -> {
                return ViewHolder(
                    RowPvUnscolBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            HSMyConstants.FILE_TYPE_AUDIOS -> {
                return ViewHolder(
                    RowAdUnscrollBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            HSMyConstants.FILE_TYPE_DOCS -> {
                return ViewHolder(
                    RowAdUnscrollBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            }
            else -> {
                return ViewHolder(
                    RowAdUnscrollBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    }


    override fun onBindViewHolder(holder: HSLimitedFilesAdapter.ViewHolder, position: Int) {
        with(holder) {
            when (FILE_TYPE) {

                HSMyConstants.FILE_TYPE_PICS -> {
                    val row = list[position] as HSMediaModelClass
                    val _binding = binding as RowPvUnscolBinding
                    _binding.tvSize.text = row.sizeInFormat
                    Glide.with(context).load(row.path).into(_binding.ivIcon)
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
                HSMyConstants.FILE_TYPE_VIDEOS -> {
                    val row = list[position] as HSMediaModelClass
                    val _binding = binding as RowPvUnscolBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.ivVideoPlayButton.visibility = View.VISIBLE
                    Glide.with(context).load(row.path).into(_binding.ivIcon)
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
                HSMyConstants.FILE_TYPE_AUDIOS -> {
                    val row = list[position] as HSFileSharingModel
                    val _binding = binding as RowAdUnscrollBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.ic_audio)
                    _binding.tvName.isSelected = true
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
                HSMyConstants.FILE_TYPE_DOCS -> {
                    val row = list[position] as HSFileSharingModel
                    val _binding = binding as RowAdUnscrollBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.ic_doc)
                    _binding.tvName.isSelected = true
                   _binding.cbSelection.visibility=View.INVISIBLE
                }
                else -> {
                    val row = list[position] as HSFileSharingModel
                    val _binding = binding as RowAdUnscrollBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.ic_audio)
                    _binding.tvName.isSelected = true
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var binding: Any

        constructor(_binding: RowPvUnscolBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowAdUnscrollBinding) : this(_binding.root) {
            binding = _binding
        }
    }
}