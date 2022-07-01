package com.example.notesorganizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notesorganizer.BuildConfig;
import com.example.notesorganizer.modelclass.ModelClass;
import com.example.notesorganizer.helperclass.MyHelperClass;
import com.example.notesorganizer.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;

public class UpdateDataActivity extends AppCompatActivity {
    Realm realm;
    MyHelperClass myHelperClass;
    ModelClass modelClass;
    int position;
    Toolbar toolbar;
    EditText editText_title, editText_details;
    String title,details;
    ImageView btn_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update);
        toolbar=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        editText_title = findViewById(R.id.update_title);
        editText_details = findViewById(R.id.update_details);
        btn_update = findViewById(R.id.btn_update);
        realm = Realm.getDefaultInstance();
        myHelperClass = new MyHelperClass(realm);
        Intent i = getIntent();
        position = i.getIntExtra("numposition", 0);
        modelClass = realm.where(ModelClass.class).equalTo("id", position).findFirst();
        editText_title.setText(modelClass.getTitle());
        editText_details.setText(modelClass.getDetails());
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update_date();
            }
        });
    }
    public void Update_date() {
        realm.beginTransaction();
        modelClass.setTitle(editText_title.getText().toString());
        modelClass.setDetails(editText_details.getText().toString());
        realm.commitTransaction();
        Intent i=new Intent(UpdateDataActivity.this, MainActivity.class);
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.update_menu_items, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*if (item.getItemId() == R.id.settings) {
            Intent i = new Intent(UpdateDataActivity.this, SettingsActivity.class);
            startActivity(i);
        }*/
          if (item.getItemId()==R.id.share)
        {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Notes Organizer");
                String shareMessage = "Install this app To Save your personal data...";
                shareMessage = shareMessage + "\n"+ "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share with..."));
            } catch(Exception e) {
            }
        }
        else if (item.getItemId()==R.id.export_file)
        {
            title=editText_title.getText().toString();
            details=editText_details.getText().toString();
            if (title.isEmpty()&&details.isEmpty())
            {
                editText_title.setError("Enter Title");
                editText_details.setError("Enter Details");
            }
            else
            {
                saved_to_text(title,details);
            }
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
    private void saved_to_text(String text,String details) {
        String timeStamp=new SimpleDateFormat("HHmm_yyyMMdd", Locale.getDefault()).format(System.currentTimeMillis());
        try {
            File path= Environment.getExternalStorageDirectory();
            File dir=new File(path+"/Notes Organizer/");
            dir.mkdir();
            String filename=timeStamp + ".txt";
            File file=new File(dir,filename);
            FileWriter fileWriter=new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.write(details);
            bufferedWriter.close();
            Toast.makeText(UpdateDataActivity.this, filename + " is saved to \n " + dir, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}