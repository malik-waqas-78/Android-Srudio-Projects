package com.example.urdupoetry.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.urdupoetry.R
import com.example.urdupoetry.activities.ViewPoetry
import com.example.urdupoetry.databinding.RowCategoryAdapterBinding
import com.example.urdupoetry.modelclasses.Categories

class CategoriesAdapter(var context: Context, var categories:ArrayList<Categories> ):RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  binding=RowCategoryAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.tvCategory.text=categories[position].type
            binding.root.setOnClickListener{
                val type=categories[adapterPosition].type
                context.startActivity(Intent(context,ViewPoetry::class.java).apply {
                    putExtra("name",type)
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
    inner class ViewHolder(val binding:RowCategoryAdapterBinding):RecyclerView.ViewHolder(binding.root) {
    }
}