package com.su.finalpaper;

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

    Spinner sp;
    String item="Standard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Expo 2020");

        ImageView logo=findViewById(R.id.imageView);
        logo.animate().rotation(360).setDuration(1000);

        MediaPlayer player = new MediaPlayer();
        AssetFileDescriptor afd = null;
        try {
            afd = getAssets().openFd("uae_anthem.mp3");
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
            player.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


        sp=findViewById(R.id.spinner);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(
                this,
                R.array.ticket_type,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
    }

    public void navigate(View view) {
        Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
        EditText et=findViewById(R.id.rtAge);
        intent.putExtra("age",Integer.valueOf(et.getText().toString()));
        intent.putExtra("tt",item);
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] arr = getResources().getStringArray(R.array.ticket_type);
        item=arr[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}