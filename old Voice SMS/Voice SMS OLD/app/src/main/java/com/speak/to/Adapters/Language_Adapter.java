package com.speak.to.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.speak.to.Models.Lang_Item;
import com.speak.to.R;

import java.util.ArrayList;

public class Language_Adapter extends ArrayAdapter<Lang_Item> {
    Context mContext;

    public Language_Adapter(@NonNull Context context, ArrayList<Lang_Item> languages) {
        super(context, 0, languages);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.lang_spinner, parent, false
            );
        }
        ImageView flagView = convertView.findViewById(R.id.lang_flag);
        TextView langName = convertView.findViewById(R.id.lang_name);

        Lang_Item langItem = getItem(position);

        if (langItem != null) {
            Glide.with(mContext).load(langItem.getFlagId()).placeholder(R.drawable.ic_flag).into(flagView);
            langName.setText(langItem.getLocalName());
        }
        return convertView;
    }
}
