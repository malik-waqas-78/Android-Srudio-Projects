package com.video.trimmer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.trimmer.R;
import com.video.trimmer.interfaces.OnVideoItemClicked;
import com.video.trimmer.modelclasses.VideoModal;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class SubVideoAdapter extends RecyclerView.Adapter<SubVideoAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<VideoModal> subVideos=new ArrayList<>();
    ArrayList<VideoModal> subVideosExample=new ArrayList<>();
    OnVideoItemClicked onVideoItemClicked;
    public void onVideoItemClicked(OnVideoItemClicked onVideoItemClicked){
        this.onVideoItemClicked=onVideoItemClicked;
    }

    public SubVideoAdapter(Context context, ArrayList<VideoModal> subVideos) {
        this.context = context;
        this.subVideos = subVideos;
        subVideosExample=subVideos;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        SubVideoAdapter.ViewHolder viewHolder = new SubVideoAdapter.ViewHolder (view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubVideoAdapter.ViewHolder holder, int position) {
        File f=new File(subVideos.get(position).getOriginalPath());
        if((subVideos.get(position).getSize()/1024)/1000==0.0){
            float file_size = (subVideos.get(position).getSize()/1024);
            holder.videoSize.setText(file_size+" KB");
        }else {
            float file_size = (subVideos.get(position).getSize() / 1024) / 1000;
            holder.videoSize.setText(file_size+" MB");
        }
        holder.videoSize.setSelected(true);
        holder.videoName.setText(subVideos.get(position).getName());
        holder.videoDuration.setText(timeConversion(subVideos.get(position).getDuration()));
        holder.videoName.setSelected(true);
        String thumb= subVideos.get(position).getThumbnail();
//        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(f.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        Glide.with(context).load(thumb)
                .skipMemoryCache(false)
                .placeholder(R.drawable.ic_video_player)
                .centerCrop()
                .into(holder.videoIcon);

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVideoItemClicked.onItemClicked(subVideos.get(position).getOriginalPath(),subVideos.get(position).getName());
            }
        });
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
    @Override
    public int getItemCount() {
        return subVideos.size();
    }

    @Override
    public Filter getFilter() {
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<VideoModal> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(subVideosExample);
                } else {
                    String filterPattern = constraint.toString();
                    for (VideoModal item : subVideosExample) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                subVideos = filteredList;
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoIcon;
        TextView videoDuration,videoName,videoSize;
        FrameLayout frameLayout;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            videoIcon=itemView.findViewById(R.id.videoIcon);
            videoDuration=itemView.findViewById(R.id.videoLength);
            videoName=itemView.findViewById(R.id.videoName);
            videoSize=itemView.findViewById(R.id.videoSize);
            frameLayout=itemView.findViewById(R.id.frame);
        }
    }
}
