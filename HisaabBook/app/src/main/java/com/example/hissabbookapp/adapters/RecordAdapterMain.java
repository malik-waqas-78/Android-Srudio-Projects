package com.example.hissabbookapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hissabbookapp.R;
import com.example.hissabbookapp.interfaces.RecordItemClickInterface;
import com.example.hissabbookapp.modalclasses.RecordData;

import java.util.ArrayList;

public class RecordAdapterMain extends RecyclerView.Adapter<RecordAdapterMain.ViewHolder> implements Filterable {
    Context context;
    ArrayList<RecordData> recordData=new ArrayList<RecordData>();
    ArrayList<RecordData> recordDataExample=new ArrayList<RecordData>();
    RecordItemClickInterface recordItemClickInterface;
    String id;
    public RecordAdapterMain(Context context, ArrayList<RecordData> recordData) {
        this.context = context;
        this.recordData = recordData;
        recordDataExample=recordData;
        recordItemClickInterface=(RecordItemClickInterface)context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_record_item, parent, false);
        RecordAdapterMain.ViewHolder viewHolder = new RecordAdapterMain.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecordAdapterMain.ViewHolder holder, int position) {

        holder.amount.setText(recordData.get(position).getTotalBalance());
        holder.remarks.setText(recordData.get(position).getRemark());
        holder.time.setText(recordData.get(position).getTime());
        holder.totalBalance.setText("Balance: "+recordData.get(position).getBalance());
        id=recordData.get(position).getId();

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordItemClickInterface.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<RecordData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(recordDataExample);
                } else {
                    String filterPattern = constraint.toString();
                    for (RecordData item : recordDataExample) {
                        if (item.getRemark().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                recordData = filteredList;
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
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
