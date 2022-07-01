package com.example.profilepicturedownloaderforinstagrame.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.example.profilepicturedownloaderforinstagrame.R;
import com.example.profilepicturedownloaderforinstagrame.interfaces.AuthenticationListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
public class AuthenticationDialog extends Dialog {
    AuthenticationListener listener;
    String request_url;
    String redirect_url;
    Context context;
    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.redirect_url = context.getResources().getString(R.string.redirect_url);
        this.request_url = context.getResources().getString(R.string.redirect_url);
        this.request_url = context.getResources().getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                context.getResources().getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=token&display=touch&scope=public_content";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);
        initilizaeWebView();
    }
    public void initilizaeWebView() {
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(webViewClient);
            Log.d("TAG", "initializeWebView: "+redirect_url);
    }
    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(redirect_url)) {
                AuthenticationDialog.this.dismiss();
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
                Log.d("TAG", "onPageFinished: "+url);
                access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
                Log.e("access_token ", access_token);
                listener.onTockenRecieved(access_token);
            } else if (url.contains("?error")) {
                Log.e("access_token", "getting error fetching access token");
                dismiss();
            }
        }
    };
}