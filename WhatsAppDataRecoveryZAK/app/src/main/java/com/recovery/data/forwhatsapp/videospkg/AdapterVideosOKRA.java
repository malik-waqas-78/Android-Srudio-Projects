package com.recovery.data.forwhatsapp.videospkg;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.bumptech.glide.Glide;
import com.recovery.data.forwhatsapp.activities.Videos_ActivityOKRA;
import com.recovery.data.forwhatsapp.activities.ActivitySeeMoreVideosOKRA;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.media.ThumbnailUtils.createImageThumbnail;
import static android.media.ThumbnailUtils.createVideoThumbnail;

public class AdapterVideosOKRA extends RecyclerView.Adapter implements Filterable {

    private static final String TAG = "92727";

    ArrayList<File> video_Files;
    ArrayList<File> example_video_Files;
    Context context;
    ArrayList<String> selectedRows = new ArrayList<>();
    LongClickInterfaceOKRA itemLongClickListeners;

    public void setVideos_url_interface(VideosUrlInterfaceOKRA videos_url_interface) {
        this.videos_url_interface = videos_url_interface;
    }

    VideosUrlInterfaceOKRA videos_url_interface;

    public AdapterVideosOKRA(ArrayList<File> video_files, Context context) {
        this.video_Files = video_files;
        this.context = context;
        example_video_Files=video_files;
    }

    public void setLongClickListeners(Videos_ActivityOKRA videos_activity) {
        this.itemLongClickListeners = videos_activity;
    }
    public void setLongClickListenersSeeMore(ActivitySeeMoreVideosOKRA videos_activity) {
        this.itemLongClickListeners = videos_activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_videos_okra, parent, false);
        return new Adapter_ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_ViewHolder row_view = (Adapter_ViewHolder) holder;
        // Log.d(TAG, "onBindViewHolder: "+video_Files.size());
        File videoFile = video_Files.get(position);
        if (selectedRows.size() != 0 && selectedRows.contains(videoFile.getAbsolutePath())) {
            //row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
            row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
            row_view.dot.setVisibility(View.VISIBLE);
        } else {
            row_view.frameLayout.setBackgroundColor(Color.TRANSPARENT);
            row_view.dot.setBackgroundResource(R.drawable.ic_unchek_icon);
            row_view.dot.setVisibility(View.GONE);
        }
        Log.d(TAG, "onBindViewHolder: viewing :: " + videoFile.getName());
        row_view.videoName.setText(videoFile.getName());
        row_view.videoName.setSelected(true);
        if((videoFile.length()/1024)/1000==0.0){
            float file_size = (videoFile.length()/1024);
            row_view.videoSize.setText(file_size+" KB");
        }else {
            float file_size = (videoFile.length() / 1024) / 1000;
            row_view.videoSize.setText(file_size+" MB");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(videoFile.lastModified()));
        row_view.dateText.setText(dateString+"");

        Glide.with(context).load(Uri.fromFile(videoFile)).into(row_view.imageView);
        row_view.imageView.setOnClickListener(view -> videos_url_interface.setUrlandLaunchInterface(videoFile.getAbsolutePath(),videoFile.getName()));
//        row_view.imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//               // videos_url_interface.onLongClick(videoFile.getAbsolutePath(),position);
//                return true;
//            }
//        });
        row_view.frameLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (selectedRows.size() != 0 && selectedRows.contains(videoFile.getAbsolutePath())) {
                    row_view.frameLayout.setBackgroundColor(Color.TRANSPARENT);
                    row_view.dot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    row_view.dot.setVisibility(View.GONE);
                    selectedRows.remove(videoFile.getAbsolutePath());
                    if (selectedRows.size() == 0) {
                        itemLongClickListeners.onLongClicked(false);
                    }
                } else if (selectedRows.size() != 0 && !selectedRows.contains(videoFile.getAbsolutePath())) {
                    //row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
                    row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
                    row_view.dot.setVisibility(View.VISIBLE);
                    selectedRows.add(videoFile.getAbsolutePath());
                } else {
                    videos_url_interface.setUrlandLaunchInterface(videoFile.getAbsolutePath(),videoFile.getName());
                }
            }
        });
        row_view.frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View view) {
                if (selectedRows.size() == 0) {
                    //row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
                    row_view.dot.setVisibility(View.VISIBLE);
                    row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
                    selectedRows.add(videoFile.getAbsolutePath());
                    itemLongClickListeners.onLongClicked(true);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return video_Files.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public ArrayList<String> getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(ArrayList<String> selectedRows) {
        this.selectedRows = selectedRows;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<File> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(example_video_Files);
                } else {
                    String filterPattern = constraint.toString();
                    for (File item : example_video_Files) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                video_Files = filteredList;
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

    private class Adapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView videoName;
        ImageView imageView;
        FrameLayout frameLayout;
        TextView videoSize;
        TextView dateText;
        ImageView dot;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoName=itemView.findViewById(R.id.videoName);
            imageView = itemView.findViewById(R.id.imagerecovered);
            frameLayout = itemView.findViewById(R.id.framelayout);
            videoSize=itemView.findViewById(R.id.videosize);
            dateText=itemView.findViewById(R.id.dateText);
            dot=itemView.findViewById(R.id.dotselect);
        }
    }
}
