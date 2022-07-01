package com.niazitvpro.official.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.RandomTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.JsonObject;
import com.niazitvpro.official.R;
import com.niazitvpro.official.ViewController.ViewController;
import com.niazitvpro.official.activity.MainActivity;
import com.niazitvpro.official.activity.VideoPlayerActivity;
import com.niazitvpro.official.app.MyApplication;
import com.niazitvpro.official.download.DemoDownloadService;
import com.niazitvpro.official.download.DownloadTracker;
import com.niazitvpro.official.download.TrackSelectionDialog;
import com.niazitvpro.official.exoplayer.ExoVideoPlayer;
import com.niazitvpro.official.exoplayer.YouTubeVideoInfoRetriever;
import com.niazitvpro.official.service.Download_Manager;
import com.niazitvpro.official.utils.AdmobBannerAd;
import com.niazitvpro.official.utils.HorizontalDottedProgress;
import com.niazitvpro.official.utils.SharedPrefTVApp;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllVideoPlayingFragment extends Fragment implements View.OnClickListener, PlaybackPreparer, DownloadTracker.Listener, PlayerControlView.VisibilityListener, Player.EventListener {

    private static final CookieManager DEFAULT_COOKIE_MANAGER;
    private static final String TAG = "AllVideoPlayingFragment";

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    public static PlayerView playerView;
    private String liveUrl,urlExtention;
    public static SimpleExoPlayer exoPlayer;
    private String userAgent;
    private ToggleButton imgFullscreen;
    private RelativeLayout rlADLayout;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private ExoVideoPlayer exoVideoPlayer;
    TrackSelection.Factory trackSelectionFactory;
    private DefaultTrackSelector trackSelector;
    private TrackGroupArray lastSeenTrackGroupArray;
    private boolean useExtensionRenderers;
    private DownloadTracker downloadTracker;
    private ImageView imgQuality;
    private boolean isShowingTrackSelectionDialog;

    public AllVideoPlayingFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Activity a = getActivity();
        if(!isVisibleToUser){
            if (exoPlayer!=null) {
                playerView.onPause();
                exoPlayer.setPlayWhenReady(false);
                if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }else{
                if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_video_playing, container, false);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onViewCreated(view, savedInstanceState);

        initView(view);

    }

    @Override
    public void onStart() {
        super.onStart();
        downloadTracker.addListener(this);
    }

    @Override
    public void onStop() {
        downloadTracker.removeListener(this);
        super.onStop();
    }

    private void initView(View view){

        AdmobBannerAd admobBannerAd = new AdmobBannerAd();
        admobBannerAd.loadAddBanner(view,getActivity());

        MyApplication application = (MyApplication) getActivity().getApplication();
        useExtensionRenderers = application.useExtensionRenderers();
        downloadTracker = application.getDownloadTracker();

        // Start the download service if it should be running but it's not currently.
        // Starting the service in the foreground causes notification flicker if there is no scheduled
        // action. Starting it in the background throws an exception if the app is in the background too
        // (e.g. if device screen is locked).
        try {
            DownloadService.start(getActivity(), DemoDownloadService.class);
        } catch (IllegalStateException e) {
            DownloadService.startForeground(getActivity(), DemoDownloadService.class);
        }

        playerView = view.findViewById(R.id.player_view);
        imgFullscreen = view.findViewById(R.id.img_full_screen);
        rlADLayout = view.findViewById(R.id.rl_ad_layout);
        progressBar = view.findViewById(R.id.video_progress);
        imgQuality = view.findViewById(R.id.img_quality);
        SharedPrefTVApp.runTimer(getActivity(),progressBar);
        progressBar.setVisibility(View.VISIBLE);
        imgFullscreen.setOnClickListener(this);
        imgQuality.setOnClickListener(this);
        setTrack();
        initPlayer();
        setPlayerView();
    }


    private void initPlayer(){

        exoPlayer = new SimpleExoPlayer.Builder(getActivity()).setTrackSelector(trackSelector).build();
        exoPlayer.addAnalyticsListener(new EventLogger(trackSelector));

        liveUrl = getArguments().getString("live_tv_show_url");
        userAgent = getArguments().getString("user_agent");

        Log.d("url===",liveUrl + "////" + userAgent);

        exoVideoPlayer = new ExoVideoPlayer(getActivity(),exoPlayer,playerView,liveUrl,userAgent);
         urlExtention = exoVideoPlayer.getfileExtension(Uri.parse(liveUrl));

        exoPlayer.addListener(new Player.EventListener() {

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d("type===", String.valueOf(error.getMessage()));
                String message = error.getMessage();
                if(message.contains("Unable to connect")){
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    exoVideoPlayer.videoPlayErroPopup(getActivity(), liveUrl, new ExoVideoPlayer.onClickDialog() {
                        @Override
                        public void onPositiveButtonClick() {
                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                        }
                    });
                }else {

                    if(exoPlayer!=null){
                        exoPlayer.retry();
                    }
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setPlayerView(){

        if (liveUrl != null
                && (liveUrl.contains("://youtu.be/") || liveUrl.contains("youtube.com/watch?v=")
                   ||liveUrl.contains("://www.youtube.com/embed/"))) {


            liveUrl = new YouTubeVideoInfoRetriever().getYoutubeLink(liveUrl);
            exoVideoPlayer = new ExoVideoPlayer(getActivity(),exoPlayer,playerView,liveUrl,userAgent);
            createLeafMediaSource(Uri.parse(liveUrl),urlExtention);
            progressBar.clearAnimation();
            progressBar.setVisibility(View.GONE);

        }else {
            createLeafMediaSource(Uri.parse(liveUrl),urlExtention);
//            exoVideoPlayer.playDashVideo();

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer!=null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.frame_home);
        if (exoPlayer!=null && currentFragment instanceof AllVideoPlayingFragment) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity.cardView.setVisibility(View.VISIBLE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getActivity().getWindow().setAttributes(attrs);
        if(exoPlayer!=null){
            releasePlayer();
        }
    }

    @Override
    public void onClick(View view) {

        if (view == imgFullscreen) {

            if (MainActivity.cardView.getVisibility() == View.VISIBLE) {
                MainActivity.cardView.setVisibility(View.GONE);
                rlADLayout.setVisibility(View.GONE);
                imgFullscreen.setChecked(true);
                WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getActivity().getWindow().setAttributes(attrs);
                SharedPrefTVApp.hideSystemUI(getActivity());


            } else {

                MainActivity.cardView.setVisibility(View.VISIBLE);
                rlADLayout.setVisibility(View.VISIBLE);
                imgFullscreen.setChecked(false);
                WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getActivity().getWindow().setAttributes(attrs);
                SharedPrefTVApp.showSystemUI(getActivity());

            }
        }

        if(view == imgQuality
                && !isShowingTrackSelectionDialog
                && TrackSelectionDialog.willHaveContent(trackSelector)) {
            isShowingTrackSelectionDialog = true;
            TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForTrackSelector(
                            trackSelector,
                            /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
            trackSelectionDialog.show(getChildFragmentManager(), /* tag= */ null);
        }

    }

    @Override
    public void preparePlayback() {
        exoPlayer.retry();
    }


    @Override
    public void onVisibilityChange(int visibility) {
    }

    private void createLeafMediaSource(Uri uri, String extension) {
        @C.ContentType int type = Util.inferContentType(uri, extension);
        switch (type) {
            case C.TYPE_DASH:
                exoVideoPlayer.playDashVideo();
                break;

            case C.TYPE_SS:
                exoVideoPlayer.playSSVideo();
                break;

            case C.TYPE_HLS:
                exoVideoPlayer.playHlsVideo();
                break;

            case C.TYPE_OTHER:
                exoVideoPlayer. playProgressiveVideo();
                break;


            default:
                break;
        }
    }


    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            playerView.setPlayer(null);
            exoPlayer.release();
            exoPlayer.stop();
            exoPlayer.seekTo(0);
            playerView=null;
            exoPlayer=null;
        }

    }

    private void setTrack(){
        trackSelectionFactory = new RandomTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(/* context= */ getContext(), trackSelectionFactory);
    }

    @Override
    @SuppressWarnings("ReferenceEquality")
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        if (trackGroups != lastSeenTrackGroupArray) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                        == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    Toast.makeText(getContext(), getString(R.string.error_unsupported_video), Toast.LENGTH_SHORT).show();
                }
                if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                        == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    Toast.makeText(getContext(), getString(R.string.error_unsupported_audio), Toast.LENGTH_SHORT).show();

                }
            }
            lastSeenTrackGroupArray = trackGroups;
        }
    }

    @Override
    public void onDownloadsChanged() {

    }

    private void startDownload(){
//        RenderersFactory renderersFactory =
//                ((MyApplication) getActivity().getApplication())
//                        .buildRenderersFactory(false);
//        downloadTracker.toggleDownload(
//                getChildFragmentManager(),
//                "video_niazitv",
//                Uri.parse("http://niazitv.tk:8080/Niazi-TV-Media/Pak-Dramas/Balveer-Return/Episode-5.mp4/index.m3u8?" +
//                        "token=ZStusWOU-3o-2dGsCR7t2ClHajHiqwy68AKab9fjp8TJ7YXl_Xb2H4GNTPEOK_fU"),
//                ".m3u8",
//                renderersFactory);
//        ViewController.startDownoad(getActivity(),new Random().nextInt()+".mp4","http://niazitv.tk:8080/Niazi-TV-Media/Pak-Dramas/Balveer-Return/Episode-5.mp4/index.m3u8?" +
//                "token=ZStusWOU-3o-2dGsCR7t2ClHajHiqwy68AKab9fjp8TJ7YXl_Xb2H4GNTPEOK_fU");

    }

    private void getFile(){
        String dirs = getContext().getCacheDir().getAbsolutePath();
        SimpleCache mCache = null;
        if (mCache == null) {
            String path = dirs + File.separator + "downloads";
            boolean isLocked = SimpleCache.isCacheFolderLocked(new File(path));
            if (!isLocked) {
                mCache = new SimpleCache(new File(path), new LeastRecentlyUsedCacheEvictor(10000));
            }
        }

        //            new ExtractorMediaSource.Factory(
//                new CacheDataSourceFactory(
//                        cache,
//                        new DefaultDataSourceFactory(getActivity(), userAgent))
//                .creatMediaSource(Uri.parse("http://niazitv.tk:8080/Niazi-TV-Media/Pak-Dramas/Balveer-Return/Episode-2.mp4/index.m3u8?token=ZStusWOU-3o-2dGsCR7t2MtLk1nQmbRiDeuJK3UM2rrvsf4QZ1dCZZKoP5IJjBjA"));

        CacheDataSourceFactory dataSourceFactory = new CacheDataSourceFactory(
                mCache, new DefaultDataSourceFactory(getActivity(), userAgent));
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource
                .Factory(dataSourceFactory)
                .createMediaSource(Uri.parse("http://niazitv.tk:8080/Niazi-TV-Media/Pak-Dramas/Balveer-Return/Episode-5.mp4/index.m3u8?" +
                        "token=ZStusWOU-3o-2dGsCR7t2ClHajHiqwy68AKab9fjp8TJ7YXl_Xb2H4GNTPEOK_fU"));
//        ConcatenatingMediaSource concatenatingMediaSource =  new ConcatenatingMediaSource();
//        concatenatingMediaSource.addMediaSource(mediaSource);
        exoPlayer.prepare(mediaSource);
        playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.seekTo(0, 0);
        playerView.setControllerShowTimeoutMs(3000);

    }


    private void initProgressBar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("wait to download video m3u8...");
    }


    private boolean havePermissionForWriteStorage() {

        //marshmallow runtime permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Log.d("Permission Allowed", "true");
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 950);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getFileSize(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        try {
            final HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("HEAD");
            final String lengthHeaderField = urlConnection.getHeaderField("content-length");
            Long result = lengthHeaderField == null ? null : Long.parseLong(lengthHeaderField);
            return result == null || result < 0L ? -1L : result;
        } catch (Exception ignored) {
        }
        return -1L;

    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}



