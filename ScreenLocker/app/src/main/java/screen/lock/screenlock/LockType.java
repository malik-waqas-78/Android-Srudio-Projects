package screen.lock.screenlock;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;



//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.LoadAdError;

public class LockType extends AppCompatActivity {
    RadioButton radiopin,radiopattern;
    int type;
    String pin=null;
    String confirmPattern=null;
//    com.google.android.gms.ads.AdView adView;
    ActivityResultLauncher<Intent> mGetContent4 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            confirmPattern=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("patrn",null);
           if(confirmPattern==null){
               radiopattern.setChecked(false);
               radiopin.setChecked(true);
               SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
               myEdit.putInt("type", 1);
               myEdit.apply();
               finish();
           }
           else {
               radiopin.setChecked(false);
               SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
               myEdit.putInt("type", 2);
               myEdit.apply();
               finish();
           }
        }
    });
    ActivityResultLauncher<Intent> mGetContent2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            pin=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("pin",null);
            if(pin==null){
                radiopin.setChecked(false);
                radiopattern.setChecked(true);
                SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                myEdit.putInt("type", 2);
                myEdit.apply();
                finish();
            }else {
                radiopattern.setChecked(false);
                SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                myEdit.putInt("type", 1);
                myEdit.apply();
                finish();
            }
        }
    });
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_type);

        getSupportActionBar().hide();
//        loadAd();
//        loadadtop();
        radiopin=(RadioButton)findViewById(R.id.radiopin);
        radiopattern=(RadioButton)findViewById(R.id.radiopattern);
        imageButton=(ImageButton)findViewById(R.id.back);
        pin=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("pin",null);
        type=getSharedPreferences("MySharedPref",MODE_PRIVATE).getInt("type",1);
        confirmPattern=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("patrn",null);

        if(type==1){
            radiopin.setChecked(true);
        }else if(type==2){
            radiopattern.setChecked(true);
        }


        radiopin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(pin==null){
                        Intent intent=new Intent(LockType.this,ConfirmPin.class);
                        mGetContent2.launch(intent);
                    }else {
                        radiopattern.setChecked(false);
                        SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                        myEdit.putInt("type", 1);
                        myEdit.apply();
                        finish();
                    }
                }


            }
        });
        radiopattern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(confirmPattern==null){
                        Intent intent=new Intent(LockType.this,CofirmPattern.class);
                        mGetContent4.launch(intent);
                    }
                    else {
                        radiopin.setChecked(false);
                        SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                        myEdit.putInt("type", 2);
                        myEdit.apply();
                        finish();
                    }
                }

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
//    public void loadAd(){
//        AdView adView=new AdView(LockType.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
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
//        LinearLayout adContainer=(LinearLayout)findViewById(R.id.bannerAdContainer);
//        adContainer.addView(adView);
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
//    public void loadadtop(){
//        adView = new com.google.android.gms.ads.AdView(this);
//        adView.setAdUnitId(getString(R.string.admob_banner_ad));
//
//
//        com.google.android.gms.ads.AdSize adSize = getAdSize();
//        // Step 4 - Set the adaptive ad size on the ad view.
//        adView.setAdSize(adSize);
//        // Step 5 - Start loading the ad in the background.
//
//
//        AdRequest adRequest =
//                new AdRequest.Builder()
//                        .build();
//        adView.loadAd(adRequest);
//        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//            }
//
//            @Override
//            public void onAdClicked() {
//                super.onAdClicked();
//            }
//
//            @Override
//            public void onAdImpression() {
//                super.onAdImpression();
//            }
//        });
//        LinearLayout adContainer=(LinearLayout)findViewById(R.id.lineartopbanner);
//        adContainer.addView(adView);
//
//    }
//    public com.google.android.gms.ads.AdSize getAdSize() {
//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float widthPixels = outMetrics.widthPixels;
//        float density = outMetrics.density;
//
//        int adWidth = (int) (widthPixels / density);
//
//        // Step 3 - Get adaptive ad size and return for setting on the ad view.
//        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
//    }
}