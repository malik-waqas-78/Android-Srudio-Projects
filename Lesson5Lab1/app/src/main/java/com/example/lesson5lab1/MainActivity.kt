package com.example.lesson5lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv_dev_status=findViewById<TextView>(R.id.tv_dev_status);

        findViewById<CheckBox>(R.id.cb_usa).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                Toast.makeText(this,buttonView.text.toString()+" is Correct Answer",Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<CheckBox>(R.id.cb_canada).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                Toast.makeText(this,buttonView.text.toString()+" is Correct Answer",Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<CheckBox>(R.id.cb_china).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                Toast.makeText(this,buttonView.text.toString()+" is not a Correct Answer",Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<RadioButton>(R.id.rb_no).setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
             tv_dev_status.text="No. not yet!!"
            }
        }
        findViewById<RadioButton>(R.id.rb_yes).setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                tv_dev_status.text="Yes. I am!!"
            }
        }

    }
}