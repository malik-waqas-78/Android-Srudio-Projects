package com.example.whatsappchatlocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.whatsappchatlocker.R;

public class Confrim_new_password extends AppCompatActivity {
    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    String password;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confrimpassword);
        getSupportActionBar().hide();
        textView = findViewById(R.id.text_confrim);
        textView.setText("Confrim new Password");
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
                Toast.makeText(Confrim_new_password.this, "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Confrim_new_password.this, MainActivity.class);
                startActivity(intent);
            }else
            {
                mPinLockView.resetPinLockView();
                Toast.makeText(Confrim_new_password.this, "Password not MAtch", Toast.LENGTH_SHORT).show();
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
