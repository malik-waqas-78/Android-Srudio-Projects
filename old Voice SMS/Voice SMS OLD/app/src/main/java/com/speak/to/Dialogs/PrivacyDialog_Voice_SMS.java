package com.speak.to.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.widget.TextView;

import com.speak.to.R;

public class PrivacyDialog_Voice_SMS {
    public static void CreatePrivacyDialog(Context context) {
        final Dialog dialog = new Dialog(context
                , android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(R.layout.privacy_policy_voice_sms_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        final TextView ok = dialog.findViewById(R.id.btn_okay);
        ok.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
