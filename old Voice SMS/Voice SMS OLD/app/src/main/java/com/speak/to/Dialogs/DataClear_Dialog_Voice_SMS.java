package com.speak.to.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.speak.to.Interfaces.ClearTextDialogInterface_Voice_SMS;
import com.speak.to.R;

public class DataClear_Dialog_Voice_SMS {
    public static void CreateClearDataDialog(Context context, ClearTextDialogInterface_Voice_SMS clearTextDialogInterface_voice_sms) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_clear_data_dialog_box_voice_sms);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.findViewById(R.id.btn_both).setOnClickListener(view -> {
            clearTextDialogInterface_voice_sms.ClearBoth();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.btn_images).setOnClickListener(view -> {
            clearTextDialogInterface_voice_sms.ClearImages();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.btn_text).setOnClickListener(view -> {
            clearTextDialogInterface_voice_sms.ClearText();
            dialog.dismiss();
        });
        dialog.show();
    }
}
