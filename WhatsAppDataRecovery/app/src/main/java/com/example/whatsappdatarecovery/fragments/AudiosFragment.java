package com.example.whatsappdatarecovery.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.adapter.AudioAdapter;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AudiosFragment extends Fragment {
    private static final String TAG ="malik" ;
    private RecyclerView recyclerView;
    TextView textView_deletd_audio;
     AudioAdapter audioAdapter;
    ArrayList<ImagesModelClass> appaudio = new ArrayList<>();
    ArrayList<ImagesModelClass> whatsappaudio = new ArrayList<>();
    ArrayList<ImagesModelClass> deletedaudio = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.chat_fragment_recyclerview);
        GridLayoutManager manger = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manger);
        textView_deletd_audio=view.findViewById(R.id.text_chat_data);
        textView_deletd_audio.setText(R.string.nodata_found_inthis_category);
        textView_deletd_audio.setSelected(true);
        audioAdapter = new AudioAdapter(getContext(), deletedaudio);
        recyclerView.setAdapter(audioAdapter);
        String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Audio";
        String WhatsApp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Audio";
        getAppaudio(new File(path));
        getWhatsappaudio(new File(WhatsApp_path));
        getDeletedaudio();
        audioAdapter.notifyDataSetChanged();
        return view;
    }
    private void getWhatsappaudio(File file) {
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getWhatsappaudio(value);
                } else {
                    Log.d("990", "file name_whatsapp: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    whatsappaudio.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
    private void getDeletedaudio() {
        boolean found;
        for (ImagesModelClass targ : appaudio) {
            found = false;
            for (ImagesModelClass src : whatsappaudio) {
                if (targ.getName().equals(src.getName())) {
                    found = true;
                    Log.d(TAG, "found: ");
                    break;
                }
            }
            if (!found) {
                Log.d(TAG, "getDeletedImages: not found ");
                deletedaudio.add(targ);
            }
        }
        if (deletedaudio.size()==0) {
            textView_deletd_audio.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    private void getAppaudio(File file) {
        Log.d("990", "dir name: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getAppaudio(value);
                } else {
                    Log.d("990", "file name: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    appaudio.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
}