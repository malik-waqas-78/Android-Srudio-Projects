package com.locker.applock.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.locker.applock.Adapters.Adapter_Theme_Selection;
import com.locker.applock.Dialogs.ThemePreviewFullScreenDialog;
import com.locker.applock.Interfaces.ThemePreviewInterface;
import com.locker.applock.Interfaces.ThemeSelectionInterface;
import com.locker.applock.Models.ThemeModel;
import com.locker.applock.R;
import com.locker.applock.Utils.SharedPrefHelper;
import com.locker.applock.databinding.ActivityThemeSelectionBinding;
import com.screen.mirror.Utils.InterAdHelper_OKRA;

import java.util.ArrayList;

import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.PATTERN;
import static com.locker.applock.Utils.Constants.SELECTED_THEME;
import static com.locker.applock.Utils.Constants.SELECTED_THEME_DEFAULT_VALUE;
import static com.locker.applock.Utils.Constants.THEME_1;
import static com.locker.applock.Utils.Constants.THEME_2;
import static com.locker.applock.Utils.Constants.THEME_3;
import static com.locker.applock.Utils.Constants.THEME_4;
import static com.locker.applock.Utils.Constants.THEME_5;
import static com.locker.applock.Utils.Constants.THEME_6;
import static com.locker.applock.Utils.Constants.THEME_7;
import static com.locker.applock.Utils.Constants.THEME_8;
import static com.locker.applock.Utils.Constants.THEME_9;

public class ThemeSelectionActivity extends AppCompatActivity {
    ActivityThemeSelectionBinding binding;
    Context context;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemeSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(ThemeSelectionActivity.this)) {
            loadTopBanner();
        }
        context = ThemeSelectionActivity.this;
        sharedPrefHelper = new SharedPrefHelper(context);

        setRecyclerView();

        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewThemesList.setLayoutManager(layoutManager);
        ArrayList<ThemeModel> mList = getList();
        int lockType = sharedPrefHelper.Get_Int_AL(LOCK_TYPE, PATTERN);
        int themeResId = (lockType == PATTERN) ? R.drawable.pattern_preview : R.drawable.pin_preview;

        Adapter_Theme_Selection adapter_door_selection = new Adapter_Theme_Selection(mList, context, new ThemeSelectionInterface() {
            @Override
            public void OnThemeSelected(ThemeModel theme) {
                ThemePreviewFullScreenDialog.ShowThemePreview(context, theme.getIcon(), themeResId, new ThemePreviewInterface() {
                    @Override
                    public void onThemeApply() {
                        sharedPrefHelper.Set_Int_AL(SELECTED_THEME, theme.getIcon());
                        setRecyclerView();
                    }
                });
            }
        });
        binding.recyclerViewThemesList.setAdapter(adapter_door_selection);
    }

    private ArrayList<ThemeModel> getList() {
        ArrayList<ThemeModel> themeList = new ArrayList<>();
        int selected_theme = sharedPrefHelper.Get_Int_AL(SELECTED_THEME, SELECTED_THEME_DEFAULT_VALUE);
        themeList.add(new ThemeModel(THEME_1, (selected_theme == THEME_1) ? 0 : 8));
        themeList.add(new ThemeModel(THEME_2, (selected_theme == THEME_2) ? 0 : 8));
        themeList.add(new ThemeModel(THEME_3, (selected_theme == THEME_3) ? 0 : 8));
        themeList.add(new ThemeModel(THEME_4, (selected_theme == THEME_4) ? 0 : 8));
        themeList.add(new ThemeModel(THEME_5, (selected_theme == THEME_5) ? 0 : 8));
        themeList.add(new ThemeModel(THEME_6, (selected_theme == THEME_6) ? 0 : 8));
        themeList.add(new ThemeModel(THEME_7, (selected_theme == THEME_7) ? 0 : 8));
        themeList.add(new ThemeModel(THEME_8, (selected_theme == THEME_8) ? 0 : 8));
        themeList.add(new ThemeModel(THEME_9, (selected_theme == THEME_9) ? 0 : 8));
        return themeList;
    }
    private void loadTopBanner() {
        AdView adViewTop = new com.google.android.gms.ads.AdView(this);
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