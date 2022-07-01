package com.ash360.cool.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ash360.cool.Interfaces.Settings_Row_interface;
import com.ash360.cool.Models.Settings_Item;
import com.ash360.cool.R;
import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class Adapter_Settings extends RecyclerView.Adapter<Adapter_Settings.ViewHolder> {

    private final ArrayList<Settings_Item> mDataSet;
    private final Context context;
    private final Settings_Row_interface settings_row_interface;

    public Adapter_Settings(ArrayList<Settings_Item> dataSet, Context context, Settings_Row_interface settings_row_interface) {
        mDataSet = dataSet;
        this.context = context;
        this.settings_row_interface = settings_row_interface;
    }

    @NonNull
    @Override
    public Adapter_Settings.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_row_settings, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Settings.ViewHolder holder, int position) {
        Settings_Item currentItem = mDataSet.get(position);
        holder.rowTitle.setText(currentItem.getTitle());
        Glide.with(context).load(currentItem.getIcon()).into(holder.rowIcon);
        holder.rowSwitch.setVisibility(currentItem.getHasSwitch());
        holder.rowSwitch.setChecked(currentItem.isChecked());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void ItemClickListener(SwitchMaterial Switch, int position) {
        Settings_Item item = mDataSet.get(position);
        item.setChecked(!item.isChecked());
        Switch.setChecked(item.isChecked());
        settings_row_interface.OnItemSelection(position, item);
        notifyDataSetChanged();
    }

    private void ItemClickListener(int position) {
        Settings_Item item = mDataSet.get(position);
        item.setChecked(!item.isChecked());
        settings_row_interface.OnItemSelection(position, item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView row;
        public TextView rowTitle;
        public ImageView rowIcon;
        public SwitchMaterial rowSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.settings_row_card);
            rowTitle = itemView.findViewById(R.id.title_settings_row);
            rowSwitch = itemView.findViewById(R.id.switch_settings_row);
            rowIcon = itemView.findViewById(R.id.icon_settings_row);

            rowSwitch.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION && getAdapterPosition() < mDataSet.size()) {
                    ItemClickListener(rowSwitch, getAdapterPosition());
                }
            });

            row.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION && getAdapterPosition() < mDataSet.size()) {
                    ItemClickListener(getAdapterPosition());
                }
            });
        }
    }
}
