package com.niazitvpro.official.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.niazitvpro.official.R;
import com.niazitvpro.official.fragment.AllVideoPlayingFragment;
import com.niazitvpro.official.fragment.ChannelSubcategoryFragment;
import com.niazitvpro.official.fragment.FMRadioPlayFragment;
import com.niazitvpro.official.fragment.WebPageLoadFragment;
import com.niazitvpro.official.model.MenuItemChannel;
import com.niazitvpro.official.utils.AdmobIntrestrialAd;
import com.niazitvpro.official.utils.SharedPrefTVApp;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.List;

import static com.niazitvpro.official.utils.Constants.ADMOB_NATIVE;
import static com.niazitvpro.official.utils.Constants.ADS_TYPE;
import static com.niazitvpro.official.utils.Constants.FACEBOOK_NATIVE;

public class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> isFavorite = new ArrayList<>();
    public List<MenuItemChannel> menuItemChannelList = new ArrayList<>();
    Activity context;
    public static com.google.android.gms.ads.InterstitialAd mInterstitialAd;
    public static ProgressDialog progressDialog;
    private static AdmobIntrestrialAd myObj;
    public static AdRequest adRequest;
    private static InterstitialAd mInterstitialFbAd;
    private SharedPrefTVApp sharedPrefTVApp;
    public final int CONTENT_TYPE = 0;
    public final int AD_TYPE = 1;
    private NativeBannerAd nativeBannerAd;
    private UnifiedNativeAd nativeAd;

    public SubCategoryAdapter(Activity context, List<MenuItemChannel> menuItemChannelList) {

        if(context!=null){

            this.context = context;
            this.menuItemChannelList = menuItemChannelList;
            sharedPrefTVApp = new SharedPrefTVApp(context);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case CONTENT_TYPE:
                Log.e("TAG", "content type : ");
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shows_item_list, parent, false);
                return new SubCategoryAdapter.MyViewHolder(view);

            case AD_TYPE:
                Log.e("TAG", "ad type : ");
                View nativeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_native_ads_layout, parent, false);
                return new SubCategoryAdapter.NativeBanner(nativeView);

            default:
                return  null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType) {

            case CONTENT_TYPE:

                MyViewHolder myViewHolder = (MyViewHolder) holder;

                MenuItemChannel menuItemChannel = menuItemChannelList.get(position);

                myViewHolder.channelName.setText(menuItemChannel.channelName);

                Glide.with(context)
                        .asBitmap()
                        .load(menuItemChannel.channelImage)
                        .placeholder(R.color.black)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(myViewHolder.channelImage);

                String channelType = menuItemChannel.channelType;

                myViewHolder.llLayout.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SourceLockedOrientationActivity")
                    @Override
                    public void onClick(View v) {


                        if (channelType.equals("0")) {

                            Fragment fragment = new FMRadioPlayFragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("liveUrl",menuItemChannelList.get(position).directLiveUrl);
                            fragment.setArguments(bundle);
                            fragmentTransaction.add(R.id.frame_home, fragment, FMRadioPlayFragment.class.getName());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commitAllowingStateLoss();
                            AdmobIntrestrialAd.getInstance().loadIntertitialAds(context);


                        } else if (channelType.equals("1")) {

                            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            String liveUrl = menuItemChannelList.get(position).directLiveUrl;
                            Fragment fragment = new AllVideoPlayingFragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("live_tv_show_url", liveUrl);
                            bundle.putString("user_agent", menuItemChannelList.get(position).userAgent);
                            fragment.setArguments(bundle);
                            fragmentTransaction.add(R.id.frame_home, fragment,AllVideoPlayingFragment.class.getName());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commitAllowingStateLoss();
                            AdmobIntrestrialAd.getInstance().loadIntertitialAds(context);


                        } else if (channelType.equals("2") || channelType.equals("3")) {

                            Fragment fragment = new WebPageLoadFragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("liveUrl", menuItemChannelList.get(position).directLiveUrl);
                            bundle.putString("userAgent", menuItemChannelList.get(position).userAgent);
                            fragment.setArguments(bundle);
                            fragmentTransaction.add(R.id.frame_home, fragment, WebPageLoadFragment.class.getName());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commitAllowingStateLoss();
                            AdmobIntrestrialAd.getInstance().loadIntertitialAds(context);

                        } else {

                            Fragment fragment = new ChannelSubcategoryFragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("channelID",menuItemChannelList.get(position).channelID);
                            bundle.putString("channelName",menuItemChannelList.get(position).channelName);
                            fragment.setArguments(bundle);
                            fragmentTransaction.add(R.id.frame_home, fragment, ChannelSubcategoryFragment.class.getName());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commitAllowingStateLoss();
                            AdmobIntrestrialAd.getInstance().loadIntertitialAds(context);
                        }
                    }

                });
                break;

            case AD_TYPE:

                break;

        }


    }

    @Override
    public int getItemViewType(int position) {

        if (menuItemChannelList.get(position) == null) {
            return AD_TYPE;
        } else {
            return CONTENT_TYPE;
        }

    }

    public void swap(List<MenuItemChannel> menuItemChannels) {
        List<MenuItemChannel> newList = menuItemChannels;
        List<MenuItemChannel> oldList = this.menuItemChannelList;

        DiffUtil.Callback diffCallback = new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition) == newList.get(oldItemPosition);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition) == newList.get(oldItemPosition);
            }
        };
        DiffUtil.DiffResult  diffResult = DiffUtil.calculateDiff(diffCallback);

        this.menuItemChannelList.clear();
        this.menuItemChannelList.addAll(menuItemChannels);
        diffResult.dispatchUpdatesTo(this);
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    MenuItemChannel getItem(int position) {
        return menuItemChannelList.get(position);
    }

    private void remove(MenuItemChannel postItems) {
        int position = menuItemChannelList.indexOf(postItems);
        if (position > -1) {
            menuItemChannelList.remove(position);
            notifyItemRemoved(position);
        }
    }


    @Override
    public int getItemCount() {
        return menuItemChannelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView channelName;
        ImageView channelImage;
        LinearLayout llLayout;

        public MyViewHolder(View view) {
            super(view);

            channelImage = view.findViewById(R.id.img_tv_channel);
            channelName = view.findViewById(R.id.tv_channel_name);
            llLayout = view.findViewById(R.id.ll_indian_channel_layout);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class NativeBanner extends RecyclerView.ViewHolder {

        LinearLayout nativeAdmobAdLayout;
        NativeAdLayout nativeFBAdLayout;
        View view;

        @SuppressLint("CutPasteId")
        public NativeBanner(View itemView) {
            super(itemView);
            nativeAdmobAdLayout = itemView.findViewById(R.id.ll_native);
            nativeFBAdLayout = itemView.findViewById(R.id.native_banner_ad_container);
//            view = itemView.findViewById(R.id.view);
            loadNetiveAds(context,nativeAdmobAdLayout,nativeFBAdLayout);

        }
    }

    private  void loadAdmobNativeBanner(LinearLayout nativeBanner){

        String admob_native_id = sharedPrefTVApp.getString(ADMOB_NATIVE);

        final AdLoader adLoader = new AdLoader.Builder(context,admob_native_id)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = unifiedNativeAd;

                        UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater()
                                .inflate(R.layout.layout_custom_admob_native_ads, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        nativeBanner.setVisibility(View.VISIBLE);
                        nativeBanner.removeAllViews();
                        nativeBanner.addView(adView);

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);


    }

    private  void loadFacebookNative(NativeAdLayout nativeBanner){

        String facebook_native_id = sharedPrefTVApp.getString(FACEBOOK_NATIVE);

        nativeBannerAd = new NativeBannerAd(context, facebook_native_id);
        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                nativeBanner.setVisibility(View.VISIBLE);
                View adView = NativeBannerAdView.render(context, nativeBannerAd, NativeBannerAdView.Type.HEIGHT_120);
                nativeBanner.addView(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        nativeBannerAd.loadAd();
    }


    public void loadNetiveAds(Activity activity,LinearLayout llAdmob,NativeAdLayout llFacebook){

        sharedPrefTVApp = new SharedPrefTVApp(activity);

        String adType =sharedPrefTVApp.getString(ADS_TYPE);

        if(adType.equalsIgnoreCase("Facebook")){

            loadFacebookNative(llFacebook);

        }else {

            loadAdmobNativeBanner(llAdmob);
        }
    }


}