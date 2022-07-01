package com.example.viewpager2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() , MyFragment.callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun oneopen() {
        Log.d("TAG", "oneopen: ")
    }

    override fun twoopen() {
        Log.d("TAG", "twoopen: ")
    }

    override fun threeopen() {
        Log.d("TAG", "threeopen: ")
    }

}