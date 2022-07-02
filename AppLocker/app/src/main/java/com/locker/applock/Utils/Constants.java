package com.locker.applock.Utils;

import com.locker.applock.R;

public class Constants {
    public static final String SHARED_PREF_APP_LOCK = "SHARED_PREF_APP_LOCK";
    public static final String LOCKED_APP = "LOCKED_APP";
    public static final String LAUNCHER_PACKAGE = "LAUNCHER_PACKAGE";
    public static final String CURRENT_PACKAGE = "CURRENT_PACKAGE";

    public static final int LOCKED = 4;
    public static final int UNLOCKED = 0;
    public static final String CHANNEL_ID = "App Lock Channel";

    // Theme drawable Ids
    public static final int THEME_1 = R.drawable.theme_1;
    public static final int THEME_2 = R.drawable.theme_2;
    public static final int THEME_3 = R.drawable.theme_3;
    public static final int THEME_4 = R.drawable.theme_4;
    public static final int THEME_5 = R.drawable.theme_5;
    public static final int THEME_6 = R.drawable.theme_6;
    public static final int THEME_7 = R.drawable.theme_7;
    public static final int THEME_8 = R.drawable.theme_8;
    public static final int THEME_9 = R.drawable.theme_9;
    public static final String SELECTED_THEME = "SELECTED_THEME";
    public static final int SELECTED_THEME_DEFAULT_VALUE = R.drawable.theme_1;

    // Pattern/Pin Lock Constants
    public static final String IS_LOCK_SET = "IS_LOCK_SET";
    public static final String IS_LOCK_ENABLED = "IS_LOCK_ENABLED";
    public static final String IS_LOCK_CHANGE_MODE = "IS_LOCK_CHANGE_MODE";
    public static final String IS_SECRET_QUESTION_SET = "IS_SECRET_QUESTION_SET";
    public static final String LOCK_TYPE = "LOCK_TYPE";
    public static final String LOCK_MODE = "LOCK_MODE";   // LOCK_MODE = 1 is for SetUp and 2 is for Match Pattern/Pin
    public static final int SETUP_LOCK = 111;
    public static final int MATCH_LOCK = 222;
    public static final String IS_FINGERPRINT_SET = "IS_FINGERPRINT_SET";
    public static final String IS_LOCK_NEW_APPS_ENABLED = "IS_LOCK_NEW_APPS_ENABLED";

    public static final String SAVED_PATTERN = "SAVED_PATTERN";
    public static final String SAVED_PIN = "SAVED_PIN";
    public static final int PATTERN = R.id.radio_pattern_lock;
    public static final int PIN = R.id.radio_pin_lock;

    public static final String IS_PATTERN_SET = "IS_PATTERN_SET";
    public static final String IS_PIN_SET_UP = "IS_PIN_SET_UP";

    public static final String CHANGE_PATTERN_MODE = "CHANGE_PATTERN_MODE";
    public static final String CHANGE_PIN_MODE = "CHANGE_PIN_MODE";

    // General Values
    public static final String IS_FIRST_RUN = "IS_FIRST_RUN";

    // Settings Screen
    public static final String ENABLE_APP_LOCK = "Enable App Lock";
    public static final String CHANGE_PASSWORD = "Change Password";
    public static final String FINGERPRINT = "Fingerprint";
    public static final String LOCK_NEW_APP = "Lock the new app";
    public static final String CHANGE_LOCK_TYPE = "Change Lock Type";
    public static final String CHANGE_SECURITY_QUESTION = "Change Security Question";
    public static final String BATTERY_OPTIMIZATION_MODE = "Battery Optimization Mode";

    public static final int SECRET_CAR = R.drawable.ic_secret_car;
    public static final int SECRET_BOOK = R.drawable.ic_secret_book;
    public static final int SECRET_MOVIE = R.drawable.ic_secret_movie;
    public static final int SECRET_SONG = R.drawable.ic_secret_music;
    public static final int SECRET_CITY = R.drawable.ic_secret_city;
    public static final int SECRET_PET = R.drawable.ic_secret_pet;

    public static final String SECRET_QUESTION_ANSWER = "SECRET_QUESTION_ANSWER";
    public static final String SECRET_IMAGE_ID = "SECRET_IMAGE_ID";
    public static final String CURRENT_PACKAGE_DEFAULT = "CURRENT_PACKAGE_DEFAULT";

    public static final String PERMISSION_CODE = "PERMISSION_CODE";
    public static final int USAGE_ACCESS_PERMISSION = 990;
    public static final int DRAW_OVER_OTHER_APPS_PERMISSION = 991;
    public static final int ACCESSIBILITY_SERVICE_PERMISSION = 992;

    public static final String ACCESSIBILITY_MODE = "ACCESSIBILITY_MODE";
    public static final String INSTALLED_PACKAGE_NAME = "INSTALLED_PACKAGE_NAME";
    public static final String SETUP_DURING_LOCK = "SETUP_DURING_LOCK";
    public static final String STOP_SERVICE_AND_HANDLER = "STOP_SERVICE_AND_HANDLER";
}
