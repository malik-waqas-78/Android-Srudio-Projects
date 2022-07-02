package com.example.deviceinfo.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deviceinfo.R;
import com.example.deviceinfo.adapter.CameraPagerAdapter;
import com.example.deviceinfo.adapter.ViewPagerAdapter;
import com.example.deviceinfo.databinding.ActivityCameraInfoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class Camera_info extends AppCompatActivity {
    ActivityCameraInfoBinding binding;
    CameraPagerAdapter adapter;

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
                binding.relativeCameraPermission.setVisibility(View.GONE);
                binding.viewpager.setVisibility(View.VISIBLE);
                binding.viewpager.setAdapter(adapter);
            }else{
//                Toast.makeText(Camera_info.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCameraInfoBinding.inflate(getLayoutInflater());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(binding.getRoot());


        binding.toolbar.setNavigationIcon(R.drawable.ic_drawer_button);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

         adapter = new CameraPagerAdapter(this);
        if(checkPermission()) {
            binding.relativeCameraPermission.setVisibility(View.GONE);
            binding.viewpager.setAdapter(adapter);
        }else{
            binding.viewpager.setVisibility(View.GONE);
            binding.relativeCameraPermission.setVisibility(View.VISIBLE);
        }
        binding.btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisssionDialog();
            }
        });

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.navigation.getMenu().getItem(position).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        binding.navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rear:
                        binding.viewpager.setCurrentItem(0);
                        break;
                    case R.id.front:
                        binding.viewpager.setCurrentItem(1);
                        break;

                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private boolean checkPermission() {
        boolean check = false;
//        if (SDK_INT >= Build.VERSION_CODES.R) {
//            check= Environment.isExternalStorageManager();
//        } else
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int result = 0;
            result = checkSelfPermission( CAMERA);
//            int result1 = 0;
//            result1 = checkSelfPermission( READ_EXTERNAL_STORAGE);
            check= result == PackageManager.PERMISSION_GRANTED ;
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
            requestPermissions( new String[]{CAMERA}, 1001);
        }else {
            binding.viewpager.setAdapter(adapter);
        }
//        }
    }
    public void permisssionDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Camera_info.this);
        ViewGroup viewGroup=findViewById(android.R.id.content);
        View dialogView= LayoutInflater.from(Camera_info.this).inflate(R.layout.dialog_manage_storage_permission,viewGroup,false);
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
            binding.relativeCameraPermission.setVisibility(View.GONE);
            binding.viewpager.setVisibility(View.VISIBLE);
            binding.viewpager.setAdapter(adapter);
        } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT >= 23) {
            if (!shouldShowRequestPermissionRationale(CAMERA)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Camera_info.this);
                ViewGroup viewGroup=findViewById(android.R.id.content);
                View dialogView= LayoutInflater.from(Camera_info.this).inflate(R.layout.rationale2,viewGroup,false);
                alertDialog.setView(dialogView);
                RelativeLayout buttonAllowRel=(RelativeLayout)dialogView.findViewById(R.id.permissionAllowRel);
                RelativeLayout buttonCancelRel=(RelativeLayout)dialogView.findViewById(R.id.permissionDenyRel);
                TextView msgText=(TextView)dialogView.findViewById(R.id.messagetext);

                msgText.setText(getResources().getString(R.string.rationalMesage));

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

}
