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
import com.video.trimmer.databinding.ActivityAddAudioToVideoBinding;
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
//import com.mohammedalaa.seekbar.DoubleValueSeekBarView;
//import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener;
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

public class AddAudioActivity extends AppCompatActivity {

    ActivityAddAudioToVideoBinding binding;
    Uri uri;
    long duration;
    int type;
    String savePath;
    MediaPlayer playerVideo;

    int handlerDuration=3000;
    boolean handlerCompleted=false;

    private AudioManager audioManager = null;
    AlertDialog dialog2;
    AlertDialog.Builder alertDialog;
    View dialogView;
    ViewGroup viewGroup;
    long startPosition,endPosition;
    int startpositionVideo,endPositionvideo;
    ProgressBar pb;
    Button btnok;
    TextView title;
    boolean islargeVideo=false;
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
                    if(binding.videoShow.isPlaying()){
                        binding.videoShow.pause();
                        binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }
                }
            }
        }
    });
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding=ActivityAddAudioToVideoBinding.inflate(getLayoutInflater());
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
        binding.videoShow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                binding.videoShow.seekTo(0);
                binding.videoShow.start();
                binding.videoDuration.setText(timeConversion(binding.videoShow.getCurrentPosition())+"/"+timeConversion(binding.videoShow.getDuration()));
            }
        });

        binding.videoShow.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(playerVideo==null) {
                    VideoTimeLine.with(String.valueOf(uri)).show(binding.fixedThumbList);
                    playerVideo = mp;
                    float log1 = (float) (Math.log(100 - 85) / Math.log(100));
                    playerVideo.setVolume(1 - log1, 1 - log1);
                    binding.volumeSeekbarVideo.setProgress(85);

                    binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    startpositionVideo = 0;
                    endPositionvideo = binding.videoShow.getDuration();
                    binding.handlerTop.setMax(binding.videoShow.getDuration());
                    binding.timeLineBar.initMaxWidth();

                    binding.videoDuration.setText(timeConversion(binding.videoShow.getCurrentPosition()).substring(1,timeConversion(binding.videoShow.getCurrentPosition()).length())+"/"+timeConversion(binding.videoShow.getDuration()).substring(1,timeConversion(binding.videoShow.getDuration()).length()));

                    binding.videoStartTime.setText(timeConversion(startpositionVideo));
                    binding.videoEndTime.setText(timeConversion(endPositionvideo));
                    startserviceforVideo();
                }else {
                    playerVideo=mp;
                    binding.videoShow.seekTo(startpositionVideo);
                    binding.videoDuration.setText(timeConversion(binding.videoShow.getCurrentPosition()).substring(1,timeConversion(binding.videoShow.getCurrentPosition()).length())+"/"+timeConversion(binding.videoShow.getDuration()).substring(1,timeConversion(binding.videoShow.getDuration()).length()));

                    //binding.videoView.start();
                }

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
                        binding.videoShow.seekTo(progress);
                        binding.videoDuration.setText(timeConversion(progress).substring(1,timeConversion(progress).length())+"/"+timeConversion(binding.videoShow.getDuration()).substring(1,timeConversion(binding.videoShow.getDuration()).length()));
                        binding.videoPlayingTime.setText(timeConversion(progress));

                        int width = binding.handlerTop.getWidth()
                                - binding.handlerTop.getPaddingLeft()
                                - binding.handlerTop.getPaddingRight();
                        float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
                        binding.videoPlayingTime.setTranslationX(thumbPos);
                    }else{
                        binding.videoShow.seekTo(startpositionVideo);
                        binding.videoDuration.setText(timeConversion(startpositionVideo).substring(1,timeConversion(startpositionVideo).length())+"/"+timeConversion(binding.videoShow.getDuration()).substring(1,timeConversion(binding.videoShow.getDuration()).length()));
                        binding.videoPlayingTime.setText(timeConversion(startpositionVideo));
                        int width = binding.handlerTop.getWidth()
                                - binding.handlerTop.getPaddingLeft()
                                - binding.handlerTop.getPaddingRight();
                        float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
                        binding.videoPlayingTime.setTranslationX(thumbPos);
                    }
                }else{
                    if(progress==endPositionvideo){
                        binding.videoShow.pause();
                        binding.videoPlayingTime.setText(timeConversion(progress));
                        binding.handlerTop.setProgress(startpositionVideo);
                        binding.videoShow.seekTo(startpositionVideo);
                        binding.videoDuration.setText(timeConversion(progress).substring(1,timeConversion(progress).length())+"/"+timeConversion(binding.videoShow.getDuration()).substring(1,timeConversion(binding.videoShow.getDuration()).length()));

                    }else {
                        binding.videoPlayingTime.setText(timeConversion(progress));
                        binding.videoDuration.setText(timeConversion(progress).substring(1,timeConversion(progress).length())+"/"+timeConversion(binding.videoShow.getDuration()).substring(1,timeConversion(binding.videoShow.getDuration()).length()));

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
                    //mp.stop();
                    mp.release();

                }
                Intent intent=new Intent(AddAudioActivity.this,AudioActivity.class);
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
        binding.btnMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerVideo!=null && mp!=null){
                    if(playerVideo.isPlaying()){
                        playerVideo.pause();
                    }
                    if(mp.isPlaying()){
                        mp.stop();
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
                    Toast.makeText(AddAudioActivity.this, "Kindly add video", Toast.LENGTH_SHORT).show();
                }else if(mp==null){
                    Toast.makeText(AddAudioActivity.this, "Kindly add Audio", Toast.LENGTH_SHORT).show();
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
                        binding.videoShow.start();
                        binding.imageplaypause.setImageResource(R.drawable.ic_baseline_pause_24);
                    }

                }
            });
        }


    }

    private void trimandCreateVideo() throws IOException {

        trimVideoFile();
        File videosAudioFile=null;
        if(isVideoHaveAudioTrack(sharedPrefClass.getTempCreatedVideofromShared())) {
             videosAudioFile = getVideosAudioFile();
        }
        trimmedVideoDuration=endPositionvideo-startpositionVideo;
        File audioFile=trimAudioFile();

        if(islargeVideo){

            File newAACFile=new File(constants.TEMP_FOLDER_PATH+"AACTEMP"+System.currentTimeMillis()+".aac");
            new TutorialAudioTrackToAacConvertor().convert(audioFile.getPath(),newAACFile.getPath());

            changeVolumeofAudio(newAACFile, videosAudioFile);

        }else {
            changeVolumeofAudio(audioFile, videosAudioFile);
        }


    }
    int count=0;
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
                    showLoadingProgress(true);
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
                        showLoadingProgress(false);
                    }else {
                        new android.os.Handler().postDelayed(new Runnable() {
                                                                 @Override
                                                                 public void run() {
                                                                     showLoadingProgress(false);
                                                                 }
                                                             }
                                ,3000-count);
                    }
                }
            });

        }
    }
    Dialog dialog;
    private void showLoadingProgress(boolean b) {
        if(b){
            dialog = new Dialog(AddAudioActivity.this, R.style.Theme_VideoToAudioConverter);
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
            Dialog dialog = new Dialog(AddAudioActivity.this, R.style.Theme_VideoToAudioConverter);
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
                    dialog.dismiss();
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


    private void trimVideoFile() {

        try {
            TrimVideoUtils.startTrim(AddAudioActivity.this, new File(String.valueOf(uri)), constants.TEMP_FOLDER_PATH, startpositionVideo, endPositionvideo, new OnTrimVideoListener() {
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
//            new TutorialAudioTrackToAacConvertor().convert(infile.getPath(),outFile.getPath());

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
            islargeVideo=true;
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
             input2 = new GeneralAudioInput(AddAudioActivity.this, Uri.parse(audioFile2.getPath()), null);
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
                        File f2=new File(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH);
                        if(!f2.exists()){
                            f2.mkdir();
                        }
                        File videoFile=new File(sharedPrefClass.getTempCreatedVideofromShared());
                        File outputMixedFile=new File(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH+"MixedAudionew"+System.currentTimeMillis()+".mp4");
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
    private void MergeAudios(ArrayList<String> files) throws IOException {
        ArrayList<AudioInput> audioInputs=new ArrayList<>();
        File outputfileAudio2=new File(constants.TEMP_FOLDER_PATH+"MergedAudio"+System.currentTimeMillis()+".mp3");

        for(int i=0;i<files.size();i++){
            audioInputs.add(new GeneralAudioInput(files.get(i)));
            float volume1=binding.volumeSeekbarAudio.getProgress()/1000f;
            audioInputs.get(i).setVolume(volume1);
        }

        final AudioMixer audioMixer = new AudioMixer(outputfileAudio2.getPath());
        for(int i=0;i<audioInputs.size();i++){
            audioMixer.addDataSource(audioInputs.get(i));
        }

        audioMixer.setSampleRate(44100);
        audioMixer.setBitRate(128000);
        audioMixer.setChannelCount(2);
        audioMixer.setLoopingEnabled(true);
        audioMixer.setMixingType(AudioMixer.MixingType.SEQUENTIAL); // or AudioMixer.MixingType.SEQUENTIAL

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
                        Toast.makeText(AddAudioActivity.this, "AudiosMerged", Toast.LENGTH_SHORT).show();
                        File videoFile=new File(sharedPrefClass.getTempCreatedVideofromShared());
                        File outputMixedFile=new File(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH+"MixedAudio"+System.currentTimeMillis()+".mp4");
                        if(sharedPrefClass.getTempCreatedVideofromShared()!=null) {
                            new AudioToVideoMuxer().mux(outputfileAudio2.getPath(), videoFile.getAbsolutePath(), outputMixedFile.getPath());
//                           dialog2.dismiss();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddAudioActivity.this);
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(AddAudioActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
                            alertDialog.setView(dialogView);
                            AlertDialog dialog2 = alertDialog.create();
                            dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialog2.setCancelable(false);
                            dialog2.show();

                            ProgressBar pb = dialogView.findViewById(R.id.progbar);
                            Button btnok = dialogView.findViewById(R.id.btnconfirmOk);
                            TextView title = dialogView.findViewById(R.id.dialogtitle);
                            TextView msg = dialogView.findViewById(R.id.txtloadinginstr);

                            title.setText(getResources().getString(R.string.videosaving));
                            msg.setText(getResources().getString(R.string.mixedVideoSaved));

                            pb.setVisibility(View.INVISIBLE);
                            btnok.setVisibility(View.VISIBLE);

                            btnok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        tmpFiles=new DataFetcherClass(constants.TEMP_FOLDER_PATH).get_Recovery_Audios();
                                        if(tmpFiles.size()!=0) {
                                            for (File f : tmpFiles) {
                                                f.delete();
                                            }
                                        }
                                        dialog2.dismiss();
                                        setResult(1);
                                        finish();

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
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
                startpositionVideo = (int) ((binding.videoShow.getDuration() * value) / 100L);
                Log.d("startandEndpostion",startpositionVideo+" start");
                break;
            }
            case Thumb.RIGHT: {
                endPositionvideo = (int) ((binding.videoShow.getDuration() * value) / 100L);
                Log.d("startandEndpostion",endPositionvideo+" end");
                break;
            }
        }

        if ((long)binding.videoShow.getCurrentPosition() < startpositionVideo) {
            binding.videoShow.seekTo(startpositionVideo);
            binding.videoPlayingTime.setText(timeConversion(startpositionVideo));
            int width = binding.handlerTop.getWidth()
                    - binding.handlerTop.getPaddingLeft()
                    - binding.handlerTop.getPaddingRight();
            float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
            binding.videoPlayingTime.setTranslationX(thumbPos);
            binding.videoDuration.setText(timeConversion(startpositionVideo).substring(1,timeConversion(startpositionVideo).length())+"/"+timeConversion(binding.videoShow.getDuration()).substring(1,timeConversion(binding.videoShow.getDuration()).length()));

        }
        if ((long)binding.videoShow.getCurrentPosition() > endPositionvideo) {
            binding.videoShow.seekTo(startpositionVideo);
            binding.videoPlayingTime.setText(timeConversion(startpositionVideo));
            int width = binding.handlerTop.getWidth()
                    - binding.handlerTop.getPaddingLeft()
                    - binding.handlerTop.getPaddingRight();
            float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
            binding.videoPlayingTime.setTranslationX(thumbPos);
            binding.videoDuration.setText(timeConversion(startpositionVideo).substring(1,timeConversion(startpositionVideo).length())+"/"+timeConversion(binding.videoShow.getDuration()).substring(1,timeConversion(binding.videoShow.getDuration()).length()));
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
        sharedPrefClass=new SharedPrefClass(AddAudioActivity.this);
        uri= Uri.parse(TrimmerActivity.VIDEOPATH);
        duration=getIntent().getIntExtra(VIDEO_TOTAL_DURATION,0);
        binding.btnMerge.setEnabled(false);
        type=TrimmerActivity.type;
        savePath=TrimmerActivity.TRIMMED_VIDEO_FOLDER_PATH;
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        binding.toolbarTitle.setText(uri.toString().substring(uri.toString().lastIndexOf("/")+1,uri.toString().length()));
        binding.toolbarTitle.setSelected(true);
        binding.videoShow.setVideoURI(uri);

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
                        binding.videoShow.start();
                    }

                if(mp.isPlaying()){
                    binding.imageplaypause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    mp.pause();
                }else {
                    binding.imageplaypause.setImageResource(R.drawable.ic_baseline_pause_24);

                    startservice2();

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
                                    binding.videoShow.pause();
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
                                   binding.videoShow.pause();
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
                                    binding.videoShow.pause();
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
                    mp.start();
                }
            }
        });

    }


    Handler handler2,handler3,handler4;
    Runnable runnable2,runnable3,runnable4;
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
                    handler2.postDelayed(runnable2, 1);
                }
            }
        };
        handler2.postDelayed(runnable2, 0);

    }
    private final void startserviceforVideo() {
        handler3 = new Handler();
        runnable3 = new Runnable() {
            @Override
            public void run() {
                binding.handlerTop.setProgress(binding.videoShow.getCurrentPosition());
                binding.videoPlayingTime.setText(timeConversion(binding.videoShow.getCurrentPosition()));
                int width = binding.handlerTop.getWidth()
                        - binding.handlerTop.getPaddingLeft()
                        - binding.handlerTop.getPaddingRight();
                float thumbPos = width * (binding.handlerTop.getProgress() / (float) binding.handlerTop.getMax());
                binding.videoPlayingTime.setTranslationX(thumbPos);
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



//    private FFmpeg ffmpeg;
//    @SuppressLint("LongLogTag")
//    private void loadFFMpegBinary() {
//        try {
//            if (ffmpeg == null) {
//                Log.d(TAG, "ffmpeg : era nulo");
//                ffmpeg = FFmpeg.getInstance(AddAudioActivity.this);
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
//                    Log.d(TAG, "FAILED with output : " + "loaded Sucessfully");
//
//
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
//        } catch (Exception e) {
//            Log.d(TAG, "EXception no controlada : " + e);
//        }
//    }
//    private void showUnsupportedExceptionDialog() {
//        new AlertDialog.Builder(AddAudioActivity.this)
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
//    int trimmedVideoDuration;
//    private void executeTrimmVideoCommand() {
//        filePath=null;
//        File dest = new File(TEMP_FOLDER_PATH+"VIDtrim"+System.currentTimeMillis()+".mp4");
//
//
//        while (dest.exists()) {
//            dest = new File(TEMP_FOLDER_PATH+"VIDtrim"+System.currentTimeMillis()+".mp4");
//        }
//
//        filePath = dest.getAbsolutePath();
//        sharedPrefClass.saveTrimmedVideoPahtinShared(filePath);
//        trimmedVideoDuration=sharedPrefClass.getVideoEndPosfromShared()-sharedPrefClass.getVideoStartPosfromShared();
//        String[] complexCommand = {"-i", String.valueOf(uri),"-ss","" + sharedPrefClass.getVideoStartPosfromShared() / 1000,"-t", "" + (sharedPrefClass.getVideoEndPosfromShared()-sharedPrefClass.getVideoStartPosfromShared()) / 1000,"-preset","ultrafast", filePath};
//
//        execFFmpegBinaryTrimVideo(complexCommand);
//    }
//
//    private void execFFmpegBinaryTrimVideo(String[] complexCommand) {
//        alertDialog = new AlertDialog.Builder(AddAudioActivity.this);
//        viewGroup = findViewById(android.R.id.content);
//        dialogView = LayoutInflater.from(AddAudioActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
//        alertDialog.setView(dialogView);
//        dialog2 = alertDialog.create();
//        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog2.setCancelable(false);
//        dialog2.show();
//
//        pb = dialogView.findViewById(R.id.progbar);
//        btnok = dialogView.findViewById(R.id.btndelyes);
//        title = dialogView.findViewById(R.id.dialogtitle);
//        msg = dialogView.findViewById(R.id.txtloadinginstr);
//        title.setText(getResources().getString(R.string.videosaving));
//        msg.setText(getResources().getString(R.string.msgsavingtrimvideo));
//
//        FFmpeg.executeAsync(complexCommand, new ExecuteCallback() {
//            @Override
//            public void apply(long executionId, int returnCode) {
//                if (returnCode == RETURN_CODE_SUCCESS) {
//                    Log.i(Config.TAG, "Command execution completed successfully.");
//                    executeTrimAudioCommand();
//                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Command execution cancelled by user.");
//                } else {
//                    Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", returnCode));
//                    Config.printLastCommandOutput(Log.INFO);
//                }
//            }
//        });
//        try {
//            ffmpeg.execute(complexCommand, new ExecuteBinaryResponseHandler() {
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onFailure(String s) {
//                    Log.d(TAG, "FAILED with output : " + s);
//                    sharedPrefClass.saveTrimmedVideoPahtinShared(null);
//                }
//
//                @Override
//                public void onSuccess(String s) {
//                    executeTrimAudioCommand();
////                    dialog2.dismiss();
//                }
//
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onProgress(String s) {
//                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
//
//                    Log.d(TAG, "progress : " + s);
//                }
//
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onStart() {
//                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
//
//                }
//
//                @Override
//                public void onFinish() {
//                    Log.d("paths",sharedPrefClass.getTrimmedVideoPathfromShared());
//                    Log.d("paths",trimmedAudioDuraiton+"audioD VideoD"+trimmedVideoDuration);
//
////                    String[] complexCommand = {"-i", String.valueOf(uri), "-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000,"-vcodec", "copy", "-acodec", "copy", filePath};
////                    execFFmpegBinary(complexCommand);
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            // do nothing for now
//        }
//    }
//    @SuppressLint("LongLogTag")
//    private void executeTrimAudioCommand() {
//        filePath=null;
//        File dest = new File(TEMP_FOLDER_PATH+"AUDtrim"+System.currentTimeMillis()+".mp3");
//
//        while (dest.exists()) {
//            dest = new File(TEMP_FOLDER_PATH+"AUDtrim"+System.currentTimeMillis()+".mp3");
//        }
//        filePath = dest.getAbsolutePath();
//        sharedPrefClass.saveTrimmedAudioPathinShared(filePath);
//        trimmedAudioDuraiton=sharedPrefClass.getAudioEndPosfromShared() - sharedPrefClass.getAudioStartPosfromShared();
//        if(trimmedVideoDuration<=trimmedAudioDuraiton){
//            String[] complexCommand = {"-i", sharedPrefClass.getAudioUrifromShared(),"-ss", "" + sharedPrefClass.getAudioStartPosfromShared() / 1000,"-t", "" + trimmedVideoDuration / 1000,"-preset", "ultrafast", filePath};
//            execFFmpegBinary2(complexCommand);
//        }else{
//            String[] complexCommand = {"-i", sharedPrefClass.getAudioUrifromShared(),"-ss", "" + sharedPrefClass.getAudioStartPosfromShared() / 1000,"-t", "" + (sharedPrefClass.getAudioEndPosfromShared() - sharedPrefClass.getAudioStartPosfromShared()) / 1000,"-preset", "ultrafast", filePath};
//            execFFmpegBinary2(complexCommand);
//        }
//
//
//
//    }
//    private void execFFmpegBinary2(String[] complexCommand) {
//        title.setText(getResources().getString(R.string.videosaving));
//        msg.setText(getResources().getString(R.string.trimingaudio));
//        FFmpeg.executeAsync(complexCommand, new ExecuteCallback() {
//            @Override
//            public void apply(long executionId, int returnCode) {
//                if (returnCode == RETURN_CODE_SUCCESS) {
//                    Log.i(Config.TAG, "Command execution completed successfully.");
//                    executeVolumeChangeCommandofVideo();
//                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Command execution cancelled by user.");
//                } else {
//                    Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", returnCode));
//                    Config.printLastCommandOutput(Log.INFO);
//                }
//            }
//        });
////        try {
////            ffmpeg.execute(complexCommand, new ExecuteBinaryResponseHandler() {
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onFailure(String s) {
////                    Log.d(TAG, "progress : trimAudio" + s);
////                    sharedPrefClass.saveTrimmedAudioPathinShared(null);
////                }
////
////                @Override
////                public void onSuccess(String s) {
////                    Log.d(TAG, "progress : trimAudio" + s);
////                    executeVolumeChangeCommandofVideo();
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onProgress(String s) {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
////
////                    Log.d(TAG, "progress : trimAudio" + s);
////
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onStart() {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
////                    Log.d(TAG, "progress : trimAudio" + "stated");
////
////
////                    title.setText(getResources().getString(R.string.videosaving));
////                    msg.setText(getResources().getString(R.string.trimingaudio));
////                }
////
////                @Override
////                public void onFinish() {
////                    Log.d("paths",sharedPrefClass.getTrimmedAudioPatjfromShared());
////                }
////            });
////        } catch (FFmpegCommandAlreadyRunningException e) {
////            // do nothing for now
////        }
//    }
//    private void executeVolumeChangeCommandofVideo() {
//        File dest = new File(TEMP_FOLDER_PATH+"VIDran"+System.currentTimeMillis()+".mp4");
//        while (dest.exists()) {
//            dest = new File(TEMP_FOLDER_PATH+"VIDran"+System.currentTimeMillis()+".mp4");
//        }
//        filePath = dest.getAbsolutePath();
//        sharedPrefClass.saveChangedVolumeVideoPathinShared(filePath);
//        int volume=sharedPrefClass.getVideoVolumefromShared()-100;
//        String[] complexCommand = {"-i", sharedPrefClass.getTrimmedVideoPathfromShared(),"-filter:a", "volume="+volume+"dB","-preset", "ultrafast", filePath};
//        execFFmpegBinarytochangevolume(complexCommand);
//
//    }
//
//    private void execFFmpegBinarytochangevolume(String[] complexCommand) {
//        msg.setText(getResources().getString(R.string.chaangevideovolume));
//        FFmpeg.executeAsync(complexCommand, new ExecuteCallback() {
//            @Override
//            public void apply(long executionId, int returnCode) {
//                if (returnCode == RETURN_CODE_SUCCESS) {
//                    Log.i(Config.TAG, "Command execution completed successfully.");
//                    executeVolumeChangeCommandofAudio();
//                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Command execution cancelled by user.");
//                } else {
//                    Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", returnCode));
//                    Config.printLastCommandOutput(Log.INFO);
//                }
//            }
//        });
//
////        try {
////            ffmpeg.execute(complexCommand, new ExecuteBinaryResponseHandler() {
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onFailure(String s) {
////                    Log.d(TAG, "FAILED with output : " + s);
////                    sharedPrefClass.saveChangedVolumeVideoPathinShared(null);
////                }
////
////                @Override
////                public void onSuccess(String s) {
////                    executeVolumeChangeCommandofAudio();
////
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onProgress(String s) {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
////
////                    Log.d(TAG, "progress : " + s);
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onStart() {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
////                    msg.setText(getResources().getString(R.string.chaangevideovolume));
////                }
////
////                @Override
////                public void onFinish() {
//////                    String[] complexCommand = {"-i", String.valueOf(uri), "-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000,"-vcodec", "copy", "-acodec", "copy", filePath};
//////                    execFFmpegBinary(complexCommand);
////                }
////            });
////        } catch (FFmpegCommandAlreadyRunningException e) {
////            // do nothing for now
////        }
//    }
//    private void executeVolumeChangeCommandofAudio() {
//        File dest = new File(TEMP_FOLDER_PATH+"AUDran"+System.currentTimeMillis()+".mp3");
//        while (dest.exists()) {
//            dest = new File(TEMP_FOLDER_PATH+"AUDran"+System.currentTimeMillis()+".mp3");
//        }
//        filePath = dest.getAbsolutePath();
//        sharedPrefClass.saveChangedVolumeAudioPathinShared(filePath);
//        int volume=sharedPrefClass.getAudioVolumefromShared()-100;
//        String[] complexCommand = {"-i", sharedPrefClass.getTrimmedAudioPatjfromShared(),"-filter:a", "volume="+volume+"dB","-preset", "ultrafast", filePath};
//
//        execFFmpegBinarytochangevolumeAudio(complexCommand);
//    }
//
//
//
//    private void execFFmpegBinarytochangevolumeAudio(String[] complexCommand) {
//        msg.setText(getResources().getString(R.string.chaangeaudiovolume));
//        FFmpeg.executeAsync(complexCommand, new ExecuteCallback() {
//            @Override
//            public void apply(long executionId, int returnCode) {
//                if (returnCode == RETURN_CODE_SUCCESS) {
//                    Log.i(Config.TAG, "Command execution completed successfully.");
////                    if(trimmedAudioDuraiton>=trimmedVideoDuration) {
//                        executeMixVideoCommand(sharedPrefClass.getChangedVolumeAudioPathfromShared());
////                    }else {
////                        executeSumAudioCommand();
////                    }
//                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Command execution cancelled by user.");
//                } else {
//                    Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", returnCode));
//                    Config.printLastCommandOutput(Log.INFO);
//                }
//            }
//        });
////        try {
////            ffmpeg.execute(complexCommand, new ExecuteBinaryResponseHandler() {
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onFailure(String s) {
////                    Log.d(TAG, "FAILED with output : " + s);
////                    sharedPrefClass.saveChangedVolumeAudioPathinShared(null);
////                }
////
////                @Override
////                public void onSuccess(String s) {
////                    if(trimmedAudioDuraiton>=trimmedVideoDuration) {
////                        executeMixVideoCommand(sharedPrefClass.getChangedVolumeAudioPathfromShared());
////                    }else {
////                        executeSumAudioCommand();
////                    }
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onProgress(String s) {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
////
////                    Log.d(TAG, "progress : " + s);
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onStart() {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
////
////
////                    msg.setText(getResources().getString(R.string.chaangeaudiovolume));
////                }
////
////                @Override
////                public void onFinish() {
////                }
////            });
////        } catch (FFmpegCommandAlreadyRunningException e) {
////            // do nothing for now
////        }
//    }
//
//
//    String filePath;
//    @SuppressLint("LongLogTag")
//     private void executeSumAudioCommand() {
//        filePath=null;
//        File dest = new File(TEMP_FOLDER_PATH+"AUD"+System.currentTimeMillis()+".mp3");
//        while (dest.exists()) {
//            dest = new File(TEMP_FOLDER_PATH+"AUD"+System.currentTimeMillis()+".mp3");
//        }
//        filePath = dest.getAbsolutePath();
//        sharedPrefClass.saveSummedVideoinShared(filePath);
//        int itereations= (int) (duration/trimmedAudioDuraiton);
//       execFFmpegBinarySumAudio(createCommandandAdd(itereations));
//
//    }
//     public String[] createCommandandAdd(int itereations){
//        String[] complexCommand = {"-stream_loop",String.valueOf(itereations),"-codec","ac3", "-i",sharedPrefClass.getChangedVolumeAudioPathfromShared(),"-preset", "ultrafast", filePath};
//         return  complexCommand;
//     }
//
//    private void execFFmpegBinarySumAudio(String[] complexCommand) {
//        msg.setText(getResources().getString(R.string.iteratingAudio));
//        FFmpeg.executeAsync(complexCommand, new ExecuteCallback() {
//            @Override
//            public void apply(long executionId, int returnCode) {
//                if (returnCode == RETURN_CODE_SUCCESS) {
//                    Log.i(Config.TAG, "Command execution completed successfully.");
//                    //executeMixVideoCommand(sharedPrefClass.getSummedVideofromShared());
//                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Command execution cancelled by user.");
//                } else {
//                    Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", returnCode));
//                    Config.printLastCommandOutput(Log.INFO);
//                }
//            }
//        });
////        try {
////            ffmpeg.execute(complexCommand, new ExecuteBinaryResponseHandler() {
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onFailure(String s) {
////                    Log.d(TAG, "progress : SumAudios" + s);
////                    sharedPrefClass.saveSummedVideoinShared(null);
////                }
////
////                @Override
////                public void onSuccess(String s) {
////                    executeMixVideoCommand(sharedPrefClass.getSummedVideofromShared());
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onProgress(String s) {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
////                    Log.d(TAG, "progress : SumAudios" + s);
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onStart() {
////                    Log.d(TAG, "progress : SumAudios" + Arrays.toString(complexCommand));
////                    Toast.makeText(AddAudioActivity.this, "Started", Toast.LENGTH_SHORT).show();
////                    title.setText(getResources().getString(R.string.videosaving));
////                    msg.setText(getResources().getString(R.string.iteratingAudio));
////                }
////
////                @Override
////                public void onFinish() {
////                    Log.d("paths",sharedPrefClass.getSummedVideofromShared());
////                    Toast.makeText(AddAudioActivity.this, "finised", Toast.LENGTH_SHORT).show();
////
////                }
////            });
////        } catch (FFmpegCommandAlreadyRunningException e) {
////            // do nothing for now
////        }
//    }
//    @SuppressLint("LongLogTag")
//    private void executeMixVideoCommand(String audiopath) {
//        filePath=null;
//        File dest = new File(ADD_AUDIO_TO_VIDEO_FOLDER_PATH+"VID"+System.currentTimeMillis()+".mp4");
//        while (dest.exists()) {
//            dest = new File(ADD_AUDIO_TO_VIDEO_FOLDER_PATH+"VID"+System.currentTimeMillis()+".mp4");
//        }
//        filePath = dest.getAbsolutePath();
//        if(isVideoHaveAudioTrack(sharedPrefClass.getChangedVolumeVideoPathfromShared())) {
//            String[] complexCommand = {"-i", sharedPrefClass.getChangedVolumeVideoPathfromShared(),"-stream_loop","-1", "-i", audiopath, "-filter_complex", "[0:a][1:a]amerge=inputs=2[a]", "-map", "0:v", "-map", "[a]", "-c:v", "copy", "-ac", "2", "-shortest", "-preset", "ultrafast", filePath};
//            execFFmpegBinary(complexCommand);
//        }else{
//            String[] complexCommand = {"-i", sharedPrefClass.getChangedVolumeVideoPathfromShared(),"-stream_loop","-1","-i",audiopath,"-map","0:v", "-map", "1:a","-c:v","copy","-shortest", filePath};
//            execFFmpegBinary(complexCommand);
//        }
//
//    }
//    private void execFFmpegBinary(final String[] command) {
//        msg.setText(getResources().getString(R.string.mixingAudio));
//        FFmpeg.executeAsync(command, new ExecuteCallback() {
//            @Override
//            public void apply(long executionId, int returnCode) {
//                if (returnCode == RETURN_CODE_SUCCESS) {
//                    Log.i(Config.TAG, "Command execution completed successfully.");
//                    dialog2.dismiss();
//
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddAudioActivity.this);
//                        ViewGroup viewGroup = findViewById(android.R.id.content);
//                        View dialogView = LayoutInflater.from(AddAudioActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
//                        alertDialog.setView(dialogView);
//                        AlertDialog dialog2 = alertDialog.create();
//                        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                        dialog2.setCancelable(false);
//                        dialog2.show();
//
//                        ProgressBar pb = dialogView.findViewById(R.id.progbar);
//                        Button btnok = dialogView.findViewById(R.id.btnconfirmOk);
//                        TextView title = dialogView.findViewById(R.id.dialogtitle);
//                        TextView msg = dialogView.findViewById(R.id.txtloadinginstr);
//
//                        title.setText(getResources().getString(R.string.videosaving));
//                        msg.setText(getResources().getString(R.string.mixedVideoSaved));
//
//                        pb.setVisibility(View.INVISIBLE);
//                        btnok.setVisibility(View.VISIBLE);
//
//                        btnok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                try {
//                                    tmpFiles=new DataFetcherClass(TEMP_FOLDER_PATH).get_Recovery_Audios();
//                                    if(tmpFiles.size()!=0) {
//                                        for (File f : tmpFiles) {
//                                            f.delete();
//                                        }
//                                    }
//                                    dialog2.dismiss();
//                                    setResult(1);
//                                    finish();
//
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            }
//                        });
//                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Command execution cancelled by user.");
//                } else {
//                    Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", returnCode));
//                    Config.printLastCommandOutput(Log.INFO);
//                }
//            }
//        });
////        try {
////            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onFailure(String s) {
////                    Log.d(TAG, "FAILED with output : " + s);
////                }
////
////                @Override
////                public void onSuccess(String s) {
////                        dialog2.dismiss();
////
////                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddAudioActivity.this);
////                        ViewGroup viewGroup = findViewById(android.R.id.content);
////                        View dialogView = LayoutInflater.from(AddAudioActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
////                        alertDialog.setView(dialogView);
////                        AlertDialog dialog2 = alertDialog.create();
////                        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
////                        dialog2.setCancelable(false);
////                        dialog2.show();
////
////                        ProgressBar pb = dialogView.findViewById(R.id.progbar);
////                        Button btnok = dialogView.findViewById(R.id.btnconfirmOk);
////                        TextView title = dialogView.findViewById(R.id.dialogtitle);
////                        TextView msg = dialogView.findViewById(R.id.txtloadinginstr);
////
////                        title.setText(getResources().getString(R.string.videosaving));
////                        msg.setText(getResources().getString(R.string.mixedVideoSaved));
////
////                        pb.setVisibility(View.INVISIBLE);
////                        btnok.setVisibility(View.VISIBLE);
////
////                        btnok.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                try {
////                                    tmpFiles=new DataFetcherClass(TEMP_FOLDER_PATH).get_Recovery_Audios();
////                                    if(tmpFiles.size()!=0) {
////                                        for (File f : tmpFiles) {
////                                            f.delete();
////                                        }
////                                    }
////                                    dialog2.dismiss();
////                                    setResult(1);
////                                    finish();
////
////                                } catch (Exception ex) {
////                                    ex.printStackTrace();
////                                }
////                            }
////                        });
////
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onProgress(String s) {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(command));
////
////                    Log.d(TAG, "progress : addaudiotovideo" + s);
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onStart() {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(command));
////
////                        title.setText(getResources().getString(R.string.videosaving));
////
////
////                }
////
////                @Override
////                public void onFinish() {
////                }
////            });
////        } catch (FFmpegCommandAlreadyRunningException e) {
////        }
//    }
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
//            handler2.removeCallbacks(runnable2);
//            handler2.postDelayed(runnable2,0);
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