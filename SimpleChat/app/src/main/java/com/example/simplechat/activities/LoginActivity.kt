package com.example.simplechat.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simplechat.databinding.ActivityLoginBinding
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult

import androidx.activity.result.ActivityResultCallback

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    companion object{
        var ID:String=""
    }

    val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> onSignInResult(result) }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
        val response = result?.idpResponse
        if (result?.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser

            registerForBtnClick()

            // ...
        } else {
            launchFirebaseSignInUi()

        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)







    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser==null) {
            launchFirebaseSignInUi()
        }else{
            registerForBtnClick()
        }
    }

    fun registerForBtnClick(){
        binding.btnLogin.setOnClickListener{
            if(!binding.etNumber.text.trim().isEmpty()&&binding.etNumber.text.trim().length==6){
                ID=binding.etNumber.text.trim().toString();

                if(FirebaseAuth.getInstance().currentUser!=null){
                    goToNextActivity()
                }else{
                    launchFirebaseSignInUi()
                }
            }else{
                binding.etNumber.error="Enter correct input"
            }
        }
    }

    private fun launchFirebaseSignInUi() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
           )

// Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun goToNextActivity() {
        startActivity(Intent(this@LoginActivity,ContactsActivity::class.java))
        //finish()
    }
}