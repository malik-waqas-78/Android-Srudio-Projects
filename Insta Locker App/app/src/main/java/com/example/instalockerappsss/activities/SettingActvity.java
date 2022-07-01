package com.example.instalockerappsss.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.example.instalockerappsss.R;
import com.example.instalockerappsss.database.RealmHelper;
import com.example.instalockerappsss.modelclass.ModelClass;
import com.example.instalockerappsss.service.AccessabiltyServiceClass;

import java.util.ArrayList;

import io.realm.Realm;

public class SettingActvity extends AppCompatActivity {

    ConstraintLayout btn_save_email, btn_change_password;
    Switch lock_permission_switch;
    EditText edit_email;
    ModelClass md = new ModelClass();
    Realm realm;
    RealmHelper realmHelper;
    ConstraintLayout recovery_mail_consraint;
    TextView show_email_text;
    ArrayList<ModelClass> modelClassArrayList = new ArrayList<>();
    public String TAG = "Setting Activity";
    SharedPreferences sharedPreferences;
    public int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        btn_change_password = findViewById(R.id.btn_change_password);
        lock_permission_switch = findViewById(R.id.lock_permission_switch);if (!isAccessibilitySettingsOn(SettingActvity.this)) {
            lock_permission_switch.setChecked(false);
        } else {
            lock_permission_switch.setChecked(true);
        }
        lock_permission_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //   startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
                    if (isAccessibilitySettingsOn(SettingActvity.this)) {
                        startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
                    }
                } else {
                    startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
                }
            }
        });
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActvity.this, Old_Password.class);
                startActivity(i);
            }
        });
        recovery_mail_consraint = findViewById(R.id.recovery_mail_consraint);
        show_email_text = findViewById(R.id.show_email_text);
        sharedPreferences = getSharedPreferences("password", 0);
        Realm.init(SettingActvity.this);
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm, this);
        modelClassArrayList = realmHelper.retrieveSavedEmail();
        Intent intent = getIntent();
        final String pin = intent.getStringExtra("password");
        Log.d(TAG, "pin Setting Acivity: " + pin);
        /*String email = String.valueOf(realmHelper.retrieveSavedEmail());*/
        for (int i = 0; i < modelClassArrayList.size(); i++) {
            //System.out.println(list.get(i));
            show_email_text.setText(modelClassArrayList.get(i).getEmail());
        }
        recovery_mail_consraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActvity.this, pin, Toast.LENGTH_SHORT).show();
                Intent it = new Intent(Intent.ACTION_SEND);
                it.putExtra(Intent.EXTRA_EMAIL, show_email_text.getText().toString());
                it.putExtra(Intent.EXTRA_SUBJECT, "Recover Your Email");
                it.putExtra(Intent.EXTRA_TEXT, pin);
                it.setType("message/rfc822");
            }
        });
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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(SettingActvity.this, MainActivity.class);
        startActivity(i);
    }
}