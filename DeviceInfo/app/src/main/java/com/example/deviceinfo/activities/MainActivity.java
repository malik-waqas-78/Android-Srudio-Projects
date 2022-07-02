package com.example.deviceinfo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.deviceinfo.R;
import com.example.deviceinfo.adapter.ViewPagerAdapter;
import com.example.deviceinfo.databinding.ActivityMainBinding;
import com.example.deviceinfo.models.FitChartValue;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationIcon(R.drawable.ic_drawer_button);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewpager.setAdapter(adapter);

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.navigation.getMenu().getItem(position).setChecked(true);
                if(position==0){
                   binding.toolbarTitle.setText(getResources().getString(R.string.app_name));

                } else if(position==1){
                    binding.toolbarTitle.setText(getResources().getString(R.string.tools));
                }else {
                    binding.toolbarTitle.setText(getResources().getString(R.string.settings));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        binding.navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        binding.viewpager.setCurrentItem(0);
                        break;
                    case R.id.tools:
                        binding.viewpager.setCurrentItem(1);
                        break;
                    case R.id.settings:
                        binding.viewpager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}