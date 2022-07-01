package com.voicesms.voice.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.voicesms.voice.Adapters.OpenMessageAdapter;
import com.voicesms.voice.Adapters.myDatabaseAdapter;
import com.voicesms.voice.Dialogs.GeneralDialog_VoiceSMS;
import com.voicesms.voice.Interfaces.GeneralDialogInterface_voiceSMS;
import com.voicesms.voice.Interfaces.OpenMessageInterface;
import com.voicesms.voice.Models.Message_Item;
import com.voicesms.voice.R;
import com.voicesms.voice.Utils.Constants;
import com.voicesms.voice.databinding.ActivityOpenSavedMessagesBinding;

import java.util.ArrayList;

public class OpenMessagesActivity_VS extends AppCompatActivity {
    ActivityOpenSavedMessagesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenSavedMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        loadBannerAdd();
//        InterstitialAddsVoiceSMS.showAdd();

        setSupportActionBar(binding.toolbarSavedMsgActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initRecylerView();
    }


    private void initRecylerView() {
        myDatabaseAdapter myDatabaseAdapter = new myDatabaseAdapter(OpenMessagesActivity_VS.this);
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
                GeneralDialog_VoiceSMS.CreateGeneralDialog(OpenMessagesActivity_VS.this
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

//    public void loadBannerAdd() {
//        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);
//
//        AdListener adListener = new AdListener() {
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.d("TAG", "onError: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                Log.d("TAG", "Ad loaded");
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
//        relativeLayout.addView(adView);
//    }
}