package com.example.deviceinfo.adapter;

import android.content.Context;
import android.gesture.GestureLibraries;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deviceinfo.R;
import com.example.deviceinfo.interfaces.OntoolsItemClicked;
import com.example.deviceinfo.models.Tools_Model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {

    ArrayList<Tools_Model> tools=new ArrayList<>();
    Context context;

    OntoolsItemClicked ontoolsItemClicked;


    public ToolsAdapter(ArrayList<Tools_Model> tools, Context context) {
        this.tools = tools;
        this.context = context;
    }
    public void onToolsItemClicked(OntoolsItemClicked ontoolsItemClicked) {
        this.ontoolsItemClicked=ontoolsItemClicked;
    }

    @NonNull
    @NotNull
    @Override
    public ToolsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tools_item, parent, false);
        ToolsAdapter.ViewHolder viewHolder = new ToolsAdapter.ViewHolder( view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ToolsAdapter.ViewHolder holder, int position) {

        holder.imageView.setImageResource(tools.get(position).getItemIcon());
        holder.textView.setText(tools.get(position).getItemName());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ontoolsItemClicked.onToolsitemClicked(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tools.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.imgIcon);
            textView=itemView.findViewById(R.id.textName);
            constraintLayout=itemView.findViewById(R.id.clickAble);


        }
    }
}
