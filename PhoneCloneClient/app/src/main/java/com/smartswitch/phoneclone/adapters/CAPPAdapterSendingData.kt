package com.smartswitch.phoneclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.modelclasses.CAPPTransferDataModel

class CAPPAdapterSendingData(var context: Context, HSTransferData: ArrayList<CAPPTransferDataModel>) :
    RecyclerView.Adapter<CAPPAdapterSendingData.MyViewHolder>() {

    var myDataList = ArrayList<CAPPTransferDataModel>()

    init {
        myDataList = HSTransferData
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_loaded_data_capp, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

     /*   if(position==1){
            holder.itemView.visibility=View.GONE
            return
        }*/
        holder.tv_itemTitle.text= myDataList[position].title
        holder.tv_itemInfo.text= myDataList[position].itemInfo
        holder.pbar.progress=myDataList[position].progress
       if(myDataList[position].progress>0){
           holder.tv_percent_progress.visibility=View.VISIBLE
           holder.pbar.visibility=View.VISIBLE
       }else{
           holder.tv_percent_progress.visibility=View.INVISIBLE
           holder.pbar.visibility=View.INVISIBLE
       }
        holder.tv_percent_progress.text="${myDataList[position].progress}%"
      //  Glide.with(context).load(context.getDrawable(myDataList[position].iconsIds)).into(  holder.iv_appIcon)
      holder.iv_appIcon.setImageDrawable(context.getDrawable(myDataList[position].iconsIds))
    }

    override fun getItemCount(): Int {
        return myDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_itemTitle:TextView=itemView.findViewById(R.id.tv_data_title)
        var tv_itemInfo:TextView=itemView.findViewById(R.id.tv_data_info)
        var pbar:ProgressBar=itemView.findViewById(R.id.pbar)
//        var cardView: MaterialCardView =itemView.findViewById(R.id.cardview)
        var iv_appIcon:ImageView=itemView.findViewById(R.id.iv_data)
        var tv_percent_progress:TextView=itemView.findViewById(R.id.tv_pbar_percent)

      /*  init{
            itemView.setOnClickListener{
                if(!myDataList[adapterPosition].itemInfo.contains("0 items")&&myDataList[position].itemInfo.indexOf("0")!=0&&!myDataList[adapterPosition].sendPressed ){
                    cb_select.isChecked=!cb_select.isChecked
                    myDataList[adapterPosition].seleted= cb_select.isChecked
                    notifyItemChanged(adapterPosition)
                    *//*if(!myDataList[adapterPosition].seleted){
                        LaunchSendData.cb_selectAllData?.isChecked=false
                    }*//*
                }

            }
        }*/

    }
}