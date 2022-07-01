package com.niazitvpro.official.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.niazitvpro.official.R;
import com.niazitvpro.official.model.NotificationItem;

import java.util.ArrayList;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    ArrayList<String> isFavorite = new ArrayList<>();
    public List<NotificationItem> notificationItemList = new ArrayList<>();
    Activity context;


    public NotificationAdapter(Activity context, List<NotificationItem> notificationItems) {

        this.context = context;
        this.notificationItemList = notificationItems;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.notificationTitle.setText(notificationItemList.get(position).notificationTitle);
        holder.notificationTime.setText(notificationItemList.get(position).notificationTime);
        holder.notificationDetail.setText(notificationItemList.get(position).notificationDetail);


    }

    @Override
    public int getItemCount() {
        return notificationItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView notificationTitle,notificationDetail,notificationTime;
        LinearLayout ll_notification;


        public MyViewHolder(View view) {
            super(view);

            notificationTime = view.findViewById(R.id.tv_notification_time);


            notificationTitle = view.findViewById(R.id.tv_notification_title);
            notificationDetail = view.findViewById(R.id.tv_notification_detail);
            ll_notification = view.findViewById(R.id.ll_notification);


        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


}
