package com.ash360.cool.Utils;

import com.ash360.cool.R;

import java.util.HashMap;

public class Constants_DoorLock {
    // Shared Pref KEYs
    public static final String MY_PREF_NAME = "COOL_DOOR_LOCK_SH_PREF";
    public static final String CHANNEL_ID = "Cool Door Lock";
    public static final String ENABLE_LOCK = "Enable Lock";
    public static final boolean ENABLE_LOCK_DEFAULT_VAL = false;
    public static final String ENABLE_SOUND = "Enable Sound";
    public static final boolean ENABLE_SOUND_DEFAULT_VAL = true; // true:Sound On
    public static final String TIME_STYLE = "Time Style";
    public static final boolean TIME_STYLE_DEFAULT_VAL = true; // true->10:40, false->22:40
    public static final String RESET_PASSWORD = "Reset Password";
    public static final String SELECT_THEME = "Select Theme";
    public static final String HOW_TO_USE = "How To Use?";
    public static final String RATE_US = "Rate Us";
    public static final String TELL_A_FRIEND = "Tell a friend";

    // Settings Screen Vals
    public static final int[] SETTINGS_SW_VISIBILITY = new int[]{0, 8, 8, 0, 0, 8, 8, 8};
    public static final int[] SETTINGS_ICON_DRAWABLES = new int[]{
            R.drawable.settings_enable_lock
            , R.drawable.settings_reset_password
            , R.drawable.settings_select_theme
            , R.drawable.settings_enable_sound
            , R.drawable.settings_time_style
            , R.drawable.settings_user_guide
            , R.drawable.settings_rate_us
            , R.drawable.settings_tell_a_friend
    };
    // Door Selection
    public static final int DOOR_1 = R.drawable.door_1_selection;
    public static final int DOOR_2 = R.drawable.door_2_selection;
    public static final int DOOR_3 = R.drawable.door_3_selection;
    public static final int DOOR_4 = R.drawable.door_4_selection;
    public static final int DOOR_5 = R.drawable.door_5_selection;
    public static final int DOOR_6 = R.drawable.door_6_selection;

    public static final String SELECTED_DOOR = "Selected_Door";
    public static final int SELECTED_DOOR_DEFAULT_VALUE = DOOR_1;
    public static final String IS_LOCK_SET = "Is_LocK_Set";
    public static final boolean IS_LOCK_SET_DEFAULT_VALUE = false;
    public static final String IS_FIRST_RUN = "Is_First_Run";
    public static final boolean IS_FIRST_RUN_DEFAULT_VALUE = true;

    public static final int PERMISSION_READ_PHONE = 5565;

    public static final String SHOULD_SHOW_SECURE_LOCK_DIALOG = "Should_show_secure_lock_dialog";
    public static final String DOT_LOCK_PATTERN = "Dot_Lock_Pattern";
    public static final String DOT_LOCK_MODE = "Dot_Lock_Mode";
    public static final int LOCK_SETUP = 1414;
    public static final int LOCK_MATCH = 1313;
    public static final int RESET_LOCK = 1212;

    public static HashMap<String, String> app_values = new HashMap<>();
}
