package com.photo.recovery.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.photo.recovery.activites.GeneralChatsAAT
import com.photo.recovery.activites.GeneralProfileAAT
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.databinding.RowRecentlyDeletedAatBinding

import com.photo.recovery.models.RecentChatsAAT

class RecentChatsAdapterAAT(var context: Context, var recentFileAATS:ArrayList<RecentChatsAAT>) : RecyclerView.Adapter<RecentChatsAdapterAAT.ViewHolder>() ,
    Filterable {
    var selectionCallBack=context as SelectionCallBackAAT
    val filteringListFull=ArrayList(recentFileAATS)
    var selectedList = ArrayList<RecentChatsAAT>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=
            RowRecentlyDeletedAatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            binding.tvDate.text=recentFileAATS[position].date
            binding.tvFileName.text=recentFileAATS[position].senderName
            Glide.with(context).load(recentFileAATS[position].icon).into(binding.imageView2)
            recentFileAATS[position].color?.let { binding.view.setBackgroundColor(it) }
            binding.cbIsSeledted.setOnCheckedChangeListener(null)
            binding.cbIsSeledted.isChecked = selectedList.contains(recentFileAATS[position])
            binding.cbIsSeledted.setOnCheckedChangeListener { buttonView, isChecked ->
                if(selectedList.contains(recentFileAATS[position])){
                    selectedList.remove(recentFileAATS[position])
                }else{
                    selectedList.add(recentFileAATS[position])
                }
                selectionCallBack.selectionChanged(selectedList.size)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return recentFileAATS.size
    }

    override fun getFilter(): Filter {
        return appsFilter
    }
    private val appsFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: ArrayList<RecentChatsAAT> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(filteringListFull)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in filteringListFull) {
                    if (item.senderName.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            recentFileAATS.clear()
            recentFileAATS.addAll(results.values as ArrayList<RecentChatsAAT>)
            notifyDataSetChanged()

        }
    }

    inner class ViewHolder(val binding: RowRecentlyDeletedAatBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {

                if(adapterPosition==RecyclerView.NO_POSITION){
                    return@setOnClickListener
                }

                if(context!=null){
                    val intent= Intent(context, GeneralChatsAAT::class.java)
                    intent.putExtra(MyConstantsAAT.PROFILE_NAME_KEY,recentFileAATS[adapterPosition].senderName)
                    GeneralProfileAAT.MessengerType=recentFileAATS[adapterPosition].chatType
                    (context as Activity).startActivity(intent)
                }
            }
        }
    }

}