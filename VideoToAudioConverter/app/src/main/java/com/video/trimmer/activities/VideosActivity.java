package com.video.trimmer.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;


import static com.video.trimmer.utils.Constants.FOLDER_NAME_INTENT_KEY;
import static com.video.trimmer.utils.Constants.LAUNCH_OR_NOT_KEY;
import static com.video.trimmer.utils.Constants.MAIN_TYPE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.NAME_INTENT_KEY;
import static com.video.trimmer.utils.Constants.TYPE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.URI_INTENT_KEY;
import static com.video.trimmer.utils.Constants.VIDEO_TOTAL_DURATION;


import com.video.trimmer.R;
import com.video.trimmer.adapters.FolderAdpater;
import com.video.trimmer.adapters.SubVideoAdapter;
import com.video.trimmer.adapters.VideosAdapter;
import com.video.trimmer.databinding.ActivityVideosBinding;
import com.video.trimmer.interfaces.OnFolderClicked;
import com.video.trimmer.interfaces.OnVideoItemClicked;
import com.video.trimmer.modelclasses.VideoModal;
import com.video.trimmer.modelclasses.FolderModel;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.FolderFetcherClass;
import com.video.trimmer.utils.SharedPrefClass;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class VideosActivity extends AppCompatActivity {
    ArrayList<VideoModal> videoList = new ArrayList<>();
    ArrayList<FolderModel> folders=new ArrayList<>();
    int type;
    Uri contentUri;
    ActivityVideosBinding binding;
    VideosAdapter videosAdapter;
    FolderAdpater folderAdpater;
    String path1="all";
    String queryText=null;
    SubVideoAdapter subVideoAdapter;
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==1) {
                if (TrimmerActivity.type == 1) {
                    Intent intent = new Intent(VideosActivity.this, OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY, 2);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.trimmedVideo));
                    intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                    startActivity(intent);
                    finish();
                } else if(TrimmerActivity.type==2) {
                    Intent intent = new Intent(VideosActivity.this, OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY, 1);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.covertedAudio));
                    intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                    startActivity(intent);
                    finish();
                }else if(TrimmerActivity.type==3){
                    Intent intent = new Intent(VideosActivity.this, OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY, 4);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.audioRemoved));
                    intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                    startActivity(intent);
                    finish();
                }else if(TrimmerActivity.type==4){
                    Intent intent = new Intent(VideosActivity.this, OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY, 5);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.audioaddedFolder));
                    intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                    startActivity(intent);
                    finish();
                }
            }
        }
    });

    Constants constants;
    SharedPrefClass sharedPrefClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVideosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        sharedPrefClass=new SharedPrefClass(VideosActivity.this);
        if(sharedPrefClass.getJugaadStatefromShared()){
            sharedPrefClass.saveJugaadStateinShared(false);
            Intent intent=new Intent(VideosActivity.this,OutputsActivity.class);
            intent.putExtra("jugaadkey",true);
            startActivity(intent);
        }
        File f2=new File(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH);
        if(!f2.exists()){
            f2.mkdir();
        }
        binding.toolbar.setTitle("");
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_icon));
        type=getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY,1);
        if(type==1){
            binding.toolbarTitle.setText(getResources().getString(R.string.videocutter));
        }else if(type==2){
            binding.toolbarTitle.setText(getResources().getString(R.string.videotoaudio));
        }else if(type==3){
            binding.toolbarTitle.setText(getResources().getString(R.string.removeaudio));
        }else if(type==4){
            binding.toolbarTitle.setText(getResources().getString(R.string.addaudiotovideo));
        }

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setData();


        binding.btnbyvideos.setBackgroundColor(ContextCompat.getColor(VideosActivity.this,R.color.btnselectedcolor));
        binding.btnbyvideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setData();
                path1="all";
                binding.btnbyvideos.setBackgroundColor(ContextCompat.getColor(VideosActivity.this,R.color.btnselectedcolor));
                binding.btnbyfolders.setBackgroundColor(ContextCompat.getColor(VideosActivity.this,R.color.white));
            }
        });
        binding.btnbyfolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAndSetFolders();
                path1="folders";
                binding.btnbyvideos.setBackgroundColor(ContextCompat.getColor(VideosActivity.this,R.color.white));
                binding.btnbyfolders.setBackgroundColor(ContextCompat.getColor(VideosActivity.this,R.color.btnselectedcolor));
            }
        });

                binding.imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.toolbarTitle.setVisibility(View.GONE);
                binding.searchView1.setVisibility(View.VISIBLE);
                binding.imgBtnSearch.setVisibility(View.GONE);
                //binding.searchView1.setIconifiedByDefault(false);
                binding.searchView1.setIconified(false);
                binding.searchView1.setFocusable(true);
                binding.searchView1.requestFocus();
            }
        });
        int id = binding.searchView1.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        int id2 = binding.searchView1.getContext()
                .getResources()
                .getIdentifier("android:id/search_button", null, null);
        int id3 = binding.searchView1.getContext()
                .getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        TextView textView = (TextView) binding.searchView1.findViewById(id);
        textView.setTextColor(Color.WHITE);
        ImageView searchClose = binding.searchView1.findViewById(id3);
        searchClose.setColorFilter(Color.WHITE);
        ImageView searchIcon = binding.searchView1.findViewById(id2);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_search_icon));
        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchView1.clearFocus();
                if(queryText==null){
                    binding.searchView1.setVisibility(View.GONE);
                    binding.toolbarTitle.setVisibility(View.VISIBLE);
                    binding.imgBtnSearch.setVisibility(View.VISIBLE);
                }else{
                    queryText=null;
                    binding.searchView1.setVisibility(View.GONE);
                    binding.toolbarTitle.setVisibility(View.VISIBLE);
                    binding.imgBtnSearch.setVisibility(View.VISIBLE);
                    if(path1.equals("all")){
                        videosAdapter.getFilter().filter(queryText);
                    }else if(path1.equals("folders")){
                        folderAdpater.getFilter().filter(queryText);
                    }else{
                        subVideoAdapter.getFilter().filter(queryText);
                    }

                }
            }
        });
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(textView, 0); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {}
        binding.searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryText=newText;
                if(path1.equals("all")){
                    videosAdapter.getFilter().filter(queryText);
                }else if(path1.equals("folders")){
                    folderAdpater.getFilter().filter(queryText);
                }else{
                    subVideoAdapter.getFilter().filter(queryText);
                }
                return true;
            }
        });
    }
    private int  getMediaDuration(Uri uriOfFile)  {
        int duration1 = 0;
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uriOfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
            duration1=mediaPlayer.getDuration();
            mediaPlayer.release();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return duration1;
    }

    private void loadAndSetFolders() {
        folders=new FolderFetcherClass(VideosActivity.this,1).get_Recovery_Audios();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        folderAdpater=new FolderAdpater(VideosActivity.this,folders);
        binding.recyclerVideo.setLayoutManager(layoutManager);
        binding.recyclerVideo.setAdapter(folderAdpater);

        folderAdpater.onFolderItemClicked(new OnFolderClicked() {
            @Override
            public void onFolderClicked(String path, int pos) {
                path1="subaudios";
                setSubVideos(path);
            }
        });

    }
    public String generatePath(String folderPath, String name) {

        if(!name.equals("")) {
            if(name.length()>=32){
                name=name.substring(0,20);
            }
            name = name.substring(0, name.length() - 4) + System.currentTimeMillis() + "" + ".mp3";

        }
        final File dir = new File(folderPath);
        if (!dir.exists())
            dir.mkdirs();
        folderPath= dir.getAbsolutePath()+"/"+name;

        return folderPath;
    }
    public void setSubVideos(String path){
        ArrayList<VideoModal> folderVideos=QueryInFolder(path);
        subVideoAdapter=new SubVideoAdapter(VideosActivity.this,folderVideos);
        LinearLayoutManager layoutManager=new LinearLayoutManager(VideosActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerVideo.setLayoutManager(layoutManager);
        binding.recyclerVideo.setAdapter(subVideoAdapter);

        subVideoAdapter.onVideoItemClicked(new OnVideoItemClicked() {
            @Override
            public void onItemClicked(String uri, String Name) {
                openTrimmerActivity(uri,Name);
            }
        });
    }
    public Uri getFileUri(String fileName) {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
        };
        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";
        Cursor cursor = getContentResolver().query(
                collection,
                projection,
                null,
                null,
                sortOrder);
        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        int path=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(idColumn);
            String originalpath=cursor.getString(path);
            if(originalpath.equals(fileName)) {
                contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            }
        }
        return contentUri;
    }


    public void openTrimmerActivity(String uri,String name){


        if(getduration(Uri.parse(uri))>0) {
            if (getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 1) {
                Intent intent = new Intent(VideosActivity.this, VideoTrimmerActivity.class);
                intent.putExtra(URI_INTENT_KEY,uri);
                intent.putExtra(NAME_INTENT_KEY,name);
                intent.putExtra(MAIN_TYPE_INTENT_KEY,1);
                TrimmerActivity.type = 1;
                mGetContent.launch(intent);
            } else if(getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 2) {
                Intent intent = new Intent(VideosActivity.this, VideoTrimmerActivity.class);
                intent.putExtra(URI_INTENT_KEY,uri);
                intent.putExtra(NAME_INTENT_KEY,name);
                intent.putExtra(MAIN_TYPE_INTENT_KEY,2);
                TrimmerActivity.type = 2;
                mGetContent.launch(intent);
            }else if(getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 3){
                Intent intent = new Intent(VideosActivity.this, VideoTrimmerActivity.class);
                intent.putExtra(URI_INTENT_KEY,uri);
                intent.putExtra(NAME_INTENT_KEY,name);
                intent.putExtra(MAIN_TYPE_INTENT_KEY,3);
                TrimmerActivity.type = 3;
                mGetContent.launch(intent);
            }else if(getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 4){
                Intent intent = new Intent(VideosActivity.this, AddAudioActivity.class);
                TrimmerActivity.VIDEOPATH = uri;
                intent.putExtra(VIDEO_TOTAL_DURATION, getMediaDuration(Uri.parse(uri)));
                TrimmerActivity.TRIMMED_VIDEO_FOLDER_PATH = generatePath(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH, name);
                TrimmerActivity.type = 4;
                mGetContent.launch(intent);
            }
        }else{
            if (getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 1) {
                Intent intent=new Intent(VideosActivity.this,VideoPlayerActivity.class);
                intent.putExtra(URI_INTENT_KEY,uri);
                intent.putExtra(NAME_INTENT_KEY,name);
                intent.putExtra(MAIN_TYPE_INTENT_KEY,1);
                TrimmerActivity.type = 1;
                mGetContent.launch(intent);
            } else if(getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 2) {
                Intent intent=new Intent(VideosActivity.this,VideoPlayerActivity.class);
                intent.putExtra(URI_INTENT_KEY,uri);
                intent.putExtra(NAME_INTENT_KEY,name);
                intent.putExtra(MAIN_TYPE_INTENT_KEY,2);
                TrimmerActivity.type = 2;
                mGetContent.launch(intent);
            }else if(getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 3){
                Intent intent=new Intent(VideosActivity.this,VideoPlayerActivity.class);
                intent.putExtra(URI_INTENT_KEY,uri);
                intent.putExtra(NAME_INTENT_KEY,name);
                intent.putExtra(MAIN_TYPE_INTENT_KEY,3);
                TrimmerActivity.type = 3;
                mGetContent.launch(intent);
            }else if(getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 4){

                Intent intent = new Intent(VideosActivity.this, AddAudioExo.class);
                TrimmerActivity.VIDEOPATH = uri;
                intent.putExtra(VIDEO_TOTAL_DURATION, getMediaDuration(Uri.parse(uri)));
                TrimmerActivity.TRIMMED_VIDEO_FOLDER_PATH = generatePath(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH, name);
                TrimmerActivity.type = 4;
                mGetContent.launch(intent);
            }
       }
    }
    private ArrayList<VideoModal> QueryInFolder(String folderPath) {
        ArrayList<VideoModal> subFolderPaths = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Thumbnails.DATA
        };

        try  {
            Cursor cursor =getContentResolver().query(
                    uri
                    , projection
                    , MediaStore.Video.Media.DATA + " like ? "
                    , new String[]{"%" + folderPath + "%"}
                    , null);
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int path=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                long duration = cursor.getInt(durationColumn);
                long size = cursor.getInt(sizeColumn);
                String originalpath=cursor.getString(path);
                String thumbnail=cursor.getString(thum);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                subFolderPaths.add(new VideoModal(contentUri, name, duration, size,thumbnail,originalpath));
            }
        }catch (Exception e){

        }
        return subFolderPaths;
    }
    private void setData() {
        new LoadData().execute();
    }
    public void openLoadedData(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        videosAdapter=new VideosAdapter(VideosActivity.this,videoList);
        binding.recyclerVideo.setLayoutManager(layoutManager);
        binding.recyclerVideo.setAdapter(videosAdapter);
        videosAdapter.onVideoItemClicked(new OnVideoItemClicked() {
            @Override
            public void onItemClicked(String uri, String Name) {

                openTrimmerActivity(uri,Name);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(binding.searchView1.getVisibility()==View.GONE){
            if(path1.equals("subaudios")){
                loadAndSetFolders();
                path1="folders";
            }else{
                super.onBackPressed();
            }
        }else{
            if(queryText==null){
                binding.searchView1.setVisibility(View.GONE);
                binding.toolbarTitle.setVisibility(View.VISIBLE);
                binding.imgBtnSearch.setVisibility(View.VISIBLE);
            }else{
                queryText=null;
                binding.searchView1.setVisibility(View.GONE);
                binding.toolbarTitle.setVisibility(View.VISIBLE);
                binding.imgBtnSearch.setVisibility(View.VISIBLE);
                if(path1.equals("all")){
                    videosAdapter.getFilter().filter(queryText);
                }else if(path1.equals("folders")){
                    folderAdpater.getFilter().filter(queryText);
                }else{
                    subVideoAdapter.getFilter().filter(queryText);
                }

            }
        }
    }

    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            videoList.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingProgress(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {

            loadData();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    openLoadedData();
                }
            });

            return "Task Completed";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            videosAdapter.notifyDataSetChanged();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingProgress(false);
                }
            });

        }
    }
long duration;
    long id;
    String name;
    String originalpath;
    String thumbnail;
    long size;
    Uri contentUri1;
    public void loadData(){
        videoList.clear();
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            projection = new String[] {
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.VideoColumns.DURATION,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Thumbnails.DATA
            };
        }else {
            projection = new String[]{
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Thumbnails.DATA
            };
        }

        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

        try  {
            Cursor cursor = getApplicationContext().getContentResolver().query(
                    collection,
                    projection,
                    null,
                    null,
                    sortOrder);
            // Cache column indices.


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                int durationColumn =
                        0;
                durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                int path=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                int thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
                while (cursor.moveToNext()) {
                    // Get values of columns for a given video.
                    id = cursor.getLong(idColumn);
                    name = cursor.getString(nameColumn);
                    duration = cursor.getInt(durationColumn);
                    originalpath=cursor.getString(path);
                    thumbnail=cursor.getString(thum);
                    size = cursor.getInt(sizeColumn);
                    contentUri1 = ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                    videoList.add(new VideoModal(contentUri1, name, duration, size, thumbnail, originalpath));
                }
            }else{
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                int path=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                int thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
                while (cursor.moveToNext()) {
                    // Get values of columns for a given video.
                    id = cursor.getLong(idColumn);
                    name = cursor.getString(nameColumn);
                    originalpath=cursor.getString(path);
                    contentUri1 = ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                    duration = getduration(contentUri1);
                    thumbnail=cursor.getString(thum);
                    size = cursor.getInt(sizeColumn);

                    videoList.add(new VideoModal(contentUri1, name, duration, size, thumbnail, originalpath));
                }
            }


        }catch (Exception e){

        }
    }

    public long getduration(Uri pathStr){
        long millSecond=0;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            Uri uri=Uri.parse("file://"+pathStr);
           retriever.setDataSource(VideosActivity.this, pathStr);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            millSecond = Long.parseLong(time);
            retriever.release();
        }catch (Exception e) {
            MediaPlayer mp = new MediaPlayer();
            try {
                mp.setDataSource(VideosActivity.this, pathStr);
                mp.prepare();
                millSecond=  mp.getDuration();
            } catch (IOException f) {
                Log.d("-MS-","Cannot parse url");

                f.printStackTrace();
            }


        }
        return millSecond;
    }

    public Uri getFileUri(Context context, String fileName) {
        return FileProvider.getUriForFile(context,  "com.example.videotoaudioconverter.fileprovider", new File(fileName));
    }

    private void showLoadingProgress(boolean state) {
        if(state) {
            binding.constraintProgress.setVisibility(View.VISIBLE);
        }else{
            binding.constraintProgress.setVisibility(View.GONE);
        }
    }
}