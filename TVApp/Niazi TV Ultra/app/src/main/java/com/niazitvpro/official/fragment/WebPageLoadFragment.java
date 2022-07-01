package com.niazitvpro.official.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.MainActivity;
import com.niazitvpro.official.utils.AdmobBannerAd;
import com.niazitvpro.official.utils.SharedPrefTVApp;
import com.niazitvpro.official.utils.VideoEnabledWebChromeClient;
import com.niazitvpro.official.utils.VideoEnabledWebView;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.niazitvpro.official.activity.MainActivity.containsLink;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebPageLoadFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar layoutProgress;
    private String liveUrl;
    private SharedPrefTVApp sharedPrefTVApp;
    private int appBackgroundColor;
    private LinearLayout rl_main;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String HTML_TAG_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
    ViewGroup webGroup;
    private String userAgent;
    private RelativeLayout rl_ADLayout;
    private View loadingView,nonVideoLayout;
    private ViewGroup videoLayout;
    public static VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Activity a = getActivity();
        if(isVisibleToUser){
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public WebPageLoadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_page_load, container, false);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onViewCreated(view, savedInstanceState);

        AdmobBannerAd admobBannerAd = new AdmobBannerAd();
        admobBannerAd.loadAddBanner(view,getActivity());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        sharedPrefTVApp = new SharedPrefTVApp(getActivity());

        swipeRefreshLayout = view.findViewById(R.id.refresh_webpage);
        rl_ADLayout = view.findViewById(R.id.rl_ad_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        rl_main = view.findViewById(R.id.ll_webpage);

        webView = (VideoEnabledWebView)view.findViewById(R.id.webView);

         nonVideoLayout = view.findViewById(R.id.nonVideoLayout);
         videoLayout = (ViewGroup)view.findViewById(R.id.videoLayout);
         loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null);

        layoutProgress = view.findViewById(R.id.google_progress);
        SharedPrefTVApp.runTimer(getActivity(),layoutProgress);



        if(savedInstanceState==null){
            webView.post(new Runnable() {
                @Override
                public void run() {
                    loadFullscreenPage();
                }
            });
        }

        // new for download any file externally from browser //

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        // new added above //


        webView.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    WebView webView = (WebView) v;

                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                            if(webView.canGoBack())
                            {

                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });

    }

    private class InsideWebViewClient extends WebViewClient {
        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void loadFullscreenPage(){

        liveUrl = getArguments().getString("liveUrl");
        userAgent = getArguments().getString("userAgent");
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //FOR WEBPAGE SLOW UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setDomStorageEnabled(true);
        //improve webView performance//
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setEnableSmoothTransition(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        // yahan se desktop view off or on  hoga //
        webView.getSettings().setUseWideViewPort(false);
        //improve webView performance//


        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                if (progress >= 85 || progress <= 1) {
                    layoutProgress.setVisibility(View.GONE);

                } else {
                    layoutProgress.setVisibility(View.VISIBLE);
                }
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                if (fullscreen)
                {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    MainActivity.cardView.setVisibility(View.GONE);
                    rl_ADLayout.setVisibility(View.GONE);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                }
                else
                {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    MainActivity.cardView.setVisibility(View.VISIBLE);
                    rl_ADLayout.setVisibility(View.VISIBLE);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new InsideWebViewClient());

        webView.getSettings().setUserAgentString(userAgent);

        boolean isUrl = containsLink(liveUrl);

        if(isUrl){

            if(validateHtml(liveUrl)){

                webView.loadDataWithBaseURL("file:///android_asset/", liveUrl, "text/html", "utf-8", null);
                webView.getSettings().setAllowFileAccess(true);


            }else {

                webView.loadUrl(liveUrl);

            }

        }else {

            webView.loadDataWithBaseURL("file:///android_asset/", liveUrl, "text/html", "utf-8", null);
            webView.getSettings().setAllowFileAccess(true);


        }

    }


    static Pattern htmlValidator = TextUtils.isEmpty(HTML_TAG_PATTERN) ? null:Pattern.compile(HTML_TAG_PATTERN);

    public static boolean validateHtml(final String text){
        if(htmlValidator !=null)
            return htmlValidator.matcher(text).find();
        return false;
    }

    @Override
    public void onRefresh() {

        webView.reload();
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            MainActivity.cardView.setVisibility(View.GONE);
            rl_ADLayout.setVisibility(View.GONE);


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

            if(!webChromeClient.isVideoFullscreen()){

                MainActivity.cardView.setVisibility(View.VISIBLE);
                rl_ADLayout.setVisibility(View.VISIBLE);

            }

        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        webView.restoreState(savedInstanceState);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity.cardView.setVisibility(View.VISIBLE);
        rl_ADLayout.setVisibility(View.VISIBLE);
        webView.destroy();

    }

}
