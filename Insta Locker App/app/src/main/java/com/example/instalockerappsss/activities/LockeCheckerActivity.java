package com.example.instalockerappsss.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instalockerappsss.R;

public class LockeCheckerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        SharedPreferences preferences = getSharedPreferences("Lock", 0);
        String password = preferences.getString("password", "0");
        if (password.equals("0")) {
            Intent intent = new Intent(LockeCheckerActivity.this, CreatePassword.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LockeCheckerActivity.this, EnterPassword.class);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}