package com.voicesms.voice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.voicesms.voice.Interfaces.OpenMessageInterface;
import com.voicesms.voice.Models.Message_Item;
import com.voicesms.voice.databinding.RowLayoutSaveMessageBinding;

import java.util.ArrayList;

public class OpenMessageAdapter extends RecyclerView.Adapter<OpenMessageAdapter.ViewHolder> {
    RowLayoutSaveMessageBinding binding;
    ArrayList<Message_Item> mDataSet;
    OpenMessageInterface openMessageInterface;
    Context context;
    myDatabaseAdapter myDatabaseAdapter;

    public OpenMessageAdapter() {
    }

    public OpenMessageAdapter(ArrayList<Message_Item> mDataSet, Context context, OpenMessageInterface OMI) {
        this.mDataSet = mDataSet;
        this.context = context;
        openMessageInterface = OMI;
        myDatabaseAdapter = new myDatabaseAdapter(context);
    }

    @NonNull
    @Override
    public OpenMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowLayoutSaveMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OpenMessageAdapter.ViewHolder holder, int position) {
        binding.rowTextSavedList.setText(mDataSet.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface notifyUpdate {
        void updateView();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull RowLayoutSaveMessageBinding v) {
            super(v.getRoot());
            binding = v;
            binding.cardViewOpenMessage.setOnClickListener(view -> {
                openMessageInterface.openMessage(mDataSet.get(getAdapterPosition()).getMsg());
            });
            binding.btnDeleteMsg.setOnClickListener(view -> {
                openMessageInterface.deleteMessage(mDataSet.get(getAdapterPosition()).getMsg()
                        , new notifyUpdate() {
                            @Override
                            public void updateView() {
                                mDataSet.remove(getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        },getItemCount());
            });
        }
    }
}
