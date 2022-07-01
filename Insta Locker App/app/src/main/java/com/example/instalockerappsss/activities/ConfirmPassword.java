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

public class ConfirmPassword extends AppCompatActivity {
    PinLockView mPinLockView;
    TextView textView_confirm_password;
    IndicatorDots indicatorDots;
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
        textView_confirm_password=findViewById(R.id.passwrod_text);
        textView_confirm_password.setText("Confirm PIN");
        /*getSupportActionBar().hide();*/
        Intent intent = getIntent();
        password = intent.getStringExtra("password");
        mPinLockView = findViewById(R.id.pin_lock_view);
        indicatorDots =  findViewById(R.id.indicator_dots);
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
                Intent intent = new Intent(ConfirmPassword.this, MainActivity.class);
                intent.putExtra("password",pin);
                startActivity(intent);
            }else
                {
                    final Animation animShake = AnimationUtils.loadAnimation(ConfirmPassword.this, R.anim.shake_animation);
                    mPinLockView.startAnimation(animShake);
                    indicatorDots.startAnimation(animShake);
                    mPinLockView.resetPinLockView();
/*
                    Toast.makeText(ConfrimPassword.this, "Password not Match", Toast.LENGTH_SHORT).show();
*/
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
    public void onBackPressed() {
        finish();
    }
}