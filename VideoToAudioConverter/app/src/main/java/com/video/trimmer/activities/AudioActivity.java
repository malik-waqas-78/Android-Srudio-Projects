package com.video.trimmer.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.video.trimmer.R;
import com.video.trimmer.adapters.AudioAdapter;
import com.video.trimmer.adapters.FolderAdpater;
import com.video.trimmer.adapters.SubAudioAdapter;
import com.video.trimmer.databinding.ActivityAudioBinding;
import com.video.trimmer.interfaces.OnAudioClickInterface;
import com.video.trimmer.interfaces.OnFolderClicked;
import com.video.trimmer.modelclasses.AudioModal;
import com.video.trimmer.modelclasses.FolderModel;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.DataFetcherClass;
import com.video.trimmer.utils.FolderFetcherClass;
import com.video.trimmer.utils.SharedPrefClass;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.video.trimmer.utils.Constants.AUDIOSIZE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.DURATION_INTENT_KEY;
import static com.video.trimmer.utils.Constants.FOLDER_NAME_INTENT_KEY;
import static com.video.trimmer.utils.Constants.FROM_ADD_AUDIO_KEY;
import static com.video.trimmer.utils.Constants.LAUNCH_OR_NOT_KEY;
import static com.video.trimmer.utils.Constants.NAME_INTENT_KEY;
import static com.video.trimmer.utils.Constants.TYPE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.URI_INTENT_KEY;

public class AudioActivity extends AppCompatActivity {
    ArrayList<AudioModal> audioModals=new ArrayList<>();
    ActivityAudioBinding binding;
    FolderAdpater folderAdpater;
    AudioAdapter audioAdapter;
    SubAudioAdapter subVideoAdapter;
    String path1="all";
    boolean fromAddAudio=false;
    SharedPrefClass sharedPrefClass;
    String queryText=null;
    ArrayList<FolderModel> folders=new ArrayList<>();

    Uri contentUri;

    ActivityResultLauncher<Intent> mGetContent3 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==1) {
                Intent intent = new Intent(AudioActivity.this, OutputsActivity.class);
                intent.putExtra(TYPE_INTENT_KEY, 3);
                intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.trimmedAudios));
                intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                startActivity(intent);
                finish();
            }
        }
    });
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAudioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        fromAddAudio=getIntent().getBooleanExtra(FROM_ADD_AUDIO_KEY,false);
        sharedPrefClass=new SharedPrefClass(AudioActivity.this);

        if(sharedPrefClass.getJugaadStatefromShared()){
            sharedPrefClass.saveJugaadStateinShared(false);
            Intent intent=new Intent(AudioActivity.this,OutputsActivity.class);
            intent.putExtra("jugaadkey",true);
            startActivity(intent);
        }

        binding.toolbar.setTitle("");
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_icon));


        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setData();
        binding.btnbyaudios.setBackgroundColor(ContextCompat.getColor(AudioActivity.this,R.color.btnselectedcolor));
        binding.btnbyaudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setData();
                path1="all";
                binding.btnbyaudios.setBackgroundColor(ContextCompat.getColor(AudioActivity.this,R.color.btnselectedcolor));
                binding.btnbyfolders.setBackgroundColor(ContextCompat.getColor(AudioActivity.this,R.color.white));
            }
        });

        binding.btnbyfolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadandSetFolder();
                path1="folders";
                binding.btnbyaudios.setBackgroundColor(ContextCompat.getColor(AudioActivity.this,R.color.white));
                binding.btnbyfolders.setBackgroundColor(ContextCompat.getColor(AudioActivity.this,R.color.btnselectedcolor));
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
                        audioAdapter.getFilter().filter(queryText);
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
                    audioAdapter.getFilter().filter(queryText);
                }else if(path1.equals("folders")){
                    folderAdpater.getFilter().filter(queryText);
                }else{
                    subVideoAdapter.getFilter().filter(queryText);
                }
                return true;
            }
        });
    }

    private void loadandSetFolder() {
        folders=new FolderFetcherClass(AudioActivity.this,2).get_Recovery_Audios();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        folderAdpater=new FolderAdpater(AudioActivity.this,folders);
        binding.recyclerAudio.setLayoutManager(layoutManager);
        binding.recyclerAudio.setAdapter(folderAdpater);

        folderAdpater.onFolderItemClicked(new OnFolderClicked() {
            @Override
            public void onFolderClicked(String path,int pos) {
                path1="subaudios";
                setSubAudios(path);
            }
        });
    }
    public void setSubAudios(String path){
        ArrayList<AudioModal> folderVideos=QueryInFolder(path);
        subVideoAdapter=new SubAudioAdapter(AudioActivity.this,folderVideos);
        LinearLayoutManager layoutManager=new LinearLayoutManager(AudioActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerAudio.setLayoutManager(layoutManager);
        binding.recyclerAudio.setAdapter(subVideoAdapter);

        subVideoAdapter.onAudioItemClicked(new OnAudioClickInterface() {
            @Override
            public void onAudioItemClick(String uri, String name, long duration,float size) {
                startPlayActivity(uri,name,duration,size);
            }
        });
    }

    private void startPlayActivity(String uri, String name, long duration, float size) {
        if(timeConversion(duration).equals("00:00")||size==0.0){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AudioActivity.this);
            ViewGroup viewGroup=findViewById(android.R.id.content);
            View dialogView= LayoutInflater.from(AudioActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
            alertDialog.setView(dialogView);
            AlertDialog dialog2=alertDialog.create();
            dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog2.show();

            ProgressBar pb=dialogView.findViewById(R.id.progbar);
            Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
            TextView title=dialogView.findViewById(R.id.dialogtitle);
            TextView msg=dialogView.findViewById(R.id.txtdeleteinstr);

            title.setText(getResources().getString(R.string.loadfailed));
            msg.setText(getResources().getString(R.string.loadfailedAudio));

            pb.setVisibility(View.INVISIBLE);
            btnok.setVisibility(View.VISIBLE);

            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog2.dismiss();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }else {
            if(fromAddAudio){
                sharedPrefClass.saveAudioUriinShared(uri);
                sharedPrefClass.saveAudioSizeinShared(size);
                setResult(1);
                finish();
            }else {
                Intent intent = new Intent(AudioActivity.this, AudioTrimmerActivity2.class);
                intent.putExtra(URI_INTENT_KEY, uri);
                intent.putExtra(NAME_INTENT_KEY, name);
                intent.putExtra(DURATION_INTENT_KEY, duration);
                intent.putExtra(AUDIOSIZE_INTENT_KEY, size);
                mGetContent3.launch(intent);
            }
        }
    }
    public String timeConversion(long value) {
        String videoTime = null;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else if(scs<0){
            videoTime=getResources().getString(R.string.lessthansec);

        }else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }
    private ArrayList<AudioModal> QueryInFolder(String folderPath) {
        ArrayList<AudioModal> subFolderPaths = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            projection = new String[] {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATA,

            };
        }else{
            projection = new String[] {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATA,

            };
        }

        try  {
            Cursor cursor =getContentResolver().query(
                    uri
                    , projection
                    , MediaStore.Audio.Media.DATA + " like ? "
                    , new String[]{"%" + folderPath + "%"}
                    , null);
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn =
                    0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            }
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int path=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    long id = cursor.getLong(idColumn);
                    String name = cursor.getString(nameColumn);
                    long duration = cursor.getInt(durationColumn);
                    long size = cursor.getInt(sizeColumn);
                    String originalpath = cursor.getString(path);
                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    subFolderPaths.add(new AudioModal(contentUri, name, duration, size, originalpath));
                }else{
                    long id = cursor.getLong(idColumn);
                    contentUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                    String name = cursor.getString(nameColumn);
                    long size = cursor.getInt(sizeColumn);
                    String originalpath = cursor.getString(path);
                    long duration = getduration(originalpath);


                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    subFolderPaths.add(new AudioModal(contentUri, name, duration, size, originalpath));
                }
            }
        }catch (Exception e){
            ArrayList<File> OutputFiles=new DataFetcherClass(folderPath).get_Recovery_Audios();
            if(OutputFiles.size()!=0) {
                for (File f : OutputFiles) {
                    subFolderPaths.add(new AudioModal(contentUri,f.getName(),getduration(f.getAbsolutePath()),f.length() / 1024,f.getAbsolutePath()));
                }
            }

        }
        return subFolderPaths;
    }
    public long getduration(String pathStr){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(AudioActivity.this, Uri.parse(pathStr));
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long millSecond = Integer.parseInt(duration);
        return millSecond;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setData() {

        new LoadData().execute();
    }
    public void setInAdapter(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        audioAdapter=new AudioAdapter(AudioActivity.this,audioModals);
        binding.recyclerAudio.setLayoutManager(layoutManager);
        binding.recyclerAudio.setAdapter(audioAdapter);

        audioAdapter.onAudioItemClick(new OnAudioClickInterface() {
            @Override
            public void onAudioItemClick(String uri,String name,long duration,float size) {
                startPlayActivity(uri,name,duration,size);

            }
        });
    }
    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            audioModals.clear();
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
                    setInAdapter();
                }
            });
            return "Task Completed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            audioAdapter.notifyDataSetChanged();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingProgress(false);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(binding.searchView1.getVisibility()==View.GONE){
        if(path1.equals("subaudios")){
            loadandSetFolder();
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
                    audioAdapter.getFilter().filter(queryText);
                }else if(path1.equals("folders")){
                    folderAdpater.getFilter().filter(queryText);
                }else{
                    subVideoAdapter.getFilter().filter(queryText);
                }

            }
        }
    }

    public void loadData(){
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            projection = new String[] {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.SIZE,

            };
        }else{
            projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.SIZE,
            };
        }

        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

        try  {
            Cursor cursor = getApplicationContext().getContentResolver().query(
                    collection,
                    projection,
                    null,
                    null,
                    sortOrder);
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn =
                    0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            }
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int path=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    long id = cursor.getLong(idColumn);
                    String name = cursor.getString(nameColumn);
                    long duration = cursor.getInt(durationColumn);
                    long size = cursor.getInt(sizeColumn);
                    String originalpath = cursor.getString(path);
                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    audioModals.add(new AudioModal(contentUri, name, duration, size, originalpath));
                }else{
                    long id = cursor.getLong(idColumn);
                    contentUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                    String name = cursor.getString(nameColumn);
                    long size = cursor.getInt(sizeColumn);
                    String originalpath = cursor.getString(path);
                    long duration = getduration(originalpath);

                    audioModals.add(new AudioModal(contentUri, name, duration, size, originalpath));
                }
            }
        }catch (Exception e){

        }
    }


    private void showLoadingProgress(boolean state) {

        if(state) {
            binding.constraintProgress.setVisibility(View.VISIBLE);

        }else{
            binding.constraintProgress.setVisibility(View.GONE);
        }
    }
}