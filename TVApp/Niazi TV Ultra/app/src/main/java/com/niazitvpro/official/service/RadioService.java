package com.niazitvpro.official.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RadioService extends Service {
    private static final String TAG = "BackgroundSoundService";
    public static MediaPlayer player;
    public static String url;
    public static SimpleExoPlayer exoPlayer;
    public static DefaultDataSourceFactory dataSourceFactory;
    private static boolean isRadioPlaying = false;
    public Messenger messageHandler;
    private Context context;

    public static synchronized SimpleExoPlayer getPlayerInstance(Context context) {

        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context);
            dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "exoPlayerSample"));
            return exoPlayer;
        } else
            return exoPlayer;

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return startId;
        }
        // Bundle extras = intent.getExtras();
        //  this.messageHandler = (Messenger) extras.get("MESSENGER");
        // url = extras.getString("URL");



        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

                isRadioPlaying = false;
                Toast.makeText(context, "Error in playing radio", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });


       /* player = new MediaPlayer();
        try {
            player.setDataSource(url);
        } catch (IOException e) {
            e.getMessage();
        }
        Log.i(TAG, "onCreate() , service started...");
        showProgress();
        player.prepareAsync();
        player.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                int duration = mediaPlayer.getDuration();
                player.start();
                RadioService.this.hideProgress();
            }
        });
        player.setOnSeekCompleteListener(new OnSeekCompleteListener() {
            public void onSeekComplete(MediaPlayer mp) {
            }
        });
        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                if (i == 100) {
                    RadioService.this.hideProgress();
                } else {
                    RadioService.this.showProgress();
                }
            }
        });
        player.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Message message = Message.obtain();
                message.arg1 = 103;
                try {
                    RadioService.this.messageHandler.send(message);
                } catch (RemoteException e) {
                    e.getMessage();
                }
            }
        });
        player.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });*/
        return START_STICKY;
    }

    public static void playRadio(String streamAudioUrl) {

        if (!exoPlayer.getPlayWhenReady()){
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(streamAudioUrl));
            exoPlayer.prepare(mediaSource);
        }
        exoPlayer.setPlayWhenReady(true);


    }

    public static void pauseRadio(){
        if (exoPlayer.getPlayWhenReady()){
            exoPlayer.setPlayWhenReady(false);
        }
    }

    public void hideProgress() {
        Message message = Message.obtain();
        message.arg1 = 101;
        try {
            this.messageHandler.send(message);
        } catch (RemoteException e) {
            e.getMessage();
        }
    }

    public void showProgress() {
        Message message = Message.obtain();
        message.arg1 = 102;
        try {
            this.messageHandler.send(message);
        } catch (RemoteException e) {
            e.getMessage();
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnBind()");
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        exoPlayer.release();
        exoPlayer = null;
        Log.i(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }
}
