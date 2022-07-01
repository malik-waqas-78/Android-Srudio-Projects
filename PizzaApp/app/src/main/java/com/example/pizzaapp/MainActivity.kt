package com.example.pizzaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var pizza_size_price=0

        var toppings_price=0

        val total_price =findViewById<TextView>(R.id.tv_order_price)

        findViewById<RadioButton>(R.id.rb_small).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                pizza_size_price=5
        }
        findViewById<RadioButton>(R.id.rb_medium).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                pizza_size_price=7
        }
        findViewById<RadioButton>(R.id.rb_large).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                pizza_size_price=9
        }

        findViewById<CheckBox>(R.id.cb_onion).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                toppings_price+=1
            else
                toppings_price-=1
        }
        findViewById<CheckBox>(R.id.cb_olives).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                toppings_price+=2
            else
                toppings_price-=2
        }
        findViewById<CheckBox>(R.id.cb_tomato).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                toppings_price+=3
            else
                toppings_price-=3
        }

        findViewById<Button>(R.id.btn_place_order).setOnClickListener {
            if(pizza_size_price!=0){
                val totalPrice=pizza_size_price+toppings_price
                total_price.setText("Total Order Price : $ $totalPrice")
            }else{
                Toast.makeText(this, "No Pizza Size Selected!",Toast.LENGTH_SHORT).show()
            }
        }



    }
}