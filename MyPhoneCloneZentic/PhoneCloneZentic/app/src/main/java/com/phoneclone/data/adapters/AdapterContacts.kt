package com.phoneclone.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phoneclone.data.R
import com.phoneclone.data.activity.ActivityStoreContact
import com.phoneclone.data.modelclasses.ContactsModel

class AdapterContacts (var context:Context, var contactsList:ArrayList<ContactsModel>) : RecyclerView.Adapter<AdapterContacts.ContactsViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_contacts, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.tv_name.text=contactsList[position].name
        holder.tv_phoneNumber.text=contactsList[position].phoneNumber
        holder.cb_selected.isChecked=contactsList[position].isSelected
        holder.cb_selected.setOnClickListener {
            contactsList[position].isSelected=holder.cb_selected.isChecked
            if(!contactsList[position].isSelected){
                    ActivityStoreContact.cbSelectAll?.isChecked=false
            }
            notifyItemChanged(position)
        }
      //  make logic for selection of contacts

    }

    override fun getItemCount(): Int {
      return contactsList.size
    }

    inner class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tv_name=itemView.findViewById<TextView>(R.id.tv_contact_name)
        var tv_phoneNumber=itemView.findViewById<TextView>(R.id.tv_phonenumber)
        var cb_selected=itemView.findViewById<CheckBox>(R.id.cb_selection)
        init{
            itemView.setOnClickListener{
                cb_selected.isChecked=!cb_selected.isChecked
                contactsList[position].isSelected=cb_selected.isChecked
                if(!contactsList[position].isSelected){
                    ActivityStoreContact.cbSelectAll?.isChecked=false
                }
                notifyItemChanged(position)
            }
        }
    }
}