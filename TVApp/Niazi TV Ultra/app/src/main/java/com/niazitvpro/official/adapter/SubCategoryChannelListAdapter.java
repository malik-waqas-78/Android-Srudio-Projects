package com.niazitvpro.official.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.niazitvpro.official.R;
import com.niazitvpro.official.model.MenuItemChannel;
import com.niazitvpro.official.model.SubCategoryChannel;
import com.niazitvpro.official.service.Download_Manager;
import com.niazitvpro.official.utils.RoundRectCornerImageView;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import java.util.ArrayList;
import java.util.List;


public class SubCategoryChannelListAdapter extends RecyclerView.Adapter<SubCategoryChannelListAdapter.MyViewHolder> {

    public List<SubCategoryChannel> subCategoryChannelList = new ArrayList<>();
    Activity context;
    private MediaController mediaController;
    private boolean isImageAvailable;
    private int resourceID;
    private int selectedItem =-1;
    private SharedPrefTVApp prefTVApp;
    private String showName;


    public SubCategoryChannelListAdapter(Activity context,int resourceId, List<SubCategoryChannel> subCategoryChannelList,boolean isImageAvailable,String showName) {

        if(context!=null){

            this.context = context;
            this.subCategoryChannelList = subCategoryChannelList;
            this.isImageAvailable = isImageAvailable;
            this.resourceID = resourceId;
            this.showName = showName;
            prefTVApp = new SharedPrefTVApp(context);

        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resourceID, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvChannelName.setText(subCategoryChannelList.get(position).channelName);

        if(isImageAvailable){

            holder.img_channelORShow.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .asBitmap()
                    .load(subCategoryChannelList.get(position).channelImage)
                    .placeholder(R.color.black)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.img_channelORShow);

        }else {

            holder.img_channelORShow.setVisibility(View.GONE);
        }

        if(subCategoryChannelList.get(position).isVideoDownload.equals("1")){
            holder.imgDownload.setVisibility(View.VISIBLE);
        }else {
            holder.imgDownload.setVisibility(View.GONE);
        }

        holder.llChannelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem=position;
                notifyDataSetChanged();

            }

        });

        holder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName =  showName + "_" +subCategoryChannelList.get(position).channelName + ".mp4";
                Download_Manager.getInstance().setDownloadPopupDialog(context,subCategoryChannelList.get(position).directLiveUrl,fileName, Integer.parseInt(subCategoryChannelList.get(position).channelID));
            }
        });

        if(selectedItem==position){
            holder.llChannelLayout.setBackgroundColor(Color.BLUE);
            holder.tvChannelName.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.llChannelLayout.setBackgroundColor(Color.WHITE);
            holder.tvChannelName.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return subCategoryChannelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvChannelName;
        CardView ll_subchannelLayout;
        ImageView img_channelORShow,imgDownload;
        LinearLayout llChannelLayout;

        public MyViewHolder(View view) {
            super(view);

            tvChannelName = view.findViewById(R.id.tv_channel_name);
            ll_subchannelLayout = view.findViewById(R.id.ll_subchannel_layout);
            img_channelORShow = view.findViewById(R.id.img_channel_show);
            llChannelLayout = view.findViewById(R.id.ll_channel_layout);
            imgDownload = view.findViewById(R.id.img_download);

        }
    }

    public void swap(List<SubCategoryChannel> subCategoryChannels) {
        List<SubCategoryChannel> newList = subCategoryChannels;
        List<SubCategoryChannel> oldList = this.subCategoryChannelList;

        DiffUtil.Callback diffCallback = new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition) == newList.get(oldItemPosition);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition) == newList.get(oldItemPosition);
            }
        };
        DiffUtil.DiffResult  diffResult = DiffUtil.calculateDiff(diffCallback);

        this.subCategoryChannelList.clear();
        this.subCategoryChannelList.addAll(subCategoryChannels);
        diffResult.dispatchUpdatesTo(this);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


}
