package screen.lock.screenlock;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdOptionsView;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.facebook.ads.InterstitialAd;
//import com.facebook.ads.InterstitialAdListener;
//import com.facebook.ads.MediaView;
//import com.facebook.ads.NativeAd;
//import com.facebook.ads.NativeAdLayout;
//import com.facebook.ads.NativeAdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.LoadAdError;



import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CardView cardTheme,cardShare,cardSettings;
    boolean checkEnable;
    CircleImageView circleImageView;
//    private NativeAdLayout nativeAdLayout;
//    private LinearLayout linearAdView;
//    private NativeAd nativeAd;
    RelativeLayout relbuttonyes,relbutyeskey,relbuttonNo,relbutnokey,relbuttonrate,relbuttonok;
    TextView messagetext,messagetext2;

    ActivityResultLauncher<Intent> mGetContent6 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (km.isKeyguardSecure()) {
                AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(MainActivity.this);
                ViewGroup viewGroup=findViewById(android.R.id.content);
                View view1=LayoutInflater.from(MainActivity.this).inflate(R.layout.defaultlockreturn,viewGroup,false);
                alertDialog4.setView(view1);
                AlertDialog alertDialog2=alertDialog4.create();
                alertDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog2.show();

                relbuttonok=view1.findViewById(R.id.allowrelret);
                relbuttonok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog2.dismiss();
                    }
                });
            }
        }
    });


    ActivityResultLauncher<Intent> mGetContent4 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            confirmPattern=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("patrn",null);
            if(confirmPattern==null){
                toggleEnable.setChecked(false);
            }else {
                toggleEnable.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(MainActivity.this, ForegroundService.class));
                } else {
                    startService(new Intent(MainActivity.this, ForegroundService.class));
                }
                SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                myEdit.putBoolean("serviceCheck", true);
                myEdit.apply();
                Toast.makeText(MainActivity.this, "Lock Screen Enabled", Toast.LENGTH_SHORT).show();
            }
        }
    });

    ActivityResultLauncher<Intent> mGetContent2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            pin=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("pin",null);
            if(pin==null){
                toggleEnable.setChecked(false);
            }
            else {
                toggleEnable.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(MainActivity.this, ForegroundService.class));
                } else {
                    startService(new Intent(MainActivity.this, ForegroundService.class));
                }
                SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                myEdit.putBoolean("serviceCheck", true);
                myEdit.apply();
                Toast.makeText(MainActivity.this, "Lock Screen Enabled", Toast.LENGTH_SHORT).show();
            }
        }
    });
    Switch toggleEnable;
    String pin;
    int type=1;
    boolean checkkey=true;
    KeyguardManager km;
    String confirmPattern=null;
    LinearLayout linearTopBanner;

//    InterstitialAd interstitialAd;
//    com.google.android.gms.ads.AdView adView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearTopBanner = findViewById(R.id.linearTopBanner);
        // Step 1 - Create an AdView and set the ad unit ID on it.

        //TODO: to change the ad id and place them all in gradle
//        loadAd();
//        //TODO: Banner for admob later in
//        loadadtop();
        //TODO: Interstitial Ads to be loaded seperately in other class
//        loadInterstitial();


        ///////////////////////////////////////////////////////////////////////////
        getSupportActionBar().hide();
        km= (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        checkkey=getSharedPreferences("MySharedPref",MODE_PRIVATE).getBoolean("checkkey",true);
        if(checkkey==true){
            checkkey=false;
        if(km.isKeyguardSecure()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            ViewGroup viewGroup=findViewById(android.R.id.content);
            View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.default_lock,viewGroup,false);
            alertDialog.setView(view);

            AlertDialog alertDialog1=alertDialog.create();
            alertDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog1.show();

            relbutyeskey=view.findViewById(R.id.allowrelkeydef);
            relbutnokey=view.findViewById(R.id.denyreldefkey);

            relbutyeskey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                    mGetContent6.launch(new Intent(Settings.ACTION_SECURITY_SETTINGS));
                }
            });
            relbutnokey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                    AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(MainActivity.this);
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View view1=LayoutInflater.from(MainActivity.this).inflate(R.layout.defaultlockreturn,viewGroup,false);
                    alertDialog4.setView(view1);
                    AlertDialog alertDialog2=alertDialog4.create();
                    alertDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alertDialog2.show();

                    relbuttonok=view1.findViewById(R.id.allowrelret);
                    relbuttonok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog2.dismiss();
                        }
                    });

                }
            });


            }
        }
        pin=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("pin",null);
        type=getSharedPreferences("MySharedPref",MODE_PRIVATE).getInt("type",1);
        confirmPattern=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("patrn",null);

        this.cardTheme = (CardView) findViewById(R.id.cardtheme);
        cardSettings=(CardView)findViewById(R.id.cardsettings);
        cardShare=(CardView)findViewById(R.id.cardshare);
        this.toggleEnable = (Switch) findViewById(R.id.toggleenable);
        this.toggleEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               checkEnable = b;
                if (!b) {
                    stopService(new Intent(MainActivity.this, ForegroundService.class));
                    SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                    myEdit.putBoolean("serviceCheck",false);
                    myEdit.apply();
                    Toast.makeText(MainActivity.this, "Lock Screen Disabled", Toast.LENGTH_SHORT).show();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(MainActivity.this)) {
                            if (type == 1) {
                                if (pin == null) {
                                    Intent intent = new Intent(MainActivity.this, ConfirmPin.class);
                                    mGetContent2.launch(intent);
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        startForegroundService(new Intent(MainActivity.this, ForegroundService.class));
                                    } else {
                                        startService(new Intent(MainActivity.this, ForegroundService.class));
                                    }
                                    SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                                    myEdit.putBoolean("serviceCheck", true);
                                    myEdit.apply();
                                    Toast.makeText(MainActivity.this, "Lock Screen Enabled ", Toast.LENGTH_SHORT).show();
                                }
                            } else if (type == 2) {
                                if (confirmPattern == null) {
                                    Intent intent = new Intent(MainActivity.this, CofirmPattern.class);
                                    mGetContent4.launch(intent);
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        startForegroundService(new Intent(MainActivity.this, ForegroundService.class));
                                    } else {
                                        startService(new Intent(MainActivity.this, ForegroundService.class));
                                    }
                                    SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                                    myEdit.putBoolean("serviceCheck", true);
                                    myEdit.apply();
                                    Toast.makeText(MainActivity.this, "Lock Screen Enabled ", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }else {
                            Toast.makeText(MainActivity.this, "This permission is must for the application to work. Kindly restart and enable this permission", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        if (type == 1) {
                            if (pin == null) {
                                Intent intent = new Intent(MainActivity.this, ConfirmPin.class);
                                mGetContent2.launch(intent);
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startForegroundService(new Intent(MainActivity.this, ForegroundService.class));
                                } else {
                                    startService(new Intent(MainActivity.this, ForegroundService.class));
                                }
                                SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                                myEdit.putBoolean("serviceCheck", true);
                                myEdit.apply();
                                Toast.makeText(MainActivity.this, "Lock Screen Enabled ", Toast.LENGTH_SHORT).show();
                            }
                        } else if (type == 2) {
                            if (confirmPattern == null) {
                                Intent intent = new Intent(MainActivity.this, CofirmPattern.class);
                                mGetContent4.launch(intent);
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startForegroundService(new Intent(MainActivity.this, ForegroundService.class));
                                } else {
                                    startService(new Intent(MainActivity.this, ForegroundService.class));
                                }
                                SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                                myEdit.putBoolean("serviceCheck", true);
                                myEdit.apply();
                                Toast.makeText(MainActivity.this, "Lock Screen Enabled ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                }
            }
        });

        this.cardTheme.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this,Theme.class);
               startActivity(intent);
            }
        });


        cardShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(interstitialAd.isAdInvalidated()&&interstitialAd.isAdLoaded()&&interstitialAd!=null){
//                    interstitialAd.show();
//                return;
//                }
                final String appPackageName = getPackageName();
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("email"));
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL,"http://play.google.com/store/apps/details?id=" + appPackageName);
                i.putExtra(Intent.EXTRA_TEXT,"Share the app");
                Intent chooser=Intent.createChooser(i,"Share App Via ");
                startActivity(chooser);

            }
        });
        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Settings_Recycler.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup=findViewById(android.R.id.content);
        View dialogView1= LayoutInflater.from(MainActivity.this).inflate(R.layout.exit_custom_dialog,viewGroup,false);

        relbuttonyes=dialogView1.findViewById(R.id.allowrel);
        relbuttonNo=dialogView1.findViewById(R.id.denyrel);
        relbuttonrate=dialogView1.findViewById(R.id.raterel);
        messagetext=dialogView1.findViewById(R.id.messagetextexit);
        messagetext2=dialogView1.findViewById(R.id.messagetextexit2);
        //nativeAdLayout =dialogView1.findViewById(R.id.native_ad_container);
        circleImageView=dialogView1.findViewById(R.id.imageviewIconcircular);
//        loadNativeAd();
        builder.setView(dialogView1);
        AlertDialog alertDialog=builder.create();

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        alertDialog.show();

        relbuttonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                MainActivity.super.onBackPressed();
            }
        });
        relbuttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        relbuttonrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

    }

    /* access modifiers changed from: protected */
    public void onResume() {
        toggleEnable.setChecked(getSharedPreferences("MySharedPref", MODE_PRIVATE).getBoolean("checkenable", false));
        pin=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("pin",null);
        type=getSharedPreferences("MySharedPref",MODE_PRIVATE).getInt("type",1);
        confirmPattern=getSharedPreferences("MySharedPref",MODE_PRIVATE).getString("patrn",null);
        super.onResume();
    }
//    public void loadAd(){
//        AdView adView=new AdView(MainActivity.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
//        AdListener adListener=new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.d("bannerAd","Ad Load Failed Banner Bottom Main"+adError.getErrorMessage());
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
//
    /* access modifiers changed from: protected */
    public void onPause() {
        SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
        myEdit.putBoolean("checkenable",checkEnable);
        myEdit.putBoolean("checkkey",checkkey);
        myEdit.apply();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
//    private void loadNativeAd() {
//
//        nativeAd = new NativeAd(MainActivity.this, getApplicationContext().getResources().getString(R.string.fb_native_ad));
//        NativeAdListener nativeAdListener = new NativeAdListener() {
//            @Override
//            public void onMediaDownloaded(Ad ad) {
//            }
//            @Override
//            public void onError(Ad ad, AdError adError) {
//            }
//            @Override
//            public void onAdLoaded(Ad ad) {
//                // Race condition, load() called again before last ad was displayed
//                if (nativeAd == null || nativeAd != ad) {
//                    return;
//                }
//                // Inflate Native Ad into Container
//                inflateAd(nativeAd);
//                messagetext2.setVisibility(View.GONE);
//                messagetext.setVisibility(View.VISIBLE);
//                circleImageView.setVisibility(View.GONE);
//            }
//            @Override
//            public void onAdClicked(Ad ad) {
//            }
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//
//        // Request an ad
//        nativeAd.loadAd(
//                nativeAd.buildLoadAdConfig()
//                        .withAdListener(nativeAdListener)
//                        .build());
//    }
//
//    private void inflateAd(NativeAd nativeAd) {
//
//        nativeAd.unregisterView();
//
//        // Add the Ad view into the ad container.
//
//        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
//        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        linearAdView = (LinearLayout) inflater.inflate(R.layout.fb_native_layout, nativeAdLayout, false);
//        LinearLayout adChoicesContainer =linearAdView. findViewById(R.id.ad_choices_container);
//        nativeAdLayout.addView(linearAdView);
//
//        // Add the AdOptionsView
//
//        AdOptionsView adOptionsView = new AdOptionsView(MainActivity.this, nativeAd, nativeAdLayout);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);
//
//        // Create native UI using the ad metadata.
//        MediaView nativeAdIcon = linearAdView.findViewById(R.id.native_ad_icon);
//        TextView nativeAdTitle = linearAdView.findViewById(R.id.native_ad_title);
//        MediaView nativeAdMedia = linearAdView.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = linearAdView.findViewById(R.id.native_ad_social_context);
//        TextView nativeAdBody = linearAdView.findViewById(R.id.native_ad_body);
//        TextView sponsoredLabel = linearAdView.findViewById(R.id.native_ad_sponsored_label);
//        Button nativeAdCallToAction = linearAdView.findViewById(R.id.native_ad_call_to_action);
//
//        // Set the Text.
//        nativeAdTitle.setText(nativeAd.getAdvertiserName());
//        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
//        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
//        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
//
//        // Create a list of clickable views
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdTitle);
//        clickableViews.add(nativeAdCallToAction);
//
//        // Register the Title and CTA button to listen for clicks.
//        nativeAd.registerViewForInteraction(
//                linearAdView, nativeAdMedia, nativeAdIcon, clickableViews);
//    }
//    public void loadadtop(){
//
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
//        linearTopBanner.addView(adView);
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
//    public void loadInterstitial(){
//        interstitialAd=new InterstitialAd(this,getApplicationContext().getResources().getString(R.string.fb_inter_ad));
//
//        InterstitialAdListener interstitialAdListener=new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//                Log.e("TAG3", "Interstitial ad displayed.");
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//                Log.e("TAG3", "Interstitial ad dismissed.");
//                final String appPackageName = getPackageName();
//                Intent i=new Intent(Intent.ACTION_SEND);
//                i.setData(Uri.parse("email"));
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_EMAIL,"http://play.google.com/store/apps/details?id=" + appPackageName);
//                i.putExtra(Intent.EXTRA_TEXT,"Share the app");
//                Intent chooser=Intent.createChooser(i,"Share App Via ");
//                startActivity(chooser);
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.e("TAG3", "Interstitial ad Failed to load."+adError.getErrorMessage());
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                Log.e("TAG3", "Interstitial ad Loaded.");
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                Log.e("TAG3", "Interstitial ad clicked.");
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                Log.e("TAG3", "Interstitial ad LLogging.");
//            }
//
//        };
//        interstitialAd.loadAd(
//                interstitialAd.buildLoadAdConfig()
//                        .withAdListener(interstitialAdListener)
//                        .build());
//    }

}
