package com.test.aasanload.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/easy/recharge/permissions/MyPermissions;", "", "()V", "Companion", "app_release"}, k = 1, mv = {1, 4, 2})
/* compiled from: MyPermissions.kt */
public final class MyPermissions {
/*    public static final Companion Companion = new Companion();
    private static String[] permissions = new String[]{"android.permission.CAMERA", "android.permission.CALL_PHONE"};

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0014B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u0013\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u000eR\"\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u000e¢\u0006\u0010\n\u0002\u0010\n\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t¨\u0006\u0015"}, d2 = {"Lcom/easy/recharge/permissions/MyPermissions$Companion;", "", "()V", "permissions", "", "", "getPermissions", "()[Ljava/lang/String;", "setPermissions", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "hasCallPermission", "", "context", "Landroid/content/Context;", "hasCameraPermission", "openAppPermissionsSettings", "", "requestCallPermission", "requestCameraPermission", "OpenSettingsForPermissions", "app_release"}, k = 1, mv = {1, 4, 2})
    *//* compiled from: MyPermissions.kt *//*
    public static final class Companion {

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lcom/easy/recharge/permissions/MyPermissions$Companion$OpenSettingsForPermissions;", "", "allowCameraPermissions", "", "context", "Landroid/content/Context;", "app_release"}, k = 1, mv = {1, 4, 2})
        *//* compiled from: MyPermissions.kt *//*
        public interface OpenSettingsForPermissions {
            void allowCameraPermissions(Context context);
        }

        private Companion() {
        }

        public *//* synthetic *//* Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String[] getPermissions() {
            return MyPermissions.permissions;
        }

        public final void setPermissions(String[] strArr) {
            Intrinsics.checkNotNullParameter(strArr, "<set-?>");
            MyPermissions.permissions = strArr;
        }

        public final boolean hasCameraPermission(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            return ContextCompat.checkSelfPermission(context, ((Companion) this).getPermissions()[0]) == 0;
        }

        public final void requestCameraPermission(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            String[] strArr = new String[1];
            Companion companion = this;
            strArr[0] = companion.getPermissions()[0];
            if (VERSION.SDK_INT >= 23) {
                Activity activity = (Activity) context;
                if (activity.shouldShowRequestPermissionRationale(companion.getPermissions()[0])) {
                    com.easy.recharge.dialogues.MyDialogues.Companion companion2 = MyDialogues.Companion;
                    String string = activity.getResources().getString(R.string.camera_permission_title);
                    Intrinsics.checkNotNullExpressionValue(string, "context.resources.getStr….camera_permission_title)");
                    String string2 = activity.getResources().getString(R.string.camera_permission_msg);
                    Intrinsics.checkNotNullExpressionValue(string2, "context.resources.getStr…ng.camera_permission_msg)");
                    companion2.showRational(context, string, string2, new MyPermissions$Companion$requestCameraPermission$1());
                    return;
                }
                ActivityCompat.requestPermissions(activity, strArr, MyConstants.Companion.getCAMERA_PERMISIION_REQUEST_CODE());
                return;
            }
            ActivityCompat.requestPermissions((Activity) context, strArr, MyConstants.Companion.getCAMERA_PERMISIION_REQUEST_CODE());
        }

        public final void openAppPermissionsSettings(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            Uri fromParts = Uri.fromParts("package", context.getPackageName(), null);
            Intrinsics.checkNotNullExpressionValue(fromParts, "Uri.fromParts(\"package\",…ontext.packageName, null)");
            intent.setData(fromParts);
            ((Activity) context).startActivityForResult(intent, MyConstants.Companion.getPERMISSION_SETTINGS());
        }

        public final boolean hasCallPermission(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            return ContextCompat.checkSelfPermission(context, ((Companion) this).getPermissions()[1]) == 0;
        }

        public final void requestCallPermission(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            String[] strArr = new String[1];
            Companion companion = this;
            strArr[0] = companion.getPermissions()[1];
            if (VERSION.SDK_INT >= 23) {
                Activity activity = (Activity) context;
                if (activity.shouldShowRequestPermissionRationale(companion.getPermissions()[1])) {
                    com.easy.recharge.dialogues.MyDialogues.Companion companion2 = MyDialogues.Companion;
                    String string = activity.getResources().getString(R.string.call_permission_title);
                    Intrinsics.checkNotNullExpressionValue(string, "context.resources.getStr…ng.call_permission_title)");
                    String string2 = activity.getResources().getString(R.string.call_permission_msg);
                    Intrinsics.checkNotNullExpressionValue(string2, "context.resources.getStr…ring.call_permission_msg)");
                    companion2.showRational(context, string, string2, new MyPermissions$Companion$requestCallPermission$1());
                    return;
                }
                ActivityCompat.requestPermissions(activity, strArr, MyConstants.Companion.getCALL_PERMISIION_REQUEST_CODE());
                return;
            }
            ActivityCompat.requestPermissions((Activity) context, strArr, MyConstants.Companion.getCALL_PERMISIION_REQUEST_CODE());
        }
    }*/
}
