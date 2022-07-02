package com.screen.mirror.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;

import com.screen.mirror.Interfaces.GeneralDialogInterface;
import com.screen.mirror.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class GeneralDialog {
    public static void CreateGeneralDialog(Context context
            , String diag_title, String diag_desc, String Positive, String Negative
            , GeneralDialogInterface generalDialogInterface) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_general_dialog_box);
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_Allow = dialog.findViewById(R.id.btn_allow);
        Button btn_Deny = dialog.findViewById(R.id.btn_deny);
        TextView diag_Desc = dialog.findViewById(R.id.dialog_text);
        TextView diag_Title = dialog.findViewById(R.id.dialog_title);
        btn_Allow.setText(Positive);
        btn_Deny.setText(Negative);
        diag_Title.setText(diag_title);
        diag_Desc.setText(diag_desc);

        btn_Allow.setOnClickListener(view -> {
            generalDialogInterface.Positive(dialog);
            dialog.dismiss();
        });
        btn_Deny.setOnClickListener(view -> {
            generalDialogInterface.Negative(dialog);
            dialog.dismiss();
        });
        dialog.show();

    }
}
