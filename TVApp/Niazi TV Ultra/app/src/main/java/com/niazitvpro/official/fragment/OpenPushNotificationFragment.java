package com.niazitvpro.official.fragment;


import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.MainActivity;
import com.niazitvpro.official.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenPushNotificationFragment extends Fragment {

    String notificationData;


    public OpenPushNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_open_push_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private  void initView(View view){

        notificationData = getArguments().getString("notificationData");

        try {
            JSONObject jsonObject = new JSONObject(notificationData);

            String channelType= jsonObject.getString("channel_type");
            String directUrl = jsonObject.getString("direct_url");
            String useragent = jsonObject.getString("user_agent");
            if (channelType.equals("0")) {

                Fragment fragment = new FMRadioPlayFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("liveUrl",directUrl);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_home, fragment, FMRadioPlayFragment.class.getName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();

            } else if (channelType.equals("1")) {

                Fragment fragment = new AllVideoPlayingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("live_tv_show_url", directUrl);
                bundle.putString("user_agent", useragent);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_home, fragment, AllVideoPlayingFragment.class.getName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();


            } else if (channelType.equals("2") || channelType.equals("3")) {

                Fragment fragment = new WebPageLoadFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("liveUrl",directUrl);
                bundle.putString("userAgent",useragent);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_home, fragment, WebPageLoadFragment.class.getName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();

            }

            MyApplication.notificationData = "";
            MainActivity.notificationDataHome = "";



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onDetach() {
        super.onDetach();

        AllVideoPlayingFragment.exoPlayer=null;
        AllVideoPlayingFragment.playerView=null;
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }
}
