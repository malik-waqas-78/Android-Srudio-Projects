package com.voicesms.voice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.voicesms.voice.Activities.VoiceSearchActivity_VS;
import com.voicesms.voice.Models.Search_List_Item;
import com.voicesms.voice.Utils.Constants;
import com.voicesms.voice.databinding.RowSearchListBinding;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    Context context;
    RowSearchListBinding binding;
    ArrayList<Search_List_Item> mDataSet;

    public SearchListAdapter(Context context, ArrayList<Search_List_Item> mDataSet) {
        this.context = context;
        this.mDataSet = mDataSet;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchListAdapter.ViewHolder(RowSearchListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, int position) {
        Search_List_Item item = mDataSet.get(position);
        Glide.with(context).load(item.getRow_icon()).into(binding.rowIcon);
        binding.rowTitle.setText(context.getString(item.getRow_title()));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(RowSearchListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.cardViewRowSearch.setOnClickListener(view -> {
                context.startActivity(new Intent(context, VoiceSearchActivity_VS.class)
                        .putExtra(Constants.SEARCH_SOURCE_NAME, mDataSet.get(getAdapterPosition()).getRow_title())
                );
            });
        }
    }
}
