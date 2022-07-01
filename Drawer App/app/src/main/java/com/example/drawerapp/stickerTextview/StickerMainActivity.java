package com.example.drawerapp.stickerTextview;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.drawerapp.R;
import com.example.drawerapp.activity.MainActivity;
import com.example.drawerapp.stickerTextview.util.FileUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class StickerMainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int PERM_RQST_CODE = 110;
    private StickerView stickerView;
    private TextSticker sticker;
    Button add_sticker_text_btn;
    BitmapStickerIcon deleteIcon, flipIcon, zoomIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sticker);
        stickerView = findViewById(R.id.sticker_view);
        add_sticker_text_btn = findViewById(R.id.add_sticker_text_btn);
        deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon));
        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);
        sticker = new TextSticker(this);
        sticker.setDrawable(ContextCompat.getDrawable(this,
                R.color.color_brown));
        sticker.setTextColor(Color.BLACK);
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();
        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //stickerView.removeAllSticker();
                if (sticker instanceof TextSticker) {
                    //((TextSticker) sticker).setTextColor(Color.RED);
                    stickerView.replace(sticker);
                    stickerView.invalidate();
                }
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerTouchedDown");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_RQST_CODE);
        } else {
        }
        add_sticker_text_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_Name_Dialogbox();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //loadSticker();
        }
    }
    public void save_Name_Dialogbox() {
        final Dialog dialog = new Dialog(StickerMainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.save_edittext_value_dialogbox);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final EditText edit_text_btn = dialog.findViewById(R.id.save_data_editText);
        final Button btn_save_data = dialog.findViewById(R.id.save_data_btn);
        btn_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit_Name = edit_text_btn.getText().toString().trim();
                if (edit_text_btn.getText().toString().isEmpty()) {
                    edit_text_btn.setError("Field can not be empty !");
                    return;
                }
                final TextSticker sticker = new TextSticker(StickerMainActivity.this);
                sticker.setText(edit_Name);
                sticker.setTextColor(Color.BLUE);
                sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                sticker.resizeText();
                String s = String.valueOf(stickerView.getIcons().toArray().length);
                stickerView.addSticker(sticker);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}