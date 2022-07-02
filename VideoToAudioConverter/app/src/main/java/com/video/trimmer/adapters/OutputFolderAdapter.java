package com.video.trimmer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.trimmer.R;
import com.video.trimmer.interfaces.OnFolderClicked;
import com.video.trimmer.modelclasses.FolderModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OutputFolderAdapter extends RecyclerView.Adapter<OutputFolderAdapter.ViewHolder> {

    Context context;
    ArrayList<FolderModel> folders=new ArrayList<>();
    OnFolderClicked onFolderClicked;

    public OutputFolderAdapter(Context context, ArrayList<FolderModel> folders) {
        this.context = context;
        this.folders = folders;
    }

    public void onFolderItemClicked(OnFolderClicked onFolderClicked){
        this.onFolderClicked=onFolderClicked;
    }
    @NonNull
    @NotNull
    @Override
    public OutputFolderAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.output_folder_item, parent, false);
        OutputFolderAdapter.ViewHolder viewHolder = new OutputFolderAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OutputFolderAdapter.ViewHolder holder, int position) {
        holder.folderName.setText(folders.get(position).getFolderName());
        holder.folderName.setSelected(true);
        holder.frameFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path=folders.get(position).getFolderPath();
                onFolderClicked.onFolderClicked(path,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameFolder;
        TextView folderName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            folderName=itemView.findViewById(R.id.folderItemName);
            frameFolder=itemView.findViewById(R.id.folderFrame);
        }
    }
}
