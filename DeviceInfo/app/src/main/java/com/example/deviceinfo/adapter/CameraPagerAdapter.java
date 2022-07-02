package com.example.deviceinfo.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.deviceinfo.fragments.Back_cam_info;
import com.example.deviceinfo.fragments.Front_cam_info;

import org.jetbrains.annotations.NotNull;

public class CameraPagerAdapter extends FragmentStateAdapter {
    public CameraPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
       if(position==0){
           return new Back_cam_info();
        }else {
           return new Front_cam_info();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
