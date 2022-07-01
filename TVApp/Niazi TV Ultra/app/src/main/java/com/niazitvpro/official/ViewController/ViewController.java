package com.niazitvpro.official.ViewController;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.niazitvpro.official.R;
import com.niazitvpro.official.app.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ViewController {

    protected static ProgressDialog progressDialog;
    private  static String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};


    public static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();

            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(activity, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 0);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
        return true;
    }

    public static void showProgressDialog(Activity activity, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
        }

        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(message);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    public static void dismissProgressDialog() {
        // For handling orientation changed.
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();

            }
        } catch (Exception e) {
        }
    }

    public static void openPopupDialog(Activity activity, String title, String message, final String positiveText, final popupPositiveBtnClick positiveBtnClick){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        positiveBtnClick.onSuccess();
                    }

                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }

                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void openPopupDialogOnlyPositiveButton(Activity activity, String title, String message, final String positiveText, final popupPositiveBtnClick positiveBtnClick){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        positiveBtnClick.onSuccess();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public interface popupPositiveBtnClick{
        void onSuccess();
    }

//    public static void doSocialShare(Activity activity,String url){
//        Uri imageUri = generateImageUri(activity,url);
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("*/*");
//        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        try {
//            activity.startActivityForResult(sharingIntent,1);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(activity, activity.getResources().getString(R.string.file_not_supported), Toast.LENGTH_SHORT).show();
//        }
//
//    }

    private static Uri generateImageUri(Activity activity,String path){
        Uri imageUri =null;
        java.io.OutputStream out;
        File file = new File(path);
        try {
            if (Build.VERSION.SDK_INT >= 24){
                imageUri = FileProvider.getUriForFile(
                        activity,
                        "com.allvideo.downloader.provider",
                        file);
            }else {
                imageUri = Uri.fromFile(file);
            }

        } catch (IllegalArgumentException e) {}

        return imageUri;
    }

    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

//    public static void shareApp(Activity activity,String text, String url){
//        Uri imageUri = generateImageUri(activity);
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        if(imageUri!=null){
//            sharingIntent.setType("*/*");
//            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        }else {
//            sharingIntent.setType("text/plain");
//        }
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, text +  "\n" + activity.getString(R.string.share_mesage) + "\n" + url);
//        try {
//            activity.startActivityForResult(sharingIntent,1);
//        } catch (android.content.ActivityNotFoundException ex) {
//        }
//
//    }

    private static Uri generateImageUri(Activity activity){
        Uri imageUri =null;
        int sharableImage = R.mipmap.ic_launcher;
        Bitmap bitmap= BitmapFactory.decodeResource(activity.getResources(), sharableImage);
        String path = activity.getExternalCacheDir()+"/sharable_image.jpg";
        java.io.OutputStream out;
        File file = new File(path);
        if(!file.exists()) {
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            if (Build.VERSION.SDK_INT >= 24){
                imageUri = FileProvider.getUriForFile(
                        activity,
                        "com.allvideo.downloader.provider",
                        file);
            }else {
                imageUri = Uri.fromFile(file);
            }

        } catch (IllegalArgumentException e) {}

        return imageUri;
    }

    public static void showKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static <T> List<T> removeDuplicates(List<T> list)
    {

        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }


//    public static void showLinkNotFoundPopup(Activity activity) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        LayoutInflater inflater = activity.getLayoutInflater();
//        View dialogLayout = inflater.inflate(R.layout.popup_no_video_link_detect, null);
//        TextView tvOk = dialogLayout.findViewById(R.id.btn_ok);
//        TextView tvCancle = dialogLayout.findViewById(R.id.btn_cancle);
//        builder.setView(dialogLayout);
//        final AlertDialog alertDialog = builder.create();
//        tvOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//        tvCancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//        InsetDrawable inset = new InsetDrawable(back, 20);
//        alertDialog.getWindow().setBackgroundDrawable(inset);
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
//    }

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        }
        catch(MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        if(url.substring(startIndex,endIndex).equals("")){
            String filename = String.valueOf(new Random().nextInt());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    filename= Paths.get(new URI(url).getPath()).getFileName().toString();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            return filename+".mp4";
        }
        return url.substring(startIndex, endIndex);
    }

    public static void setClipboard(Activity context, String text) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static void shareBrowserUrl(Activity activity,String url){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.app_name) +  "\n" + url + "\n");
        try {
            activity.startActivityForResult(sharingIntent,1);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }



    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static File getDownloadLoaction() {
        File file = null;
        file = new File(Environment.getExternalStorageDirectory(), "NiaziTvPro");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
}
