package com.example.whatsappdatarecovery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.modelclass.GetImages;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMainAdapter extends RecyclerView.Adapter<RecyclerViewMainAdapter.RecyclerMainViewHolder> {
    View view;
    LayoutInflater inflater;
    Context context;
    ArrayList<GetImages> arrayList;
    private OnItemClick onItemClick;

    public RecyclerViewMainAdapter(Context context, ArrayList<GetImages> arrayList, OnItemClick onItemClick) {
        this.context = context;
        this.arrayList = arrayList;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public RecyclerMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.images_text_cardview, parent, false);
        return new RecyclerMainViewHolder(view, onItemClick);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerMainViewHolder holder, final int position) {
        holder.AllImages = arrayList.get(position);
        holder.image_cardview.setOnClickListener(null);
        holder.textView.setText(String.valueOf(arrayList.get(position).getName()));
        if (arrayList.get(position).isSelected()) {
            holder.imageView.setImageResource(arrayList.get(position).getCheckedImages());
            holder.textView.setTextColor(Color.parseColor("#FF000000"));

        } else {
            holder.textView.setTextColor(Color.parseColor("#E2E2E2"));
            holder.imageView.setImageResource(arrayList.get(position).getImages());

        }
        holder.image_cardview.setOnClickListener(v -> {
            arrayList.get(position).setSelected(true);
            //holder.imageView.setImageResource(arrayList.get(position).getCheckedImages());
            for (int i = 0; i < arrayList.size(); i++) {
                if (i != position) {
                    arrayList.get(i).setSelected(false);
                }
            }
            onItemClick.onItemClick(position);
            notifyDataSetChanged();
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class RecyclerMainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;
        GetImages AllImages;
        CardView image_cardview;
        OnItemClick onItemClick;

        public RecyclerMainViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            this.onItemClick = onItemClick;
            image_cardview = itemView.findViewById(R.id.image_cardview);
            imageView = itemView.findViewById(R.id.image_show);
            textView = itemView.findViewById(R.id.text_image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onItemClick.onItemClick(getAdapterPosition());
        }
    }
    public interface OnItemClick {
        void onItemClick(int position);
    }
}