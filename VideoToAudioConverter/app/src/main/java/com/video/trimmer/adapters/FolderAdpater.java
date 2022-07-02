package com.video.trimmer.adapters;

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

import com.video.trimmer.R;
import com.video.trimmer.interfaces.OnFolderClicked;
import com.video.trimmer.modelclasses.FolderModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FolderAdpater extends RecyclerView.Adapter<FolderAdpater.ViewHolder> implements Filterable {
    Context context;
    ArrayList<FolderModel> folders=new ArrayList<>();
    ArrayList<FolderModel> foldersExample=new ArrayList<>();
    OnFolderClicked onFolderClicked;

    public FolderAdpater(Context context, ArrayList<FolderModel> folders) {
        this.context = context;
        this.folders = folders;
        foldersExample=folders;
    }

    public void onFolderItemClicked(OnFolderClicked onFolderClicked){
        this.onFolderClicked=onFolderClicked;
    }
    @NonNull
    @NotNull
    @Override
    public FolderAdpater.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false);
        FolderAdpater.ViewHolder viewHolder = new FolderAdpater.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FolderAdpater.ViewHolder holder, int position) {
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<FolderModel> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(foldersExample);
                } else {
                    String filterPattern = constraint.toString();
                    for (FolderModel item : foldersExample) {
                        if (item.getFolderName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                folders = filteredList;
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
        FrameLayout frameFolder;
        TextView folderName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            folderName=itemView.findViewById(R.id.folderItemName);
            frameFolder=itemView.findViewById(R.id.folderFrame);
        }
    }
}
