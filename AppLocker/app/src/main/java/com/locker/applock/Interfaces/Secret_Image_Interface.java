package com.locker.applock.Interfaces;

import com.locker.applock.Models.Secret_Image_Item;

import java.util.ArrayList;

public interface Secret_Image_Interface {
    void OnClick(ArrayList<Secret_Image_Item> mDataSet, int adapterPosition);

    void initiateResetLockActivity();
}
