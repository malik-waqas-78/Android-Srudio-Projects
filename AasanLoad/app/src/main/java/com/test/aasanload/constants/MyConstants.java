package com.test.aasanload.constants;

import android.graphics.Bitmap;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/easy/recharge/constants/MyConstants;", "", "()V", "Companion", "app_release"}, k = 1, mv = {1, 4, 2})
/* compiled from: MyConstants.kt */
public final class MyConstants {
    private static Bitmap BITMAP = null;
    private static final int CALL_PERMISIION_REQUEST_CODE = 2263;
    private static final int CAMERA_PERMISIION_REQUEST_CODE = 2263;
    public static final String CODE_ARRAY = "CODEARRAY";
    private static final int CODE_OCR_CAPTURE_ACTIVITY = 2278;
    public static final Companion Companion = new Companion();
    private static final int DIALER_ACTIVITY_REQUEST_CODE = 2255;
    private static final int MY_REQUEST_CODE = 8732;
    public static final String NETWORK_KEY = "NETWORKKEY";
    private static final int PERMISSION_SETTINGS = 2273;
    public static final String TAG = "92727";

    /* compiled from: MyConstants.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int getCAMERA_PERMISIION_REQUEST_CODE() {
            return MyConstants.CAMERA_PERMISIION_REQUEST_CODE;
        }

        public final int getDIALER_ACTIVITY_REQUEST_CODE() {
            return MyConstants.DIALER_ACTIVITY_REQUEST_CODE;
        }

        public final int getPERMISSION_SETTINGS() {
            return MyConstants.PERMISSION_SETTINGS;
        }

        public final String getNETWORK_KEY() {
            return MyConstants.NETWORK_KEY;
        }

        public final int getCALL_PERMISIION_REQUEST_CODE() {
            return MyConstants.CALL_PERMISIION_REQUEST_CODE;
        }

        public final Bitmap getBITMAP() {
            return MyConstants.BITMAP;
        }

        public final void setBITMAP(Bitmap bitmap) {
            MyConstants.BITMAP = bitmap;
        }

        public final String getCODE_ARRAY() {
            return MyConstants.CODE_ARRAY;
        }

        public final int getCODE_OCR_CAPTURE_ACTIVITY() {
            return MyConstants.CODE_OCR_CAPTURE_ACTIVITY;
        }

        public final int getMY_REQUEST_CODE() {
            return MyConstants.MY_REQUEST_CODE;
        }
    }
}
