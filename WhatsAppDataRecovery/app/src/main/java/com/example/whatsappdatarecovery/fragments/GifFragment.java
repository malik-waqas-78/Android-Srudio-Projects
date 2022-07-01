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
import com.example.whatsappdatarecovery.adapter.AnimatedFullAdapter;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GifFragment extends Fragment {
    private static final String TAG = "malik";
    private RecyclerView recyclerView;
    TextView textView;
    AnimatedFullAdapter animatedGifFullAdapterRecyclerView;
    ArrayList<ImagesModelClass> appGifs = new ArrayList<>();
    ArrayList<ImagesModelClass> whatsappGifs = new ArrayList<>();
    ArrayList<ImagesModelClass> deletedGifs = new ArrayList<>();
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
        animatedGifFullAdapterRecyclerView = new AnimatedFullAdapter(getContext(), deletedGifs);
        recyclerView.setAdapter(animatedGifFullAdapterRecyclerView);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Gifs";
        String WhatsApp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Animated Gifs";
        getAppgifs(new File(path));
        getWhatsappgifs(new File(WhatsApp_path));
        getDeletedgifs();
        animatedGifFullAdapterRecyclerView.notifyDataSetChanged();
        return view;
    }

    private void getWhatsappgifs(File file) {
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getWhatsappgifs(value);
                } else {
                    Log.d("990", "file name_whatsapp: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    whatsappGifs.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
    private void getDeletedgifs() {
        boolean found;
        for (ImagesModelClass targ : appGifs) {
            found = false;
            for (ImagesModelClass src : whatsappGifs) {
                if (targ.getName().equals(src.getName())) {
                    found = true;
                    Log.d(TAG, "found: ");
                    break;
                }
            }
            if (!found) {
                deletedGifs.add(targ);
            }
        }
        if (deletedGifs.size()==0) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    private void getAppgifs(File file) {
        Log.d("990", "dir name: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getAppgifs(value);
                } else {
                    Log.d("990", "file name: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    appGifs.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
}