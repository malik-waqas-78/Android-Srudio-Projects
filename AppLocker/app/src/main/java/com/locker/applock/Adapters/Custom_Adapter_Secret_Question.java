package com.locker.applock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.locker.applock.Interfaces.Secret_Image_Interface;
import com.locker.applock.Models.Secret_Image_Item;
import com.locker.applock.R;

import java.util.ArrayList;

public class Custom_Adapter_Secret_Question extends RecyclerView.Adapter<Custom_Adapter_Secret_Question.ViewHolder> {
    private final ArrayList<Secret_Image_Item> mDataSet;
    private final Context context;
    private final Secret_Image_Interface secret_image_click_listener;

    public Custom_Adapter_Secret_Question(ArrayList<Secret_Image_Item> dataSet, Context context, Secret_Image_Interface SICL) {
        mDataSet = dataSet;
        this.context = context;
        secret_image_click_listener = SICL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_row_secret_img, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Secret_Image_Item item = mDataSet.get(position);
        Glide.with(context).load(item.getSecretImage()).placeholder(R.drawable.ic_secret_place_holder).into(holder.secretImg);
        holder.selectionImg.setVisibility(item.getSelected());
        holder.title.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView secretImg, selectionImg;
        TextView title;
        CardView card_secret_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            secretImg = itemView.findViewById(R.id.single_secret_image);
            selectionImg = itemView.findViewById(R.id.single_secret_image_selection);
            title = itemView.findViewById(R.id.title_secret_img);
            card_secret_img = itemView.findViewById(R.id.card_secret_img);

            card_secret_img.setOnClickListener(view -> {
                secret_image_click_listener.OnClick(mDataSet, getAdapterPosition());
            });
        }
    }
}
