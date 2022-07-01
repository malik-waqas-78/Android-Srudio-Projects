package com.example.whatsappdatarecovery.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.adapter.StickersFullAdapter;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StickerFragment extends Fragment {
    private static final String TAG = "malik";
    private RecyclerView recyclerView;
    TextView textView;
    StickersFullAdapter adapter;
    ArrayList<ImagesModelClass> appStickers = new ArrayList<>();
    ArrayList<ImagesModelClass> whatsappStickers = new ArrayList<>();
    ArrayList<ImagesModelClass> deletedStickers = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.chat_fragment_recyclerview);
        GridLayoutManager manger = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(manger);
        textView=view.findViewById(R.id.text_chat_data);
        textView.setText(R.string.nodata_found_inthis_category);
        textView.setSelected(true);
        adapter = new StickersFullAdapter(getContext(), deletedStickers);
        recyclerView.setAdapter(adapter);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Stickers";
        String WhatsApp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Stickers";
        getAppImages(new File(path));
        getWhatsappImages(new File(WhatsApp_path));
        getDeletedImages();
        adapter.notifyDataSetChanged();
        return view;
    }
    private void getWhatsappImages(File file) {
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getWhatsappImages(value);
                } else {
                    Log.d("990", "file name_whatsapp: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    whatsappStickers.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
    private void getDeletedImages() {
        boolean found;
        for (ImagesModelClass targ : appStickers) {
            found = false;
            for (ImagesModelClass src : whatsappStickers) {
                if (targ.getName().equals(src.getName())) {
                    found = true;
                    Log.d(TAG, "found: ");
                    break;
                }
            }
            if (!found) {
                Log.d(TAG, "getDeletedImages: not found ");
                deletedStickers.add(targ);
            }
        }
        if (deletedStickers.size()==0) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    private void getAppImages(File file) {
        Log.d("990", "dir name: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getAppImages(value);
                } else {
                    Log.d("990", "file name: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    appStickers.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
}