package com.example.instalockerappsss.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.PackageInfoCompat;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.instalockerappsss.R;
import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

public class EnterPassword extends AppCompatActivity  implements FingerPrintAuthCallback {
    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    TextView textView_confirm_password;
    SharedPreferences preferences;
    String password;
    ImageView btn_fingerprint_enter_lock;
    FingerPrintAuthHelper fingerPrintAuthHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getSupportActionBar().hide();*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_enter_passcode);
        btn_fingerprint_enter_lock=findViewById(R.id.btn_fingerprint_enter_lock);
        btn_fingerprint_enter_lock.setVisibility(View.VISIBLE);
        fingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);
        textView_confirm_password = findViewById(R.id.passwrod_text);
        textView_confirm_password.setText("Enter 4-digit PIN");
        preferences = getSharedPreferences("Lock", 0);
        boolean finger_checked=preferences.getBoolean("fingerLock",true);
        if (preferences.getBoolean("fingerLock",false)==true){
            fingerPrintAuthHelper.startAuth();
        }
        if (preferences.getBoolean("fingerLock",false)==false){
            fingerPrintAuthHelper.stopAuth();
        }
        password = preferences.getString("password", "0");
        mPinLockView = findViewById(R.id.pin_lock_view);
        indicatorDots = findViewById(R.id.indicator_dots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(indicatorDots);
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (password.equals(pin)) {
                Intent intent = new Intent(EnterPassword.this, MainActivity.class);
                intent.putExtra("password",pin);
                startActivity(intent);
            } else {
                final Animation animShake = AnimationUtils.loadAnimation(EnterPassword.this, R.anim.shake_animation);
                mPinLockView.startAnimation(animShake);
                indicatorDots.startAnimation(animShake);
                /*Toast.makeText(EnterPassword.this, "Password Incorrect", Toast.LENGTH_SHORT).show();*/
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
        //start fingerprint authentication
        fingerPrintAuthHelper.startAuth();
    }
    @Override
    protected void onPause() {
        super.onPause();
        fingerPrintAuthHelper.stopAuth();
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(EnterPassword.this, SplashScreenActivity.class);
        startActivity(i);
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
        Intent i=new Intent(EnterPassword.this,MainActivity.class);
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