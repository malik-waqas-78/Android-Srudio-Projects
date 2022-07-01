package com.example.whatsappdatarecovery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.activities.FullVideoActivity;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoFullAdapter extends RecyclerView.Adapter<VideoFullAdapter.ViewHolder> {

    Context context;
    ArrayList<ImagesModelClass> imagesArrayList;
    public VideoFullAdapter(Context context, ArrayList<ImagesModelClass> iMagesArrayList) {
        this.context = context;
        this.imagesArrayList = iMagesArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.video_grid_layout, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = Uri.parse(imagesArrayList.get(position).getPath());
        Log.d("TAG", "onBindViewHolder_video2: "+uri);
        Bitmap b= ThumbnailUtils.createVideoThumbnail(uri.toString(), MediaStore.Video.Thumbnails.MICRO_KIND);
        ((ViewHolder)holder).imageView.setImageBitmap(b);
        ((ViewHolder) holder).imageView.setOnClickListener((View.OnClickListener) v -> {
            Intent i=new Intent(context, FullVideoActivity.class);
            ((ViewHolder) holder).imageView.setDrawingCacheEnabled(true);
            i.putExtra("video", uri.toString());
            context.startActivity(i);
        });
        ((ViewHolder) holder).delete_btn.setOnClickListener((View.OnClickListener) v -> {
            ImagesModelClass images= imagesArrayList.get(position);
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

        ((ViewHolder) holder).btn_share.setOnClickListener((View.OnClickListener) v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("video/mp4");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(intent, "Share Video "));
        });
    }
    private String getPathToDelete(String name) {
        String purename=getpurename(name);
        String path=load_Directory_files(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Videos"),purename);
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
        ImageButton delete_btn,btn_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.video_image);
            delete_btn=itemView.findViewById(R.id.btn_video_delete);
            btn_share=itemView.findViewById(R.id.btn_video_share);
        }
    }
}
