package com.video.trimmer.activities;


//import com.arthenica.mobileffmpeg.Config;
//import com.arthenica.mobileffmpeg.ExecuteCallback;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.arthenica.mobileffmpeg.FFmpeg;
import com.airbnb.lottie.LottieAnimationView;
import com.daasuu.imagetovideo.EncodeListener;
import com.daasuu.imagetovideo.ImageToVideoConverter;
import com.video.trimmer.BuildConfig;
import com.video.trimmer.R;
import com.video.trimmer.databinding.ActivityImagesToVideoBinding;
import com.video.trimmer.utils.AudioExtractor;
import com.video.trimmer.utils.AudioToVideoMuxer;
import com.video.trimmer.utils.CheapSoundFile;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.DataFetcherClass;
import com.video.trimmer.utils.SharedPrefClass;
import com.video.trimmer.utils.Util;
import com.video.trimmer.videoTrimmer.interfaces.OnRangeSeekBarListener;
import com.video.trimmer.videoTrimmer.view.RangeSeekBarView;
import com.video.trimmer.videoTrimmer.view.Thumb;
//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.iceteck.silicompressorr.SiliCompressor;
import com.video.timeline.VideoTimeLine;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


//import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
//import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import com.video.trimmer.utils.TutorialAudioTrackToAacConvertor;


import org.jetbrains.annotations.NotNull;

import zeroonezero.android.audio_mixer.AudioMixer;
import zeroonezero.android.audio_mixer.input.AudioInput;
import zeroonezero.android.audio_mixer.input.GeneralAudioInput;


import static com.video.trimmer.utils.Constants.FOLDER_NAME_INTENT_KEY;
import static com.video.trimmer.utils.Constants.FROM_ADD_AUDIO_KEY;
import static com.video.trimmer.utils.Constants.LAUNCH_OR_NOT_KEY;
import static com.video.trimmer.utils.Constants.SILLI_TEMP_FOLDER_PATH;

import static com.video.trimmer.utils.Constants.TYPE_INTENT_KEY;


public class ImagesToVideoActivity extends AppCompatActivity {
    ActivityImagesToVideoBinding binding;
    ArrayList<String> images = new ArrayList<>();
    AlertDialog dialog2;
    AlertDialog.Builder alertDialog;
    View dialogView;
    ViewGroup viewGroup;
    ProgressBar pb;
    Button btnok;
    TextView title;
    TextView msg;
    String filePath;
    ArrayList<File> tmpFiles = new ArrayList<>();
    FileWriter writer;
    boolean largeVideo=false;
    SharedPrefClass sharedPrefClass;
    boolean isWriteSucessfull = true;
    boolean isCreatingVideo=true;
    boolean isVolumeVisible=false;
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == 1) {
                images = new ArrayList<>(ImagesActivity.selectedImages);
                Log.d("textdoc", images.size() + "");
                binding.relativeSelectImages.setVisibility(View.GONE);
                binding.constraintPlayer.setVisibility(View.VISIBLE);
                new LoadData2().execute();
            } else {
                Toast.makeText(ImagesToVideoActivity.this, "No Images Selected", Toast.LENGTH_SHORT).show();
            }
        }
    });
    ActivityResultLauncher<Intent> mGetContent2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
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
            }else{
                if(handler2!=null){
                    startservice2();
                }
            }
        }
    });
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagesToVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        File f2=new File(constants.TEMP_FOLDER_PATH);
        if(!f2.exists()){
            f2.mkdir();
        }
        File f3=new File(constants.CREATED_VIDEO_WITH_IMAGES_FOLDER_PATH);
        if(!f3.exists()){
            f3.mkdir();
        }
        sharedPrefClass = new SharedPrefClass(ImagesToVideoActivity.this);
        sharedPrefClass.saveAudioUriinShared(null);

        binding.toolbar.setTitle("");
        binding.toolbar.setNavigationIcon(R.drawable.ic_back_icon);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initComponents();
        binding.chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImagesToVideoActivity.this, ImagesActivity.class);
                mGetContent.launch(intent);
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
        binding.videoShow.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (playerVideo == null) {

                    playerVideo = mp;
                    float log1 = (float) (Math.log(100 - 50) / Math.log(100));
                    playerVideo.setVolume(1 - log1, 1 - log1);
                    binding.volumeSeekbarVideo.setProgress(50);

                    startpositionVideo = 0;
                    endPositionvideo = binding.videoShow.getDuration();
                    binding.handlerTop.setMax(binding.videoShow.getDuration());
                    binding.handlerTop.setProgress(0);
//                    binding.timeLineBar.initMaxWidth();
                    binding.videoDuration.setText(timeConversion(binding.videoShow.getCurrentPosition()).substring(1, timeConversion(binding.videoShow.getCurrentPosition()).length()) + "/" + timeConversion(binding.videoShow.getDuration()).substring(1, timeConversion(binding.videoShow.getDuration()).length()));
                    binding.videoStartTime.setText(timeConversion(startpositionVideo));
                    binding.videoEndTime.setText(timeConversion(endPositionvideo));
                    startserviceforVideo();
                } else {
                    playerVideo = mp;
                    binding.videoShow.seekTo(startpositionVideo);
                    binding.videoDuration.setText(timeConversion(binding.videoShow.getCurrentPosition()).substring(1, timeConversion(binding.videoShow.getCurrentPosition()).length()) + "/" + timeConversion(binding.videoShow.getDuration()).substring(1, timeConversion(binding.videoShow.getDuration()).length()));

                    // binding.videoView.start();
                }
            }
        });
        binding.videoShow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                binding.videoShow.seekTo(0);
                binding.videoShow.start();
                binding.videoDuration.setText(timeConversion(binding.videoShow.getCurrentPosition()) + "/" + timeConversion(binding.videoShow.getDuration()));
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
                    mp.stop();

                }
                Intent intent=new Intent(ImagesToVideoActivity.this,AudioActivity.class);
                intent.putExtra(FROM_ADD_AUDIO_KEY,true);
                mGetContent2.launch(intent);
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
                if(handler2!=null) {
                    handler2.removeCallbacks(runnable2);
                }
                if(playerVideo!=null && mp!=null){
                    if(playerVideo.isPlaying()){
                        playerVideo.pause();
                    }
                    if(sharedPrefClass.getAudioUrifromShared()!=null){
                        mp=null;
                    }

                    new LoadData().execute();
                }else if(playerVideo==null) {
                    Toast.makeText(ImagesToVideoActivity.this, "Kindly add video", Toast.LENGTH_SHORT).show();
                }else if(mp==null){
                    Toast.makeText(ImagesToVideoActivity.this, "Kindly add Audio", Toast.LENGTH_SHORT).show();
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

                        audioMixer.release();
                    }
                });
            }
        });
        audioMixer.start();
        audioMixer.processAsync();

    }
    AlertDialog.Builder alertDialog3;
    ViewGroup viewGroup3;
    View dialogView3;
    AlertDialog dialog4 = null;
    private void showLoadingProgress3(boolean state) {

        if(state) {
            alertDialog3 = new AlertDialog.Builder(ImagesToVideoActivity.this);
            viewGroup3 = findViewById(android.R.id.content);
            dialogView3 = LayoutInflater.from(ImagesToVideoActivity.this).inflate(R.layout.loading_dialog_new, viewGroup3, false);
            alertDialog3.setView(dialogView3);
            dialog4 = alertDialog3.create();
            dialog4.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog4.setCancelable(false);
            dialog4.show();
            TextView txtMsg=dialogView3.findViewById(R.id.txtdeleteinstr);
            txtMsg.setText(getResources().getString(R.string.creatingVideo));

        }else{
            dialog4.dismiss();
        }
    }

    Dialog dialog5;
    private class LoadData2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   showLoadingProgress3(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                createTempFileandVideo();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Task Completed";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



        }
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

            addAudiotoVideo();
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
    private void showLoadingProgress(boolean b) {
        if(b){
            dialog = new Dialog(ImagesToVideoActivity.this, R.style.Theme_VideoToAudioConverter);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_video_saving);
            dialog.show();

            pb = dialog.findViewById(R.id.progbar);
            btnok = dialog.findViewById(R.id.btndelyes);
            title = dialog.findViewById(R.id.dialogtitle);
            msg = dialog.findViewById(R.id.txtloadinginstr);

            title.setText(getResources().getString(R.string.creatingVideo));
            msg.setText(getResources().getString(R.string.creatingVideo));
        }else{
            dialog.dismiss();
            Dialog dialog = new Dialog(ImagesToVideoActivity.this, R.style.Theme_VideoToAudioConverter);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_video_saving);
            dialog.show();

            ProgressBar  pb=dialog.findViewById(R.id.progbar);
            Button btnok=dialog.findViewById(R.id.btnconfirmOk);
            TextView title=dialog.findViewById(R.id.dialogtitle);
            TextView msg=dialog.findViewById(R.id.txtloadinginstr);

            title.setText(getResources().getString(R.string.creatingVideo));
            msg.setText(getResources().getString(R.string.createdVideo));

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
                        tmpFiles=new DataFetcherClass(SILLI_TEMP_FOLDER_PATH).get_Recovery_Audios();
                        if(tmpFiles.size()!=0) {
                            for (File f : tmpFiles) {
                                f.delete();
                            }
                        }

                        Intent intent=new Intent(ImagesToVideoActivity.this,OutputsActivity.class);
                        intent.putExtra(TYPE_INTENT_KEY,6);
                        intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.videoCreatedFromImages));
                        intent.putExtra(LAUNCH_OR_NOT_KEY,true);
                        startActivity(intent);
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

    private void addAudiotoVideo() {


        File audioFile= null;
        try {
            audioFile = trimAudioFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(largeVideo){
            File newAACFile=new File(constants.TEMP_FOLDER_PATH+"AACTEMP"+System.currentTimeMillis()+".aac");
            new TutorialAudioTrackToAacConvertor().convert(audioFile.getPath(),newAACFile.getPath());

            File videoFile=new File(sharedPrefClass.getTempCreatedVideofromShared());
            File outputMixedFile=new File(constants.CREATED_VIDEO_WITH_IMAGES_FOLDER_PATH+"CreatedVideo"+System.currentTimeMillis()+".mp4");
            sharedPrefClass.saveOutputPathinShared(outputMixedFile.getPath());
            if(sharedPrefClass.getTempCreatedVideofromShared()!=null) {
                new AudioToVideoMuxer().mux(newAACFile.getPath(), videoFile.getAbsolutePath(), outputMixedFile.getPath());
            }
        }else {
            File videoFile=new File(sharedPrefClass.getTempCreatedVideofromShared());
            File outputMixedFile=new File(constants.CREATED_VIDEO_WITH_IMAGES_FOLDER_PATH+"CreatedVideo"+System.currentTimeMillis()+".mp4");
            sharedPrefClass.saveOutputPathinShared(outputMixedFile.getPath());
            if(sharedPrefClass.getTempCreatedVideofromShared()!=null) {
                new AudioToVideoMuxer().mux(audioFile.getPath(), videoFile.getAbsolutePath(), outputMixedFile.getPath());
            }
        }

    }
    private String appendVideos(ArrayList<String> paths)
    {
        try {
            Movie[] inMovies = new Movie[paths.size()];

            for (int i=0;i<paths.size();i++){
                inMovies[i] = MovieCreator.build(paths.get(i));
            }


            List<Track> videoTracks = new LinkedList<>();
//            List<Track> audioTracks = new LinkedList<>();

            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
//                    if (t.getHandler().equals("soun")) {
//                        audioTracks.add(t);
//                    }
                    if (t.getHandler().equals("vide")) {
                        videoTracks.add(t);
                    }
                }
            }

            Movie result = new Movie();

//            if (audioTracks.size() > 0) {
//                result.addTrack(new AppendTrack(audioTracks
//                        .toArray(new Track[audioTracks.size()])));
//            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack(videoTracks
                        .toArray(new Track[videoTracks.size()])));
            }

            BasicContainer out = (BasicContainer) new DefaultMp4Builder().build(result);

            @SuppressWarnings("resource")
            FileChannel fc = new RandomAccessFile(constants.TEMP_FOLDER_PATH + "wishbyvideo.mp4", "rw").getChannel();
            out.writeContainer(fc);
            fc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mFileName = constants.TEMP_FOLDER_PATH;
        mFileName += "wishbyvideo.mp4";
        return mFileName;
    }
    private File trimAudioFile() throws IOException {
        return convertAudiotoAAC();
    }
    private File convertAudiotoAAC() throws IOException {
        File infile=new File(sharedPrefClass.getAudioUrifromShared());
        File outFile2=new File(constants.TEMP_FOLDER_PATH+"Trimmed"+System.currentTimeMillis()+".mp3");
        if((sharedPrefClass.getAudioSizefromShared()/1024)/1000<=1.0){
            File outFile=new File(constants.TEMP_FOLDER_PATH+"TrimmedAuido"+System.currentTimeMillis()+".aac");
//            new TutorialAudioTrackToAacConvertor().convert(infile.getPath(),outFile.getPath());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
            bos.write(convert(infile.getPath()));
            bos.flush();
            bos.close();

            trimmedAudioDuration=endPosition-startPosition;

            if(trimmedAudioDuration<binding.videoShow.getDuration()){

                try {
                    new AudioExtractor().genVideoUsingMuxer(outFile.getPath(),outFile2.getPath(),Integer.parseInt(String.valueOf(startPosition)),Integer.parseInt(String.valueOf(endPosition)),true,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                endPosition=startPosition+(binding.videoShow.getDuration());
                try {
                    new AudioExtractor().genVideoUsingMuxer(outFile.getPath(),outFile2.getPath(),Integer.parseInt(String.valueOf(startPosition)),Integer.parseInt(String.valueOf(endPosition)),true,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else{
            largeVideo=true;
            final CheapSoundFile.ProgressListener listener = new CheapSoundFile.ProgressListener() {
                public boolean reportProgress(double frac) {
                    Log.d("progresstrimmingAuduio",frac+"");
                    return true;
                }
            };
            trimmedAudioDuration=endPosition-startPosition;
            if(trimmedAudioDuration<=binding.videoShow.getDuration()){
                CheapSoundFile cheapSoundFile= CheapSoundFile.create(infile.getPath(),listener);
                int mSampleRate = cheapSoundFile.getSampleRate();

                int mSamplesPerFrame = cheapSoundFile.getSamplesPerFrame();

                int startFrame = Util.secondsToFrames(startPosition/1000,mSampleRate, mSamplesPerFrame);

                int endFrame = Util.secondsToFrames(endPosition/1000, mSampleRate,mSamplesPerFrame);

                cheapSoundFile.WriteFile(outFile2, startFrame, endFrame-startFrame);


            }else{
                endPosition=startPosition+(binding.videoShow.getDuration());

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
    long trimmedVideoDuration,trimmedAudioDuration;

    private void initComponents() {
        sharedPrefClass.saveSummedVideoinShared(null);
        sharedPrefClass.saveTrimmedVideoPahtinShared(null);
        sharedPrefClass.saveTrimmedAudioPathinShared(null);
        sharedPrefClass.saveChangedVolumeAudioPathinShared(null);
        sharedPrefClass.saveChangedVolumeVideoPathinShared(null);
        sharedPrefClass.saveAudioUriinShared(null);
        sharedPrefClass.saveVideoDurationinShared(0);
        sharedPrefClass.saveAudioDurationinShared(0);
        sharedPrefClass.saveVideoVolumeinShared(50);
        sharedPrefClass.saveAudioVolumeinShared(50);
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

        binding.rangeseekbar4.setVisibility(View.VISIBLE);



        float log1 = (float) (Math.log(100-50)/Math.log(100));
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
                                if(progress>=endPosition || progress<startPosition){
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

    int j=0;
    ArrayList<String> paths=new ArrayList<>();
    public void createTempFileandVideo() throws IOException {


        List<Uri> imageUris=new LinkedList<>();

        for(int i=0;i<images.size();i++){
            imageUris.add( getFileUri(ImagesToVideoActivity.this,images.get(i)));
        }


            for (int i = 0; i < images.size(); i++) {

                String filePath = SiliCompressor.with(ImagesToVideoActivity.this).compress(String.valueOf(getFileUri(ImagesToVideoActivity.this,images.get(i))), new File(constants.TEMP_FOLDER_PATH));
                String body;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    body =  getPath(Uri.parse(filePath)) ;
                } else {
                    body =  filePath ;
                }
//            Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(), getFileUri(ImagesToVideoActivity.this,images.get(i)));
//
//            bitmap = Bitmap.createScaledBitmap(bitmap, 240, 320, true);

                File outFile = new File(constants.TEMP_FOLDER_PATH + "images2Video" + System.currentTimeMillis() + ".mp4");

//            sharedPrefClass.saveTempCreatedVideoinShared(outFile.getPath());
                paths.add(outFile.getPath());
                ImageToVideoConverter imageToVideo = new ImageToVideoConverter(outFile.getPath(), body, new EncodeListener() {
                    @Override
                    public void onProgress(float v) {
                        Log.d("errorcreatingVideo", v + "");
                    }

                    @Override
                    public void onCompleted() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (j == images.size() - 1) {
                                    sharedPrefClass.saveTempCreatedVideoinShared(appendVideos(paths));
                                    showLoadingProgress3(false);
                                    openVideo();
                                }
                                j++;

                            }
                        });
                    }

                    @Override
                    public void onFailed(@NotNull Exception e) {
                        showLoadingProgress3(false);
                        Log.d("errorcreatingVideo", e.toString());

                    }
                }, new Size(720, 720), TimeUnit.SECONDS.toMicros(1));
                imageToVideo.start();


//        new TimeLapseEncoder().encode(outFile.getPath(),imageUris,getContentResolver());

        }
    }
    FileOutputStream outputStream;
    File f;
    public File saveImage(Bitmap bitmap){
        f=null;
        File filepath = getFilesDir();
        File dir = new File(filepath.getAbsolutePath()+"/temp2/");
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dir,System.currentTimeMillis()+".png");
        try {
            outputStream = new FileOutputStream(file);
            f=file;
        } catch (FileNotFoundException e) {
            f=null;
            setResult(0);
            finish();
            e.printStackTrace();

        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try {
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            setResult(0);
            finish();
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(ImagesToVideoActivity.this,new String[]{file.getPath()},new String[] {"image/jpeg"},null);
        return f;
    }


    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    long startPosition, endPosition;
    int startpositionVideo, endPositionvideo;
    MediaPlayer playerVideo;

    private void openVideo() {
        binding.videoShow.setVideoURI(Uri.parse(sharedPrefClass.getTempCreatedVideofromShared()));

        VideoTimeLine.with(sharedPrefClass.getTempCreatedVideofromShared()).show(binding.fixedThumbList);
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

    Handler handler2, handler3;
    Runnable runnable2, runnable3;

    private final void startservice2() {
        handler2 = new Handler();
        runnable2 = new Runnable() {
            @Override
            public void run() {
                if(mp!=null) {
                    binding.pbarAudio4.setProgress(mp.getCurrentPosition());
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
                if(binding.videoShow.isPlaying()) {
                    binding.handlerTop.setProgress(binding.videoShow.getCurrentPosition());
                }
                handler3.postDelayed(runnable3, 1);
            }
        };
        handler3.postDelayed(runnable3, 0);

    }
    public Uri getFileUri(Context context, String fileName) {
        return FileProvider.getUriForFile(context,  BuildConfig.APPLICATION_ID+".fileprovider", new File(fileName));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sharedPrefClass.getAudioUrifromShared()!=null) {
            if (handler2 != null) {
                handler2.removeCallbacks(runnable2);
                handler2.postDelayed(runnable2, 0);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sharedPrefClass.getAudioUrifromShared()!=null){
            if (handler2 != null) {
                handler2.removeCallbacks(runnable2);
                handler2.postDelayed(runnable2, 0);
            }
            if(mp!=null){
                mp.release();
            }
        }
    }
}