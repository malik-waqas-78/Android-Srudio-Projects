package com.ash360.cool.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.widget.TextView;

import com.ash360.cool.Interfaces.GeneralDialogInterface;
import com.ash360.cool.R;

public class FullScreenDialog_DoorLock {
    public static void CreateGeneralDialog(Context context
            , String diag_title, String diag_desc, String Positive, String Negative
            , GeneralDialogInterface generalDialogInterface) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_fullscreen_dialog_door_lock);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT
                , WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView diag_Title = dialog.findViewById(R.id.gen_dialog_title);
        TextView diag_Desc = dialog.findViewById(R.id.gen_dialog_desc);
        TextView btn_Yes = dialog.findViewById(R.id.gen_btn_yes);
        TextView btn_No = dialog.findViewById(R.id.gen_btn_no);
        btn_Yes.setText(Positive);
        btn_No.setText(Negative);
        diag_Title.setText(diag_title);
        diag_Desc.setText(diag_desc);

        btn_Yes.setOnClickListener(v -> {
            generalDialogInterface.OnPositiveClick(dialog);
            dialog.dismiss();
        });
        btn_No.setOnClickListener(v -> {
            generalDialogInterface.OnNegativeClick(dialog);
            dialog.dismiss();
        });
        dialog.show();

    }
}
