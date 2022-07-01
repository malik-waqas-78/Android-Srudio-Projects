package com.example.notesorganizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

/*import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;*/

import com.example.notesorganizer.R;

import static android.view.inputmethod.EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING;

public class SettingsActivity extends AppCompatActivity {
    static boolean word_count, monospaced_font, mail_link, incognito_keyboards, enable_line_wrapup, place_cursor1, show_keyboard_startup1;
    boolean small_radio, medium_radio, larger_radio, extra_large_radio;
    SwitchCompat toogle_btn, mail_link_btn, monospaced_font_btn, keyboards_btn, enable_line_wrap_btn, place_cursor, show_keyboard_startup;
    ConstraintLayout font_size_constraint, font_alignmnet_constraint;
    TextView font_size_text, font_alignmnet_tex;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    RadioButton small, medium, large, extra_large;
    RadioButton left, center, right;
    String left1, center1, right1;
    AddNewDataActivity newManinActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        toogle_btn = findViewById(R.id.count_down_toggleButton);
        mail_link_btn = findViewById(R.id.email_clickable_toogle_btn);
        monospaced_font_btn = findViewById(R.id.use_monospaced_toogle_btn);
        keyboards_btn = findViewById(R.id.enable_keyboards_toogle_btn);
        enable_line_wrap_btn = findViewById(R.id.line_wrap_up_toggle_btn);
        place_cursor = findViewById(R.id.place_cursor);
        toolbar=findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        show_keyboard_startup = findViewById(R.id.show_keyboard_startUp);
        font_size_constraint = findViewById(R.id.font_size_constraint);
        font_alignmnet_constraint = findViewById(R.id.font_alignment_constraint);
        font_size_text = findViewById(R.id.font_size_text);
        font_alignmnet_tex = findViewById(R.id.font_alignmnet_text);
        font_alignmnet_constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alignment_Dialog();
            }
        });
        font_size_constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privacy_Dialog();
                /*new_font_size();*/
            }
        });
        sharedPrefs = getSharedPreferences("com.example.notesorganizer", MODE_PRIVATE);
        editor = sharedPrefs.edit();
        small_radio = sharedPrefs.getBoolean("small_checked", false);
        medium_radio = sharedPrefs.getBoolean("medium_checked", false);
        larger_radio = sharedPrefs.getBoolean("large_checked", false);
        extra_large_radio = sharedPrefs.getBoolean("extra_large_checked", false);
     /*   left1 = sharedPrefs.getBoolean("left", false);
        center1 = sharedPrefs.getBoolean("center", false);
        right1 = sharedPrefs.getBoolean("right", false);*/
        left1 = sharedPrefs.getString("left", "");
        center1 = sharedPrefs.getString("center", "");
        right1 = sharedPrefs.getString("right", "");
        if (left1.equals("left1")) {
            font_alignmnet_tex.setText("Left");
            Log.d("TAG", "onCreate_left: " + left1);
        }
        if (center1.equals("center1")) {
            font_alignmnet_tex.setText("Center");
        }
        if (right1.equals("right1")) {
            font_alignmnet_tex.setText("Right");
        }
        if (small_radio) {
            font_size_text.setText("Small");
        }
        if (medium_radio) {
            font_size_text.setText("Medium");
        }
        if (larger_radio) {
            font_size_text.setText("Large");
        }
        if (extra_large_radio) {
            font_size_text.setText("Extra Large");
        }
        word_count = sharedPrefs.getBoolean("word_count", false);
        mail_link = sharedPrefs.getBoolean("mail_link", false);
        monospaced_font = sharedPrefs.getBoolean("mono_font", false);
        incognito_keyboards = sharedPrefs.getBoolean("incognito_keyboards", false);
        enable_line_wrapup = sharedPrefs.getBoolean("word_count", false);
        place_cursor1 = sharedPrefs.getBoolean("place_cursor", false);
        show_keyboard_startup1 = sharedPrefs.getBoolean("show_keyboard_startup", false);

        if (word_count) {
            AddNewDataActivity.show_count_down.setVisibility(View.VISIBLE);
            Log.d("TAG", "onCreate123: " + word_count);
            toogle_btn.setChecked(true);
        } else {
            AddNewDataActivity.show_count_down.setVisibility(View.GONE);
            Log.d("TAG", "onCreate1234: " + word_count);
            toogle_btn.setChecked(false);
        }
        if (mail_link) {
            AddNewDataActivity.text_details.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
            Log.d("TAG", "onCreate_mail: " + mail_link);
            mail_link_btn.setChecked(true);
        } else {
            mail_link_btn.setChecked(false);
        }
        if (monospaced_font) {
            AddNewDataActivity.text_details.setTextScaleX(1.0f);
            AddNewDataActivity.text_details.setTypeface(Typeface.MONOSPACE);
            Log.d("TAG", "onCreate_monospaced: " + monospaced_font);
            monospaced_font_btn.setChecked(true);
        } else {
            AddNewDataActivity.text_details.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            monospaced_font_btn.setChecked(false);
        }
        if (incognito_keyboards) {
            AddNewDataActivity.text_details.setImeOptions(IME_FLAG_NO_PERSONALIZED_LEARNING);
            keyboards_btn.setChecked(true);
        } else {
            keyboards_btn.setChecked(false);
        }
        if (place_cursor1) {
            AddNewDataActivity.text_details.setSelection(AddNewDataActivity.text_details.getText().length());
            place_cursor.setChecked(true);
        } else {
            AddNewDataActivity.text_details.setSelection(0);
            place_cursor.setChecked(false);
        }
       /* if (show_keyboard_startup1)
        {
            UIUtil.showKeyboard(newManinActivity,NewManinActivity.text_title);
            *//*  InputMethodManager imm = (InputMethodManager) getSystemService(NewManinActivity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(NewManinActivity.text_title, InputMethodManager.SHOW_IMPLICIT);*//*
            show_keyboard_startup.setChecked(true);
        }
        else {
            UIUtil.hideKeyboard(newManinActivity,NewManinActivity.text_title);
            *//*InputMethodManager imm = (InputMethodManager) getSystemService(NewManinActivity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(NewManinActivity.text_title.getWindowToken(), 0);*//*
            show_keyboard_startup.setChecked(false);
        }*/
        if (show_keyboard_startup1) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(AddNewDataActivity.text_title, InputMethodManager.SHOW_FORCED);
            show_keyboard_startup.setChecked(true);
        } else {
            /*UIUtil.hideKeyboard(newManinActivity,NewManinActivity.text_title);*/
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(AddNewDataActivity.text_title.getWindowToken(), 0);
            show_keyboard_startup.setChecked(false);
        }
        toogle_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_checked) {
                if (is_checked) {
                    AddNewDataActivity.show_count_down.setVisibility(View.VISIBLE);
                    editor.putBoolean("word_count", true);
                    editor.apply();
                } else {
                    AddNewDataActivity.show_count_down.setVisibility(View.GONE);
                    editor.putBoolean("word_count", false);
                    editor.apply();

                }
            }
        });
        mail_link_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    AddNewDataActivity.text_details.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
                    editor.putBoolean("mail_link", true);
                    editor.apply();
                } else {
                    AddNewDataActivity.text_details.setAutoLinkMask(1);
                    editor.putBoolean("mail_link", false);
                    editor.apply();
                }
            }
        });
        monospaced_font_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    AddNewDataActivity.text_details.setTextScaleX(1.0f);
                    AddNewDataActivity.text_details.setTypeface(Typeface.MONOSPACE);
                    editor.putBoolean("mono_font", true);
                    editor.apply();
                } else {
                    AddNewDataActivity.text_details.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    editor.putBoolean("mono_font", false);
                    editor.apply();
                }
            }
        });
        keyboards_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    AddNewDataActivity.text_details.setImeOptions(IME_FLAG_NO_PERSONALIZED_LEARNING);
                    editor.putBoolean("incognito_keyboards", true);
                    editor.apply();
                } else {
                    /*NewManinActivity.text_details.setImeOptions(IME_FLAG_NO_PERSONALIZED_LEARNING);
                    editor.putBoolean("mono_font", false);
                    editor.apply();*/
                }
            }
        });
        place_cursor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    AddNewDataActivity.text_details.setSelection(AddNewDataActivity.text_details.getText().length());
                    editor.putBoolean("place_cursor", true);
                    editor.apply();
                } else {
                    AddNewDataActivity.text_details.setSelection(0);
                    editor.putBoolean("place_cursor", false);
                    editor.apply();
                }
            }
        });
        show_keyboard_startup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    /*UIUtil.showKeyboard(newManinActivity,NewManinActivity.text_title);*/
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.showSoftInput(AddNewDataActivity.text_title, InputMethodManager.SHOW_FORCED);
                    editor.putBoolean("show_keyboard_startup", true);
                    editor.apply();
                } else {
                    /*UIUtil.hideKeyboard(newManinActivity,NewManinActivity.text_title);*/
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(AddNewDataActivity.text_title.getWindowToken(), 0);
                    editor.putBoolean("show_keyboard_startup", false);
                    editor.apply();
                }
            }
        });
    }

    private void privacy_Dialog() {
        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;*/
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.new_font_size_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        small = dialog.findViewById(R.id.small_radio1);
        medium = dialog.findViewById(R.id.medium_radio1);
        large = dialog.findViewById(R.id.large_radio1);
        extra_large = dialog.findViewById(R.id.extra_large_radio1);
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewDataActivity.text_details.setTextSize(10);
                font_size_text.setText("Small");
                editor.putBoolean("small_checked", true);
                editor.apply();
                dialog.dismiss();
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewDataActivity.text_details.setTextSize(15);
                font_size_text.setText("Medium");
                editor.putBoolean("medium_checked", true);
                editor.apply();
                dialog.dismiss();
            }
        });
        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (large.isChecked()) {
                    AddNewDataActivity.text_details.setTextSize(20);
                    font_size_text.setText("Large");
                    editor.putBoolean("large_checked", true);
                    editor.apply();
                    dialog.dismiss();
                }
            }
        });
        extra_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extra_large.isChecked()) {
                    AddNewDataActivity.text_details.setTextSize(25);
                    font_size_text.setText("Extra Large");
                    editor.putBoolean("extra_large_checked", true);
                    editor.apply();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void Alignment_Dialog() {
        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;*/
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alignmnt_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        left = dialog.findViewById(R.id.alignment_left);
        center = dialog.findViewById(R.id.alignment_center);
        right = dialog.findViewById(R.id.alignment_right);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*font_alignmnet_tex.setText("Left");
                NewManinActivity.text_details.setGravity(Gravity.START);
                editor.putBoolean("left", true);
                editor.apply();
                dialog.dismiss();*/
                font_alignmnet_tex.setText("Left");
                AddNewDataActivity.text_details.setGravity(Gravity.START);
                editor.putString("left", "left1");
                editor.apply();
                dialog.dismiss();
            }
        });
        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* font_alignmnet_tex.setText("Center");
                NewManinActivity.text_details.setGravity(Gravity.CENTER);
                editor.putBoolean("center", true);
                editor.apply();
                dialog.dismiss();*/
                AddNewDataActivity.text_details.setGravity(Gravity.CENTER);
                font_alignmnet_tex.setText("Center");
                editor.putString("center", "center1");
                editor.apply();
                dialog.dismiss();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* font_alignmnet_tex.setText("Right");
                NewManinActivity.text_details.setGravity(Gravity.END);
                editor.putBoolean("right", true);
                editor.apply();
                dialog.dismiss();*/
                font_alignmnet_tex.setText("Right");
                AddNewDataActivity.text_details.setGravity(Gravity.END);
                editor.putString("right", "right1");
                editor.apply();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

   /* private void new_font_size() {
        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.new_font_size_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
      final   RadioButton radio_small, radio_medium, radio_large, radio_extra_large;
        radio_small = findViewById(R.id.radio_small);
        radio_medium = findViewById(R.id.radio_medium);
        radio_large = findViewById(R.id.radio_large);
        radio_extra_large = findViewById(R.id.radio_extra_large);
        radio_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewManinActivity.text_details.setTextSize(10);
                editor.putBoolean("small_checked", true);
                editor.apply();
                dialog.dismiss();
            }
        });
*/
   /*        radio_medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean medium_checked) {

                NewManinActivity.text_details.setTextSize(14);
                font_size_text.setText("Medium");
                dialog.dismiss();
            }
        });
        radio_large.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean large_checked) {

                NewManinActivity.text_details.setTextSize(20);
                font_size_text.setText("Large");
                dialog.dismiss();
            }
        });
        radio_extra_large.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean radio_extra_large) {

                NewManinActivity.text_details.setTextSize(23);
                font_size_text.setText("Extra");
                dialog.dismiss();
            }
        });*/
   /*
        dialog.show();
    }*/

    @Override
    protected void onResume() {
        if (word_count) {
            AddNewDataActivity.show_count_down.setVisibility(View.VISIBLE);
            Log.d("TAG", "onCreate123: " + word_count);
            toogle_btn.setChecked(true);
        } else {
            AddNewDataActivity.show_count_down.setVisibility(View.GONE);
            Log.d("TAG", "onCreate123: " + word_count);
            toogle_btn.setChecked(false);
        }
        if (mail_link) {
            AddNewDataActivity.text_details.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
            mail_link_btn.setChecked(true);
        } else {
            mail_link_btn.setChecked(false);
        }
        if (monospaced_font) {
            AddNewDataActivity.text_details.setTextScaleX(1.0f);
            AddNewDataActivity.text_details.setTypeface(Typeface.MONOSPACE);
            monospaced_font_btn.setChecked(true);
        } else {
            AddNewDataActivity.text_details.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            monospaced_font_btn.setChecked(false);
        }
        if (incognito_keyboards) {
            AddNewDataActivity.text_details.setImeOptions(IME_FLAG_NO_PERSONALIZED_LEARNING);
            keyboards_btn.setChecked(true);
        } else {
            keyboards_btn.setChecked(false);
        }
        if (place_cursor1) {
            AddNewDataActivity.text_details.setSelection(AddNewDataActivity.text_details.getText().length());
            place_cursor.setChecked(true);
        } else {
            AddNewDataActivity.text_details.setSelection(0);
            place_cursor.setChecked(false);
        }
        if (show_keyboard_startup1) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(AddNewDataActivity.text_title, InputMethodManager.SHOW_FORCED);
            show_keyboard_startup.setChecked(true);
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(AddNewDataActivity.text_title.getWindowToken(), 0);
            show_keyboard_startup.setChecked(false);
        }
        if (small_radio) {
            font_size_text.setText("Small");
        }
        if (medium_radio) {
            font_size_text.setText("Medium");
        }
        if (larger_radio) {
            font_size_text.setText("Large");
        }
        if (extra_large_radio) {
            font_size_text.setText("Extra Large");
        }
        if (left1.equals("left1")) {
            font_alignmnet_tex.setText("Left");
        }
        if (center1.equals("center1")){
            font_alignmnet_tex.setText("Center");
        }
        if (right1.equals("right1")) {
            font_alignmnet_tex.setText("Right");
        }
        super.onResume();
    }

  /*  @Override
    protected void onDestroy() {
        if (word_count) {
            NewManinActivity.show_count_down.setVisibility(View.VISIBLE);
            Log.d("TAG", "onCreate123: " + word_count);
            toogle_btn.setChecked(true);
        } else {
            NewManinActivity.show_count_down.setVisibility(View.GONE);
            Log.d("TAG", "onCreate123: " + word_count);
            toogle_btn.setChecked(false);
        }
        if (mail_link) {
            NewManinActivity.text_details.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
            mail_link_btn.setChecked(true);
        } else {
            mail_link_btn.setChecked(false);
        }
        if (monospaced_font) {
            NewManinActivity.text_details.setTextScaleX(1.0f);
            NewManinActivity.text_details.setTypeface(Typeface.MONOSPACE);
            monospaced_font_btn.setChecked(true);
        } else {
            NewManinActivity.text_details.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            monospaced_font_btn.setChecked(false);
        }
        if (incognito_keyboards) {
            NewManinActivity.text_details.setImeOptions(IME_FLAG_NO_PERSONALIZED_LEARNING);
            keyboards_btn.setChecked(true);
        } else {
            keyboards_btn.setChecked(false);
        }
        if (place_cursor1) {
            NewManinActivity.text_details.setSelection(NewManinActivity.text_details.getText().length());
            place_cursor.setChecked(true);
        } else {
            NewManinActivity.text_details.setSelection(0);
            place_cursor.setChecked(false);
        }
        super.onDestroy();
    }*/
}