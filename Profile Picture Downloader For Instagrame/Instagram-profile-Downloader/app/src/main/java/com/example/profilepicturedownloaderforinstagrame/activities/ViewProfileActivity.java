package com.example.profilepicturedownloaderforinstagrame.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import android.widget.ImageButton;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.profilepicturedownloaderforinstagrame.GlobalConstant;
import com.example.profilepicturedownloaderforinstagrame.R;
import com.example.profilepicturedownloaderforinstagrame.repositry.DataObjectRepositry;
import com.example.profilepicturedownloaderforinstagrame.tables.Downloads;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class ViewProfileActivity extends AppCompatActivity {
    private PhotoView imageView;
    private Bitmap bitmap;
    ImageButton button_save;
    String username = "", userId = "";
    private DataObjectRepositry dataObjectRepositry;
    FileOutputStream out = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_profile);
        imageView = findViewById(R.id.main_imageview);
        button_save = findViewById(R.id.button_save);
        dataObjectRepositry = DataObjectRepositry.dataObjectRepositry;
        try {
            bitmap = BitmapFactory.decodeStream(ViewProfileActivity.this.openFileInput("myImage"));
            Glide.with(this).load(bitmap).into(imageView);
            Log.d("TAG", "bitmap_onCreate: " + bitmap.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (getIntent() != null) {
            userId = getIntent().getStringExtra("user_id");
            username = getIntent().getStringExtra("username");
        }
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(bitmap);
/*
                Toast.makeText(ViewProfileActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
*/
             /*   Dexter.withActivity(ViewProfileActivity.this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    saveImage(bitmap);
                }


                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    PermissionListener dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
                            .withContext(ViewProfileActivity.this)
                            .withTitle("Storage permission")
                            .withMessage("Storage permission is needed to save pictures")
                            .withButtonText(android.R.string.ok)
                            .build();
                }
                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                }
            });*/
            }
        });
    }

    private void saveImage(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + GlobalConstant.SAVED_FILE_NAME);
        if (!file.exists())
            file.mkdirs();

        String fileName = GlobalConstant.SAVED_FILE_NAME + "-" + System.currentTimeMillis() + ".jpg";

        File newImage = new File(file, fileName);
        Log.d("TAG", "saveImage: " + newImage);
        if (newImage.exists()) file.delete();
        Log.d("TAG", "fileName: " + fileName + userId + username);

        try {
            FileOutputStream out = new FileOutputStream(newImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Downloads downloads = new Downloads();
            Log.d("TAG", "fileName1: " + fileName);
            downloads.setUser_id(userId);
            downloads.setPath(newImage.getPath());
            downloads.setUsername(username);
            downloads.setFilename(fileName);
            Log.d("TAG", "fileName: " + fileName);
            downloads.setType(0);
            Toast.makeText(this, "Saving image...", Toast.LENGTH_SHORT).show();
            dataObjectRepositry.addDownloadedData(downloads);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "fileName2: " + e.getMessage());
        }
    }
}