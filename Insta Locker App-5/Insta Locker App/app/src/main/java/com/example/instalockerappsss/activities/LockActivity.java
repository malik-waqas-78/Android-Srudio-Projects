package com.example.instalockerappsss.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.instalockerappsss.R;
import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

import java.util.concurrent.locks.Lock;

public class LockActivity extends AppCompatActivity implements FingerPrintAuthCallback {
    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    SharedPreferences preferences;
    TextView textView_confirm_password;
    ImageView btn_fingerprint_enter_lock;
    String password;
    Boolean DoubleTap = false;
    FingerPrintAuthHelper fingerPrintAuthHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_enter_passcode);
        textView_confirm_password = findViewById(R.id.passwrod_text);
        btn_fingerprint_enter_lock=findViewById(R.id.btn_fingerprint_enter_lock);
        btn_fingerprint_enter_lock.setVisibility(View.VISIBLE);
        fingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);
        textView_confirm_password.setText("Enter 4-digit PIN");
        preferences = getSharedPreferences("Lock", 0);
        password = preferences.getString("password", "0");
        mPinLockView = findViewById(R.id.pin_lock_view);
        indicatorDots = findViewById(R.id.indicator_dots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(indicatorDots);
        Intent intent = getIntent();
        if (intent.hasExtra("resume")) {
            if (intent.getBooleanExtra("resume", false)) {
                SharedPreferences preferences = getSharedPreferences("Lock", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("resume", true);
                editor.apply();
            }
        }
    }
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (password.equals(pin)) {
                SharedPreferences preferences = getSharedPreferences("Lock", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("resume", false);
                editor.putBoolean("appLock", false);
                editor.putBoolean("resumeAppLock",false);
                editor.apply();
                ExitAppActivity.exitApplication(getApplicationContext());
                //   finishAffinity();
            } else {
                final Animation animShake = AnimationUtils.loadAnimation(LockActivity.this, R.anim.shake_animation);
                mPinLockView.startAnimation(animShake);
                indicatorDots.startAnimation(animShake);
/*
                Toast.makeText(LockActivity.this, "Passsword Incorrect", Toast.LENGTH_SHORT).show();
*/
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
        fingerPrintAuthHelper.startAuth();
    }
    @Override
    protected void onPause() {
        super.onPause();
        fingerPrintAuthHelper.stopAuth();
    }
    @Override
    public void onBackPressed() {
        SharedPreferences preferences = getSharedPreferences("Lock", 0);
        boolean lock1 = preferences.getBoolean("appLock", true);
        Log.d("TAG", "onBackPressed: " + lock1);
        if (lock1) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.addCategory(Intent.CATEGORY_HOME);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            Log.d("TAG", "onBackPressed: " + lock1);
            startActivity(i);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.instagram.android", "com.instagram.android.activity.MainTabActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finishAffinity();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onNoFingerPrintHardwareFound() {
        btn_fingerprint_enter_lock.setVisibility(View.GONE);
    }
    @Override
    public void onNoFingerPrintRegistered() {
    }
    @Override
    public void onBelowMarshmallow() {
        btn_fingerprint_enter_lock.setVisibility(View.GONE);
    }
    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(LockActivity.this, MainActivity.class);
        startActivity(i);
    }
    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        switch (errorCode) {
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                //Can not recognize the fingerprint scanned
                Toast.makeText(this, "Cannot Recognize!", Toast.LENGTH_SHORT).show();
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                //This is not recoverable error.Try other options for user authentication.like pin, password
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:
                //Any recoverable erro.Display message to user
                break;
        }
    }
}