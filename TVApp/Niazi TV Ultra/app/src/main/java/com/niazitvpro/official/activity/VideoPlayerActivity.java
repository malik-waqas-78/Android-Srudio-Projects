package com.niazitvpro.official.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.niazitvpro.official.R;
import com.niazitvpro.official.utils.AdmobBannerAd;
import com.niazitvpro.official.utils.SharedPrefTVApp;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    public static PlayerView videoView;
    private ImageView image_back;
    private ToggleButton imgFullScreen;
    private String path,filename;
    private ProgressBar progressBar;
    private MediaController mediaController;
    private boolean isLandscape = false;
    private RelativeLayout rlToolbar;
    private int videoWidth,videoHeight;
    private RelativeLayout rlVideoView;
    private RelativeLayout.LayoutParams lp;
    private int screenHeight = 0;
    public static SimpleExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        SharedPrefTVApp.transparentStatusAndNavigation(VideoPlayerActivity.this);

        initView();
    }

    private void initView(){

        AdmobBannerAd admobBannerAd = new AdmobBannerAd();
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_video_player,null,false);
        admobBannerAd.loadAddBanner(view,VideoPlayerActivity.this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;



         videoView =(PlayerView) findViewById(R.id.show_downloaded_video);
         imgFullScreen =(ToggleButton) findViewById(R.id.img_video_full_screen);
         image_back = findViewById(R.id.img_video_back);
         rlToolbar = findViewById(R.id.ll_toolbar);
         rlVideoView = findViewById(R.id.rl_video_view);
         progressBar = findViewById(R.id.video_progress);


        lp = (RelativeLayout.LayoutParams) rlVideoView.getLayoutParams();
        lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height = (int) (screenHeight * 0.35);
        rlVideoView.setLayoutParams(lp);

         path = getIntent().getExtras().getString("video_image_path");
         filename = getIntent().getExtras().getString("filename");

        progressBar.setVisibility(View.VISIBLE);
        startVideo();

      image_back.setOnClickListener(this);
      imgFullScreen.setOnClickListener(this);

    }

    private void startVideo(){
        //specify the location of media file
        Uri uri= Uri.parse(path);
        prepareExoPlayerFromFileUri(uri);

//        //Setting MediaController and URI, then starting the videoView
//        videoView.setMediaController(mediaController);
//        videoView.setVideoURI(uri);
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        ViewGroup.LayoutParams params = videoView.getLayoutParams();
//        params.height = displayMetrics.widthPixels;
//        params.width = displayMetrics.heightPixels/2;
//        videoView.setLayoutParams(params);
//        videoView.requestLayout();
//        videoView.start();
//
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                        /*
//                         *  add media controller
//                         */
//                        mediaController = new MediaController(VideoPlayerActivity.this);;
//                        videoView.setMediaController(mediaController);
//                        /*
//                         * and set its position on screen
//                         */
//                        mediaController.setAnchorView(videoView);
//
//
//                        videoWidth  = width;
//                        videoHeight =height;
//
//                        progressBar.setVisibility(View.GONE);
//
//                    }
//                });
//            }
//        });
//
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
//        {
//            @Override
//            public void onCompletion(MediaPlayer mp)
//            {
//
//                onBackPressed();
//
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        if(v==imgFullScreen){
            if(isLandscape){
                imgFullScreen.setChecked(false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                rlToolbar.setVisibility(View.VISIBLE);
                lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                lp.height = (int) (screenHeight * 0.35);
                MainActivity.cardView.setVisibility(View.VISIBLE);
                lp.removeRule(RelativeLayout.CENTER_VERTICAL);
                WindowManager.LayoutParams attrs =getWindow().getAttributes();
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getWindow().setAttributes(attrs);
                SharedPrefTVApp.showSystemUI(VideoPlayerActivity.this);
                isLandscape = false;
            }else {
                imgFullScreen.setChecked(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                rlToolbar.setVisibility(View.GONE);
                lp = (RelativeLayout.LayoutParams) rlVideoView.getLayoutParams();
                lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                rlVideoView.setLayoutParams(lp);
                WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                attrs.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                getWindow().setAttributes(attrs);
                SharedPrefTVApp.hideSystemUI(VideoPlayerActivity.this);
                isLandscape = true;
            }

        }

        if(v==image_back){
            onBackPressed();
        }
    }

    public void release() {

        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.release();
            exoPlayer = null;
            videoView = null;
        }
    }

    @Override
    public void onBackPressed() {
        if(isLandscape){
            imgFullScreen.setChecked(false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            rlToolbar.setVisibility(View.VISIBLE);
            lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            lp.height = (int) (screenHeight * 0.35);
            MainActivity.cardView.setVisibility(View.VISIBLE);
            lp.removeRule(RelativeLayout.CENTER_VERTICAL);
            WindowManager.LayoutParams attrs =getWindow().getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            isLandscape = false;
        }else {

            release();
            super.onBackPressed();
        }

    }

    private void prepareExoPlayerFromFileUri(Uri uri){
        exoPlayer =new SimpleExoPlayer.Builder(VideoPlayerActivity.this).build();

        DataSpec dataSpec = new DataSpec(uri);
        final FileDataSource fileDataSource = new FileDataSource();
        try {
            fileDataSource.open(dataSpec);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return fileDataSource;
            }
        };
        MediaSource audioSource = new ExtractorMediaSource(fileDataSource.getUri(),
                factory, new DefaultExtractorsFactory(), null, null);

        exoPlayer.prepare(audioSource);
        videoView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.seekTo(0, 0);
        videoView.setControllerShowTimeoutMs(3000);
        progressBar.setVisibility(View.GONE);
    }

}