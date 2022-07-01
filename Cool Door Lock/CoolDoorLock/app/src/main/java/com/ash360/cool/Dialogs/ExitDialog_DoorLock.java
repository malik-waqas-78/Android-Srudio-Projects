package com.ash360.cool.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ash360.cool.AdUtils.NativeAdsCoolDoorLock;
import com.ash360.cool.Interfaces.ExitInterface_DoorLock;
import com.ash360.cool.R;
import com.facebook.ads.NativeAdLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ExitDialog_DoorLock {
    public static void CreateExitDialog(Context context, ExitInterface_DoorLock interface_doorLock) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_exit_dialog_door_lock);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        ImageView btn_vote_up = dialog.findViewById(R.id.btn_vote_up);
        ImageView btn_vote_down = dialog.findViewById(R.id.btn_vote_down);
        TextView btn_exit = dialog.findViewById(R.id.btn_exit);

        btn_exit.setOnClickListener(view -> {
            interface_doorLock.ExitFromApp();
            dialog.dismiss();
        });
        btn_vote_down.setOnClickListener(view -> {
            interface_doorLock.DownVote();
            dialog.dismiss();
        });
        btn_vote_up.setOnClickListener(view -> {
            interface_doorLock.UpVote();
            dialog.dismiss();
        });
        NativeAdLayout nativeAdLayout = dialog.findViewById(R.id.native_ad_container);
        NativeAdsCoolDoorLock.showAd(context, nativeAdLayout);
        dialog.show();
    }
}
