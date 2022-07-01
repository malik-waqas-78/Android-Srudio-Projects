package com.blog.bloggingapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class RegisterUser : AppCompatActivity() {

    lateinit var name:EditText
    lateinit var pass:EditText
    lateinit var confirmPass:EditText
    lateinit var email:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        name=findViewById(R.id.et_name)
        pass=findViewById(R.id.et_password)
        confirmPass=findViewById(R.id.et_pass_confirm)
        email=findViewById(R.id.et_mail)

        findViewById<Button>(R.id.btn_register).setOnClickListener {
            if(name.text.isEmpty()||pass.text.isEmpty()||confirmPass.text.isEmpty()||email.text.isEmpty()){
                showToast("No feild should be empty")
            }else if(confirmPass.text.toString()!=pass.text.toString()){
                showToast("Passwords doesn;t match")
            }else{
                val user=User(name.text.toString(),pass.text.toString())

                registerUser(user)

            }
        }

    }

    private fun registerUser(user:User) {
        MainActivity.userDbModel.usersList?.add(user)
        val f = File(this@RegisterUser.filesDir, "text")
        if(!f.exists()){
            f.mkdir()
        }
        val file=File(f,"user-db.txt")

        try {
                var streamWriter:OutputStreamWriter = OutputStreamWriter(file.outputStream())
                streamWriter.write(MainActivity.userDbModel.toString());
                streamWriter.close();

            showToast("Successfully registered")

            finish()

            }
            catch ( e: IOException) {
                Log.e("Exception", "File write failed: " + e.toString());
            }


    }

    private fun showToast(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
    }
}