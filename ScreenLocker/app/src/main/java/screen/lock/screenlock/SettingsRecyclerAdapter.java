package screen.lock.screenlock;

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


import java.util.ArrayList;

public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder> {

    ArrayList<SettingsModal> settingsModals=new ArrayList<SettingsModal>();
    Context context;
    OnItemClickListner listner;

    public SettingsRecyclerAdapter(ArrayList<SettingsModal> settingsModals, Context context) {
        this.settingsModals = settingsModals;
        this.context = context;
    }

    public  interface OnItemClickListner{
        void onItemClick(View v,int pos);
        void onCheckedchanged(boolean isCheck);
        void onFingerSwitchChanged(boolean ischeck);
    }
    public  void setOnItemClickListener(SettingsRecyclerAdapter.OnItemClickListner onItemClickListener){
        this.listner=onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_recycler_item, parent, false);
        SettingsRecyclerAdapter.ViewHolder viewHolder = new SettingsRecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsRecyclerAdapter.ViewHolder holder, int position) {
        //SettingsModal settingsModal=settingsModals.get(position);
        ImageView icon=holder.iconView;
        TextView primTxt=holder.primaryTxt;
        TextView secTxt=holder.secondaryTxt;
        Switch setSwitch=holder.aSwitch;

        Glide.with(context)
                .load(settingsModals.get(position).getIcon())
                .into(icon);
        primTxt.setText(settingsModals.get(position).getPrimaryText());
        secTxt.setText(settingsModals.get(position).getSecondaryText());
        if(settingsModals.get(position).isSwitchCheck()){
            setSwitch.setVisibility(View.VISIBLE);
            setSwitch.setChecked(settingsModals.get(position).isSwitchState());
        }
        setSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (position==0){
                    listner.onCheckedchanged(isChecked);
                }
                else if(position==1){
                    listner.onFingerSwitchChanged(isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingsModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconView;
        TextView primaryTxt,secondaryTxt;
        Switch aSwitch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView=(ImageView)itemView.findViewById(R.id.settingsItemIcon);
            primaryTxt=(TextView)itemView.findViewById(R.id.primaryText);
            secondaryTxt=(TextView)itemView.findViewById(R.id.secondaryText);
            aSwitch=(Switch)itemView.findViewById(R.id.switchSettingsItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n=getAdapterPosition();
                    if(listner!=null && n!=-1){
                        listner.onItemClick(v,n);
                    }
                }
            });

        }
    }
}
