package screen.lock.screenlock;

import android.content.Context;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
//
//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.google.android.material.button.MaterialButton;

import com.marcoscg.fingerauth.FingerAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LockScreenActivity extends AppCompatActivity {
    FingerAuth fingerAuth;
    LayoutInflater inflater;
    public WindowManager.LayoutParams mParams;
    public WindowManager mWindowManager;
    RelativeLayout relativeLayout;
    View view;
    ConstraintLayout dialogConstraint;
    int n=5,checkWrong=0;
    boolean checkenable,checkService,checksound,checkfinger;
    int checkstyle=1;
    int backgroundval;
    TextView textdate,tvInstruction;
    ImageView fingerimage;
    LinearLayout pinlinear;
    TextView questionTxt;
    /* access modifiers changed from: protected */
    AudioManager audioManager;
    MediaPlayer mp;
    TextClock digitalClock;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    AnalogClock analogClock;
    RelativeLayout btnok,btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnback;
    RelativeLayout rd1,rd2,rd3,rd4,relButtonSubmit;
    TextView textView;
    String question,answer,answerNow;
    int pinsize=0;
    RelativeLayout buttonSubmit,cancelButton;
    Vibrator vibrator;
    LinearLayout topBannner;
    EditText editAnswer;
    String pin,confirmpin=null;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp=getSharedPreferences("MySharedPref", MODE_PRIVATE);
        checkenable=sp.getBoolean("checkenable", false);
        checkService=sp.getBoolean("serviceCheck",false);
        checksound=sp.getBoolean("sound",false);
        checkstyle=sp.getInt("checkclock",1);
        confirmpin=sp.getString("pin",null);
        checkfinger=sp.getBoolean("fingerprint",false);
        question=sp.getString("question",null);
        answer=sp.getString("answer",null);

        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        fingerAuth=new FingerAuth(LockScreenActivity.this);
        fingerAuth.setMaxFailedCount(5);
        calendar=Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        date = dateFormat.format(calendar.getTime());
        int prm;
        mp= MediaPlayer.create(LockScreenActivity.this, R.raw.slide);
        mp.setLooping(false);

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
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        relativeLayout=new RelativeLayout(this);
        View newview=View.inflate(LockScreenActivity.this,R.layout.activity_lockscreen,null);
        newview.setSystemUiVisibility(flags);
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

        textView=(TextView)view.findViewById(R.id.tvInstructions);
        textdate=(TextView)view.findViewById(R.id.textviewDate);
        btnok=(RelativeLayout) view.findViewById(R.id.ok);
        btn0=(RelativeLayout) view.findViewById(R.id.zero1);
        btn1=(RelativeLayout) view.findViewById(R.id.one1);
        btn2=(RelativeLayout) view.findViewById(R.id.two1);
        btn3=(RelativeLayout) view.findViewById(R.id.three1);
        btn4=(RelativeLayout) view.findViewById(R.id.four1);
        btn5=(RelativeLayout) view.findViewById(R.id.five1);
        btn6=(RelativeLayout) view.findViewById(R.id.six1);
        btn7=(RelativeLayout) view.findViewById(R.id.seven1);
        btn8=(RelativeLayout) view.findViewById(R.id.eight1);
        btn9=(RelativeLayout) view.findViewById(R.id.nine1);
        btnback=(RelativeLayout) view.findViewById(R.id.cancel1);
        rd1=(RelativeLayout) view.findViewById(R.id.radio1);
        rd2=(RelativeLayout) view.findViewById(R.id.radio2);
        rd3=(RelativeLayout) view.findViewById(R.id.radio3);
        rd4=(RelativeLayout) view.findViewById(R.id.radio4);
        pinlinear=(LinearLayout)view.findViewById(R.id.pinlinear);
        fingerimage=(ImageView)view.findViewById(R.id.fingerimage);
        tvInstruction=(TextView) view.findViewById(R.id.tvInstructions);
        editAnswer=(EditText)view.findViewById(R.id.editanswer);
        topBannner=(LinearLayout) view.findViewById(R.id.topbanner);

//        loadadtop();

        answerNow=editAnswer.getText().toString();



        //TODO:security quesiton and edit text to place the answer and submit
        questionTxt=(TextView)view.findViewById(R.id.questiontext);
        questionTxt.setText(question);

        dialogConstraint=(ConstraintLayout)view.findViewById(R.id.constraintdialog);
        buttonSubmit=(RelativeLayout)view.findViewById(R.id.submitButtonrel);
        cancelButton=(RelativeLayout)view.findViewById(R.id.denyButtonrel);


        tvInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkWrong>=5){
                   dialogConstraint.setVisibility(View.VISIBLE);
                }
            }
        });

        digitalClock=(TextClock) view.findViewById(R.id.digitalClock);
        textdate.setText(date);

       if(checkstyle==2) {

            digitalClock.setVisibility(View.VISIBLE);
            digitalClock.setFormat12Hour("k:mm ");
        }else if(checkstyle==3){

            digitalClock.setVisibility(View.VISIBLE);
            digitalClock.setFormat24Hour("k:mm ");
        }
       if(checkfinger){
           fingerimage.setVisibility(View.VISIBLE);
       }
      relativeLayout.addView(view, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
      backgroundval=sp.getInt("background1",R.drawable.themeone);
      relativeLayout.setBackgroundResource(backgroundval);

        mWindowManager.addView(relativeLayout, mParams);

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"0");
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"2");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"3");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"4");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"5");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"6");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"7");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"8");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinsize++;
                Check(pinsize,"9");
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinsize>0) {

                    if (pinsize == 1) {
                        rd1.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    } else if (pinsize == 2) {
                        rd2.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    } else if (pinsize == 3) {
                        rd3.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    } else if (pinsize == 4) {
                        rd4.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    }
                    pinsize--;
                    pin = pin.substring(0, pin.length() - 1);
                }
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd1.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                rd2.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                rd3.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                rd4.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                pin=null;
                pinsize=0;
//                mWindowManager.removeView(relativeLayout);
//                finishAffinity();
            }
        });
        fingerAuth.setOnFingerAuthListener(new FingerAuth.OnFingerAuthListener() {
            @Override
            public void onSuccess() {
                if(checkfinger){
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
                    Toast.makeText(LockScreenActivity.this, "Not Enabled Kindly Enable from app", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LockScreenActivity.this, "Fingerprint Authentication Failed "+n+" tries left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(LockScreenActivity.this, "You are out of tries. Now Kindly use PIN  to Unlock", Toast.LENGTH_SHORT).show();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerNow=editAnswer.getText().toString();
                if(answerNow.equals(answer)){
                    mWindowManager.removeView(relativeLayout);
                    Intent intent=new Intent(LockScreenActivity.this,ConfirmPin.class);
                    startActivity(intent);
                    finishAffinity();
                }else{
                    Toast.makeText(LockScreenActivity.this, "Answer Didn't matched", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAnswer.setText("");
                dialogConstraint.setVisibility(View.GONE);
            }
        });
        ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(new StateListner2(), 32);
    }
    public void Check(int pinSize,String num){

            if (pinSize == 1) {
                rd1.setBackgroundResource(R.drawable.ic_pindotbackgroundfilled);
                pin = num;
            } else if (pinSize == 2) {
                rd2.setBackgroundResource(R.drawable.ic_pindotbackgroundfilled);
                pin += num;
            } else if (pinSize == 3) {
                rd3.setBackgroundResource(R.drawable.ic_pindotbackgroundfilled);
                pin += num;
            } else if (pinSize == 4) {
                rd4.setBackgroundResource(R.drawable.ic_pindotbackgroundfilled);
                pin += num;
                if(pin.equals(confirmpin)){
                    rd1.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    rd2.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    rd3.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    rd4.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    pin=null;
                    pinsize=0;
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
                }
                else {
                    checkWrong++;
                    vibrator.vibrate(250);
                    pinlinear.setBackgroundResource(R.drawable.pinback2);
                    rd1.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    rd2.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    rd3.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    rd4.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    pin=null;
                    pinsize=0;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pinlinear.setBackgroundResource(R.drawable.pinback);
                        }
                    },250);
                    if(checkWrong>=5){
                        tvInstruction.setText("Forgot PIN?");
                        tvInstruction.setTextColor(getResources().getColor(R.color.red));
                    }

                }
        }
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
                        LockScreenActivity.this.mWindowManager.addView(LockScreenActivity.this.relativeLayout, LockScreenActivity.this.mParams);
                    }
                    if (this.prevstate == 1) {
                        this.prevstate = state;
                        LockScreenActivity.this.mWindowManager.addView(LockScreenActivity.this.relativeLayout, LockScreenActivity.this.mParams);
                        return;
                    }
                    return;
                case 1:
                    Log.d("PhoneState", "CallRinging");
                    dialogConstraint.setVisibility(View.GONE);
                    LockScreenActivity.this.mWindowManager.removeView(LockScreenActivity.this.relativeLayout);
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
//    public void loadadtop(){
//        AdView adView=new AdView(LockScreenActivity.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
//        AdListener adListener=new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.d("bannerAd","Ad Load Failed "+adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                Log.d("bannerAd","Ad Loaded Successfully");
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
//        topBannner.addView(adView);
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }

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
}
