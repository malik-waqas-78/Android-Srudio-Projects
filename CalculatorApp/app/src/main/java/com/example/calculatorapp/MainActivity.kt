package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var no1:Float=0.0F
    var no2:Float=0.0F

    var number1:EditText?=null
    var number2:EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        number1=findViewById<EditText>(R.id.number1)
        number2=findViewById<EditText>(R.id.number2)

        val total=findViewById<TextView>(R.id.total)


        findViewById<Button>(R.id.add).setOnClickListener{
            getNumbers()
            total.text="Total = ${no1+no2} "
        }
        findViewById<Button>(R.id.subtract).setOnClickListener{
            getNumbers()
            total.text="Total = ${no1-no2} "
        }
        findViewById<Button>(R.id.mul).setOnClickListener{
            getNumbers()
            total.text="Total = ${no1*no2} "
        }
        findViewById<Button>(R.id.div).setOnClickListener{
            getNumbers()
            total.text="Total = ${no1/no2} "
        }

        findViewById<Button>(R.id.clear).setOnClickListener{
            no1=0.0F
            no2=0.0F
            number1?.setText("")
            number2?.setText("")
            number1?.hint = "e.g 123"
            number2?.hint = "e.g 432"
            total.text="Total = 0.0"
        }
        findViewById<Button>(R.id.exit).setOnClickListener{
            finish()
        }

    }

    fun getNumbers(){
        no1=(number1?.text.toString()).toFloat()
        no2=(number2?.text.toString()).toFloat()
    }
}