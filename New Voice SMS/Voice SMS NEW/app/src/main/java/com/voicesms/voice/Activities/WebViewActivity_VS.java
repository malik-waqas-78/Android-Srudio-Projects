package com.voicesms.voice.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.voicesms.voice.Utils.Constants;
import com.voicesms.voice.databinding.ActivityWebViewBinding;

public class WebViewActivity_VS extends AppCompatActivity {
    ActivityWebViewBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());

        setSupportActionBar(binding.toolbarWebview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbarWebview.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
        setContentView(binding.getRoot());

        String url = getIntent().getStringExtra(Constants.SEARCH_QUERY);
        binding.webviewSearchResults.getSettings().setJavaScriptEnabled(true);
        binding.webviewSearchResults.setWebViewClient(new MyWebViewClient(binding.progressSearch));
        binding.webviewSearchResults.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (binding.webviewSearchResults.canGoBack()) {
            binding.webviewSearchResults.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public class MyWebViewClient extends WebViewClient {
        private final ProgressBar pb;

        public MyWebViewClient(ProgressBar pb) {
            this.pb = pb;
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("https") || url.startsWith("http")) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pb.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }
}