package com.example.pizzader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pizzader.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    val USER_NAME="nasir alsuwaidi"
    val PASSWORD="16172"

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val userName=binding.etName.text.toString().trim()
            val password=binding.etPassword.text.toString().trim()

            if(userName.isNotEmpty()&&password.isNotEmpty()){
                if(userName.lowercase(Locale.getDefault()) ==USER_NAME&&
                        password==PASSWORD){
                    Toast.makeText(this@MainActivity,"Login Successful",
                    Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@MainActivity,PizzaFlavours::class.java)
                    intent.putExtra("name",USER_NAME)
                    startActivity(intent)
                    finish()

                }else{
                    Toast.makeText(this@MainActivity,"Failed to Login",
                        Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.etName.error="Check Name"
                binding.etPassword.error="Check Password"
            }
        }

    }
}