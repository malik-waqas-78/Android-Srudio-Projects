package com.screen.mirror.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.screen.mirror.Adapters.LocaleAdapter;
import com.screen.mirror.Interfaces.LocaleSelectionInterface;
import com.screen.mirror.Models.LocaleModel;
import com.screen.mirror.R;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.SharedPrefHelperCA;
import com.screen.mirror.databinding.ActivityLanguageBinding;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import static com.screen.mirror.Utils.Constants_CA.ARABIC;
import static com.screen.mirror.Utils.Constants_CA.BENGALI;
import static com.screen.mirror.Utils.Constants_CA.ENGLISH;
import static com.screen.mirror.Utils.Constants_CA.FRENCH;
import static com.screen.mirror.Utils.Constants_CA.GERMAN;
import static com.screen.mirror.Utils.Constants_CA.HINDI;
import static com.screen.mirror.Utils.Constants_CA.INDONESIAN;
import static com.screen.mirror.Utils.Constants_CA.ITALIAN;
import static com.screen.mirror.Utils.Constants_CA.JAPANESE;
import static com.screen.mirror.Utils.Constants_CA.LANGUAGE_KEY;
import static com.screen.mirror.Utils.Constants_CA.CHINESE;
import static com.screen.mirror.Utils.Constants_CA.PORTUGUESE;
import static com.screen.mirror.Utils.Constants_CA.RUSSIAN;
import static com.screen.mirror.Utils.Constants_CA.SERBIAN;
import static com.screen.mirror.Utils.Constants_CA.SPANISH;
import static com.screen.mirror.Utils.Constants_CA.THAI;
import static com.screen.mirror.Utils.Constants_CA.TURKISH;
import static com.screen.mirror.Utils.Constants_CA.URDU;

public class LanguageActivity_OKRA extends AppCompatActivity {
    ActivityLanguageBinding binding;
    Context context;
    SharedPrefHelperCA sharedPrefHelperSM;
    ArrayList<LocaleModel> localeList;
    LocaleAdapter adapter;
    private AdView adViewTop,adviewBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ////adloading
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(LanguageActivity_OKRA.this)) {
            initSdkandLoadBottomBannerAd();
        }
        context = LanguageActivity_OKRA.this;
        sharedPrefHelperSM = new SharedPrefHelperCA(context);

        setSupportActionBar(binding.toolbarLanguage);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        localeList = getLangList();
        binding.langListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new LocaleAdapter(localeList, context, new LocaleSelectionInterface() {
            @Override
            public void OnClick(int adapterPosition) {
                OnLocaleClicked(adapterPosition);
            }
        });
        binding.langListRecyclerView.setAdapter(adapter);

    }

    private void OnLocaleClicked(int adapterPosition) {
        LocaleModel model = localeList.get(adapterPosition);
        changeLocale(context, model.getLocaleCode());
    }

    private void changeLocale(Context context, String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.locale = locale;
        } else {
            config.setLocale(new Locale(localeCode));
        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        sharedPrefHelperSM.Set_String_SM(LANGUAGE_KEY, localeCode);
        Intent intent = new Intent(context, MainActivity_OKRA.class);
        context.startActivity(intent);
        finish();
    }

    private ArrayList<LocaleModel> getLangList() {
        ArrayList<LocaleModel> mData = new ArrayList<>();
        mData.add(new LocaleModel(getResources().getString(R.string.english), ENGLISH, R.drawable.ic_united_kingdom));
        mData.add(new LocaleModel(getResources().getString(R.string.urdu), URDU, R.drawable.ic_pakistan));
        mData.add(new LocaleModel(getResources().getString(R.string.arabic), ARABIC, R.drawable.ic_united_arab_emirates));
        mData.add(new LocaleModel(getResources().getString(R.string.turkish), TURKISH, R.drawable.ic_turkey));
        mData.add(new LocaleModel(getResources().getString(R.string.chinese), CHINESE, R.drawable.ic_china));
        mData.add(new LocaleModel(getResources().getString(R.string.hindi), HINDI, R.drawable.ic_india));
        mData.add(new LocaleModel(getResources().getString(R.string.serbian), SERBIAN, R.drawable.ic_serbia));
        mData.add(new LocaleModel(getResources().getString(R.string.japanese), JAPANESE, R.drawable.ic_japan));
        mData.add(new LocaleModel(getResources().getString(R.string.bengali), BENGALI, R.drawable.ic_bangladesh));
        mData.add(new LocaleModel(getResources().getString(R.string.russian), RUSSIAN, R.drawable.ic_russia));
        mData.add(new LocaleModel(getResources().getString(R.string.indonesian), INDONESIAN, R.drawable.ic_indonesia));
        mData.add(new LocaleModel(getResources().getString(R.string.italian), ITALIAN, R.drawable.ic_italy));
        mData.add(new LocaleModel(getResources().getString(R.string.spanish), SPANISH, R.drawable.ic_spain));
        mData.add(new LocaleModel(getResources().getString(R.string.french), FRENCH, R.drawable.ic_france));
        mData.add(new LocaleModel(getResources().getString(R.string.german), GERMAN, R.drawable.ic_germany));
        mData.add(new LocaleModel(getResources().getString(R.string.portuguese), PORTUGUESE, R.drawable.ic_brazil));
        mData.add(new LocaleModel(getResources().getString(R.string.thai), THAI, R.drawable.ic_thailand));
        return mData;
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    public void initSdkandLoadBottomBannerAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> map = initializationStatus.getAdapterStatusMap();
                for (Map.Entry<String,AdapterStatus> entry : map.entrySet()) {
                    String className = entry.getKey();
                    AdapterStatus status = entry.getValue();
                    if (status.getInitializationState().equals(AdapterStatus.State.NOT_READY)) {

                        // The adapter initialization did not complete.
                        Log.d("92727586243", "Adapter: " + className + " not ready.");
                    }

                    else if (status.getInitializationState().equals(AdapterStatus.State.READY))  {
                        // The adapter was successfully initialized.
                        Log.d("92727586243", "Adapter: " + className + " is initialized.");
                    }

                }
                loadTopBanner();
               // loadBottomBanner();
            }
        });
    }


    private void loadTopBanner() {
        adViewTop = new com.google.android.gms.ads.AdView(this);
        adViewTop.setAdUnitId(getString(R.string.admob_banner));


        com.google.android.gms.ads.AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adViewTop.setAdSize(adSize);
        // Step 5 - Start loading the ad in the background.

        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();
        adViewTop.loadAd(adRequest);
        adViewTop.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
        binding.topBanner.addView(adViewTop);

    }
    public com.google.android.gms.ads.AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}