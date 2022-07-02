package com.screen.mirror.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.screen.mirror.Interfaces.Settings_Row_Click_Listener;
import com.screen.mirror.Models.SettingsItem;
import com.screen.mirror.R;

import java.util.ArrayList;


public class Settings_Adapter extends RecyclerView.Adapter<Settings_Adapter.ViewHolder> {

    private final ArrayList<SettingsItem> mDataSet;
    private final Context context;
    private final Settings_Row_Click_Listener settings_row_click_listener;

    public Settings_Adapter(ArrayList<SettingsItem> dataSet, Context context, Settings_Row_Click_Listener SRCL) {
        mDataSet = dataSet;
        this.context = context;
        settings_row_click_listener = SRCL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_row_settings, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingsItem currentItem = mDataSet.get(position);
        holder.title.setText(currentItem.getName());
        Glide.with(context).load(currentItem.getIcon()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        ConstraintLayout card_back;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.settings_name);
            title.setSelected(true);
            icon = itemView.findViewById(R.id.settings_icon);
            card_back = itemView.findViewById(R.id.settings_item_back);

            card_back.setOnClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    settings_row_click_listener.OnClick(getAdapterPosition());
                }
            });
        }
    }
}
