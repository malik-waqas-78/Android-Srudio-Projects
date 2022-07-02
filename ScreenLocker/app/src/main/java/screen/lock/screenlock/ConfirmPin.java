package screen.lock.screenlock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;



//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;

public class ConfirmPin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RelativeLayout btnok,btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnback;
    RelativeLayout rd1,rd2,rd3,rd4;
    TextView textView;

    String[] questions={"Your pet name?","Favorite School Teacher Name?","Bestfriend's last name?","Favorite Video Game?","Your Nickname"};
    int pinsize=0,checkInput;
    String pin,confirmpin;
    int statecheck=0;
    int backgroundval;
    Vibrator vibrator;
    Spinner questionSpinner;
    EditText editAnswer;
    ConstraintLayout constraintsecurity,constraintPin;
    String question,answer;
    RelativeLayout buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pin);
//        loadadtop();
        SharedPreferences sp=getSharedPreferences("MySharedPref",MODE_PRIVATE);
        backgroundval=sp.getInt("background1",R.drawable.themeone);
        getWindow().getDecorView().setBackgroundResource(backgroundval);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);

        textView=(TextView)findViewById(R.id.instr);
        btnok=(RelativeLayout) findViewById(R.id.ok);
        btn0=(RelativeLayout) findViewById(R.id.zero);
        btn1=(RelativeLayout) findViewById(R.id.one);
        btn2=(RelativeLayout) findViewById(R.id.two);
        btn3=(RelativeLayout) findViewById(R.id.three);
        btn4=(RelativeLayout) findViewById(R.id.four);
        btn5=(RelativeLayout) findViewById(R.id.five);
        btn6=(RelativeLayout) findViewById(R.id.six);
        btn7=(RelativeLayout) findViewById(R.id.seven);
        btn8=(RelativeLayout) findViewById(R.id.eight);
        btn9=(RelativeLayout) findViewById(R.id.nine);
        btnback=(RelativeLayout) findViewById(R.id.cancel);
        rd1=(RelativeLayout) findViewById(R.id.radio1);
        rd2=(RelativeLayout) findViewById(R.id.radio2);
        rd3=(RelativeLayout) findViewById(R.id.radio3);
        rd4=(RelativeLayout) findViewById(R.id.radio4);
        constraintsecurity=(ConstraintLayout)findViewById(R.id.constrainsecurity);
        constraintPin=(ConstraintLayout)findViewById(R.id.constraintPin);
        questionSpinner=(Spinner)findViewById(R.id.questiontext);
        editAnswer=(EditText)findViewById(R.id.answerEdit);
        buttonSubmit=(RelativeLayout)findViewById(R.id.submitButtonrel);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer=editAnswer.getText().toString();
                SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                myEdit.putString("pin", pin);
                myEdit.putString("question",question);
                myEdit.putString("answer",answer);
                myEdit.apply();
                finish();
            }
        });

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,questions);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        questionSpinner.setAdapter(aa);
        questionSpinner.setOnItemSelectedListener(this);

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
    }
    public void Check(int pinSize,String num){
        if(statecheck==0) {
            textView.setText("Enter New PIN");
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
                statecheck=1;
                confirmpin=pin;
                textView.setText("Confirm PIN");
                rd1.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                rd2.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                rd3.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                rd4.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                pin=null;
                pinsize=0;
            }
        }else if(statecheck==1){
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
                if(pin.equals(confirmpin)) {
                    constraintPin.setVisibility(View.INVISIBLE);
                    constraintsecurity.setVisibility(View.VISIBLE);
                }else{
                    vibrator.vibrate(500);
                    rd1.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    rd2.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    rd3.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    rd4.setBackgroundResource(R.drawable.ic_pindotbackgroundempty);
                    pin=null;
                    pinsize=0;
                    Toast.makeText(this, "PIN Doesn't Matched Retry", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        question=questions[position];
        answer=editAnswer.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
//    public void loadadtop(){
//        AdView adView=new AdView(ConfirmPin.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
//        AdListener adListener=new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.d("bannerAd","Ad Load Failed "+adError.getErrorMessage());
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
//        LinearLayout adContainer=(LinearLayout)findViewById(R.id.LinTopBanner);
//        adContainer.addView(adView);
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
}