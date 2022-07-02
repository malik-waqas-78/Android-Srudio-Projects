package com.locker.applock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.locker.applock.Interfaces.SettingsRowInterface;
import com.locker.applock.Models.SettingsModel;
import com.locker.applock.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    private final ArrayList<SettingsModel> mSettingsList;
    private final Context context;
    private final SettingsRowInterface settingsRowInterface;

    public SettingsAdapter(ArrayList<SettingsModel> mSettingsList, Context context, SettingsRowInterface settings_row_interface) {
        this.mSettingsList = mSettingsList;
        this.context = context;
        this.settingsRowInterface = settings_row_interface;

    }

    @Override
    public SettingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.settings_model_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsAdapter.ViewHolder holder, int position) {
        SettingsModel currentItem = mSettingsList.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.desc.setText(currentItem.getDesc());
        holder.extra_text.setText(currentItem.getDesc());
        Glide.with(context).load(currentItem.getIcon()).into(holder.icon);
        holder.switch_row.setVisibility(currentItem.getHasSwitch());
        holder.switch_row.setChecked(currentItem.isChecked());
        holder.desc.setVisibility(invertVisibility(currentItem.isShouldShowExtraText()));
        holder.extra_text.setVisibility(currentItem.isShouldShowExtraText());
    }

    private int invertVisibility(int shouldShowExtraText) {
        return (shouldShowExtraText == VISIBLE) ? GONE : VISIBLE;
    }

    @Override
    public int getItemCount() {
        return mSettingsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, extra_text;
        ImageView icon;
        SwitchMaterial switch_row;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_settings_row);
            desc = itemView.findViewById(R.id.desc_settings_row);
            extra_text = itemView.findViewById(R.id.extra_text);
            icon = itemView.findViewById(R.id.icon_settings_row);
            switch_row = itemView.findViewById(R.id.switch_settings_row);

            itemView.setOnClickListener(view -> {
                SettingsModel item = mSettingsList.get(getAdapterPosition());
                item.setChecked(!item.isChecked());
                switch_row.setChecked(item.isChecked());
                settingsRowInterface.OnClick(getAdapterPosition());
                notifyDataSetChanged();
            });

            switch_row.setOnClickListener(view -> {
                SettingsModel item = mSettingsList.get(getAdapterPosition());
                item.setChecked(!item.isChecked());
                settingsRowInterface.OnClick(getAdapterPosition());
                notifyDataSetChanged();
            });

        }
    }
}
