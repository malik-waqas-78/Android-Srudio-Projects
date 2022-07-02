package com.video.trimmer.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//import com.arthenica.mobileffmpeg.Config;
//import com.arthenica.mobileffmpeg.ExecuteCallback;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

//import com.arthenica.mobileffmpeg.FFmpeg;
import com.airbnb.lottie.LottieAnimationView;
import com.video.trimmer.R;
import com.video.trimmer.databinding.ActivityAudioTrimmer2Binding;
import com.video.trimmer.interfaces.OnRangeSeekBarListener;
import com.video.trimmer.utils.AudioExtractor;
import com.video.trimmer.utils.CheapSoundFile;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.DataFetcherClass;
import com.video.trimmer.utils.RangeSeekBarView2;
import com.video.trimmer.utils.SharedPrefClass;
import com.video.trimmer.utils.Util;
import com.video.trimmer.videoTrimmer.view.Thumb;
//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
//import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import static com.video.trimmer.utils.Constants.AUDIOSIZE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.NAME_INTENT_KEY;


import static com.video.trimmer.utils.Constants.URI_INTENT_KEY;

public class AudioTrimmerActivity2 extends AppCompatActivity {
    ActivityAudioTrimmer2Binding binding;
    long startposition,endposition;
    MediaPlayer mp;
    String url;
    AlertDialog dialog2,dialog3;
    AlertDialog.Builder alertDialog,alertDialog2;
    View dialogView,dialogView2;
    ViewGroup viewGroup,viewGroup2;
    ProgressBar pb;

    SharedPrefClass sharedPrefClass;
    Button btnok;
    TextView title;
    TextView msg;
    boolean check=false;
    ScheduledExecutorService service;
    private double oneSecValue;
    boolean firstTime=true;
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAudioTrimmer2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        binding.toolbar.setTitle("");
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_icon));
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sharedPrefClass=new SharedPrefClass(AudioTrimmerActivity2.this);

        playAudio();
        binding.rangeseekbar4.addOnRangeSeekBarListener(binding.progrssView4);
        binding.rangeseekbar4.addOnRangeSeekBarListener(new OnRangeSeekBarListener() {
            @Override
            public void onCreate(RangeSeekBarView2 rangeSeekBarView, int index, float value) {

            }

            @Override
            public void onSeek(RangeSeekBarView2 rangeSeekBarView, int index, float value) {
                onSeekThumbsAudio(index,value);
            }

            @Override
            public void onSeekStart(RangeSeekBarView2 rangeSeekBarView, int index, float value) {

            }

            @Override
            public void onSeekStop(RangeSeekBarView2 rangeSeekBarView, int index, float value) {

            }
        });
        binding.forwardAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.getCurrentPosition()+2000<=Integer.parseInt(String.valueOf(endposition))){
                    mp.seekTo(mp.getCurrentPosition()+2000);
                    if(!mp.isPlaying()){
                        binding.pbarAudio4.setProgress(mp.getCurrentPosition());
                    }
                }
            }
        });
        binding.reverseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.getCurrentPosition()-2000>=Integer.parseInt(String.valueOf(startposition))){
                    mp.seekTo(mp.getCurrentPosition()-2000);
                    if(!mp.isPlaying()){
                        binding.pbarAudio4.setProgress(mp.getCurrentPosition());
                    }
                }
            }
        });
        binding.btnPlusStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeConversion(startposition) .equals(timeConversion(endposition)) ) {
                    startposition = startposition + 1000;
                    binding.audioStartTime.setText(timeConversion(startposition));
                    long dif = endposition - startposition;
                    binding.audioRemainingdurationtxt4.setText(timeConversion(dif));
                    int pos= (int) (startposition/1000);
                    if(!mp.isPlaying()){
                        mp.seekTo(Integer.parseInt(String.valueOf(startposition)));
                        binding.pbarAudio4.setProgress(mp.getCurrentPosition());
                    }
                     double f = (double) (binding.rangeseekbar4.getThumbValue(0) + getOneSecValue());
                    binding.rangeseekbar4.setThumbValue(0, (float) f);
                }
            }
        });
        binding.btnMinusStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeConversion(startposition) .equals(timeConversion(0)) ) {
                    startposition = startposition - 1000;
                    binding.audioStartTime.setText(timeConversion(startposition));
                    long dif = endposition - startposition;
                    binding.audioRemainingdurationtxt4.setText(timeConversion(dif));
                    int pos= (int) (startposition/1000);
                    if(!mp.isPlaying()){
                        mp.seekTo(Integer.parseInt(String.valueOf(startposition)));
                        binding.pbarAudio4.setProgress(mp.getCurrentPosition());
                    }
//                    binding.rangeseekbar.setCurrentMinValue((int) startposition);
                    double f = (double) (binding.rangeseekbar4.getThumbValue(0) - getOneSecValue());
                    binding.rangeseekbar4.setThumbValue(0, (float) f);
                }
            }
        });
        binding.btnPlusEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeConversion(endposition) .equals(timeConversion(mp.getDuration())) ) {
                    endposition += 1000;
                    binding.audioEndTime.setText(timeConversion(endposition));
                    long dif = endposition - startposition;
                    binding.audioRemainingdurationtxt4.setText(timeConversion(dif));
                    int pos=(int)(endposition/1000);
//                    binding.rangeseekbar.setCurrentMaxValue((int) endposition);
                    float f = (float) (binding.rangeseekbar4.getThumbValue(1) + getOneSecValue());
                    binding.rangeseekbar4.setThumbValue(1, f);
                }
            }
        });
        binding.btnMinusEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeConversion(endposition).equals(timeConversion(startposition))) {
                    endposition -= 1000;
                    binding.audioEndTime.setText(timeConversion(endposition));
                    long dif = endposition - startposition;
                    binding.audioRemainingdurationtxt4.setText(timeConversion(dif));
//                    binding.rangeseekbar.setCurrentMaxValue((int) endposition);
                    float f = (float) (binding.rangeseekbar4.getThumbValue(1) - getOneSecValue());
                    binding.rangeseekbar4.setThumbValue(1, f);
                }
            }
        });
//        loadFromFile(url.toString());

        binding.btnTrimAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();
                    binding.imageplayPause.setImageResource(R.drawable.ic_play);
                }
                new LoadData().execute();

            }
        });
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                binding.imageplayPause.setImageResource(R.drawable.ic_pause);
                startservice();
//                binding.rangeseekbar4.setVisibility(View.VISIBLE);
//                binding.rangeseekbar4.initMaxWidth();
            }
        });
        binding.pbarAudio4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(firstTime) {
                    firstTime=false;
                    binding.rangeseekbar4.initMaxWidth();
                    binding.rangeseekbar4.setVisibility(View.VISIBLE);
                }
                binding.tvCurrentlyPlayed.setText(timeConversion(progress));

                if (fromUser) {
//                            Log.d(TESTING_TAG, "onProgressChanged: $progress")
                    if (progress >= Integer.parseInt(String.valueOf(startposition)) && progress <=Integer.parseInt(String.valueOf(endposition))) {
                        mp.seekTo(progress);
                        binding.audioPlayingTime.setText(timeConversion(progress));
                        int width = binding.pbarAudio4.getWidth()
                                - binding.pbarAudio4.getPaddingLeft()
                                - binding.pbarAudio4.getPaddingRight();
                        float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                        binding.audioPlayingTime.setTranslationX(thumbPos);
                    }else if(progress<=Integer.parseInt(String.valueOf(startposition))){
                        mp.seekTo(Integer.parseInt(String.valueOf(startposition)));
                        binding.audioPlayingTime.setText(timeConversion(progress));
                        int width = binding.pbarAudio4.getWidth()
                                - binding.pbarAudio4.getPaddingLeft()
                                - binding.pbarAudio4.getPaddingRight();
                        float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                        binding.audioPlayingTime.setTranslationX(thumbPos);
                    }else if(progress>=Integer.parseInt(String.valueOf(endposition))){
                        mp.seekTo(Integer.parseInt(String.valueOf(startposition)));
                        binding.audioPlayingTime.setText(timeConversion(progress));
                        int width = binding.pbarAudio4.getWidth()
                                - binding.pbarAudio4.getPaddingLeft()
                                - binding.pbarAudio4.getPaddingRight();
                        float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                        binding.audioPlayingTime.setTranslationX(thumbPos);
                        binding.imageplayPause.setImageResource(R.drawable.ic_play);
                        mp.pause();
                    }
                } else{
                    binding.audioPlayingTime.setText(timeConversion(progress));
                    int width = binding.pbarAudio4.getWidth()
                            - binding.pbarAudio4.getPaddingLeft()
                            - binding.pbarAudio4.getPaddingRight();
                    float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                    binding.audioPlayingTime.setTranslationX(thumbPos);
                    if (progress == Integer.parseInt(String.valueOf(endposition))) {
                        mp.seekTo( Integer.parseInt(String.valueOf(startposition)));
                        binding.audioPlayingTime.setText(timeConversion(progress));
                        int width2 = binding.pbarAudio4.getWidth()
                                - binding.pbarAudio4.getPaddingLeft()
                                - binding.pbarAudio4.getPaddingRight();
                        float thumbPos2 = width2 * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                        binding.audioPlayingTime.setTranslationX(thumbPos2);
                        binding.imageplayPause.setImageResource(R.drawable.ic_play);
                        mp.pause();

                    }
                }
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.seekTo( Integer.parseInt(String.valueOf(startposition)));
                        binding.audioPlayingTime.setText(timeConversion(progress));
                        int width = binding.pbarAudio4.getWidth()
                                - binding.pbarAudio4.getPaddingLeft()
                                - binding.pbarAudio4.getPaddingRight();
                        float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
                        binding.audioPlayingTime.setTranslationX(thumbPos);
                        binding.imageplayPause.setImageResource(R.drawable.ic_play);
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
                    showLoadingProgress(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                convertAudiotoAAC();
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
                    }else{
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
    ArrayList<File> tmpFiles=new ArrayList<>();
    private void showLoadingProgress(boolean b) {
        if(b){
            dialog = new Dialog(AudioTrimmerActivity2.this, R.style.Theme_VideoToAudioConverter);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_video_saving);
            dialog.show();

            pb = dialog.findViewById(R.id.progbar);
            btnok = dialog.findViewById(R.id.btndelyes);
            title = dialog.findViewById(R.id.dialogtitle);
            msg = dialog.findViewById(R.id.txtloadinginstr);


            title.setText(getResources().getString(R.string.trimingaudio));
            msg.setText(getResources().getString(R.string.msgtrimmingAudio));
        }else{
            dialog.dismiss();
            Dialog dialog = new Dialog(AudioTrimmerActivity2.this, R.style.Theme_VideoToAudioConverter);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_video_saving);
            dialog.show();

            ProgressBar  pb=dialog.findViewById(R.id.progbar);
            Button btnok=dialog.findViewById(R.id.btnconfirmOk);
            TextView title=dialog.findViewById(R.id.dialogtitle);
            TextView msg=dialog.findViewById(R.id.txtloadinginstr);
            title.setText(getResources().getString(R.string.trimingaudio));
            msg.setText(getResources().getString(R.string.trimmedAudiosucess));
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

                    tmpFiles=new DataFetcherClass(constants.TEMP_FOLDER_PATH).get_Recovery_Audios();
                    if(tmpFiles.size()!=0) {
                        for (File f : tmpFiles) {
                            f.delete();
                        }
                    }
                    setResult(1);
                    finish();
                }
            });
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }
    private void convertAudiotoAAC() throws IOException {
        File infile=new File(url);

        if((size/1024)/1000<=1.0){
            File f=new File(constants.TEMP_FOLDER_PATH);
            if(!f.exists()){
                f.mkdir();
            }
            File outFile=new File(constants.TEMP_FOLDER_PATH+"TrimmedAudio"+System.currentTimeMillis()+".aac");

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
            bos.write(convert(infile.getPath()));
            bos.flush();
            bos.close();

//            new AudioTrackToAacConvertor().convert(infile.getPath(),outFile.getPath(),(int)(endposition-startposition),1,1);

            //TODO:
            File f2=new File(constants.TRIMMED_AUDIO_FOLDER_PATH);
            if(!f2.exists()){
                f2.mkdir();
            }
            File outFile2=new File(constants.TRIMMED_AUDIO_FOLDER_PATH+"TrimmedAudio"+System.currentTimeMillis()+".mp3");
            sharedPrefClass.saveOutputPathinShared(outFile2.getPath());
                try {
                   new AudioExtractor().genVideoUsingMuxer(outFile.getPath(),outFile2.getPath(),Integer.parseInt(String.valueOf(startposition)),Integer.parseInt(String.valueOf(endposition)),true,false);
//                    dialog2.dismiss();
//                    changeVolumeofAudio(infile);

                } catch (IOException e) {
                    e.printStackTrace();
                }




        }else{
            File f2=new File(constants.TRIMMED_AUDIO_FOLDER_PATH);
            if(!f2.exists()){
                f2.mkdir();
            }
            File outFile=new File(constants.TRIMMED_AUDIO_FOLDER_PATH+"TrimmedAudio"+System.currentTimeMillis()+".mp3");
            sharedPrefClass.saveOutputPathinShared(outFile.getPath());
            final CheapSoundFile.ProgressListener listener = new CheapSoundFile.ProgressListener() {
                public boolean reportProgress(double frac) {
                    Log.d("progresstrimmingAuduio",frac+"");
                    return true;
                }
            };
            CheapSoundFile cheapSoundFile= CheapSoundFile.create(infile.getPath(),listener);
            int mSampleRate = cheapSoundFile.getSampleRate();

            int mSamplesPerFrame = cheapSoundFile.getSamplesPerFrame();

            int startFrame = Util.secondsToFrames(startposition/1000,mSampleRate, mSamplesPerFrame);

            int endFrame = Util.secondsToFrames(endposition/1000, mSampleRate,mSamplesPerFrame);

            cheapSoundFile.WriteFile(outFile, startFrame, endFrame-startFrame);
        }



//        new TutorialAudioTrackToAacConvertor().convert(infile.getPath(),outFile.getPath());

    }

    public double getOneSecValue() {
        oneSecValue=(1000f/binding.pbarAudio4.getMax())*100;
        return oneSecValue;
    }
    private void onSeekThumbsAudio(int index, float value) {
        switch (index) {
            case Thumb.LEFT: {
                startposition = (int) ((binding.pbarAudio4.getMax() * value) / 100L);
                Log.d("startandEndpostion",startposition+" start");
                break;
            }
            case Thumb.RIGHT: {
                endposition = (int) ((binding.pbarAudio4.getMax() * value) / 100L);
                Log.d("startandEndpostion",endposition+" end");
                break;
            }
        }

        long diff=endposition-startposition;
        binding.audioRemainingdurationtxt4.setText(timeConversion(diff));

        if ((long)mp.getCurrentPosition() < startposition) {
            mp.seekTo(Integer.parseInt(String.valueOf(startposition)));
            binding.audioPlayingTime.setText(timeConversion(startposition));
            int width = binding.pbarAudio4.getWidth()
                    - binding.pbarAudio4.getPaddingLeft()
                    - binding.pbarAudio4.getPaddingRight();
            float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
            binding.pbarAudio4.setProgress(Integer.parseInt(String.valueOf(startposition)));

        }
        if ((long)mp.getCurrentPosition() > endposition) {
            mp.seekTo(Integer.parseInt(String.valueOf(startposition)));
            binding.audioPlayingTime.setText(timeConversion(startposition));
            int width = binding.pbarAudio4.getWidth()
                    - binding.pbarAudio4.getPaddingLeft()
                    - binding.pbarAudio4.getPaddingRight();
            float thumbPos = width * (binding.pbarAudio4.getProgress() / (float) binding.pbarAudio4.getMax());
            binding.pbarAudio4.setProgress(Integer.parseInt(String.valueOf(startposition)));
        }
        binding.audioStartTime.setText(timeConversion(startposition));
        binding.audioEndTime.setText(timeConversion(endposition));
    }
    float size;
    private void playAudio() {
        mp=null;
        mp=new MediaPlayer();
        binding.toolbarTitle.setText(getIntent().getStringExtra(NAME_INTENT_KEY));
        url=getIntent().getStringExtra(URI_INTENT_KEY);
        size=getIntent().getFloatExtra(AUDIOSIZE_INTENT_KEY,0f);

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

        //binding.rangeseekbar4.setVisibility(View.VISIBLE);

        binding.imageplayPause.setImageResource(R.drawable.ic_play);
        startposition=0;
        endposition= binding.pbarAudio4.getMax();

//        binding.rangeseekbar4.initMaxWidth();
        long diff=endposition-startposition;
        binding.audioRemainingdurationtxt4.setText(timeConversion(diff));
        binding.audioStartTime.setText(timeConversion(startposition));
        binding.audioEndTime.setText(timeConversion(endposition));

        service = Executors.newScheduledThreadPool(0);

        binding.imageplayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    binding.imageplayPause.setImageResource(R.drawable.ic_play);
                    mp.pause();
                }else {
                    binding.imageplayPause.setImageResource(R.drawable.ic_pause);
                    startservice();
                    mp.start();
                }
            }
        });
    }
//    private void loadFFMpegBinary() {
//        try {
//            if (ffmpeg == null) {
//                Log.d(TAG, "ffmpeg : era nulo");
//                ffmpeg = FFmpeg.getInstance(AudioTrimmerActivity2.this);
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
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
//        } catch (Exception e) {
//            Log.d(TAG, "EXception no controlada : " + e);
//        }
//    }
//    private void showUnsupportedExceptionDialog() {
//        new AlertDialog.Builder(AudioTrimmerActivity2.this)
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
//    String filePath;
//    private void executeTrimAudioCommand() {
//        File dest = new File(TRIMMED_AUDIO_FOLDER_PATH+"AUD"+System.currentTimeMillis()+".mp3");
//        while (dest.exists()) {
//            dest = new File(TRIMMED_AUDIO_FOLDER_PATH+"AUD"+System.currentTimeMillis()+".mp3");
//        }
//        filePath = dest.getAbsolutePath();
//        String[] complexCommand = {"-i", String.valueOf(url),"-ss", "" + startposition / 1000, "-t", "" + (endposition - startposition) / 1000, filePath};
//        execFFmpegBinary2(complexCommand);
//
//    }
////    private FFmpeg ffmpeg;
//    private void execFFmpegBinary2(String[] complexCommand) {
//                    alertDialog = new AlertDialog.Builder(AudioTrimmerActivity2.this);
//                    viewGroup = findViewById(android.R.id.content);
//                    dialogView = LayoutInflater.from(AudioTrimmerActivity2.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
//                    alertDialog.setView(dialogView);
//                    dialog2 = alertDialog.create();
//                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                    dialog2.setCancelable(false);
//                    dialog2.show();
//
//                    pb = dialogView.findViewById(R.id.progbar);
//                    btnok = dialogView.findViewById(R.id.btndelyes);
//                    title = dialogView.findViewById(R.id.dialogtitle);
//                    msg = dialogView.findViewById(R.id.txtloadinginstr);
//
//                    title.setText(getResources().getString(R.string.trimingaudio));
//                    msg.setText(getResources().getString(R.string.msgtrimmingAudio));
//        FFmpeg.executeAsync(complexCommand, new ExecuteCallback() {
//            @Override
//            public void apply(long executionId, int returnCode) {
//                if (returnCode == RETURN_CODE_SUCCESS) {
//                    Log.i(Config.TAG, "Command execution completed successfully.");
//                    dialog2.dismiss();
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AudioTrimmerActivity2.this);
//                    ViewGroup viewGroup=findViewById(android.R.id.content);
//                    View dialogView= LayoutInflater.from(AudioTrimmerActivity2.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
//                    alertDialog.setView(dialogView);
//                    AlertDialog dialog2=alertDialog.create();
//                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                    dialog2.show();
//
//                    ProgressBar  pb=dialogView.findViewById(R.id.progbar);
//                    Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
//                    TextView title=dialogView.findViewById(R.id.dialogtitle);
//                    TextView msg=dialogView.findViewById(R.id.txtloadinginstr);
//
//                    title.setText(getResources().getString(R.string.trimingaudio));
//                    msg.setText(getResources().getString(R.string.trimmedAudiosucess));
//
//                    pb.setVisibility(View.INVISIBLE);
//                    btnok.setVisibility(View.VISIBLE);
//
//                    btnok.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            try {
//                                dialog2.dismiss();
//                                setResult(1);
//                                finish();
//
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    });
//                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Command execution cancelled by user.");
//                } else {
//                    Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", returnCode));
//                    Config.printLastCommandOutput(Log.INFO);
//                }
//            }
//        });
//
//
////        try {
////            ffmpeg.execute(complexCommand, new ExecuteBinaryResponseHandler() {
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onFailure(String s) {
////                    Log.d(TAG, "progress : trimAudio" + s);
////
////                }
////
////                @Override
////                public void onSuccess(String s) {
////                    Log.d(TAG, "progress : trimAudio" + s);
////
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onProgress(String s) {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
////
////                    Log.d(TAG, "progress : trimAudio" + s);
////                }
////
////                @SuppressLint("LongLogTag")
////                @Override
////                public void onStart() {
////                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(complexCommand));
////                    Log.d(TAG, "progress : trimAudio" + "stated");
////
////                }
////
////                @Override
////                public void onFinish() {
////
////                    // executeMixVideoCommand();
//////                    String[] complexCommand = {"-i", String.valueOf(uri), "-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000,"-vcodec", "copy", "-acodec", "copy", filePath};
//////                    execFFmpegBinary(complexCommand);
////                }
////            });
////        } catch (FFmpegCommandAlreadyRunningException e) {
////            // do nothing for now
////        }
//    }

//    private void changeVolumeofAudio(File audioFile1) throws IOException {
//
//        File outputfileAudio2=new File(constants.TEMP_FOLDER_PATH+"TempAudioChanged"+System.currentTimeMillis()+".mp3");
//        AudioInput input1 = new GeneralAudioInput(audioFile1.getPath());
//        input1.setVolume(0.1f);
//
//        final AudioMixer audioMixer = new AudioMixer(outputfileAudio2.getPath());
//        audioMixer.addDataSource(input1);
//
//        audioMixer.setSampleRate(44100);
//        audioMixer.setBitRate(128000);
//        audioMixer.setChannelCount(2);
//
////        audioMixer.setLoopingEnabled(true);
////        audioMixer.setMixingType(AudioMixer.MixingType.PARALLEL); // or AudioMixer.MixingType.SEQUENTIAL
//        audioMixer.setProcessingListener(new AudioMixer.ProcessingListener() {
//            @Override
//            public void onProgress(final double progress) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        Toast.makeText(AudioTrimmerActivity2.this, "Started!!!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            @Override
//            public void onEnd() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AudioTrimmerActivity2.this);
//                        ViewGroup viewGroup=findViewById(android.R.id.content);
//                        View dialogView= LayoutInflater.from(AudioTrimmerActivity2.this).inflate(R.layout.dialog_video_saving,viewGroup,false);
//                        alertDialog.setView(dialogView);
//                        AlertDialog dialog2=alertDialog.create();
//                        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                        dialog2.show();
//
//                        ProgressBar  pb=dialogView.findViewById(R.id.progbar);
//                        Button btnok=dialogView.findViewById(R.id.btnconfirmOk);
//                        TextView title=dialogView.findViewById(R.id.dialogtitle);
//                        TextView msg=dialogView.findViewById(R.id.txtloadinginstr);
//
//                        title.setText(getResources().getString(R.string.trimingaudio));
//                        msg.setText(getResources().getString(R.string.trimmedAudiosucess));
//
//                        pb.setVisibility(View.INVISIBLE);
//                        btnok.setVisibility(View.VISIBLE);
//
//                        btnok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                try {
//                                    dialog2.dismiss();
//                                    setResult(1);
//                                    finish();
//
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            }
//                        });
//                        audioMixer.release();
//                    }
//                });
//            }
//        });
//        audioMixer.start();
//        audioMixer.processAsync();
//
//    }
    private final void startservice() {
        if (service != null) {
            service.scheduleWithFixedDelay((Runnable)(new Runnable() {
                public final void run() {
                    binding.pbarAudio4.setProgress(mp.getCurrentPosition());
                }
            }), 1L, 1L, TimeUnit.MICROSECONDS);
        } else {
            service = null;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mp!=null){
            if(mp.isPlaying()){
                mp.stop();
            }
            mp.release();
        }
    }
}