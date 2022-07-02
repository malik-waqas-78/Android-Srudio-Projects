package com.locker.applock.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.locker.applock.Interfaces.ExitDialogInterface;
import com.locker.applock.R;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.NativeAdHelper_OKRA;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ExitDialog_AppLock {
    public static void CreateExitDialog(Context context, ExitDialogInterface exitInterface) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_exit_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        Button btn_Allow = dialog.findViewById(R.id.req_perm_diag_btn_allow);
        Button btn_Deny = dialog.findViewById(R.id.req_perm_exit_diag_btn_deny);
        ConstraintLayout btn_rate_us = dialog.findViewById(R.id.rateus);
        FrameLayout ad_frame=dialog.findViewById(R.id.ad_frame);

        btn_Allow.setOnClickListener(view -> {
            exitInterface.ExitFromApp();
            dialog.dismiss();
        });
        btn_Deny.setOnClickListener(view -> dialog.dismiss());
        btn_rate_us.setOnClickListener(view -> {
            exitInterface.RateUs();
            dialog.dismiss();
        });

        if(InterAdHelper_OKRA.isAppInstalledFromPlay(context)) {
            NativeAdHelper_OKRA.showAdmobNativeAd(context,ad_frame);
        }

//        NativeAdLayout nativeAdLayout = dialog.findViewById(R.id.native_ad_container);
//        NativeAdsTouchDisable.showAd(context, nativeAdLayout);
        dialog.show();
    }
}
