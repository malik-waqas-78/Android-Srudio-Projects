package com.example.urdupoetry.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.urdupoetry.R
import com.example.urdupoetry.adapters.PoetryAdapter
import com.example.urdupoetry.callbacks.PoetryCallBacks
import com.example.urdupoetry.databinding.ActivityViewPoetryBinding
import com.example.urdupoetry.modelclasses.Poetry
import com.example.urdupoetry.utils.Constants
import com.example.urdupoetry.utils.PoetryFileReader

class ViewPoetry : AppCompatActivity() {
    lateinit var binding:ActivityViewPoetryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityViewPoetryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val file=ArrayList<Poetry>()

        if(intent!=null&&intent.hasExtra("name")){
            val type:String=intent.getStringExtra("name").toString()
            val poetryFileReader= PoetryFileReader(this@ViewPoetry,object:PoetryCallBacks{
                override fun fileLoaded() {
                    val adapter= Constants.poetries[type]?.let { PoetryAdapter(it) }
                    binding.rvPoetries.layoutManager= LinearLayoutManager(this@ViewPoetry).apply {
                        orientation=LinearLayoutManager.VERTICAL
                    }
                    binding.rvPoetries.adapter= adapter
                }

            },type)
            poetryFileReader
                .ReadFile().execute()

        }



    }
}