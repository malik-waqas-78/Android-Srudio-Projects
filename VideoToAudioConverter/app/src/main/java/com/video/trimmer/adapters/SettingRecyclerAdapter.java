package com.video.trimmer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.trimmer.R;
import com.video.trimmer.interfaces.SettingItemClickInterface;
import com.video.trimmer.modelclasses.SettingsModaltems;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<SettingsModaltems> settingsModaltems=new ArrayList<SettingsModaltems>();
    SettingItemClickInterface settingItemClickInterface;

    public SettingRecyclerAdapter(Context context, ArrayList<SettingsModaltems> settingsModaltems) {
        this.context = context;
        this.settingsModaltems = settingsModaltems;
        settingItemClickInterface=(SettingItemClickInterface)context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SettingRecyclerAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(settingsModaltems.get(position).getImageId())
                .placeholder(R.drawable.exo_ic_settings)
                .fitCenter()
                .into(holder.imageView);
        holder.textView.setText(settingsModaltems.get(position).getName());
        if(settingsModaltems.get(position).isSwitchEnable()){
            holder.aSwitch.setVisibility(View.VISIBLE);
            holder.aSwitch.setChecked(settingsModaltems.get(position).isSwitchState());
        }
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(position==1) {
                    settingItemClickInterface.OnThemeSwitchStateChange(b);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingsModaltems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        Switch aSwitch;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.settingsImageIcon);
            textView=itemView.findViewById(R.id.settingName);
            aSwitch=itemView.findViewById(R.id.settingSwitch);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int num=getAdapterPosition();
                    if(settingItemClickInterface!=null && num!=-1){
                        settingItemClickInterface.OnItemClick(num);
                    }
                }
            });
        }
    }
}
