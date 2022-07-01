package com.example.lab7


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.lab7.databinding.SecondAcitvityBinding

class SecondActivity : AppCompatActivity() {
    lateinit var binding:SecondAcitvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= SecondAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent?.hasExtra("name") == true){
            binding.tvWelcomNote.setText("Welcome to ${intent.getStringExtra("name")}")
        }

        binding.btnBack.setOnClickListener {
            finish()
        }


    }
}