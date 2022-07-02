package com.recovery.data.forwhatsapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.google.android.gms.ads.LoadAdError;
import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.audiopkg.AdapterAudiosOKRA;
import com.recovery.data.forwhatsapp.audiopkg.AudiosManageRecoveryDirectoryOKRA;
import com.recovery.data.forwhatsapp.audiopkg.AudiosPlayInterfaceOKRA;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ActivityAudiosOKRA extends AppCompatActivity implements AudiosPlayInterfaceOKRA, LongClickInterfaceOKRA {

    private static final String TAG = "92727";
    RecyclerView recyclerView;
    AdapterAudiosOKRA _audios_adapter;
    ArrayList<File> audios_files = new ArrayList<>();
    AudiosManageRecoveryDirectoryOKRA manage_directory;
    public static Context context;
    ImageView delete, share;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    SearchView searchView;
    LinearLayout linearSelected;
    ImageView selectDot;
    ArrayList<String> selectall=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_audios_okra);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        if(AATAdsManager.isAppInstalledFromPlay(this)) {
////            loadFbBannerAdd();
////            loadAdmobBanner();
//        }
        this.context = getApplicationContext();
        initComponents();
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        int id2 = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_button", null, null);
        int id3 = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);
        ImageView searchClose = searchView.findViewById(id3);
        searchClose.setColorFilter(Color.GRAY);
        ImageView searchIcon = searchView.findViewById(id2);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_search_black));
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(textView, 0); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {}
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                _audios_adapter.getFilter().filter(newText);
                return true;
            }
        });
        linearSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectall.size()==0) {
                    selectDot.setBackgroundResource(R.drawable.ic_chek_icon);
                    for (int i = 0; i < audios_files.size(); i++) {
                        selectall.add(audios_files.get(i).getAbsolutePath());
                    }
                    _audios_adapter.setSelectedRows(selectall);
                    _audios_adapter.notifyDataSetChanged();
                }else{
                    selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    _audios_adapter.setSelectedRows(new ArrayList<String>());
                    selectall.clear();
                    _audios_adapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void initComponents() {
        manage_directory = new AudiosManageRecoveryDirectoryOKRA();
        delete = findViewById(R.id.delete);
        constraintLayout = findViewById(R.id.nodatacons);
        progressBar = findViewById(R.id.audioprogressbar);
        searchView=findViewById(R.id.searchView2);
        share = findViewById(R.id.share);
        recyclerView = findViewById(R.id.audios_recycleview);
        linearSelected=findViewById(R.id.linearSelectAll6);
        selectDot=findViewById(R.id.imageselectdot6);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //init adapter
        _audios_adapter = new AdapterAudiosOKRA(audios_files, this);
        _audios_adapter.setAudios_play_interface(this);
        _audios_adapter.setLongClickListeners(this);
        //setAadapteData
        //cardView_images_adapter.setItemClickListeners(this);
        recyclerView.setAdapter(_audios_adapter);
        delete.setOnClickListener(new DeleteSelectedItems());
        share.setOnClickListener(new ShareSelectedItems());


        delete.setVisibility(View.GONE);
        share.setVisibility(View.GONE);
    }

    class DeleteSelectedItems implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(_audios_adapter.getSelectedRows().size()>0) {
                deleteWarningDialog();
            }else{
                Toast.makeText(ActivityAudiosOKRA.this, "Kindly select atleast one audio", Toast.LENGTH_SHORT).show();
            }
            //            ArrayList<String> paths=_audios_adapter.getSelectedRows();
//            if(paths!=null&&paths.size()!=0){
//                for(String path:paths){
//                    File f=new File(path);
//                    f.delete();
//                }
//                _audios_adapter.setSelectedRows(new ArrayList<>());
//                setAdapterData();
//            }
        }
    }

    class ShareSelectedItems implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ArrayList<String> paths = _audios_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                intent.setType("audio/*"); /* This example is sharing jpeg images. */
                ArrayList<Uri> files = new ArrayList<Uri>();
                for (String path : paths /* List of the files you want to send */) {
                    File file = new File(path);
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getResources().getString(R.string.authority), file);
                    files.add(uri);
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                startActivity(intent);
            }
            _audios_adapter.setSelectedRows(new ArrayList<>());
            setAdapterData();
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.example_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        int id = searchView.getContext()
//                .getResources()
//                .getIdentifier("android:id/search_src_text", null, null);
//        int id2 = searchView.getContext()
//                .getResources()
//                .getIdentifier("android:id/search_button", null, null);
//        int id3 = searchView.getContext()
//                .getResources()
//                .getIdentifier("android:id/search_close_btn", null, null);
//        TextView textView = (TextView) searchView.findViewById(id);
//        textView.setTextColor(Color.BLACK);
//        ImageView searchClose = searchView.findViewById(id3);
//        searchClose.setColorFilter(Color.BLACK);
//
//        try {
//            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
//            mCursorDrawableRes.setAccessible(true);
//            mCursorDrawableRes.set(textView, 0); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
//        } catch (Exception e) {}
//        ImageView searchIcon = searchView.findViewById(id2);
//        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_search_black));
//        searchView.setBackgroundResource(R.drawable.searchview_background);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                _audios_adapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//        return true;
//    }
    private void setAdapterData() {
        linearSelected.setVisibility(View.GONE);
        selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
        new LoadData().execute();
        //image_files=manage_directory.get_Recovery_Images();
        Log.d(TAG, "setAdapterData: recoverd images size :: " + audios_files.size());
    }

    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            audios_files.clear();
            _audios_adapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... strings) {
            audios_files.addAll(manage_directory.get_Recovery_Audios());
            return "Task Completed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (audios_files.size() == 0) {
                constraintLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                share.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
//                share.setVisibility(View.VISIBLE);
//                delete.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.GONE);
                _audios_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAdapterData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void playAudio(int position, String url, String fileName) {
//        MediaPlayer mp = audioPlayer(url);
//        PlayDialog dialog = new PlayDialog(url);
//        dialog.showDialog(this, mp);


        Intent intent = new Intent(this, ActivityPlayAudioOKRA.class);
        intent.putExtra("url", url);
        intent.putExtra("name", fileName);
        startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediaPlayer audioPlayer(String url) {
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).
                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        mp.setAudioAttributes(
                audioAttributes
        );

        try {
            mp.setDataSource(url);
            Log.d(TAG, "audioPlayer: source");
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d(TAG, "audioPlayer: started");
        mp.start();
        return mp;
    }


    @Override
    public void onLongClicked(boolean flag) {
        if (flag) {
            delete.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            linearSelected.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            linearSelected.setVisibility(View.GONE);
            selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
        }
    }

    private void deleteWarningDialog() {
        Dialog dialog = new Dialog(ActivityAudiosOKRA.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_warning_layout_okra);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(getDrawable(android.R.color.transparent));

        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button no = dialog.findViewById(R.id.btn_no);


        no.setOnClickListener(view -> {
            dialog.dismiss();
        });
        btn_yes.setOnClickListener(view -> {
            ArrayList<String> paths = _audios_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                for (String path : paths) {
                    File f = new File(path);
                    f.delete();
                }
                _audios_adapter.setSelectedRows(new ArrayList<>());
                setAdapterData();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    public class PlayDialog {
        MediaPlayer mp;
        String url;
        int duration;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(0);
        ProgressBar progressBar;
        boolean finished = false;

        public PlayDialog(String url) {
            this.url = url;
        }

        public void showDialog(Context activity, MediaPlayer mp) {
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                }
            });
            duration = mp.getDuration();
            this.mp = mp;
            final Dialog dialog = new Dialog(activity);
            dialog.setTitle(R.string.title);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_audio_playing_okra);
            ImageView play = dialog.findViewById(R.id.play);
            TextView filename = dialog.findViewById(R.id.filename);
            progressBar = dialog.findViewById(R.id.progressBar);
            progressBar.setProgress(0);
            progressBar.setMax(duration);
            ProgressBar progressbar = progressBar;
            if (progressBar.getProgress() == 0) {
                startservice();
            }
            filename.setText(new File(url).getName());
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //mp.reset();
                    play.setImageResource(R.drawable.play);
                    try {
                        service.awaitTermination(10, TimeUnit.MICROSECONDS);
                        progressbar.setProgress(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;

                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    if (mp.isPlaying()) {
                        mp.pause();
                        play.setImageResource(R.drawable.play);
                    } else {
                        mp.start();
                        play.setImageResource(R.drawable.pause);
                    }
                }
            });
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mp.stop();
                    finished = true;
                    service.shutdown();
                }
            });
            dialog.show();
        }

        private void startservice() {
            ScheduledFuture scheduledFuture = null;
            ScheduledFuture finalScheduledFuture = scheduledFuture;
            scheduledFuture = service.scheduleWithFixedDelay(() -> {
                progressBar.setProgress(mp.getCurrentPosition());
            }, 1, 1, TimeUnit.MICROSECONDS);
        }
    }

//    public void loadFbBannerAdd() {
//        AdView adView = new AdView(this, getResources().getString(R.string.fbbannerad), AdSize.BANNER_HEIGHT_50);
//
//        AdListener adListener = new AdListener() {
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.d("TAG", "onError: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//
//
//        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
//        relativeLayout.addView(adView);
//
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
//    public void loadAdmobBanner() {
//
//        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(ActivityAudiosAAT.this);
//        com.google.android.gms.ads.AdSize adSize = AATAdmobFullAdManager.getAdSize(ActivityAudiosAAT.this);
//        mAdView.setAdSize(adSize);
//
//        mAdView.setAdUnitId(getResources().getString(R.string.admob_banner));
//
//        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
//
//        final RelativeLayout adViewLayout = (RelativeLayout) findViewById(R.id.bottom_banner);
//        adViewLayout.addView(mAdView);
//
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                adViewLayout.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAdClicked() {
//                super.onAdClicked();
//            }
//
//            @Override
//            public void onAdImpression() {
//                super.onAdImpression();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                adViewLayout.setVisibility(View.VISIBLE);
//            }
//        });
//
//    }

}