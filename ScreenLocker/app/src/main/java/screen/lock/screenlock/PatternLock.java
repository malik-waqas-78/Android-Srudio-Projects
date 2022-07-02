package screen.lock.screenlock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AnalogClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;

import com.marcoscg.fingerauth.FingerAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PatternLock extends AppCompatActivity {
    int checkWrong=0;
    FingerAuth fingerAuth;
    LayoutInflater inflater;
    public WindowManager.LayoutParams mParams;
    public WindowManager mWindowManager;
    Vibrator vibrator;
    TextView textDate;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    ConstraintLayout dialogConstraint;
    private String date;
    RelativeLayout relativeLayout;
    int prm;
    int n=5;
    PatternLockView patternLockView;
    String patterncode,confirmPattern=null;
    ImageView fingerimage;
    View view;
    boolean checkenable,checkService,checksound,checkfiger;
    int checkstyle;
    int backgroundval;
    EditText answerEdit;
    String answerNow;
    /* access modifiers changed from: protected */
    AudioManager audioManager;
    MediaPlayer mp;
    TextClock digitalClock;
    TextView textWrongPin,txtinstr,textquestion;
    String question,answer;
    AnalogClock analogClock;
    RelativeLayout buttonsubmit,cancelButton;
    LinearLayout topBanner;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp=getSharedPreferences("MySharedPref", MODE_PRIVATE);
        checkenable=sp.getBoolean("checkenable", false);
        checkService=sp.getBoolean("serviceCheck",false);
        checksound=sp.getBoolean("sound",false);
        checkstyle=sp.getInt("checkclock",2);
        confirmPattern=sp.getString("patrn",null);
        checkfiger=sp.getBoolean("fingerprint",false);
        question=sp.getString("question",null);
        answer=sp.getString("answer",null);

        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        fingerAuth=new FingerAuth(PatternLock.this);
        fingerAuth.setMaxFailedCount(5);
        calendar=Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        date = dateFormat.format(calendar.getTime());
        int flags=View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(flags);

        view = new View(this);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        relativeLayout=new RelativeLayout(this);
        View newview=View.inflate(PatternLock.this, R.layout.activity_pattern_lock,null);
        newview.setSystemUiVisibility(flags);
        mp= MediaPlayer.create(PatternLock.this,R.raw.slide);
        mp.setLooping(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prm = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            prm = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                prm,
                2098560,
                PixelFormat.OPAQUE);
        layoutParams.gravity = 17;
        mParams = layoutParams;

        view=newview;

        patternLockView=(PatternLockView)view.findViewById(R.id.patternLock);
        textWrongPin=(TextView)view.findViewById(R.id.textwrongpin);
        fingerimage=(ImageView)view.findViewById(R.id.imageviewfingerprint);
        txtinstr=(TextView)view.findViewById(R.id.instrution);
        textquestion=(TextView)view.findViewById(R.id.questiontext);
        answerEdit=(EditText)view.findViewById(R.id.editanswer);
        topBanner=(LinearLayout)view.findViewById(R.id.topbanner1);
        dialogConstraint=(ConstraintLayout)view.findViewById(R.id.constraintdialog);
        buttonsubmit=(RelativeLayout)view.findViewById(R.id.submitButtonrel);
        cancelButton=(RelativeLayout)view.findViewById(R.id.denyButtonpatrel);

//        loadadtop();

        answerNow=answerEdit.getText().toString();
        textquestion.setText(question);
        txtinstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkWrong>=5){
                    dialogConstraint.setVisibility(View.VISIBLE);
                }
            }
        });

        if(checkfiger){
            fingerimage.setVisibility(View.VISIBLE);
        }
        textDate=(TextView)view.findViewById(R.id.digitalClock2);
        textDate.setText(date);
        digitalClock=(TextClock)view.findViewById(R.id.digitalClock);

        if(checkstyle==2) {
            digitalClock.setVisibility(View.VISIBLE);
            digitalClock.setFormat12Hour("k:mm ");
        }else if(checkstyle==3){

            digitalClock.setVisibility(View.VISIBLE);
            digitalClock.setFormat24Hour("k:mm ");
        }


        //TODO: to change the style and type of clock textclock

        relativeLayout.addView(view, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        backgroundval=sp.getInt("background1",R.drawable.themeone);
        relativeLayout.setBackgroundResource(backgroundval);
        mWindowManager.addView(relativeLayout, mParams);
        patternLockView.setNormalStateColor(ResourceUtils.getColor(this, R.color.white));
        patternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.white));
        patternLockView.setWrongStateColor(ResourceUtils.getColor(this, R.color.pomegranate));
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                patterncode= PatternLockUtils.patternToString(patternLockView,pattern);
                if(patterncode.equals(confirmPattern)){
                    if(checksound) {
                        mp.start();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                finishAffinity();
                                mWindowManager.removeView(relativeLayout);
                                patternLockView.clearPattern();
                            }
                        });
                    }
                    else {
                        finishAffinity();
                        mWindowManager.removeView(relativeLayout);
                        patternLockView.clearPattern();
                    }

                }
                else {
                    checkWrong++;
                    vibrator.vibrate(400);
                    textWrongPin.setVisibility(View.VISIBLE);
                    patternLockView.clearPattern();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textWrongPin.setVisibility(View.GONE);
                        }
                    },400);
                    if(checkWrong>=5){
                        txtinstr.setText("Forgot Pattern?");
                        txtinstr.setTextColor(getResources().getColor(R.color.red));
                    }
                    Toast.makeText(PatternLock.this, "Wrong Pattern", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCleared() {

            }
        });
        fingerAuth.setOnFingerAuthListener(new FingerAuth.OnFingerAuthListener() {
            @Override
            public void onSuccess() {
                if(checkfiger){
                    if(checksound) {
                        mp.start();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                finishAffinity();
                                mWindowManager.removeView(relativeLayout);
                            }
                        });
                    }
                    else {
                        finishAffinity();
                        mWindowManager.removeView(relativeLayout);
                    }
                }else{
                    Toast.makeText(PatternLock.this, "Not Enabled Kindly Enable from app", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure() {
                n--;
                vibrator.vibrate(250);
                fingerimage.setImageResource(R.drawable.fingerprintred);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fingerimage.setImageResource(R.drawable.ic_baseline_fingerprint_24);
                    }
                },250);

                Toast.makeText(PatternLock.this, "Fingerprint Authentication Failed "+n+" tries left", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError() {
                Toast.makeText(PatternLock.this, "You are out of tries. Now Kindly use Pattern  to Unlock", Toast.LENGTH_SHORT).show();

            }
        });
        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerNow=answerEdit.getText().toString();
                if(answer.equals(answerNow)) {
                    mWindowManager.removeView(relativeLayout);
                    Intent intent = new Intent(PatternLock.this, CofirmPattern.class);
                    startActivity(intent);
                    finishAffinity();
                }else{
                    Toast.makeText(PatternLock.this, "Answer Didn't matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerEdit.setText("");
                dialogConstraint.setVisibility(View.GONE);
            }
        });
        ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(new PatternLock.StateListner2(), 32);
    }
    public class StateListner2 extends PhoneStateListener {
        int prevstate;

        public StateListner2() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case 0:
                    Log.d("PhoneState", "State Idle");
                    if (this.prevstate == 2) {
                        this.prevstate = state;
                        mWindowManager.addView(PatternLock.this.relativeLayout, PatternLock.this.mParams);
                    }
                    if (this.prevstate == 1) {
                        this.prevstate = state;
                        mWindowManager.addView(PatternLock.this.relativeLayout, PatternLock.this.mParams);
                        return;
                    }
                    return;
                case 1:
                    Log.d("PhoneState", "CallRinging");
                    dialogConstraint.setVisibility(View.GONE);
                   mWindowManager.removeView(PatternLock.this.relativeLayout);
                    this.prevstate = state;
                    return;
                case 2:
                    Log.d("PhoneState", "State Off Hook");
                    this.prevstate = state;
                    return;
                default:
                    throw new IllegalStateException("Unexpected value: " + state);
            }
        }
    }
    public void onBackPressed() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 25 || keyCode == 26 || keyCode == 24 || keyCode == 27 || keyCode == 3) {
            return true;
        }
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 24 || event.getKeyCode() == 25 || event.getKeyCode() == 26 || event.getKeyCode() != 3) {
            return false;
        }
        return true;
    }
//    public void loadadtop(){
//        AdView adView=new AdView(PatternLock.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
//        AdListener adListener=new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//
//        topBanner.addView(adView);
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
}