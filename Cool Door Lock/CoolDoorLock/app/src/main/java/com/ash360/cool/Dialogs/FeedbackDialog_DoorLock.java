package com.ash360.cool.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ash360.cool.Interfaces.GeneralDialogInterface;
import com.ash360.cool.R;

public class FeedbackDialog_DoorLock {
    public static void CreateFeedbackDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_feedback_dialog_door_lock);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView diag_Title = dialog.findViewById(R.id.gen_dialog_title);
        EditText feedbackText = dialog.findViewById(R.id.gen_dialog_desc);
        TextView btn_Yes = dialog.findViewById(R.id.gen_btn_yes);
        TextView btn_No = dialog.findViewById(R.id.gen_btn_no);
        btn_Yes.setText(context.getApplicationContext().getResources().getString(R.string.submit));
        btn_No.setText(context.getApplicationContext().getResources().getString(R.string.cancel));
        diag_Title.setText(context.getApplicationContext().getResources().getString(R.string.send_feedback));

        btn_Yes.setOnClickListener(v -> {
            if (feedbackText != null && feedbackText.length() > 0) {
                Toast.makeText(context, "Feedback Sent to our developer team, Many Thanks.", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        btn_No.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

    }
}
