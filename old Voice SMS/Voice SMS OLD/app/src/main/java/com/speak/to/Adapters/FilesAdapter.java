package com.speak.to.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.speak.to.Activities.FileViewActivity;
import com.speak.to.Models.Model_Class;
import com.speak.to.R;

import java.io.File;
import java.util.ArrayList;

import static com.speak.to.Utils.Constants.Files_Count;
import static com.speak.to.Utils.Constants.Max_files;
import static com.speak.to.Utils.Constants.files_list;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Model_Class> mDataSet;
    private final FileViewActivity.Files_Interface filesInterface;

    public FilesAdapter(Context context, ArrayList<Model_Class> mDataSet, FileViewActivity.Files_Interface filesInterface) {
        this.context = context;
        this.mDataSet = mDataSet;
        this.filesInterface = filesInterface;
    }

    public static String getFolderSizeLabel(File file) {
        long size = getFolderSize(file) / 1024; // Get size and convert bytes into Kb.
        if (size >= 1024) {
            return (size / 1024) + " MB";
        } else {
            return size + " KB";
        }
    }

    public static long getFolderSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                size += getFolderSize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_file_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model_Class current_item = mDataSet.get(position);
        String absolutePath = current_item.getFileUri();
        File file = new File(absolutePath);
        holder.fileName.setText(file.getName());
        holder.checkBox.setChecked(mDataSet.get(position).isSelected());
        holder.fileSize.setText(getFolderSizeLabel(file));
        holder.checkBox.setOnClickListener(view -> {
            if (Files_Count < Max_files) {
                current_item.setSelected(!current_item.isSelected());
                if (current_item.isSelected()) {
                    files_list.add(current_item);
                    Files_Count++;
                } else {
                    files_list.remove(current_item);
                    Files_Count--;
                }
            } else {
                if (current_item.isSelected()) {
                    files_list.remove(current_item);
                    Files_Count--;
                    current_item.setSelected(false);
                } else {
                    Toast.makeText(context, "Max Limit Reached", Toast.LENGTH_SHORT).show();
                }
            }

            mDataSet.remove(position);
            mDataSet.add(position, current_item);
            filesInterface.sendCallbacksFromFileActivity(Max_files - Files_Count);
            notifyDataSetChanged();
        });
        holder.file_list_row.setOnClickListener(view -> {
            if (Files_Count < Max_files) {
                current_item.setSelected(!current_item.isSelected());
                if (current_item.isSelected()) {
                    files_list.add(current_item);
                    Files_Count++;
                } else {
                    files_list.remove(current_item);
                    Files_Count--;
                }
            } else {
                if (current_item.isSelected()) {
                    files_list.remove(current_item);
                    Files_Count--;
                    current_item.setSelected(false);
                } else {
                    Toast.makeText(context, "Max Limit Reached", Toast.LENGTH_SHORT).show();
                }
            }

            mDataSet.remove(position);
            mDataSet.add(position, current_item);
            filesInterface.sendCallbacksFromFileActivity(Max_files - Files_Count);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        TextView fileSize;
        CheckBox checkBox;
        RelativeLayout file_list_row;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_name);
            checkBox = itemView.findViewById(R.id.checkBoxFiles);
            fileSize = itemView.findViewById(R.id.fileSize);
            file_list_row = itemView.findViewById(R.id.file_list_row);
        }
    }
}
