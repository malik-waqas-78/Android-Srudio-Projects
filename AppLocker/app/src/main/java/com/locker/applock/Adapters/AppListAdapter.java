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
import com.locker.applock.Interfaces.AppClickInterface;
import com.locker.applock.Models.AppModel;
import com.locker.applock.R;
import com.locker.applock.Utils.SharedPrefHelper;

import java.util.ArrayList;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    ArrayList<AppModel> mApps;
    Context context;
    AppClickInterface appClickInterface;
    SharedPrefHelper sharedPrefHelper;

    public AppListAdapter(ArrayList<AppModel> mApps, Context context, AppClickInterface appClickInterface) {
        this.mApps = mApps;
        this.context = context;
        this.appClickInterface = appClickInterface;
        sharedPrefHelper = new SharedPrefHelper(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_app_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppListAdapter.ViewHolder holder, int position) {
        AppModel app = mApps.get(position);

        Glide.with(context).load(app.getIcon()).into(holder.appIcon);
        holder.appName.setText(app.getName());
        holder.unlock.setVisibility(app.getIsLocked());
        holder.lock.setVisibility(invertLock(app.getIsLocked()));

    }

    private int invertLock(int lockStatus) {
        return (int) Math.sqrt(Math.pow(lockStatus - 4, 2));
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public void filterList(ArrayList<AppModel> filteredList) {
        mApps = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView appIcon;
        TextView appName;
        TextView appDescription;
        ImageView lock;
        ImageView unlock;
        CardView row_app; // whole row selection

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
            appDescription = itemView.findViewById(R.id.app_description);
            lock = itemView.findViewById(R.id.ic_lock);
            unlock = itemView.findViewById(R.id.ic_unlock);
            row_app = itemView.findViewById(R.id.app_row_card_view);

            lock.setOnClickListener(this);
            unlock.setOnClickListener(this);
            row_app.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mApps != null && mApps.size() > 0
                    && getAdapterPosition() != RecyclerView.NO_POSITION
                    && getAdapterPosition() < mApps.size()) {
                AppModel app = mApps.get(getAdapterPosition());
                app.setIsLocked(invertLock(app.getIsLocked()));
                lock.setVisibility(View.VISIBLE);
                unlock.setVisibility(View.INVISIBLE);
                appClickInterface.OnClick(app, getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }
}
