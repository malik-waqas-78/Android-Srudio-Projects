package com.example.notesorganizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesorganizer.BuildConfig;
import com.example.notesorganizer.modelclass.ModelClass;
import com.example.notesorganizer.helperclass.MyHelperClass;
import com.example.notesorganizer.R;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;

import static android.view.inputmethod.EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING;

public class AddNewDataActivity extends AppCompatActivity {
    static EditText text_title, text_details;
    ImageView btn_save_database;
    MyHelperClass myHelperClass;
    String title, details;
    Toolbar toolbar;
    Realm realm;
    static TextView show_count_down;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_new_data);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        myHelperClass = new MyHelperClass(realm);
        text_details = findViewById(R.id.note_details);
        text_title = findViewById(R.id.note_title);
        show_count_down = findViewById(R.id.show_count_down);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        btn_save_database = findViewById(R.id.save_database_btn);


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = df.format(c.getTime());
        text_details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = text_details.getText().toString();
                text = text.replace("\n", " ");
                String[] textarray = text.split(" ");
                show_count_down.setText("" + textarray.length);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_save_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = text_title.getText().toString().trim();
                details = text_details.getText().toString().trim();
                if (title.isEmpty() && (details.isEmpty())) {
                    text_title.setError("Enter Title");
                    text_details.setError("Enter Details");
                } else {
                    save_data();
                    Toast.makeText(AddNewDataActivity.this, "successfully saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent i = new Intent(AddNewDataActivity.this, SettingsActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.share) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Notes Organizer");
                String shareMessage = "Install this app To Save your personal data..";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share with..."));
            } catch (Exception e) {
            }
        } else if (item.getItemId() == R.id.export_file) {
            title = text_title.getText().toString();
            details = text_details.getText().toString();
            if (title.isEmpty() && details.isEmpty()) {
                text_title.setError("Enter Title");
                text_details.setError("Enter Details");
            } else {
                saved_to_text(title, details);
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void saved_to_text(String text, String details) {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        try {
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/Notes Organizer/");
            dir.mkdir();
            String filename = timeStamp + ".txt";
            File file = new File(dir, filename);
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.write(details);
            bufferedWriter.close();
            Toast.makeText(AddNewDataActivity.this, filename + " is saved to \n " + dir, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save_data() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxid = realm.where(ModelClass.class).max("id");
                int newKey = (maxid == null) ? 1 : maxid.intValue() + 1;
                ModelClass modelClass = realm.createObject(ModelClass.class, newKey);
                modelClass.setTitle(text_title.getText().toString());
                modelClass.setDetails(text_details.getText().toString());
                modelClass.setDate(formattedDate);
                realm.copyToRealm(modelClass);
                /*Intent i = new Intent(AddNewDataActivity.this, MainActivity.class);
                startActivity(i);*/
            }
        });
    }
    @Override
    protected void onResume() {
        if (SettingsActivity.word_count) {
            show_count_down.setVisibility(View.VISIBLE);
        } else {
            show_count_down.setVisibility(View.GONE);
        }
        if (SettingsActivity.mail_link) {
            AddNewDataActivity.text_details.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        }
        if (SettingsActivity.monospaced_font) {
            AddNewDataActivity.text_details.setTextScaleX(1.0f);
            AddNewDataActivity.text_details.setTypeface(Typeface.MONOSPACE);
        } else {
            AddNewDataActivity.text_details.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        if (SettingsActivity.incognito_keyboards) {
            AddNewDataActivity.text_details.setImeOptions(IME_FLAG_NO_PERSONALIZED_LEARNING);
        }
        if (SettingsActivity.place_cursor1) {
            text_details.setSelection(text_details.getText().length());
        } else {
            text_details.setSelection(0);
        }
        if (SettingsActivity.show_keyboard_startup1) {
            /*UIUtil.showKeyboard(AddNewDataActivity.this,NewManinActivity.text_title);*/
            InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(AddNewDataActivity.text_title, InputMethodManager.SHOW_FORCED);
        } else {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(AddNewDataActivity.text_title.getWindowToken(), 0);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(AddNewDataActivity.this,MainActivity.class);
        startActivity(i);
    }
}