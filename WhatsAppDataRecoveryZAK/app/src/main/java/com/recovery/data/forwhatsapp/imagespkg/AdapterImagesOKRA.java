package com.recovery.data.forwhatsapp.imagespkg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.activities.ActivityImagesOKRA;
import com.recovery.data.forwhatsapp.activities.ActivitySeeMoreImagesOKRA;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterImagesOKRA extends RecyclerView.Adapter implements Filterable {
    private static final String TAG = "92727";
    ArrayList<File> image_files;
    ImagesUrlInterfaceOKRA url_interface;
    Context context;
    ArrayList<String> selectedRows = new ArrayList<>();
    LongClickInterfaceOKRA itemLongClickListeners;
    private List<File> exampleListFull;

    public AdapterImagesOKRA(ArrayList<File> image_files, Context context) {
        this.context = context;
        this.image_files = image_files;
        exampleListFull = image_files;
    }

    public void setLongClickListeners(ActivityImagesOKRA images_activity) {
        this.itemLongClickListeners = images_activity;
    }

    public void setLongClickListenersSeeMore(ActivitySeeMoreImagesOKRA images_activity) {
        this.itemLongClickListeners = images_activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_images_okra, parent, false);
        return new Adapter_ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_ViewHolder row_view = (Adapter_ViewHolder) holder;
        File imageFile = image_files.get(position);
        if (selectedRows.size() != 0 && selectedRows.contains(imageFile.getAbsolutePath())) {
            //row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
            row_view.relativeChecked.setVisibility(View.VISIBLE);
        } else {
            row_view.relativeChecked.setVisibility(View.GONE);
            row_view.frameLayout.setBackgroundColor(Color.TRANSPARENT);
        }
        row_view.frameLayout.setBackgroundColor(Color.TRANSPARENT);
        Log.d(TAG, "onBindViewHolder: viewing :: " + imageFile.getName());
        row_view.imagename.setText(imageFile.getName());
        row_view.imagename.setSelected(true);
        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        row_view.imageView.setImageBitmap(myBitmap);
        row_view.imageView.setOnClickListener(view -> url_interface.setUrlandLaunchInterface(imageFile.getAbsolutePath(),imageFile.getName()));
//        row_view.imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                url_interface.onLongClick(imageFile.getAbsolutePath(),position);
//                return true;
//            }
//        });
        row_view.frameLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (selectedRows.size() != 0 && selectedRows.contains(imageFile.getAbsolutePath())) {
                    row_view.frameLayout.setBackgroundColor(Color.TRANSPARENT);
                    row_view.relativeChecked.setVisibility(View.GONE);
                    selectedRows.remove(imageFile.getAbsolutePath());
                    if (selectedRows.size() == 0) {
                        itemLongClickListeners.onLongClicked(false);
                    }
                } else if (selectedRows.size() != 0 && !selectedRows.contains(imageFile.getAbsolutePath())) {
                   // row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
                    row_view.relativeChecked.setVisibility(View.VISIBLE);
                    selectedRows.add(imageFile.getAbsolutePath());
                } else {
                    url_interface.setUrlandLaunchInterface(imageFile.getAbsolutePath(),imageFile.getName());
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
                    selectedRows.add(imageFile.getAbsolutePath());
                }
                return true;
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return image_files.size();
    }

    public ArrayList<String> getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(ArrayList<String> selectedRows) {
        this.selectedRows = selectedRows;
    }

    public void setInterface(ImagesUrlInterfaceOKRA url_interface) {
        this.url_interface = url_interface;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<File> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(exampleListFull);
                } else {
                    String filterPattern = constraint.toString();
                    for (File item : exampleListFull) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                image_files = filteredList;
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
        TextView imagename;
        RelativeLayout relativeChecked;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagename=itemView.findViewById(R.id.imagename);
            imageView = itemView.findViewById(R.id.imagerecovered);
            frameLayout = itemView.findViewById(R.id.framelayout);
            relativeChecked=itemView.findViewById(R.id.checkedrel);
        }
    }
}
