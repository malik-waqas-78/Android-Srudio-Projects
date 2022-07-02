package com.photo.recovery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.databinding.RowRecentlyDeletedAatBinding
import com.photo.recovery.models.AllFilesModelClassAAT
import java.lang.Exception

class RecentsAdapterAAT(var context: Context, var recentFiles:ArrayList<AllFilesModelClassAAT>) : RecyclerView.Adapter<RecentsAdapterAAT.ViewHolder>() ,
    Filterable {
    var selectionCallBack=context as SelectionCallBackAAT
    val filteringListFull=ArrayList(recentFiles)
    var selectedList = ArrayList<AllFilesModelClassAAT>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=RowRecentlyDeletedAatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            binding.tvDate.text=recentFiles[position].dateInFormat
            binding.tvFileName.text=recentFiles[position].fileName
            Glide.with(context).load(recentFiles[position].icon).into(binding.imageView2)
            recentFiles[position].color?.let { binding.view.setBackgroundColor(it) }
            binding.cbIsSeledted.setOnCheckedChangeListener(null)
            binding.cbIsSeledted.isChecked = selectedList.contains(recentFiles[position])
            binding.cbIsSeledted.setOnCheckedChangeListener { buttonView, isChecked ->
               try{
                   if(selectedList.contains(recentFiles[position])){
                       selectedList.remove(recentFiles[position])
                   }else{
                       selectedList.add(recentFiles[position])
                   }
               }catch (e:Exception){

               }
                selectionCallBack.selectionChanged(selectedList.size)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return recentFiles.size
    }

    override fun getFilter(): Filter {
        return appsFilter
    }
    private val appsFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: ArrayList<AllFilesModelClassAAT> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(filteringListFull)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in filteringListFull) {
                    if (item.fileName.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            recentFiles.clear()
            recentFiles.addAll(results.values as ArrayList<AllFilesModelClassAAT>)
            notifyDataSetChanged()

        }
    }

    inner class ViewHolder(val binding:RowRecentlyDeletedAatBinding):RecyclerView.ViewHolder(binding.root) {

    }

}