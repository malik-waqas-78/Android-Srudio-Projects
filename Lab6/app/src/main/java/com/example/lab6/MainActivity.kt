package com.example.lab6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab6.databinding.ActivityMainBinding
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.app.ProgressDialog
import java.lang.Exception

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    val myCalendar: Calendar = Calendar.getInstance()
    var datePickerDialog =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAlert.setOnClickListener {
            showAlertDialog()
        }

        binding.btnDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnProgress.setOnClickListener {
            showProgressDialog()
        }

    }

    private fun showAlertDialog(){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("Are you sure you want to exit?")
        alertDialog.setCancelable(true)

        alertDialog.setPositiveButton(
            "Exit",
            DialogInterface.OnClickListener { dialog, id -> finish() })

        alertDialog.setNegativeButton(
            "No",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })


        alertDialog.create().show()
    }

    private fun showProgressDialog(){
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMax(100) // Progress Dialog Max Value

        progressDialog.setMessage("Loading Demo...") // Setting Message

        progressDialog.setTitle("ProgressDialog") // Setting Title

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL) // Progress Dialog Style Horizontal

        progressDialog.show() // Display Progress Dialog

        progressDialog.setCancelable(false)
        Thread {
            try {
                while (progressDialog.getProgress() < progressDialog.getMax()) {
                    progressDialog.progress=progressDialog.progress+1
                    Thread.sleep(200)
                    if (progressDialog.getProgress() === progressDialog.getMax()) {
                        progressDialog.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showDatePickerDialog(){
        DatePickerDialog(
            this@MainActivity, datePickerDialog, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
}