package com.photo.recovery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.photo.recovery.databinding.RowChatsAatBinding

import androidx.recyclerview.widget.RecyclerView
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.models.ChatsAAT
import java.lang.Exception

class ChatsAdapterAAT(var context: Context, var chats: ArrayList<ChatsAAT>) :
    RecyclerView.Adapter<ChatsAdapterAAT.ViewHolder>(), Filterable {
    var selectionCallBack=context as SelectionCallBackAAT
    var filteringList = ArrayList(chats)
    var selectedList = ArrayList<ChatsAAT>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowChatsAatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.tvMsg.text = chats[position].textMsg
            binding.tvDate.text = chats[position].date
            binding.checkBox.setOnCheckedChangeListener(null)
            binding.checkBox.isChecked = selectedList.contains(chats[position])
            binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                try{
                    if(selectedList.contains(chats[position])){
                        selectedList.remove(chats[position])
                    }else{
                        selectedList.add(chats[position])
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                selectionCallBack.selectionChanged(selectedList.size)
                notifyItemChanged(position)
            }
        }
    }



    override fun getItemCount(): Int {
        return chats.size
    }


   inner  class ViewHolder(val binding: RowChatsAatBinding) : RecyclerView.ViewHolder(binding.root){

    }


    override fun getFilter(): Filter {
        return appsFilter
    }

    private val appsFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: ArrayList<ChatsAAT> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(filteringList)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in filteringList) {
                    if (item.textMsg.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            chats.clear()
            chats.addAll(results.values as ArrayList<ChatsAAT>)
            notifyDataSetChanged()

        }
    }

}