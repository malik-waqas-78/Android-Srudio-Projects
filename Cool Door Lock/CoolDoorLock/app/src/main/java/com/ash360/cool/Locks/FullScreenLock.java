package com.ash360.cool.Locks;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ash360.cool.Activities.Settings_DoorLock;
import com.ash360.cool.R;
import com.ash360.cool.Utils.Constants_DoorLock;
import com.ash360.cool.Utils.Shared_Pref_DoorLock;
import com.ash360.cool.databinding.ActivityFullScreenLockBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_1;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_2;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_3;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_4;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_5;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_6;
import static com.ash360.cool.Utils.Constants_DoorLock.ENABLE_SOUND;
import static com.ash360.cool.Utils.Constants_DoorLock.ENABLE_SOUND_DEFAULT_VAL;
import static com.ash360.cool.Utils.Constants_DoorLock.SELECTED_DOOR;
import static com.ash360.cool.Utils.Constants_DoorLock.SELECTED_DOOR_DEFAULT_VALUE;
import static com.ash360.cool.Utils.Constants_DoorLock.TIME_STYLE;
import static com.ash360.cool.Utils.Constants_DoorLock.TIME_STYLE_DEFAULT_VAL;

public class FullScreenLock extends AppCompatActivity {
    private final PhoneStateBroadcastReceiver phone_state_receiver = new PhoneStateBroadcastReceiver();
    private ActivityFullScreenLockBinding binding;
    private Context context;
    private RelativeLayout wrapperView;
    private WindowManager winMgr;
    private WindowManager.LayoutParams params;
    private RelativeLayout leftDoor, rightDoor, time_date;
    private ImageView handleDoor, accessDenied;
    private Animation handleRotate, doorLeftAnim, doorRightAnim, access_denied_shake;
    private SeekBar[] seekbars;
    private LinearLayout pattern_sliders;
    private Shared_Pref_DoorLock shared_pref_doorLock;
    private MediaPlayer mediaPlayer;
    private TextView dateView;
    private TextClock clockView;
    private boolean isSoundOn, clockFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AddWrapperView();
        SetViews();
        SetListeners();
        loadBannerAdd();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void SetListeners() {
        IntentFilter intentFilter = new IntentFilter("RINGING_DURING_LOCK");
        registerReceiver(phone_state_receiver, intentFilter);

        doorRightAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FinishActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        handleRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handleDoor.setVisibility(View.GONE);
                pattern_sliders.setVisibility(View.GONE);
                time_date.setVisibility(View.GONE);
                leftDoor.startAnimation(doorLeftAnim);
                rightDoor.startAnimation(doorRightAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        access_denied_shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                accessDenied.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        handleDoor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (LockMatched()) {
                        if (isSoundOn) {
                            mediaPlayer.start();
                        }
                        handleDoor.startAnimation(handleRotate);
                        handleDoor.setOnTouchListener(null);
                    } else {
                        accessDenied.setVisibility(View.VISIBLE);
                        accessDenied.startAnimation(access_denied_shake);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private boolean LockMatched() {
        int currentPattern = getDotPin();
        int savedPattern = shared_pref_doorLock.GetInt(Constants_DoorLock.DOT_LOCK_PATTERN, 9999);
        return currentPattern == savedPattern;
    }

    private int getDotPin() {
        String lockValue = "";
        for (SeekBar seekBar : seekbars) {
            lockValue += String.valueOf(seekBar.getProgress() + 1);
            seekBar.setProgress(0);
        }
        return Integer.parseInt(lockValue);
    }


    private void SetViews() {
        shared_pref_doorLock = new Shared_Pref_DoorLock(context);
        leftDoor = wrapperView.findViewById(R.id.door_left_side);
        rightDoor = wrapperView.findViewById(R.id.door_right_side);
        clockView = wrapperView.findViewById(R.id.clock_door_lock);
        dateView = wrapperView.findViewById(R.id.date_door_lock);
        mediaPlayer = MediaPlayer.create(context, R.raw.door_open_sfx);

        isSoundOn = shared_pref_doorLock.GetBool(ENABLE_SOUND, ENABLE_SOUND_DEFAULT_VAL);
        clockFormat = shared_pref_doorLock.GetBool(TIME_STYLE, TIME_STYLE_DEFAULT_VAL);

        int selected_door = shared_pref_doorLock.GetInt(SELECTED_DOOR, SELECTED_DOOR_DEFAULT_VALUE);
        switch (selected_door) {
            case DOOR_1: {
                leftDoor.setBackgroundResource(R.drawable.door_1_1);
                rightDoor.setBackgroundResource(R.drawable.door_1_2);
                break;
            }
            case DOOR_2: {
                leftDoor.setBackgroundResource(R.drawable.door_2_1);
                rightDoor.setBackgroundResource(R.drawable.door_2_2);
                break;
            }
            case DOOR_3: {
                leftDoor.setBackgroundResource(R.drawable.door_3_1);
                rightDoor.setBackgroundResource(R.drawable.door_3_2);
                break;
            }
            case DOOR_4: {
                leftDoor.setBackgroundResource(R.drawable.door_4_1);
                rightDoor.setBackgroundResource(R.drawable.door_4_2);
                break;
            }
            case DOOR_5: {
                leftDoor.setBackgroundResource(R.drawable.door_5_1);
                rightDoor.setBackgroundResource(R.drawable.door_5_2);
                break;
            }
            case DOOR_6: {
                leftDoor.setBackgroundResource(R.drawable.door_6_1);
                rightDoor.setBackgroundResource(R.drawable.door_6_2);
                break;
            }
        }


        handleDoor = wrapperView.findViewById(R.id.door_handle);
        time_date = wrapperView.findViewById(R.id.time_date_relative_layout);
        pattern_sliders = wrapperView.findViewById(R.id.linearLayoutLockPattern);
        accessDenied = wrapperView.findViewById(R.id.access_denied);

        seekbars = new SeekBar[]{
                wrapperView.findViewById(R.id.seek_bar_1_lock)
                , wrapperView.findViewById(R.id.seek_bar_2_lock)
                , wrapperView.findViewById(R.id.seek_bar_3_lock)
                , wrapperView.findViewById(R.id.seek_bar_4_lock)};

        doorLeftAnim = AnimationUtils.loadAnimation(
                getApplicationContext()
                , R.anim.translate_left
        );
        doorRightAnim = AnimationUtils.loadAnimation(
                getApplicationContext()
                , R.anim.translate_right
        );
        handleRotate = new RotateAnimation(0.0f, 80.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        handleRotate.setDuration(1000L);

        access_denied_shake = AnimationUtils.loadAnimation(
                getApplicationContext()
                , R.anim.shake_anim);
    }

    private void AddWrapperView() {
        binding = ActivityFullScreenLockBinding.inflate(getLayoutInflater());
        context = FullScreenLock.this;

        winMgr = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wrapperView = new RelativeLayout(getApplicationContext());
        wrapperView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        wrapperView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(MATCH_PARENT, MATCH_PARENT
                    , WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    , WindowManager.LayoutParams.FLAG_FULLSCREEN
                    , PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(MATCH_PARENT, MATCH_PARENT
                    , WindowManager.LayoutParams.TYPE_PHONE
                    , WindowManager.LayoutParams.FLAG_FULLSCREEN
                    , PixelFormat.TRANSLUCENT);
        }
        getWindow().setAttributes(params);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                if (wrapperView != null && wrapperView.getParent() == null && !FullScreenLock.this.isFinishing()) {
                    winMgr.addView(wrapperView, params);
                }
            } else {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                Toast.makeText(context, "Provide Draw Over Apps Permission First", Toast.LENGTH_SHORT).show();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                startActivity(new Intent(context, Settings_DoorLock.class));
                finish();
            }
        } else {
            if (wrapperView != null && wrapperView.getParent() == null && !FullScreenLock.this.isFinishing()) {
                winMgr.addView(wrapperView, params);
            }
        }
        View.inflate(context, R.layout.activity_full_screen_lock, wrapperView);
    }

    private void FinishActivity() {
        if (wrapperView.getParent() != null) {
            winMgr.removeView(wrapperView);
        }
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        if (phone_state_receiver != null) {
            unregisterReceiver(phone_state_receiver);
        }
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (clockFormat) {
            clockView.setFormat12Hour("hh:mm a");
        } else {
            clockView.setFormat12Hour("kk:mm");
        }
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EE dd/MM/yyyy", Locale.getDefault());
        dateView.setText(df.format(c));
    }

    public void loadBannerAdd() {
        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);

        AdListener adListener = new AdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAG", "onError: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d("TAG", "Ad loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
        RelativeLayout relativeLayout = wrapperView.findViewById(R.id.top_banner);
        relativeLayout.addView(adView);
    }

    private class PhoneStateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("RINGING_DURING_LOCK")) {
                FinishActivity();
                Log.v("lock_state", intent.getAction());
            }
        }
    }
}