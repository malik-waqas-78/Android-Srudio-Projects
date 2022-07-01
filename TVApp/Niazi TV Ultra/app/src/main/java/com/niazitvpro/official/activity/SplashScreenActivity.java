package com.niazitvpro.official.activity;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.niazitvpro.official.R;
import com.niazitvpro.official.app.MyApplication;
import com.niazitvpro.official.model.SplashModel;
import com.niazitvpro.official.utils.ReadWriteJsonFileUtils;
import com.niazitvpro.official.utils.RetrofitUtils;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niazitvpro.official.app.MyApplication.notificationData;
import static com.niazitvpro.official.utils.Constants.ADMOB_BANNER;
import static com.niazitvpro.official.utils.Constants.ADMOB_INTERESTITIAL;
import static com.niazitvpro.official.utils.Constants.ADMOB_NATIVE;
import static com.niazitvpro.official.utils.Constants.ADS_TYPE;
import static com.niazitvpro.official.utils.Constants.APP_BACKGROUND_COLOR;
import static com.niazitvpro.official.utils.Constants.APP_LOGO;
import static com.niazitvpro.official.utils.Constants.FACEBOOK_BANNER;
import static com.niazitvpro.official.utils.Constants.FACEBOOK_INTERESTITIAL;
import static com.niazitvpro.official.utils.Constants.FACEBOOK_NATIVE;
import static com.niazitvpro.official.utils.Constants.IS_FIRST_TIME;
import static com.niazitvpro.official.utils.Constants.PRIVACY_POLICY;
import static com.niazitvpro.official.utils.Constants.RATE_APP;
import static com.niazitvpro.official.utils.Constants.RATE_APP_MESSAGE;
import static com.niazitvpro.official.utils.Constants.WELCOME_MESSAGE;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int MULTIPLE_PERMISSIONS = 0;
    private ImageView splash_image;
    private SharedPrefTVApp sharedPrefTVApp;
    private static Context context;
    private static NetworkInfo activeNetworkInfo;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private boolean isAllPermisstionGranted = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        SharedPrefTVApp.transparentStatusAndNavigation(SplashScreenActivity.this);

        context = getApplicationContext();

        progressBar = findViewById(R.id.google_progress);
        SharedPrefTVApp.runTimer(SplashScreenActivity.this,progressBar);

        if(!isNetworkAvailable(SplashScreenActivity.this)){

            Toast.makeText(MyApplication.getAppContext(), "something went wrong!!!please check your connection", Toast.LENGTH_SHORT).show();
        }

        sharedPrefTVApp = new SharedPrefTVApp(getApplicationContext());
        splash_image = findViewById(R.id.splash_image);

        if(sharedPrefTVApp.getString(RATE_APP).isEmpty()){

            sharedPrefTVApp.putBoolean(IS_FIRST_TIME,true);

        }else {

            sharedPrefTVApp.putBoolean(IS_FIRST_TIME,false);

        }

        if(sharedPrefTVApp.getBoolean(IS_FIRST_TIME)){

            if(checkPermissions()){
                setSplashApi();
            }

        }else {

            if(!isNetworkAvailable(SplashScreenActivity.this)){

                setSplashApi();

            }else {

                String getCacheResponse = new ReadWriteJsonFileUtils(getApplicationContext()).readJsonFileData("splash");

                if(getCacheResponse!=null && !getCacheResponse.equals("")){

                    Glide.with(getApplicationContext())
                            .load(getCacheResponse)
                            .apply(new RequestOptions().placeholder(android.R.color.black)
                                    .error((int) android.R.color.black).
                                            diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .priority(Priority.HIGH))
                            .transition(GenericTransitionOptions.with(R.anim.fade_in))
                            .into(splash_image);

                    loadSplashFromChache(getCacheResponse);
                    saveSplashUpdate();

                }else {

                    setSplashApi();
                }
            }



        }

    }

    private void setSplashApi() {
        if (!isNetworkAvailable(SplashScreenActivity.this)) {

            String getCacheResponse = new ReadWriteJsonFileUtils(getApplicationContext()).readJsonFileData("splash");
            if (getCacheResponse != null && !getCacheResponse.equals("")) {

                Glide.with(getApplicationContext())
                        .load(getCacheResponse)
                        .apply(new RequestOptions().placeholder(android.R.color.black)
                                .error((int) android.R.color.black).
                                        diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH))
                        .transition(GenericTransitionOptions.with(R.anim.fade_in))
                        .into(splash_image);

                loadSplashFromChache(getCacheResponse);

            } else {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("notificationData",notificationData);
                startActivity(intent);
            }

        } else {

            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPi(getApplicationContext()).setting();
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("ResourceType")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        try {
                            String str_response = responseBody.string();


                            try {
                                setSplash_image(str_response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(MyApplication.getAppContext(), "something went wrong!!!please check your connection", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("notificationData",notificationData);
                    startActivity(intent);
                    finish();

                }
            });
        }
    }

    private void saveSplashData(SplashModel model){

        sharedPrefTVApp.putString(WELCOME_MESSAGE,model.welcomeMessage);
        sharedPrefTVApp.putString(RATE_APP,model.rateApp);
        sharedPrefTVApp.putString(RATE_APP_MESSAGE,model.rateAppMessage);
        sharedPrefTVApp.putString(PRIVACY_POLICY,model.privacyPolicy);
        sharedPrefTVApp.putString(APP_BACKGROUND_COLOR,model.background_color);
        sharedPrefTVApp.putString(ADS_TYPE,model.adType);
        sharedPrefTVApp.putString(ADMOB_BANNER,model.admobBanner);
        sharedPrefTVApp.putString(ADMOB_NATIVE,model.admobNative);
        sharedPrefTVApp.putString(ADMOB_INTERESTITIAL,model.admobInterestitial);
        sharedPrefTVApp.putString(FACEBOOK_BANNER,model.facebookBanner);
        sharedPrefTVApp.putString(FACEBOOK_NATIVE,model.facebookNative);
        sharedPrefTVApp.putString(FACEBOOK_INTERESTITIAL,model.facebookInterestitial);
        sharedPrefTVApp.putString(APP_LOGO,model.appLogo);

    }

    public static boolean isNetworkAvailable(Activity context) {

        if(context!=null){

            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
             activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private  void startMainActivity(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sharedPrefTVApp.getBoolean(IS_FIRST_TIME)) {

                    Intent intent = new Intent(getApplicationContext(),WelcomeScreenActivity.class);
                    startActivity(intent);
                    finish();

                }else {

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("notificationData",notificationData);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();

                }



            }
        },3000);
    }

    private void loadSplashFromChache(String getCacheResponse){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("notificationData",notificationData);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();


            }
        },2000);

    }

    private  void saveSplashUpdate(){

        Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPi(getApplicationContext()).setting();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null && response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    try {
                        String str_response = responseBody.string();

                        JSONObject jsonObject = new JSONObject(str_response);
                        String status = jsonObject.getString("status");
                        String messge = jsonObject.getString("msg");

                        if(status.equals("1")){

                            JSONObject data = jsonObject.getJSONObject("data");
                            SplashModel splashModel = new SplashModel(data);
                            saveSplashData(splashModel);
                            try {
                                new ReadWriteJsonFileUtils(getApplicationContext()).createJsonFileData("splash", splashModel.splashImageUrl);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(MyApplication.getAppContext(), "something went wrong!!!please check your connection", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("notificationData",notificationData);
                startActivity(intent);
            }
        });
    }

    private  void setSplash_image(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        String status = jsonObject.getString("status");
        String messge = jsonObject.getString("msg");

        if (status.equals("1")) {

            JSONObject data = jsonObject.getJSONObject("data");
            SplashModel splashModel = new SplashModel(data);

            saveSplashData(splashModel);
            if (splashModel.splashImageUrl != null) {

                try {
                    new ReadWriteJsonFileUtils(getApplicationContext()).createJsonFileData("splash", splashModel.splashImageUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Glide.with(getApplicationContext())
                        .load(splashModel.splashImageUrl)
                        .apply(new RequestOptions().placeholder(android.R.color.black)
                                .error((int) android.R.color.black).
                                        diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH))
                        .transition(GenericTransitionOptions.with(R.anim.fade_in))
                        .into(splash_image);

                    startMainActivity();

            }

        }

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(checkAllPermissionisGranted()){
                        setSplashApi(); // Now you call here what ever you want :)
                    }else {
                        permissionNotGrantedDialog();
                    }
                } else {
                  permissionNotGrantedDialog();
                }
                return;
            }
        }
    }

    private boolean checkAllPermissionisGranted(){
        for (String p : permissions) {
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result == PackageManager.PERMISSION_GRANTED) {
                isAllPermisstionGranted = true;
            }else {
                isAllPermisstionGranted = false;
            }
        }
        return isAllPermisstionGranted;
    }

    private void permissionNotGrantedDialog(){

        AlertDialog.Builder builder   = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setMessage(R.string.__permission_not_granted)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        checkPermissions();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        finishAffinity();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(R.string.alert_permission);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
