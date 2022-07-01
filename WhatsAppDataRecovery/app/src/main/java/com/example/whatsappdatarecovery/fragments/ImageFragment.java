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
import com.example.whatsappdatarecovery.adapter.ImageFullAdapter;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageFragment extends Fragment {
    private static final String TAG = "malik";
    private RecyclerView recyclerView;
    TextView deletd_data;
    ImageFullAdapter adapter;
    ArrayList<ImagesModelClass> appImages = new ArrayList<>();
    ArrayList<ImagesModelClass> whatsappImages = new ArrayList<>();
    ArrayList<ImagesModelClass> deletedImages = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.chat_fragment_recyclerview);
        GridLayoutManager manger = new GridLayoutManager(getContext(), 4);
        deletd_data = view.findViewById(R.id.text_chat_data);
        deletd_data.setText(R.string.nodata_found_inthis_category);
        deletd_data.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        deletd_data.setSelected(true);
        deletd_data.setSelected(true);
        recyclerView.setLayoutManager(manger);
        adapter = new ImageFullAdapter(getContext(), deletedImages);
        recyclerView.setAdapter(adapter);
        String path =getContext().getExternalFilesDir(null).getAbsolutePath()+ "/WhatsAppRecovery/Images";
        String WhatsApp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Images";
        getAppImages(new File(path));
        getWhatsappImages(new File(WhatsApp_path));
        getDeletedImages();
        Log.d(TAG, "delete_images: " + deletedImages.size() + "   " + appImages.size() + "   " + whatsappImages.size());
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
                    whatsappImages.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
    private void getDeletedImages() {
        boolean found;
        for (ImagesModelClass targ : appImages) {
            found = false;
            for (ImagesModelClass src : whatsappImages) {
                if (targ.getName().equals(src.getName())) {
                    deletd_data.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    found = true;
                    Log.d(TAG, "found:");
                    break;
                }
            }
            if (!found) {
                Log.d(TAG, "found_deleted_images"+"deletedImages.size()");
                deletedImages.add(targ);
                Log.d("990", "file name11234: " + targ.getName());
            }
        }
        if (deletedImages.size()==0) {
            deletd_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    private void getAppImages(File file) {
        Log.d("990", "dir name123: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getAppImages(value);
                    Log.d("990", "file name11234: " + value.getName());
                } else {
                    Log.d("990", "file name112344: " + value.getName());
                    Log.d("990", "file name11: " + Arrays.toString(value.getPath().getBytes()));
                    appImages.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                    Log.d(TAG, "found_deleted_images"+value.getName());
                }
            }
        }
    }
}