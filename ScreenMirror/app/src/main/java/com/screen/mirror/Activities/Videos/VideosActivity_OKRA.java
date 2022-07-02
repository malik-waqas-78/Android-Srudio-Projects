package com.screen.mirror.Activities.Videos;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.screen.mirror.Adapters.Videos.VideosAdapter;
import com.screen.mirror.Dialogs.GeneralDialog;
import com.screen.mirror.Interfaces.GeneralDialogInterface;
import com.screen.mirror.Models.MediaModel;
import com.screen.mirror.R;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.SharedPrefHelperCA;
import com.screen.mirror.databinding.ActivityVideosBinding;

import java.util.ArrayList;
import java.util.Map;

import static com.screen.mirror.Utils.Constants_CA.COLLAPSED;


public class VideosActivity_OKRA extends AppCompatActivity {
    ActivityVideosBinding binding;
    Context context;
    public static final int PERMISSIONS_REQUEST_CODE = 2;
    SharedPrefHelperCA sharedPrefHelperSM;
    String[] perms = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    ArrayList<String> perm_required;
    ArrayList<MediaModel> videosList;
    VideosAdapter adapter;
    private AdView adViewTop,adviewBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ///adloading
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(VideosActivity_OKRA.this)) {
            initSdkandLoadBottomBannerAd();
        }
        context = VideosActivity_OKRA.this;
        sharedPrefHelperSM = new SharedPrefHelperCA(context);
        perm_required = new ArrayList<>();
        videosList = new ArrayList<>();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (CheckStoragePermission()) {
            setData();
        } else {
            storageDialog();
        }

        binding.btnGrantPermission.setOnClickListener(view -> {
            requestPermissions();
            binding.grantPermissionsCardView.setVisibility(View.GONE);
        });
    }
    private boolean CheckStoragePermission() {
        perm_required.clear();
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                perm_required.add(perm);
                return false;
            }
        }
        return true;
    }

    private void setData() {
        getVideos();
        adapter = new VideosAdapter(videosList, context);
        binding.folderViewRecycler.setLayoutManager(new LinearLayoutManager(context));
        binding.folderViewRecycler.setAdapter(adapter);
    }

    private void getVideos() {
        videosList.clear();
        ArrayList<String> temp_folder_list = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_ID};
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                String folderPath = dataPath.substring(0, dataPath.lastIndexOf(folder + "/"));
                folderPath = folderPath + folder + "/";
                if (!temp_folder_list.contains(folderPath)) {
                    temp_folder_list.add(folderPath);
                    videosList.add(new MediaModel(folder, folderPath, COLLAPSED));
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (MediaModel folder : videosList) {
            folder.setFilesInFolder(QueryInFolder(folder.getFolderPath()));
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


    private void storageDialog() {
        GeneralDialog.CreateGeneralDialog(
                context
                , getResources().getString(R.string.storage_permission_title)
                , getResources().getString(R.string.storage_permission_desc)
                , getResources().getString(R.string.allow)
                , getResources().getString(R.string.deny)
                , new GeneralDialogInterface() {
                    @Override
                    public void Positive(Dialog dialog) {
                        requestPermissions();
                    }

                    @Override
                    public void Negative(Dialog dialog) {
                        binding.grantPermissionsCardView.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    private void requestPermissions() {
        if (!perm_required.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this
                    , perm_required.toArray(new String[perm_required.size()])
                    , PERMISSIONS_REQUEST_CODE);
        }
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
               // loadBottomBanner();
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