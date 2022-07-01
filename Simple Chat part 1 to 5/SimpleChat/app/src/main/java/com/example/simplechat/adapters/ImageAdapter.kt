package com.example.simplechat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplechat.databinding.ImageLayoutBinding
import com.example.simplechat.modelclasses.Image

class ImageAdapter(val context: Context,val images:ArrayList<Image>,
val itemClick:ItemClicked):RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
       return ImageViewHolder(ImageLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        with(holder) {
            Glide.with(context).load(images[position].path).into(binding.image)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
    inner class ImageViewHolder(val binding: ImageLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClick.itemClick(images[adapterPosition])
            }
        }
    }

    interface ItemClicked{
        fun itemClick(image:Image)
    }
}