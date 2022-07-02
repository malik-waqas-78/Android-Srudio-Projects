package com.video.trimmer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.video.trimmer.databinding.ActivityVideoInfoBinding;

public class videoInfo extends AppCompatActivity {
    ActivityVideoInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVideoInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.info.setText(getIntent().getStringExtra("videoinforamtion"));
    }
}