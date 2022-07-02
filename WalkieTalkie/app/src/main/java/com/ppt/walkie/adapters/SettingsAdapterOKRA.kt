package com.ppt.walkie.adapters

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ppt.walkie.R
import com.ppt.walkie.callbacks.SettingsCallBacksOKRA
import com.ppt.walkie.databinding.RowSetttingsItemsOkraBinding

import com.ppt.walkie.services.MyServiceOKRA
import com.ppt.walkie.utils.SettingsModelClassOKRA
import com.ppt.walkie.utils.SharePrefHelperOKRA


class SettingsAdapterOKRA(var context: Context, var list:ArrayList<SettingsModelClassOKRA>, val callBacks:SettingsCallBacksOKRA) : RecyclerView.Adapter<SettingsAdapterOKRA.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsAdapterOKRA.ViewHolder {
    val binding= RowSetttingsItemsOkraBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingsAdapterOKRA.ViewHolder, position: Int) {
        with(holder){
            val sharePrefHelper=SharePrefHelperOKRA(context)
            binding.tvTitle.text=list[position].title
            if(list[position].title.equals("User profile")){
                binding.userName.text=sharePrefHelper.getName()
            }
            binding.swOnOff.visibility=list[position].switchVisibility
            if(list[position].title.equals("Share")){
                binding.bottomView.visibility= View.GONE
            }
            binding.swOnOff.setOnCheckedChangeListener(null)
            binding.swOnOff.isChecked= list[position].canReceiveCallInBackground
            Glide.with(context).load(list[position].iconDrawable).placeholder(ContextCompat.getDrawable(context,R.drawable.ic_change_name_icon)).into(binding.imageView7)
            binding.swOnOff.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->

                sharePrefHelper.setCanReceiveInBackground(isChecked)
                list[position].canReceiveCallInBackground=isChecked

                if(isChecked){
                    if(!context.isMyServiceRunning(MyServiceOKRA::class.java)){
                        val intent=Intent(context,MyServiceOKRA::class.java)
                        ContextCompat.startForegroundService(context,intent)
                    }
                }else{
                    callBacks.stopMyService()
                }

                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: RowSetttingsItemsOkraBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if(adapterPosition!=RecyclerView.NO_POSITION){
                    callBacks?.itemClicked(list[adapterPosition])
                }
            }
        }
    }

    fun Context.isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Int.MAX_VALUE)
            .any { it.service.className == serviceClass.name }
    }

}