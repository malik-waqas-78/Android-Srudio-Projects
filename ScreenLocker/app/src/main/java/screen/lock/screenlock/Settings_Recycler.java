package screen.lock.screenlock;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.LoadAdError;



import java.util.ArrayList;

public class Settings_Recycler extends AppCompatActivity {
    androidx.biometric.BiometricManager biometricManager;
    RecyclerView recyclerView;
    ArrayList<SettingsModal> settingsModals=new ArrayList<SettingsModal>();
    int type;
    KeyguardManager km;
    SettingsRecyclerAdapter recyclerAdapter;
    String pin,confirmPattern;
    boolean checkSound,checkFinger;
    ImageButton imageButton;
    RelativeLayout fingerbuttonok,fingerbuttoncancel,relButtonok2;
    TextView headingText,messageText;
   // com.google.android.gms.ads.AdView adView;
    ActivityResultLauncher<Intent> mGetContent3 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            type=getSharedPreferences("MySharedPref",MODE_PRIVATE).getInt("type",1);
            Toast.makeText(Settings_Recycler.this, ""+type, Toast.LENGTH_SHORT).show();
        }
    });
    ActivityResultLauncher<Intent> mGetContent4 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            switch (biometricManager.canAuthenticate()){
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    settingsModals.set(1,new SettingsModal(R.drawable.ic_fingprinticon,"Fingerprint","Use Fingerprint to unlock faster",true,false));
                    recyclerAdapter.notifyItemChanged(1);
                    break;
                case BiometricManager.BIOMETRIC_SUCCESS:
                    SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                    myEdit.putBoolean("fingerprint",true);
                    myEdit.apply();
                    Toast.makeText(Settings_Recycler.this, "fingerPrint Enabled", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    });
    ActivityResultLauncher<Intent> mGetContent6 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (km.isKeyguardSecure()) {
                AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(Settings_Recycler.this);
                ViewGroup viewGroup=findViewById(android.R.id.content);
                View view1=LayoutInflater.from(Settings_Recycler.this).inflate(R.layout.defaultlockreturn,viewGroup,false);
                alertDialog4.setView(view1);
                AlertDialog alertDialog2=alertDialog4.create();
                alertDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog2.show();

                relButtonok2=view1.findViewById(R.id.allowrelret);
                relButtonok2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog2.dismiss();
                    }
                });
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_recycler);

        getSupportActionBar().hide();
//        loadAd();
//        loadadtop();
        imageButton=(ImageButton)findViewById(R.id.back1);
        biometricManager= androidx.biometric.BiometricManager.from(this);
        recyclerView=(RecyclerView)findViewById(R.id.settingsRecyclerview);
        pin=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("pin",null);
        type=getSharedPreferences("MySharedPref",MODE_PRIVATE).getInt("type",1);
        confirmPattern=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("patrn",null);
        checkSound=getSharedPreferences("MySharedPref",MODE_PRIVATE).getBoolean("sound",false);
        checkFinger=getSharedPreferences("MySharedPref",MODE_PRIVATE).getBoolean("fingerprint",false);
        km=(KeyguardManager)getSystemService(KEYGUARD_SERVICE);

        settingsModals.add(new SettingsModal(R.drawable.ic_soundicon,"Enable Sound","Enable to play Sound on Screen Unlock",true,checkSound));
        settingsModals.add(new SettingsModal(R.drawable.ic_fingprinticon,"Fingerprint","Use Fingerprint to unlock faster",true,checkFinger));
        settingsModals.add(new SettingsModal(R.drawable.ic_locktypeicon,"Lock Types","Select Pattern/Pin",false,false));
        settingsModals.add(new SettingsModal(R.drawable.ic_changepasswordicon,"Change Password","Pattern or Passcode",false,false));
        settingsModals.add(new SettingsModal(R.drawable.ic_default_lock_icon,"Disable Default Lock","Disable Device's default lock",false,false));
        settingsModals.add(new SettingsModal(R.drawable.ic_wall_clock,"Clock Styles","Clock Style Formats 24hrs / 12 hrs",false,false));
        settingsModals.add(new SettingsModal(R.drawable.ic_rating,"Rate Us","Rate Us Higher to get better services",false,false));

        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
         recyclerAdapter=new SettingsRecyclerAdapter(settingsModals,Settings_Recycler.this);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new SettingsRecyclerAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(View v, int pos) {
                if(pos==2){
                    Intent intent=new Intent(Settings_Recycler.this,LockType.class);
                    mGetContent3.launch(intent);
                }else if(pos==3){
                    if(type==1) {
                        Intent intent = new Intent(Settings_Recycler.this, ConfirmPin.class);
                        startActivity(intent);
                    }
                    else if(type==2){
                        Intent intent = new Intent(Settings_Recycler.this, CofirmPattern.class);
                        startActivity(intent);
                    }
                }else if(pos==4){
                    Intent intent=new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    mGetContent6.launch(intent);
                }else if(pos==5){
                    Intent intent=new Intent(Settings_Recycler.this,ClockTypeSet.class);
                    startActivity(intent);
                }else if(pos==6){
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                }
            }

            @Override
            public void onCheckedchanged(boolean isCheck) {
                if(isCheck){
                    SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                    myEdit.putBoolean("sound",true);
                    myEdit.apply();
                    Toast.makeText(Settings_Recycler.this, "Sound Enabled", Toast.LENGTH_SHORT).show();

                }else {
                    SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                    myEdit.putBoolean("sound",false);
                    myEdit.apply();
                    Toast.makeText(Settings_Recycler.this, "Sound Disabled", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFingerSwitchChanged(boolean ischeck) {
                if(ischeck){
                    switch (biometricManager.canAuthenticate()){
                        case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                            Toast.makeText(Settings_Recycler.this, "Your Device Dont have fingerprint", Toast.LENGTH_SHORT).show();
                            settingsModals.set(1,new SettingsModal(R.drawable.ic_fingprinticon,"Fingerprint","Use Fingerprint to unlock faster",true,false));
                            recyclerAdapter.notifyItemChanged(1);
                            break;
                        case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                            Toast.makeText(Settings_Recycler.this, "Kindly enroll atleast one fingerprint", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(Settings_Recycler.this);
                            ViewGroup viewGroup=findViewById(android.R.id.content);
                            View view1=LayoutInflater.from(Settings_Recycler.this).inflate(R.layout.dialog_finger_enrollment,viewGroup,false);
                            alertDialog4.setView(view1);
                            AlertDialog alertDialog2=alertDialog4.create();
                            alertDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            alertDialog2.show();

                            fingerbuttonok=view1.findViewById(R.id.allowrelfin);
                            fingerbuttoncancel=view1.findViewById(R.id.denyrelfin);
                            fingerbuttonok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog2.dismiss();
                                    Intent intent=new Intent(Settings.ACTION_SECURITY_SETTINGS);
                                    mGetContent4.launch(intent);
                                }
                            });
                            fingerbuttoncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog2.dismiss();
                                    settingsModals.set(1,new SettingsModal(R.drawable.ic_fingprinticon,"Fingerprint","Use Fingerprint to unlock faster",true,false));
                                    recyclerAdapter.notifyItemChanged(1);
                                }
                            });

                            break;
                        case BiometricManager.BIOMETRIC_SUCCESS:
                            SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                            myEdit.putBoolean("fingerprint",true);
                            myEdit.apply();
                            Toast.makeText(Settings_Recycler.this, "fingerPrint Enabled", Toast.LENGTH_SHORT).show();

                            break;
                        case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                            Toast.makeText(Settings_Recycler.this, "Your Device Dont have fingerprint", Toast.LENGTH_SHORT).show();
                            settingsModals.set(1,new SettingsModal(R.drawable.ic_fingprinticon,"Fingerprint","Use Fingerprint to unlock faster",true,false));
                            recyclerAdapter.notifyItemChanged(1);
                            break;
                    }

                }else {
                    SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                    myEdit.putBoolean("fingerprint",false);
                    myEdit.apply();
                    Toast.makeText(Settings_Recycler.this, "fingerprint disabled", Toast.LENGTH_SHORT).show();

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
//        AdView adView=new AdView(Settings_Recycler.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
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
//
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