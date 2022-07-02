package com.locker.applock.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.locker.applock.Dialogs.NewAppInstallDialog;
import com.locker.applock.Interfaces.GeneralDialogInterface;
import com.locker.applock.R;
import com.locker.applock.Utils.SharedPrefHelper;
import com.locker.applock.databinding.ActivityTransparentDialogBinding;

import static com.locker.applock.Utils.Constants.INSTALLED_PACKAGE_NAME;
import static com.locker.applock.Utils.Constants.LOCKED;
import static com.locker.applock.Utils.Constants.UNLOCKED;

public class TransparentDialogActivity extends AppCompatActivity {
    ActivityTransparentDialogBinding binding;
    Context context;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransparentDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = TransparentDialogActivity.this;
        sharedPrefHelper = new SharedPrefHelper(context);

        if (getIntent().hasExtra(INSTALLED_PACKAGE_NAME)) {
            String packageName = getIntent().getStringExtra(INSTALLED_PACKAGE_NAME);
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(packageName, 0);
            } catch (final PackageManager.NameNotFoundException e) {
                ai = null;
            }
            final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
            final Drawable appIcon = pm.getApplicationIcon(ai);
            NewAppInstallDialog.CreateGeneralDialog(this
                    , applicationName + " " + getResources().getString(R.string.new_added_app_title)
                    , getResources().getString(R.string.new_added_app_desc)
                    , appIcon
                    , getResources().getString(R.string.yes)
                    , getResources().getString(R.string.no)
                    , new GeneralDialogInterface() {
                        @Override
                        public void Positive(Dialog dialog) {
                            sharedPrefHelper.Set_Int_AL(packageName, LOCKED);
                            Toast.makeText(context, applicationName + " Locked", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(0, 0);
                        }

                        @Override
                        public void Negative(Dialog dialog) {
                            sharedPrefHelper.Set_Int_AL(packageName, UNLOCKED);
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    }
            );
        }
    }

}