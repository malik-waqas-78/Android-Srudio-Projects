package com.video.trimmer.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.video.trimmer.R;
import com.video.trimmer.adapters.MainMenuAdapter;
import com.video.trimmer.adapters.OutputFolderAdapter;
import com.video.trimmer.databinding.ActivityMainBinding;
import com.video.trimmer.interfaces.MenuItemClickListener;
import com.video.trimmer.interfaces.OnFolderClicked;
import com.video.trimmer.modelclasses.FolderModel;
import com.video.trimmer.modelclasses.MainMenuModal;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.SharedPrefClass;

import java.io.File;
import java.util.ArrayList;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.video.trimmer.utils.Constants.ADD_AUDIO_ICON;

import static com.video.trimmer.utils.Constants.AUDIO_CUTTER_ICON;

import static com.video.trimmer.utils.Constants.FOLDER_NAME_INTENT_KEY;
import static com.video.trimmer.utils.Constants.MAIN_TYPE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.PERMISSIONS_REQUSTCODE2;

import static com.video.trimmer.utils.Constants.REMOVE_AUDIO_ICON;

import static com.video.trimmer.utils.Constants.TYPE_INTENT_KEY;
import static com.video.trimmer.utils.Constants.VIDEO_CUTTER_ICON;
import static com.video.trimmer.utils.Constants.VIDEO_TO_AUDIO_ICON;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<MainMenuModal> mainMenuModals=new ArrayList<>();
    MainMenuAdapter mainMenuAdapter;
    int position;
    OutputFolderAdapter folderAdpater;
    SharedPrefClass sharedPrefClass;
    boolean isForFolders=false;
    ArrayList<FolderModel> folderModels=new ArrayList<>();
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
//            if (SDK_INT >= Build.VERSION_CODES.R) {
//                if (Environment.isExternalStorageManager()) {
//                    startNextActivity(position);
//                } else {
//                    Toast.makeText(MainActivity.this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
//                }
//            }
            if(checkPermission()){
                startNextActivity(position);
            }else{
                Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    });
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        sharedPrefClass=new SharedPrefClass(MainActivity.this);
        addElementsInMenu();
        setItemsInArray();
        mainMenuAdapter=new MainMenuAdapter(MainActivity.this,mainMenuModals);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.recyclerMenu.setLayoutManager(gridLayoutManager);
        binding.recyclerMenu.setAdapter(mainMenuAdapter);

        mainMenuAdapter.onMenuItemClick(new MenuItemClickListener() {
            @Override
            public void onMenuItemClick(int pos) {
                position=pos;
                isForFolders=false;
                if (checkPermission()) {
                    startNextActivity(pos);
                } else {
                    permisssionDialog();
                }
            }
        });
        binding.imgBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void startNextActivity(int pos){

        File f=new File(constants.CONVERTED_AUDIO_FOLDER_PATH);
        File f2=new File(constants.TRIMMED_VIDEO_FOLDER_PATH);
        File f3=new File(constants.TRIMMED_AUDIO_FOLDER_PATH);
        File f4=new File(constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH);
        File f5=new File(constants.REMOVED_AUDIO_VIDEO_FOLDER_PATH);
        File f6=new File(constants.CREATED_VIDEO_WITH_IMAGES_FOLDER_PATH);
        File f7=new File(constants.TEMP_FOLDER_PATH);

        if(!f.exists()){
            f.mkdir();
        }if(!f2.exists()){
            f2.mkdir();
        }if(!f3.exists()){
            f3.mkdir();
        }if(!f4.exists()){
            f4.mkdir();
        }if(!f5.exists()){
            f5.mkdir();
        }if(!f6.exists()){
            f6.mkdir();
        }if(!f7.exists()){
            f7.mkdir();
        }
        if(!isForFolders) {

                if (pos == 0) {
                    Intent intent = new Intent(MainActivity.this, AudioActivity.class);
                    startActivity(intent);
                } else if (pos == 1) {
                    Intent intent = new Intent(MainActivity.this, VideosActivity.class);
                    intent.putExtra(MAIN_TYPE_INTENT_KEY, 1);
                    startActivity(intent);
                } else if (pos == 2) {
                    Intent intent = new Intent(MainActivity.this, VideosActivity.class);
                    intent.putExtra(MAIN_TYPE_INTENT_KEY, 2);
                    startActivity(intent);
                } else if (pos == 3) {
                    Intent intent = new Intent(MainActivity.this, VideosActivity.class);
                    intent.putExtra(MAIN_TYPE_INTENT_KEY, 3);
                    startActivity(intent);
                } else if (pos == 4) {
                    Intent intent = new Intent(MainActivity.this, VideosActivity.class);
                    intent.putExtra(MAIN_TYPE_INTENT_KEY, 4);
                    startActivity(intent);
                } else if (pos == 5) {
                    Intent intent = new Intent(MainActivity.this, ImagesToVideoActivity.class);
                    intent.putExtra(MAIN_TYPE_INTENT_KEY, 5);
                    startActivity(intent);
                }

        }else{
            sharedPrefClass.saveJugaadStateinShared(false);
            if(pos==0){

                Intent intent=new Intent(MainActivity.this,OutputsActivity.class);
                intent.putExtra(TYPE_INTENT_KEY,1);
                intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.covertedAudio));
                startActivity(intent);
            }else if(pos==1){
                Intent intent=new Intent(MainActivity.this,OutputsActivity.class);
                intent.putExtra(TYPE_INTENT_KEY,3);
                intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.trimmedAudios));
                startActivity(intent);
            }else if(pos==2){
                Intent intent=new Intent(MainActivity.this,OutputsActivity.class);
                intent.putExtra(TYPE_INTENT_KEY,2);
                intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.trimmedVideo));
                startActivity(intent);
            }else if(pos==3){
                Intent intent=new Intent(MainActivity.this,OutputsActivity.class);
                intent.putExtra(TYPE_INTENT_KEY,4);
                intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.removeaudio));
                startActivity(intent);
            }else if(pos==4){
                Intent intent=new Intent(MainActivity.this,OutputsActivity.class);
                intent.putExtra(TYPE_INTENT_KEY,5);
                intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.audioaddedFolder));
                startActivity(intent);
            }else if(pos==5){
                Intent intent=new Intent(MainActivity.this,OutputsActivity.class);
                intent.putExtra(TYPE_INTENT_KEY,6);
                intent.putExtra(FOLDER_NAME_INTENT_KEY,getResources().getString(R.string.videoCreatedFromImages));
                startActivity(intent);
            }
        }
    }
    private boolean checkPermission() {
        boolean check = false;
//        if (SDK_INT >= Build.VERSION_CODES.R) {
//            check= Environment.isExternalStorageManager();
//        } else
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int result = 0;
            result = checkSelfPermission( WRITE_EXTERNAL_STORAGE);
            int result1 = 0;
            result1 = checkSelfPermission( READ_EXTERNAL_STORAGE);
            check= result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
        return check;
    }
    private void requestPermission() {
//        if (SDK_INT >= Build.VERSION_CODES.R) {
//            try {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
//                // startActivityForResult(intent, 2296);
//                mGetContent.launch(intent);
//            } catch (Exception e) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                // startActivityForResult(intent, 2296);
//                mGetContent.launch(intent);
//            }
//        } else {
            //below android 11
            if (SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions( new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUSTCODE2);
            }else {
                startNextActivity(position);
            }
//        }
    }
    public void permisssionDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup=findViewById(android.R.id.content);
        View dialogView= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_manage_storage_permission,viewGroup,false);
        alertDialog.setView(dialogView);
        AlertDialog dialog=alertDialog.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Button btnAllow=dialogView.findViewById(R.id.btnallow);
        Button btnDeny=dialogView.findViewById(R.id.btndeny);

        btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                dialog.dismiss();
            }
        });
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startNextActivity(position);
        } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT >= 23) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                ViewGroup viewGroup=findViewById(android.R.id.content);
                View dialogView= LayoutInflater.from(MainActivity.this).inflate(R.layout.rationale2,viewGroup,false);
                alertDialog.setView(dialogView);
                RelativeLayout buttonAllowRel=(RelativeLayout)dialogView.findViewById(R.id.permissionAllowRel);
                RelativeLayout buttonCancelRel=(RelativeLayout)dialogView.findViewById(R.id.permissionDenyRel);

                AlertDialog dialog=alertDialog.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                buttonAllowRel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        mGetContent.launch(intent);
                    }
                });
                buttonCancelRel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            return;
        }
    }
    private void addElementsInMenu() {
        mainMenuModals.add(new MainMenuModal(AUDIO_CUTTER_ICON, R.drawable.shape_audio_trimmer,getResources().getString(R.string.audiocutter)));
        mainMenuModals.add(new MainMenuModal(VIDEO_CUTTER_ICON, R.drawable.shape_video_trimmer,getResources().getString(R.string.videocutter)));
        mainMenuModals.add(new MainMenuModal(VIDEO_TO_AUDIO_ICON, R.drawable.shape_converter,getResources().getString(R.string.videotoaudio)));
        mainMenuModals.add(new MainMenuModal(REMOVE_AUDIO_ICON, R.drawable.shape_remove_audio,getResources().getString(R.string.audioremove)));
        mainMenuModals.add(new MainMenuModal(ADD_AUDIO_ICON, R.drawable.shape_add_music,getResources().getString(R.string.addmusic)));
//        mainMenuModals.add(new MainMenuModal(CREATE_VIDEO_ICON, R.drawable.shape_image_to_video,getResources().getString(R.string.imagetovideo)));

    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }
    private void exitDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup=findViewById(android.R.id.content);
        View dialogView= LayoutInflater.from(MainActivity.this).inflate(R.layout.exit_dialog,viewGroup,false);
        alertDialog.setView(dialogView);
        AlertDialog dialog=alertDialog.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        ImageButton imageButton=dialogView.findViewById(R.id.imgclose);
        Button yes=dialogView.findViewById(R.id.btnyes);
        Button no=dialogView.findViewById(R.id.btnno);
        ImageView like=dialogView.findViewById(R.id.btnlikeus);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finishAffinity();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }
    private void setItemsInArray() {
        folderModels.add(new FolderModel(getResources().getString(R.string.covertedAudio),constants.CONVERTED_AUDIO_FOLDER_PATH));
        folderModels.add(new FolderModel(getResources().getString(R.string.trimmedAudios),constants.TRIMMED_AUDIO_FOLDER_PATH));
        folderModels.add(new FolderModel(getResources().getString(R.string.trimmedVideo),constants.TRIMMED_VIDEO_FOLDER_PATH));
        folderModels.add(new FolderModel(getResources().getString(R.string.removeAudioFolder),constants.REMOVED_AUDIO_VIDEO_FOLDER_PATH));
        folderModels.add(new FolderModel(getResources().getString(R.string.audioaddedFolder),constants.ADD_AUDIO_TO_VIDEO_FOLDER_PATH));
//        folderModels.add(new FolderModel(getResources().getString(R.string.createvideo),CREATED_VIDEO_WITH_IMAGES_FOLDER_PATH));

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        folderAdpater=new OutputFolderAdapter(this,folderModels);
        binding.outputsRecycler.setLayoutManager(layoutManager);
        binding.outputsRecycler.setAdapter(folderAdpater);

        folderAdpater.onFolderItemClicked(new OnFolderClicked() {
            @Override
            public void onFolderClicked(String path, int pos) {
                isForFolders=true;
                if(checkPermission()){
                    startNextActivity(pos);
                }else{
                    permisssionDialog();
                }
            }
        });
    }


}