package com.niazitvpro.official.fragment;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.niazitvpro.official.R;
import com.niazitvpro.official.service.RadioService;
import com.niazitvpro.official.utils.AdmobBannerAd;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import static com.niazitvpro.official.service.RadioService.exoPlayer;
import static com.niazitvpro.official.service.RadioService.playRadio;
import static com.niazitvpro.official.utils.Constants.APP_BACKGROUND_COLOR;

/**
 * A simple {@link Fragment} subclass.
 */
public class FMRadioPlayFragment extends Fragment {

    public static ToggleButton toggleplayButton;
    private String liveUrl;
    private SharedPrefTVApp sharedPrefTVApp;
    private int appBackgroundColor;
    private RelativeLayout ll_main;

    public FMRadioPlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fmradio_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPrefTVApp = new SharedPrefTVApp(getActivity());

        AdmobBannerAd admobBannerAd = new AdmobBannerAd();
        admobBannerAd.loadAddBanner(view,getActivity());


        ll_main =view.findViewById(R.id.ll_fmradio);

        if (!sharedPrefTVApp.getString(APP_BACKGROUND_COLOR).isEmpty()) {

            appBackgroundColor = Color.parseColor(sharedPrefTVApp.getString(APP_BACKGROUND_COLOR));

        } else {

            appBackgroundColor = R.color.white;
        }

        ll_main.setBackgroundColor(appBackgroundColor);

        liveUrl = getArguments().getString("liveUrl");

        toggleplayButton = view.findViewById(R.id.toggle_play_pause);
        toggleplayButton.setChecked(true);

        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.O){

            if (!isMyServiceRunning(getActivity(), RadioService.class)) {

                getActivity().startForegroundService(new Intent(getActivity(), RadioService.class));
            }

        } else{

            if (!isMyServiceRunning(getActivity(), RadioService.class)) {
                getActivity().startService(new Intent(getActivity(), RadioService.class));
            }
        }

        setUrlToPlayer();

        this.toggleplayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {

                    exoPlayer.setPlayWhenReady(false);

                } else {

                    exoPlayer.setPlayWhenReady(true);

                }
            }
        });
    }

    private void setUrlToPlayer() {

        RadioService.getPlayerInstance(getActivity());
        playRadio(liveUrl);
    }

    public  static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(exoPlayer!=null){

            exoPlayer.setPlayWhenReady(false);

        }

    }
}
