package com.example.deviceinfo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.deviceinfo.R;
import com.example.deviceinfo.adapter.General_info_Adapter;
import com.example.deviceinfo.databinding.ActivityBatteryInfoBinding;
import com.example.deviceinfo.models.General_info_Model;

import java.util.ArrayList;

public class Battery_Info extends AppCompatActivity {

    ActivityBatteryInfoBinding binding;
    BatteryManager batteryManager;
    float batteryTemp;
    IntentFilter intentfilter;
    General_info_Adapter general_info_adapter;

    ArrayList<General_info_Model> general_info_models=new ArrayList<>();

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBatteryInfoBinding.inflate(getLayoutInflater());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationIcon(R.drawable.ic_drawer_button);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        batteryManager= (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
        intentfilter = new IntentFilter();
        intentfilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentfilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentfilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(broadcastreceiver,intentfilter);


        general_info_models.add(new General_info_Model(getResources().getString(R.string.technology),""));
        general_info_models.add(new General_info_Model(getResources().getString(R.string.health)," "));
        general_info_models.add(new General_info_Model(getResources().getString(R.string.status)," "));
        general_info_models.add(new General_info_Model(getResources().getString(R.string.powerSource)," "));
        general_info_models.add(new General_info_Model(getResources().getString(R.string.Temperatue),""));
        general_info_models.add(new General_info_Model(getResources().getString(R.string.voltage)," "));
        general_info_models.add(new General_info_Model(getResources().getString(R.string.capacity),""));

        general_info_adapter=new General_info_Adapter(general_info_models,this);
        binding.recyclerBatteryInfo.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerBatteryInfo.setAdapter(general_info_adapter);


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

    private void updateBatteryData(Intent intent) {

        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);

        if (present) {

//            batteryTemp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;




            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            int healthLbl = -1;

            switch (health) {
                case BatteryManager.BATTERY_HEALTH_COLD:
                    healthLbl = R.string.battery_health_cold;
                    break;

                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthLbl = R.string.battery_health_dead;
                    break;

                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthLbl = R.string.battery_health_good;
                    break;

                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthLbl = R.string.battery_health_over_voltage;
                    break;

                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthLbl = R.string.battery_health_overheat;
                    break;

                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthLbl = R.string.battery_health_unspecified_failure;
                    break;

                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                default:
                    break;
            }

            if (healthLbl != -1) {


                general_info_models.get(1).setInfo(getString(healthLbl));
                general_info_adapter.notifyItemChanged(1);
            }

            // Calculate Battery Pourcentage ...
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                binding.textBatteryPercent.setText(batteryPct+"%");
                binding.pbarBattery.setProgress(batteryPct);
                binding.seekbar11.setProgress(batteryPct);
                if(batteryPct>30){
//                    binding.seekbar11.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progressbargreen));
                    binding.seekbar11.getIndeterminateDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                }else if(batteryPct<=29 && batteryPct>10){
                    binding.seekbar11.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progressbar));
                }else{
                    binding.seekbar11.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progressbarred));
                }
            }

            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            int pluggedLbl = R.string.battery_plugged_none;

            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    pluggedLbl = R.string.battery_plugged_wireless;
                    break;

                case BatteryManager.BATTERY_PLUGGED_USB:
                    pluggedLbl = R.string.battery_plugged_usb;
                    break;

                case BatteryManager.BATTERY_PLUGGED_AC:
                    pluggedLbl = R.string.battery_plugged_ac;
                    break;

                default:
                    pluggedLbl = R.string.battery_plugged_none;
                    break;
            }

            // display plugged status ...
            general_info_models.get(2).setInfo(getString(pluggedLbl));
            general_info_adapter.notifyItemChanged(2);

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int statusLbl = R.string.battery_status_discharging;

            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    statusLbl = R.string.battery_status_charging;
                    break;

                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    statusLbl = R.string.battery_status_discharging;
                    break;

                case BatteryManager.BATTERY_STATUS_FULL:
                    statusLbl = R.string.battery_status_full;
                    break;

                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    statusLbl = -1;
                    break;

                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                default:
                    statusLbl = R.string.battery_status_discharging;
                    break;
            }

            if (statusLbl != -1) {
                general_info_models.get(3).setInfo(getString(statusLbl));
                general_info_adapter.notifyItemChanged(3);
//                chargingStatusTv.setText("Battery Charging Status : " + getString(statusLbl));
            }

            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);

                if (!"".equals(technology)) {
                    general_info_models.get(0).setInfo(technology);
                    general_info_adapter.notifyItemChanged(0);
                }
            }

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

            if (temperature > 0) {
                float temp = ((float) temperature) / 10f;
                general_info_models.get(4).setInfo(temp +" "+ (char) 0x00B0 +"C");
                general_info_adapter.notifyItemChanged(4);
            }

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

            if (voltage > 0) {
//                voltageTv.setText("Voltage : " + );
                general_info_models.get(5).setInfo(voltage + " mV");
                general_info_adapter.notifyItemChanged(5);
            }

            long capacity = (long) getBatteryCapacity(this);

            if (capacity > 0) {
                general_info_models.get(6).setInfo(capacity + " mah");
                general_info_adapter.notifyItemChanged(6);
            }

        } else {

        }

    }
}