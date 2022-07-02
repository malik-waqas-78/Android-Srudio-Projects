package com.video.trimmer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.video.trimmer.R;
import com.video.trimmer.adapters.FolderAdpater;
import com.video.trimmer.adapters.Images_Adapter;
import com.video.trimmer.databinding.ActivityImagesBinding;
import com.video.trimmer.interfaces.OnFolderClicked;
import com.video.trimmer.interfaces.OnOutputItemsLongClicked;
import com.video.trimmer.modelclasses.FolderModel;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.FolderFetcherClass;

import java.util.ArrayList;

public class ImagesActivity extends AppCompatActivity {
    ActivityImagesBinding binding;
    ArrayList<FolderModel> folders=new ArrayList<>();
    FolderAdpater folderAdpater;
    Images_Adapter images_adapter;
    static ArrayList<String> selectedImages=new ArrayList<>();
    boolean isFolderOpened=false;
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        binding.toolbar.setTitle("");
        binding.toolbar.setNavigationIcon(R.drawable.ic_back_icon);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setImages();
        binding.btnbyImages.setBackgroundColor(ContextCompat.getColor(ImagesActivity.this,R.color.btnselectedcolor));
        binding.btnbyImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setImages();
                    isFolderOpened=false;
                binding.btnbyImages.setBackgroundColor(ContextCompat.getColor(ImagesActivity.this,R.color.btnselectedcolor));
                binding.btnbyfolders.setBackgroundColor(ContextCompat.getColor(ImagesActivity.this,R.color.white));
            }
        });
        binding.btnbyfolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadandSetFolder();
                isFolderOpened=false;
                binding.btnbyImages.setBackgroundColor(ContextCompat.getColor(ImagesActivity.this,R.color.white));
                binding.btnbyfolders.setBackgroundColor(ContextCompat.getColor(ImagesActivity.this,R.color.btnselectedcolor));
            }
        });
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImages=new ArrayList<>(images_adapter.getSelected());
                Log.d("textdoc",selectedImages.size()+" "+images_adapter.getSelected().size());
                setResult(1);
                finish();
            }
        });
    }

    public static ArrayList<String> getSelectedImages() {
        return selectedImages;
    }

    public  void setSelectedImages(ArrayList<String> selectedImages) {
        this.selectedImages = selectedImages;
    }

    private void setImages() {
        new LoadData().execute();
    }
    public void setImagesinAdapter(){
        images_adapter=new Images_Adapter(im,ImagesActivity.this);
        binding.recyclerImages.setLayoutManager(new GridLayoutManager(ImagesActivity.this,3));
        binding.recyclerImages.setAdapter(images_adapter);
        images_adapter.onImageClicked(new OnOutputItemsLongClicked() {
            @Override
            public void onLongPressed(boolean state) {
                if(state){
                    binding.btnOk.setVisibility(View.VISIBLE);
                }else{
                    binding.btnOk.setVisibility(View.GONE);
                }
            }
        });
    }
    private void loadUImages() {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            im.add(absolutePathOfImage);
        }

    }

    private void loadandSetFolder() {
        folders=new FolderFetcherClass(ImagesActivity.this,3).get_Recovery_Audios();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        folderAdpater=new FolderAdpater(ImagesActivity.this,folders);
        binding.recyclerImages.setLayoutManager(layoutManager);
        binding.recyclerImages.setAdapter(folderAdpater);

        folderAdpater.onFolderItemClicked(new OnFolderClicked() {
            @Override
            public void onFolderClicked(String path,int pos) {
                isFolderOpened=true;
                setSubAudios(path);
            }
        });
    }
    public void setSubAudios(String path){
        ArrayList<String> folderVideos=QueryInFolder(path);
        images_adapter=new Images_Adapter(folderVideos,ImagesActivity.this);
        binding.recyclerImages.setLayoutManager(new GridLayoutManager(ImagesActivity.this,3));
        binding.recyclerImages.setAdapter(images_adapter);
        images_adapter.onImageClicked(new OnOutputItemsLongClicked() {
            @Override
            public void onLongPressed(boolean state) {
                if(state){
                    binding.btnOk.setVisibility(View.VISIBLE);
                }else{
                    binding.btnOk.setVisibility(View.GONE);
                }
            }
        });
    }

    private ArrayList<String> QueryInFolder(String path) {

        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = getContentResolver().query(uri, projection,
                MediaStore.Images.Media.DATA + " like ? "
                , new String[]{"%" + path + "%"},
                 null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    @Override
    public void onBackPressed() {
        if(images_adapter.getSelected().size()!=0){
            images_adapter.setSelected(new ArrayList<>());
            images_adapter.notifyDataSetChanged();
        }else if(isFolderOpened){
            isFolderOpened=false;
            loadandSetFolder();
        }else {
            super.onBackPressed();
        }
    }
    ArrayList<String> im=new ArrayList<>();
    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            im.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingProgress(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {

            loadUImages();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setImagesinAdapter();
                }
            });

            return "Task Completed";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            images_adapter.notifyDataSetChanged();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingProgress(false);
                }
            });

        }
    }
    private void showLoadingProgress(boolean state) {
        if(state) {
            binding.constraintProgress.setVisibility(View.VISIBLE);
        }else{
            binding.constraintProgress.setVisibility(View.GONE);
        }
    }
}