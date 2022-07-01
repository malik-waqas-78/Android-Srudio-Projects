package com.example.instalockerappsss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.instalockerappsss.R;

public class CreatePassword extends AppCompatActivity {
    PinLockView mPinLockView;
    TextView textView_confirm_password;
    IndicatorDots indicatorDots;
    ImageView btn_fingerprint_enter_lock;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_enter_passcode);
        btn_fingerprint_enter_lock=findViewById(R.id.btn_fingerprint_enter_lock);
        btn_fingerprint_enter_lock.setVisibility(View.GONE);
        /*getSupportActionBar().hide();*/
        mPinLockView = findViewById(R.id.pin_lock_view);
        indicatorDots =  findViewById(R.id.indicator_dots);
        textView_confirm_password = findViewById(R.id.passwrod_text);
        textView_confirm_password.setText("Enter 4-digit PIN");
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(indicatorDots);
    }
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Intent intent = new Intent(CreatePassword.this, ConfirmPassword.class);
            intent.putExtra("password", pin);
            startActivity(intent);
        }
        @Override
        public void onEmpty() {

        }
        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
        }
    };
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(CreatePassword.this, SplashScreenActivity.class);
        startActivity(i);
    }
}