package com.blog.bloggingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.*

class MainActivity : AppCompatActivity() {

    companion object{
        var userDbModel:UserDbModel= UserDbModel()
        var user:User?=null
    }

    lateinit var etName:EditText
    lateinit var etPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etName=findViewById(R.id.et_name)
        etPassword=findViewById(R.id.et_password)
        findViewById<Button>(R.id.login).setOnClickListener {
            if(userDbModel.usersList!!.size>0){
                user=User(etName.text.toString(),etPassword.text.toString())
                if(userDbModel.usersList?.contains(user)
                    == true){
                    startActivity(Intent(this@MainActivity,BlogEntries::class.java).apply {
                        putExtra("isGuest",false)
                    })
                }else{
                    showToast("Username or Password is wrong.")
                }
            }else{
                showToast("No registered users. PLease register first")
            }
        }

        findViewById<Button>(R.id.guest).setOnClickListener {
            startActivity(Intent(this@MainActivity,BlogEntries::class.java).apply {
                putExtra("isGuest",true)
            })
        }

        findViewById<Button>(R.id.register).setOnClickListener {
            startActivity(Intent(this@MainActivity,RegisterUser::class.java))
        }

    }

    private fun showToast(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        readFile()
    }

    private fun readFile(){


        var isFirst=true;

        userDbModel.usersList= ArrayList()
        var reader:BufferedReader? = null;

        try {
            val f = File(this@MainActivity.filesDir, "text")
            if(!f.exists()){
                f.mkdir()
            }
            val file=File(f,"user-db.txt")
            reader = BufferedReader(
                    InputStreamReader(file.inputStream(), "UTF-8")
            )

            if(reader==null){
                init(file)
                reader = BufferedReader(
                    InputStreamReader(file.inputStream(), "UTF-8")
                )
            }


            // do reading, usually loop until end of file reading
            var mLine="";
            while (reader.readLine()?.apply { mLine=this }!=null) {
                if(isFirst){
                    userDbModel.size=mLine.toInt()
                    isFirst=false
                }else{
                    userDbModel.usersList?.add(User(mLine,reader.readLine().toString()))
                }
            }
        } catch ( e : IOException) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch ( e : IOException) {
                    //log the exception
                }
            }
        }

    }

    fun init(f:File){
        var streamWriter: OutputStreamWriter = OutputStreamWriter(f.outputStream())
        streamWriter.write("0");
        streamWriter.close();
    }

}