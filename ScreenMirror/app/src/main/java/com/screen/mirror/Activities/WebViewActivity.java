package com.screen.mirror.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.screen.mirror.R;
import com.screen.mirror.databinding.ActivityWebViewBinding;

import java.util.Map;

public class WebViewActivity extends AppCompatActivity {
    private final String ytLink = "https://www.youtube.com";
    private ActivityWebViewBinding binding;
    private Context context;
    private AdView adViewTop,adviewBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Adloading

        context = WebViewActivity.this;

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl(ytLink);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBar.setVisibility(View.GONE);
                binding.webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                binding.progressBar.setVisibility(View.GONE);
                binding.webView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}