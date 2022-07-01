package com.example.whatsappchatlocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.whatsappchatlocker.R;

public class EnterPassword  extends AppCompatActivity {

    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    SharedPreferences preferences;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_enter_passcode);
        preferences = getSharedPreferences("Lock", 0);
        password = preferences.getString("password", "0");
        mPinLockView = findViewById(R.id.pin_lock_view);
        indicatorDots =  findViewById(R.id.indicator_dots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(indicatorDots);
//        indicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (password.equals(pin)) {
                    Intent intent = new Intent(EnterPassword.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(EnterPassword.this, "Passsword Incorrect", Toast.LENGTH_SHORT).show();
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
