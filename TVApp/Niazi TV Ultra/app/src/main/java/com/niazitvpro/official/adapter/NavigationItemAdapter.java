package com.niazitvpro.official.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.niazitvpro.official.R;
import com.niazitvpro.official.fragment.AllVideoPlayingFragment;
import com.niazitvpro.official.fragment.ChannelSubcategoryFragment;
import com.niazitvpro.official.fragment.FMRadioPlayFragment;
import com.niazitvpro.official.fragment.MenuCategoryItemsFragment;
import com.niazitvpro.official.fragment.OpenPushNotificationFragment;
import com.niazitvpro.official.fragment.SubCategoryFragment;
import com.niazitvpro.official.fragment.WebPageLoadFragment;
import com.niazitvpro.official.model.NavigationItemModel;
import com.niazitvpro.official.utils.AdmobIntrestrialAd;

import java.util.ArrayList;
import java.util.List;

import static com.niazitvpro.official.activity.MainActivity.drawerLayout;
import static com.niazitvpro.official.fragment.FMRadioPlayFragment.toggleplayButton;
import static com.niazitvpro.official.service.RadioService.exoPlayer;

public class NavigationItemAdapter extends RecyclerView.Adapter<NavigationItemAdapter.MyViewHolder> {

    public List<NavigationItemModel> navigationItemList = new ArrayList<>();
    Activity context;


    public NavigationItemAdapter(Activity context, List<NavigationItemModel> navigationItemList) {

        if(context!=null){

            this.context = context;
            this.navigationItemList = navigationItemList;

        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_nav_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.navItemName.setText(navigationItemList.get(position).navItemName);

        Glide.with(context)
                .asBitmap()
                .load(navigationItemList.get(position).navItemImage)
                .placeholder(R.color.black)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.navItemImage);

        holder.llLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment f = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.frame_home);

                if(f instanceof AllVideoPlayingFragment || f instanceof ChannelSubcategoryFragment || f instanceof WebPageLoadFragment
                ||f instanceof OpenPushNotificationFragment) {

                    ((AppCompatActivity) context).getSupportFragmentManager().popBackStackImmediate();

                }
                if(exoPlayer!=null){

                    exoPlayer.setPlayWhenReady(false);
                    toggleplayButton.setChecked(false);

                }

                String categoryID = navigationItemList.get(position).navItemId;
                AdmobIntrestrialAd.getInstance().loadIntertitialAds(context);
                navigatonItemOnClick(position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return navigationItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView navItemName;
        ImageView navItemImage;
        LinearLayout llLayout;

        public MyViewHolder(View view) {
            super(view);

            navItemImage = view.findViewById(R.id.nav_item_image);
            navItemName = view.findViewById(R.id.tv_nav_item_name);
            llLayout = view.findViewById(R.id.ll_nav_layout);

        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void navigatonItemOnClick(int position){

        String menuType = navigationItemList.get(position).menuType;
        String categoryID = navigationItemList.get(position).navItemId;
        String channelType = navigationItemList.get(position).channelType;
        String directLiveUrl = navigationItemList.get(position).directUrl;
        String userAgent = navigationItemList.get(position).userAgent;
        String channelName = navigationItemList.get(position).navItemName;
        if(menuType.equals("category")){

            if(channelName.equals("Home")){
                ((AppCompatActivity)context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Fragment fragment = new MenuCategoryItemsFragment();
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("menu_id",categoryID);
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frame_home, fragment, MenuCategoryItemsFragment.class.getName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();

        }else if(menuType.equals("subcategory")){

            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Fragment fragment = new SubCategoryFragment();
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("category_id",categoryID);
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frame_home, fragment, SubCategoryFragment.class.getName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();


        }else {

            if (channelType.equals("0")) {

                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Fragment fragment = new FMRadioPlayFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("liveUrl",directLiveUrl);
                fragment.setArguments(bundle);
                fragmentTransaction.add(R.id.frame_home, fragment, FMRadioPlayFragment.class.getName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();


            } else if (channelType.equals("1")) {

                Fragment fragment = new AllVideoPlayingFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("live_tv_show_url", directLiveUrl);
                bundle.putString("user_agent", userAgent);
                fragment.setArguments(bundle);
                fragmentTransaction.add(R.id.frame_home, fragment,AllVideoPlayingFragment.class.getName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();


            } else if (channelType.equals("2") || channelType.equals("3")) {

                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Fragment fragment = new WebPageLoadFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("liveUrl", directLiveUrl);
                bundle.putString("userAgent", userAgent);
                fragment.setArguments(bundle);
                fragmentTransaction.add(R.id.frame_home, fragment, WebPageLoadFragment.class.getName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();

            } else {

                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Fragment fragment = new ChannelSubcategoryFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("channelID",categoryID);
                bundle.putString("channelName",channelName);
                fragment.setArguments(bundle);
                fragmentTransaction.add(R.id.frame_home, fragment, ChannelSubcategoryFragment.class.getName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();

            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);


    }
}
