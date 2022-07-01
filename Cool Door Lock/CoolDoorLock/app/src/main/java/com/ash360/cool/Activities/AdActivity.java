package com.ash360.cool.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ash360.cool.databinding.ActivityAdBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;

import static com.ash360.cool.Utils.Constants_DoorLock.app_values;


public class AdActivity extends AppCompatActivity {
    ActivityAdBinding binding;
    Context context;
    HashMap<String, String> Ad_App = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = AdActivity.this;

        Ad_App = app_values;

        binding.centerTextAdActivity.setText(Ad_App.get("AppDescription"));
        binding.appNameAdActivity.setText(Ad_App.get("AppName"));
        Glide.with(context).load(Ad_App.get("AppIcon")).into(binding.appIconAdActivity);
        Glide.with(context).load(Ad_App.get("AppImageLink"))
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        binding.backAdActivity.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        binding.btnCloseAdActivity.setOnClickListener(view -> finish());
        binding.btnClose.setOnClickListener(view -> finish());
        binding.btnInstallAdActivity.setOnClickListener(view -> {
            InstallApp(Ad_App.get("AppLink"));
            finish();
        });
    }

    private void InstallApp(String packageName) {
        try {
            String url = "market://details?id=";
            context.startActivity(new Intent(Intent.ACTION_VIEW
                    , Uri.parse(url + packageName)));
        } catch (Exception e) {
            String url = "https://play.google.com/store/apps/details?id=";
            context.startActivity(new Intent(Intent.ACTION_VIEW
                    , Uri.parse(url + packageName)));
        }
    }
}