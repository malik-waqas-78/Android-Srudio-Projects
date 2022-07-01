package com.example.hissabbookapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hissabbookapp.R;
import com.example.hissabbookapp.modalclasses.SubRecordData;

import java.util.ArrayList;

public class SubRecordAdapter extends RecyclerView.Adapter<SubRecordAdapter.ViewHolder> {
    Context context;
    ArrayList<SubRecordData> subRecordData=new ArrayList<SubRecordData>();
    String id;
    public SubRecordAdapter(Context context, ArrayList<SubRecordData> subRecordData) {
        this.context = context;
        this.subRecordData = subRecordData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_record_item, parent, false);
        SubRecordAdapter.ViewHolder viewHolder = new SubRecordAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubRecordAdapter.ViewHolder holder, int position) {
        holder.amount.setText(subRecordData.get(position).getTotalBalance());
        holder.remarks.setText(subRecordData.get(position).getRemark());
        holder.time.setText(subRecordData.get(position).getTime());
        //holder.totalBalance.setText("Balance: "+subRecordData.get(position).getBalance());
        id=subRecordData.get(position).getId();

    }

    @Override
    public int getItemCount() {
        return subRecordData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount,remarks,time,totalBalance;
        FrameLayout frameLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount=itemView.findViewById(R.id.amountValue);
            remarks=itemView.findViewById(R.id.remarkTag);
            time=itemView.findViewById(R.id.texttime);
            totalBalance=itemView.findViewById(R.id.totalBalance);
            frameLayout=itemView.findViewById(R.id.frame);
        }
    }
}
