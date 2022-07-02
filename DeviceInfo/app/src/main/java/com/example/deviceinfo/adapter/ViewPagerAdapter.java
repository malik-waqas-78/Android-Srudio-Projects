package com.example.deviceinfo.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.deviceinfo.fragments.Back_cam_info;
import com.example.deviceinfo.fragments.Front_cam_info;
import com.example.deviceinfo.fragments.HomeFragment;
import com.example.deviceinfo.fragments.SettingsFragment;
import com.example.deviceinfo.fragments.ToolsFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return new HomeFragment();
        }else if(position==1){
            return new ToolsFragment();
        }else{
            return new SettingsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
