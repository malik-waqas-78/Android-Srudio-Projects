package com.locker.applock.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.locker.applock.Interfaces.GeneralDialogInterface;
import com.locker.applock.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class NewAppInstallDialog {
    public static void CreateGeneralDialog(Activity activity
            , String diag_title, String diag_desc, Drawable appIcon, String Positive, String Negative
            , GeneralDialogInterface generalDialogInterface) {
        Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_new_app_install_dialog_box);
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_Allow = dialog.findViewById(R.id.btn_allow);
        Button btn_Deny = dialog.findViewById(R.id.btn_deny);
        TextView diag_Desc = dialog.findViewById(R.id.dialog_text);
        TextView diag_Title = dialog.findViewById(R.id.dialog_title);
        ImageView diag_Icon = dialog.findViewById(R.id.dialog_icon);
        btn_Allow.setText(Positive);
        btn_Deny.setText(Negative);
        diag_Title.setText(diag_title);
        diag_Desc.setText(diag_desc);
        Glide.with(activity).load(appIcon).into(diag_Icon);

        btn_Allow.setOnClickListener(view -> {
            generalDialogInterface.Positive(dialog);
            dialog.dismiss();
        });
        btn_Deny.setOnClickListener(view -> {
            generalDialogInterface.Negative(dialog);
            dialog.dismiss();
        });
        if (!activity.isFinishing()) {
            dialog.show();
        }

    }
}
