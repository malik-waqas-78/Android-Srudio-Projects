package com.smartswitch.phoneclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.activities.CAPPShowItemsForSelection
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.databinding.*
import com.smartswitch.phoneclone.modelclasses.CAPPFileSharingModel
import com.smartswitch.phoneclone.modelclasses.CAPPMediaModelClass
import com.bumptech.glide.Glide

class CAPPLimitedFilesAdapter (var context: Context, var list: ArrayList<*>, var callBack: CAPPShowItemsForSelection.ShowItemsCallBack, var FILE_TYPE:String) : RecyclerView.Adapter<CAPPLimitedFilesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CAPPLimitedFilesAdapter.ViewHolder {
        when (FILE_TYPE) {
            CAPPMConstants.FILE_TYPE_PICS -> {
                return ViewHolder(
                    RowPvUnscolCappBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            CAPPMConstants.FILE_TYPE_VIDEOS -> {
                return ViewHolder(
                    RowPvUnscolCappBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            CAPPMConstants.FILE_TYPE_AUDIOS -> {
                return ViewHolder(
                    RowAdUnscrollCappBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            CAPPMConstants.FILE_TYPE_DOCS -> {
                return ViewHolder(
                    RowAdUnscrollCappBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            }
            else -> {
                return ViewHolder(
                    RowAdUnscrollCappBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    }


    override fun onBindViewHolder(holder: CAPPLimitedFilesAdapter.ViewHolder, position: Int) {
        with(holder) {
            when (FILE_TYPE) {

                CAPPMConstants.FILE_TYPE_PICS -> {
                    val row = list[position] as CAPPMediaModelClass
                    val _binding = binding as RowPvUnscolCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.imagevideoname.text=row.displayName
                    Glide.with(context).load(row.path).into(_binding.ivIcon)
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
                CAPPMConstants.FILE_TYPE_VIDEOS -> {
                    val row = list[position] as CAPPMediaModelClass
                    val _binding = binding as RowPvUnscolCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.imagevideoname.text=row.displayName
                    _binding.ivVideoPlayButton.visibility = View.VISIBLE
                    Glide.with(context).load(row.path).into(_binding.ivIcon)
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
                CAPPMConstants.FILE_TYPE_AUDIOS -> {
                    val row = list[position] as CAPPFileSharingModel
                    val _binding = binding as RowAdUnscrollCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.audio)
                    _binding.tvName.isSelected = true
                    _binding.cbSelection.visibility=View.INVISIBLE
                }
                CAPPMConstants.FILE_TYPE_DOCS -> {
                    val row = list[position] as CAPPFileSharingModel
                    val _binding = binding as RowAdUnscrollCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.docs)
                    _binding.tvName.isSelected = true
                   _binding.cbSelection.visibility=View.INVISIBLE
                }
                else -> {
                    val row = list[position] as CAPPFileSharingModel
                    val _binding = binding as RowAdUnscrollCappBinding
                    _binding.tvSize.text = row.sizeInFormat
                    _binding.tvName.text = row.fileName
                    _binding.ivIcon.setBackgroundResource(R.drawable.audio)
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

        constructor(_binding: RowPvUnscolCappBinding) : this(_binding.root) {
            binding = _binding
        }

        constructor(_binding: RowAdUnscrollCappBinding) : this(_binding.root) {
            binding = _binding
        }
    }
}