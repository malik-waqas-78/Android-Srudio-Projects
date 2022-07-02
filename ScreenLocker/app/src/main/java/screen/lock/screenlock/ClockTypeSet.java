package screen.lock.screenlock;

import androidx.appcompat.app.AppCompatActivity;

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

public class ClockTypeSet extends AppCompatActivity {
    int clockType;

    ImageButton imageButton;
    RadioButton radioAnalog,radiodigt12,raadiodigit24;
//    com.google.android.gms.ads.AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_type_set);
        getSupportActionBar().hide();
       // loadAd();
//        loadadtop();
        clockType=getSharedPreferences("MySharedPref",MODE_PRIVATE).getInt("checkclock",2);
        radioAnalog=(RadioButton)findViewById(R.id.radioanalog);
        radiodigt12=(RadioButton)findViewById(R.id.radiodgt12);
        raadiodigit24=(RadioButton)findViewById(R.id.radiodigtfour);
        imageButton=(ImageButton)findViewById(R.id.back);

        if(clockType==1){
            radioAnalog.setChecked(true);
            radiodigt12.setChecked(false);
            raadiodigit24.setChecked(false);
        }else if(clockType==2){
            radioAnalog.setChecked(false);
            radiodigt12.setChecked(true);
            raadiodigit24.setChecked(false);
        }else if(clockType==3){
            radioAnalog.setChecked(false);
            radiodigt12.setChecked(false);
            raadiodigit24.setChecked(true);
        }

        radioAnalog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radiodigt12.setChecked(false);
                raadiodigit24.setChecked(false);
                SharedPreferences.Editor myEdit=getSharedPreferences("MySharedPref",MODE_PRIVATE).edit();
                myEdit.putInt("checkclock",1);
                myEdit.apply();
                finish();
            }
        });
        radiodigt12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioAnalog.setChecked(false);
                raadiodigit24.setChecked(false);
                SharedPreferences.Editor myEdit=getSharedPreferences("MySharedPref",MODE_PRIVATE).edit();
                myEdit.putInt("checkclock",2);
                myEdit.apply();
                finish();
            }
        });
        raadiodigit24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioAnalog.setChecked(false);
                radiodigt12.setChecked(false);
                SharedPreferences.Editor myEdit=getSharedPreferences("MySharedPref",MODE_PRIVATE).edit();
                myEdit.putInt("checkclock",3);
                myEdit.apply();
                finish();
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
//        AdView adView=new AdView(ClockTypeSet.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
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
//
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