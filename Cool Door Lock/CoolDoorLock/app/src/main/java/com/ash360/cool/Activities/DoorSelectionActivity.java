package com.ash360.cool.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ash360.cool.Adapters.Adapter_Door_Selection;
import com.ash360.cool.Interfaces.Door_Grid_interface;
import com.ash360.cool.Models.DoorModel;
import com.ash360.cool.R;
import com.ash360.cool.Utils.Shared_Pref_DoorLock;
import com.ash360.cool.databinding.ActivityDoorSelectionBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.util.ArrayList;

import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_1;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_2;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_3;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_4;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_5;
import static com.ash360.cool.Utils.Constants_DoorLock.DOOR_6;
import static com.ash360.cool.Utils.Constants_DoorLock.SELECTED_DOOR;
import static com.ash360.cool.Utils.Constants_DoorLock.SELECTED_DOOR_DEFAULT_VALUE;

public class DoorSelectionActivity extends AppCompatActivity {
    ActivityDoorSelectionBinding binding;
    Context context;
    Shared_Pref_DoorLock shared_pref_doorLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoorSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadBannerAdd();

        context = DoorSelectionActivity.this;
        shared_pref_doorLock = new Shared_Pref_DoorLock(context);
        setRecyclerView();

        binding.btnConfirm.setOnClickListener(v -> {
            finish();
        });
    }

    private void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewDoorSelection.setLayoutManager(layoutManager);
        ArrayList<DoorModel> mList = getList();
        Adapter_Door_Selection adapter_door_selection = new Adapter_Door_Selection(mList, context
                , new Door_Grid_interface() {
            @Override
            public void OnItemSelection(DoorModel item) {
                shared_pref_doorLock.SetInt(SELECTED_DOOR, item.getIcon());
            }
        });
        binding.recyclerViewDoorSelection.setAdapter(adapter_door_selection);
    }

    private ArrayList<DoorModel> getList() {
        ArrayList<DoorModel> mList = new ArrayList<>();
        int selected_door = shared_pref_doorLock.GetInt(SELECTED_DOOR, SELECTED_DOOR_DEFAULT_VALUE);
        mList.add(new DoorModel(DOOR_1
                , (selected_door == DOOR_1) ? 0 : 8));
        mList.add(new DoorModel(DOOR_2
                , (selected_door == DOOR_2) ? 0 : 8));
        mList.add(new DoorModel(DOOR_3
                , (selected_door == DOOR_3) ? 0 : 8));
        mList.add(new DoorModel(DOOR_4
                , (selected_door == DOOR_4) ? 0 : 8));
        mList.add(new DoorModel(DOOR_5
                , (selected_door == DOOR_5) ? 0 : 8));
        mList.add(new DoorModel(DOOR_6
                , (selected_door == DOOR_6) ? 0 : 8));

        return mList;
    }

    @Override
    public void onBackPressed() {

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