package com.blog.bloggingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blog.bloggingapp.R

class ChoosePic : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen5)

        var rv=findViewById<RecyclerView>(R.id.rv)

        rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        var list=getImagesList()

        rv.adapter=AdapterChoosePic(this@ChoosePic,list)

    }

    private fun getImagesList(): ArrayList<Int> {
        var list=ArrayList<Int>()

        list.add(R.drawable.fig001)
        list.add(R.drawable.fig002)
        list.add(R.drawable.fig003)
        list.add(R.drawable.fig004)
        list.add(R.drawable.fig005)
        list.add(R.drawable.fig006)
        list.add(R.drawable.fig007)
        list.add(R.drawable.fig008)
        list.add(R.drawable.fig009)
        list.add(R.drawable.fig010)
        list.add(R.drawable.fig011)
        list.add(R.drawable.fig012)
        list.add(R.drawable.fig013)
        list.add(R.drawable.fig014)
        list.add(R.drawable.fig015)
        list.add(R.drawable.fig016)
        list.add(R.drawable.fig017)
        list.add(R.drawable.fig018)
        return list
    }
}