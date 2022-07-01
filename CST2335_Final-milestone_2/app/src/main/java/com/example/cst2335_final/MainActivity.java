package com.example.cst2335_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.cst2335_final.adapters.SearchListAdapter;
import com.example.cst2335_final.beans.SearchItem;

import java.util.ArrayList;

/**
 * Main Activity for application
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BaseAdapter searchListAdapter;
    private ArrayList<SearchItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent nextPage =new Intent(this, GuardianSearchActivity.class);
        Button guardianSearchBtn = findViewById(R.id.guardianSearchBtn);
        guardianSearchBtn.setOnClickListener( (click) -> startActivity(nextPage));


    }
}