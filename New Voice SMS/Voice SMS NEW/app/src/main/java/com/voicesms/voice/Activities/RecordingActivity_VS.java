package com.voicesms.voice.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.voicesms.voice.R;
import com.voicesms.voice.Utils.Constants;
import com.voicesms.voice.databinding.ActivityRecordingBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RecordingActivity_VS extends AppCompatActivity {
    ActivityRecordingBinding binding;
    boolean running;
    MediaRecorder recorder;
    CountDownTimer timer;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

//        loadBannerAdd();
//        InterstitialAddsVoiceSMS.showAdd();

        setSupportActionBar(binding.toolbarRecordingActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.instructionStartRecording.setText(getString(R.string.tap_to_start));

        binding.toolbarRecordingActivity.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        running = false;
        long maxTime = 7200000; // 2 Hours
        timer = new CountDownTimer(maxTime, 1000) {
            @Override
            public void onTick(long timeInMilliSeconds) {
                if (timeInMilliSeconds < 0) {
                    stopRecording();
                    startActivity(new Intent(context, RecordingListActivity_VS.class));
                }
                long elapsed_time_long = maxTime - timeInMilliSeconds + 1000;
                @SuppressLint("DefaultLocale") String elapsed_time_string = String.format("%02d:%02d:%02d"
                        , TimeUnit.MILLISECONDS.toHours(elapsed_time_long)
                        , TimeUnit.MILLISECONDS.toMinutes(elapsed_time_long) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsed_time_long))
                        , TimeUnit.MILLISECONDS.toSeconds(elapsed_time_long) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed_time_long))
                );
                binding.countupText.setText(elapsed_time_string);
                @SuppressLint("DefaultLocale") String remaining_time_string = String.format("%02d:%02d:%02d"
                        , TimeUnit.MILLISECONDS.toHours(timeInMilliSeconds)
                        , TimeUnit.MILLISECONDS.toMinutes(timeInMilliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMilliSeconds))
                        , TimeUnit.MILLISECONDS.toSeconds(timeInMilliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMilliSeconds))
                );
                binding.countdownText.setText(remaining_time_string);
            }

            @Override
            public void onFinish() {
                stopRecording();
                startActivity(new Intent(context, RecordingListActivity_VS.class));
            }
        };

        binding.btnRecord.setOnClickListener(view -> {
            if (!running) {
                checkPermAndStartRecording();
            } else {
                stopRecording();
                startActivity(new Intent(context, RecordingListActivity_VS.class));
                binding.countdownText.setText(getString(R.string.time));
                binding.countupText.setText(getString(R.string.time));
            }
        });

        binding.btnHistory.setOnClickListener(view -> {
            if (!running) {
                startActivity(new Intent(this, RecordingListActivity_VS.class));
            } else {
                Toast.makeText(this, "Please finish current recordings first.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPermAndStartRecording() {
        if (isPermissionAllowed(Manifest.permission.RECORD_AUDIO)
                &&
                isPermissionAllowed(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                &&
                isPermissionAllowed(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            startRecording();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!isPermissionAllowed(Manifest.permission.RECORD_AUDIO)) {
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Constants.REQUEST_RECORD_AUDIO_PERMISSION);
                } else if (!isPermissionAllowed(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                } else if (!isPermissionAllowed(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case Constants.REQUEST_RECORD_AUDIO_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
                                break;
                            }
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                                showMessageOKCancel("You need voice allow access voice microphone voice record your audio",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, Constants.REQUEST_RECORD_AUDIO_PERMISSION);
                                            }
                                        });
                            }
                        }
                    }
                    break;
                }
                case Constants.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                                break;
                            }
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need voice allow write access voice storage voice save your audio",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, Constants.REQUEST_RECORD_AUDIO_PERMISSION);
                                            }
                                        });
                            }
                        }
                    }
                    break;
                }
                case Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkPermAndStartRecording();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need voice allow read access voice storage voice save your audio",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                                            }
                                        });
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_RECORD_AUDIO_PERMISSION: {
                if (isPermissionAllowed(Manifest.permission.RECORD_AUDIO)) {
                    if (!isPermissionAllowed(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        }
                    } else {
                        checkPermAndStartRecording();
                    }
                }
                break;
            }
            case Constants.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION: {
                if (isPermissionAllowed(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (!isPermissionAllowed(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                        }
                    } else {
                        checkPermAndStartRecording();
                    }
                }
                break;
            }
            case Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION: {
                checkPermAndStartRecording();
                break;
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(RecordingActivity_VS.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void startRecording() {
        // Record voice the external cache directory for visibility
        long time = System.currentTimeMillis();
        String fileName = new SimpleDateFormat("HmsS_dLLy").format(new Date(time));

        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/Recordings";
        File myDir = new File(root);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        fileName = root + "/" + fileName + ".mp3";

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        recorder.setAudioSamplingRate(16000);
        recorder.setOutputFile(fileName);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("Recorder", "prepare() failed");
        }
        recorder.start();
        running = true;
        binding.instructionStartRecording.setText(getString(R.string.tap_to_stop));
        binding.btnRecord.setImageResource(R.drawable.ic_stop_voice_record);
        binding.countdownText.setVisibility(View.VISIBLE);
        timer.start();
    }

    private void stopRecording() {
        if (recorder != null) {
            if (running) {
                try {
                    recorder.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            recorder.release();
        }
        recorder = null;
        running = false;
        binding.btnRecord.setImageResource(R.drawable.ic_mic_voice_record);
        binding.countdownText.setText(getString(R.string.time));
        binding.countupText.setText(getString(R.string.time));
        timer.cancel();
        binding.countdownText.setVisibility(View.INVISIBLE);
        binding.instructionStartRecording.setText(getString(R.string.tap_to_start));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecording();
    }

    public boolean isPermissionAllowed(String perm) {
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
    }

//    public void loadBannerAdd() {
//        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);
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
//                Log.d("TAG", "Ad loaded");
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
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
//        relativeLayout.addView(adView);
//    }
}