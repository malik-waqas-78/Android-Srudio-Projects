package com.example.pizzader

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzader.databinding.RowCustomListBinding


class CustomList(val context: Context, val pizzaList: ArrayList<PizzaModelClass>) : RecyclerView.Adapter<CustomList.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  binding=RowCustomListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            clImage.background= pizzaList.get(position).pizzaImage?.let { context.getDrawable(it) }
            tvName.text=pizzaList.get(position).pizzaFlavour
        }
    }

    override fun getItemCount(): Int {
        return pizzaList.size
    }
    inner class ViewHolder(val binding: RowCustomListBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                //go to next activity
                val intent= Intent(context,OrderActivity::class.java)
                intent.putExtra("obj",pizzaList[adapterPosition])
                context.startActivity(intent)
            }
        }
    }

}