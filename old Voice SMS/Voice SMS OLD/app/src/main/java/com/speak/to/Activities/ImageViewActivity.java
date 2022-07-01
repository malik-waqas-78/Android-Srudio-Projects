package com.speak.to.Activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speak.to.Adapters.ImagesAdapter;
import com.speak.to.Models.Model_Class;
import com.speak.to.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.util.ArrayList;

import static com.speak.to.Utils.Constants.Files_Count;
import static com.speak.to.Utils.Constants.Max_files;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        loadBannerAdd();
//        InterstitialAddsVoiceSMS.showAdd();

        ArrayList<Model_Class> mDataSet = getAllImagesList(this);

        Max_files = Max_files - Files_Count;
        Files_Count = 0;

        RecyclerView recyclerView = findViewById(R.id.image_recycler_view);
        Button btnSelect = findViewById(R.id.btnSelectImage);
        TextView countTextView = findViewById(R.id.textCountImage);

        Context context = this;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        ImagesAdapter imagesAdapter = new ImagesAdapter(context, mDataSet, new imageInterface() {
            @Override
            public void sendCallbacks(int count) {
                countTextView.setText("Files Remaining: " + Files_Count + "/ " + Max_files);
            }
        });
        recyclerView.setAdapter(imagesAdapter);
        btnSelect.setOnClickListener(view -> {
            finish();
        });
    }

    private ArrayList<Model_Class> getAllImagesList(Activity imageViewActivity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<Model_Class> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DISPLAY_NAME};

        String orderby = MediaStore.Images.ImageColumns.DATE_TAKEN ;

        cursor = imageViewActivity.getContentResolver().query(uri, projection, null,
                null, orderby);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(new Model_Class(absolutePathOfImage, false));
        }
        return listOfAllImages;
    }

    public interface imageInterface {
        void sendCallbacks(int count);
    }

    public void loadBannerAdd() {
        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);

        AdListener adListener = new AdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAG", "onError: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d("TAG", "Ad loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
        relativeLayout.addView(adView);
    }
}