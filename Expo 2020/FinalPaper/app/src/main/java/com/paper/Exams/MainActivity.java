package com.paper.Exams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    String selectedItem ="Standard";
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        MediaPlayer player = new MediaPlayer();
        AssetFileDescriptor src = null;
        try {
            src = getAssets().openFd("uae_anthem.mp3");
            player.setDataSource(src.getFileDescriptor(),src.getStartOffset(),src.getLength());
            player.prepare();
            player.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageView background=findViewById(R.id.imageView);
        background.animate().rotation(360).setDuration(1000);

        spinner =findViewById(R.id.sp);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(
                this,
                R.array.type,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void btnClicked(View view) {
        Intent intent=new Intent(MainActivity.this, Details.class);
        EditText et=findViewById(R.id.et_age);
        intent.putExtra("age",Integer.valueOf(et.getText().toString()));
        intent.putExtra("type", selectedItem);
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] arr = getResources().getStringArray(R.array.type);
        selectedItem =arr[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}