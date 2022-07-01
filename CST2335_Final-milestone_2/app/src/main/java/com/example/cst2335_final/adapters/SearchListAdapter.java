package com.example.cst2335_final.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cst2335_final.R;
import com.example.cst2335_final.beans.SearchItem;

import java.util.ArrayList;

/**
 * List adapter for SearchItem
 * @see SearchItem
 * @see ArrayAdapter
 */

public class SearchListAdapter extends ArrayAdapter<SearchItem> {

    private Context context;
    private final ArrayList<SearchItem> items;

    /**
     * Constructor for adapter
     * @param context the context
     * @param items the SearchItem arrayList
     */
    public SearchListAdapter(@NonNull Context context, ArrayList<SearchItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() { return items.size(); }

    @Override
    public SearchItem getItem(int position) { return items.get(position); }

    @Override
    public long getItemId(int position) {return getItem(position).getId(); }

    @NonNull
    @Override
    public View getView(int position, @Nullable View old, @NonNull ViewGroup parent) {
        View newView = old;

        if(newView == null){
            newView = LayoutInflater.from(getContext()).inflate(R.layout.row_searchitem, null);
        }

        TextView searchItemTitle = newView.findViewById(R.id.searchItemTitle);
        TextView searchItemSection = newView.findViewById(R.id.searchItemSection);
        if (searchItemTitle != null){
            searchItemTitle.setText(getItem(position).getTitle());
        }
        if (searchItemSection != null){
            searchItemSection.setText(getItem(position).getSection());
        }

        return newView;
    }
}
