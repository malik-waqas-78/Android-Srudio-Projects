package com.niazitvpro.official.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.agrawalsuneet.dotsloader.loaders.CircularDotsLoader;
import com.agrawalsuneet.dotsloader.loaders.LinearDotsLoader;
import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.RandomTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.MainActivity;
import com.niazitvpro.official.activity.SplashScreenActivity;
import com.niazitvpro.official.activity.VideoPlayerActivity;
import com.niazitvpro.official.adapter.SubCategoryChannelListAdapter;
import com.niazitvpro.official.app.MyApplication;
import com.niazitvpro.official.download.DemoDownloadService;
import com.niazitvpro.official.download.DownloadTracker;
import com.niazitvpro.official.download.TrackSelectionDialog;
import com.niazitvpro.official.exoplayer.YouTubeVideoInfoRetriever;
import com.niazitvpro.official.model.SubCategoryChannel;
import com.niazitvpro.official.service.Download_Manager;
import com.niazitvpro.official.utils.AdmobBannerAd;


// I added below New //
import com.niazitvpro.official.utils.AdmobIntrestrialAd;
//above new //

import com.niazitvpro.official.exoplayer.ExoVideoPlayer;
import com.niazitvpro.official.utils.HorizontalDottedProgress;
import com.niazitvpro.official.utils.ItemOffsetDecoration;
import com.niazitvpro.official.utils.ReadWriteJsonFileUtils;
import com.niazitvpro.official.utils.RecyclerTouchListener;
import com.niazitvpro.official.utils.RetrofitUtils;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.niazitvpro.official.utils.Constants.APP_BACKGROUND_COLOR;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelSubcategoryFragment extends Fragment implements View.OnClickListener, PlaybackPreparer, Player.EventListener, DownloadTracker.Listener {


    public static RecyclerView recycler_subChannelList;
    private RelativeLayout rl_videoView;
    private SubCategoryChannelListAdapter adapter;
    private SubCategoryChannel subCategoryChannel;
    private RelativeLayout ll_main;
    private SharedPrefTVApp sharedPrefTVApp;
    private int appBackgroundColor;
    public static PlayerView playerView;
    public static ImageView img_preview;
    private MediaController mediaController;
    private ProgressBar progressBar;
    public static ToggleButton img_FullScreenVideo;
    private TextView tv_showName;
    private RelativeLayout.LayoutParams lp;
    private RelativeLayout rlAdview;
    private String channelID;
    private String channelName;
    private int videoViewHight;
    private int screenHeight = 0;

    private ExoVideoPlayer exoVideoPlayer;
    public static SimpleExoPlayer exoPlayer;
    private String liveUrl,userAgent;
    private ProgressBar progressLoadApiData;
    TrackSelection.Factory trackSelectionFactory;
    private DefaultTrackSelector trackSelector;
    private TrackGroupArray lastSeenTrackGroupArray;

    private DownloadTracker downloadTracker;
    private ImageView imgQuality;
    private boolean isShowingTrackSelectionDialog;

    // new added //
    private static AdmobIntrestrialAd myObj;
    private BroadcastReceiver onComplete;
    // above new //

    public ChannelSubcategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_channel_subcategory, container, false);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onViewCreated(view, savedInstanceState);

        AdmobBannerAd admobBannerAd = new AdmobBannerAd();
        admobBannerAd.loadAddBanner(view,getActivity());

        MyApplication application = (MyApplication) getActivity().getApplication();
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;


        sharedPrefTVApp = new SharedPrefTVApp(getActivity());

        if (!sharedPrefTVApp.getString(APP_BACKGROUND_COLOR).isEmpty()) {

            appBackgroundColor = Color.parseColor(sharedPrefTVApp.getString(APP_BACKGROUND_COLOR));

        } else {

            appBackgroundColor = R.color.white;
        }

        ll_main = view.findViewById(R.id.ll_subcat_main);
        rlAdview = view.findViewById(R.id.rl_adview);
        ll_main.setBackgroundColor(appBackgroundColor);
        recycler_subChannelList = view.findViewById(R.id.category_subchannel_list);
        playerView = view.findViewById(R.id.videoview_subchannel);
        img_preview = view.findViewById(R.id.img_preview);
        progressBar = view.findViewById(R.id.progress_video);
        img_FullScreenVideo = view.findViewById(R.id.img_video_full_screen);
        tv_showName = view.findViewById(R.id.tv_show_name);
        rl_videoView = view.findViewById(R.id.rl_videoview);
        progressLoadApiData = view.findViewById(R.id.progress_load_data);
        imgQuality = view.findViewById(R.id.img_quality);

        imgQuality.setOnClickListener(this);

        lp = (RelativeLayout.LayoutParams) rl_videoView.getLayoutParams();
        lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height = (int) (screenHeight * 0.35);
        rl_videoView.setLayoutParams(lp);
        img_preview.setLayoutParams(lp);

        img_FullScreenVideo.setOnClickListener(this);

        channelID = getArguments().getString("channelID");
        channelName = getArguments().getString("channelName");

        tv_showName.setText(channelName);

        String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Channel"+channelID);
        if(getCacheResponse!=null && !getCacheResponse.equals("")){

            setRecycler_subChannelList(getCacheResponse);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        getSubCategoryChannelList(channelID);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            },100);

        }else {

            try {

                SharedPrefTVApp.runTimer(getActivity(),progressLoadApiData);
                progressLoadApiData.setVisibility(View.VISIBLE);
                getSubCategoryChannelList(channelID);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        initView();

        recycler_subChannelList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recycler_subChannelList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                AdmobIntrestrialAd.getInstance().loadIntertitialAds(getActivity());
                img_preview.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
                img_FullScreenVideo.setVisibility(View.VISIBLE);
                imgQuality.setVisibility(View.VISIBLE);
                liveUrl = subCategoryChannel.subCategoryChannelList.get(position).directLiveUrl;
                userAgent = "NiaziTv";

                exoVideoPlayer = new ExoVideoPlayer(getActivity(),exoPlayer,playerView,liveUrl,userAgent);
                Log.d("url===",liveUrl + "/////////" + userAgent);
                String urlExtention = exoVideoPlayer.getfileExtension(Uri.parse(liveUrl));

                if (liveUrl != null
                        && (liveUrl.contains("://youtu.be/") || liveUrl.contains("youtube.com/watch?v=")
                        ||liveUrl.contains("://www.youtube.com/embed/"))) {

                    liveUrl = new YouTubeVideoInfoRetriever().getYoutubeLink(liveUrl);
                    exoVideoPlayer = new ExoVideoPlayer(getActivity(),exoPlayer,playerView,liveUrl,userAgent);
                    createLeafMediaSource(Uri.parse(liveUrl),urlExtention);
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                } else {
                    createLeafMediaSource(Uri.parse(liveUrl),urlExtention);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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


    private void initView(){
        setTrack();
        exoPlayer = new SimpleExoPlayer.Builder(getActivity()).setTrackSelector(trackSelector).build();
        exoPlayer.addAnalyticsListener(new EventLogger(trackSelector));



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
//                    progressBar.startAnimation();
                    SharedPrefTVApp.runTimer(getActivity(),progressBar);
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    private void getSubCategoryChannelList(String channelID) throws JSONException {

        if(!SplashScreenActivity.isNetworkAvailable(getActivity())){

            String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Channel"+channelID);

            if(getCacheResponse!=null && !getCacheResponse.equals("")){

                setRecycler_subChannelList(getCacheResponse);

            }

        }else {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPi(getActivity()).getSubChannelsList(channelID);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        try {
                            String str_response = responseBody.string();

                            try {
                                new ReadWriteJsonFileUtils(getContext()).createJsonFileData("Channel"+channelID, str_response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            checkIfUpdateAVailable(str_response,channelID);
                            setRecycler_subChannelList(str_response);
                            progressLoadApiData.setVisibility(View.GONE);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressLoadApiData.setVisibility(View.GONE);
                    Toast.makeText(MyApplication.getAppContext(), "something went wrong!!!please check your connection", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    private  void setRecycler_subChannelList(String jsonResponse) {

        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(jsonResponse);
            String status = jsonObject.getString("status");

            if (jsonObject.has("msg")) {

                String message = jsonObject.getString("msg");

            } else {


            }

            if (status.equals("1")) {

                JSONArray data = jsonObject.getJSONArray("data");
                subCategoryChannel = new SubCategoryChannel(data);

                if (subCategoryChannel.subCategoryChannelList.get(0).channelImage.equals("")) {

                    if(adapter!=null){
                        adapter.swap(subCategoryChannel.subCategoryChannelList);
                    }else {
                        adapter = new SubCategoryChannelListAdapter(getActivity(), R.layout.subcategory_channel_item_list, subCategoryChannel.subCategoryChannelList, false,tv_showName.getText().toString());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recycler_subChannelList.setHasFixedSize(true);
                        recycler_subChannelList.setNestedScrollingEnabled(false);
                        recycler_subChannelList.setLayoutManager(linearLayoutManager);
                        recycler_subChannelList.setAdapter(adapter);
                        if (recycler_subChannelList.getItemDecorationCount() == 0) {

                            recycler_subChannelList.addItemDecoration(new ItemOffsetDecoration(1, 20, true));

                        }
                    }

                    adapter.notifyDataSetChanged();

                } else {

                    recycler_subChannelList.setVisibility(View.VISIBLE);

                    if(adapter!=null){
                        adapter.swap(subCategoryChannel.subCategoryChannelList);
                    }else {

                        adapter = new SubCategoryChannelListAdapter(getActivity(), R.layout.subcategory_channel_image_item_list, subCategoryChannel.subCategoryChannelList, true,tv_showName.getText().toString());
                        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
                        recycler_subChannelList.setHasFixedSize(true);
                        recycler_subChannelList.setNestedScrollingEnabled(false);
                        recycler_subChannelList.setLayoutManager(linearLayoutManager);
                        recycler_subChannelList.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recycler_subChannelList.setAdapter(adapter);
                            }
                        },100);
                        if (recycler_subChannelList.getItemDecorationCount() == 0) {

                            recycler_subChannelList.addItemDecoration(new ItemOffsetDecoration(2, 20, true));

                        }
                    }

                    adapter.notifyDataSetChanged();


                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
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


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onDetach() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         MainActivity.cardView.setVisibility(View.VISIBLE);

            if(exoPlayer!=null){
                releasePlayer();
            }

            super.onDetach();

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
        if (exoPlayer!=null && currentFragment instanceof ChannelSubcategoryFragment) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {

        if(v==img_FullScreenVideo){

            if(recycler_subChannelList.getVisibility()==View.VISIBLE){

                img_FullScreenVideo.setChecked(true);
                recycler_subChannelList.setVisibility(View.GONE);
                tv_showName.setVisibility(View.GONE);
                lp = (RelativeLayout.LayoutParams) rl_videoView.getLayoutParams();
                lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                rl_videoView.setLayoutParams(lp);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                MainActivity.cardView.setVisibility(View.GONE);
                ll_main.setBackgroundColor(Color.parseColor("#555555"));
                rlAdview.setVisibility(View.GONE);
                WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getActivity().getWindow().setAttributes(attrs);
                SharedPrefTVApp.hideSystemUI(getActivity());


            }else {

                img_FullScreenVideo.setChecked(false);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                recycler_subChannelList.setVisibility(View.VISIBLE);
                tv_showName.setVisibility(View.VISIBLE);
                lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                lp.height = (int) (screenHeight * 0.35);
                MainActivity.cardView.setVisibility(View.VISIBLE);
                lp.removeRule(RelativeLayout.CENTER_VERTICAL);
                ll_main.setBackgroundColor(appBackgroundColor);
                rlAdview.setVisibility(View.VISIBLE);
                WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getActivity().getWindow().setAttributes(attrs);
                SharedPrefTVApp.showSystemUI(getActivity());

            }

        }

        if(v == imgQuality
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
                throw new IllegalStateException("Unsupported type: " + type);
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


    @Override
    public void preparePlayback() {
        exoPlayer.retry();
    }

    @Override
    public void onDownloadsChanged() {

    }




}
