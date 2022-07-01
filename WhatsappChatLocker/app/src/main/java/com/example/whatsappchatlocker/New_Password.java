package com.example.whatsappchatlocker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.whatsappchatlocker.R;

public class New_Password extends AppCompatActivity {
    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createpassword);
        getSupportActionBar().hide();
        textView = findViewById(R.id.text_password);
        textView.setText("Enter new Password");
        mPinLockView = findViewById(R.id.pin_lock_view1);
        indicatorDots =  findViewById(R.id.indicator_dots1);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(indicatorDots);
    }
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Intent intent = new Intent(New_Password.this, Confrim_new_password.class);
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
