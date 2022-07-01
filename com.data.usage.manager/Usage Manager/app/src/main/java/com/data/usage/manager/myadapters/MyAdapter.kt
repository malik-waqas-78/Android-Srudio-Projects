package com.data.usage.manager.myadapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.data.usage.manager.modelclasses.AppsInfo_ModelClass
import com.data.usage.manager.R

class MyAdapter(var myContext: Context,var allAppsData:ArrayList<AppsInfo_ModelClass>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

   companion object{
       var total_noofBytes=0.0
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_appdatausage,null))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(position!=RecyclerView.NO_POSITION){
            val app_dataUsageInfo=allAppsData.get(position)
            /* holder.pbar_dataUsage.max=100*/
            holder.pbar_dataUsage.progress=(((app_dataUsageInfo.getTotalDataUsed()!!.div(total_noofBytes))*100).toInt())
            holder.tv_dataused.setText(app_dataUsageInfo.getProperDataUsageInFormating())
            val icon: Drawable? =app_dataUsageInfo.getAppInfo().loadIcon(myContext.packageManager)
            holder.iv_appicon.setImageDrawable(icon)
        }
    }

    override fun getItemCount(): Int {
        return allAppsData.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        public var pbar_dataUsage:ProgressBar

        public var iv_appicon:ImageView

        public var tv_dataused: TextView


        init {
            pbar_dataUsage=itemView.findViewById(R.id.pbar_dataused)
            iv_appicon=itemView.findViewById(R.id.iv_appicon)
            tv_dataused=itemView.findViewById(R.id.tv_dataused)
        }
    }

}