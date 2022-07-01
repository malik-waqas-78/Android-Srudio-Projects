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
import com.example.whatsappdatarecovery.adapter.VoiceAdapter;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VoiceFragment extends Fragment {
    private static final String TAG = "malik";
    TextView textView;
    ArrayList<ImagesModelClass> appvoice = new ArrayList<>();
    ArrayList<ImagesModelClass> whatsappVoice = new ArrayList<>();
    ArrayList<ImagesModelClass> deletedvoice = new ArrayList<>();
    private RecyclerView recyclerView;
     VoiceAdapter voiceNoteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.chat_fragment_recyclerview);
        GridLayoutManager manger = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manger);
        textView = view.findViewById(R.id.text_chat_data);
        textView.setText(R.string.nodata_found_inthis_category);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSelected(true);
        textView.setSelected(true);
        voiceNoteAdapter = new VoiceAdapter(getContext(), deletedvoice);
        recyclerView.setAdapter(voiceNoteAdapter);
        String path = getContext().getExternalFilesDir(null).getAbsolutePath()  + "/WhatsAppRecovery/Voice Notes";
        String WhatsApp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Voice Notes";
        getAppVoice(new File(path));
        getWhatsappVoice(new File(WhatsApp_path));
        getDeletedVoice();
        voiceNoteAdapter.notifyDataSetChanged();
        return view;
    }

    private void getWhatsappVoice(File file) {
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getWhatsappVoice(value);
                } else {
                    Log.d("990", "file name_whatsapp: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    whatsappVoice.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }

    private void getDeletedVoice() {
        boolean found;
        for (ImagesModelClass targ : appvoice) {
            found = false;
            for (ImagesModelClass src : whatsappVoice) {
                if (targ.getName().equals(src.getName())) {
                    found = true;
                    Log.d(TAG, "found: ");
                    break;
                }
            }
            if (!found) {
                Log.d(TAG, "getDeletedImages: not found ");
                deletedvoice.add(targ);
            }
        }
        if (deletedvoice.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void getAppVoice(File file) {
        Log.d("990", "dir name: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getAppVoice(value);
                } else {
                    Log.d("990", "file name: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    appvoice.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
}