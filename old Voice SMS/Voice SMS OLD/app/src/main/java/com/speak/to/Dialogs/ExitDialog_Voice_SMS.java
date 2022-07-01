package com.speak.to.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.speak.to.Interfaces.ExitDialogInterface_Voice_SMS;
import com.speak.to.R;
import com.speak.to.Utils.NativeAdsVoiceSMS;
import com.facebook.ads.NativeAdLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ExitDialog_Voice_SMS {
    public static void CreateExitDialog(Context context, ExitDialogInterface_Voice_SMS exitDialogInterface_voice_sms) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_exit_dialog_box_voice_sms);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        Button btn_Allow = dialog.findViewById(R.id.req_perm_diag_btn_allow);
        Button btn_Deny = dialog.findViewById(R.id.req_perm_exit_diag_btn_deny);
        ConstraintLayout btn_rate_us = dialog.findViewById(R.id.rateus);

        btn_Allow.setOnClickListener(view -> {
            exitDialogInterface_voice_sms.ExitFromApp();
            dialog.dismiss();
        });
        btn_Deny.setOnClickListener(view -> dialog.dismiss());
        btn_rate_us.setOnClickListener(view -> {
            exitDialogInterface_voice_sms.RateUs();
            dialog.dismiss();
        });
        NativeAdLayout nativeAdLayout = dialog.findViewById(R.id.native_ad_container);
        NativeAdsVoiceSMS.showAd(context, nativeAdLayout);
        dialog.show();
    }
}
