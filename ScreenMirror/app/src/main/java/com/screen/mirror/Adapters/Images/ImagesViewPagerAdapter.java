package com.screen.mirror.Adapters.Images;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.screen.mirror.R;

import java.util.ArrayList;


public class ImagesViewPagerAdapter extends PagerAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final ArrayList<String> ImageList;

    public ImagesViewPagerAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.ImageList = imageList;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return ImageList.size();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_images_layout, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image_preview);
        Glide.with(context).load(ImageList.get(position)).into(imageView);
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

}
