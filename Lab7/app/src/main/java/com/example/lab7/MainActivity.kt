package com.example.lab7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab7.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNextScreen.setOnClickListener {
            if(binding.etName.text.trim().toString().isNullOrEmpty()){
                binding.etName.error="Name can't be empty."
            }else{
                startActivity(Intent(this@MainActivity,SecondActivity::class.java).apply {
                    putExtra("name",binding.etName.text.toString())
                })
            }
        }


    }
}