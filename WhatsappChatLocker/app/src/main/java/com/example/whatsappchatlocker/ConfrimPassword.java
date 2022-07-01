package com.example.whatsappchatlocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.whatsappchatlocker.R;

public class ConfrimPassword  extends AppCompatActivity {
    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    String password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confrimpassword);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        password = intent.getStringExtra("password");
        mPinLockView = findViewById(R.id.pin_lock_view2);
        indicatorDots =  findViewById(R.id.indicator_dots2);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(indicatorDots);


    }
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (password.equals(pin)) {
                SharedPreferences preferences = getSharedPreferences("Lock", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("password", pin);
                editor.apply();
                Intent intent = new Intent(ConfrimPassword.this, MainActivity.class);
                startActivity(intent);
            }else
                {
                    mPinLockView.resetPinLockView();
                    Toast.makeText(ConfrimPassword.this, "Password not MAtch", Toast.LENGTH_SHORT).show();
                }


        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
        }
    };
}
