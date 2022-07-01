package com.example.lab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.lab5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    val taskList=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arrayAdapter= ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,taskList)
        val listView = binding.listView
        listView.adapter = arrayAdapter
        listView.setOnItemLongClickListener { parent, view, position, id ->
            taskList.removeAt(position)
            arrayAdapter.notifyDataSetChanged()
            return@setOnItemLongClickListener true
        }

        binding.btnAdd.setOnClickListener {
            if(binding.etTask.text.trim().toString().isNullOrEmpty()){
                binding.etTask.error="Wrong input"
            }else{
                taskList.add(binding.etTask.text.toString())
                binding.etTask.setText("")
                arrayAdapter.notifyDataSetChanged()
            }
        }


    }
}