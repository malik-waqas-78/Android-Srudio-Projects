package com.recovery.data.forwhatsapp.fragmentstatuspkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.imagespkg.ImagesUrlInterfaceOKRA;

import java.io.File;
import java.util.ArrayList;

public class AdapterStatusOKRA extends RecyclerView.Adapter<AdapterStatusOKRA.Status_ViewHolder> implements Filterable {

    ArrayList<File> files;
    ArrayList<File> example_files;
    Context context;
    boolean selected=false;
    ImagesUrlInterfaceOKRA url_interface;
    public ArrayList<File> getSelectedFiles() {
        return selectedFiles;
    }

    public void setSelectedFiles() {
        this.selectedFiles = new ArrayList<>();
    }

    ArrayList<File> selectedFiles=new ArrayList<>();

    AdapterStatusOKRA(Context context, ImagesUrlInterfaceOKRA images_url_interface, ArrayList<File> files) {
        this.context = context;
        this.files = files;
        url_interface= images_url_interface;
        example_files=files;
    }

    @NonNull
    @Override
    public Status_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_images_okra, parent, false);
        return new Status_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Status_ViewHolder holder, int position) {
        Glide.with(context).load(files.get(position)).into(holder.iv_image);
        holder.name.setText(files.get(position).getName());
        holder.name.setSelected(true);
       /* if(!selectedFiles.isEmpty()){
            if(selectedFiles.contains(files.get(position))){
                holder.checkBox.setChecked(true);
                holder.checked_box.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.checked));
            }else{
                holder.checkBox.setChecked(false);
                holder.checked_box.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.transparent));
            }
        }else{
            holder.checkBox.setChecked(false);
            holder.checked_box.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.transparent));
        }*/

       /* holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFiles.contains(files.get(position))){
                    selectedFiles.remove(files.get(position));
                    holder.checkBox.setChecked(false);
                    holder.checked_box.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.transparent));
                }else{
                    selectedFiles.add(files.get(position));
                    holder.checkBox.setChecked(true);
                    holder.checked_box.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.checked));
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return files.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<File> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(example_files);
                } else {
                    String filterPattern = constraint.toString();
                    for (File item : example_files) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                files = filteredList;
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
    public class Status_ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView name;
      /*  CheckBox checkBox;
        FrameLayout checked_box;*/
        public Status_ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image=itemView.findViewById(R.id.imagerecovered);
            name=itemView.findViewById(R.id.imagename);
           /* checkBox=itemView.findViewById(R.id.cb_status);
            checked_box=itemView.findViewById(R.id.framelayout);*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if(!selectedFiles.isEmpty()){
                        if(selectedFiles.contains(files.get(getAdapterPosition()))){
                            selectedFiles.remove(files.get(getAdapterPosition()));
                            checkBox.setChecked(false);
                           checked_box.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.transparent));
                        }else{
                            selectedFiles.add(files.get(getAdapterPosition()));
                            checkBox.setChecked(true);
                            checked_box.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.checked));
                        }
                    }else{*/
                        //open image or video
                        url_interface.setUrlandLaunchInterface(files.get(getAdapterPosition()).getAbsolutePath(),files.get(getAdapterPosition()).getName());
                  //  }
                }
            });
           /* itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                        if(selectedFiles.contains(files.get(getAdapterPosition()))){
                            selectedFiles.remove(files.get(getAdapterPosition()));
                            checkBox.setChecked(false);
                            checked_box.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.transparent));
                        }else {
                            selectedFiles.add(files.get(getAdapterPosition()));
                            checkBox.setChecked(true);
                            checked_box.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.checked));
                        }
                    return true;
                }
            });*/
        }
    }

}
