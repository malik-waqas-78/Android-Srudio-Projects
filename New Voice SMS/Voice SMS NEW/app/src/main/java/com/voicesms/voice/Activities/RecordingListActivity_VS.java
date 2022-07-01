package com.voicesms.voice.Activities;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.voicesms.voice.Adapters.Recording_List_Adapter;
import com.voicesms.voice.Interfaces.RecordingListInterface;
import com.voicesms.voice.Models.Recording_List_Item;
import com.voicesms.voice.R;
import com.voicesms.voice.databinding.ActivityRecordingListBinding;

import java.io.File;
import java.util.ArrayList;

public class RecordingListActivity_VS extends AppCompatActivity {
    ActivityRecordingListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        loadBannerAdd();

        setSupportActionBar(binding.toolbarRecordingsList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.toolbarRecordingsList.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private ArrayList<Recording_List_Item> getFilesList() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/" + getResources().getString(R.string.app_name) + "/Recordings";
        File root_file = new File(root);
        File[] files = root_file.listFiles();
        ArrayList<Recording_List_Item> filesList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                filesList.add(new Recording_List_Item(file.getAbsolutePath(), file.getName()));
            }
            if (filesList.size() > 0) {
                binding.textEmptyListview.setVisibility(View.GONE);
                binding.recyclerViewRecordingList.setVisibility(View.VISIBLE);
            } else {
                binding.textEmptyListview.setVisibility(View.VISIBLE);
                binding.recyclerViewRecordingList.setVisibility(View.GONE);
                filesList = new ArrayList<>();
            }
        }
        return filesList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Recording_List_Item> recordingList = getFilesList();
        if (recordingList.size() > 0) {
            Recording_List_Adapter adapter = new Recording_List_Adapter(this, recordingList, new RecordingListInterface() {
                @Override
                public void UpdateList(int listSize) {
                    if (listSize == 0) {
                        binding.recyclerViewRecordingList.setVisibility(View.GONE);
                        binding.textEmptyListview.setVisibility(View.VISIBLE);
                    }
                }
            });
            binding.recyclerViewRecordingList.setAdapter(adapter);
        }
    }

//    public void loadBannerAdd() {
//        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);
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
//                Log.d("TAG", "Ad loaded");
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
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
//        relativeLayout.addView(adView);
//    }
}