package com.example.urdupoetry.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.urdupoetry.databinding.RowPoetryAdapterBinding
import com.example.urdupoetry.modelclasses.Poetry

class PoetryAdapter(var poetries:ArrayList<Poetry>):RecyclerView.Adapter<PoetryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =RowPoetryAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            tvPoetry.text=poetries[position].poetry
        }
    }

    override fun getItemCount(): Int {
        return poetries.size
    }
    class ViewHolder(val binding:RowPoetryAdapterBinding):RecyclerView.ViewHolder(binding.root) {

    }
}