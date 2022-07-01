package com.example.whatsappdatarecovery.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.adapter.VideoFullAdapter;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideosFragment extends Fragment {
    private static final String TAG = "malik";
    private RecyclerView recyclerView;
    TextView textView;
    VideoFullAdapter videoFullAdapterRecyclerView;
    ArrayList<ImagesModelClass> appIMages = new ArrayList<>();
    ArrayList<ImagesModelClass> whatsappImages = new ArrayList<>();
    ArrayList<ImagesModelClass> deletedImages = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.chat_fragment_recyclerview);
        GridLayoutManager manger = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(manger);
        textView=view.findViewById(R.id.text_chat_data);
        textView.setText(R.string.nodata_found_inthis_category);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSelected(true);
        textView.setSelected(true);
        videoFullAdapterRecyclerView = new VideoFullAdapter(getContext(), deletedImages);
        recyclerView.setAdapter(videoFullAdapterRecyclerView);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Videos";
        String WhatsApp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Video";
        getAppVideos(new File(path));
        getWhatsappVideos(new File(WhatsApp_path));
        getDeletedVideos();
        Log.d(TAG, "delete_images: " + deletedImages.size() + "   " + appIMages.size() + "   " + whatsappImages.size());//lala//ab 1 image ksi or jga sy utha k apny folder my copy kia hy to wo show ho rai hy
        videoFullAdapterRecyclerView.notifyDataSetChanged();
        return view;
    }

    private void getWhatsappVideos(File file) {
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getWhatsappVideos(value);
                } else {
                    Log.d("990", "file name_whatsapp: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    whatsappImages.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
    private void getDeletedVideos() {
        boolean found;
        for (ImagesModelClass targ : appIMages) {
            found = false;
            for (ImagesModelClass src : whatsappImages) {
                if (targ.getName().equals(src.getName())) {
                    found = true;
                    Log.d(TAG, "found: ");
                    break;
                }
            }
            if (!found) {
                deletedImages.add(targ);
            }
        }
        if (deletedImages.size()==0) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    private void getAppVideos(File file) {
        Log.d("990", "dir name: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getAppVideos(value);
                } else {
                    Log.d("990", "file name: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    appIMages.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
}