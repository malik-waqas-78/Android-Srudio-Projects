package com.recovery.data.forwhatsapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.facebook.ads.NativeAdLayout;
//import com.google.android.gms.ads.LoadAdError;
import com.recovery.data.forwhatsapp.MainMenuAdapterOKRA;
import com.recovery.data.forwhatsapp.MainMenuModalOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.SliderAdapterExampleOKRA;
import com.recovery.data.forwhatsapp.SliderItemOKRA;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivityOKRA extends AppCompatActivity {
    RecyclerView menuRecycler;
    MainMenuAdapterOKRA mainMenuAdapterOKRA;
    ImageButton setting;
    ArrayList<MainMenuModalOKRA> mainMenuModalOKRAS =new ArrayList<MainMenuModalOKRA>();
    ConstraintLayout cardView_Chats, cardView_Images, cardView_Videos, cardView_Gifs, cardView_Stickers, cardView_Documents, cardView_Voice_Notes, cardView_Audio, cardView_status;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_okra);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Whats Recovery");
//        setSupportActionBar(toolbar);
//        if(AATAdsManager.isAppInstalledFromPlay(this)) {
//            AATAdmobFullAdManager.showAdmobIntersitial(MainActivityAAT.this);
////            loadFbBannerAdd();
////            loadAdmobBanner();
//        }
        //init componenets and data
        /* getSupportActionBar().setTitle("Whats Recovery");*/
        initilize_Componenents();

        //SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapterExampleOKRA adapter = new SliderAdapterExampleOKRA(this);
        adapter.addItem(new SliderItemOKRA(R.drawable.slider1));
        adapter.addItem(new SliderItemOKRA(R.drawable.slider2));
        adapter.addItem(new SliderItemOKRA(R.drawable.slider3));
        adapter.addItem(new SliderItemOKRA(R.drawable.slider4));
//        sliderView.setSliderAdapter(adapter);
//
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//
//        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
//        sliderView.startAutoCycle();
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityOKRA.this, ActivitySettingsOKRA.class);
                startActivity(intent);
            }
        });
        mainMenuModalOKRAS.add(new MainMenuModalOKRA("   Chats   ",R.drawable.ic_chats_icon));
        mainMenuModalOKRAS.add(new MainMenuModalOKRA("   Videos  ",R.drawable.ic_video_icon));
        mainMenuModalOKRAS.add(new MainMenuModalOKRA("   Images  ",R.drawable.ic_images_icon));
        mainMenuModalOKRAS.add(new MainMenuModalOKRA("  Stickers ",R.drawable.ic_sticker_icon));
        mainMenuModalOKRAS.add(new MainMenuModalOKRA("    Gifs   ",R.drawable.ic_gift_icon));
        mainMenuModalOKRAS.add(new MainMenuModalOKRA("   Audios  ",R.drawable.ic_audio_icon));
        mainMenuModalOKRAS.add(new MainMenuModalOKRA(" Documents ",R.drawable.ic_document_icon_for_screen));
        mainMenuModalOKRAS.add(new MainMenuModalOKRA("Voice Notes",R.drawable.ic_voice_note_icon));
        mainMenuModalOKRAS.add(new MainMenuModalOKRA("   Status  ",R.drawable.ic_stataus));
        menuRecycler=(RecyclerView)findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        mainMenuAdapterOKRA =new MainMenuAdapterOKRA(mainMenuModalOKRAS, MainActivityOKRA.this);
        menuRecycler.setLayoutManager(staggeredGridLayoutManager);
        menuRecycler.setAdapter(mainMenuAdapterOKRA);

        mainMenuAdapterOKRA.setOnItemClickListener(new MainMenuAdapterOKRA.OnItemClickListner() {
            @Override
            public void onItemClick(View v, int pos) {
                if(pos==0){
                    if (checkPermissions()) {
                        start_CardView_ChatsActivity();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                        if (preferences.getBoolean("ASKAGAIN", false) == true) {
                            checkAndRequestPermissions();
                        } else {
                            runtimePermissionsWarning("Permission Required", "" +
                                    "This Action needs Storage permission. DO you want to allow it?");
                        }
                    }
                }else if(pos==1){
                    if (checkPermissions()) {
                        start_CardView_VideosActivity();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                        if (preferences.getBoolean("ASKAGAIN", false) == true) {
                            checkAndRequestPermissions();
                        } else {
                            runtimePermissionsWarning("Permission Required", "" +
                                    "This Action needs Storage permission. DO you want to allow it?");
                        }
                    }
                }else if(pos==2){
                    if (checkPermissions()) {
                        start_CardView_ImagesActivity();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                        if (preferences.getBoolean("ASKAGAIN", false) == true) {
                            checkAndRequestPermissions();
                        } else {
                            runtimePermissionsWarning("Permission Required", "" +
                                    "This Action needs Storage permission. DO you want to allow it?");
                        }
                    }
                }else if(pos==3){
                    if (checkPermissions()) {
                        start_CardView_StickersActivity();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                        if (preferences.getBoolean("ASKAGAIN", false) == true) {
                            checkAndRequestPermissions();
                        } else {
                            runtimePermissionsWarning("Permission Required", "" +
                                    "This Action needs Storage permission. DO you want to allow it?");
                        }
                    }
                }else if(pos==4){
                    if (checkPermissions()) {
                        start_CardView_GifsActivity();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                        if (preferences.getBoolean("ASKAGAIN", false) == true) {
                            checkAndRequestPermissions();
                        } else {
                            runtimePermissionsWarning("Permission Required", "" +
                                    "This Action needs Storage permission. DO you want to allow it?");
                        }
                    }
                }else if(pos==5){
                    if (checkPermissions()) {
                        start_CardView_AudiosActivity();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                        if (preferences.getBoolean("ASKAGAIN", false) == true) {
                            checkAndRequestPermissions();
                        } else {
                            runtimePermissionsWarning("Permission Required", "" +
                                    "This Action needs Storage permission. DO you want to allow it?");
                        }
                    }
                }else if(pos==6){
                    if (checkPermissions()) {
                        start_CardView_DocumentsActivity();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                        if (preferences.getBoolean("ASKAGAIN", false) == true) {
                            checkAndRequestPermissions();
                        } else {
                            runtimePermissionsWarning("Permission Required", "" +
                                    "This Action needs Storage permission. DO you want to allow it?");
                        }
                    }
                }else if(pos==7){
                    if (checkPermissions()) {
                        start_CardView_VoiceNotesActivity();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                        if (preferences.getBoolean("ASKAGAIN", false) == true) {
                            checkAndRequestPermissions();
                        } else {
                            runtimePermissionsWarning("Permission Required", "" +
                                    "This Action needs Storage permission. DO you want to allow it?");
                        }
                    }
                }else if(pos==8){
                    if (checkPermissions()) {
                        Intent intent = new Intent(MainActivityOKRA.this, ActivityStatusMainOKRA.class);
                        startActivity(intent);
                    } else {
                        SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                        if (preferences.getBoolean("ASKAGAIN", false) == true) {
                            checkAndRequestPermissions();
                        } else {
                            runtimePermissionsWarning("Permission Required", "" +
                                    "This Action needs Storage permission. DO you want to allow it?");
                        }
                    }
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void initilize_Componenents() {
        cardView_Chats = findViewById(R.id.cardview_Chats);
        cardView_Images = findViewById(R.id.cardview_images);
        cardView_Videos = findViewById(R.id.cardview_videos);
        cardView_Stickers = findViewById(R.id.cardview_stickers);
        cardView_Gifs = findViewById(R.id.cardview_gifs);
        cardView_Documents = findViewById(R.id.cardview_documents);
        cardView_Voice_Notes = findViewById(R.id.cardview_voicenotes);
        cardView_Audio = findViewById(R.id.cardview_audios);
        cardView_status = findViewById(R.id.cardView_status);
        setting=findViewById(R.id.settings);


        //listeners
        cardView_Chats.setOnClickListener(new CardView_Chats_Clicks());
        cardView_Images.setOnClickListener(new CardView_Images_Clicks());
        cardView_Videos.setOnClickListener(new CardView_Videos_Clicks());
        cardView_Gifs.setOnClickListener(new CardView_Gifs_Clicks());
        cardView_Stickers.setOnClickListener(new CardView_Stickers_Clicks());
        cardView_Documents.setOnClickListener(new CardView_Documents_Clicks());
        cardView_Voice_Notes.setOnClickListener(new CardView_VoiceNotes_Clicks());
        cardView_Audio.setOnClickListener(new CardView_Audios_Clicks());

        cardView_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    Intent intent = new Intent(MainActivityOKRA.this, ActivityStatusMainOKRA.class);
                    startActivity(intent);
                } else {
                    SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                    if (preferences.getBoolean("ASKAGAIN", false) == true) {
                        checkAndRequestPermissions();
                    } else {
                        runtimePermissionsWarning("Permission Required", "" +
                                "This Action needs Storage permission. DO you want to allow it?");
                    }
                }

            }
        });
    }


    class CardView_Chats_Clicks implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (checkPermissions()) {
                start_CardView_ChatsActivity();
            } else {
                SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                if (preferences.getBoolean("ASKAGAIN", false) == true) {
                    checkAndRequestPermissions();
                } else {
                    runtimePermissionsWarning("Permission Required", "" +
                            "This Action needs Storage permission. DO you want to allow it?");
                }
            }
        }
    }

    class CardView_Images_Clicks implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (checkPermissions()) {
                start_CardView_ImagesActivity();
            } else {
                SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                if (preferences.getBoolean("ASKAGAIN", false) == true) {
                    checkAndRequestPermissions();
                } else {
                    runtimePermissionsWarning("Permission Required", "" +
                            "This Action needs Storage permission. DO you want to allow it?");
                }
            }
        }
    }

    class CardView_Videos_Clicks implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (checkPermissions()) {
                start_CardView_VideosActivity();
            } else {
                SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                if (preferences.getBoolean("ASKAGAIN", false) == true) {
                    checkAndRequestPermissions();
                } else {
                    runtimePermissionsWarning("Permission Required", "" +
                            "This Action needs Storage permission. DO you want to allow it?");
                }
            }
        }
    }

    class CardView_Gifs_Clicks implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (checkPermissions()) {
                start_CardView_GifsActivity();
            } else {
                SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                if (preferences.getBoolean("ASKAGAIN", false) == true) {
                    checkAndRequestPermissions();
                } else {
                    runtimePermissionsWarning("Permission Required", "" +
                            "This Action needs Storage permission. DO you want to allow it?");
                }
            }
        }
    }

    class CardView_Stickers_Clicks implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (checkPermissions()) {
                start_CardView_StickersActivity();
            } else {
                SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                if (preferences.getBoolean("ASKAGAIN", false) == true) {
                    checkAndRequestPermissions();
                } else {
                    runtimePermissionsWarning("Permission Required", "" +
                            "This Action needs Storage permission. DO you want to allow it?");
                }
            }
        }
    }

    class CardView_Documents_Clicks implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (checkPermissions()) {
                start_CardView_DocumentsActivity();
            } else {
                SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                if (preferences.getBoolean("ASKAGAIN", false) == true) {
                    checkAndRequestPermissions();
                } else {
                    runtimePermissionsWarning("Permission Required", "" +
                            "This Action needs Storage permission. DO you want to allow it?");
                }
            }
        }
    }

    class CardView_VoiceNotes_Clicks implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (checkPermissions()) {
                start_CardView_VoiceNotesActivity();
            } else {
                SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                if (preferences.getBoolean("ASKAGAIN", false) == true) {
                    checkAndRequestPermissions();
                } else {
                    runtimePermissionsWarning("Permission Required", "" +
                            "This Action needs Storage permission. DO you want to allow it?");
                }
            }
        }
    }

    class CardView_Audios_Clicks implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (checkPermissions()) {
                start_CardView_AudiosActivity();
            } else {
                SharedPreferences preferences = getSharedPreferences("MyPermissions", 0);
                if (preferences.getBoolean("ASKAGAIN", false) == true) {
                    checkAndRequestPermissions();
                } else {
                    runtimePermissionsWarning("Permission Required", "" +
                            "This Action needs Storage permission. DO you want to allow it?");
                }
            }
        }
    }

    void runtimePermissionsWarning(String title, String msg) {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialoguebox_layout_okra);
        Button positive = dialog.findViewById(R.id.btn_allow);
        positive.setText("Allow");
        Button negative = dialog.findViewById(R.id.btn_dismiss);
        negative.setText("Dismiss");
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setText(title);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        positive.setOnClickListener(view -> {
            dialog.dismiss();
            ignoreRunTimePermission();
        });
        negative.setOnClickListener(view -> {
            dialog.dismiss();

        });
        dialog.show();
    }

    public void ignoreRunTimePermission() {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + this.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        this.startActivityForResult(i, 978);
    }

    private static final int PERMISSIONS_REQUSTCODE = 927;

    private boolean checkAndRequestPermissions() {
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }
        if (!permissionsNotGranted.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNotGranted
                    .toArray(new String[permissionsNotGranted.size()]), PERMISSIONS_REQUSTCODE);
            return false;
        }

        return true;

    }

    private void start_CardView_AudiosActivity() {
        Intent intent = new Intent(this, ActivityAudiosOKRA.class);
        startActivity(intent);
    }

    private void start_CardView_VoiceNotesActivity() {
        Intent intent = new Intent(this, ActivityVoiceNotesOKRA.class);
        startActivity(intent);
    }

    private void start_CardView_DocumentsActivity() {
        Intent intent = new Intent(this, ActivityDocumentsOKRA.class);
        startActivity(intent);
    }

    private void start_CardView_StickersActivity() {
        Intent intent = new Intent(this, ActivityStickersOKRA.class);
        startActivity(intent);
    }

    private void start_CardView_GifsActivity() {
        Intent intent = new Intent(this, ActivityGifsOKRA.class);
        startActivity(intent);
    }

    private void start_CardView_VideosActivity() {
        Intent intent = new Intent(this, Videos_ActivityOKRA.class);
        startActivity(intent);
    }

    private void start_CardView_ImagesActivity() {
        Intent intent = new Intent(this, ActivityImagesOKRA.class);
        startActivity(intent);
    }

    private void start_CardView_ChatsActivity() {
        Intent intent = new Intent(this, ActivityAllChatsProfilesOKRA.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {

//        AATAdmobFullAdManager.showAdmobIntersitial(MainActivityAAT.this);
        backDialog();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void backDialog() {
        Dialog dialog = new Dialog(MainActivityOKRA.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_exit_okra);
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout exitLayout = dialog.findViewById(R.id.exitLayout);
        RelativeLayout cancelLayout = dialog.findViewById(R.id.cancelLayout);
        RelativeLayout rateusLayout = dialog.findViewById(R.id.rateusLayout);
        rateusLayout.setOnClickListener(view -> {
            rateApp();
        });
        cancelLayout.setOnClickListener(view -> {
            if(!this.isFinishing()&&!this.isDestroyed())
            dialog.dismiss();
        });
        exitLayout.setOnClickListener(view -> {
            finishAffinity();
        });


//        NativeAdLayout nativeAdLayout = dialog.findViewById(R.id.native_ad_container);
//        if (AATAdsManager.nativeAdFacebook != null && AATAdsManager.nativeAdFacebook.isAdLoaded()) {
//            AATAdsManager.inflateAd(
//                    AATAdsManager.nativeAdFacebook,
//                    MainActivityAAT.this,
//                    nativeAdLayout,
//                    R.layout.native_ad_layout
//            );
//        }


        if(!this.isFinishing()&&!this.isDestroyed()){
            dialog.show();
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + Objects.requireNonNull(MainActivityOKRA.this).getPackageName()));
        }
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    private void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details?id=");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=");
            startActivity(rateIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu_okra, menu);
        return true;
    }

    private boolean checkPermissions() {
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }
        if (!permissionsNotGranted.isEmpty()) {
            return false;
        }
        SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("ASKAGAIN", true);
        editor.apply();
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_settings:
                Intent intent = new Intent(MainActivityOKRA.this, ActivitySettingsOKRA.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 978) {
            if (checkPermissions()) {

            } else {
                boolean showRational = shouldShowRequestPermissionRationale(permissions[0]);
                if (!showRational) {
                    //never asked again pressed
                    SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putBoolean("ASKAGAIN", false);
                    editor.apply();
                } else {
                    SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putBoolean("ASKAGAIN", true);
                    editor.apply();
                    Intent intent = new Intent(this, ActivitySettingsOKRA.class);
                    startActivity(intent);
                }

            }
        }
    }

//    public void loadFbBannerAdd() {
//        AdView adView = new AdView(this, getResources().getString(R.string.fbbannerad), AdSize.BANNER_HEIGHT_50);
//
//        AdListener adListener = new AdListener() {
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.d("TAG", "onError: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//
//
//        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
//        relativeLayout.addView(adView);
//
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
//    public void loadAdmobBanner() {
//
//        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(MainActivityAAT.this);
//        com.google.android.gms.ads.AdSize adSize = AATAdmobFullAdManager.getAdSize(MainActivityAAT.this);
//        mAdView.setAdSize(adSize);
//
//        mAdView.setAdUnitId(getResources().getString(R.string.admob_banner));
//
//        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
//
//        final RelativeLayout adViewLayout = (RelativeLayout) findViewById(R.id.bottom_banner);
//        adViewLayout.addView(mAdView);
//
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                adViewLayout.setVisibility(View.GONE);
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
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                adViewLayout.setVisibility(View.VISIBLE);
//            }
//        });
//
//    }

}