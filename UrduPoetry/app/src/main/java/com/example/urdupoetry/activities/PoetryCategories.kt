package com.example.urdupoetry.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.urdupoetry.adapters.CategoriesAdapter
import com.example.urdupoetry.adapters.PoetryAdapter
import com.example.urdupoetry.databinding.PoetryCategoriesBinding
import com.example.urdupoetry.modelclasses.Categories
import com.example.urdupoetry.utils.CategorySheetHandler
import com.example.urdupoetry.utils.Constants


class PoetryCategories : AppCompatActivity() {



    lateinit var binding : PoetryCategoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= PoetryCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var list=populateCategories()

        var adapter=CategoriesAdapter(this@PoetryCategories, list)

        binding.rvCategories.layoutManager =GridLayoutManager(this,2)

        binding.rvCategories.adapter=adapter



        /*binding.tv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val categorySheetHandler= CategorySheetHandler()
                categorySheetHandler.GetCategoryData().execute()
            }

        })*/
    }

    private fun populateCategories() :ArrayList<Categories>{
        var list=ArrayList<Categories>()
        for(category in Constants.poetryTypes){
            list.add(Categories(category))
        }
        return list
    }
}