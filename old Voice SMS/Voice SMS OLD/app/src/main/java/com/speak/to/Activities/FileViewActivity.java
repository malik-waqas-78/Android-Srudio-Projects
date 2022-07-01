package com.speak.to.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.speak.to.Adapters.FilesAdapter;
import com.speak.to.Models.Model_Class;
import com.speak.to.R;

import java.io.File;
import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.speak.to.Utils.Constants.Files_Count;
import static com.speak.to.Utils.Constants.Max_files;
import static com.speak.to.Utils.Constants.files_list;

public class FileViewActivity extends AppCompatActivity {
    ArrayList<Model_Class> listFiles;
    Context context;
    RecyclerView recyclerView;
    Button btnSelect;
    TextView countTextView;
    ProgressBar progressBar;
    int Current_object_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);

        loadBannerAdd();
//        InterstitialAddsVoiceSMS.showAdd();
        Current_object_count = 0;

        recyclerView = findViewById(R.id.file_recycler_view);
        btnSelect = findViewById(R.id.btnSelect);
        btnSelect.setVisibility(View.INVISIBLE);
        countTextView = findViewById(R.id.textCount);
        progressBar = findViewById(R.id.file_progress_bar);
        context = this;

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new AsyncFileLoader().execute();
    }

    private void initRecyclerView(ArrayList<Model_Class> listFiles) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Max_files = Max_files - Files_Count;
        Files_Count = 0;

        FilesAdapter filesAdapter = new FilesAdapter(context, listFiles, new Files_Interface() {
            int current_count = 0;

            @SuppressLint("SetTextI18n")
            @Override
            public void sendCallbacksFromFileActivity(int count) {
                current_count += 1;
                Current_object_count = current_count;
                if (current_count > 0) {
                    btnSelect.setVisibility(View.VISIBLE);
                } else {
                    btnSelect.setVisibility(View.INVISIBLE);
                }
                countTextView.setText("Files Remaining: " + Files_Count + "/ " + Max_files);
            }
        });
        recyclerView.setAdapter(filesAdapter);
    }

    private ArrayList<Model_Class> getFilesList(File dir, ArrayList<Model_Class> listFiles) {
        String extension = ".pdf";
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    getFilesList(fileList[i], listFiles);
                } else {
                    if (fileList[i].getName().endsWith(extension)) {
                        listFiles.add(new Model_Class(fileList[i].getAbsolutePath(), false));
                    }
                }
            }
        }
        return listFiles;
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

    @Override
    public void onBackPressed() {
        if (Current_object_count > 0) {
            try {
                int start = files_list.size() - Current_object_count;
                int end = files_list.size();
                for (int i = start; i < end; i++) {
                    files_list.remove(files_list.size() - 1);
                    Files_Count--;
                    Max_files++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onBackPressed();
    }

    public interface Files_Interface {
        void sendCallbacksFromFileActivity(int count);
    }

    private class AsyncFileLoader extends AsyncTask<File, Integer, ArrayList<Model_Class>> {
        @Override
        protected ArrayList<Model_Class> doInBackground(File... files) {
            return getFilesList(Environment.getExternalStorageDirectory(), new ArrayList<Model_Class>());
        }

        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(INVISIBLE);
            progressBar.setVisibility(VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Model_Class> model_classes) {
            super.onPostExecute(model_classes);
            progressBar.setVisibility(INVISIBLE);
            recyclerView.setVisibility(VISIBLE);
            initRecyclerView(model_classes);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }
}