package com.zak.clone.zakadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zak.clone.R
import com.zak.clone.zakactivity.ZakShowItemsForSelection
import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.databinding.*
import com.zak.clone.zakmodelclasses.ZakFileSharingModel
import com.zak.clone.zakmodelclasses.ZakMediaModelClass
import com.bumptech.glide.Glide

class ZakLimitedFilesAdapter (var context: Context, var list: ArrayList<*>, var callBack: ZakShowItemsForSelection.ShowItemsCallBack, var FILE_TYPE:String) : RecyclerView.Adapter<ZakLimitedFilesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ZakLimitedFilesAdapter.ViewHolder {
        when (FILE_TYPE) {
            ZakMyConstants.FILE_TYPE_PICS -> {
                return ViewHolder(
                    ZakRowPvUnscolBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ZakMyConstants.FILE_TYPE_VIDEOS -> {
                return ViewHolder(
                    ZakRowPvUnscolBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ZakMyConstants.FILE_TYPE_AUDIOS -> {
                return ViewHolder(
                    ZakRowAdUnscrollBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ZakMyConstants.FILE_TYPE_DOCS -> {
                return ViewHolder(
                    ZakRowAdUnscrollBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            }
            else -> {
                return ViewHolder(
                    ZakRowAdUnscrollBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    }


    override fun onBindViewHolder(holder: ZakLimitedFilesAdapter.ViewHolder, position: Int) {
        with(holder) {
            when (FILE_TYPE) {

                ZakMyConstants.FILE_TYPE_PICS -> {
                    val row = list[position] as ZakMediaModelClass
                    val _binding = binding as ZakRowPvUnscolBinding
                    _binding.tvSize.text = row.sizeInFormat
                    Glide.with(context).load(row.path).into(_binding.ivIcon)
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
                ZakMyConstants.FILE_TYPE_VIDEOS -> {
                    val row = list[position] as ZakMediaModelClass
                    val _binding = binding as ZakRowPvUnscolBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.ivVideoPlayButton.visibility = View.VISIBLE
                    Glide.with(context).load(row.path).into(_binding.ivIcon)
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
                ZakMyConstants.FILE_TYPE_AUDIOS -> {
                    val row = list[position] as ZakFileSharingModel
                    val _binding = binding as ZakRowAdUnscrollBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.ic_audio)
                    _binding.tvName.isSelected = true
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
                ZakMyConstants.FILE_TYPE_DOCS -> {
                    val row = list[position] as ZakFileSharingModel
                    val _binding = binding as ZakRowAdUnscrollBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.ic_doc)
                    _binding.tvName.isSelected = true
                   _binding.cbSelection.visibility=View.INVISIBLE
                }
                else -> {
                    val row = list[position] as ZakFileSharingModel
                    val _binding = binding as ZakRowAdUnscrollBinding
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

        constructor(_binding: ZakRowPvUnscolBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: ZakRowAdUnscrollBinding) : this(_binding.root) {
            binding = _binding
        }
    }
}