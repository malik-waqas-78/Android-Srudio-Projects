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
import com.photo.recovery.callbacks.SelectionCallBackAAT
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.databinding.RowRecentlyDeletedAatBinding
import com.photo.recovery.models.ChatsAAT

class ProfilesAdapterAAT(var context: Context, var chats: ArrayList<ChatsAAT>) :
    RecyclerView.Adapter<ProfilesAdapterAAT.ViewHolder>(), Filterable {
    var selectedList = ArrayList<ChatsAAT>()
    var filteringList = ArrayList<ChatsAAT>(chats)
    var selectionCallBack=context as SelectionCallBackAAT
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowRecentlyDeletedAatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            //populate chats
            val item = chats[position]

            Glide.with(context).load(item.icon).into(binding.imageView2)
            binding.tvFileName.text = item.senderName
            binding.tvDate.text = item.textMsg
            binding.cbIsSeledted.setOnCheckedChangeListener(null)
            binding.cbIsSeledted.isChecked = selectedList.contains(chats[position])
            binding.cbIsSeledted.setOnCheckedChangeListener { buttonView, isChecked ->
                if(selectedList.contains(chats[position])){
                    selectedList.remove(chats[position])
                }else{
                    selectedList.add(chats[position])
                }
                selectionCallBack.selectionChanged(selectedList.size)
                notifyItemChanged(position)
            }
      //     binding.view.setBackgroundColor(GeneralProfile.color)
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    inner class ViewHolder(val binding: RowRecentlyDeletedAatBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener{


                    if(adapterPosition==RecyclerView.NO_POSITION){
                        return@setOnClickListener
                    }

                    val intent= Intent(context, GeneralChatsAAT::class.java)

                    intent.putExtra(MyConstantsAAT.PROFILE_NAME_KEY,chats[adapterPosition].senderName)
                    (context as Activity).startActivity(intent)
                }
            }

    }

    override fun getFilter(): Filter {
        return appsFilter
    }

    private val appsFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: ArrayList<ChatsAAT> = ArrayList<ChatsAAT>()
            if (constraint.isEmpty()) {
                filteredList.addAll(filteringList)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in filteringList) {
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
            chats.clear()
            chats.addAll(results.values as ArrayList<ChatsAAT>)
            notifyDataSetChanged()

        }
    }
}