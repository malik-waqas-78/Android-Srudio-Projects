package com.ppt.walkie.utils;

import org.jetbrains.annotations.NotNull;

/** A set of constants used within the app. */
public class ConstantsOKRA {
  /** A tag for logging. Use 'adb logcat -s WalkieTalkie' to follow the logs. */
  public static final String TAG = "WalkieTalkie";
  public static final int RAP=727;
  public static final int LOCATION_PERMISSION_REQUEST_CODE=5772;
  public static final int REQUEST_LOCATION_TURNON=7586;
  public static final int REQUEST_WIFI_TURN_ON=7986;
  public static final int REQUEST_BLUETOOTH_TURN_ON=7286;

  public static final String RECEIVING_CALLS_CHANNEL_ID="Walkie Talkie";
  public static final String INCOMING_CALLS_CHANNEL_ID="INCOMING Calls";
  public static final int SERVICE_NOTIFICATION_ID=927;
  public static final String INCOMING_CALL="INCOMING Call";

  public static final String CALLER_NAME="CALLER_NAME";
  @NotNull
  public static final String CALl_ACCEPTED="CALL_ACCEPTED";

  public static final long CALL_TIME_OUT=60000;
  public static final String USER_NAME="USERNAMEPASSESD";
}
