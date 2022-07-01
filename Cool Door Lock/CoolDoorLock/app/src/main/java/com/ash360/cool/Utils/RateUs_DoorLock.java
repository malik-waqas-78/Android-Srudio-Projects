package com.ash360.cool.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.util.Objects;

public class RateUs_DoorLock {
    Context context;

    public RateUs_DoorLock(Context context) {
        this.context = context;
        rateApp();
    }

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details?id=");
            context.startActivity(rateIntent);
        } catch (Exception e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=");
            context.startActivity(rateIntent);
        }
    }

    public Intent rateIntentForUrl(String url) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + Objects.requireNonNull(context).getPackageName()));
        }
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags = flags | Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            flags = flags | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
}
