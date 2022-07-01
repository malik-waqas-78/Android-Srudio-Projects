package com.blog.bloggingapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterBlogEntries(var context: Context, var list:ArrayList<Blog>):
    RecyclerView.Adapter<AdapterBlogEntries.ViewHolder>() {
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        var image: ImageView =view.findViewById(R.id.iv)
        var place: TextView =view.findViewById(R.id.place)
        var desc: TextView =view.findViewById(R.id.desc)
        var sdesc: TextView =view.findViewById(R.id.sdesc)
        var rb: RatingBar =view.findViewById(R.id.rb)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.row_blogpost,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            image.setImageResource(list[position].picId)
            desc.text=list[position].longDesc
            sdesc.text=list[position].shortDesc
            place.text=list[position].place
            view.setOnClickListener {
                context.startActivity(Intent(context,BlogDetails::class.java).
                putExtra("item",list[position]))
            }
        }
    }

    override fun getItemCount()=list.size
}