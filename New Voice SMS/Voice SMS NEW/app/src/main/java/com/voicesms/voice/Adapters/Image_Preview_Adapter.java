package com.voicesms.voice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.voicesms.voice.Models.Model_Class;
import com.voicesms.voice.R;

import java.util.ArrayList;

import static com.voicesms.voice.Utils.Constants.Files_Count;
import static com.voicesms.voice.Utils.Constants.files_list;

public class Image_Preview_Adapter extends RecyclerView.Adapter<Image_Preview_Adapter.ViewHolder> {
    private final ArrayList<Model_Class> mDataSet;
    private final Context context;


    public Image_Preview_Adapter(Context mContext) {
        this.context = mContext;
        mDataSet = files_list;
    }

    @NonNull
    @Override
    public Image_Preview_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_layout_image_preview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Image_Preview_Adapter.ViewHolder holder, int position) {
        Model_Class current_item = mDataSet.get(position);
        String stringUri = current_item.getFileUri();
        if (stringUri.endsWith(".pdf")) {
            Glide.with(context)
                    .load(R.drawable.ic_document_recycler_view)
                    .into(holder.imgPreview);
        } else {
            Glide.with(context)
                    .load(stringUri)
                    .into(holder.imgPreview);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPreview;
        public ImageView closeImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPreview = itemView.findViewById(R.id.imgView);
            closeImg = itemView.findViewById(R.id.small_cancel);

            closeImg.setOnClickListener(view -> {
                mDataSet.remove(getAdapterPosition());
                notifyDataSetChanged();
                files_list = mDataSet;
                Files_Count--;
            });
        }
    }
}
