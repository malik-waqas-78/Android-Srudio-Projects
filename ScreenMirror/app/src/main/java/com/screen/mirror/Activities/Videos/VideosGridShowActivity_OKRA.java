package com.screen.mirror.Activities.Videos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.screen.mirror.Activities.Images.ImagesGridShowActivity_OKRA;
import com.screen.mirror.Activities.MainActivity_OKRA;
import com.screen.mirror.Adapters.Videos.GridVideosAdapter;
import com.screen.mirror.Dialogs.ExitDialog;
import com.screen.mirror.Interfaces.ExitDialogInterface;
import com.screen.mirror.R;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.RateUs_CA;
import com.screen.mirror.Utils.SharedPrefHelperCA;
import com.screen.mirror.databinding.ActivityVideosGridShowCaBinding;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static com.screen.mirror.Utils.Constants_CA.FOLDER_NAME;
import static com.screen.mirror.Utils.Constants_CA.FOLDER_PATH;

public class VideosGridShowActivity_OKRA extends AppCompatActivity implements InterAdHelper_OKRA.Companion.AdLoadCallBack {
    ActivityVideosGridShowCaBinding binding;
    Context context;
    SharedPrefHelperCA sharedPrefHelperCA;
    String rootFolderPath;
    private AdView adViewTop,adviewBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showInterstital();
        binding = ActivityVideosGridShowCaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // adloading
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(VideosGridShowActivity_OKRA.this)) {
            initSdkandLoadBottomBannerAd();
        }
        context = VideosGridShowActivity_OKRA.this;
        sharedPrefHelperCA = new SharedPrefHelperCA(context);
        Intent incomingIntent = getIntent();

        if (incomingIntent.hasExtra(FOLDER_NAME)){
            binding.toolbarVideosGrid.setTitle(getIntent().getStringExtra(FOLDER_NAME));
        } else {
            binding.toolbarVideosGrid.setTitle(getResources().getString(R.string.videos));
        }
        setSupportActionBar(binding.toolbarVideosGrid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().hasExtra(FOLDER_PATH)) {
            rootFolderPath = getIntent().getStringExtra(FOLDER_PATH);
        }
        if (rootFolderPath != null) {
            ArrayList<String> list = QueryInFolder(rootFolderPath);
            if (list.size() > 0){
                binding.gridRecyclerViewVideos.setLayoutManager(new GridLayoutManager(context,3));
                binding.gridRecyclerViewVideos.setAdapter(new GridVideosAdapter(context, list));
            }
        }
    }

    private void showInterstital() {

            if(InterAdHelper_OKRA.isAppInstalledFromPlay(VideosGridShowActivity_OKRA.this)) {
                InterAdHelper_OKRA.showAdmobIntersitial(VideosGridShowActivity_OKRA.this);
            }

    }

    private ArrayList<String> QueryInFolder(String folderPath) {
        ArrayList<String> subFolderPaths = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DATE_MODIFIED};
        Cursor cursor = context.getContentResolver().query(
                uri
                , projection
                , MediaStore.Video.Media.DATA + " like ? "
                , new String[]{"%" + folderPath + "%"}
                , null);
        try {
            cursor.moveToFirst();
            do {
                subFolderPaths.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subFolderPaths;
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
    public void initSdkandLoadBottomBannerAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> map = initializationStatus.getAdapterStatusMap();
                for (Map.Entry<String,AdapterStatus> entry : map.entrySet()) {
                    String className = entry.getKey();
                    AdapterStatus status = entry.getValue();
                    if (status.getInitializationState().equals(AdapterStatus.State.NOT_READY)) {

                        // The adapter initialization did not complete.
                        Log.d("92727586243", "Adapter: " + className + " not ready.");
                    }

                    else if (status.getInitializationState().equals(AdapterStatus.State.READY))  {
                        // The adapter was successfully initialized.
                        Log.d("92727586243", "Adapter: " + className + " is initialized.");
                    }

                }
                loadTopBanner();
                //loadBottomBanner();
            }
        });
    }


    private void loadTopBanner() {
        adViewTop = new com.google.android.gms.ads.AdView(this);
        adViewTop.setAdUnitId(getString(R.string.admob_banner));


        com.google.android.gms.ads.AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adViewTop.setAdSize(adSize);
        // Step 5 - Start loading the ad in the background.

        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();
        adViewTop.loadAd(adRequest);
        adViewTop.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
        binding.topBanner.addView(adViewTop);

    }

    @Override
    public void adClosed() {
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(VideosGridShowActivity_OKRA.this)) {
            InterAdHelper_OKRA.loadAdmobInterstitial(VideosGridShowActivity_OKRA.this);
        }
    }
    public com.google.android.gms.ads.AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}