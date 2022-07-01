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
import com.example.whatsappdatarecovery.activities.FullImageActivity;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class ImageFullAdapter extends RecyclerView.Adapter<ImageFullAdapter.ViewHolder> {

    Context context;
    ArrayList<ImagesModelClass> imagesArrayList;
    public ImageFullAdapter(Context context, ArrayList<ImagesModelClass> imagesArrayList) {
        this.context = context;
        this.imagesArrayList = imagesArrayList;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_grid_layout, parent, false));
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = Uri.parse(imagesArrayList.get(position).getPath());
        Bitmap b= ThumbnailUtils.createImageThumbnail(uri.toString(), MediaStore.Images.Thumbnails.MICRO_KIND);
        (holder).imageView.setImageBitmap(b);
        (holder).imageView.setOnClickListener((View.OnClickListener) v -> {
            Intent i=new Intent(context, FullImageActivity.class);
            (holder).imageView.setDrawingCacheEnabled(true);
            i.putExtra("image", uri.toString());
            context.startActivity(i);
        });
        (holder).btn_image_deleted.setOnClickListener((View.OnClickListener) v -> {
            ImagesModelClass images=imagesArrayList.get(position);
            String path=getPathToDelete(images.getName());
            if(path!=null){
                File imageToDelete=new File(path);
                imageToDelete.delete();
                imageToDelete=new File(images.getPath());
                if(imageToDelete.delete()){
                    imagesArrayList.remove(position);
                }
                System.gc();
            }
            notifyDataSetChanged();
        });
        (holder).btn_share.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("images/png");
            //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(intent, "Share Image "));
        });
    }

    private String getPathToDelete(String name) {
        String purename=getpurename(name);
        String path=load_Directory_files(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Images"),purename);
        return path;
    }

    private String getpurename(String name) {
        int ind=name.indexOf("_");
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
        return  null;
    }
    @Override
    public int getItemCount() {
    return imagesArrayList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        ImageButton btn_image_deleted,btn_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView_1);
            btn_share=itemView.findViewById(R.id.btn_share);
            btn_image_deleted=itemView.findViewById(R.id.btn_image_delete);
        }
    }
}
