package com.test.myapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnNewGame:Button=findViewById(R.id.btnNewGame)
        var btnAbout:Button =findViewById(R.id.btnAbout)

        btnNewGame.setOnClickListener {
            var intent =Intent(this@HomeActivity,GameActivity::class.java)
            startActivity(intent)
        }

        btnAbout.setOnClickListener {
            var dialog = AlertDialog.Builder(this)
                .setTitle("Name : STD ID")
                .setMessage(applicationContext.getString(R.string.about_description))
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })
                .setNegativeButton(null,null)
            dialog.show()
        }

    }


}