package com.speak.to.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.speak.to.Activities.ImageViewActivity;
import com.speak.to.Models.Model_Class;
import com.speak.to.R;

import java.util.ArrayList;

import static com.speak.to.Utils.Constants.Files_Count;
import static com.speak.to.Utils.Constants.Max_files;
import static com.speak.to.Utils.Constants.files_list;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Model_Class> mDataSet;
    private final ImageViewActivity.imageInterface imageInterface;

    public ImagesAdapter(Context context, ArrayList<Model_Class> mDataSet, ImageViewActivity.imageInterface imageInterface) {
        this.context = context;
        this.mDataSet = mDataSet;
        this.imageInterface = imageInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_image_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model_Class current_item = mDataSet.get(position);
        String stringUri = current_item.getFileUri();
        holder.checkBox.setChecked(current_item.isSelected());
        Glide.with(context)
                .load(stringUri)
                .into(holder.img);
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
            imageInterface.sendCallbacks(Max_files - Files_Count);
            notifyDataSetChanged();
        });

        holder.img.setOnClickListener(view -> {
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
            imageInterface.sendCallbacks(Max_files - Files_Count);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
