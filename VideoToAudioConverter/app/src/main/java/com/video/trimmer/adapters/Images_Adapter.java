package com.video.trimmer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.trimmer.R;
import com.video.trimmer.interfaces.OnOutputItemsLongClicked;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Images_Adapter extends RecyclerView.Adapter<Images_Adapter.ViewHolder> {
    ArrayList<String> images=new ArrayList<>();
    Context context;
    ArrayList<String> selected=new ArrayList<>();
    OnOutputItemsLongClicked onOutputItemsLongClicked;
    public void onImageClicked(OnOutputItemsLongClicked onOutputItemsLongClicked){
        this.onOutputItemsLongClicked=onOutputItemsLongClicked;
    }
    public Images_Adapter(ArrayList<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    public ArrayList<String> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<String> selected) {
        this.selected = selected;
    }

    @NonNull
    @NotNull
    @Override
    public Images_Adapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_folder_media_preview, parent, false);
        Images_Adapter.ViewHolder viewHolder = new Images_Adapter.ViewHolder (view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Images_Adapter.ViewHolder holder, int position) {
        if(selected.size()!=0 && selected.contains(images.get(position))){
            holder.relSelections.setVisibility(View.VISIBLE);
        }else{
            holder.relSelections.setVisibility(View.GONE);
        }
        Glide.with(context).load(images.get(position))
                .centerInside()
                .into(holder.imageView);

        holder.frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected.size()!=0 && selected.contains(images.get(position))){
                    holder.relSelections.setVisibility(View.GONE);
                    selected.remove(images.get(position));
                    if (selected.size() == 0) {
                        onOutputItemsLongClicked.onLongPressed(false);
                    }
                }else {
                    holder.relSelections.setVisibility(View.VISIBLE);
                    selected.add(images.get(position));
                    onOutputItemsLongClicked.onLongPressed(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relSelections;
        FrameLayout frame;
        ImageView imageView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            relSelections=itemView.findViewById(R.id.relSelected);
            frame=itemView.findViewById(R.id.frame);
            imageView=itemView.findViewById(R.id.imageHolder);

        }
    }
}
