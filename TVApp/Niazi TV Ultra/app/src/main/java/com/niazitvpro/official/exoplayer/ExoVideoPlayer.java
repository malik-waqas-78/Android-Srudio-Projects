package com.niazitvpro.official.exoplayer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.google.android.exoplayer2.util.EventLogger;
import com.niazitvpro.official.R;

import java.util.ArrayList;
import java.util.List;

public class ExoVideoPlayer {

    private static Activity activity;
    private static SimpleExoPlayer exoPlayer;
    private static PlayerView playerView;
    private static String liveUrl,userAgent;
    private static List<ExtractorMediaSource> mediaSources = new ArrayList<>();
    private static PlayerControlView playerControlView;

    public ExoVideoPlayer(Activity activity, SimpleExoPlayer exoPlayer, PlayerView playerView,String liveUrl,String userAgent){
        this.activity = activity;
        this.exoPlayer =exoPlayer;
        this.playerView =playerView;
        this.liveUrl =liveUrl;
        this.userAgent =userAgent;

        LoadControl loadControl = new DefaultLoadControl.Builder()
                .setAllocator(new DefaultAllocator(true, 16))
                .setBufferDurationsMs(VideoPlayerConfig.MIN_BUFFER_DURATION,
                        VideoPlayerConfig.MAX_BUFFER_DURATION,
                        VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                        VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER)
                .setTargetBufferBytes(-1)
                .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl();
    }

    public static void playNormalVideo()
    {

        DefaultHttpDataSourceFactory dataSourceFactory  = new DefaultHttpDataSourceFactory(userAgent,null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true );
        DefaultDataSourceFactory sourceFactory = new DefaultDataSourceFactory(
                activity,
                null /* listener */,
                dataSourceFactory
        );
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(activity, trackSelector);
        mediaSources.add(new ExtractorMediaSource.Factory(sourceFactory).createMediaSource(Uri.parse(liveUrl)));
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(mediaSources.toArray(new MediaSource[mediaSources.size()]));
        exoPlayer.prepare(concatenatingMediaSource);
        setPlayerView();
    }

    public static String getfileExtension(Uri uri)
    {
        String extension;
        ContentResolver contentResolver = activity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }

    public static void playHlsVideo()
    {
        DefaultHttpDataSourceFactory dataSourceFactory  = new DefaultHttpDataSourceFactory(userAgent,null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true );
        DefaultDataSourceFactory sourceFactory = new DefaultDataSourceFactory(
                activity,
                null /* listener */,
                dataSourceFactory
        );
        DefaultHlsExtractorFactory defaultHlsExtractorFactory =
                new DefaultHlsExtractorFactory(DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES,true);
        MediaSource videoSource = new HlsMediaSource.Factory(sourceFactory)
                .setExtractorFactory(defaultHlsExtractorFactory)
                .createMediaSource(Uri.parse(liveUrl));
        exoPlayer.prepare(videoSource);
        setPlayerView();
    }

    public static void playDashVideo()
    {
        DefaultHttpDataSourceFactory dataSourceFactory  = new DefaultHttpDataSourceFactory(userAgent,null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true );
        DefaultDataSourceFactory sourceFactory = new DefaultDataSourceFactory(
                activity,
                null /* listener */,
                dataSourceFactory
        );
        MediaSource dashSource = new  DashMediaSource.Factory(sourceFactory)
                .setDrmSessionManager(new DrmSessionManager<ExoMediaCrypto>() {
                    @Override
                    public boolean canAcquireSession(DrmInitData drmInitData) {
                        return false;
                    }

                    @Override
                    public DrmSession<ExoMediaCrypto> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Class<? extends ExoMediaCrypto> getExoMediaCryptoType(DrmInitData drmInitData) {
                        return null;
                    }
                })
                .createMediaSource(Uri.parse(liveUrl));
        exoPlayer.prepare(dashSource);
        setPlayerView();
    }

    public static void playProgressiveVideo()
    {
        if(liveUrl.contains("mp4")
        ||liveUrl.contains("M4A")||liveUrl.contains("FMP4")){
            DefaultHttpDataSourceFactory dataSourceFactory  = new DefaultHttpDataSourceFactory(userAgent,null,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                    true );
            DefaultDataSourceFactory sourceFactory = new DefaultDataSourceFactory(
                    activity,
                    null /* listener */,
                    dataSourceFactory
            );
            MediaSource dashSource = new  ProgressiveMediaSource.Factory(sourceFactory)
                    .setDrmSessionManager(new DrmSessionManager<ExoMediaCrypto>() {
                        @Override
                        public boolean canAcquireSession(DrmInitData drmInitData) {
                            return false;
                        }

                        @Override
                        public DrmSession<ExoMediaCrypto> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
                            return null;
                        }

                        @Nullable
                        @Override
                        public Class<? extends ExoMediaCrypto> getExoMediaCryptoType(DrmInitData drmInitData) {
                            return null;
                        }
                    })
                    .createMediaSource(Uri.parse(liveUrl));
            exoPlayer.prepare(dashSource);
            setPlayerView();
        }else {
            playHlsVideo();
        }

    }


    public static void playSSVideo()
    {
        DefaultHttpDataSourceFactory dataSourceFactory  = new DefaultHttpDataSourceFactory(userAgent,null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true );
        DefaultDataSourceFactory sourceFactory = new DefaultDataSourceFactory(
                activity,
                null /* listener */,
                dataSourceFactory
        );
        MediaSource SSSourceMedia = new SsMediaSource.Factory(sourceFactory)
                .setDrmSessionManager(new DrmSessionManager<ExoMediaCrypto>() {
                    @Override
                    public boolean canAcquireSession(DrmInitData drmInitData) {
                        return false;
                    }

                    @Override
                    public DrmSession<ExoMediaCrypto> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Class<? extends ExoMediaCrypto> getExoMediaCryptoType(DrmInitData drmInitData) {
                        return null;
                    }
                })
                .createMediaSource(Uri.parse(liveUrl));
        exoPlayer.prepare(SSSourceMedia);
        setPlayerView();
    }

    private static void setPlayerView(){
        playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.seekTo(0, 0);
        playerView.setControllerShowTimeoutMs(3000);

    }

    public static   void videoPlayErroPopup(Activity activity, String liveUrl,onClickDialog onClickDialog){
        AlertDialog.Builder builder  = new AlertDialog.Builder(activity);

        builder.setMessage("This Channel Stream has been Expired. Soon will be Fixed. Or Restart Niazi TV App!")
                .setCancelable(false)
                .setPositiveButton("Play in External Player", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                        if(exoPlayer!=null){
                            exoPlayer.retry();
                        }
                        try {

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(liveUrl));
                            activity.startActivity(browserIntent);

                        }catch (ActivityNotFoundException e){

                            e.printStackTrace();
                        }
                        onClickDialog.onPositiveButtonClick();
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        exoPlayer.retry();

                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Niazi TV Stream Expired!");
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"));
    }

    public interface onClickDialog{

        void onPositiveButtonClick();
    }

    private static class PlayerErrorMessageProvider implements ErrorMessageProvider<ExoPlaybackException> {

        @Override
        public Pair<Integer, String> getErrorMessage(ExoPlaybackException e) {
            String errorString = activity.getString(R.string.error_generic);
            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                Exception cause = e.getRendererException();
                if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                    // Special case for decoder initialization failures.
                    MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                            (MediaCodecRenderer.DecoderInitializationException) cause;
                    if (decoderInitializationException.codecInfo == null) {
                        if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                            errorString =activity.getString(R.string.error_querying_decoders);
                        } else if (decoderInitializationException.secureDecoderRequired) {
                            errorString =
                                    activity.getString(
                                            R.string.error_no_secure_decoder, decoderInitializationException.mimeType);
                        } else {
                            errorString =
                                    activity.getString(R.string.error_no_decoder, decoderInitializationException.mimeType);
                        }
                    } else {
                        errorString =
                                activity.getString(
                                        R.string.error_instantiating_decoder,
                                        decoderInitializationException.codecInfo.name);
                    }
                }
            }
            return Pair.create(0, errorString);
        }
    }
}
