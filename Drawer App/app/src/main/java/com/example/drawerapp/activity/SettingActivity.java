package com.example.drawerapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.drawerapp.R;
import com.example.drawerapp.constantValues.ConstantClass;
import com.example.drawerapp.fragment.TextEditorDialogFragment;
import com.example.drawerapp.stickerTextview.StickerMainActivity;

import java.security.CodeSigner;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    Switch brush_size_tool_switch, prevent_switch;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ImageButton button_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.setting_activity);
        button_back_arrow=findViewById(R.id.btn_back_arrow);
        button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.super.onBackPressed();
            }
        });
        prevent_switch = findViewById(R.id.prevent_switch);
        brush_size_tool_switch = findViewById(R.id.brush_size_tool_switch);
        preferences = getSharedPreferences(ConstantClass.shared_Prefernces_Value, 0);
        editor = preferences.edit();
        if (preferences.getBoolean(ConstantClass.brush_size_prefernces_values, false) == true) {
            brush_size_tool_switch.setChecked(true);
        } else if (preferences.getBoolean(ConstantClass.brush_size_prefernces_values, false) == false) {
            brush_size_tool_switch.setChecked(false);
        }
        if (preferences.getBoolean(ConstantClass.prevent_prefernces_values, false) == true) {
            prevent_switch.setChecked(true);
        } else {
            prevent_switch.setChecked(false);
        }
        brush_size_tool_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    brush_size_tool_switch.setChecked(true);
                    editor.putBoolean(ConstantClass.brush_size_prefernces_values, true);
                    editor.apply();
                } else {
                    brush_size_tool_switch.setChecked(false);
                    editor.putBoolean(ConstantClass.brush_size_prefernces_values, false);
                    editor.apply();

                }
            }
        });
        prevent_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    prevent_switch.setChecked(true);
                    editor.putBoolean(ConstantClass.prevent_prefernces_values, true);
                    editor.apply();
                } else {
                    prevent_switch.setChecked(false);
                    editor.putBoolean(ConstantClass.prevent_prefernces_values, false);
                    editor.apply();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (preferences.getBoolean(ConstantClass.brush_size_prefernces_values, false) == true) {
            MainActivity.drawView_seekBar.setVisibility(View.VISIBLE);
            MainActivity.increment_seekBar_value.setVisibility(View.VISIBLE);
            MainActivity.decrement_seekBarValue.setVisibility(View.VISIBLE);
        } else if (preferences.getBoolean(ConstantClass.brush_size_prefernces_values, false) == false) {
            MainActivity.drawView_seekBar.setVisibility(View.GONE);
            MainActivity.increment_seekBar_value.setVisibility(View.GONE);
            MainActivity.decrement_seekBarValue.setVisibility(View.GONE);
        }
        if (preferences.getBoolean(ConstantClass.prevent_prefernces_values, false) == true) {
            MainActivity.isPrevent_Sleep = true;
        } else if (preferences.getBoolean(ConstantClass.prevent_prefernces_values, true) == false) {
            MainActivity.isPrevent_Sleep = false;
        }
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();*/
        Intent i = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(i);
    }
}