package com.niazitvpro.official.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niazitvpro.official.R;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.niazitvpro.official.utils.Constants.APP_BACKGROUND_COLOR;
import static com.niazitvpro.official.utils.Constants.INSTALL_APP_DATE;
import static com.niazitvpro.official.utils.Constants.WELCOME_MESSAGE;

public class WelcomeScreenActivity extends AppCompatActivity {

    private TextView tv_welcomeMessage;
    private SharedPrefTVApp sharedPrefTVApp;
    private int appBackgroundColor;
    private LinearLayout ll_welcomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        SharedPrefTVApp.transparentStatusAndNavigation(WelcomeScreenActivity.this);

        tv_welcomeMessage = findViewById(R.id.tv_welcome_message);
        ll_welcomeScreen = findViewById(R.id.ll_welcomescreen);
        sharedPrefTVApp = new SharedPrefTVApp(getApplicationContext());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentDate = df.format(c);

        sharedPrefTVApp.putString(INSTALL_APP_DATE,currentDate);

        if (!sharedPrefTVApp.getString(APP_BACKGROUND_COLOR).isEmpty()) {

            appBackgroundColor = Color.parseColor(sharedPrefTVApp.getString(APP_BACKGROUND_COLOR));

        } else {

            appBackgroundColor = R.color.black;
        }

        ll_welcomeScreen.setBackgroundColor(appBackgroundColor);

        if(!sharedPrefTVApp.getString(WELCOME_MESSAGE).isEmpty()){

            String welcomeMessage = sharedPrefTVApp.getString(WELCOME_MESSAGE);

            tv_welcomeMessage.setText(welcomeMessage);

        }

        Animation rotation = AnimationUtils.loadAnimation(WelcomeScreenActivity.this, R.anim.rotate_animation);
        rotation.setFillAfter(true);
        tv_welcomeMessage.startAnimation(rotation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("notificationData","");
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
