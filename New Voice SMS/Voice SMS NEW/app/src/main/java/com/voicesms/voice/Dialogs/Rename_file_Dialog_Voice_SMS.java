package com.voicesms.voice.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.voicesms.voice.Interfaces.RenameFileDialog_Interface;
import com.voicesms.voice.R;

public class Rename_file_Dialog_Voice_SMS {
    public static void CreateRenameDialogBox(Context context, RenameFileDialog_Interface renameFileDialog_interface) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_rename_file_dialog_box_voice_sms);
        Button btn_Rename = dialog.findViewById(R.id.btn_rename);
        Button btn_Cancel = dialog.findViewById(R.id.btn_cancel_rename);
        EditText fileName_new = dialog.findViewById(R.id.edit_text_file_name);

        btn_Rename.setOnClickListener(view -> {
            if (fileName_new.getText().length() > 0) {
                renameFileDialog_interface.RenameFile(fileName_new.getText().toString() + ".mp3");
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Name Cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        btn_Cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }
}
