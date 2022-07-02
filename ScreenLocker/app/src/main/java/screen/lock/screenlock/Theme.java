package screen.lock.screenlock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;

public class Theme extends AppCompatActivity {
    RelativeLayout btncnfrm;
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    ArrayList<Integer> imagespaths=new ArrayList<Integer>();
    ImageView imageHolder;
    ImageButton cancelbtn;
    int position;
    ImageButton back;
   // com.google.android.gms.ads.AdView adView;
    int selected;
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
//        loadAd();
//        loadadtop();
        SharedPreferences sp=getSharedPreferences("MySharedPref",MODE_PRIVATE);
        selected=sp.getInt("background1",R.drawable.themeone);
        btncnfrm= (RelativeLayout) findViewById(R.id.cofirm);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        relativeLayout=(RelativeLayout)findViewById(R.id.relative);
        recyclerView=(RecyclerView)findViewById(R.id.imagesRecycler);
        getSupportActionBar().hide();
        back=(ImageButton)findViewById(R.id.back);
        imageHolder=(ImageView)findViewById(R.id.imageHolder);
        cancelbtn=(ImageButton)findViewById(R.id.imagecancel);

        imagespaths.add(R.drawable.themeone);
        imagespaths.add(R.drawable.themetwo);
        imagespaths.add(R.drawable.themethree);
        imagespaths.add(R.drawable.themefour);
        imagespaths.add(R.drawable.themefive);
        imagespaths.add(R.drawable.themesix);
        imagespaths.add(R.drawable.themeseven);
        imagespaths.add(R.drawable.themeeight);
        imagespaths.add(R.drawable.themenine);
        imagespaths.add(R.drawable.themeten);
        imagespaths.add(R.drawable.themeeleven);
        imagespaths.add(R.drawable.themetwelve);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        ArrayAdapterList arrayAdapterList=new ArrayAdapterList(this,imagespaths,selected);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(arrayAdapterList);


        arrayAdapterList.setOnItemClickListener(new ArrayAdapterList.OnItemClickListner() {
            @Override
            public void onItemClick(View v, int pos) {
                position=pos;
                Glide.with(Theme.this)
                        .load(imagespaths.get(position))
                        .centerCrop()
                        .into(imageHolder);
                relativeLayout.setVisibility(View.VISIBLE);

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
            }
        });
        btncnfrm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences.Editor myEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                myEdit.putInt("background1",imagespaths.get(position));
                myEdit.apply();
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
//    public void loadAd(){
//        AdView adView=new AdView(Theme.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
//        AdListener adListener=new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//        LinearLayout adContainer=(LinearLayout)findViewById(R.id.bannerAdContainer);
//        adContainer.addView(adView);
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
//    public void loadadtop(){
//        adView = new com.google.android.gms.ads.AdView(this);
//        adView.setAdUnitId(getString(R.string.admob_banner_ad));
//
//
//        com.google.android.gms.ads.AdSize adSize = getAdSize();
//        // Step 4 - Set the adaptive ad size on the ad view.
//        adView.setAdSize(adSize);
//        // Step 5 - Start loading the ad in the background.
//
//        AdRequest adRequest =
//                new AdRequest.Builder()
//                        .build();
//        adView.loadAd(adRequest);
//
//        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//            }
//
//            @Override
//            public void onAdClicked() {
//                super.onAdClicked();
//            }
//
//            @Override
//            public void onAdImpression() {
//                super.onAdImpression();
//            }
//        });
//
//        LinearLayout adContainer=(LinearLayout)findViewById(R.id.lineartopbanner);
//        adContainer.addView(adView);
//
//    }
//    public com.google.android.gms.ads.AdSize getAdSize() {
//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float widthPixels = outMetrics.widthPixels;
//        float density = outMetrics.density;
//
//        int adWidth = (int) (widthPixels / density);
//
//        // Step 3 - Get adaptive ad size and return for setting on the ad view.
//        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
//    }
}
