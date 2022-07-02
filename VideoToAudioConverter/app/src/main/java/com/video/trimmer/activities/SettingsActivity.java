package com.video.trimmer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.video.trimmer.R;
import com.video.trimmer.adapters.SettingRecyclerAdapter;
import com.video.trimmer.databinding.ActivitySettingsBinding;
import com.video.trimmer.interfaces.SettingItemClickInterface;
import com.video.trimmer.modelclasses.SettingsModaltems;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.LocaleManager_CA;
import com.video.trimmer.utils.SharedPrefClass;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity  implements SettingItemClickInterface {
    ActivitySettingsBinding binding;
    SharedPrefClass sharedPrefClass;
    int position;
    SettingRecyclerAdapter settingRecyclerAdapter;
    ArrayList<SettingsModaltems> settingsModaltems=new ArrayList<>();
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constants=new Constants(this);
        initComponents();
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_icon));
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponents() {
        sharedPrefClass=new SharedPrefClass(SettingsActivity.this);
        addElementsinSettings();
    }

    private void addElementsinSettings() {
        settingsModaltems.clear();
        settingsModaltems.add(new SettingsModaltems(R.drawable.ic_translate,getResources().getString(R.string.selectLanguage),false,false));
        settingsModaltems.add(new SettingsModaltems(R.drawable.ic_dark_mode,getResources().getString(R.string.darkTheme),true,sharedPrefClass.getThemeStatefromShared()));
        settingsModaltems.add(new SettingsModaltems(R.drawable.ic_share_icon,getResources().getString(R.string.shareApp),false,false));

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        settingRecyclerAdapter=new SettingRecyclerAdapter(this,settingsModaltems);
        binding.settingsRecycler.setLayoutManager(layoutManager);
        binding.settingsRecycler.setAdapter(settingRecyclerAdapter);

    }

    @Override
    protected void attachBaseContext(Context baseContext) {
        super.attachBaseContext(new LocaleManager_CA(baseContext).SetLocale());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        new LocaleManager_CA(this).SetLocale();
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void OnItemClick(int pos) {
        position=pos;
        if(pos==0){
            Intent intent=new Intent(SettingsActivity.this,ActivitySelectLanguage.class);
            startActivity(intent);
            finish();
        }else if(pos==2){
            final String appPackageName = getPackageName();
            Intent i=new Intent(Intent.ACTION_SEND);
            i.setData(Uri.parse("email"));
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL,"http://play.google.com/store/apps/details?id=" + appPackageName);
            i.putExtra(Intent.EXTRA_TEXT,getString(R.string.shareApp));
            Intent chooser=Intent.createChooser(i,getString(R.string.shareApp));
            startActivity(chooser);
        }
    }

    @Override
    public void OnThemeSwitchStateChange(boolean state) {
        sharedPrefClass.saveThemeStateinShared(state);
        if(state){
            new LocaleManager_CA(SettingsActivity.this).SetLocale();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            new LocaleManager_CA(SettingsActivity.this).SetLocale();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        initComponents();
    }

}