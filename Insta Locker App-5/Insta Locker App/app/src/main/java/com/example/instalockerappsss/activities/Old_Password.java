package com.example.instalockerappsss.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.instalockerappsss.R;

public class Old_Password  extends AppCompatActivity {
    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    TextView textView_confirm_password;
    SharedPreferences preferences;
    String password;
    ImageView btn_fingerprint_enter_lock;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_enter_passcode);
        btn_fingerprint_enter_lock = findViewById(R.id.btn_fingerprint_enter_lock);
        btn_fingerprint_enter_lock.setVisibility(View.GONE);
        textView_confirm_password = findViewById(R.id.passwrod_text);
        textView_confirm_password.setText("Enter Old PIN");
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
                    final Animation animShake = AnimationUtils.loadAnimation(Old_Password.this, R.anim.shake_animation);
                    mPinLockView.startAnimation(animShake);
                    indicatorDots.startAnimation(animShake);
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