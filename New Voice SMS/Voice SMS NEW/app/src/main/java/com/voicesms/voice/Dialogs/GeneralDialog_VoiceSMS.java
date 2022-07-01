package com.voicesms.voice.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;

import com.voicesms.voice.Interfaces.GeneralDialogInterface_voiceSMS;
import com.voicesms.voice.R;

public class GeneralDialog_VoiceSMS {
    public static void CreateGeneralDialog(Context context
            , String diag_title, String diag_desc, String Positive, String Negative
            , GeneralDialogInterface_voiceSMS generalDialogInterface_voiceSMS) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_general_dialog_voice_sms);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_Allow = dialog.findViewById(R.id.btn_allow);
        Button btn_Deny = dialog.findViewById(R.id.btn_deny);
        TextView diag_Desc = dialog.findViewById(R.id.general_dialog_text_voice_sms);
        TextView diag_Title = dialog.findViewById(R.id.general_dialog_title_voice_sms);
        btn_Allow.setText(Positive);
        btn_Deny.setText(Negative);
        diag_Title.setText(diag_title);
        diag_Desc.setText(diag_desc);
        btn_Allow.setOnClickListener(view -> {
            generalDialogInterface_voiceSMS.Positive(dialog);
            dialog.dismiss();
        });
        btn_Deny.setOnClickListener(view -> {
            generalDialogInterface_voiceSMS.Negative(dialog);
            dialog.dismiss();
        });
        dialog.show();

    }
}
