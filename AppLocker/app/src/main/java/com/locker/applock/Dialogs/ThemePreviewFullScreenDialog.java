package com.locker.applock.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.locker.applock.Interfaces.ThemePreviewInterface;
import com.locker.applock.R;

public class ThemePreviewFullScreenDialog {
    public static void ShowThemePreview(Context context, int themeResId, int lockResId, ThemePreviewInterface previewCallback) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_fullscreen_dialog_theme_preview);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT
                , WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView themeView = dialog.findViewById(R.id.theme_view);
        ImageView overlayLock = dialog.findViewById(R.id.overlay_lock);
        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        Button btnApply = dialog.findViewById(R.id.btn_apply);

        Glide.with(context).load(themeResId)
                .centerCrop()
                .into(themeView);
        Glide.with(context).load(lockResId).into(overlayLock);

        btnApply.setOnClickListener(view -> {
            previewCallback.onThemeApply();
            dialog.dismiss();
        });
        btnClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}
