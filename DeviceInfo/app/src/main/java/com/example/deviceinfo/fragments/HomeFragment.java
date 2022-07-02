package com.example.deviceinfo.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deviceinfo.R;
import com.example.deviceinfo.databinding.FragmentHomeBinding;
import com.example.deviceinfo.models.FitChartValue;
import com.example.deviceinfo.utils.SharedPrefHelper;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnChartValueSelectedListener {

    FragmentHomeBinding binding;
    List<FitChartValue> values ;
    BatteryManager batteryManager;
    Context context;
    float batteryTemp;
    IntentFilter intentfilter;
    SensorManager sensorManager;
    List<Sensor> deviceSensors;
    SharedPrefHelper sharedPrefHelper;


    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            batteryTemp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
            binding.textbatteryPercent.setText(intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0)+"%");
            binding.textBatteryTemp.setText(batteryTemp +" "+ (char) 0x00B0 +"C");

            binding.textBatteryType.setText((int)getBatteryCapacity(context)+" mah");

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.context=context;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        values = new ArrayList<>();
        batteryManager= (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        intentfilter= new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sharedPrefHelper=new SharedPrefHelper(context);
         deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,parent,false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        values.clear();
        binding.percentageSelected.setText(getUsedMemorySize()+"");
        binding.pieChart.setMinValue(0f);
        binding.pieChart.setMaxValue(100f);
        values.add(new FitChartValue(getUsedMemorySize(),getResources().getColor(R.color.pieChartBarColor)));
        binding.pieChart.setValues(values);
        context.registerReceiver(broadcastreceiver,intentfilter);
        binding.testnumbers.setText(sharedPrefHelper.getTestMadefromShared()+"");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textVersionCode.setText(Build.VERSION.RELEASE+"");
        binding.textDeviceName.setText(Build.MODEL+" " +Settings.Global.getString(context.getContentResolver(), "device_name"));
        binding.textnumberSensors.setText(deviceSensors.size()+"");


    }

    public double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            batteryCapacity=getBatteryCapacitybackup(context);
        }
        if(batteryCapacity==0){
            batteryCapacity=getBatteryCapacitybackup(context);
        }

        return batteryCapacity;

    }
    public long getBatteryCapacitybackup(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            Integer chargeCounter = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Integer capacity = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if(chargeCounter == Integer.MIN_VALUE || capacity == Integer.MIN_VALUE)
                return 0;

            return (chargeCounter/capacity) *100;
        }
        return 0;
    }
    public int getUsedMemorySize() {
        double freeSize = 0.0;
        double totalSize = 0.0;
        double usedSize = -1.0;
        int usedPercent=0;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
            usedPercent= (int) ((usedSize/totalSize)*100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usedPercent;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
        context.unregisterReceiver(broadcastreceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}