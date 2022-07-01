package com.example.whatsappdatarecovery.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.adapter.StatusImageFullAdapter;
import com.example.whatsappdatarecovery.adapter.StatusVideoFullAdapter;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StatusFragment extends Fragment {
    private static final String TAG = "malik";

    private boolean more_videos,more_images;
    private RecyclerView recyclerView_videos, recyclerView_images;
    private StatusImageFullAdapter imageadapter;
    private StatusVideoFullAdapter videoadpater;
    private GridLayoutManager layoutManager;
    private TextView btn_more_images, btn_more_videos;
    private ProgressBar progressBar_status;
    private TextView image_text, video_text;
    private TextView textView_status;
    private FragmentManager fragmentManager;
    private TextView textView_video, textView_image;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<ImagesModelClass> AllStatus_images_videos = new ArrayList<>();
    private ArrayList<ImagesModelClass> status_images = new ArrayList<>();
    private ArrayList<ImagesModelClass> status_videos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_fragment_recyclerview, container, false);
        recyclerView_images = view.findViewById(R.id.image_status_fragment_recyclerview);
        recyclerView_videos = view.findViewById(R.id.video_status_fragment_recyclerview);
        progressBar_status = view.findViewById(R.id.status_progressbar);
        btn_more_images = view.findViewById(R.id.more_images);
        btn_more_videos = view.findViewById(R.id.more_videos);
        textView_status=view.findViewById(R.id.text_status);
        textView_image = view.findViewById(R.id.text_image_recyclerview);
        textView_image.setText(R.string.nodata_found_inthis_category);
        textView_video = view.findViewById(R.id.text_video_recyclerview);
        textView_video.setText(R.string.nodata_found_inthis_category);
        image_text = view.findViewById(R.id.text_status_image);
        video_text = view.findViewById(R.id.text_status_video);
        new Load_data_progressbar().execute();
        btn_more_videos.setOnClickListener(v -> {
            StatusMoreVideosFragment statusMoreVideosFragment = new StatusMoreVideosFragment();
            fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, statusMoreVideosFragment, null);
            fragmentTransaction.commit();
        });
        btn_more_images.setOnClickListener(v -> {
            StatusMoreImagesFragment statusMoreImagesFragment = new StatusMoreImagesFragment();
            fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, statusMoreImagesFragment, null);
            fragmentTransaction.commit();
        });
        setImageRecyclerviewAdapter();
        setVideoRecyclerviewAdapter();
        return view;
    }

    private void setImageRecyclerviewAdapter() {
        imageadapter = new StatusImageFullAdapter(getContext(), status_images);
        layoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView_images.setLayoutManager(layoutManager);
        recyclerView_images.setAdapter(imageadapter);
    }

    private void setVideoRecyclerviewAdapter() {
        videoadpater = new StatusVideoFullAdapter(getContext(), status_videos);
        layoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView_videos.setLayoutManager(layoutManager);
        recyclerView_videos.setAdapter(videoadpater);
    }
    private void get_8_Status_Data() {
        String image_path = getContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Status/Images";
        String video_path = getContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Status/Videos";
        AllStatus_images_videos.clear();
        status_images.clear();
        status_videos.clear();
        load_8_image_Directory_files(new File(image_path), 0);
        load_8_video_Directory_files(new File(video_path), 0);
        status_extension();
    }
        public void status_extension() {
        for (ImagesModelClass file : AllStatus_images_videos) {
            String ext = file.getName().substring(file.getName().length() - 4);
            if (ext.equals(".png") || ext.equals(".jpg") || ext.equals("jpeg")) {
                status_images.add(file);
            } else {
                status_videos.add(file);
            }
        }
    }
    public void load_Directory_files(File file) {
        Log.d("990", "dir name_status: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    load_Directory_files(value);
                } else {
                    Log.d("990", "file name: " + value.getName());
                    AllStatus_images_videos.add(new ImagesModelClass(value.getAbsolutePath(), value.getName()));
                }
            }
        }
    }
    public void load_8_image_Directory_files(File file, int count) {
        Log.d("990", "dir name_status: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    Log.d("991", "found directory: ");
                    load_8_image_Directory_files(value, count);
                } else {
                    count++;
                    Log.d("990", "file name: " + value.getName());
                    AllStatus_images_videos.add(new ImagesModelClass(value.getAbsolutePath(), value.getName()));
                    if (count == 8) {
                        more_images = filelist[9] != null && filelist[9].exists();
                        return;
                    }
                }
            }
            if (count<8)
            {
                more_images=false;
            }
        }
    }
    public void load_8_video_Directory_files(File file, int count) {
        Log.d("990", "dir name_status: " + file.getName());
        Log.d(TAG, "load_8_vide0_Directory_files: " + count);
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    Log.d("991", "found directory: ");
                    load_8_video_Directory_files(value, count);
                } else {
                    count++;
                    Log.d("990", "file name1111: " + value.getName());
                    AllStatus_images_videos.add(new ImagesModelClass(value.getAbsolutePath(), value.getName()));
                    if (count == 8) {
                        more_videos = filelist[9] != null && filelist[9].exists();
                        return;
                    }

                }
            }
            if (count<8)
            {
                more_videos=false;
            }

        }
    }

    public class Load_data_progressbar extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar_status.setVisibility(View.VISIBLE);
            AllStatus_images_videos.clear();
            status_images.clear();
            status_videos.clear();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            get_8_Status_Data();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar_status.setVisibility(View.GONE);
            recyclerView_images.setVisibility(View.VISIBLE);
            recyclerView_videos.setVisibility(View.VISIBLE);
            image_text.setVisibility(View.VISIBLE);
            video_text.setVisibility(View.VISIBLE);
            if(more_videos){
                btn_more_videos.setVisibility(View.VISIBLE);
            }else{
                btn_more_videos.setVisibility(View.GONE);
            }
            if(more_images){
                btn_more_images.setVisibility(View.VISIBLE);
            }else{
                btn_more_videos.setVisibility(View.GONE);
            }
            if (status_images.size() == 0) {
                textView_image.setVisibility(View.GONE);
                recyclerView_videos.setVisibility(View.GONE);
                btn_more_images.setVisibility(View.GONE);
            }
            if (status_videos.size() == 0) {
                textView_video.setVisibility(View.GONE);
                recyclerView_videos.setVisibility(View.GONE);
                btn_more_videos.setVisibility(View.GONE);
            }
            if (status_images.size()==0 && status_videos.size()==0) {
                textView_status.setVisibility(View.VISIBLE);
                textView_image.setVisibility(View.GONE);
                image_text.setVisibility(View.GONE);
                video_text.setVisibility(View.GONE);
                textView_video.setVisibility(View.VISIBLE);
                recyclerView_videos.setVisibility(View.GONE);
                btn_more_images.setVisibility(View.GONE);
                textView_video.setVisibility(View.GONE);
                recyclerView_videos.setVisibility(View.GONE);
                btn_more_videos.setVisibility(View.GONE);
            }
            imageadapter.notifyDataSetChanged();
            videoadpater.notifyDataSetChanged();
        }
    }
}