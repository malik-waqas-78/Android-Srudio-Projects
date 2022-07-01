package com.example.notesorganizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notesorganizer.BuildConfig;
import com.example.notesorganizer.modelclass.ModelClass;
import com.example.notesorganizer.R;

import io.realm.Realm;

public class FullDataShowActivity extends AppCompatActivity {
    Realm realm;
    TextView textView_title, textView_details,textView_date;
    ImageView edit_btn;
    /*ImageButton delete_btn;*/
    int position;
    ModelClass modelClass;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_text_show);
        toolbar=findViewById(R.id.main_delete_toolbar);
        setSupportActionBar(toolbar);
        textView_title = findViewById(R.id.full_title_show);
        textView_details = findViewById(R.id.full_details_show);
        textView_date=findViewById(R.id.full_date_show);
        edit_btn = findViewById(R.id.edit_btn1);
        /*delete_btn = findViewById(R.id.delete_btn);*/
        realm = Realm.getDefaultInstance();
        Intent i = getIntent();
        position = i.getIntExtra("numposition", 0);
        modelClass = realm.where(ModelClass.class).equalTo("id", position).findFirst();
        textView_title.setText(modelClass.getTitle());
        textView_details.setText(modelClass.getDetails());
        textView_date.setText(modelClass.getDate());
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FullDataShowActivity.this, UpdateDataActivity.class);
                i.putExtra("numposition", position);
                startActivity(i);
            }
        });
       /* delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_data();
            }
        });*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_delete_menu_items, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.text_delete) {
          Confirm_Dialogue();
        }
        return true;
    }
    public void delete_data() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                modelClass.deleteFromRealm();
                Intent i=new Intent(FullDataShowActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
    private void Confirm_Dialogue() {
        final Dialog dialog = new Dialog(FullDataShowActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;*/
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.confirm_delete_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button btn_yes, btn_no;
        btn_yes = dialog.findViewById(R.id.dlt_yes_btn);
        btn_no = dialog.findViewById(R.id.dlt_no_btn);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_data();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}