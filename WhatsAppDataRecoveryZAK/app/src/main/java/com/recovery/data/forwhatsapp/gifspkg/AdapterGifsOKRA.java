package com.recovery.data.forwhatsapp.gifspkg;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.bumptech.glide.Glide;
import com.recovery.data.forwhatsapp.activities.ActivityGifsOKRA;

import java.io.File;
import java.util.ArrayList;

public class AdapterGifsOKRA extends RecyclerView.Adapter  implements Filterable {
    ArrayList<File> gif_files;
    ArrayList<File> example_gif_files;
    private static final String TAG = "92727";
    Context context;
    ArrayList<String> selectedRows = new ArrayList<>();
    LongClickInterfaceOKRA itemLongClickListeners;

    public void setGifs_url_interface(GifsUrlInterfaceOKRA gifs_url_interface) {
        this.gifs_url_interface = gifs_url_interface;
    }

    GifsUrlInterfaceOKRA gifs_url_interface;

    public AdapterGifsOKRA(ArrayList<File> gif_files, Context context) {
        this.gif_files = gif_files;
        this.context = context;
        example_gif_files=gif_files;
    }

    public void setLongClickListeners(ActivityGifsOKRA gifs_activity) {
        this.itemLongClickListeners = gifs_activity;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_gifs_okra, parent, false);
        return new Adapter_ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_ViewHolder row_view = (Adapter_ViewHolder) holder;
        Log.d(TAG, "onBindViewHolder: " + gif_files.size());
        File videoFile = gif_files.get(position);
        if (selectedRows.size() != 0 && selectedRows.contains(videoFile.getAbsolutePath())) {
           // row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
            row_view.relativeChecked.setVisibility(View.VISIBLE);
        } else {
            row_view.frameLayout.setBackgroundColor(Color.TRANSPARENT);
            row_view.relativeChecked.setVisibility(View.GONE);
        }
        Log.d(TAG, "onBindViewHolder: viewing :: " + videoFile.getName());
        Glide.with(context).load(Uri.fromFile(videoFile)).into(row_view.imageView);
        row_view.imageView.setOnClickListener(view -> gifs_url_interface.setUrlandLaunchInterface(videoFile.getAbsolutePath()));
        row_view.frameLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (selectedRows.size() != 0 && selectedRows.contains(videoFile.getAbsolutePath())) {
                    row_view.frameLayout.setBackgroundColor(Color.TRANSPARENT);
                    row_view.relativeChecked.setVisibility(View.GONE);
                    selectedRows.remove(videoFile.getAbsolutePath());

                    if (selectedRows.size() == 0){
                        itemLongClickListeners.onLongClicked(false);
                    }

                } else if (selectedRows.size() != 0 && !selectedRows.contains(videoFile.getAbsolutePath())) {
                    //row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
                    row_view.relativeChecked.setVisibility(View.VISIBLE);
                    selectedRows.add(videoFile.getAbsolutePath());
                } else {
                    gifs_url_interface.setUrlandLaunchInterface(videoFile.getAbsolutePath());
                }
            }
        });
        row_view.frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View view) {
                if (selectedRows.size() == 0) {
                    itemLongClickListeners.onLongClicked(true);
                    //row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
                    row_view.relativeChecked.setVisibility(View.VISIBLE);
                    selectedRows.add(videoFile.getAbsolutePath());
                }
                return true;
            }
        });
//        row_view.imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                gifs_url_interface.onLOngClick(videoFile.getAbsolutePath(),position);
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return gif_files.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public ArrayList<String> getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(ArrayList<String> selectedRows) {
        this.selectedRows = selectedRows;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<File> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(example_gif_files);
                } else {
                    String filterPattern = constraint.toString();
                    for (File item : example_gif_files) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                gif_files = filteredList;
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                image_files.clear();
//                image_files.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private class Adapter_ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        FrameLayout frameLayout;
        RelativeLayout relativeChecked;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagerecovered);
            frameLayout = itemView.findViewById(R.id.framelayout);
            relativeChecked=itemView.findViewById(R.id.checkedrel);
        }


    }
}
