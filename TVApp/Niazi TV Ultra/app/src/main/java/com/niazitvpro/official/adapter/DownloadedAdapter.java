package com.niazitvpro.official.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAdView;
import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.VideoPlayerActivity;
import com.niazitvpro.official.utils.AdmobIntrestrialAd;
import com.niazitvpro.official.utils.NativeBannerAd;
import com.niazitvpro.official.utils.RoundRectCornerImageView;
import com.niazitvpro.official.utils.SharedPrefTVApp;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.niazitvpro.official.utils.Constants.FACEBOOK_NATIVE;

public class DownloadedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<File> list = new ArrayList<>();
    private int fileType = 0;
    private PopupWindow popupWindow;
    private int pos=0;
    public final int CONTENT_TYPE = 0;
    public final int AD_TYPE = 1;
    private SharedPrefTVApp sharedPrefTVApp;




    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public DownloadedAdapter(Activity context, List<File> list, int fileType){
        this.context = context;
        this.list = list;
        this.fileType = fileType;
        sharedPrefTVApp = new SharedPrefTVApp(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case CONTENT_TYPE:
                Log.e("TAG", "content type : ");
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloaded_list_item, parent, false);
                return new ItemViewHolder(view);

            case AD_TYPE:
                Log.e("TAG", "ad type : ");
                View nativeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_admob_layout, parent, false);
                return new NativeAd(nativeView);

            default:
                return  null;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType) {

            case CONTENT_TYPE:

                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

                String fileSize = formatSize(list.get(position).length());

                ((ItemViewHolder) holder).txt_videosize.setText(""+fileSize);

                try {

                    ((ItemViewHolder) holder).txt_videofile_name.setText(URLDecoder.decode(list.get(position).getName(), "UTF-8"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Glide.with(context)
                        .asBitmap()
                        .load(Uri.fromFile(new File(list.get(position).getAbsolutePath())))
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo)
                        .into(((ItemViewHolder) holder).video_thumbnail);

                ((ItemViewHolder) holder).ll_play_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("video_image_path",list.get(position).getAbsolutePath());
                        intent.putExtra("filename",list.get(position).getName());
                        context.startActivity(intent);
                        AdmobIntrestrialAd.getInstance().loadIntertitialAds(context);
                    }
                });



                break;

            case AD_TYPE:

                NativeAd nativeAdViewHolder = (NativeAd) holder;
                NativeBannerAd.loadNetiveAds(context,nativeAdViewHolder.nativeAdLayout);



                break;

        }


    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public RoundRectCornerImageView video_thumbnail;
        public TextView txt_videofile_name,txt_videosize;
        public LinearLayout ll_play_video;

        public ItemViewHolder(View itemView) {
            super(itemView);

            video_thumbnail = itemView.findViewById(R.id.imageView_downloaded_item);
            txt_videofile_name = itemView.findViewById(R.id.fileName_downloaded_item);
            txt_videosize = itemView.findViewById(R.id.fileSize_downloaded_item);
            ll_play_video = itemView.findViewById(R.id.ll_downloded_video);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (list.get(position) == null) {
            return AD_TYPE;
        } else {
            return CONTENT_TYPE;
        }

    }

    protected class NativeAd extends RecyclerView.ViewHolder {

        LinearLayout nativeAdLayout;
        View view;

        @SuppressLint("CutPasteId")
        public NativeAd(View itemView) {
            super(itemView);
            nativeAdLayout = itemView.findViewById(R.id.frame);
//            view = itemView.findViewById(R.id.view);

        }
    }


    @SuppressLint("ResourceAsColor")

    public static String formatSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat(" GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" MB");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }



}
