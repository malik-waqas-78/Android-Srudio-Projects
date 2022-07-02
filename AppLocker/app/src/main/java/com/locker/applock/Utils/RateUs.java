package com.locker.applock.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Objects;

public class RateUs {
    Context context;

    public RateUs(Context context) {
        this.context = context;
        rateApp();
    }

    private void rateApp() {
        try {
            String url = "market://details?id=";
            context.startActivity(new Intent(Intent.ACTION_VIEW
                    , Uri.parse(url + Objects.requireNonNull(context).getPackageName())));
        } catch (Exception e) {
            String url = "https://play.google.com/store/apps/details?id=";
            context.startActivity(new Intent(Intent.ACTION_VIEW
                    , Uri.parse(url + Objects.requireNonNull(context).getPackageName())));
        }
    }
}
