package com.video.trimmer.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import com.arthenica.mobileffmpeg.Config;
//import com.arthenica.mobileffmpeg.ExecuteCallback;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.arthenica.mobileffmpeg.FFmpeg;
import com.airbnb.lottie.LottieAnimationView;
import com.video.trimmer.R;
import com.video.trimmer.databinding.ActivityVideoPlayerBinding;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.SharedPrefClass;
import com.video.trimmer.videoTrimmer.interfaces.OnRangeSeekBarListener;
import com.video.trimmer.videoTrimmer.interfaces.OnTrimVideoListener;
import com.video.trimmer.videoTrimmer.utils.AudioExtractor;
import com.video.trimmer.videoTrimmer.utils.TrimVideoUtils;
import com.video.trimmer.videoTrimmer.view.RangeSeekBarView;
import com.video.trimmer.videoTrimmer.view.Thumb;


import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
//import com.mohammedalaa.seekbar.DoubleValueSeekBarView;
//import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener;
import com.video.timeline.VideoTimeLine;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import static com.video.trimmer.utils.Constants.MAIN_TYPE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.NAME_INTENT_KEY;

import static com.video.trimmer.utils.Constants.URI_INTENT_KEY;
import static com.video.trimmer.videoTrimmer.utils.TrimVideoUtils.stringForTime;


public class VideoPlayerActivity extends AppCompatActivity {
    ActivityVideoPlayerBinding binding;
    Uri uri;
    String name;
    boolean  check;
    int type;
    private final int progressStatus = 0;
    Float oneSecValue;
    Thread progressThread;
    ScheduledExecutorService service;
    private Handler handler = new Handler();
    SimpleExoPlayer player;
    long startposition,endposition;
    boolean firstTime=true;
    SharedPrefClass sharedPrefClass;
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        File f2=new File(constants.TEMP_FOLDER_PATH);
        if(!f2.exists()){
            f2.mkdir();
        }
        File f3=new File(constants.CONVERTED_AUDIO_FOLDER_PATH);
        if(!f3.exists()){
            f3.mkdir();
        }
        File f4=new File(constants.REMOVED_AUDIO_VIDEO_FOLDER_PATH);
        if(!f4.exists()){
            f4.mkdir();
        }
        File f5=new File(constants.TRIMMED_VIDEO_FOLDER_PATH);
        if(!f5.exists()){
            f5.mkdir();
        }

        showLoadingProgress(true);
        binding.toolbar.setTitle("");
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_icon));
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sharedPrefClass=new SharedPrefClass(VideoPlayerActivity.this);
        uri = Uri.parse(getIntent().getStringExtra(URI_INTENT_KEY));
        name = getIntent().getStringExtra(NAME_INTENT_KEY);
        type = getIntent().getIntExtra(MAIN_TYPE_INTENT_KEY, 1);
        service = Executors.newScheduledThreadPool(0);
        binding.toolbarTitle.setText(name);

        if(type==1){
            binding.btntitleTrim.setVisibility(View.VISIBLE);
        }else if(type==2){
            binding.btntitleconvert.setVisibility(View.VISIBLE);
        }else if(type==3){
            binding.btntitleRemove.setVisibility(View.VISIBLE);
        }

        player = new SimpleExoPlayer.Builder(VideoPlayerActivity.this).build();


        binding.videoView.setPlayer(player);
        player.setPlayWhenReady(true);
        MediaItem mediaItem = MediaItem.fromUri(uri);

        player.setMediaItem(mediaItem);

        player.prepare();
        player.addListener(new Player.Listener() {
            @Override
            public void onSeekForwardIncrementChanged(long seekForwardIncrementMs) {
                binding.seekbar.setProgress((int) seekForwardIncrementMs);
            }
        });
        player.addListener(new Player.Listener() {
            @Override
            public void onSeekBackIncrementChanged(long seekBackIncrementMs) {
                binding.seekbar.setProgress((int) seekBackIncrementMs);
            }
        });

        PlayerControlView.ProgressUpdateListener progressUpdateListener=new PlayerControlView.ProgressUpdateListener() {
            @Override
            public void onProgressUpdate(long position, long bufferedPosition) {
                binding.seekbar.setProgress((int) position);
            }
        };
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    binding.iconVideoPlay.setBackground(getResources().getDrawable(R.drawable.ic_on_video_icon));
                    if(firstTime) {
                        VideoTimeLine.with(String.valueOf(uri)).show(binding.fixedThumbList);
                        showLoadingProgress(false);
                        firstTime=false;

                        binding.seekbar.setMax(Integer.parseInt(String.valueOf(player.getDuration())));
                        startservice();
                        startposition = 0;
                        endposition = player.getDuration();

                        binding.textTimeSelection.setText(timeConversion(startposition));
                        binding.textTimeSelectionEnd.setText(timeConversion(endposition));

                        long dif = endposition - startposition;
                        binding.textTime.setText(timeConversion(dif));
                        binding.rangeseekbar2.initMaxWidth();
                    }
                }
                else {
                    binding.iconVideoPlay.setBackground(getResources().getDrawable(R.drawable.ic_off_video_icon));
                    if(firstTime) {
                        VideoTimeLine.with(String.valueOf(uri)).show(binding.fixedThumbList);
                        showLoadingProgress(false);
                        firstTime=false;

                        binding.seekbar.setMax(Integer.parseInt(String.valueOf(player.getDuration())));
                        startservice();
                        startposition = 0;
                        endposition = player.getDuration();

                        binding.textTimeSelection.setText(timeConversion(startposition));
                        binding.textTimeSelectionEnd.setText(timeConversion(endposition));

                        long dif = endposition - startposition;
                        binding.textTime.setText(timeConversion(dif));
                        binding.rangeseekbar2.initMaxWidth();
                    }
                }
            }
        });

        binding.btnPlusStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeConversion(startposition) .equals(timeConversion(endposition)) ) {
                    startposition+=1000;
                    player.seekTo(Integer.parseInt(String.valueOf(startposition)));
                    binding.seekbar.setProgress((int) startposition);
                    binding.textTimeSelection.setText(timeConversion(startposition));
                    int pos= (int) player.getCurrentPosition();
                    double f = (double) (binding.rangeseekbar2.getThumbValue(0) + getOneSecValue());
                    binding.rangeseekbar2.setThumbValue(0, (float) f);


                }
            }
        });
        binding.btnMinusStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeConversion(startposition) .equals(timeConversion(0)) ) {
                    startposition-=1000;
                    player.seekTo(Integer.parseInt(String.valueOf(startposition)));
                    binding.seekbar.setProgress((int) startposition);
                    binding.textTimeSelection.setText(timeConversion(startposition));
                    int pos= (int) player.getCurrentPosition();
                    double f = (double) (binding.rangeseekbar2.getThumbValue(0) - getOneSecValue());
                    binding.rangeseekbar2.setThumbValue(0, (float) f);

                }
            }
        });
        binding.btnPlusEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeConversion(endposition) .equals(timeConversion(player.getDuration())) ) {
                    endposition += 1000;
                    binding.textTimeSelectionEnd.setText(timeConversion(endposition));
                    long dif = endposition - startposition;
                    binding.textTime.setText(timeConversion(dif));
                    int pos=(int)(endposition/1000);
                    float f = (float) (binding.rangeseekbar2.getThumbValue(1) + getOneSecValue());
                   binding.rangeseekbar2.setThumbValue(1, f);

                }
            }
        });
        binding.btnMinusEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeConversion(endposition).equals(timeConversion(startposition))) {
                    endposition -= 1000;
                    binding.textTimeSelectionEnd.setText(timeConversion(endposition));
                    long dif = endposition - startposition;
                    binding.textTime.setText(timeConversion(dif));
                    float f = (float) (binding.rangeseekbar2.getThumbValue(1) - getOneSecValue());
                    binding.rangeseekbar2.setThumbValue(1, f);
                }
            }
        });
        binding.rangeseekbar2.addOnRangeSeekBarListener(binding.progressview2);
        binding.rangeseekbar2.addOnRangeSeekBarListener(new OnRangeSeekBarListener() {
            @Override
            public void onCreate(RangeSeekBarView rangeSeekBarView, int index, float value) {

            }

            @Override
            public void onSeek(RangeSeekBarView rangeSeekBarView, int index, float value) {
                onSeekThumbs(index, value);

                long diff=endposition-startposition;
                binding.textTime.setText(stringForTime((int) diff));
            }

            @Override
            public void onSeekStart(RangeSeekBarView rangeSeekBarView, int index, float value) {

            }

            @Override
            public void onSeekStop(RangeSeekBarView rangeSeekBarView, int index, float value) {
                player.pause();
            }
        });

        binding.iconForwardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(player.getCurrentPosition()+2000>endposition)){
                    player.seekTo(player.getCurrentPosition()+2000);
                    binding.seekbar.setProgress(Integer.parseInt(String.valueOf(player.getCurrentPosition())));
                }

            }
        });
        binding.iconReverseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(player.getCurrentPosition()-2000<startposition)){
                    player.seekTo(player.getCurrentPosition()-2000);
                    binding.seekbar.setProgress(Integer.parseInt(String.valueOf(player.getCurrentPosition())));
                }
            }
        });
        binding.iconVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying()){
                    player.pause();
                }else{
                    player.play();
                }
            }
        });
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    if(progress>startposition&&progress<endposition) {
                        player.seekTo(progress);
                        binding.textSize.setText(timeConversion(progress));

                        int width = binding.seekbar.getWidth()
                                - binding.seekbar.getPaddingLeft()
                                - binding.seekbar.getPaddingRight();
                        float thumbPos = width * (binding.seekbar.getProgress() / (float) binding.seekbar.getMax());
                        binding.textSize.setTranslationX(thumbPos);
                    }else{
                        player.seekTo(startposition);
                        binding.textSize.setText(timeConversion(startposition));
                        player.pause();
                        int width = binding.seekbar.getWidth()
                                - binding.seekbar.getPaddingLeft()
                                - binding.seekbar.getPaddingRight();
                        float thumbPos = width * (binding.seekbar.getProgress() / (float) binding.seekbar.getMax());
                        binding.textSize.setTranslationX(thumbPos);
                    }
                }else{
                    if(progress==endposition){
                        player.pause();
                        binding.seekbar.setProgress(Integer.parseInt(String.valueOf(startposition)));
                        player.seekTo(startposition);
                    }else {
                        binding.textSize.setText(timeConversion(progress));
                        int width = binding.seekbar.getWidth()
                                - binding.seekbar.getPaddingLeft()
                                - binding.seekbar.getPaddingRight();
                        float thumbPos = width * (binding.seekbar.getProgress() / (float) binding.seekbar.getMax());
                        binding.textSize.setTranslationX(thumbPos);
                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(player.isPlaying()){
                    player.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(!player.isPlaying()){
                    player.play();
                }
            }
        });
        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new LoadData().execute();
            }
        });

    }
    int count=0;
    Handler handler4;
    Runnable runnable4;
    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler4 = new Handler();
                    runnable4 = new Runnable() {
                        @Override
                        public void run() {
                            count++;
                            handler4.postDelayed(runnable4, 1);
                        }
                    };
                    handler4.postDelayed(runnable4, 0);
                    if(TrimmerActivity.type==1){
                        showLoadingProgress2(true,2);
                    }else if(TrimmerActivity.type==2){
                        showLoadingProgress2(true,1);
                    }else if(TrimmerActivity.type==3){
                        showLoadingProgress2(true,3);
                    }
                }
            });

        }

        @Override
        protected String doInBackground(String... strings) {

            if (TrimmerActivity.type == 1) {

                if (player.isPlaying()) {
                    player.pause();
                    binding.iconVideoPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
                }
                try {
                    TrimVideoUtils.startTrim(VideoPlayerActivity.this, new File(String.valueOf(uri)), constants.TRIMMED_VIDEO_FOLDER_PATH, startposition, endposition, new OnTrimVideoListener() {
                        @Override
                        public void onTrimStarted(int type) {

                        }

                        @Override
                        public void getResult(Uri uri, int check) {
                            sharedPrefClass.saveOutputPathinShared(String.valueOf(uri));
                        }

                        @Override
                        public void cancelAction() {

                        }

                        @Override
                        public void onError(String message) {

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (TrimmerActivity.type == 2) {

                if (player.isPlaying()) {
                    player.pause();
                    binding.iconVideoPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
                }
                File f=new File(constants.CONVERTED_AUDIO_FOLDER_PATH+"Converted"+System.currentTimeMillis()+".mp3");
                sharedPrefClass.saveOutputPathinShared(f.getPath());
                try {
                    if(isVideoHaveAudioTrack(String.valueOf(uri))) {
                        new AudioExtractor().genVideoUsingMuxer(String.valueOf(uri), f.getPath(), Integer.parseInt(String.valueOf(startposition)), Integer.parseInt(String.valueOf(endposition)), true, false);
                    }else{
                        showLoadingProgress2(false,4);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (TrimmerActivity.type == 3) {
                if (player.isPlaying()) {
                    player.pause();
                    binding.iconVideoPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_off_video_icon));
                }
                File f=new File(constants.REMOVED_AUDIO_VIDEO_FOLDER_PATH+"AudioRemoved"+System.currentTimeMillis()+".mp4");
                sharedPrefClass.saveOutputPathinShared(f.getPath());
                try {
                    new AudioExtractor().genVideoUsingMuxer(String.valueOf(uri), f.getPath(),Integer.parseInt(String.valueOf(startposition)), Integer.parseInt(String.valueOf(endposition)),false,true);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return "Task Completed";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler4.removeCallbacks(runnable4);
                    if(count>3000) {
                        if(TrimmerActivity.type==1){
                            showLoadingProgress2(false,2);
                        }else if(TrimmerActivity.type==2){
                            showLoadingProgress2(false,1);
                        }else if(TrimmerActivity.type==3){
                            showLoadingProgress2(false,3);
                        }
                    }else{
                        new android.os.Handler().postDelayed(new Runnable() {
                                                                 @Override
                                                                 public void run() {
                                                                     if(TrimmerActivity.type==1){
                                                                         showLoadingProgress2(false,2);
                                                                     }else if(TrimmerActivity.type==2){
                                                                         showLoadingProgress2(false,1);
                                                                     }else if(TrimmerActivity.type==3){
                                                                         showLoadingProgress2(false,3);
                                                                     }
                                                                 }
                                                             }
                                ,3000-count);
                    }
                }
            });


        }
    }
    AlertDialog dialog2;
    AlertDialog.Builder alertDialog;
    View dialogView;
    ViewGroup viewGroup;
    ProgressBar pb;
    Button btnok;
    TextView title;
    TextView msg;
    private boolean isVideoHaveAudioTrack(String path) {
        boolean audioTrack =false;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String hasAudioStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO);
        if(hasAudioStr!=null && hasAudioStr.equals("yes")){
            audioTrack=true; }
        else{
            audioTrack=false; }

        return audioTrack;
    }
    Dialog dialog;
    private void showLoadingProgress2(boolean b,int check) {
        if(b){
            if(check==2){
                dialog = new Dialog(VideoPlayerActivity.this, R.style.Theme_VideoToAudioConverter);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_video_saving);
                dialog.show();

                pb = dialog.findViewById(R.id.progbar);
                btnok = dialog.findViewById(R.id.btndelyes);
                title = dialog.findViewById(R.id.dialogtitle);
                msg = dialog.findViewById(R.id.txtloadinginstr);

                title.setText(getResources().getString(R.string.videosaving));
                msg.setText(getResources().getString(R.string.msgsavingtrimvideo));
            }else if(check==1){
                dialog = new Dialog(VideoPlayerActivity.this, R.style.Theme_VideoToAudioConverter);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_video_saving);
                dialog.show();

                pb = dialog.findViewById(R.id.progbar);
                btnok = dialog.findViewById(R.id.btndelyes);
                title = dialog.findViewById(R.id.dialogtitle);
                msg = dialog.findViewById(R.id.txtloadinginstr);

                title.setText(getResources().getString(R.string.audiosaving));
                msg.setText(getResources().getString(R.string.msgsavingconvaudio));
            }else if(check==3){
                dialog = new Dialog(VideoPlayerActivity.this, R.style.Theme_VideoToAudioConverter);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_video_saving);
                dialog.show();

                pb = dialog.findViewById(R.id.progbar);
                btnok = dialog.findViewById(R.id.btndelyes);
                title = dialog.findViewById(R.id.dialogtitle);
                msg = dialog.findViewById(R.id.txtloadinginstr);

                title.setText(getResources().getString(R.string.videosaving));
                msg.setText(getResources().getString(R.string.removingAudio));
            }

        }else{
            dialog.dismiss();
            if(check==2){
                Dialog dialog = new Dialog(VideoPlayerActivity.this, R.style.Theme_VideoToAudioConverter);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_video_saving);
                dialog.show();

                ProgressBar  pb=dialog.findViewById(R.id.progbar);
                Button btnok=dialog.findViewById(R.id.btnconfirmOk);
                TextView title=dialog.findViewById(R.id.dialogtitle);
                TextView msg=dialog.findViewById(R.id.txtloadinginstr);

                title.setText(getResources().getString(R.string.videosaving));
                msg.setText(getResources().getString(R.string.trimmedvideosaved));

                LottieAnimationView lottieClock=dialog.findViewById(R.id.lottieClock);
                LottieAnimationView lottieComplete=dialog.findViewById(R.id.lottieComplete);
                TextView plzWait=dialog.findViewById(R.id.plzWit);




                pb.setVisibility(View.INVISIBLE);
                btnok.setVisibility(View.VISIBLE);
                lottieClock.setVisibility(View.GONE);
                lottieComplete.setVisibility(View.VISIBLE);
                plzWait.setVisibility(View.INVISIBLE);
                lottieComplete.playAnimation();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        try {
                            setResult(1);
                            finish();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }else if(check==1){
                Dialog dialog = new Dialog(VideoPlayerActivity.this, R.style.Theme_VideoToAudioConverter);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_video_saving);
                dialog.show();

                ProgressBar  pb=dialog.findViewById(R.id.progbar);
                Button btnok=dialog.findViewById(R.id.btnconfirmOk);
                TextView title=dialog.findViewById(R.id.dialogtitle);
                TextView msg=dialog.findViewById(R.id.txtloadinginstr);

                title.setText(getResources().getString(R.string.audiosaving));
                msg.setText(getResources().getString(R.string.convertedaudiosaved));

                LottieAnimationView lottieClock=dialog.findViewById(R.id.lottieClock);
                LottieAnimationView lottieComplete=dialog.findViewById(R.id.lottieComplete);
                TextView plzWait=dialog.findViewById(R.id.plzWit);




                pb.setVisibility(View.INVISIBLE);
                btnok.setVisibility(View.VISIBLE);
                lottieClock.setVisibility(View.GONE);
                lottieComplete.setVisibility(View.VISIBLE);
                plzWait.setVisibility(View.INVISIBLE);
                lottieComplete.playAnimation();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        try {
                            setResult(1);
                            finish();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }else if(check==3){
                Dialog dialog = new Dialog(VideoPlayerActivity.this, R.style.Theme_VideoToAudioConverter);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_video_saving);
                dialog.show();

                ProgressBar  pb=dialog.findViewById(R.id.progbar);
                Button btnok=dialog.findViewById(R.id.btnconfirmOk);
                TextView title=dialog.findViewById(R.id.dialogtitle);
                TextView msg=dialog.findViewById(R.id.txtloadinginstr);

                title.setText(getResources().getString(R.string.videosaving));
                msg.setText(getResources().getString(R.string.removedAudio));

                LottieAnimationView lottieClock=dialog.findViewById(R.id.lottieClock);
                LottieAnimationView lottieComplete=dialog.findViewById(R.id.lottieComplete);
                TextView plzWait=dialog.findViewById(R.id.plzWit);




                pb.setVisibility(View.INVISIBLE);
                btnok.setVisibility(View.VISIBLE);
                lottieClock.setVisibility(View.GONE);
                lottieComplete.setVisibility(View.VISIBLE);
                plzWait.setVisibility(View.INVISIBLE);
                lottieComplete.playAnimation();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        try {
                            setResult(1);
                            finish();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }else if(check==4){
                Dialog dialog = new Dialog(VideoPlayerActivity.this, R.style.Theme_VideoToAudioConverter);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_video_saving);
                dialog.show();

                ProgressBar  pb=dialog.findViewById(R.id.progbar);
                Button btnok=dialog.findViewById(R.id.btnconfirmOk);
                TextView title=dialog.findViewById(R.id.dialogtitle);
                TextView msg=dialog.findViewById(R.id.txtloadinginstr);

                title.setText(getResources().getString(R.string.videosaving));
                msg.setText(getResources().getString(R.string.noAudio));

                LottieAnimationView lottieClock=dialog.findViewById(R.id.lottieClock);
                LottieAnimationView lottieComplete=dialog.findViewById(R.id.lottieFailed);
                TextView plzWait=dialog.findViewById(R.id.plzWit);


                pb.setVisibility(View.INVISIBLE);
                btnok.setVisibility(View.VISIBLE);
                lottieClock.setVisibility(View.GONE);
                lottieComplete.setVisibility(View.VISIBLE);
                plzWait.setVisibility(View.INVISIBLE);
                lottieComplete.playAnimation();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        try {
                            setResult(0);
                            finish();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        }
    }

    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case Thumb.LEFT: {
                startposition = (int) ((player.getDuration() * value) / 100L);
                break;
            }
            case Thumb.RIGHT: {
                endposition = (int) ((player.getDuration() * value) / 100L);
                break;
            }
        }

                binding.textTimeSelection.setText(timeConversion(startposition));
                binding.textTimeSelectionEnd.setText(timeConversion(endposition));

                if ((long)binding.seekbar.getProgress() < startposition) {
                    binding.seekbar.setProgress(Integer.parseInt(String.valueOf(startposition)));
                    binding.textSize.setText(timeConversion(startposition));
                    int width = binding.seekbar.getWidth()
                            - binding.seekbar.getPaddingLeft()
                            - binding.seekbar.getPaddingRight();
                    float thumbPos = width * (binding.seekbar.getProgress() / (float) binding.seekbar.getMax());
                    binding.textSize.setTranslationX(thumbPos);
                    player.seekTo(startposition);
                }
                if ((long)binding.seekbar.getProgress() > endposition) {
                    binding.seekbar.setProgress(Integer.parseInt(String.valueOf(startposition)));
                    binding.textSize.setText(timeConversion(startposition));
                    int width = binding.seekbar.getWidth()
                            - binding.seekbar.getPaddingLeft()
                            - binding.seekbar.getPaddingRight();
                    float thumbPos = width * (binding.seekbar.getProgress() / (float) binding.seekbar.getMax());
                    binding.textSize.setTranslationX(thumbPos);
                    player.seekTo(startposition);
                }
                long dif = endposition - startposition;

                binding.textTime.setText(timeConversion(dif));

                binding.textTimeSelection.setText(timeConversion(startposition));
                binding.textTimeSelectionEnd.setText(timeConversion(endposition));
    }
    public void setOneSecValue(Float oneSecValue) {
        this.oneSecValue = oneSecValue;
    }
    public double getOneSecValue() {
            oneSecValue=(1000f/player.getDuration())*100;
        return oneSecValue;
    }
    AlertDialog.Builder alertDialog3;
    ViewGroup viewGroup3;
    View dialogView3;
    AlertDialog dialog4 = null;
    private void showLoadingProgress(boolean state) {

        if(state) {
             alertDialog3 = new AlertDialog.Builder(VideoPlayerActivity.this);
             viewGroup3 = findViewById(android.R.id.content);
            dialogView3 = LayoutInflater.from(VideoPlayerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup3, false);
            alertDialog3.setView(dialogView3);
             dialog4 = alertDialog3.create();
            dialog4.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog4.setCancelable(false);
            dialog4.show();
            TextView txtMsg=dialogView3.findViewById(R.id.txtloadinginstr);
            txtMsg.setText(getResources().getString(R.string.loadingVideo));

        }else{
            dialog4.dismiss();
        }
    }

    Runnable runnable;
    private final void startservice() {
        handler = new Handler();
         runnable = new Runnable() {
            @Override
            public void run() {
                binding.seekbar.setProgress((int) ((player.getCurrentPosition())));
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public String timeConversion(long value) {
        String videoTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }

    @Override
    public void onBackPressed() {
        if((dialog2!=null&&dialog2.isShowing())||((dialog4!=null&&dialog4.isShowing()))){
            dialog2.dismiss();
            setResult(1);
            finish();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
        player.release();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



//    @SuppressLint("LongLogTag")
//    private void loadFFMpegBinary() {
//        try {
//            if (ffmpeg == null) {
//                Log.d(TAG, "ffmpeg : era nulo");
//                ffmpeg = FFmpeg.getInstance(VideoPlayerActivity.this);
//            }
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//                @Override
//                public void onFailure() {
//                    showUnsupportedExceptionDialog();
//                }
//
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onSuccess() {
//                    if(type==1){
//                        executeCutVideoCommand((int)startposition,(int)endposition,1);
//                    }else if(type==2){
//                        executeConvertVideoCommand((int)startposition,(int)endposition,2);
//                    }else if(type==3){
//                        executeRemoveAudioCommand((int)startposition,(int)endposition,3);
//                    }
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
//        } catch (Exception e) {
//            Log.d(TAG, "EXception no controlada : " + e);
//        }
//    }
//    private void showUnsupportedExceptionDialog() {
//        new AlertDialog.Builder(VideoPlayerActivity.this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Not Supported")
//                .setMessage("Device Not Supported")
//                .setCancelable(false)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .create()
//                .show();
//
//    }
//    @SuppressLint("LongLogTag")
//    private void executeCutVideoCommand(int startMs, int endMs, int type) {
//
//        File dest = new File(TRIMMED_VIDEO_FOLDER_PATH+"VID"+System.currentTimeMillis()+".mp4");
//
//        while (dest.exists()) {
//            dest = new File(TRIMMED_VIDEO_FOLDER_PATH+"VID"+System.currentTimeMillis()+".mp4");
//        }
//
//        filePath = dest.getAbsolutePath();
//        String[] complexCommand = {"-i", String.valueOf(uri), "-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000,"-preset","ultrafast", filePath};
//
//        execFFmpegBinary(complexCommand,type);
//
//    }
//    String filePath;
//    @SuppressLint("LongLogTag")
//    private void executeConvertVideoCommand(int startMs, int endMs, int type) {
//        File dest = new File(CONVERTED_AUDIO_FOLDER_PATH+"AUD"+System.currentTimeMillis()+".mp3");
//        while (dest.exists()) {
//            dest = new File(CONVERTED_AUDIO_FOLDER_PATH+"AUD"+System.currentTimeMillis()+".mp3");
//        }
//        filePath = dest.getAbsolutePath();
//        String[] complexCommand = {"-i", String.valueOf(uri),"-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000, filePath};
//
//        execFFmpegBinary(complexCommand,type);
//
//    }
//    private void executeRemoveAudioCommand(int startMs, int endMs, int type) {
//        File dest = new File(REMOVED_AUDIO_VIDEO_FOLDER_PATH+"VID"+System.currentTimeMillis()+".mp4");
//        while (dest.exists()) {
//            dest = new File(REMOVED_AUDIO_VIDEO_FOLDER_PATH+"VID"+System.currentTimeMillis()+".mp4");
//        }
//        filePath = dest.getAbsolutePath();
//        String[] complexCommand = {"-i ", String.valueOf(uri), "-c","copy","-an " , filePath};
//
//        execFFmpegBinary(complexCommand,type);
//
//    }
//    AlertDialog dialog2;
//    AlertDialog.Builder alertDialog;
//    View dialogView;
//    ViewGroup viewGroup;
//    ProgressBar pb;
//    Button btnok;
//    TextView title;
//    TextView msg;
//    private void execFFmpegBinary(final String[] command,int type) {
//            if(type ==2) {
//                alertDialog = new AlertDialog.Builder(VideoPlayerActivity.this);
//                viewGroup = findViewById(android.R.id.content);
//                dialogView = LayoutInflater.from(VideoPlayerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
//                alertDialog.setView(dialogView);
//                dialog2 = alertDialog.create();
//                dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                dialog2.setCancelable(false);
//                dialog2.show();
//
//                pb = dialogView.findViewById(R.id.progbar);
//                btnok = dialogView.findViewById(R.id.btndelyes);
//                title = dialogView.findViewById(R.id.dialogtitle);
//                msg = dialogView.findViewById(R.id.txtloadinginstr);
//
//                title.setText(getResources().getString(R.string.videosaving));
//                msg.setText(getResources().getString(R.string.msgsavingtrimvideo));
//            }else if(type==1){
//                alertDialog = new AlertDialog.Builder(VideoPlayerActivity.this);
//                viewGroup = findViewById(android.R.id.content);
//                dialogView = LayoutInflater.from(VideoPlayerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
//                alertDialog.setView(dialogView);
//                dialog2 = alertDialog.create();
//                dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                dialog2.setCancelable(false);
//                dialog2.show();
//
//                pb = dialogView.findViewById(R.id.progbar);
//                btnok = dialogView.findViewById(R.id.btndelyes);
//                title = dialogView.findViewById(R.id.dialogtitle);
//                msg = dialogView.findViewById(R.id.txtloadinginstr);
//
//                title.setText(getResources().getString(R.string.audiosaving));
//                msg.setText(getResources().getString(R.string.msgsavingconvaudio));
//            }else if(type==3){
//                alertDialog = new AlertDialog.Builder(VideoPlayerActivity.this);
//                viewGroup = findViewById(android.R.id.content);
//                dialogView = LayoutInflater.from(VideoPlayerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
//                alertDialog.setView(dialogView);
//                dialog2 = alertDialog.create();
//                dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                dialog2.setCancelable(false);
//                dialog2.show();
//
//                pb = dialogView.findViewById(R.id.progbar);
//                btnok = dialogView.findViewById(R.id.btndelyes);
//                title = dialogView.findViewById(R.id.dialogtitle);
//                msg = dialogView.findViewById(R.id.txtloadinginstr);
//
//                title.setText(getResources().getString(R.string.videosaving));
//                msg.setText(getResources().getString(R.string.removingAudio));
//            }
//            FFmpeg.executeAsync(command, new ExecuteCallback() {
//           @Override
//           public void apply(long executionId, int returnCode) {
//               if (returnCode == RETURN_CODE_SUCCESS) {
//                   Log.i(Config.TAG, "Command execution completed successfully.");
//                   dialog2.dismiss();
//                   if (type == 2) {
//                       AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlayerActivity.this);
//                       ViewGroup viewGroup = findViewById(android.R.id.content);
//                       View dialogView = LayoutInflater.from(VideoPlayerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
//                       alertDialog.setView(dialogView);
//                       AlertDialog dialog2 = alertDialog.create();
//                       dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                       dialog2.setCancelable(false);
//                       dialog2.show();
//
//                       ProgressBar pb = dialogView.findViewById(R.id.progbar);
//                       Button btnok = dialogView.findViewById(R.id.btnconfirmOk);
//                       TextView title = dialogView.findViewById(R.id.dialogtitle);
//                       TextView msg = dialogView.findViewById(R.id.txtloadinginstr);
//
//                       title.setText(getResources().getString(R.string.videosaving));
//                       msg.setText(getResources().getString(R.string.trimmedvideosaved));
//
//                       pb.setVisibility(View.INVISIBLE);
//                       btnok.setVisibility(View.VISIBLE);
//
//                       btnok.setOnClickListener(new View.OnClickListener() {
//                           @Override
//                           public void onClick(View v) {
//                               try {
//                                   dialog2.dismiss();
//                                   setResult(1);
//                                   finish();
//
//                               } catch (Exception ex) {
//                                   ex.printStackTrace();
//                               }
//                           }
//                       });
//                   }else if(type==1){

//                   }else if(type==3){
//                       AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlayerActivity.this);
//                       ViewGroup viewGroup = findViewById(android.R.id.content);
//                       View dialogView = LayoutInflater.from(VideoPlayerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
//                       alertDialog.setView(dialogView);
//                       AlertDialog dialog2 = alertDialog.create();
//                       dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                       dialog2.setCancelable(false);
//                       dialog2.show();
//
//                       ProgressBar pb = dialogView.findViewById(R.id.progbar);
//                       Button btnok = dialogView.findViewById(R.id.btnconfirmOk);
//                       TextView title = dialogView.findViewById(R.id.dialogtitle);
//                       TextView msg = dialogView.findViewById(R.id.txtloadinginstr);
//
//                       title.setText(getResources().getString(R.string.videosaving));
//                       msg.setText(getResources().getString(R.string.removedAudio));
//
//                       pb.setVisibility(View.INVISIBLE);
//                       btnok.setVisibility(View.VISIBLE);
//
//                       btnok.setOnClickListener(new View.OnClickListener() {
//                           @Override
//                           public void onClick(View v) {
//                               try {
//                                   dialog2.dismiss();
//                                   setResult(1);
//                                   finish();
//
//                               } catch (Exception ex) {
//                                   ex.printStackTrace();
//                               }
//                           }
//                       });
//                   }
//               } else if (returnCode == RETURN_CODE_CANCEL) {
//                   Log.i(Config.TAG, "Command execution cancelled by user.");
//               } else {
//                   Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", returnCode));
//                   Config.printLastCommandOutput(Log.INFO);
//               }
//           }
//       });
//
//
//    }

}