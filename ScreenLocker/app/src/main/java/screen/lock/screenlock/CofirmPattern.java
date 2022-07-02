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

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;

import java.util.List;

public class CofirmPattern extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    PatternLockView patternLockView;
    String patterncode,confirmPattern;
    int state=0;
    TextView textView;
    Vibrator vibrator;
    int backgroundval;

    String[] questions={"Your pet name?","Favorite School Teacher Name?","Bestfriend's last name?","Favorite Video Game?","Your Nickname?"};
    Spinner questionSpinner;
    EditText editAnswer;
    ConstraintLayout constraintsecurity,constraintPin;
    String question,answer;
    RelativeLayout buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cofirm_pattern);
        getSupportActionBar().hide();
//        loadadtop();
        SharedPreferences sp=getSharedPreferences("MySharedPref",MODE_PRIVATE);
        backgroundval=sp.getInt("background1",R.drawable.themeone);
        getWindow().getDecorView().setBackgroundResource(backgroundval);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        textView=findViewById(R.id.instr);
        patternLockView=(PatternLockView)findViewById(R.id.patternLock);
        constraintsecurity=(ConstraintLayout)findViewById(R.id.constrainsecurity1);
        constraintPin=(ConstraintLayout)findViewById(R.id.constraintPin1);
        questionSpinner=(Spinner)findViewById(R.id.questiontext1);
        editAnswer=(EditText)findViewById(R.id.answerEdit1);
        buttonSubmit=(RelativeLayout)findViewById(R.id.submitButtonrel1);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,questions);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionSpinner.setAdapter(aa);

        questionSpinner.setOnItemSelectedListener(this);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer=editAnswer.getText().toString();
                SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                myEdit.putString("patrn",confirmPattern);
                myEdit.putString("question",question);
                myEdit.putString("answer",answer);
                myEdit.apply();
                finish();
            }
        });

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if(state==0){
                    if(PatternLockUtils.patternToString(patternLockView,pattern).length()>=4) {
                        confirmPattern = PatternLockUtils.patternToString(patternLockView, pattern);
                        textView.setText("Re-Enter Pattern to Confirm");
                        state = 1;
                        patternLockView.clearPattern();


                    }
                    else {
                        Toast.makeText(CofirmPattern.this, "Connect Atleast 4 dots", Toast.LENGTH_SHORT).show();
                        patternLockView.clearPattern();
                    }
                }else if(state==1){
                    patterncode=PatternLockUtils.patternToString(patternLockView,pattern);

                    if(patterncode.equals(confirmPattern)){
                        constraintPin.setVisibility(View.INVISIBLE);
                        constraintsecurity.setVisibility(View.VISIBLE);
                    }
                    else {
                        patternLockView.clearPattern();
                        vibrator.vibrate(500);
                        Toast.makeText(CofirmPattern.this, "Pattern Doesn't Match. Retry", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCleared() {

            }
        });
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
//        AdView adView=new AdView(CofirmPattern.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
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
//        LinearLayout adContainer=(LinearLayout)findViewById(R.id.linTopBanner);
//        adContainer.addView(adView);
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
}