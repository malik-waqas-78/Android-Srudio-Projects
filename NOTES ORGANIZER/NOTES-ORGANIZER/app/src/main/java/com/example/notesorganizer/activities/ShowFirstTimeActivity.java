package com.example.notesorganizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notesorganizer.R;
import com.example.notesorganizer.adapter.ShowDatabaseAdapter;

public class ShowFirstTimeActivity extends AppCompatActivity {
    ImageView imageButton_floating_btn;
Toolbar toolbar;
ShowDatabaseAdapter showDatabaseAdapter;
EditText editText_search;
ImageView btn_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_first_time);
        editText_search=findViewById(R.id.first_edit_search);
        btn_search=findViewById(R.id.first_search_images);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_search.setVisibility(View.VISIBLE);
            }
        });
        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Toast.makeText(ShowFirstTimeActivity.this, "save some data before searching...", Toast.LENGTH_SHORT).show();
            }
        });
        /*toolbar=findViewById(R.id.first_time_toolbar);*/
        /*setSupportActionBar(toolbar);*/
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERNCES", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
        String First_Time = sharedPreferences.getString("FIRST_TIME_INSTALL", "");
        if (First_Time.equals("YES")) {
            Intent i = new Intent(ShowFirstTimeActivity.this, MainActivity.class);
            startActivity(i);
        } else {
            editor.putString("FIRST_TIME_INSTALL", "YES");
            editor.apply();
        }
        imageButton_floating_btn = findViewById(R.id.floating_btn_first_time);
        imageButton_floating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowFirstTimeActivity.this, AddNewDataActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Confirm_Dialogue();
    }

    private void Confirm_Dialogue() {
        final Dialog dialog = new Dialog(ShowFirstTimeActivity.this);
        /*dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;*/
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.confirm_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Button btn_yes, btn_no, btn_rates;
        btn_yes = dialog.findViewById(R.id.yes_btn);
        btn_no = dialog.findViewById(R.id.No_btn);
        btn_rates = dialog.findViewById(R.id.Rate_btn);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_rates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_items, menu);
        MenuItem searchitem = menu.findItem(R.id.text_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchitem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                /*showDatabaseAdapter.getFilter().filter(s);*/
                Toast.makeText(ShowFirstTimeActivity.this, "Save Some Data before searching...", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }
}