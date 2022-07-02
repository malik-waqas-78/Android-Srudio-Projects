package com.locker.applock.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.locker.applock.R;
import com.locker.applock.databinding.ActivityOverlayBinding;

import static com.locker.applock.Utils.Constants.ACCESSIBILITY_SERVICE_PERMISSION;
import static com.locker.applock.Utils.Constants.DRAW_OVER_OTHER_APPS_PERMISSION;
import static com.locker.applock.Utils.Constants.PERMISSION_CODE;
import static com.locker.applock.Utils.Constants.USAGE_ACCESS_PERMISSION;

public class OverlayActivity extends AppCompatActivity {
    ActivityOverlayBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOverlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int which_permission = getIntent().getIntExtra(PERMISSION_CODE, DRAW_OVER_OTHER_APPS_PERMISSION);
        switch (which_permission) {
            case USAGE_ACCESS_PERMISSION: {
                SetTexts(getResources().getString(R.string.usage_access_permission_title)
                        , getResources().getString(R.string.usage_access_permission_desc));
                break;
            }
            case ACCESSIBILITY_SERVICE_PERMISSION: {
                SetTexts(getResources().getString(R.string.accessibility_service_permission_title)
                        , getResources().getString(R.string.accessibility_service_permission_desc));
                break;
            }
            case DRAW_OVER_OTHER_APPS_PERMISSION: {
                SetTexts(getResources().getString(R.string.draw_over_apps_permission_title)
                        , getResources().getString(R.string.draw_over_apps_permission_desc));
                break;
            }
        }
        binding.btnGotIt.setOnClickListener(view -> {
            finish();
        });
    }
    public void SetTexts(String title, String desc){
        binding.overlayTitle.setText(title);
        binding.overlayDesc.setText(desc);
    }

}
