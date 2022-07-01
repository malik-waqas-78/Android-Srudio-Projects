package com.speak.to.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speak.to.Adapters.OpenMessageAdapter;
import com.speak.to.Adapters.myDatabaseAdapter;
import com.speak.to.Dialogs.GeneralDialog_VoiceSMS;
import com.speak.to.Interfaces.GeneralDialogInterface_voiceSMS;
import com.speak.to.Interfaces.OpenMessageInterface;
import com.speak.to.Models.Message_Item;
import com.speak.to.R;
import com.speak.to.Utils.Constants;
import com.speak.to.databinding.ActivityOpenSavedMessagesBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.util.ArrayList;

public class OpenMessagesActivity extends AppCompatActivity {
    ActivityOpenSavedMessagesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenSavedMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadBannerAdd();
//        InterstitialAddsVoiceSMS.showAdd();

        setSupportActionBar(binding.toolbarSavedMsgActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initRecylerView();
    }


    private void initRecylerView() {
        myDatabaseAdapter myDatabaseAdapter = new myDatabaseAdapter(OpenMessagesActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.savedMsgsRecyclerReview.setLayoutManager(layoutManager);
        ArrayList<Message_Item> data = myDatabaseAdapter.getData();
        if (data.size() > 0) {
            binding.savedMsgsRecyclerReview.setVisibility(View.VISIBLE);
            binding.textEmptyListviewOpenMessages.setVisibility(View.GONE);
        }
        OpenMessageAdapter adapter = new OpenMessageAdapter(data, this, new OpenMessageInterface() {
            @Override
            public void openMessage(String msg) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constants.KEY_OPEN_MESSAGE, msg);
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void deleteMessage(String msg, OpenMessageAdapter.notifyUpdate notifyUpdate, int itemCount) {
                GeneralDialog_VoiceSMS.CreateGeneralDialog(OpenMessagesActivity.this
                        , getResources().getString(R.string.deleteMessage)
                        , getResources().getString(R.string.deleteMessageDesc)
                        , getResources().getString(R.string.yes)
                        , getResources().getString(R.string.no)
                        , new GeneralDialogInterface_voiceSMS() {
                            @Override
                            public void Positive(Dialog dialog) {
                                myDatabaseAdapter.delete(msg);
                                notifyUpdate.updateView();
                                if (itemCount == 0) {
                                    binding.savedMsgsRecyclerReview.setVisibility(View.VISIBLE);
                                    binding.textEmptyListviewOpenMessages.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void Negative(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }
                );
            }
        });
        binding.savedMsgsRecyclerReview.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
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