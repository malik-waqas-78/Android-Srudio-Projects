package com.video.trimmer.activities;




import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.video.trimmer.R;
import com.video.trimmer.databinding.ActivityAddAudioExoBinding;
import com.video.trimmer.utils.AudioExtractor;
import com.video.trimmer.utils.AudioToVideoMuxer;
import com.video.trimmer.utils.CheapSoundFile;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.DataFetcherClass;
import com.video.trimmer.utils.SharedPrefClass;
import com.video.trimmer.utils.TrimVideoUtils;
import com.video.trimmer.utils.Util;
import com.video.trimmer.videoTrimmer.interfaces.OnRangeSeekBarListener;
import com.video.trimmer.videoTrimmer.interfaces.OnTrimVideoListener;
import com.video.trimmer.videoTrimmer.view.RangeSeekBarView;
import com.video.trimmer.videoTrimmer.view.Thumb;
//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.video.timeline.VideoTimeLine;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import com.video.trimmer.utils.TutorialAudioTrackToAacConvertor;
import zeroonezero.android.audio_mixer.AudioMixer;
import zeroonezero.android.audio_mixer.input.AudioInput;
import zeroonezero.android.audio_mixer.input.BlankAudioInput;
import zeroonezero.android.audio_mixer.input.GeneralAudioInput;
import static com.video.trimmer.utils.Constants.FROM_ADD_AUDIO_KEY;
import static com.video.trimmer.utils.Constants.VIDEO_TOTAL_DURATION;

public class AddAudioExo extends AppCompatActivity {
    ActivityAddAudioExoBinding binding;
    Uri uri;
    long duration;
    int type;
    String savePath;
    MediaPlayer playerVideo;
    boolean isLargeVideos=false;
    private AudioManager audioManager = null;
    AlertDialog dialog2;
    AlertDialog.Builder alertDialog;
    View dialogView;
    ViewGroup viewGroup;
    long startPosition,endPosition;
    int startpositionVideo,endPositionvideo;
    ProgressBar pb;
    Button btnok;
    SimpleExoPlayer player;
    TextView title;
    boolean isVolumeVisible=false;
    TextView msg;
    Runnable runnable;
    long trimmedAudioDuraiton;
    ArrayList<File> tmpFiles=new ArrayList<>();
    private Handler handler = new Handler();
    SharedPrefClass sharedPrefClass;
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==1) {
                if (sharedPrefClass.getAudioUrifromShared() != null) {
                    playAudio(sharedPrefClass.getAudioUrifromShared());
                    binding.btnMerge.setEnabled(true);
                    binding.btnMerge.setVisibility(View.VISIBLE);
                    if(player.isPlaying()){
                        player.pause();
                        binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }
                }
            }
        }
    });
    private boolean firstTime=true;
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding=ActivityAddAudioExoBinding.inflate(getLayoutInflater());
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(binding.getRoot());
        constants=new Constants(this);
        startserviceforSystemVolume();
        binding.toolbar.setTitle("");
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_icon));
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initComponents();
        binding.videoView.setPlayer(player);
        player.setPlayWhenReady(true);
        MediaItem mediaItem = MediaItem.fromUri(uri);

        player.setMediaItem(mediaItem);

        player.prepare();
        player.addListener(new Player.Listener() {
            @Override
            public void onSeekForwardIncrementChanged(long seekForwardIncrementMs) {
                binding.pbarAudio4.setProgress((int) seekForwardIncrementMs);
            }
        });
        player.addListener(new Player.Listener() {
            @Override
            public void onSeekBackIncrementChanged(long seekBackIncrementMs) {
                binding.pbarAudio4.setProgress((int) seekBackIncrementMs);
            }
        });

        PlayerControlView.ProgressUpdateListener progressUpdateListener=new PlayerControlView.ProgressUpdateListener() {
            @Override
            public void onProgressUpdate(long position, long bufferedPosition) {
                binding.pbarAudio4.setProgress((int) position);
            }
        };
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    if(firstTime) {
                        VideoTimeLine.with(String.valueOf(uri)).show(binding.fixedThumbList);
                        showLoadingProgress(false);
                        firstTime=false;

                        binding.pbarAudio4.setMax(Integer.parseInt(String.valueOf(player.getDuration())));
                        startserviceforVideo();
                        startpositionVideo = 0;
                        endPositionvideo = Integer.parseInt(String.valueOf(player.getDuration()));

                        binding.videoStartTime.setText(timeConversion(startpositionVideo));
                        binding.videoEndTime.setText(timeConversion(endPositionvideo));

                        binding.rangeseekbar4.initMaxWidth();
                    }
                }
                else {
                    binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    if(firstTime) {
                        VideoTimeLine.with(String.valueOf(uri)).show(binding.fixedThumbList);
                        showLoadingProgress(false);
                        firstTime=false;

                        binding.pbarAudio4.setMax(Integer.parseInt(String.valueOf(player.getDuration())));
                        startserviceforVideo();
                        startpositionVideo = 0;
                        endPositionvideo = Integer.parseInt(String.valueOf(player.getDuration()));

                        binding.videoStartTime.setText(timeConversion(startpositionVideo));
                        binding.videoEndTime.setText(timeConversion(endPositionvideo));

                        binding.rangeseekbar4.initMaxWidth();
                    }
                }
            }
        });
        binding.systemVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int i=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    int i2=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int i3=progress;
                    Log.d("ncan",i+" "+i2+i3);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.lineartxtvolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.arrowupdown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
                binding.relativeVolumesVisible.startAnimation(animSlideUp);
                binding.relativeVolumesVisible.setVisibility(View.GONE);
                binding.relbackblack.setVisibility(View.GONE);
                isVolumeVisible=false;
            }
        });
        binding.lineartxtvolumehidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.relativeVolumesVisible.setVisibility(View.VISIBLE);
                binding.relbackblack.setVisibility(View.VISIBLE);
                binding.arrowupdown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                binding.relativeVolumesVisible.startAnimation(animSlideUp);
                isVolumeVisible=true;

            }
        });
        binding.handlerTop.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if(progress>startpositionVideo&&progress<endPositionvideo) {
                        player.seekTo(progress);
                        binding.videoDuration.setText(timeConversion(progress).substring(1,timeConversion(progress).length())+"/"+timeConversion(player.getDuration()).substring(1,timeConversion(player.getDuration()).length()));
                        binding.videoPlayingTime.setText(timeConversion(progress));

                        int width = binding.handlerTop.getWidth()
                                - binding.handlerTop.getPaddingLeft()
                                - binding.handlerTop.getPaddingRight();
                        float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
                        binding.videoPlayingTime.setTranslationX(thumbPos);
                    }else{
                        player.seekTo(startpositionVideo);
                        binding.videoDuration.setText(timeConversion(startpositionVideo).substring(1,timeConversion(startpositionVideo).length())+"/"+timeConversion(player.getDuration()).substring(1,timeConversion(player.getDuration()).length()));
                        binding.videoPlayingTime.setText(timeConversion(startpositionVideo));
                        int width = binding.handlerTop.getWidth()
                                - binding.handlerTop.getPaddingLeft()
                                - binding.handlerTop.getPaddingRight();
                        float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
                        binding.videoPlayingTime.setTranslationX(thumbPos);
                    }
                }else{
                    if(progress==endPositionvideo){
                        player.pause();
                        binding.videoPlayingTime.setText(timeConversion(progress));
                        binding.handlerTop.setProgress(startpositionVideo);
                        player.seekTo(startpositionVideo);
                        binding.videoDuration.setText(timeConversion(progress).substring(1,timeConversion(progress).length())+"/"+timeConversion(player.getDuration()).substring(1,timeConversion(player.getDuration()).length()));

                    }else {
                        binding.videoPlayingTime.setText(timeConversion(progress));
                        binding.videoDuration.setText(timeConversion(progress).substring(1,timeConversion(progress).length())+"/"+timeConversion(player.getDuration()).substring(1,timeConversion(player.getDuration()).length()));

                        int width = binding.handlerTop.getWidth()
                                - binding.handlerTop.getPaddingLeft()
                                - binding.handlerTop.getPaddingRight();
                        float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
                        binding.videoPlayingTime.setTranslationX(thumbPos);
                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.volumeSeekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    float log1 = (float) (Math.log(100-progress)/Math.log(100));
                    playerVideo.setVolume(1-log1, 1-log1);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.fabSelectAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefClass.getAudioUrifromShared()!=null){
                    if(handler2!=null) {
                        handler2.removeCallbacks(runnable2);
                        handler2.postDelayed(runnable2, 1);
                    }
                    mp.stop();
                    mp.release();

                }
                Intent intent=new Intent(AddAudioExo.this,AudioActivity.class);
                intent.putExtra(FROM_ADD_AUDIO_KEY,true);
                mGetContent.launch(intent);
            }
        });
        binding.volumeSeekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float log1 = (float) (Math.log(100-progress)/Math.log(100));
                mp.setVolume(1-log1, 1-log1);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {



            }
        });

        binding.timeLineBar.addOnRangeSeekBarListener(binding.timeVideoView);
        binding.timeLineBar.addOnRangeSeekBarListener(new OnRangeSeekBarListener() {
            @Override
            public void onCreate(RangeSeekBarView rangeSeekBarView, int index, float value) {

            }

            @Override
            public void onSeek(RangeSeekBarView rangeSeekBarView, int index, float value) {
                onSeekThumbs(index, value);


            }

            @Override
            public void onSeekStart(RangeSeekBarView rangeSeekBarView, int index, float value) {

            }

            @Override
            public void onSeekStop(RangeSeekBarView rangeSeekBarView, int index, float value) {

            }
        });
        binding.rangeseekbar4.addOnRangeSeekBarListener(binding.progrssbarView4);
        binding.rangeseekbar4.addOnRangeSeekBarListener(new OnRangeSeekBarListener() {
            @Override
            public void onCreate(RangeSeekBarView rangeSeekBarView, int index, float value) {

            }

            @Override
            public void onSeek(RangeSeekBarView rangeSeekBarView, int index, float value) {
                onSeekThumbsAudio(index,value);
            }

            @Override
            public void onSeekStart(RangeSeekBarView rangeSeekBarView, int index, float value) {

            }

            @Override
            public void onSeekStop(RangeSeekBarView rangeSeekBarView, int index, float value) {

            }
        });
        binding.pbarAudio4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    mp.seekTo(progress);
                    binding.audioPlayingTime.setText(timeConversion(progress));
                    int width = binding.pbarAudio4.getWidth()
                            - binding.pbarAudio4.getPaddingLeft()
                            - binding.pbarAudio4.getPaddingRight();
                    float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                    binding.audioPlayingTime.setTranslationX(thumbPos);
                    if(progress>=endPosition ||progress<startPosition){
                        mp.seekTo(Integer.parseInt(String.valueOf(startPosition)));

                        binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        mp.pause();
                        player.pause();
                    }
                } else {
                    binding.audioPlayingTime.setText(timeConversion(progress));
                    int width = binding.pbarAudio4.getWidth()
                            - binding.pbarAudio4.getPaddingLeft()
                            - binding.pbarAudio4.getPaddingRight();
                    float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                    binding.audioPlayingTime.setTranslationX(thumbPos);
                    if(mp.getCurrentPosition()==endPosition){
                        binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        mp.pause();
                        player.pause();
                    }
                }
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.seekTo(Integer.parseInt(String.valueOf(startPosition)));
                        binding.audioPlayingTime.setText(timeConversion(progress));
                        int width = binding.pbarAudio4.getWidth()
                                - binding.pbarAudio4.getPaddingLeft()
                                - binding.pbarAudio4.getPaddingRight();
                        float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                        binding.audioPlayingTime.setTranslationX(thumbPos);
                        binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        player.pause();
                        mp.pause();
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        binding.btnMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerVideo!=null && mp!=null){
                    if(playerVideo.isPlaying()){
                        playerVideo.pause();
                    }
                    if(mp.isPlaying()){
                        mp.pause();
                    }
                    sharedPrefClass.saveAudioEndPosinShared(endPosition);
                    sharedPrefClass.saveAudioStartPosinShared(startPosition);
                    sharedPrefClass.saveVideoStartPosinShared(startpositionVideo);
                    sharedPrefClass.saveVideoEndPosinShared(endPositionvideo);
                    sharedPrefClass.saveVideoVolumeinShared(binding.volumeSeekbarVideo.getProgress());
                    sharedPrefClass.saveAudioVolumeinShared(binding.volumeSeekbarAudio.getProgress());

                    //executeVolumeChangeCommandofAudio();
//                    executeTrimmVideoCommand();
                    new LoadData().execute();
                }else if(playerVideo==null) {
                    Toast.makeText(AddAudioExo.this, "Kindly add video", Toast.LENGTH_SHORT).show();
                }else if(mp==null){
                    Toast.makeText(AddAudioExo.this, "Kindly add Audio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(sharedPrefClass.getAudioUrifromShared()==null){
            binding.btnPlayAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playerVideo.isPlaying()) {
                        playerVideo.pause();
                        binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    } else {
                        player.play();
                        binding.imageplaypause.setImageResource(R.drawable.ic_baseline_pause_24);
                    }

                }
            });
        }

    }
    AlertDialog.Builder alertDialog3;
    ViewGroup viewGroup3;
    View dialogView3;
    AlertDialog dialog4 = null;
    private void showLoadingProgress(boolean state) {

        if(state) {
            alertDialog3 = new AlertDialog.Builder(AddAudioExo.this);
            viewGroup3 = findViewById(android.R.id.content);
            dialogView3 = LayoutInflater.from(AddAudioExo.this).inflate(R.layout.dialog_video_saving, viewGroup3, false);
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
    private void onSeekThumbsAudio(int index, float value) {
        switch (index) {
            case Thumb.LEFT: {
                startPosition = (int) ((binding.pbarAudio4.getMax() * value) / 100L);
                Log.d("startandEndpostion",startPosition+" start");
                break;
            }
            case Thumb.RIGHT: {
                endPosition = (int) ((binding.pbarAudio4.getMax() * value) / 100L);
                Log.d("startandEndpostion",endPosition+" end");
                break;
            }
        }

        long diff=endPosition-startPosition;
        binding.audioRemainingdurationtxt4.setText(timeConversion(diff));

        if ((long)mp.getCurrentPosition() < startPosition) {
            mp.seekTo(Integer.parseInt(String.valueOf(startPosition)));
            binding.audioPlayingTime.setText(timeConversion(startPosition));
            int width = binding.pbarAudio4.getWidth()
                    - binding.pbarAudio4.getPaddingLeft()
                    - binding.pbarAudio4.getPaddingRight();
            float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
            binding.audioPlayingTime.setTranslationX(thumbPos);
            binding.pbarAudio4.setProgress(Integer.parseInt(String.valueOf(startPosition)));

        }
        if ((long)mp.getCurrentPosition() > endPosition) {
            mp.seekTo(Integer.parseInt(String.valueOf(startPosition)));
            binding.audioPlayingTime.setText(timeConversion(startPosition));
            int width = binding.pbarAudio4.getWidth()
                    - binding.pbarAudio4.getPaddingLeft()
                    - binding.pbarAudio4.getPaddingRight();
            float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
            binding.audioPlayingTime.setTranslationX(thumbPos);
            binding.pbarAudio4.setProgress(Integer.parseInt(String.valueOf(startPosition)));
        }
        binding.audioStartTime.setText(timeConversion(startPosition));
        binding.audioEndTime.setText(timeConversion(endPosition));
    }

    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case Thumb.LEFT: {
                startpositionVideo = (int) ((player.getDuration() * value) / 100L);
                Log.d("startandEndpostion",startpositionVideo+" start");
                break;
            }
            case Thumb.RIGHT: {
                endPositionvideo = (int) ((player.getDuration() * value) / 100L);
                Log.d("startandEndpostion",endPositionvideo+" end");
                break;
            }
        }

        if ((long)player.getCurrentPosition() < startpositionVideo) {
            player.seekTo(startpositionVideo);
            binding.videoPlayingTime.setText(timeConversion(startpositionVideo));
            int width = binding.handlerTop.getWidth()
                    - binding.handlerTop.getPaddingLeft()
                    - binding.handlerTop.getPaddingRight();
            float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
            binding.videoPlayingTime.setTranslationX(thumbPos);
            binding.videoDuration.setText(timeConversion(startpositionVideo).substring(1,timeConversion(startpositionVideo).length())+"/"+timeConversion(player.getDuration()).substring(1,timeConversion(player.getDuration()).length()));

        }
        if ((long)player.getCurrentPosition() > endPositionvideo) {
            player.seekTo(startpositionVideo);
            binding.videoPlayingTime.setText(timeConversion(startpositionVideo));
            int width = binding.handlerTop.getWidth()
                    - binding.handlerTop.getPaddingLeft()
                    - binding.handlerTop.getPaddingRight();
            float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
            binding.videoPlayingTime.setTranslationX(thumbPos);
            binding.videoDuration.setText(timeConversion(startpositionVideo).substring(1,timeConversion(startpositionVideo).length())+"/"+timeConversion(player.getDuration()).substring(1,timeConversion(player.getDuration()).length()));
        }
        binding.videoStartTime.setText(timeConversion(startpositionVideo));
        binding.videoEndTime.setText(timeConversion(endPositionvideo));
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
    private void initComponents() {
        File dest = new File(constants.TEMP_FOLDER_PATH);
        if(!dest.exists()){
            dest.mkdir();
        }
        File f2=new File(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH);
        if(!f2.exists()){
            f2.mkdir();
        }
        sharedPrefClass=new SharedPrefClass(AddAudioExo.this);
        uri= Uri.parse(TrimmerActivity.VIDEOPATH);
        duration=getIntent().getIntExtra(VIDEO_TOTAL_DURATION,0);
        binding.btnMerge.setEnabled(false);
        type=TrimmerActivity.type;
        savePath=TrimmerActivity.TRIMMED_VIDEO_FOLDER_PATH;
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        binding.toolbarTitle.setText(uri.toString().substring(uri.toString().lastIndexOf("/")+1,uri.toString().length()));
        binding.toolbarTitle.setSelected(true);
        player = new SimpleExoPlayer.Builder(AddAudioExo.this).build();


        binding.systemVolume.setVisibility(View.VISIBLE);
        binding.volumeSeekbarAudio.setProgress(85);
        binding.volumeSeekbarVideo.setProgress(85);
        mp=new MediaPlayer();

        binding.systemVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        binding.systemVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        sharedPrefClass.saveSummedVideoinShared(null);
        sharedPrefClass.saveTrimmedVideoPahtinShared(null);
        sharedPrefClass.saveTrimmedAudioPathinShared(null);
        sharedPrefClass.saveChangedVolumeAudioPathinShared(null);
        sharedPrefClass.saveChangedVolumeVideoPathinShared(null);
        sharedPrefClass.saveAudioUriinShared(null);
        sharedPrefClass.saveVideoDurationinShared(0);
        sharedPrefClass.saveAudioDurationinShared(0);
        sharedPrefClass.saveVideoVolumeinShared(85);
        sharedPrefClass.saveAudioVolumeinShared(85);



    }


    public byte[] convert(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];

        for (int readNum; (readNum = fis.read(b)) != -1;) {
            bos.write(b, 0, readNum);
        }

        byte[] bytes = bos.toByteArray();

        return bytes;
    }
    MediaPlayer mp;
    private void playAudio(String url) {
        mp=null;
        mp=new MediaPlayer();
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        mp.setAudioAttributes(audioAttributes);
        try {
            mp.setDataSource(url);
        } catch (IllegalArgumentException e ) {

            e.printStackTrace();
        } catch (IllegalStateException e ) {

            e.printStackTrace();
        } catch (IOException e ) {

            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IllegalStateException e ) {

            e.printStackTrace();
        } catch (IOException e ) {

            e.printStackTrace();
        }

        try {
            binding.waveForm.setRawData(convert(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.pbarAudio4.setMax(mp.getDuration());
        Toast.makeText(this, ""+binding.pbarAudio4.getMax(), Toast.LENGTH_SHORT).show();
        binding.rangeseekbar4.setVisibility(View.VISIBLE);

        float log1 = (float) (Math.log(100-85)/Math.log(100));
        mp.setVolume(1-log1, 1-log1);

        binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        startPosition=0;
        endPosition= binding.pbarAudio4.getMax();
        binding.rangeseekbar4.initMaxWidth();
        long diff=endPosition-startPosition;
        binding.audioRemainingdurationtxt4.setText(timeConversion(diff));
        binding.audioStartTime.setText(timeConversion(startPosition));
        binding.audioEndTime.setText(timeConversion(endPosition));
        binding.btnPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerVideo.isPlaying()) {
                    playerVideo.pause();
                } else {
                    player.play();
                }

                if(mp.isPlaying()){
                    binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    mp.pause();
                }else {
                    binding.imageplaypause.setImageResource(R.drawable.ic_baseline_pause_24);

                    startservice2();


                    mp.start();
                }
            }
        });

    }


    Handler handler2,handler3;
    Runnable runnable2,runnable3;
    private final void startservice2() {
        handler2 = new Handler();
        runnable2 = new Runnable() {
            @Override
            public void run() {
                if(mp.isPlaying()) {
                    binding.pbarAudio4.setProgress(mp.getCurrentPosition());
                    binding.audioPlayingTime.setText(timeConversion(mp.getCurrentPosition()));
                    int width = binding.pbarAudio4.getWidth()
                            - binding.pbarAudio4.getPaddingLeft()
                            - binding.pbarAudio4.getPaddingRight();
                    float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                    binding.audioPlayingTime.setTranslationX(thumbPos);
                }
                handler2.postDelayed(runnable2, 1);
            }
        };
        handler2.postDelayed(runnable2, 0);

    }
    private final void startserviceforVideo() {
        handler3 = new Handler();
        runnable3 = new Runnable() {
            @Override
            public void run() {
                if(player.isPlaying()) {
                    binding.handlerTop.setProgress(Integer.parseInt(String.valueOf(player.getCurrentPosition())));
                    binding.videoPlayingTime.setText(timeConversion(player.getCurrentPosition()));
                    int width = binding.handlerTop.getWidth()
                            - binding.handlerTop.getPaddingLeft()
                            - binding.handlerTop.getPaddingRight();
                    float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
                    binding.videoPlayingTime.setTranslationX(thumbPos);
                }
                handler3.postDelayed(runnable3, 1);
            }
        };
        handler3.postDelayed(runnable3, 0);

    }
    private final void startserviceforSystemVolume() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                binding.systemVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                handler.postDelayed(runnable, 1);
            }
        };
        handler.postDelayed(runnable, 0);

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
                    showLoadingProgress2(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                trimandCreateVideo();
            } catch (IOException e) {
                e.printStackTrace();
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
                        showLoadingProgress2(false);
                    }else{
                        new android.os.Handler().postDelayed(new Runnable() {
                                                                 @Override
                                                                 public void run() {
                                                                     showLoadingProgress2(false);
                                                                 }
                                                             }
                                ,3000-count);
                    }
                }
            });

        }
    }

    Dialog dialog;
    private void showLoadingProgress2(boolean b) {
        if(b){
            dialog = new Dialog(AddAudioExo.this, R.style.Theme_VideoToAudioConverter);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_video_saving);
            dialog.show();

            pb = dialog.findViewById(R.id.progbar);
            btnok = dialog.findViewById(R.id.btndelyes);
            title = dialog.findViewById(R.id.dialogtitle);
            msg = dialog.findViewById(R.id.txtloadinginstr);

            title.setText(getResources().getString(R.string.trimingaudio));
            msg.setText(getResources().getString(R.string.mixingAudio));
        }else{
            dialog.dismiss();
            Dialog dialog = new Dialog(AddAudioExo.this, R.style.Theme_VideoToAudioConverter);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_video_saving);
            dialog.show();

            ProgressBar  pb=dialog.findViewById(R.id.progbar);
            Button btnok=dialog.findViewById(R.id.btnconfirmOk);
            TextView title=dialog.findViewById(R.id.dialogtitle);
            TextView msg=dialog.findViewById(R.id.txtloadinginstr);

            title.setText(getResources().getString(R.string.videosaving));
            msg.setText(getResources().getString(R.string.mixedVideoSaved));

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
                        tmpFiles=new DataFetcherClass(constants.TEMP_FOLDER_PATH).get_Recovery_Audios();
                        if(tmpFiles.size()!=0) {
                            for (File f : tmpFiles) {
                                f.delete();
                            }
                        }
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
                    dialog2.dismiss();
                }
            });
        }
    }
    private File getVideosAudioFile() {
        File f=new File(constants.TEMP_FOLDER_PATH+"ConvertedVideo"+System.currentTimeMillis()+".mp3");

        try {
            new com.video.trimmer.videoTrimmer.utils.AudioExtractor().genVideoUsingMuxer(sharedPrefClass.getTempCreatedVideofromShared(), f.getPath(), -1, -1, true, false);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
    private void trimandCreateVideo() throws IOException {

        trimVideoFile();
        File videosAudioFile=null;
        if(isVideoHaveAudioTrack(sharedPrefClass.getTempCreatedVideofromShared())) {
            videosAudioFile = getVideosAudioFile();
        }
        trimmedVideoDuration=endPositionvideo-startpositionVideo;
        File audioFile=trimAudioFile();

        if(isLargeVideos){
            File newAACFile=new File(constants.TEMP_FOLDER_PATH+"AACTEMP"+System.currentTimeMillis()+".aac");
            new TutorialAudioTrackToAacConvertor().convert(audioFile.getPath(),newAACFile.getPath());

            changeVolumeofAudio(newAACFile, videosAudioFile);

        }else {
            changeVolumeofAudio(audioFile, videosAudioFile);
        }

//        changeVolumeofAudio(audioFile, videosAudioFile);
    }
    private void trimVideoFile() {

        try {
            TrimVideoUtils.startTrim(AddAudioExo.this, new File(String.valueOf(uri)), constants.TEMP_FOLDER_PATH, startpositionVideo, endPositionvideo, new OnTrimVideoListener() {
                @Override
                public void onTrimStarted(int type) {

                }

                @Override
                public void getResult(Uri uri, int check) {
                    sharedPrefClass.saveTempCreatedVideoinShared(String.valueOf(uri));
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

    }
    ArrayList<String> audiosPaths=new ArrayList<String>();
    private File trimAudioFile() throws IOException {
        return convertAudiotoAAC();
    }
    private File convertAudiotoAAC() throws IOException {
        File infile=new File(sharedPrefClass.getAudioUrifromShared());
        File outFile2=new File(constants.TEMP_FOLDER_PATH+"Trimmed"+System.currentTimeMillis()+".mp3");
        trimmedAudioDuraiton=endPosition-startPosition;
        if((sharedPrefClass.getAudioSizefromShared()/1024)/1000<=1.0){
            File outFile=new File(constants.TEMP_FOLDER_PATH+"TrimmedAuido"+System.currentTimeMillis()+".aac");

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
            bos.write(convert(infile.getPath()));
            bos.flush();
            bos.close();



            if(trimmedAudioDuraiton<trimmedVideoDuration){
                try {
                    new AudioExtractor().genVideoUsingMuxer(infile.getPath(),outFile2.getPath(),Integer.parseInt(String.valueOf(startPosition)),Integer.parseInt(String.valueOf(endPosition)),true,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                endPosition=startPosition+(endPositionvideo-startpositionVideo);
                try {
                    new AudioExtractor().genVideoUsingMuxer(infile.getPath(),outFile2.getPath(),Integer.parseInt(String.valueOf(startPosition)),Integer.parseInt(String.valueOf(endPosition)),true,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else{
            isLargeVideos=true;
            final CheapSoundFile.ProgressListener listener = new CheapSoundFile.ProgressListener() {
                public boolean reportProgress(double frac) {
                    Log.d("progresstrimmingAuduio",frac+"");
                    return true;
                }
            };
            if(trimmedAudioDuraiton<=trimmedVideoDuration){
                CheapSoundFile cheapSoundFile= CheapSoundFile.create(infile.getPath(),listener);
                int mSampleRate = cheapSoundFile.getSampleRate();

                int mSamplesPerFrame = cheapSoundFile.getSamplesPerFrame();

                int startFrame = Util.secondsToFrames(startPosition/1000,mSampleRate, mSamplesPerFrame);

                int endFrame = Util.secondsToFrames(endPosition/1000, mSampleRate,mSamplesPerFrame);

                cheapSoundFile.WriteFile(outFile2, startFrame, endFrame-startFrame);
            }else{
                endPosition=startPosition+(endPositionvideo-startpositionVideo);
                CheapSoundFile cheapSoundFile= CheapSoundFile.create(infile.getPath(),listener);
                int mSampleRate = cheapSoundFile.getSampleRate();

                int mSamplesPerFrame = cheapSoundFile.getSamplesPerFrame();

                int startFrame = Util.secondsToFrames(startPosition/1000,mSampleRate, mSamplesPerFrame);

                int endFrame = Util.secondsToFrames(endPosition/1000, mSampleRate,mSamplesPerFrame);

                cheapSoundFile.WriteFile(outFile2, startFrame, endFrame-startFrame);
            }
        }



//        new TutorialAudioTrackToAacConvertor().convert(infile.getPath(),outFile.getPath());
        return outFile2;
    }
    private void changeVolumeofAudio(File audioFile1,File audioFile2) throws IOException {

        File outputfileAudio2=new File(constants.TEMP_FOLDER_PATH+"TempAudioChangedandmixed"+System.currentTimeMillis()+".mp3");
        AudioInput input1 = new GeneralAudioInput(audioFile1.getPath());
        float volume1=binding.volumeSeekbarAudio.getProgress()/1000f;
        input1.setVolume(volume1);
        AudioInput input2 ;
        if(audioFile2!=null) {
            input2 = new GeneralAudioInput(AddAudioExo.this, Uri.parse(audioFile2.getPath()), null);
            float volume2 = binding.volumeSeekbarVideo.getProgress() / 1000f;
            input2.setVolume(volume2);
        }else{
            input2=new BlankAudioInput(trimmedVideoDuration);
        }
        final AudioMixer audioMixer = new AudioMixer(outputfileAudio2.getPath());
        audioMixer.addDataSource(input1);
        audioMixer.addDataSource(input2);
        audioMixer.setSampleRate(44100);
        audioMixer.setBitRate(128000);
        audioMixer.setChannelCount(2);
        audioMixer.setLoopingEnabled(true);
        audioMixer.setMixingType(AudioMixer.MixingType.PARALLEL); // or AudioMixer.MixingType.SEQUENTIAL

        audioMixer.setProcessingListener(new AudioMixer.ProcessingListener() {
            @Override
            public void onProgress(final double progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(AddAudioActivity.this, "Started!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onEnd() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        File videoFile=new File(sharedPrefClass.getTempCreatedVideofromShared());
                        File outputMixedFile=new File(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH+"MixedAudio"+System.currentTimeMillis()+".mp4");
                        sharedPrefClass.saveOutputPathinShared(outputMixedFile.getPath());
                        if(sharedPrefClass.getTempCreatedVideofromShared()!=null) {
                            new AudioToVideoMuxer().mux(outputfileAudio2.getPath(), videoFile.getAbsolutePath(), outputMixedFile.getPath());
//                           dialog2.dismiss();

                        }
                        audioMixer.release();
                    }
                });
            }
        });
        audioMixer.start();
        audioMixer.processAsync();

    }
    long trimmedVideoDuration,trimmedAudioDuration;




    @Override
    public void onBackPressed() {
        if(isVolumeVisible){
            binding.arrowupdown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
            binding.relativeVolumesVisible.startAnimation(animSlideUp);
            binding.relativeVolumesVisible.setVisibility(View.GONE);
            binding.relbackblack.setVisibility(View.GONE);
            isVolumeVisible=false;
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp!=null){
            mp.release();
        }if(playerVideo!=null){
            playerVideo.release();
        }
    }
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
}