package com.zak.clone.zakadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zak.clone.R
import com.zak.clone.zakactivity.ZakActivityStoreContact
import com.zak.clone.zakmodelclasses.ZakContactsModel

class ZakAdapterContacts (var context:Context, var HSContactsList:ArrayList<ZakContactsModel>) : RecyclerView.Adapter<ZakAdapterContacts.ContactsViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.zak_row_contacts, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.tv_name.text=HSContactsList[position].name
        holder.tv_phoneNumber.text=HSContactsList[position].phoneNumber
        holder.cb_selected.isChecked=HSContactsList[position].isSelected
        holder.cb_selected.setOnClickListener {
            HSContactsList[position].isSelected=holder.cb_selected.isChecked
            if(!HSContactsList[position].isSelected){
                    ZakActivityStoreContact.cbSelectAll?.isChecked=false
            }
            notifyItemChanged(position)
        }
      //  make logic for selection of contacts

    }

    override fun getItemCount(): Int {
      return HSContactsList.size
    }

    inner class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tv_name=itemView.findViewById<TextView>(R.id.tv_contact_name)
        var tv_phoneNumber=itemView.findViewById<TextView>(R.id.tv_phonenumber)
        var cb_selected=itemView.findViewById<CheckBox>(R.id.cb_selection)
        init{
            itemView.setOnClickListener{
                cb_selected.isChecked=!cb_selected.isChecked
                HSContactsList[adapterPosition].isSelected=cb_selected.isChecked
                if(!HSContactsList[adapterPosition].isSelected){
                    ZakActivityStoreContact.cbSelectAll?.isChecked=false
                }
                notifyItemChanged(adapterPosition)
            }
        }
    }
}