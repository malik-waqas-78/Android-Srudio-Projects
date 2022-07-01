package com.example.whatsappdatarecovery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.activities.StickersMainImageActivity;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class StickersFullAdapter extends RecyclerView.Adapter<StickersFullAdapter.ViewHolder> {

    Context context;
    ArrayList<ImagesModelClass> iMagesArrayList;
    public StickersFullAdapter(Context context, ArrayList<ImagesModelClass> iMagesArrayList) {
        this.context = context;
        this.iMagesArrayList = iMagesArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.video_grid_layout, parent, false));
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = Uri.parse(iMagesArrayList.get(position).getPath());
        Bitmap b = ThumbnailUtils.createImageThumbnail(uri.toString(), MediaStore.Images.Thumbnails.MICRO_KIND);
        ((ViewHolder) holder).imageView.setImageBitmap(b);
        ((ViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, StickersMainImageActivity.class);
                ((ViewHolder) holder).imageView.setDrawingCacheEnabled(true);
                i.putExtra("stickers", uri.toString());
                context.startActivity(i);
            }
        });
        ((ViewHolder) holder).btn_stickers_dlt.setOnClickListener((View.OnClickListener) v -> {
            ImagesModelClass images = iMagesArrayList.get(position);
            String path = getPathToDelete(images.getName());
            if (path != null) {
                File imageToDelete = new File(path);
                imageToDelete.delete();
                imageToDelete = new File(images.getPath());
                if (imageToDelete.delete()) {
                    iMagesArrayList.remove(position);
                }
                System.gc();
            }
            notifyDataSetChanged();
        });
        ((ViewHolder) holder).btn_stickers_share.setOnClickListener((View.OnClickListener) v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("stickers/webp");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(intent, "Share Stickers "));
        });
    }

    private String getPathToDelete(String name) {
        String purename = getpurename(name);
        String path = load_Directory_files(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Stickers"), purename);
        return path;
    }

    private String getpurename(String name) {
        int ind = name.indexOf("_");
        return name.substring(++ind);
    }

    public String load_Directory_files(File file, String name) {
        Log.d("990", "dir name: " + file.getName());
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    load_Directory_files(value, name);
                } else if (value.getName().toLowerCase().equals(name)) {
                    Log.d("990", "file name: " + value.getName());
                    return value.getAbsolutePath();
                }
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return iMagesArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton btn_stickers_dlt, btn_stickers_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.video_image);
            btn_stickers_dlt = itemView.findViewById(R.id.btn_video_delete);
            btn_stickers_share = itemView.findViewById(R.id.btn_video_share);
        }
    }
}
