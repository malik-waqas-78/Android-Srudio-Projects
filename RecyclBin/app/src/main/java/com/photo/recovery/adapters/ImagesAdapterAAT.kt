package com.photo.recovery.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.photo.recovery.activites.ShowImageAAT
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.databinding.RowGeneralMediaAatBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImagesAdapterAAT(var context: Context, var list: ArrayList<File>) :
    RecyclerView.Adapter<ImagesAdapterAAT.ViewHolder>() {
    var selectionCallBack = context as SelectionCallBackAAT
    var selectedList = ArrayList<File>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowGeneralMediaAatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            Glide.with(context).load(list[position]).into(holder.binding.ivIcon)
            binding.ivIcon.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.ivIcon.setPadding(0, 0, 0, 0)
            binding.tvFileName.text = list[position].name
            binding.tvDate.text = list[position].getDateInHumanFormat()
            binding.cbIsSelected.setOnCheckedChangeListener(null)
            binding.cbIsSelected.isChecked = selectedList.contains(list[position])
            //  binding.clBtmView.setBackgroundColor(context.resources.getColor(R.color.purple))
            binding.cbIsSelected.setOnCheckedChangeListener { buttonView, isChecked ->
                if (selectedList.contains(list[position])) {
                    selectedList.remove(list[position])
                } else {
                    selectedList.add(list[position])
                }
                selectionCallBack.selectionChanged(selectedList.size)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: RowGeneralMediaAatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivIcon.setOnClickListener {
                if(adapterPosition!=RecyclerView.NO_POSITION)
                    ShowImageAAT.position=adapterPosition
                val intent = Intent(context, ShowImageAAT::class.java)
                context.startActivity(intent)

            }
        }
    }

    private fun File.getDateInHumanFormat(): String {
        // Create a DateFormatter object for displaying date in specified format.
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat("dd/MM/yyyy")

        // Create a calendar object that will convert the date and time value in milliseconds to date.

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = this.lastModified()
        return formatter.format(calendar.time)
    }
}