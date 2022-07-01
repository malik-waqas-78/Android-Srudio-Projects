package com.blog.bloggingapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class AdapterChoosePic(var context: Context, var list:ArrayList<Int>):
    RecyclerView.Adapter<AdapterChoosePic.ViewHolder>() {
    class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {

        var image: ImageView =view.findViewById(R.id.img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.row_images,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            image.setImageResource(list[position])

            view.setOnClickListener {
                var intent=Intent()
                intent.putExtra("int",list[position])
                intent.putExtra("name","fig00$position")
                (context as Activity).setResult(972,intent)
                (context as Activity).finish()
            }
        }
    }

    override fun getItemCount()=list.size
}