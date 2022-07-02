package com.video.trimmer.adapters;

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
import com.video.trimmer.R;
import com.video.trimmer.interfaces.LocaleSelectionInterface;
import com.video.trimmer.modelclasses.LocaleModel;


import java.util.ArrayList;

public class LocaleAdapter extends RecyclerView.Adapter<LocaleAdapter.ViewHolder> {
    private final ArrayList<LocaleModel> localeList;
    private final Context context;
    private final LocaleSelectionInterface callbacks;

    public LocaleAdapter(ArrayList<LocaleModel> localeList, Context context, LocaleSelectionInterface localeSelectionInterface) {
        this.localeList = localeList;
        this.context = context;
        callbacks = localeSelectionInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_lang_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocaleModel model = localeList.get(position);

        Glide.with(context).load(model.getIcon()).into(holder.icon);
        holder.name.setText(model.getLocaleName());
    }

    @Override
    public int getItemCount() {
        return localeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView locale_card_back;
        ImageView icon;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            locale_card_back = itemView.findViewById(R.id.locale_card_view);
            icon = itemView.findViewById(R.id.localeIcon);
            name = itemView.findViewById(R.id.tv_locale_name);

            locale_card_back.setOnClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    callbacks.OnClick(getAdapterPosition());
                }
            });
        }
    }
}
