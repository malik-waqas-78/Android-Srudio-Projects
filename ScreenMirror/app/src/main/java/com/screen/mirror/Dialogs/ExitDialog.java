package com.screen.mirror.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.FrameStats;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.screen.mirror.Activities.MainActivity_OKRA;
import com.screen.mirror.Interfaces.ExitDialogInterface;
import com.screen.mirror.R;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.NativeAdHelper_OKRA;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ExitDialog {
    public static void CreateExitDialog(Context context, ExitDialogInterface exitInterface) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_exit_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        Button btn_Allow = dialog.findViewById(R.id.req_perm_diag_btn_allow);
        Button btn_Deny = dialog.findViewById(R.id.req_perm_exit_diag_btn_deny);
        ConstraintLayout btn_rate_us = dialog.findViewById(R.id.rateus);
        FrameLayout frameLayout=dialog.findViewById(R.id.ad_frame);

        btn_Allow.setOnClickListener(view -> {
            exitInterface.ExitFromApp();
            dialog.dismiss();
        });
        btn_Deny.setOnClickListener(view -> dialog.dismiss());
        btn_rate_us.setOnClickListener(view -> {
            exitInterface.RateUs();
            dialog.dismiss();
        });
//        NativeAdLayout nativeAdLayout = dialog.findViewById(R.id.native_ad_container);
//        NativeAds_CA.showAd(context, nativeAdLayout);
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(context)) {
            NativeAdHelper_OKRA.showAdmobNativeAd(context,frameLayout);
        }

        dialog.show();
    }

}
