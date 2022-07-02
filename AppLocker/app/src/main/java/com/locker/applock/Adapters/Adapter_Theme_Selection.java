package com.locker.applock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.locker.applock.Interfaces.ThemeSelectionInterface;
import com.locker.applock.Models.ThemeModel;
import com.locker.applock.R;

import java.util.ArrayList;

public class Adapter_Theme_Selection extends RecyclerView.Adapter<Adapter_Theme_Selection.ViewHolder> {

    private final ArrayList<ThemeModel> mThemesList;
    private final Context context;
    private final ThemeSelectionInterface themeSelectionInterface;

    public Adapter_Theme_Selection(ArrayList<ThemeModel> dataSet, Context context, ThemeSelectionInterface themeSelectionInterface) {
        mThemesList = dataSet;
        this.context = context;
        this.themeSelectionInterface = themeSelectionInterface;
    }

    @NonNull
    @Override
    public Adapter_Theme_Selection.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_theme_selection, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Theme_Selection.ViewHolder holder, int position) {
        ThemeModel currentItem = mThemesList.get(position);
        Glide.with(context).load(currentItem.getIcon()).into(holder.themeIcon);
        holder.selection.setVisibility(currentItem.getIsSelected());
    }

    @Override
    public int getItemCount() {
        return mThemesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView themeIcon, selection;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            themeIcon = itemView.findViewById(R.id.theme_preview);
            selection = itemView.findViewById(R.id.selection_overlay);

            themeIcon.setOnClickListener(v -> {
                if (getAdapterPosition() < mThemesList.size()) {
                    ThemeModel theme = mThemesList.get(getAdapterPosition());
                    themeSelectionInterface.OnThemeSelected(theme);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
