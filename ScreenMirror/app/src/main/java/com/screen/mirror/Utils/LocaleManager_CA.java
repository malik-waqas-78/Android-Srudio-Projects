package com.screen.mirror.Utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Locale;

import static com.screen.mirror.Utils.Constants_CA.ENGLISH;
import static com.screen.mirror.Utils.Constants_CA.LANGUAGE_KEY;

public class LocaleManager_CA {
    private final SharedPrefHelperCA sharedPrefHelper;
    private final Context context;

    public LocaleManager_CA(Context context) {
        this.context = context;
        sharedPrefHelper = new SharedPrefHelperCA(context);
    }

    public Context SetLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(sharedPrefHelper.Get_String_SM(LANGUAGE_KEY, ENGLISH));
        } else {
            return updateResourcesLegacy(sharedPrefHelper.Get_String_SM(LANGUAGE_KEY, ENGLISH));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Context updateResources(String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    private Context updateResourcesLegacy(String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}
