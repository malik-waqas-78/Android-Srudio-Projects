package com.speak.to.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.speak.to.Adapters.SearchListAdapter;
import com.speak.to.Models.Search_List_Item;
import com.speak.to.R;
import com.speak.to.Utils.Constants;
import com.speak.to.databinding.ActivitySearchOptionsListBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.util.ArrayList;

public class SearchOptionsList extends AppCompatActivity {
    private String listMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.speak.to.databinding.ActivitySearchOptionsListBinding binding = ActivitySearchOptionsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadBannerAdd();

        setSupportActionBar(binding.toolbarSearchListActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbarSearchListActivity.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        if (getIntent().hasExtra(Constants.SEARCH_MODE)) {
            listMode = getIntent().getStringExtra(Constants.SEARCH_MODE);
            getSupportActionBar().setTitle(listMode);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerviewSearchList.setLayoutManager(layoutManager);
        ArrayList<Search_List_Item> list = getList();
        SearchListAdapter adapter = new SearchListAdapter(this, list);
        binding.recyclerviewSearchList.setAdapter(adapter);
    }

    private ArrayList<Search_List_Item> getList() {
        ArrayList<Search_List_Item> arrayList = new ArrayList<>();

        switch (listMode) {
            case Constants.SEARCH_SHOPPING: {
                arrayList.add(new Search_List_Item(R.drawable.ic_shop_amazon, R.string.amazon));
                arrayList.add(new Search_List_Item(R.drawable.ic_shop_alibaba, R.string.alibaba));
                arrayList.add(new Search_List_Item(R.drawable.ic_shop_daraz, R.string.daraz));
                arrayList.add(new Search_List_Item(R.drawable.ic_shop_olx, R.string.olx));
                arrayList.add(new Search_List_Item(R.drawable.ic_shop_ebay, R.string.ebay));
                arrayList.add(new Search_List_Item(R.drawable.ic_shop_aliexpress, R.string.aliexpress));
                break;
            }
            case Constants.SEARCH_WEB: {
                arrayList.add(new Search_List_Item(R.drawable.ic_se_google, R.string.google));
                arrayList.add(new Search_List_Item(R.drawable.ic_se_bing, R.string.bing));
                arrayList.add(new Search_List_Item(R.drawable.ic_se_duckduckgo, R.string.duckduckgo));
                arrayList.add(new Search_List_Item(R.drawable.ic_se_yahoo, R.string.yahoo));
                arrayList.add(new Search_List_Item(R.drawable.ic_se_wikipedia, R.string.wikipedia));
                break;
            }
            case Constants.SEARCH_COMMUNICATION: {
                arrayList.add(new Search_List_Item(R.drawable.ic_com_reddit, R.string.reddit));
                arrayList.add(new Search_List_Item(R.drawable.ic_com_quora, R.string.quora));
                arrayList.add(new Search_List_Item(R.drawable.ic_com_flipboard, R.string.flipboard));
                arrayList.add(new Search_List_Item(R.drawable.ic_com_msn, R.string.msn));
                break;
            }
            case Constants.SEARCH_SOCIAL: {
                arrayList.add(new Search_List_Item(R.drawable.ic_social_facebook, R.string.facebook));
                arrayList.add(new Search_List_Item(R.drawable.ic_social_instagram, R.string.app_instagram));
                arrayList.add(new Search_List_Item(R.drawable.ic_social_twitter, R.string.app_twitter));
                arrayList.add(new Search_List_Item(R.drawable.ic_social_youtube, R.string.youtube));
                arrayList.add(new Search_List_Item(R.drawable.ic_social_tiktok, R.string.tiktok));
                arrayList.add(new Search_List_Item(R.drawable.ic_social_pinterest, R.string.pinterest));
                break;
            }
            case Constants.SEARCH_OTHERS: {
                arrayList.add(new Search_List_Item(R.drawable.ic_others_medium, R.string.medium));
                arrayList.add(new Search_List_Item(R.drawable.ic_others_stack_overflow, R.string.stack_overflow));
                arrayList.add(new Search_List_Item(R.drawable.ic_others_imdb, R.string.imdb));
                arrayList.add(new Search_List_Item(R.drawable.ic_others_map, R.string.maps));
                break;
            }
        }

        return arrayList;
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