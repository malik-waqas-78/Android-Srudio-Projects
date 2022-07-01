package com.example.whatsappdatarecovery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.activities.MainPdfActivity;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class DocumentsFullAdapter extends RecyclerView.Adapter<DocumentsFullAdapter.ViewHolder> {

    Context context;
    ArrayList<ImagesModelClass> iMagesArrayList;
    public DocumentsFullAdapter(Context context, ArrayList<ImagesModelClass> iMagesArrayList) {
        this.context = context;
        this.iMagesArrayList = iMagesArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.documents_grid_layout, parent, false));
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = Uri.parse(iMagesArrayList.get(position).getPath());
        ParcelFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = ParcelFileDescriptor.open(new File(iMagesArrayList.get(position).getPath()),
                    ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert fileDescriptor != null;
            PdfRenderer renderer=new PdfRenderer(fileDescriptor);
            PdfRenderer.Page mPdfPage = renderer.openPage(0);
            Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(), mPdfPage.getHeight(), Bitmap.Config.ARGB_8888);
            mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            ((ViewHolder) holder).imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((ViewHolder) holder).imageView.setOnClickListener((View.OnClickListener) v -> {
            Intent i=new Intent(context, MainPdfActivity.class);
            i.putExtra("image",uri.toString());
            context.startActivity(i);
                        });
        ((ViewHolder) holder).btn_image_deleted.setOnClickListener((View.OnClickListener) v -> {
            ImagesModelClass images=iMagesArrayList.get(position);
            String path=getPathToDelete(images.getName());
            if(path!=null){
                File imageToDelete=new File(path);
                imageToDelete.delete();
                imageToDelete=new File(images.getPath());
                if(imageToDelete.delete()){
                    iMagesArrayList.remove(position);
                }
                System.gc();
            }
            notifyDataSetChanged();
        });
        ((ViewHolder) holder).btn_share.setOnClickListener((View.OnClickListener) v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("documents/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(intent, "Share File "));
        });
    }

    private String getPathToDelete(String name) {
        String purename=getpurename(name);
        String path=load_Directory_files(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Documents"),purename);
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
    return iMagesArrayList.size();
    }
    public static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        ImageButton btn_image_deleted,btn_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.documents_thumbnail);
            btn_image_deleted=itemView.findViewById(R.id.btn_documents_delete);
            btn_share=itemView.findViewById(R.id.btn_documents_share);
        }
    }
}
