package com.example.whatsappchatlocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappchatlocker.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getSupportActionBar().hide();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("Lock", 0);
                String password = preferences.getString("password", "0");
                if (password.equals("0"))
                {
                    Intent intent = new Intent(Splash.this, CreatePassword.class);
                    startActivity(intent);
                }else
                    {
                        Intent intent = new Intent(Splash.this, EnterPassword.class);
                        startActivity(intent);
                    }

            }
        }, 3000);

    }
}
