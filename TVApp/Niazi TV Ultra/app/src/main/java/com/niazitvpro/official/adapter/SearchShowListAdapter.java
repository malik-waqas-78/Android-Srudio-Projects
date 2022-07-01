package com.niazitvpro.official.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.niazitvpro.official.R;
import com.niazitvpro.official.fragment.AllVideoPlayingFragment;
import com.niazitvpro.official.fragment.ChannelSubcategoryFragment;
import com.niazitvpro.official.fragment.FMRadioPlayFragment;
import com.niazitvpro.official.fragment.OpenPushNotificationFragment;
import com.niazitvpro.official.fragment.WebPageLoadFragment;
import com.niazitvpro.official.model.SearchItemsModel;
import com.niazitvpro.official.utils.AdmobIntrestrialAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.niazitvpro.official.activity.MainActivity.et_searchShow;
import static com.niazitvpro.official.activity.MainActivity.hideKeyboard;
import static com.niazitvpro.official.fragment.FMRadioPlayFragment.toggleplayButton;
import static com.niazitvpro.official.service.RadioService.exoPlayer;

public class SearchShowListAdapter  extends ArrayAdapter<SearchItemsModel> {
    private  Activity mContext;
    private List<SearchItemsModel> searchItemsList = new ArrayList<>();
    private final List<SearchItemsModel> searchItemsListAll;
    private final int mLayoutResourceId;

    public SearchShowListAdapter(Activity context, int resource, List<SearchItemsModel> searchItemsList) {
        super(context, resource, searchItemsList);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.searchItemsList = searchItemsList;
        this.searchItemsListAll = new ArrayList<SearchItemsModel>();
        this.searchItemsListAll.addAll(searchItemsList);
    }

    public int getCount() {
        return searchItemsList.size();
    }

    public SearchItemsModel getItem(int position) {
        return searchItemsList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_nav_item_list, parent, false);

        ImageView navItemImage = itemView.findViewById(R.id.nav_item_image);
        TextView navItemName = itemView.findViewById(R.id.tv_nav_item_name);
        LinearLayout linearLayout =itemView.findViewById(R.id.ll_nav_layout);

        navItemName.setText(searchItemsList.get(position).searchItemName);

        Glide.with(mContext)
                .asBitmap()
                .load(searchItemsList.get(position).searchItemImage)
                .placeholder(R.color.black)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(navItemImage);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {

                hideKeyboard(mContext);

                et_searchShow.dismissDropDown();
                String channelType = searchItemsList.get(position).searchLinkType;

                Fragment f = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.frame_home);
                if(f instanceof AllVideoPlayingFragment || f instanceof ChannelSubcategoryFragment || f instanceof WebPageLoadFragment
                        ||f instanceof OpenPushNotificationFragment) {

                    ((AppCompatActivity) mContext).getSupportFragmentManager().popBackStackImmediate();

                }

                if(exoPlayer!=null){

                    exoPlayer.setPlayWhenReady(false);
                    toggleplayButton.setChecked(false);
                }

                if (channelType.equals("1")) {
                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    Fragment fragment = new AllVideoPlayingFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("live_tv_show_url", searchItemsList.get(position).searchItemUrl);
                    bundle.putString("user_agent", searchItemsList.get(position).userAgent);
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.frame_home, fragment,AllVideoPlayingFragment.class.getName());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();

                }else if (channelType.equals("0")) {

                    Fragment fragment = new FMRadioPlayFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("liveUrl",searchItemsList.get(position).searchItemUrl);
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.frame_home, fragment, FMRadioPlayFragment.class.getName());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();

                } else if (channelType.equals("2") || channelType.equals("3")) {

                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    Fragment fragment = new WebPageLoadFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("liveUrl",searchItemsList.get(position).searchItemUrl);
                    bundle.putString("userAgent",searchItemsList.get(position).userAgent);
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.frame_home, fragment, WebPageLoadFragment.class.getName());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();

                }else {

                    Fragment fragment = new ChannelSubcategoryFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("channelID",searchItemsList.get(position).searchID);
                    bundle.putString("channelName",searchItemsList.get(position).searchItemName);
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.frame_home, fragment, ChannelSubcategoryFragment.class.getName());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        });

        return itemView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchItemsList.clear();
        if (charText.length() == 0) {
            searchItemsList.addAll(searchItemsListAll);
        } else {
            for (SearchItemsModel wp : searchItemsListAll) {
                if (wp.searchItemName.toLowerCase(Locale.getDefault()).contains(charText)) {

                    searchItemsList.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }


}

