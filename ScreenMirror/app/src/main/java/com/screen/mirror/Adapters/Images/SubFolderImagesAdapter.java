package com.screen.mirror.Adapters.Images;

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
import com.screen.mirror.Activities.Images.ImagePreviewActivity_OKRA;
import com.screen.mirror.R;

import java.util.ArrayList;

import static com.screen.mirror.Utils.Constants_CA.IMAGE_NUMBER;
import static com.screen.mirror.Utils.Constants_CA.IMAGES_LIST;


public class SubFolderImagesAdapter extends RecyclerView.Adapter<SubFolderImagesAdapter.ViewHolder> {
    Context context;
    ArrayList<String> filesInFolder;

    public SubFolderImagesAdapter(Context context, ArrayList<String> filesInFolder) {
        this.context = context;
        this.filesInFolder = filesInFolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mini_grid_folder_media_preview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
                IMAGES_LIST = filesInFolder;
                context.startActivity(new Intent(context, ImagePreviewActivity_OKRA.class)
                        .putExtra(IMAGE_NUMBER, getAdapterPosition()));
            });
        }
    }
}
