package com.screen.mirror.Adapters.Videos;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.screen.mirror.R;

import java.util.ArrayList;

public class VideosViewPagerAdapter extends PagerAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final ArrayList<String> videoList;
    private final int currentItem;
    private MediaController mediaController;
    private VideoView videoView;

    public VideosViewPagerAdapter(Context context, ArrayList<String> videoList, int item) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.videoList = videoList;
        this.currentItem = item;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        videoView.stopPlayback();
        Log.v("videoPlayer", "isPlaying :: " + videoView.isPlaying());
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View videoLayout = inflater.inflate(R.layout.sliding_videos_layout, view, false);
        assert videoLayout != null;
        videoView = (VideoView) videoLayout.findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse(videoList.get(position)));
        mediaController = new MediaController(context);
        videoView.setMediaController(mediaController);
        view.addView(videoLayout, 0);
        return videoLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup view, int position, @NonNull Object object) {
        super.setPrimaryItem(view, position, object);
    }
}
