package com.speak.to.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.speak.to.R;
import com.speak.to.databinding.ActivitySendMessageBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.util.ArrayList;

import static com.speak.to.Utils.Constants.INSTAGRAM_PACKAGE_NAME;
import static com.speak.to.Utils.Constants.KEY_TXT_MESSAGE;
import static com.speak.to.Utils.Constants.MESSAGES_PACKAGE_NAME;
import static com.speak.to.Utils.Constants.MESSENGER_PACKAGE_NAME;
import static com.speak.to.Utils.Constants.TWITTER_PACKAGE_NAME;
import static com.speak.to.Utils.Constants.VIBER_PACKAGE_NAME;
import static com.speak.to.Utils.Constants.WHATSAPP_PACKAGE_NAME;
import static com.speak.to.Utils.Constants.files_list;

public class SendMessageActivity extends AppCompatActivity {
    ActivitySendMessageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadBannerAdd();

        String textInput = getIntent().getStringExtra(KEY_TXT_MESSAGE);
        ArrayList<Uri> URI_LIST = new ArrayList<>();
        for (int i = 0; i < files_list.size(); i++) {
            URI_LIST.add(Uri.parse(files_list.get(i).getFileUri()));
        }

        binding.whatsappButton.setOnClickListener(view -> {
//            startShareTextIntent(WHATSAPP_PACKAGE_NAME, textInput);
            startShareImagesIntent(WHATSAPP_PACKAGE_NAME, URI_LIST, textInput);
        });

        binding.instaButton.setOnClickListener(view -> {
//            startShareTextIntent(INSTAGRAM_PACKAGE_NAME, textInput);
            startShareImagesIntent(INSTAGRAM_PACKAGE_NAME, URI_LIST, textInput);
        });

        binding.twitterButton.setOnClickListener(view -> {
//            startShareTextIntent(TWITTER_PACKAGE_NAME, textInput);
            startShareImagesIntent(TWITTER_PACKAGE_NAME, URI_LIST, textInput);
        });

        binding.messengerButton.setOnClickListener(view -> {
//            startShareTextIntent(MESSENGER_PACKAGE_NAME, textInput);
            startShareImagesIntent(MESSENGER_PACKAGE_NAME, URI_LIST, textInput);
        });

        binding.viberButton.setOnClickListener(view -> {
//            startShareTextIntent(VIBER_PACKAGE_NAME, textInput);
            startShareImagesIntent(VIBER_PACKAGE_NAME, URI_LIST, textInput);
        });

        binding.messagesButton.setOnClickListener(view -> {
//            startShareTextIntent(MESSAGES_PACKAGE_NAME, textInput);
            startShareImagesIntent(MESSAGES_PACKAGE_NAME, URI_LIST, textInput);
        });
    }

    private void startShareImagesIntent(String packageName, ArrayList<Uri> uriList, String textInput) {
        if (uriList.size() > 0) {
            Intent img_share_intent = new Intent();
            img_share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            img_share_intent.setType("text/plain");
            img_share_intent.setType("*/*");
            img_share_intent.setPackage(packageName);
            img_share_intent.putExtra(Intent.EXTRA_TEXT, textInput);
            img_share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            startActivity(Intent.createChooser(img_share_intent, "Share with"));
        } else {
            startShareTextIntent(packageName, textInput);
        }
    }

    private void startShareTextIntent(String packageName, String textInput) {
        PackageManager pm = getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            PackageInfo info = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage(packageName);

            waIntent.putExtra(Intent.EXTRA_TEXT, textInput);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, packageName + " is not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    public void loadBannerAdd() {
        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);

        AdListener adListener = new AdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAG", "onError: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d("TAG", "Ad loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
        relativeLayout.addView(adView);
    }
}