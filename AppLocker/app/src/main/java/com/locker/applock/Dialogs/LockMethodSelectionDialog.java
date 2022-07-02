package com.locker.applock.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.RadioGroup;

import com.locker.applock.Interfaces.LockSelectionInterface;
import com.locker.applock.R;
import com.locker.applock.Utils.SharedPrefHelper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.PATTERN;

public class LockMethodSelectionDialog {
    public static void createListSelectionDialog(Context context, LockSelectionInterface lockSelectionInterface) {
        Dialog dialog = new Dialog(context);
        SharedPrefHelper shared_pref_helper = new SharedPrefHelper(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_lock_selection_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        RadioGroup radioGroup = dialog.findViewById(R.id.lock_select_radio_group);
        radioGroup.check(shared_pref_helper.Get_Int_AL(LOCK_TYPE,PATTERN));
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);

        dialog.setOnDismissListener(dialogInterface -> {
            lockSelectionInterface.Dismiss(dialog);
        });

        btnConfirm.setOnClickListener(view -> {
            int radioId = radioGroup.getCheckedRadioButtonId();
            lockSelectionInterface.SelectLock(dialog, radioId);
            dialog.dismiss();
        });
        dialog.show();

    }
}
