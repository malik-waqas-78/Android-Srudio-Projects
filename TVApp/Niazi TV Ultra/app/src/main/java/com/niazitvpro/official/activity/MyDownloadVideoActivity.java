package com.niazitvpro.official.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdView;
import com.niazitvpro.official.R;
import com.niazitvpro.official.adapter.DownloadedAdapter;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.niazitvpro.official.utils.Constants.APP_BACKGROUND_COLOR;

public class MyDownloadVideoActivity extends AppCompatActivity {

    private DownloadedAdapter downloadAdapter;
    private List<File> downloadedList = new ArrayList<>();
    public static RecyclerView recyclerView;
    private File[] files;
    ImageView img_back;
    private String downloadPath;
    private AdView adView;
    private boolean isNullAdded = false;
    private SharedPrefTVApp sharedPrefTVApp;
    private int appBackgroundColor;
    private LinearLayout llMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_download_video);

        SharedPrefTVApp.transparentStatusAndNavigation(MyDownloadVideoActivity.this);

        sharedPrefTVApp = new SharedPrefTVApp(getApplicationContext());

        if (!sharedPrefTVApp.getString(APP_BACKGROUND_COLOR).isEmpty()) {

            appBackgroundColor = Color.parseColor(sharedPrefTVApp.getString(APP_BACKGROUND_COLOR));

        } else {

            appBackgroundColor = R.color.white;
        }

        recyclerView = findViewById(R.id.downloaded_list);
        llMain = findViewById(R.id.ll_main);
        img_back = findViewById(R.id.img_back_myvideo);

        llMain.setBackgroundColor(appBackgroundColor);
        recyclerView.setBackgroundColor(appBackgroundColor);


        try {
            getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getData() throws IOException {

        String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "NiaziTv Pro" + File.separator;

        downloadPath =  new File(mBaseFolderPath).getPath() + "/" ;

        files = new File(downloadPath).listFiles();


        if (files != null) {

            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {

                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });


            for (int i = 0; i < files.length; i++) {
                if (i % 3 == 2) {
                    downloadedList.add(null);
                    isNullAdded = true;

                } else {

                    File file1 = files[i];
                    downloadedList.add(file1);

                }

                if (isNullAdded) {

                    downloadedList.add(files[i]);
                    isNullAdded = false;

                }


            }

        }

        downloadAdapter = new DownloadedAdapter(MyDownloadVideoActivity.this, downloadedList, 1);
        recyclerView.setAdapter(downloadAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));


    }
}