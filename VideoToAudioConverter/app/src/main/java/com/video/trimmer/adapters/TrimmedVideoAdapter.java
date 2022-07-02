package com.video.trimmer.adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.trimmer.R;
import com.video.trimmer.interfaces.Checkallselected;
import com.video.trimmer.interfaces.OnOutputItemsLongClicked;
import com.video.trimmer.interfaces.OnVideoItemClicked;
import com.video.trimmer.interfaces.OutputItemShareDeleteClicked;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class TrimmedVideoAdapter extends RecyclerView.Adapter<TrimmedVideoAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<File> outputFiles=new ArrayList<>();
    ArrayList<File> outputFilesExamples=new ArrayList<>();
    OnVideoItemClicked onVideoItemClicked;
    OutputItemShareDeleteClicked outputItemShareDeleteClicked;
    ArrayList<String> selectedFiles=new ArrayList<>();
    OnOutputItemsLongClicked outputItemsLongClicked;
    Checkallselected checkallselected;

    public TrimmedVideoAdapter(Context context, ArrayList<File> outputFiles) {
        this.context = context;
        this.outputFiles = outputFiles;
    }

    public void onItemClicked(OnVideoItemClicked onVideoItemClicked){
        this.onVideoItemClicked=onVideoItemClicked;
    }
    public void onShareDeleteClicked(OutputItemShareDeleteClicked outputItemShareDeleteClicked){
        this.outputItemShareDeleteClicked=outputItemShareDeleteClicked;
    }
    public void onItemLongClicked(OnOutputItemsLongClicked onOutputItemsLongClicked){
        this.outputItemsLongClicked=onOutputItemsLongClicked;
    }
    public void onAllChecked(Checkallselected checkallselected){
        this.checkallselected=checkallselected;
    }

    public ArrayList<String> getSelectedFiles() {
        return selectedFiles;
    }

    public void setSelectedFiles(ArrayList<String> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }
    @NonNull
    @NotNull
    @Override
    public TrimmedVideoAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        TrimmedVideoAdapter.ViewHolder viewHolder = new TrimmedVideoAdapter.ViewHolder (view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TrimmedVideoAdapter.ViewHolder holder, int position) {
        if (selectedFiles.size() != 0 && selectedFiles.contains(outputFiles.get(position).getAbsolutePath())) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.constraintsharedelete.setVisibility(View.VISIBLE);
        if((outputFiles.get(position).length()/1024)/1000==0.0){
            float file_size = (outputFiles.get(position).length()/1024);
            holder.videoSize.setText(file_size+" KB");
        }else {
            float file_size = (outputFiles.get(position).length() / 1024) / 1000;
            holder.videoSize.setText(file_size+" MB");
        }
        holder.videoSize.setSelected(true);
        holder.videoName.setText(outputFiles.get(position).getName());
        String path=outputFiles.get(position).getAbsolutePath();
        holder.videoDuration.setText(timeConversion(getduration(path)));

        holder.videoName.setSelected(true);

//        Glide.with(context).load(outputFiles.get(position))
//                .skipMemoryCache(false)
//
//                .centerCrop()
//                .into(holder.videoIcon);


//        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(outputFiles.get(position).getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        Glide.with(context).load(outputFiles.get(position).getAbsolutePath())
                .skipMemoryCache(false)
                .placeholder(R.drawable.ic_video_player)
                .centerCrop()
                .into(holder.videoIcon);

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFiles.size() != 0 && selectedFiles.contains(outputFiles.get(position).getAbsolutePath())) {
                    holder.checkBox.setVisibility(View.GONE);
                    selectedFiles.remove(outputFiles.get(position).getAbsolutePath());
                    checkallselected.allselected(selectedFiles.size());
                    if (selectedFiles.size() == 0) {
                        outputItemsLongClicked.onLongPressed(false);
                    }
                } else if (selectedFiles.size() != 0 && !selectedFiles.contains(outputFiles.get(position).getAbsolutePath())) {
                    holder.checkBox.setVisibility(View.VISIBLE);
                    selectedFiles.add(outputFiles.get(position).getAbsolutePath());
                    checkallselected.allselected(selectedFiles.size());
                } else {
                    onVideoItemClicked.onItemClicked(outputFiles.get(position).getPath(),outputFiles.get(position).getName());
                }

            }
        });

        holder.frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (selectedFiles.size() == 0) {
                    outputItemsLongClicked.onLongPressed(true);
                    holder.checkBox.setVisibility(View.VISIBLE);
                    selectedFiles.add(outputFiles.get(position).getAbsolutePath());
                    checkallselected.allselected(selectedFiles.size());
                }
                return true;
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputItemShareDeleteClicked.onShareClicked(outputFiles.get(position).getAbsolutePath());
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputItemShareDeleteClicked.onDeleteClicked(outputFiles.get(position).getAbsolutePath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return outputFiles.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<File> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(outputFilesExamples);
                } else {
                    String filterPattern = constraint.toString();
                    for (File item : outputFilesExamples) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                outputFiles = filteredList;
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
        ConstraintLayout constraintsharedelete;
        ImageButton share;
        ImageButton delete;
        CheckBox checkBox;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            videoIcon=itemView.findViewById(R.id.videoIcon);
            videoDuration=itemView.findViewById(R.id.videoLength);
            videoName=itemView.findViewById(R.id.videoName);
            videoSize=itemView.findViewById(R.id.videoSize);
            frameLayout=itemView.findViewById(R.id.frame);
            constraintsharedelete=itemView.findViewById(R.id.constraintshareDel);
            share=itemView.findViewById(R.id.imgshare);
            delete=itemView.findViewById(R.id.imgDelete);
            checkBox=itemView.findViewById(R.id.checkbox);

        }
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
    public long getduration(String pathStr){
        long millSecond = 0;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, Uri.parse(pathStr));
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            millSecond = Integer.parseInt(duration);

        }catch (Exception e){
            millSecond=0;
        }
        return millSecond;

    }
}
