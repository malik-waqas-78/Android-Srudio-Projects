package com.example.whatsappchatlocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.whatsappchatlocker.R;

public class CreatePassword extends AppCompatActivity {
    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createpassword);
        getSupportActionBar().hide();
        mPinLockView = findViewById(R.id.pin_lock_view1);
        indicatorDots =  findViewById(R.id.indicator_dots1);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(indicatorDots);
    }
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Intent intent = new Intent(CreatePassword.this, ConfrimPassword.class);
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
}
