package com.example.instalockerappsss.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.instalockerappsss.R;
import com.example.instalockerappsss.activities.New_Password;
import com.example.instalockerappsss.activities.Old_Password;
import com.example.instalockerappsss.activities.SplashScreenActivity;
import com.example.instalockerappsss.service.AccessabiltyServiceClass;

public class SettingFragment extends Fragment {
    ConstraintLayout appLock_ConstraintLayout, setLockPermission_ConstraintLayout, changePassword_ConstraintLayout, fingerPrint_ConstraintLayout;
    Switch applock_switch, setLockPermission_switch, fingerPrint_switch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        appLock_ConstraintLayout = view.findViewById(R.id.appLock_constraint);
        setLockPermission_ConstraintLayout = view.findViewById(R.id.setLock_Permission_constraint);
        changePassword_ConstraintLayout = view.findViewById(R.id.password_constraint);
        fingerPrint_ConstraintLayout = view.findViewById(R.id.finger_print_consraint);
        applock_switch = view.findViewById(R.id.switch_appLock);
        setLockPermission_switch = view.findViewById(R.id.switch_setLockPermission);
        fingerPrint_switch = view.findViewById(R.id.switch_fingerPrint);
        final SharedPreferences preferences = getContext().getSharedPreferences("Lock", 0);
        final SharedPreferences.Editor editor=preferences.edit();
        if (preferences.getBoolean("fingerLock",false)==true){
            fingerPrint_switch.setChecked(true);
        }
        if (preferences.getBoolean("fingerLock",false)==false){
            fingerPrint_switch.setChecked(false);
        }
        if (preferences.getBoolean("setLockPermission",false)==true){
            setLockPermission_switch.setChecked(true);
        }
        if (preferences.getBoolean("setLockPermission",false)==false){
            setLockPermission_switch.setChecked(false);
        }
        if (preferences.getBoolean("setAppPermission",false)==true){
            applock_switch.setChecked(true);
        }
        if (preferences.getBoolean("setAppPermission",false)==false){
            applock_switch.setChecked(false);
        }
        fingerPrint_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putBoolean("fingerLock",true);
                    editor.apply();
                    fingerPrint_switch.setChecked(true);
                }
                else{
                    editor.putBoolean("fingerLock",false);
                    editor.apply();
                    fingerPrint_switch.setChecked(false);
                }
            }
        });
        changePassword_ConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Old_Password.class);
                startActivity(i);
            }
        });
        setLockPermission_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAccessibilitySettingsOn(getContext())) {
                    startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
                }
            }
        });
        setLockPermission_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setLockPermission_switch.setChecked(true);
                    editor.putBoolean("setLockPermission",true);
                    editor.commit();
                }
                else {
                    setLockPermission_switch.setChecked(false);
                    editor.putBoolean("setLockPermission",false);
                    editor.commit();
                }
            }
        });
        applock_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    applock_switch.setChecked(true);
                    editor.putBoolean("setAppPermission",true);
                    editor.commit();
                }
                else {
                    applock_switch.setChecked(false);
                    editor.putBoolean("setAppPermission",false);
                    editor.commit();
                }
            }
        });
        return view;
    }
    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName()
                + "/" + AccessabiltyServiceClass.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v("TAG", "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("TAG",
                    "Error finding setting, default accessibility to not found: "
                            + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
                ':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessabilityService = mStringColonSplitter.next();
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}