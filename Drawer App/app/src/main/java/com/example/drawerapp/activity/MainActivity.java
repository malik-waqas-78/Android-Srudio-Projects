package com.example.drawerapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.divyanshu.draw.BuildConfig;
import com.divyanshu.draw.widget.DrawView;
import com.example.drawerapp.R;
import com.example.drawerapp.classes.DrawingView;
import com.example.drawerapp.constantValues.ConstantClass;
import com.example.drawerapp.stickerTextview.BitmapStickerIcon;
import com.example.drawerapp.stickerTextview.DeleteIconEvent;
import com.example.drawerapp.stickerTextview.Sticker;
import com.example.drawerapp.stickerTextview.StickerView;
import com.example.drawerapp.stickerTextview.TextSticker;
import com.example.drawerapp.stickerTextview.ZoomIconEvent;
import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ColorPickerDialogListener, ColorPickerView.OnColorChangedListener, View.OnTouchListener, PopupMenu.OnMenuItemClickListener {
    public String TAG = "MainActivity";
    DrawView drawView;
    ColorPanelView newColorPanelView;
    ColorPickerView colorPickerView;
    ImageButton redo_btn, undo_btn, color_picker, clear_drawView_btn,erase_btn;
    public static SeekBar drawView_seekBar;
    int color_Name;
    int seekBarDrawView_value;
    public static TextView increment_seekBar_value, decrement_seekBarValue,brush_size;
    int drawView_background_color;
    private static int RESULT_LOAD_IMAGE = 1;
    SharedPreferences preferences;
    float dX;
    float dY;
    int lastAction;
    ConstraintLayout drawView_ConstraintLayout;
    TextSticker textSticker;
    public static final int PERM_RQST_CODE = 110;
    private StickerView stickerView;
    private TextSticker sticker;
    ImageButton add_sticker_text_btn;
    ConstraintLayout color_picker_constraint;
    BitmapStickerIcon deleteIcon, zoomIcon;
    int text_color;
    EditText edit_text_btn;
    String image_Save_name;
    Toolbar toolbar;
    DrawingView drawingView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity1);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        brush_size=findViewById(R.id.brush_size);
        erase_btn=findViewById(R.id.erase_btn);
        erase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erase_btn.setImageResource(R.drawable.erase_btn);
                drawingView.setErase(true);
                drawingView.setBrushSize(15);
            }
        });
        stickerView = findViewById(R.id.main_sticker_view);
        add_sticker_text_btn = findViewById(R.id.main_text_add_btn);
        color_picker_constraint = findViewById(R.id.main_color_picker_constraint);
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
       /* brush_drawView_btn = findViewById(R.id.brush_drawView_btn);
        brush_drawView_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_Name_Dialogbox();
            }
        });*/
        textSticker = new TextSticker(this);
        textSticker.setDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.sticker_transparent_background));
        textSticker.setText("");
        textSticker.setTextColor(Color.BLACK);
        textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        textSticker.resizeText();
        color_picker = findViewById(R.id.main_color_picker_btn);
        drawView = findViewById(R.id.main_draw_view);
        redo_btn = findViewById(R.id.main_redo);
        undo_btn = findViewById(R.id.main_undo);
        // open_gallery = findViewById(R.id.open_file_btn);
        clear_drawView_btn = findViewById(R.id.main_clear_drawView_btn);
        drawView_seekBar = findViewById(R.id.main_drawView_seekBar);
        increment_seekBar_value = findViewById(R.id.main_increment_seekbar_value);
        decrement_seekBarValue = findViewById(R.id.main_decrement_seekBar_value);
        // background_color = findViewById(R.id.background_color);
        // setting_btn = findViewById(R.id.setting_btn);
        drawView_ConstraintLayout = findViewById(R.id.main_draw_view_constraint);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        seekBarDrawView_value = prefs.getInt("seekbar_value", 0);
        color_Name = prefs.getInt("color_3", 0xFF000000);
        drawView_background_color = prefs.getInt("background_color", 0xFF000000);
        Log.d(TAG, "Color Name2 Is: " + drawView_background_color);
        drawView.setColor(color_Name);
        drawView.setStrokeWidth(seekBarDrawView_value);

        /*CHANGE THE COLOR OF THE DRAWABLE FILE*/
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.color_round_background);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, color_Name);
        //color_picker.getBackground().setColorFilter(new LightingColorFilter(color_Name,0));
        Log.d(TAG, "seekBarValue: " + seekBarDrawView_value);
        drawView.setBackgroundColor(drawView_background_color);
        drawView_seekBar.setProgress(seekBarDrawView_value);
        preferences = getSharedPreferences(ConstantClass.shared_Prefernces_Value, 0);
        if (preferences.getBoolean(ConstantClass.brush_size_prefernces_values, false) == true) {
            drawView_seekBar.setVisibility(View.VISIBLE);
            increment_seekBar_value.setVisibility(View.VISIBLE);
            decrement_seekBarValue.setVisibility(View.VISIBLE);
            brush_size.setVisibility(View.VISIBLE);
        } else {
            drawView_seekBar.setVisibility(View.GONE);
            increment_seekBar_value.setVisibility(View.GONE);
            decrement_seekBarValue.setVisibility(View.GONE);
            brush_size.setVisibility(View.GONE);
        }
        if (preferences.getBoolean(ConstantClass.force_portrait_prefernces_values, false) == true) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (preferences.getBoolean(ConstantClass.prevent_prefernces_values, false) == true) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        drawView_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                seekBarDrawView_value = i;
                drawView.setStrokeWidth(i);
                editor.putInt("seekbar_value", i);
                increment_seekBar_value.setText(seekBarDrawView_value + "");
                Log.d(TAG, "onProgressChanged: " + seekBarDrawView_value);
                editor.apply();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        redo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.redo();
            }
        });
        undo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.undo();
            }
        });
        clear_drawView_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.clearCanvas();
                return;
            }
        });
        color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView_Color_Dialog();
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.color_round_background);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, color_Name);
            }
        });
       /* background_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView_Background_Color_Dialoge();
            }
        });*/
     /*   open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });*/
       /* setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });*/
    }
    public void toggleDrawTools(View view, boolean showView) {
        if (showView){
            view.animate().translationY((0));
        }else{
            view.animate().translationY((56));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pop_up_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.save_menu_btn){
            save_image_extension_dialogbox();
        }
        if (id == R.id.share_menu)
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Drawer App");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        if (id == R.id.open_file_menu) {
            Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
        if (id == R.id.background_color_menu) {
            drawView_Background_Color_Dialog();
        }
        if (id == R.id.setting_menu) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
}
    private void saveImage(Bitmap finalBitmap, String imageName) {
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Drawer Folder");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (directory.exists()) {
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            image_Save_name = "Image_" + n + ".jpg";
            File file = new File(directory, imageName);
            Log.d(TAG, "saveImage_imagename: " + imageName);
            if (file.exists()) {
                Toast.makeText(this, "File Already Exist", Toast.LENGTH_SHORT).show();
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                save_clear_dialogbox();
            } catch (Exception e) {
                e.printStackTrace();
            }
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        Log.d(TAG, "onColorSelected: " + String.valueOf(color) + dialogId);
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    private void drawView_Color_Dialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_color_picker);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int initialColor = prefs.getInt("color_3", 0xFF000000);
        colorPickerView = dialog.findViewById(R.id.cpv_color_picker_view);
        final ColorPanelView colorPanelView = dialog.findViewById(R.id.cpv_color_panel_old);
        newColorPanelView = dialog.findViewById(R.id.cpv_color_panel_new);
        Button btnOK = dialog.findViewById(R.id.okButton);
        Button btnCancel = dialog.findViewById(R.id.cancelButton);
        ((ConstraintLayout) colorPanelView.getParent()).setPadding(colorPickerView.getPaddingLeft(), 0,
                colorPickerView.getPaddingRight(), 0);
        colorPickerView.setOnColorChangedListener((ColorPickerView.OnColorChangedListener) this);
        colorPickerView.setColor(initialColor, true);
        colorPanelView.setColor(initialColor);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                edit.putInt("color_3", colorPickerView.getColor());
                edit.apply();
                color_Name = colorPickerView.getColor();
                drawView.setColor(color_Name);
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.color_round_background);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, color_Name);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onColorChanged(int newColor) {
        newColorPanelView.setColor(colorPickerView.getColor());
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                break;
            default:
                return false;
        }
        return true;
    }

    public void save_Name_Dialogbox() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_text_dialogbox);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        edit_text_btn = dialog.findViewById(R.id.add_text_editText);
        final ImageButton name_color_picker_btn = dialog.findViewById(R.id.name_color_picker_btn);
        final Button btn_save_data = dialog.findViewById(R.id.btn_save_text);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        text_color = prefs.getInt("save_text_color", 0xFF000000);
        /*CHANGE THE COLOR OF THE DRAWABLE FILE*/
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.color_text_round_background);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, text_color);
        btn_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit_Name = edit_text_btn.getText().toString().trim();
                if (edit_text_btn.getText().toString().isEmpty()) {
                    edit_text_btn.setError("Field can not be empty !");
                    return;
                }
                final TextSticker sticker = new TextSticker(MainActivity.this);
                sticker.setText(edit_Name);
                sticker.setTextColor(text_color);
                edit_text_btn.setTextColor(text_color);
                sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                sticker.resizeText();
                String s = String.valueOf(stickerView.getIcons().toArray().length);
                stickerView.addSticker(sticker);
                /*CHANGE THE COLOR OF THE DRAWABLE FILE*/
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.color_text_round_background);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, text_color);
                dialog.dismiss();
            }
        });
        name_color_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_name_dialogbox();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_menu:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Drawer App");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.open_file_menu:
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
            case R.id.background_color_menu:
                drawView_Background_Color_Dialog();
                break;
            case R.id.setting_menu:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            drawView.setBackground(Drawable.createFromPath(picturePath));
        }
    }

    private void drawView_Background_Color_Dialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_color_picker);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int initialColor = prefs.getInt("background_color", 0xFF000000);
        colorPickerView = dialog.findViewById(R.id.cpv_color_picker_view);
        final ColorPanelView colorPanelView = dialog.findViewById(R.id.cpv_color_panel_old);
        newColorPanelView = dialog.findViewById(R.id.cpv_color_panel_new);
        Button btnOK = dialog.findViewById(R.id.okButton);
        Button btnCancel = dialog.findViewById(R.id.cancelButton);
        ((ConstraintLayout) colorPanelView.getParent()).setPadding(colorPickerView.getPaddingLeft(), 0,
                colorPickerView.getPaddingRight(), 0);
        colorPickerView.setOnColorChangedListener((ColorPickerView.OnColorChangedListener) this);
        colorPickerView.setColor(initialColor, true);
        colorPanelView.setColor(initialColor);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                edit.putInt("background_color", colorPickerView.getColor());
                edit.apply();
                drawView_background_color = colorPickerView.getColor();
                drawView.setBackgroundColor(drawView_background_color);
                Log.d(TAG, "Color Name1  Is: " + drawView_background_color);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void exit_confirm_dialogbox() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.exit_confirm_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button ok = dialog.findViewById(R.id.btn_yes);
        final Button no = dialog.findViewById(R.id.btn_no);
        final Button rate_us = dialog.findViewById(R.id.btn_rate_us);
        rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateApp();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
        dialog.show();
    }
    public void save_image_extension_dialogbox() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.save_dialogbox_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final TextView image_name_text = dialog.findViewById(R.id.image_name_text);
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        image_Save_name = "Image_" + n;
        final String png_extension = "Image_" + n + ".png";
        final String jpg_extension = "Image_" + n + ".jpg";
        final String svg_extension = "Image_" + n + ".svg";
        image_name_text.setText(image_Save_name);
        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_png_extension:
                        drawView_ConstraintLayout.setDrawingCacheEnabled(true);
                        drawView_ConstraintLayout.buildDrawingCache();
                        Bitmap bm_png = drawView_ConstraintLayout.getDrawingCache();
                        ByteArrayOutputStream bytes_png = new ByteArrayOutputStream();
                        bm_png.compress(Bitmap.CompressFormat.JPEG, 100, bytes_png);
                        saveImage(bm_png, png_extension);
                        dialog.dismiss();
                        break;
                    case R.id.radio_jpg_extension:
                        drawView_ConstraintLayout.setDrawingCacheEnabled(true);
                        drawView_ConstraintLayout.buildDrawingCache();
                        Bitmap bm_jpg = drawView_ConstraintLayout.getDrawingCache();
                        ByteArrayOutputStream bytes_jpg = new ByteArrayOutputStream();
                        bm_jpg.compress(Bitmap.CompressFormat.JPEG, 100, bytes_jpg);
                        saveImage(bm_jpg, jpg_extension);
                        dialog.dismiss();
                        break;
                    case R.id.radio_svg_extension:
                        drawView_ConstraintLayout.setDrawingCacheEnabled(true);
                        drawView_ConstraintLayout.buildDrawingCache();
                        Bitmap bm_svg = drawView_ConstraintLayout.getDrawingCache();
                        ByteArrayOutputStream bytes_svg = new ByteArrayOutputStream();
                        bm_svg.compress(Bitmap.CompressFormat.JPEG, 100, bytes_svg);
                        saveImage(bm_svg, svg_extension);
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.show();
    }

    public void save_clear_dialogbox() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.save_suucess_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button btn_okay = dialog.findViewById(R.id.save_okay);
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + Objects.requireNonNull(MainActivity.this).getPackageName()));
        }
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    private void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details?id=");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=");
            startActivity(rateIntent);
        }
    }

    @Override
    public void onBackPressed() {
        exit_confirm_dialogbox();
    }

    private void save_name_dialogbox() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.save_text_color_picker);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int initialColor = prefs.getInt("save_text_color", 0xFF000000);
        colorPickerView = dialog.findViewById(R.id.save_text_color_picker_view);
        final ColorPanelView colorPanelView = dialog.findViewById(R.id.save_text_color_panel_old);
        newColorPanelView = dialog.findViewById(R.id.save_text_color_panel_new);
        Button btnOK = dialog.findViewById(R.id.save_text_okButton);
        Button btnCancel = dialog.findViewById(R.id.save_text_cancelButton);
        ((ConstraintLayout) colorPanelView.getParent()).setPadding(colorPickerView.getPaddingLeft(), 0,
                colorPickerView.getPaddingRight(), 0);
        colorPickerView.setOnColorChangedListener((ColorPickerView.OnColorChangedListener) this);
        colorPickerView.setColor(initialColor, true);
        colorPanelView.setColor(initialColor);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.color_text_round_background);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, text_color);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                edit.putInt("save_text_color", colorPickerView.getColor());
                edit.apply();
                text_color = colorPickerView.getColor();
                Log.d(TAG, "Color Name1  Is: " + drawView_background_color);
                /*CHANGE THE COLOR OF THE DRAWABLE FILE*/
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.color_text_round_background);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, text_color);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}