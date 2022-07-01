package com.example.whatsappchatlocker;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.whatsappchatlocker.R;

public class LockActivity extends AppCompatActivity {
    PinLockView mPinLockView;
    IndicatorDots indicatorDots;
    SharedPreferences preferences;
    String password;
    Boolean DouleTap = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_passcode);
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
                ExitActivity.exitApplication(getApplicationContext());
            //   finishAffinity();
            } else {
                Toast.makeText(LockActivity.this, "Passsword Incorrect", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        if (DouleTap)
        {
            // finishAffinity();
            Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.HomeActivity"));
            intent.setPackage("com.whatsapp");
            intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
            startActivity(intent);
            ExitActivity.exitApplication(getApplicationContext());
        }else
        {
            DouleTap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DouleTap = false;

                }
            }, 10000);
        }
    }
}
