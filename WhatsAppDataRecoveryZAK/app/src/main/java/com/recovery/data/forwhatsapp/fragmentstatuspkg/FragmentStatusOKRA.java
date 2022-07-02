package com.recovery.data.forwhatsapp.fragmentstatuspkg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.recovery.data.forwhatsapp.MyFilesComparatorOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.StatusDataSearchInterfaceOKRA;
import com.recovery.data.forwhatsapp.activities.ActivityStatusMainOKRA;
import com.recovery.data.forwhatsapp.activities.ActivityViewImageOKRA;
import com.recovery.data.forwhatsapp.activities.ActivityViewVideoOKRA;
import com.recovery.data.forwhatsapp.activities.ActivitySeeMoreImagesOKRA;
import com.recovery.data.forwhatsapp.activities.ActivitySeeMoreVideosOKRA;
import com.recovery.data.forwhatsapp.imagespkg.ImagesUrlInterfaceOKRA;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentStatusOKRA extends Fragment  {

    String status_root = "";
    public  static ArrayList<File> images_files = new ArrayList<>();
    public static ArrayList<File> videos_files = new ArrayList<>();
    Context mycontext;
    RecyclerView rc_images;
    RecyclerView rc_videos;
    AdapterStatusOKRA images_Adapter;
    AdapterStatusOKRA videos_Adapter;
    View vi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status_layout_okra, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> rootPathToWatch = new ArrayList<>();
        rootPathToWatch.add(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses");
        rootPathToWatch.add(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses");
        for(String path:rootPathToWatch){
            try{
                if(new File(path).exists()){
                    status_root = path;
                    break;
                }
            }catch (Exception e){

            }

        }
        vi=view;

        view.findViewById(R.id.tv_seemore_images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFilesListStatusOKRA.files =getimagesList(true);
                Log.d("TAG", "onClick: " + MyFilesListStatusOKRA.files.size());
                Intent intent = new Intent(getContext(), ActivitySeeMoreImagesOKRA.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.tv_seemore_videos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFilesListStatusOKRA.files = getimagesList(false);
                Log.d("TAG", "onClick: " + MyFilesListStatusOKRA.files.size());
                Intent intent = new Intent(getContext(), ActivitySeeMoreVideosOKRA.class);
                startActivity(intent);
            }
        });

    }

    private ArrayList<File> getimagesList(boolean images) {
        ArrayList<File> files=new ArrayList<>();
        File f = new File(status_root);

        if (f.exists()) {
            File[] filesLIst = f.listFiles();
            Arrays.sort(filesLIst,new MyFilesComparatorOKRA());
            for (File i : filesLIst) {
                Log.d("TAG", "loadStatusData: " + i.getName());
                if (i.getName().endsWith(".jpg")&&images) {
                   files.add(i);
                }else if (i.getName().endsWith(".mp4")&&!images) {
                    files.add(i);
                }
            }
        }
        return files;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.example_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                _images_adapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//        return true;
//    }

    public class ClickHandler implements ImagesUrlInterfaceOKRA {

        @Override
        public void setUrlandLaunchInterface(String url,String name) {
            if (url.endsWith(".jpg")) {
                //launch image
                Intent intent = new Intent(getContext(), ActivityViewImageOKRA.class);
                intent.putExtra("url", url);
                intent.putExtra("imgname",name);
                getContext().startActivity(intent);

            } else {
                //launch video
                Intent intent = new Intent(getContext(), ActivityViewVideoOKRA.class);
                intent.putExtra("url", url);
                intent.putExtra("videoname",name);
                getContext().startActivity(intent);
            }
        }

        @Override
        public void onLongClick(String url, int position) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        new LoadData(vi).execute();
        ((ActivityStatusMainOKRA)getActivity()).setIntrface(new StatusDataSearchInterfaceOKRA() {
            @Override
            public void sendTextToFragment(String newText) {
                if(images_Adapter!=null) {
                    images_Adapter.getFilter().filter(newText);
                }
                if(videos_Adapter!=null) {
                    videos_Adapter.getFilter().filter(newText);
                }
            }
        });
    }

    public class LoadData extends AsyncTask<String, String, String> {
        View view;

        LoadData(View view) {
            this.view = view;

        }

        @Override
        protected String doInBackground(String... strings) {
            loadStatusData();
            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            images_files.clear();
            videos_files.clear();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressBar pbar = view.findViewById(R.id.status_pbar);
            pbar.setVisibility(View.GONE);
            if (images_files.size() == 0 && videos_files.size() == 0) {
                view.findViewById(R.id.noimages).setVisibility(View.VISIBLE);
                view.findViewById(R.id.cv_images).setVisibility(View.GONE);
                view.findViewById(R.id.cv_videos).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.datafound).setVisibility(View.VISIBLE);
                GridLayoutManager images = new GridLayoutManager(getContext(),3);
                GridLayoutManager videos =  new GridLayoutManager(getContext(),3);

                if (images_files.size() > 0) {
                    view.findViewById(R.id.tv_images).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.rc_images).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.tv_seemore_images).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.cv_images).setVisibility(View.VISIBLE);
                    //do the recycleview
                    rc_images = view.findViewById(R.id.rc_images);
                    rc_images.setLayoutManager(images);

                    images_Adapter = new AdapterStatusOKRA(getContext(), new ClickHandler(), images_files);
                    rc_images.setAdapter(images_Adapter);
                    if (videos_files.size() > 0) {
                        view.findViewById(R.id.tv_videos).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.rc_videos).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.tv_seemore_videos).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.cv_videos).setVisibility(View.VISIBLE);

                        //do the recycleview
                        rc_videos = view.findViewById(R.id.rc_videos);
                        rc_videos.setLayoutManager(videos);

                        videos_Adapter = new AdapterStatusOKRA(getContext(), new ClickHandler(), videos_files);
                        rc_videos.setAdapter(videos_Adapter);
                    } else {
                        view.findViewById(R.id.tv_videos).setVisibility(View.GONE);
                        view.findViewById(R.id.rc_videos).setVisibility(View.GONE);
                        view.findViewById(R.id.tv_seemore_videos).setVisibility(View.GONE);
                        view.findViewById(R.id.cv_videos).setVisibility(View.GONE);

                    }

                } else {
                    view.findViewById(R.id.tv_images).setVisibility(View.GONE);
                    view.findViewById(R.id.rc_images).setVisibility(View.GONE);
                    view.findViewById(R.id.tv_seemore_images).setVisibility(View.GONE);
                    view.findViewById(R.id.cv_images).setVisibility(View.GONE);
                    if (videos_files.size() > 0) {
                        view.findViewById(R.id.tv_videos).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.rc_videos).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.tv_seemore_videos).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.cv_videos).setVisibility(View.VISIBLE);
                        //do the recycleview
                        rc_videos = view.findViewById(R.id.rc_videos);
                        rc_videos.setVisibility(View.VISIBLE);
                        rc_videos.setLayoutManager(videos);

                        videos_Adapter = new AdapterStatusOKRA(getContext(), new ClickHandler(), videos_files);
                        rc_videos.setAdapter(videos_Adapter);
                    } else {
                        view.findViewById(R.id.tv_videos).setVisibility(View.GONE);
                        view.findViewById(R.id.rc_videos).setVisibility(View.GONE);
                        view.findViewById(R.id.tv_seemore_videos).setVisibility(View.GONE);
                        view.findViewById(R.id.cv_videos).setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void loadStatusData() {
        File f = new File(status_root);
        int count_videos=0;
        int count_images=0;

        if (f.exists()) {
            File[] filesLIst = f.listFiles();
            Arrays.sort(filesLIst,new MyFilesComparatorOKRA());
            for (File i : filesLIst) {
                Log.d("TAG", "loadStatusData: " + i.getName());
                if(count_images==4&&count_videos==4){
                    break;
                }
                if (i.getName().endsWith(".jpg")) {
                    if(count_images<4){
                        images_files.add(i);
                        count_images++;
                    }
                } else if (i.getName().endsWith(".mp4")) {
                    if(count_videos<4){
                        videos_files.add(i);
                        count_videos++;
                    }
                }
            }
        }
    }


    public class StoreFilesToPublicDir extends AsyncTask<Void, Void, Void> {
        ArrayList<File> files;

        public StoreFilesToPublicDir(ArrayList<File> files) {
            this.files = files;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String recoveryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Data Recovery/WhatsApp Status/";
            try {
                for (File srcFile : files) {
                    File targetFile = new File(recoveryPath + srcFile.getName());
                    OutputStream out = new FileOutputStream(targetFile);
                    InputStream in = new FileInputStream(srcFile);
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(targetFile);
                        scanIntent.setData(contentUri);
                        getContext().sendBroadcast(scanIntent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                        getContext().sendBroadcast(intent);
                    }
                }
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (images_Adapter != null) {
                images_Adapter.setSelectedFiles();
            }
            if (videos_Adapter != null) {
                videos_Adapter.setSelectedFiles();
            }
        }
    }
}
