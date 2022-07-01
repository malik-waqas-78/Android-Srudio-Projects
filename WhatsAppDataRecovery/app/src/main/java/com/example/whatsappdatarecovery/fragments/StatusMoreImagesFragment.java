package com.example.whatsappdatarecovery.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.adapter.StatusMoreImageFullAdapter;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StatusMoreImagesFragment extends Fragment {
    private RecyclerView recyclerView_images;
    StatusMoreImageFullAdapter statusMoreImageFullAdapterRecyclerView;
     GridLayoutManager layoutManager;
    private ProgressBar progressBar_status;
    private TextView image_text;
    ArrayList<ImagesModelClass> status_images = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_image, container, false);
        recyclerView_images = view.findViewById(R.id.status_image_fragment_recyclerview);
        progressBar_status = view.findViewById(R.id.progressbar_more_images);
        image_text = view.findViewById(R.id.more_images_text);
        image_text.setText(R.string.images);
        new Load_All_data_progressbar().execute();
        setImageRecyclerviewAdapter();
        return view;
    }
    private void setImageRecyclerviewAdapter() {
        statusMoreImageFullAdapterRecyclerView = new StatusMoreImageFullAdapter(getContext(), status_images);
        layoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView_images.setLayoutManager(layoutManager);
        recyclerView_images.setAdapter(statusMoreImageFullAdapterRecyclerView);
    }
    private void get_Status_Data() {
        String image_path =  getContext().getExternalFilesDir(null).getAbsolutePath()  + "/WhatsAppRecovery/Status/Images";
        status_images.clear();
        load_Directory_files(new File(image_path));
    }
    public void load_Directory_files(File file) {
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    load_Directory_files(value);
                } else {
                    status_images.add(new ImagesModelClass(value.getAbsolutePath(), value.getName()));
                }
            }
        }
    }
    public class Load_All_data_progressbar extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar_status.setVisibility(View.VISIBLE);
            image_text.setVisibility(View.GONE);
            recyclerView_images.setVisibility(View.GONE);
            status_images.clear();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            get_Status_Data();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            image_text.setVisibility(View.VISIBLE);
            progressBar_status.setVisibility(View.GONE);
            recyclerView_images.setVisibility(View.VISIBLE);
            statusMoreImageFullAdapterRecyclerView.notifyDataSetChanged();
            statusMoreImageFullAdapterRecyclerView.notifyDataSetChanged();
        }
    }
}