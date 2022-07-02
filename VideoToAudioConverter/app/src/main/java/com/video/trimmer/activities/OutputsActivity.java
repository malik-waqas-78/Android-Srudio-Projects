package com.video.trimmer.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.video.trimmer.BuildConfig;
import com.video.trimmer.R;
import com.video.trimmer.adapters.ConvertedandTrimmedAudioAdapter;
import com.video.trimmer.adapters.TrimmedVideoAdapter;
import com.video.trimmer.databinding.ActivityOutputsBinding;
import com.video.trimmer.interfaces.Checkallselected;
import com.video.trimmer.interfaces.OnAudioClickInterface;
import com.video.trimmer.interfaces.OnOutputItemsLongClicked;
import com.video.trimmer.interfaces.OnVideoItemClicked;
import com.video.trimmer.interfaces.OutputItemShareDeleteClicked;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.SharedPrefClass;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;


import static com.video.trimmer.utils.Constants.AUDIOSIZE_INTENT_KEY;

import static com.video.trimmer.utils.Constants.DURATION_INTENT_KEY;
import static com.video.trimmer.utils.Constants.FOLDER_NAME_INTENT_KEY;
import static com.video.trimmer.utils.Constants.LAUNCH_OR_NOT_KEY;
import static com.video.trimmer.utils.Constants.MAIN_TYPE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.NAME_INTENT_KEY;

import static com.video.trimmer.utils.Constants.TYPE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.URI_INTENT_KEY;
import static com.video.trimmer.utils.Constants.VIDEO_TOTAL_DURATION;
import static com.video.trimmer.utils.RangeSeekBarView2.TAG;

public class OutputsActivity extends AppCompatActivity {
    ActivityOutputsBinding binding;
    ArrayList<File> OutputFiles=new ArrayList<>();
    ConvertedandTrimmedAudioAdapter convertedandTrimmedAudioAdapter;
    TrimmedVideoAdapter trimmedVideoAdapter;
    String queryText=null;
    boolean launchornot=false;
    SharedPrefClass sharedPrefClass;

    int type;
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==1) {
                if (TrimmerActivity.type == 1) {
                    Intent intent = new Intent(OutputsActivity.this, OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY, 2);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.trimmedVideo));
                    intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                    startActivity(intent);
                    finish();
                } else if (TrimmerActivity.type == 2) {
                    Intent intent = new Intent(OutputsActivity.this, OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY, 1);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.covertedAudio));
                    intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                    startActivity(intent);
                    finish();
                } else if (TrimmerActivity.type == 4) {
                    Intent intent = new Intent(OutputsActivity.this, OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY, 4);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.removeaudio));
                    intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                    startActivity(intent);
                    finish();
                } else if (TrimmerActivity.type == 5) {
                    Intent intent = new Intent(OutputsActivity.this, OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY, 5);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.addaudiotovideo));
                    intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                    startActivity(intent);
                    finish();
                } else if (type == 6) {
                    Intent intent = new Intent(OutputsActivity.this, OutputsActivity.class);
                    intent.putExtra(TYPE_INTENT_KEY, 6);
                    intent.putExtra(FOLDER_NAME_INTENT_KEY, getResources().getString(R.string.videoCreatedFromImages));
                    intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                    startActivity(intent);
                    finish();
                }
            }
        }
    });
    ActivityResultLauncher<Intent> mGetContent3 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==1) {
                Intent intent = new Intent(OutputsActivity.this, OutputsActivity.class);
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
        binding=ActivityOutputsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        sharedPrefClass=new SharedPrefClass(OutputsActivity.this);

        if(getIntent().getBooleanExtra("jugaadkey",false)){
            finish();
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



        type=getIntent().getIntExtra(TYPE_INTENT_KEY,1);
        launchornot=getIntent().getBooleanExtra(LAUNCH_OR_NOT_KEY,false);

        if(type==1){
            binding.toolbarTitle.setText(getIntent().getStringExtra(FOLDER_NAME_INTENT_KEY));
            getAndSetConvertedAudios();
            if(launchornot){
                File f=new File(sharedPrefClass.getOutputPatjfromShared());
                String name =f.getName();
                String uri=f.getPath();
                long duration=getduration(Uri.parse(sharedPrefClass.getOutputPatjfromShared()));
                float size=f.length();
                loadAudioTrimmer(uri,name,duration,size);
            }
        }else if(type==3){
            binding.toolbarTitle.setText(getIntent().getStringExtra(FOLDER_NAME_INTENT_KEY));
            getAndSetTrimmedAudios();
            if(launchornot){
                File f=new File(sharedPrefClass.getOutputPatjfromShared());
                String name =f.getName();
                String uri=f.getPath();
                long duration=getduration(Uri.parse(sharedPrefClass.getOutputPatjfromShared()));
                float size=f.length();
                loadAudioTrimmer(uri,name,duration,size);

            }
        }else if(type==2){
            binding.toolbarTitle.setText(getIntent().getStringExtra(FOLDER_NAME_INTENT_KEY));
            getAndSetTrimmedVideos();
            if(launchornot){
                File f=new File(sharedPrefClass.getOutputPatjfromShared());
                String name =f.getName();
                String uri=f.getPath();

                if(getMediaDuration(Uri.parse(uri))>0) {
                    if (getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 1) {
                        Intent intent = new Intent(OutputsActivity.this, VideoTrimmerActivity.class);
                        intent.putExtra(URI_INTENT_KEY,uri);
                        intent.putExtra(NAME_INTENT_KEY,name);
                        intent.putExtra(MAIN_TYPE_INTENT_KEY,1);
                        TrimmerActivity.type = 1;
                        mGetContent.launch(intent);
                    } else {
                        Intent intent = new Intent(OutputsActivity.this, VideoTrimmerActivity.class);
                        intent.putExtra(URI_INTENT_KEY,uri);
                        intent.putExtra(NAME_INTENT_KEY,name);
                        intent.putExtra(MAIN_TYPE_INTENT_KEY,2);
                        TrimmerActivity.type = 2;
                        mGetContent.launch(intent);
                    }
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2=alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb=dialogView.findViewById(R.id.progbar);
                    Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title=dialogView.findViewById(R.id.dialogtitle);
                    TextView msg=dialogView.findViewById(R.id.plzWit);

                    title.setText(getResources().getString(R.string.loadfailed));
                    msg.setText(getResources().getString(R.string.loadfailedVideo));

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
                }

            }
        }else if(type==4){
            binding.toolbarTitle.setText(getIntent().getStringExtra(FOLDER_NAME_INTENT_KEY));
            getAndSetAudioRemovedVideos();
            if(launchornot){
                File f=new File(sharedPrefClass.getOutputPatjfromShared());
                String name =f.getName();
                String uri=f.getPath();


                if(getMediaDuration(Uri.parse(uri))>0) {
                    Intent intent = new Intent(OutputsActivity.this, AddAudioActivity.class);
                    TrimmerActivity.VIDEOPATH = uri;
                    intent.putExtra(VIDEO_TOTAL_DURATION, getMediaDuration(Uri.parse(uri)));
                    TrimmerActivity.TRIMMED_VIDEO_FOLDER_PATH = generatePath(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH, "");
                    TrimmerActivity.type = 4;
                    mGetContent.launch(intent);
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2=alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb=dialogView.findViewById(R.id.progbar);
                    Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title=dialogView.findViewById(R.id.dialogtitle);
                    TextView msg=dialogView.findViewById(R.id.plzWit);

                    title.setText(getResources().getString(R.string.loadfailed));
                    msg.setText(getResources().getString(R.string.loadfailedVideo));

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
                }

            }
        }else if(type==5){
            binding.toolbarTitle.setText(getIntent().getStringExtra(FOLDER_NAME_INTENT_KEY));
            getAndSetAudioAddedVideos();
            if(launchornot){
                File f=new File(sharedPrefClass.getOutputPatjfromShared());
                String name =f.getName();
                String uri=f.getPath();

                if(getMediaDuration(Uri.parse(uri))>0) {
                    Intent intent = new Intent(OutputsActivity.this, VideoTrimmerActivity.class);
                    intent.putExtra(URI_INTENT_KEY,uri);
                    intent.putExtra(NAME_INTENT_KEY,name);
                    intent.putExtra(MAIN_TYPE_INTENT_KEY,1);
                    TrimmerActivity.type = 1;
                    mGetContent.launch(intent);
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2=alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb=dialogView.findViewById(R.id.progbar);
                    Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title=dialogView.findViewById(R.id.dialogtitle);
                    TextView msg=dialogView.findViewById(R.id.plzWit);

                    title.setText(getResources().getString(R.string.loadfailed));
                    msg.setText(getResources().getString(R.string.loadfailedVideo));

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
                }

            }
        }else if(type==6){
            binding.toolbarTitle.setText(getIntent().getStringExtra(FOLDER_NAME_INTENT_KEY));
            getAndSetCreatedVideos();
            if(launchornot){
                File f=new File(sharedPrefClass.getOutputPatjfromShared());
                String name =f.getName();
                String uri=f.getPath();

                if(getMediaDuration(Uri.parse(uri))>0) {
                    Intent intent = new Intent(OutputsActivity.this, VideoTrimmerActivity.class);
                    intent.putExtra(URI_INTENT_KEY,uri);
                    intent.putExtra(NAME_INTENT_KEY,name);
                    intent.putExtra(MAIN_TYPE_INTENT_KEY,1);
                    TrimmerActivity.type = 1;
                    mGetContent.launch(intent);
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2=alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb=dialogView.findViewById(R.id.progbar);
                    Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title=dialogView.findViewById(R.id.dialogtitle);
                    TextView msg=dialogView.findViewById(R.id.plzWit);

                    title.setText(getResources().getString(R.string.loadfailed));
                    msg.setText(getResources().getString(R.string.loadfailedVideo));

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
                }


            }
        }
        binding.imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchView1.setIconifiedByDefault(true);
                binding.searchView1.setFocusable(true);
                binding.searchView1.setIconified(false);
                binding.searchView1.requestFocusFromTouch();
                binding.toolbarTitle.setVisibility(View.GONE);
                binding.searchView1.setVisibility(View.VISIBLE);
                binding.imgBtnSearch.setVisibility(View.GONE);
                binding.maincheck.setVisibility(View.GONE);
                if(binding.maincheck.isChecked()){
                    binding.maincheck.setChecked(false);
                    if(type==1){
                        convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                        convertedandTrimmedAudioAdapter.notifyDataSetChanged();
                    }else if(type==2){
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }else if(type==3) {
                        convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                        convertedandTrimmedAudioAdapter.notifyDataSetChanged();
                    }else if(type==4){
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }else if(type==5){
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }else if(type==6){
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }
                }
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
                    binding.maincheck.setVisibility(View.VISIBLE);
                }else{
                    queryText=null;
                    binding.searchView1.setVisibility(View.GONE);
                    binding.toolbarTitle.setVisibility(View.VISIBLE);
                    binding.imgBtnSearch.setVisibility(View.VISIBLE);
                    binding.maincheck.setVisibility(View.VISIBLE);
                    if(type==1){
                        convertedandTrimmedAudioAdapter.getFilter().filter(queryText);
                    }else if(type==2){
                        trimmedVideoAdapter.getFilter().filter(queryText);
                    }else if(type==3){
                        convertedandTrimmedAudioAdapter.getFilter().filter(queryText);
                    }else if(type==4){
                        trimmedVideoAdapter.getFilter().filter(queryText);
                    }else if(type==5){
                        trimmedVideoAdapter.getFilter().filter(queryText);
                    }else if(type==6){
                        trimmedVideoAdapter.getFilter().filter(queryText);
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
                if(type==1){
                    convertedandTrimmedAudioAdapter.getFilter().filter(queryText);
                }else if(type==2){
                    trimmedVideoAdapter.getFilter().filter(queryText);
                }else if(type==3){
                    convertedandTrimmedAudioAdapter.getFilter().filter(queryText);
                }else if(type==4){
                    trimmedVideoAdapter.getFilter().filter(queryText);
                }else if(type==5){
                    trimmedVideoAdapter.getFilter().filter(queryText);
                }else if(type==6){
                    trimmedVideoAdapter.getFilter().filter(queryText);
                }
                return true;
            }
        });
        binding.maincheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.constraintshareDel.setVisibility(View.VISIBLE);
                    if(type==1){
                        ArrayList<String> selected=new ArrayList<>();
                        for(int i=0;i<OutputFiles.size();i++){
                            selected.add(OutputFiles.get(i).getAbsolutePath());
                        }
                        convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>(selected));
                        convertedandTrimmedAudioAdapter.notifyDataSetChanged();
                    }else if(type==2){
                        ArrayList<String> selected=new ArrayList<>();
                        for(int i=0;i<OutputFiles.size();i++){
                            selected.add(OutputFiles.get(i).getAbsolutePath());
                        }
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>(selected));
                       trimmedVideoAdapter.notifyDataSetChanged();
                    }else if(type==3) {
                        ArrayList<String> selected=new ArrayList<>();
                        for(int i=0;i<OutputFiles.size();i++){
                            selected.add(OutputFiles.get(i).getAbsolutePath());
                        }
                        convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>(selected));
                        convertedandTrimmedAudioAdapter.notifyDataSetChanged();
                    }else if(type==4){
                        ArrayList<String> selected=new ArrayList<>();
                        for(int i=0;i<OutputFiles.size();i++){
                            selected.add(OutputFiles.get(i).getAbsolutePath());
                        }
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>(selected));
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }else if(type==5){
                        ArrayList<String> selected=new ArrayList<>();
                        for(int i=0;i<OutputFiles.size();i++){
                            selected.add(OutputFiles.get(i).getAbsolutePath());
                        }
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>(selected));
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }else if(type==6){
                        ArrayList<String> selected=new ArrayList<>();
                        for(int i=0;i<OutputFiles.size();i++){
                            selected.add(OutputFiles.get(i).getAbsolutePath());
                        }
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>(selected));
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }
                }else{
                    binding.constraintshareDel.setVisibility(View.GONE);
                    if(type==1){

                        convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                        convertedandTrimmedAudioAdapter.notifyDataSetChanged();
                    }else if(type==2){

                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }else if(type==3) {

                        convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                        convertedandTrimmedAudioAdapter.notifyDataSetChanged();
                    }else if(type==4){
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }else if(type==5){
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }else if(type==6){
                        trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                        trimmedVideoAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        binding.imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==1){
                    shareAudios(null);
                }else{
                    shareVideo(null);
                }
            }
        });
        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWarningDialog(null);
            }
        });

    }
    public void getAndSetCreatedVideos(){
        new LoadData().execute();
    }
    private void getAndSetCreatedVideosInAdapter() {
        Collections.reverse(OutputFiles);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        trimmedVideoAdapter=new TrimmedVideoAdapter(OutputsActivity.this,OutputFiles);
        binding.recyclerOutputs.setLayoutManager(layoutManager);
        binding.recyclerOutputs.setAdapter(trimmedVideoAdapter);
        binding.recyclerOutputs.scrollToPosition(OutputFiles.size());

        trimmedVideoAdapter.onItemClicked(new OnVideoItemClicked() {
            @Override
            public void onItemClicked(String uri,String name) {
                if(getMediaDuration(Uri.parse(uri))>0) {
                    Intent intent = new Intent(OutputsActivity.this, VideoTrimmerActivity.class);
                    intent.putExtra(URI_INTENT_KEY,uri);
                    intent.putExtra(NAME_INTENT_KEY,name);
                    intent.putExtra(MAIN_TYPE_INTENT_KEY,1);
                    TrimmerActivity.type = 1;
                    mGetContent.launch(intent);
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2=alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb=dialogView.findViewById(R.id.progbar);
                    Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title=dialogView.findViewById(R.id.dialogtitle);
                    TextView msg=dialogView.findViewById(R.id.txtdeleteinstr);

                    title.setText(getResources().getString(R.string.loadfailed));
                    msg.setText(getResources().getString(R.string.loadfailedVideo));

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
                }
            }
        });
        trimmedVideoAdapter.onShareDeleteClicked(new OutputItemShareDeleteClicked() {
            @Override
            public void onDeleteClicked(String path) {
                deleteWarningDialog(path);
            }

            @Override
            public void onShareClicked(String path) {
                shareVideo(path);
            }
        });
        trimmedVideoAdapter.onItemLongClicked(new OnOutputItemsLongClicked() {
            @Override
            public void onLongPressed(boolean state) {
                if(state){
                    binding.constraintshareDel.setVisibility(View.VISIBLE);
                }else{
                    binding.constraintshareDel.setVisibility(View.GONE);
                }
            }
        });
        trimmedVideoAdapter.onAllChecked(new Checkallselected() {
            @Override
            public void allselected(int size) {
                binding.maincheck.setChecked(size == OutputFiles.size());
            }
        });
    }
    public long getduration(Uri pathStr){
        long millSecond=0;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            Uri uri=Uri.parse("file://"+pathStr);
            retriever.setDataSource(OutputsActivity.this, pathStr);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            millSecond = Long.parseLong(time);
            retriever.release();
        }catch (Exception e) {
            MediaPlayer mp = new MediaPlayer();
            try {
                mp.setDataSource(OutputsActivity.this, pathStr);
                mp.prepare();
                millSecond=  mp.getDuration();
            } catch (IOException f) {
                Log.d("-MS-","Cannot parse url");

                f.printStackTrace();
            }


        }
        return millSecond;
    }
    public  void getAndSetAudioAddedVideos(){
        new LoadData().execute();
    }
    private void getAndSetAudioAddedVideosInAdapter() {

        Collections.reverse(OutputFiles);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        trimmedVideoAdapter=new TrimmedVideoAdapter(OutputsActivity.this,OutputFiles);
        binding.recyclerOutputs.setLayoutManager(layoutManager);
        binding.recyclerOutputs.setAdapter(trimmedVideoAdapter);
        binding.recyclerOutputs.scrollToPosition(OutputFiles.size());
        trimmedVideoAdapter.onItemClicked(new OnVideoItemClicked() {
            @Override
            public void onItemClicked(String uri,String name) {
                if(getMediaDuration(Uri.parse(uri))>0) {
                    Intent intent = new Intent(OutputsActivity.this, VideoTrimmerActivity.class);
                    intent.putExtra(URI_INTENT_KEY,uri);
                    intent.putExtra(NAME_INTENT_KEY,name);
                    intent.putExtra(MAIN_TYPE_INTENT_KEY,1);
                    TrimmerActivity.type = 1;
                    mGetContent.launch(intent);
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2=alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb=dialogView.findViewById(R.id.progbar);
                    Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title=dialogView.findViewById(R.id.dialogtitle);
                    TextView msg=dialogView.findViewById(R.id.txtdeleteinstr);

                    title.setText(getResources().getString(R.string.loadfailed));
                    msg.setText(getResources().getString(R.string.loadfailedVideo));

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
                }
            }
        });
        trimmedVideoAdapter.onShareDeleteClicked(new OutputItemShareDeleteClicked() {
            @Override
            public void onDeleteClicked(String path) {
                deleteWarningDialog(path);
            }

            @Override
            public void onShareClicked(String path) {
                shareVideo(path);
            }
        });
        trimmedVideoAdapter.onItemLongClicked(new OnOutputItemsLongClicked() {
            @Override
            public void onLongPressed(boolean state) {
                if(state){
                    binding.constraintshareDel.setVisibility(View.VISIBLE);
                }else{
                    binding.constraintshareDel.setVisibility(View.GONE);
                }
            }
        });
        trimmedVideoAdapter.onAllChecked(new Checkallselected() {
            @Override
            public void allselected(int size) {
                binding.maincheck.setChecked(size == OutputFiles.size());
            }
        });
    }
    public  void getAndSetAudioRemovedVideos(){
        new LoadData().execute();
    }
    private void getAndSetAudioRemovedVideosInAdapter() {
        Collections.reverse(OutputFiles);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        trimmedVideoAdapter=new TrimmedVideoAdapter(OutputsActivity.this,OutputFiles);
        binding.recyclerOutputs.setLayoutManager(layoutManager);
        binding.recyclerOutputs.setAdapter(trimmedVideoAdapter);
        binding.recyclerOutputs.scrollToPosition(OutputFiles.size());


        trimmedVideoAdapter.onItemClicked(new OnVideoItemClicked() {
            @Override
            public void onItemClicked(String uri,String name) {
                if(getMediaDuration(Uri.parse(uri))>0) {
                    Intent intent = new Intent(OutputsActivity.this, AddAudioActivity.class);
                    TrimmerActivity.VIDEOPATH = uri;
                    intent.putExtra(VIDEO_TOTAL_DURATION, getMediaDuration(Uri.parse(uri)));
                    TrimmerActivity.TRIMMED_VIDEO_FOLDER_PATH = generatePath(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH, "");
                    TrimmerActivity.type = 4;
                    mGetContent.launch(intent);
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2=alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb=dialogView.findViewById(R.id.progbar);
                    Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title=dialogView.findViewById(R.id.dialogtitle);
                    TextView msg=dialogView.findViewById(R.id.txtdeleteinstr);

                    title.setText(getResources().getString(R.string.loadfailed));
                    msg.setText(getResources().getString(R.string.loadfailedVideo));

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
                }
            }
        });
        trimmedVideoAdapter.onShareDeleteClicked(new OutputItemShareDeleteClicked() {
            @Override
            public void onDeleteClicked(String path) {
                deleteWarningDialog(path);
            }

            @Override
            public void onShareClicked(String path) {
                shareVideo(path);
            }
        });
        trimmedVideoAdapter.onItemLongClicked(new OnOutputItemsLongClicked() {
            @Override
            public void onLongPressed(boolean state) {
                if(state){
                    binding.constraintshareDel.setVisibility(View.VISIBLE);
                }else{
                    binding.constraintshareDel.setVisibility(View.GONE);
                }
            }
        });
        trimmedVideoAdapter.onAllChecked(new Checkallselected() {
            @Override
            public void allselected(int size) {
                binding.maincheck.setChecked(size == OutputFiles.size());
            }
        });
    }

    public  void getAndSetConvertedAudios(){
        new LoadData().execute();
    }
    private void getAndSetConvertedAudiosInAdapter() {
        Collections.reverse(OutputFiles);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        convertedandTrimmedAudioAdapter=new ConvertedandTrimmedAudioAdapter(OutputsActivity.this,OutputFiles);
        binding.recyclerOutputs.setLayoutManager(layoutManager);
        binding.recyclerOutputs.setAdapter(convertedandTrimmedAudioAdapter);
        binding.recyclerOutputs.scrollToPosition(OutputFiles.size());
        convertedandTrimmedAudioAdapter.onAudioItemClick(new OnAudioClickInterface() {
            @Override
            public void onAudioItemClick(String uri, String name,long duration,float size) {
                loadAudioTrimmer(uri,name,duration,size);
            }
        });
        convertedandTrimmedAudioAdapter.onShareDeleteClicked(new OutputItemShareDeleteClicked() {
            @Override
            public void onDeleteClicked(String path) {
                deleteWarningDialog(path);
            }

            @Override
            public void onShareClicked(String path) {
                shareAudios(path);

            }
        });

        convertedandTrimmedAudioAdapter.onItemLongClicked(new OnOutputItemsLongClicked() {
            @Override
            public void onLongPressed(boolean state) {
                if(state){
                    binding.constraintshareDel.setVisibility(View.VISIBLE);
                }else{
                    binding.constraintshareDel.setVisibility(View.GONE);
                }
            }
        });
        convertedandTrimmedAudioAdapter.onallselected(new Checkallselected() {
            @Override
            public void allselected(int size) {
                binding.maincheck.setChecked(size == OutputFiles.size());
            }
        });

    }

    private void shareAudios(String path) {
        if(path!=null && !(path.isEmpty())) {
            File file = new File(path);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
            intent.setType("audio/*");
             /* List of the files you want to send */
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);

            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(intent);

        }else{
            ArrayList<String> paths = convertedandTrimmedAudioAdapter.getSelectedFiles();
            if (paths != null && paths.size() != 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                intent.setType("audio/*"); /* This example is sharing jpeg images. */
                ArrayList<Uri> files = new ArrayList<Uri>();
                for (String path1 : paths /* List of the files you want to send */) {
                    File file = new File(path1);
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    files.add(uri);
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                startActivity(intent);

            }
            convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
            convertedandTrimmedAudioAdapter.notifyDataSetChanged();
            binding.constraintshareDel.setVisibility(View.GONE);
        }
    }

    private void deleteWarningDialog(String path) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
        ViewGroup viewGroup=findViewById(android.R.id.content);
        View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.delete_warning_dialog,viewGroup,false);
        alertDialog.setView(dialogView);
        AlertDialog dialog=alertDialog.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        ImageButton imageButton=dialogView.findViewById(R.id.imgclose);
        Button yes=dialogView.findViewById(R.id.btndelyes);
        Button no=dialogView.findViewById(R.id.btndelno);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path != null && !(path.isEmpty())) {
                    File f = new File(path);
                    if (f.exists()) {
                        f.delete();
                    }
                    if(type==1) {
                        getAndSetConvertedAudios();
                    }else if(type==3){
                        getAndSetTrimmedAudios();
                    }else if(type==2){
                        getAndSetTrimmedVideos();
                    }else if(type==4){
                        getAndSetAudioRemovedVideos();
                    }else if(type==5){
                        getAndSetAudioAddedVideos();
                    }else if(type==6){
                        getAndSetCreatedVideos();
                    }
                }else{
                    if(type==1) {
                        ArrayList<String> paths=convertedandTrimmedAudioAdapter.getSelectedFiles();
                        if (paths != null && paths.size() > 0) {
                            for (String path : paths) {
                                File f = new File(path);
                                if (f.exists()) {
                                    f.delete();
                                }
                            }
                            convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                            getAndSetConvertedAudios();
                        }

                    }else if(type==2){
                        ArrayList<String> paths=trimmedVideoAdapter.getSelectedFiles();
                        if (paths != null && paths.size() > 0) {
                            for (String path : paths) {
                                File f = new File(path);
                                if (f.exists()) {
                                    f.delete();
                                }
                            }
                            trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                            getAndSetTrimmedVideos();
                        }
                    }else if(type==3){
                        ArrayList<String> paths=convertedandTrimmedAudioAdapter.getSelectedFiles();
                        if (paths != null && paths.size() > 0) {
                            for (String path : paths) {
                                File f = new File(path);
                                if (f.exists()) {
                                    f.delete();
                                }
                            }
                            convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                            getAndSetTrimmedAudios();
                        }
                    }else if(type==4){
                        ArrayList<String> paths=trimmedVideoAdapter.getSelectedFiles();
                        if (paths != null && paths.size() > 0) {
                            for (String path : paths) {
                                File f = new File(path);
                                if (f.exists()) {
                                    f.delete();
                                }
                            }
                            trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                            getAndSetAudioRemovedVideos();
                        }
                    }else if(type==5){
                        ArrayList<String> paths=trimmedVideoAdapter.getSelectedFiles();
                        if (paths != null && paths.size() > 0) {
                            for (String path : paths) {
                                File f = new File(path);
                                if (f.exists()) {
                                    f.delete();
                                }
                            }
                            trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                            getAndSetAudioAddedVideos();
                        }
                    }else if(type==6){
                        ArrayList<String> paths=trimmedVideoAdapter.getSelectedFiles();
                        if (paths != null && paths.size() > 0) {
                            for (String path : paths) {
                                File f = new File(path);
                                if (f.exists()) {
                                    f.delete();
                                }
                            }
                            trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                            getAndSetCreatedVideos();
                        }
                    }
                    dialog.dismiss();
                    binding.constraintshareDel.setVisibility(View.GONE);
                    if(binding.maincheck.isChecked()){
                        binding.maincheck.setChecked(false);
                    }
                }

                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void getAndSetTrimmedAudios(){
        new LoadData().execute();
    }
    private void getAndSetTrimmedAudiosInAdapter() {
        Collections.reverse(OutputFiles);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        convertedandTrimmedAudioAdapter=new ConvertedandTrimmedAudioAdapter(OutputsActivity.this,OutputFiles);
        binding.recyclerOutputs.setLayoutManager(layoutManager);
        binding.recyclerOutputs.setAdapter(convertedandTrimmedAudioAdapter);
        binding.recyclerOutputs.scrollToPosition(OutputFiles.size());
        convertedandTrimmedAudioAdapter.onAudioItemClick(new OnAudioClickInterface() {
            @Override
            public void onAudioItemClick(String uri, String name,long duration,float size) {
               loadAudioTrimmer(uri,name,duration,size);
            }
        });
        convertedandTrimmedAudioAdapter.onShareDeleteClicked(new OutputItemShareDeleteClicked() {
            @Override
            public void onDeleteClicked(String path) {
                deleteWarningDialog(path);
            }

            @Override
            public void onShareClicked(String path) {
                shareAudios(path);

            }
        });
        convertedandTrimmedAudioAdapter.onItemLongClicked(new OnOutputItemsLongClicked() {
            @Override
            public void onLongPressed(boolean state) {
                if(state){
                    binding.constraintshareDel.setVisibility(View.VISIBLE);
                }else{
                    binding.constraintshareDel.setVisibility(View.GONE);
                }
            }
        });
        convertedandTrimmedAudioAdapter.onallselected(new Checkallselected() {
            @Override
            public void allselected(int size) {
                binding.maincheck.setChecked(size == OutputFiles.size());
            }
        });


    }
    public void loadAudioTrimmer(String uri, String name,long duration,float size){
        if(timeConversion(duration).equals("00:00")||size==0.0){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
            ViewGroup viewGroup=findViewById(android.R.id.content);
            View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
            alertDialog.setView(dialogView);
            AlertDialog dialog2=alertDialog.create();
            dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog2.show();

            ProgressBar pb=dialogView.findViewById(R.id.progbar);
            Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
            TextView title=dialogView.findViewById(R.id.dialogtitle);
            TextView msg=dialogView.findViewById(R.id.plzWit);

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
            Intent intent = new Intent(OutputsActivity.this, AudioTrimmerActivity2.class);
            intent.putExtra(URI_INTENT_KEY, uri);
            intent.putExtra(NAME_INTENT_KEY, name);
            intent.putExtra(DURATION_INTENT_KEY, duration);
            intent.putExtra(AUDIOSIZE_INTENT_KEY, size);
            mGetContent3.launch(intent);
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
    public void getAndSetTrimmedVideos(){
        new LoadData().execute();
    }
    private void getAndSetTrimmedVideosInAdapter() {
        Collections.reverse(OutputFiles);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        trimmedVideoAdapter=new TrimmedVideoAdapter(OutputsActivity.this,OutputFiles);
        binding.recyclerOutputs.setLayoutManager(layoutManager);
        binding.recyclerOutputs.setAdapter(trimmedVideoAdapter);
        binding.recyclerOutputs.scrollToPosition(OutputFiles.size());
        trimmedVideoAdapter.onItemClicked(new OnVideoItemClicked() {
            @Override
            public void onItemClicked(String uri,String name) {
                if(getMediaDuration(Uri.parse(uri))>0) {
                    if (getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1) == 1) {
                        Intent intent = new Intent(OutputsActivity.this, VideoTrimmerActivity.class);
                        intent.putExtra(URI_INTENT_KEY,uri);
                        intent.putExtra(NAME_INTENT_KEY,name);
                        intent.putExtra(MAIN_TYPE_INTENT_KEY,1);
                        TrimmerActivity.type = 1;
                        mGetContent.launch(intent);
                    } else {
                        Intent intent = new Intent(OutputsActivity.this, VideoTrimmerActivity.class);
                        intent.putExtra(URI_INTENT_KEY,uri);
                        intent.putExtra(NAME_INTENT_KEY,name);
                        intent.putExtra(MAIN_TYPE_INTENT_KEY,2);
                        TrimmerActivity.type = 2;
                        mGetContent.launch(intent);
                    }
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OutputsActivity.this);
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(OutputsActivity.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2=alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb=dialogView.findViewById(R.id.progbar);
                    Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title=dialogView.findViewById(R.id.dialogtitle);
                    TextView msg=dialogView.findViewById(R.id.txtdeleteinstr);

                    title.setText(getResources().getString(R.string.loadfailed));
                    msg.setText(getResources().getString(R.string.loadfailedVideo));

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
                }
            }
        });
        trimmedVideoAdapter.onShareDeleteClicked(new OutputItemShareDeleteClicked() {
            @Override
            public void onDeleteClicked(String path) {
                deleteWarningDialog(path);
            }

            @Override
            public void onShareClicked(String path) {
                shareVideo(path);
            }
        });
        trimmedVideoAdapter.onItemLongClicked(new OnOutputItemsLongClicked() {
            @Override
            public void onLongPressed(boolean state) {
                if(state){
                    binding.constraintshareDel.setVisibility(View.VISIBLE);
                }else{
                    binding.constraintshareDel.setVisibility(View.GONE);
                }
            }
        });
        trimmedVideoAdapter.onAllChecked(new Checkallselected() {
            @Override
            public void allselected(int size) {
                binding.maincheck.setChecked(size == OutputFiles.size());
            }
        });

    }

    private void shareVideo(String path) {
        if(path!=null && !(path.isEmpty())) {
            File file = new File(path);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
            intent.setType("video/*");
            /* List of the files you want to send */
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);

            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(intent);

        }else{
            ArrayList<String> paths = trimmedVideoAdapter.getSelectedFiles();
            if (paths != null && paths.size() != 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                intent.setType("video/*"); /* This example is sharing jpeg images. */
                ArrayList<Uri> files = new ArrayList<Uri>();
                for (String path1 : paths /* List of the files you want to send */) {
                    File file = new File(path1);
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    files.add(uri);
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                startActivity(intent);

            }
            trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
            trimmedVideoAdapter.notifyDataSetChanged();
            binding.constraintshareDel.setVisibility(View.GONE);
        }
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
    private int  getMediaDuration(Uri uriOfFile)  {
        MediaPlayer mp = MediaPlayer.create(this,uriOfFile);
        int duration = mp.getDuration();
        return  duration;
    }

    @Override
    public void onBackPressed() {
        if(binding.maincheck.getVisibility()==View.VISIBLE){
        if(binding.maincheck.isChecked()){
            binding.maincheck.setChecked(false);
            if(type==1){
                convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                convertedandTrimmedAudioAdapter.notifyDataSetChanged();
            }else if(type==2){

                trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                trimmedVideoAdapter.notifyDataSetChanged();
            }else if(type==3) {

                convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                convertedandTrimmedAudioAdapter.notifyDataSetChanged();
            }else if(type==4){
                trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                trimmedVideoAdapter.notifyDataSetChanged();
            }else if(type==5){
                trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                trimmedVideoAdapter.notifyDataSetChanged();
            }else if(type==6){
                trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                trimmedVideoAdapter.notifyDataSetChanged();
            }

        }else{
            if(type==1){
                if(convertedandTrimmedAudioAdapter.getSelectedFiles().size()>0){
                    convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                    convertedandTrimmedAudioAdapter.notifyDataSetChanged();
                }else{
                    super.onBackPressed();
                }
            }else if(type==2){
                if(trimmedVideoAdapter.getSelectedFiles().size()>0){
                    trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                    trimmedVideoAdapter.notifyDataSetChanged();
                }else{
                    super.onBackPressed();
                }
            }else if(type==3) {
                if(convertedandTrimmedAudioAdapter.getSelectedFiles().size()>0){
                    convertedandTrimmedAudioAdapter.setSelectedFiles(new ArrayList<>());
                    convertedandTrimmedAudioAdapter.notifyDataSetChanged();
                }else{
                    super.onBackPressed();
                }
            }else if(type==4){
                if(trimmedVideoAdapter.getSelectedFiles().size()>0){
                    trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                    trimmedVideoAdapter.notifyDataSetChanged();
                }else{
                    super.onBackPressed();
                }
            }else if(type==5){
                if(trimmedVideoAdapter.getSelectedFiles().size()>0){
                    trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                    trimmedVideoAdapter.notifyDataSetChanged();
                }else{
                    super.onBackPressed();
                }
            }else if(type==6){
                if(trimmedVideoAdapter.getSelectedFiles().size()>0){
                    trimmedVideoAdapter.setSelectedFiles(new ArrayList<>());
                    trimmedVideoAdapter.notifyDataSetChanged();
                }else{
                    super.onBackPressed();
                }
            }

        }
        }else {
                if (queryText == null) {
                    binding.searchView1.setVisibility(View.GONE);
                    binding.toolbarTitle.setVisibility(View.VISIBLE);
                    binding.imgBtnSearch.setVisibility(View.VISIBLE);
                    binding.maincheck.setVisibility(View.VISIBLE);
                } else {
                    queryText = null;
                    binding.searchView1.setVisibility(View.GONE);
                    binding.toolbarTitle.setVisibility(View.VISIBLE);
                    binding.imgBtnSearch.setVisibility(View.VISIBLE);
                    binding.maincheck.setVisibility(View.VISIBLE);
                    if (type == 1) {
                        convertedandTrimmedAudioAdapter.getFilter().filter(queryText);
                    } else if (type == 2) {
                        trimmedVideoAdapter.getFilter().filter(queryText);
                    } else if(type==3) {
                        convertedandTrimmedAudioAdapter.getFilter().filter(queryText);
                    }else if(type==4){
                        trimmedVideoAdapter.getFilter().filter(queryText);
                    }else if(type==5){
                        trimmedVideoAdapter.getFilter().filter(queryText);
                    }else if(type==6){
                        trimmedVideoAdapter.getFilter().filter(queryText);
                    }
            }
        }
    }
    public class LoadData extends  AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            OutputFiles.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingProgress(true);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(type==1){
                File file= setRecovery_Audio_Directory(constants.CONVERTED_AUDIO_FOLDER_PATH);
                File[] files = file.listFiles();
                Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
                if(files!=null) {
                    for (File f : files) {
                        if (!f.isDirectory()) {
                            Log.d(TAG, "getFilesList: filePath :: " + f.getPath());
                            Log.d(TAG, "getFilesList: fileName :: " + f.getName());
                            OutputFiles.add(f);
                        }
                    }
                }
            }else if(type==3){
                File file= setRecovery_Audio_Directory(constants.TRIMMED_AUDIO_FOLDER_PATH);
                File[] files = file.listFiles();
                Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
                if(files!=null) {
                    for (File f : files) {
                        if (!f.isDirectory()) {
                            Log.d(TAG, "getFilesList: filePath :: " + f.getPath());
                            Log.d(TAG, "getFilesList: fileName :: " + f.getName());
                            OutputFiles.add(f);
                        }
                    }
                }
            }else if(type==2){

                File file= setRecovery_Audio_Directory(constants.TRIMMED_VIDEO_FOLDER_PATH);
                File[] files = file.listFiles();
                Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
                if(files!=null) {
                    for (File f : files) {
                        if (!f.isDirectory()) {
                            Log.d(TAG, "getFilesList: filePath :: " + f.getPath());
                            Log.d(TAG, "getFilesList: fileName :: " + f.getName());
                            OutputFiles.add(f);
                        }
                    }
                }
            }else if(type==4){

                File file= setRecovery_Audio_Directory(constants.REMOVED_AUDIO_VIDEO_FOLDER_PATH);
                File[] files = file.listFiles();
                Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
                if(files!=null) {
                    for (File f : files) {
                        if (!f.isDirectory()) {
                            Log.d(TAG, "getFilesList: filePath :: " + f.getPath());
                            Log.d(TAG, "getFilesList: fileName :: " + f.getName());
                            OutputFiles.add(f);
                        }
                    }
                }
            }else if(type==5){

                File file= setRecovery_Audio_Directory(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH);
                File[] files = file.listFiles();
                Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
                if(files!=null) {
                    for (File f : files) {
                        if (!f.isDirectory()) {
                            Log.d(TAG, "getFilesList: filePath :: " + f.getPath());
                            Log.d(TAG, "getFilesList: fileName :: " + f.getName());
                            OutputFiles.add(f);
                        }
                    }
                }
            }else if(type==6){

                File file= setRecovery_Audio_Directory(constants.CREATED_VIDEO_WITH_IMAGES_FOLDER_PATH);
                File[] files = file.listFiles();
                Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
                if(files!=null) {
                    for (File f : files) {
                        if (!f.isDirectory()) {
                            Log.d(TAG, "getFilesList: filePath :: " + f.getPath());
                            Log.d(TAG, "getFilesList: fileName :: " + f.getName());
                            OutputFiles.add(f);
                        }
                    }
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type==1){
                        getAndSetConvertedAudiosInAdapter();
                    }else if(type==3){
                        getAndSetTrimmedAudiosInAdapter();
                    }else if(type==2){
                        getAndSetTrimmedVideosInAdapter();

                    }else if(type==4){
                        getAndSetAudioRemovedVideosInAdapter();
                    }else if(type==5){
                        getAndSetAudioAddedVideosInAdapter();
                    }else if(type==6){
                        getAndSetCreatedVideosInAdapter();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(type==1){
                convertedandTrimmedAudioAdapter.notifyDataSetChanged();
            }else if(type==2){
                trimmedVideoAdapter.notifyDataSetChanged();
            }else if(type==3) {
                convertedandTrimmedAudioAdapter.notifyDataSetChanged();
            }else if(type==4){
                trimmedVideoAdapter.notifyDataSetChanged();
            }else if(type==5){
                trimmedVideoAdapter.notifyDataSetChanged();
            }else if(type==6){
                trimmedVideoAdapter.notifyDataSetChanged();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingProgress(false);
                }
            });
        }
    }
    private void showLoadingProgress(boolean state) {

        if(state) {
            binding.constraintProgress.setVisibility(View.VISIBLE);

        }else{
            binding.constraintProgress.setVisibility(View.GONE);
        }
    }
    public File setRecovery_Audio_Directory(String recovery_audios_path) {
        File recoveryDirectory=new File(recovery_audios_path);
        if(!recoveryDirectory.exists()){
            recoveryDirectory.mkdirs();
        }
        return recoveryDirectory;
    }

}