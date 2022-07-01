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

public class Old_Password  extends AppCompatActivity {
    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    SharedPreferences preferences;
    String password;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_passcode);

        textView = findViewById(R.id.textView);
        textView.setText("Enter Your Password");
        preferences = getSharedPreferences("Lock", 0);
        password = preferences.getString("password", "0");
        mPinLockView = findViewById(R.id.pin_lock_view);
        indicatorDots =  findViewById(R.id.indicator_dots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(indicatorDots);

    }
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (password.equals(pin)) {
                Intent intent = new Intent(Old_Password.this, New_Password.class);
                startActivity(intent);
            } else {
                Toast.makeText(Old_Password.this, "Passsword Incorrect", Toast.LENGTH_SHORT).show();
                mPinLockView.resetPinLockView();
            }


        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mPinLockView.resetPinLockView();
    }
}
