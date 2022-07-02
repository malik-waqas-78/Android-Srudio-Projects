package com.ppt.walkie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.ppt.walkie.databinding.RowEndpointsOkraBinding
import com.ppt.walkie.services.ConnectionsServiceOKRA



class AdapterEndPointsOKRA(
    var list: ArrayList<ConnectionsServiceOKRA.Endpoint>,
    var callBack: OnItemSelected
) : RecyclerView.Adapter<AdapterEndPointsOKRA.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowEndpointsOkraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.tvDeviceName.text = list[position].name
        }
    }

    override fun getItemCount(): Int {
        return list.size

    }

    inner class ViewHolder(val binding: RowEndpointsOkraBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION)
                    return@setOnClickListener
                callBack?.itemSelected(list[adapterPosition])
            }
        }
    }

    interface OnItemSelected {
        fun itemSelected(endpoint: ConnectionsServiceOKRA.Endpoint)
    }
}