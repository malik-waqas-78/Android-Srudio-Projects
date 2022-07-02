package com.screen.mirror.Adapters.Videos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.screen.mirror.Activities.Videos.VideoPreviewActivity_OKRA;
import com.screen.mirror.R;

import java.util.ArrayList;

import static com.screen.mirror.Utils.Constants_CA.VIDEOS_LIST;
import static com.screen.mirror.Utils.Constants_CA.VIDEO_NUMBER;

public class GridVideosAdapter extends RecyclerView.Adapter<GridVideosAdapter.ViewHolder> {
    Context context;
    ArrayList<String> filesInFolder;

    public GridVideosAdapter(Context context, ArrayList<String> filesInFolder) {
        this.context = context;
        this.filesInFolder = filesInFolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_folder_media_preview, parent, false);
        return new GridVideosAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GridVideosAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(filesInFolder.get(position)).into(holder.iv_img);
    }

    @Override
    public int getItemCount() {
        return filesInFolder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv_img_back;
        ImageView iv_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_subfolder_image);
            cv_img_back = itemView.findViewById(R.id.img_back_card);
            cv_img_back.setOnClickListener(view -> {
                VIDEOS_LIST = filesInFolder;
                context.startActivity(new Intent(context, VideoPreviewActivity_OKRA.class)
                        .putExtra(VIDEO_NUMBER, getAdapterPosition()));
            });
        }
    }
}
