package com.niazitvpro.official.fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.MainActivity;
import com.niazitvpro.official.activity.SplashScreenActivity;
import com.niazitvpro.official.adapter.NotificationAdapter;
import com.niazitvpro.official.app.MyApplication;
import com.niazitvpro.official.model.NotificationItem;
import com.niazitvpro.official.utils.AdmobBannerAd;
import com.niazitvpro.official.utils.EqualSpacingItemDecoration;
import com.niazitvpro.official.utils.ReadWriteJsonFileUtils;
import com.niazitvpro.official.utils.RetrofitUtils;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niazitvpro.official.activity.MainActivity.rl_notificationCount;
import static com.niazitvpro.official.fragment.AllVideoPlayingFragment.exoPlayer;
import static com.niazitvpro.official.fragment.AllVideoPlayingFragment.playerView;
import static com.niazitvpro.official.fragment.WebPageLoadFragment.webView;
import static com.niazitvpro.official.utils.Constants.APP_BACKGROUND_COLOR;
import static com.niazitvpro.official.utils.Constants.INSTALL_APP_DATE;
import static com.niazitvpro.official.utils.Constants.RATE_APP_MESSAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recycler_notificationList;
    private NotificationAdapter adapter;
    private NotificationItem notificationItem;
    private LinearLayout ll_main;
    private List<NotificationItem> notificationItemList = new ArrayList<>();
    private SharedPrefTVApp sharedPrefTVApp;
    private int appBackgroundColor;
    private ImageView img_delete_notification;
    private ProgressBar progressBar;
    private String android_id;
    private TextView tvNoNotification;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onViewCreated(view, savedInstanceState);

        if(webView!=null){

            webView.onPause();
        }

        AdmobBannerAd admobBannerAd = new AdmobBannerAd();
        admobBannerAd.loadAddBanner(view,getActivity());

        rl_notificationCount.setVisibility(View.GONE);
        MainActivity.notificationCount = "0";

        sharedPrefTVApp = new SharedPrefTVApp(getActivity());

        if (!sharedPrefTVApp.getString(APP_BACKGROUND_COLOR).isEmpty()) {

            appBackgroundColor = Color.parseColor(sharedPrefTVApp.getString(APP_BACKGROUND_COLOR));

        } else {

            appBackgroundColor = R.color.white;
        }

        ll_main = view.findViewById(R.id.ll_notification_main);
        ll_main.setBackgroundColor(appBackgroundColor);

        recycler_notificationList = view.findViewById(R.id.notification_list);
        img_delete_notification = view.findViewById(R.id.img_delete_notification);
        progressBar = view.findViewById(R.id.progress_notification);
        tvNoNotification = view.findViewById(R.id.tv_no_notification);

        img_delete_notification.setOnClickListener(this);
        notificationItemList.clear();

         android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Notification");
        if(getCacheResponse!=null && !getCacheResponse.equals("")){

            setRecycler_notificationList(getCacheResponse);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        getNotificationlList(android_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            },1000);

        }else {

            try {
                SharedPrefTVApp.runTimer(getActivity(),progressBar);
                progressBar.setVisibility(View.VISIBLE);
                getNotificationlList(android_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private void getNotificationlList(String androidID) throws JSONException {

        if(!SplashScreenActivity.isNetworkAvailable(getActivity())){

            String getCacheResponse = new ReadWriteJsonFileUtils(getContext()).readJsonFileData("Notification");

            if(getCacheResponse!=null && !getCacheResponse.equals("")){

                setRecycler_notificationList(getCacheResponse);

            }


        }else {

            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPi(getActivity()).getNotificationList(androidID);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        try {
                            String str_response = responseBody.string();

                            try {
                                new ReadWriteJsonFileUtils(getContext()).createJsonFileData("Notification", str_response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            setRecycler_notificationList(str_response);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(MyApplication.getAppContext(), "something went wrong!!!please check your connection", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            });

        }


    }

    private void clearNotificationList(String androidID) throws JSONException {

            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPi(getActivity()).clearUserNotificationList(androidID);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        try {
                            String str_response = responseBody.string();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(MyApplication.getAppContext(), "something went wrong!!!please check your connection", Toast.LENGTH_SHORT).show();

                }
            });

        }





    private  void setRecycler_notificationList(String jsonResponse){

        notificationItemList.clear();

        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(jsonResponse);
            String status = jsonObject.getString("status");

            if (jsonObject.has("msg")) {

                String message = jsonObject.getString("msg");

            } else {

            }

            if (status.equals("1")) {

                JSONArray data = jsonObject.getJSONArray("data");
                notificationItem = new NotificationItem(data);

                for(int i= 0 ;i<notificationItem.notificationItemList.size();i++){

                    String installDate = sharedPrefTVApp.getString(INSTALL_APP_DATE);
                    Date date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(installDate);

                    String notificationDate = notificationItem.notificationItemList.get(i).notificationTime;
                    Date date1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(notificationDate);

                    if (date1.after(date)){

                       notificationItemList.add(notificationItem.notificationItemList.get(i));

                    }
                }

                Collections.sort(notificationItemList, new Comparator<NotificationItem>() {
                    @Override
                    public int compare(NotificationItem o1, NotificationItem o2) {
                        try {
                            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                            long time = convertStringTOLong(o1.notificationTime);
                            cal.setTimeInMillis(time * 1000L);
                            String date = DateFormat.format("yyyy-MM-dd hh:mm:ss", cal).toString();

                            Calendar cal2 = Calendar.getInstance(Locale.ENGLISH);
                            long time2 = convertStringTOLong(o2.notificationTime);
                            cal2.setTimeInMillis(time2 * 1000L);
                            String date2 = DateFormat.format("yyyy-MM-dd hh:mm:ss", cal2).toString();

                            return date2.compareTo(date);

                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                if(notificationItemList.size()>=9999999){

                    int count = notificationItemList.size()-9999998 ;

                    for(int i=0;i<count;i++){

                        notificationItemList.remove(notificationItemList.get(0));
                        if(adapter!=null){

                            adapter.notifyDataSetChanged();
                        }

                    }

                }

                adapter = new NotificationAdapter(getActivity(), notificationItemList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
                recycler_notificationList.setHasFixedSize(true);
                recycler_notificationList.setNestedScrollingEnabled(false);
                recycler_notificationList.setLayoutManager(linearLayoutManager);
                recycler_notificationList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recycler_notificationList.setAdapter(adapter);
                    }
                },100);
                recycler_notificationList.addItemDecoration(new EqualSpacingItemDecoration(2));
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(notificationItemList.size()==0){
                    tvNoNotification.setVisibility(View.VISIBLE);
                }else {
                    tvNoNotification.setVisibility(View.GONE);
                }

            }else {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }



    private  void setClearNotificationPopup(){

        String message = sharedPrefTVApp.getString(RATE_APP_MESSAGE);
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure to delete all notifications??")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        notificationItemList.clear();
                        if(adapter!=null){

                            adapter.notifyDataSetChanged();
                        }
                        try {
                            clearNotificationList(android_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"));
    }


    @Override
    public void onClick(View v) {

        if(v==img_delete_notification){

            setClearNotificationPopup();
        }
    }

    private long convertStringTOLong(String date){

        long longTime = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date1 = sdf.parse(date);

            long startDate = date1.getTime();

            longTime = startDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  longTime;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onDetach() {
        super.onDetach();

        if(webView!=null){

            webView.onResume();
        }
        if(AllVideoPlayingFragment.exoPlayer!=null){
            AllVideoPlayingFragment.playerView.onResume();
            AllVideoPlayingFragment.exoPlayer.setPlayWhenReady(true);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        if(ChannelSubcategoryFragment.exoPlayer!=null){
            ChannelSubcategoryFragment.playerView.onResume();
            ChannelSubcategoryFragment.exoPlayer.setPlayWhenReady(true);
        }
    }
}
