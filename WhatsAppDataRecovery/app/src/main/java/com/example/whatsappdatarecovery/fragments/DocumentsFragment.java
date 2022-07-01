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
import com.example.whatsappdatarecovery.adapter.DocumentsFullAdapter;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DocumentsFragment extends Fragment {
    private static final String TAG = "malik";
    private RecyclerView recyclerView;
    DocumentsFullAdapter documentsFullAdapterRecyclerView;
    ArrayList<ImagesModelClass> appIdocuments = new ArrayList<>();
    TextView textView_delete_documents;
    ArrayList<ImagesModelClass> whatsappdocuments = new ArrayList<>();
    ArrayList<ImagesModelClass> deleteddocuments = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.chat_fragment_recyclerview);
        GridLayoutManager manger = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(manger);
        textView_delete_documents=view.findViewById(R.id.text_chat_data);
        textView_delete_documents.setText(R.string.nodata_found_inthis_category);
        textView_delete_documents.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView_delete_documents.setSelected(true);
        documentsFullAdapterRecyclerView = new DocumentsFullAdapter(getContext(), deleteddocuments);
        recyclerView.setAdapter(documentsFullAdapterRecyclerView);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Documents";
        String WhatsApp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Documents";
        getAppGifs(new File(path));
        getWhatsappGifs(new File(WhatsApp_path));
        getDeletedGifs();
        documentsFullAdapterRecyclerView.notifyDataSetChanged();
        return view;
    }
    private void getWhatsappGifs(File file) {
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getWhatsappGifs(value);
                } else {
                    Log.d("990", "file name_whatsapp: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    whatsappdocuments.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
    private void getDeletedGifs() {
        boolean found;
        for (ImagesModelClass targ : appIdocuments) {
            found = false;
            for (ImagesModelClass src : whatsappdocuments) {
                if (targ.getName().equals(src.getName())) {
                    found = true;
                    Log.d(TAG, "found: ");
                    break;
                }

            }
            if (!found) {
                Log.d(TAG, "getDeletedImages: not found ");
                deleteddocuments.add(targ);
            }
        }
        if (deleteddocuments.size()==0) {
            textView_delete_documents.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    private void getAppGifs(File file) {
        Log.d("990", "dir name: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    getAppGifs(value);
                } else {
                    Log.d("990", "file name: " + value.getName());
                    Log.d("990", "file name: " + Arrays.toString(value.getPath().getBytes()));
                    appIdocuments.add(new ImagesModelClass(value.getAbsolutePath(), value.getName().toLowerCase()));
                }
            }
        }
    }
}