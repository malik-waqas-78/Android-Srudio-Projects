package com.example.profilepicturedownloaderforinstagrame.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.profilepicturedownloaderforinstagrame.R;
import com.example.profilepicturedownloaderforinstagrame.interfaces.AuthenticationListener;

public class AuthenticationActivity extends AppCompatActivity implements AuthenticationListener {
    String request_url;
    String redirect_url;
    AuthenticationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_activity);
        redirect_url = getResources().getString(R.string.redirect_url);
        request_url = getResources().getString(R.string.redirect_url);
        request_url = getResources().getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                getResources().getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=token&display=touch&scope=public_content";
        initializeWebView();
    }
    public void initializeWebView() {
        WebView webView = findViewById(R.id.activity_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(webViewClient);
        Log.d("TAG", "initializeWebView: " + redirect_url);
    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(redirect_url)) {
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.contains("access_token")) {
                Uri uri = Uri.EMPTY.parse(url);
                String access_token = uri.getEncodedFragment();
                Log.d("TAG", "onPageFinished: " + url);
                access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
                Log.e("access_token ", access_token);
                listener.onTockenRecieved(access_token);
            } else if (url.contains("?error")) {
                Log.e("access_token", "getting error fetching access token");
            }
        }
    };

    @Override
    public void onTockenRecieved(String auth_tocken) {

    }
}