package com.video.trimmer.adapters;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.trimmer.R;
import com.video.trimmer.interfaces.OnVideoItemClicked;
import com.video.trimmer.modelclasses.VideoModal;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<VideoModal> videoModals=new ArrayList<>();
    ArrayList<VideoModal> videoModalsExample=new ArrayList<>();
    OnVideoItemClicked onVideoItemClicked;

    public VideosAdapter(Context context, ArrayList<VideoModal> videoModals) {
        this.context = context;
        this.videoModals = videoModals;
        videoModalsExample=videoModals;
    }

    public  void onVideoItemClicked(OnVideoItemClicked onVideoItemClicked){
        this.onVideoItemClicked=onVideoItemClicked;
    }

    @NonNull
    @NotNull
    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        VideosAdapter.ViewHolder viewHolder = new VideosAdapter.ViewHolder (view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VideosAdapter.ViewHolder holder, int position) {
        File f=new File(videoModals.get(position).getOriginalPath());
        if(videoModals.get(position).getDuration()==0){
            holder.videoSize.setText(context.getResources().getString(R.string.notAvailable));
        }else {
            if ((f.length() / 1024) / 1000 == 0.0) {
                float file_size = (f.length() / 1024);
                holder.videoSize.setText(file_size + " KB");
            } else {
                float file_size = (f.length() / 1024) / 1000;
                holder.videoSize.setText(file_size + " MB");
            }
        }
        holder.videoSize.setSelected(true);
        holder.videoName.setText(f.getName());
        holder.videoDuration.setText(timeConversion(videoModals.get(position).getDuration()));
        holder.videoName.setSelected(true);
        String thumb= videoModals.get(position).getThumbnail();
        //Bitmap bMap = ThumbnailUtils.createVideoThumbnail(f.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        Glide.with(context).load(thumb)

                .centerCrop()
                .placeholder(R.drawable.ic_video_player)
                .into(holder.videoIcon);



        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVideoItemClicked.onItemClicked(videoModals.get(position).getOriginalPath(),videoModals.get(position).getName());
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
    public long getduration(String pathStr){
        long millSecond=0;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, Uri.parse(pathStr));
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            millSecond = Integer.parseInt(duration);
        }catch (Exception e){
            MediaPlayer mp = MediaPlayer.create(context, Uri.parse(pathStr));
            if(mp!=null) {
                millSecond = mp.getDuration();
                mp.release();
            }

        }

        return millSecond;
    }

    public Uri getFileUri(Context context, String fileName) {
        return FileProvider.getUriForFile(context,  "com.example.videotoaudioconverter.fileprovider", new File(fileName));
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    @Override
    public int getItemCount() {
        return videoModals.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<VideoModal> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(videoModalsExample);
                } else {
                    String filterPattern = constraint.toString();
                    for (VideoModal item : videoModalsExample) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                videoModals = filteredList;
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
