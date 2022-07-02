package com.example.deviceinfo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.deviceinfo.R;
import com.example.deviceinfo.adapter.General_info_Adapter;
import com.example.deviceinfo.databinding.ActivityInfoGeneralBinding;
import com.example.deviceinfo.models.General_info_Model;

import java.util.ArrayList;


public class Activity_Info_General extends AppCompatActivity {
    ActivityInfoGeneralBinding binding;
    ArrayList<General_info_Model>  info_models=new ArrayList<>();
    String hardwareName;
    General_info_Adapter general_info_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityInfoGeneralBinding.inflate(getLayoutInflater());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(binding.getRoot());

//        try {
//            Process proc = Runtime.getRuntime().exec("cat /proc/cpuinfo");
//            InputStream is = proc.getInputStream();
//
//            BufferedReader r = new BufferedReader(new InputStreamReader(is));
//            StringBuilder total = new StringBuilder();
//            for (String line; (line = r.readLine()) != null; ) {
//                total.append(line).append('\n');
//            }
//            hardwareName=total.toString();
//
//        }
//        catch (IOException e) {
//            Log.e("TAG", "------ getCpuInfo " + e.getMessage());
//        }

        hardwareName= "Hardware: "+Build.HARDWARE;

        binding.chipSetName.setText(hardwareName);

        if(!(hardwareName==null || hardwareName.isEmpty())) {
            if (hardwareName.toLowerCase().contains("exynos")) {
                binding.chipSetImage.setImageResource(R.drawable.exynos);
            } else if (hardwareName.toLowerCase().contains("dimen")) {
                binding.chipSetImage.setImageResource(R.drawable.mediatech_dimen);
            } else if (hardwareName.toLowerCase().contains("helio")) {
                binding.chipSetImage.setImageResource(R.drawable.mediatech_helio);
            } else if (hardwareName.toLowerCase().contains("snapdragon")) {
                binding.chipSetImage.setImageResource(R.drawable.snapdragon_chip);
            } else if (hardwareName.toLowerCase().contains("kirin")) {
                binding.chipSetImage.setImageResource(R.drawable.kirin);
            }
        }
        addInfoData();
    }
    private void addInfoData() {
        info_models.add(new General_info_Model(getResources().getString(R.string.serial),Build.SERIAL));
        info_models.add(new General_info_Model(getResources().getString(R.string.model),Build.MODEL));
        info_models.add(new General_info_Model(getResources().getString(R.string.id),Build.ID));
        info_models.add(new General_info_Model(getResources().getString(R.string.manufacturer),Build.MANUFACTURER));
        info_models.add(new General_info_Model(getResources().getString(R.string.brand),Build.BRAND));
        info_models.add(new General_info_Model(getResources().getString(R.string.type),Build.TYPE));
        info_models.add(new General_info_Model(getResources().getString(R.string.user),Build.USER));
        info_models.add(new General_info_Model(getResources().getString(R.string.base),Build.VERSION_CODES.BASE+""));
        info_models.add(new General_info_Model(getResources().getString(R.string.incremental),Build.VERSION.INCREMENTAL+""));
//        info_models.add(new General_info_Model(getResources().getString(R.string.sdk),Build.SERIAL));
        info_models.add(new General_info_Model(getResources().getString(R.string.board),Build.BOARD));
//        info_models.add(new General_info_Model(getResources().getString(R.string.brand),Build.SERIAL));
        info_models.add(new General_info_Model(getResources().getString(R.string.host),Build.HOST));
//        info_models.add(new General_info_Model(getResources().getString(R.string.virsionCode),Build.SERIAL));
        info_models.add(new General_info_Model(getResources().getString(R.string.hardware),Build.HARDWARE));
        general_info_adapter=new General_info_Adapter(info_models,Activity_Info_General.this);
        binding.recyclerInfo.setLayoutManager(new GridLayoutManager(Activity_Info_General.this,1));
        binding.recyclerInfo.setAdapter(general_info_adapter);
    }
}