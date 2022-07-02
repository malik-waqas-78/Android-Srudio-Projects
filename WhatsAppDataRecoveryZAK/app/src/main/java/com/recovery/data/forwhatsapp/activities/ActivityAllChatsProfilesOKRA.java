package com.recovery.data.forwhatsapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.google.android.gms.ads.LoadAdError;
import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.chatspkg.AllChatsProfilesModelClassOKRA;
import com.recovery.data.forwhatsapp.chatspkg.CardViewChatsInterfaceOKRA;
import com.recovery.data.forwhatsapp.chatspkg.MessagesSingleModelClassOKRA;
import com.recovery.data.forwhatsapp.chatspkg.AdapterAllChatsProfileOKRA;
import com.recovery.data.forwhatsapp.chatspkg.ProfileModalClassOKRA;

import java.lang.reflect.Field;
import java.util.ArrayList;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class ActivityAllChatsProfilesOKRA extends AppCompatActivity implements CardViewChatsInterfaceOKRA, LongClickInterfaceOKRA {

    RecyclerView recyclerView;
    AdapterAllChatsProfileOKRA cardView_profile_adapter;
    ArrayList<AllChatsProfilesModelClassOKRA> cardView_profiles_modelClasses = new ArrayList<>();
    ArrayList<ProfileModalClassOKRA> profileModalClassOKRAS = new ArrayList<>();
    Realm realm_chat;
    Realm realm_msg;
    ImageView delete, share;
    ConstraintLayout constraintLayout;
    SearchView searchView;
    LinearLayout linearSelected;
    ImageView selectDot;
    ArrayList<String> selectall=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_chats_profiles_okra);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        if(AATAdsManager.isAppInstalledFromPlay(this)){
////        loadFbBannerAdd();
////        loadAdmobBanner();
//        }
       /* getSupportActionBar().setTitle("Chats");*/
        initRealm();
        initComponents();
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        int id2 = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_button", null, null);
        int id3 = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);
        ImageView searchClose = searchView.findViewById(id3);
        searchClose.setColorFilter(Color.GRAY);
        ImageView searchIcon = searchView.findViewById(id2);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_search_black));
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(textView, 0); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {}
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cardView_profile_adapter.getFilter().filter(newText);
                return true;
            }
        });
        linearSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectall.size()==0) {
                    selectDot.setBackgroundResource(R.drawable.ic_chek_icon);
                    for (int i = 0; i < profileModalClassOKRAS.size(); i++) {
                        selectall.add(profileModalClassOKRAS.get(i).getProfile_name());
                    }
                    cardView_profile_adapter.setSelectedRows(selectall);
                    cardView_profile_adapter.notifyDataSetChanged();
                }else{
                    selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    cardView_profile_adapter.setSelectedRows(new ArrayList<String>());
                    selectall.clear();
                    cardView_profile_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initComponents() {
        recyclerView = findViewById(R.id.chats_recyleview);
        delete = findViewById(R.id.delete);
        share = findViewById(R.id.share);
        constraintLayout=findViewById(R.id.nodatacons);
        searchView=findViewById(R.id.searchView1);
        linearSelected=findViewById(R.id.linearSelectAll1);
        selectDot=findViewById(R.id.imageselectdot1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //init adapter
        cardView_profile_adapter = new AdapterAllChatsProfileOKRA(cardView_profiles_modelClasses, this, profileModalClassOKRAS);
        //setAadapteData
        setAdapterData();
        cardView_profile_adapter.setItemClickListeners(this);
        cardView_profile_adapter.setLongClickListeners(this);
        recyclerView.setAdapter(cardView_profile_adapter);
        delete.setOnClickListener(new DeleteClickListener());
        share.setOnClickListener(new ShareSelectedItems());

        delete.setVisibility(View.GONE);
        share.setVisibility(View.GONE);
    }

    @Override
    public void onLongClicked(boolean flag) {
        if (flag) {
            delete.setVisibility(View.VISIBLE);
            share.setVisibility(View.GONE);
            linearSelected.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            linearSelected.setVisibility(View.GONE);
            selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
        }
    }

    class DeleteClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if(cardView_profile_adapter.getSelectedRows().size()>0) {
                deleteWarningDialog();
            }else {
                Toast.makeText(ActivityAllChatsProfilesOKRA.this, "Kindly Select atleast one Profile", Toast.LENGTH_SHORT).show();
            }
            /*if (recyclerView.getVisibility() == View.VISIBLE && cardView_profile_adapter != null) {
                ArrayList<String> selectedChats = cardView_profile_adapter.getSelectedRows();
                if (selectedChats.size() != 0) {
                    RealmResults realmResults = null;
                    for (String id : selectedChats) {
                        realmResults = realm_msg.where(CardView_Messages_ModelClass.class).equalTo("name", id).findAll();
                        realm_msg.beginTransaction();
                        realmResults.deleteAllFromRealm();
                        realm_msg.commitTransaction();
                        realmResults = realm_chat.where(CardView_profiles_ModelClass.class).equalTo("profile_name", id).findAll();
                        realm_chat.beginTransaction();
                        realmResults.deleteAllFromRealm();
                        realm_chat.commitTransaction();
                    }
                }
                cardView_profile_adapter.setSelectedRows(new ArrayList<>());
                cardView_profile_adapter.setSelectedRowsMsgs(new ArrayList<>());
                setAdapterData();
            }*/
        }
    }

    private void deleteWarningDialog() {
        Dialog dialog = new Dialog(ActivityAllChatsProfilesOKRA.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_warning_layout_okra);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(getDrawable(android.R.color.transparent));

        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button no = dialog.findViewById(R.id.btn_no);


        no.setOnClickListener(view -> {
            cardView_profile_adapter.setSelectedRows(new ArrayList<>());
            cardView_profile_adapter.setSelectedRowsMsgs(new ArrayList<>());
            dialog.dismiss();
        });
        btn_yes.setOnClickListener(view -> {
            if (recyclerView.getVisibility() == View.VISIBLE && cardView_profile_adapter != null) {
                ArrayList<String> selectedChats = cardView_profile_adapter.getSelectedRows();
                if (selectedChats.size() != 0) {
                    RealmResults realmResults = null;
                    for (String id : selectedChats) {
                        realmResults = realm_msg.where(MessagesSingleModelClassOKRA.class).equalTo("name", id).findAll();
                        realm_msg.beginTransaction();
                        realmResults.deleteAllFromRealm();
                        realm_msg.commitTransaction();
                        realmResults = realm_chat.where(AllChatsProfilesModelClassOKRA.class).equalTo("profile_name", id).findAll();
                        realm_chat.beginTransaction();
                        realmResults.deleteAllFromRealm();
                        realm_chat.commitTransaction();
                    }
                }
                cardView_profile_adapter.setSelectedRows(new ArrayList<>());
                cardView_profile_adapter.setSelectedRowsMsgs(new ArrayList<>());
                setAdapterData();
            }
            dialog.dismiss();
        });
        dialog.show();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.example_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        int id = searchView.getContext()
//                .getResources()
//                .getIdentifier("android:id/search_src_text", null, null);
//        int id2 = searchView.getContext()
//                .getResources()
//                .getIdentifier("android:id/search_button", null, null);
//        int id3 = searchView.getContext()
//                .getResources()
//                .getIdentifier("android:id/search_close_btn", null, null);
//        TextView textView = (TextView) searchView.findViewById(id);
//        textView.setTextColor(Color.BLACK);
//        ImageView searchClose = searchView.findViewById(id3);
//        searchClose.setColorFilter(Color.BLACK);
//
//        try {
//            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
//            mCursorDrawableRes.setAccessible(true);
//            mCursorDrawableRes.set(textView, 0); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
//        } catch (Exception e) {}
//        ImageView searchIcon = searchView.findViewById(id2);
//        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_search_black));
//        searchView.setBackgroundResource(R.drawable.searchview_background);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                cardView_profile_adapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//        return true;
//    }
    private void setAdapterData() {
        profileModalClassOKRAS.clear();
        cardView_profiles_modelClasses.clear();
        cardView_profile_adapter.notifyDataSetChanged();
        RealmResults realmResults = realm_chat.where(AllChatsProfilesModelClassOKRA.class).sort("timeinmilies", Sort.DESCENDING).findAll();
        cardView_profiles_modelClasses.addAll(realmResults);
        for(int i=0;i<cardView_profiles_modelClasses.size();i++){
            profileModalClassOKRAS.add(new ProfileModalClassOKRA(cardView_profiles_modelClasses.get(i).getProfile_name(),cardView_profiles_modelClasses.get(i).getLast_msg(),cardView_profiles_modelClasses.get(i).getTime(),cardView_profiles_modelClasses.get(i).getTimeinmilies(),cardView_profiles_modelClasses.get(i).getBytearray(),cardView_profiles_modelClasses.get(i).getUrl()));
        }
        if(cardView_profiles_modelClasses.size()==0){
            delete.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
            selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
            linearSelected.setVisibility(View.GONE);
        }
        cardView_profile_adapter.notifyDataSetChanged();
        realmResults.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults>() {
            @Override
            public void onChange(RealmResults realmResults, OrderedCollectionChangeSet changeSet) {
                cardView_profiles_modelClasses.clear();
                cardView_profiles_modelClasses.addAll(realmResults);

                cardView_profile_adapter.notifyDataSetChanged();
            }
        });
    }

    private void initRealm() {
        RealmConfiguration realm_chats = new RealmConfiguration.Builder()
                .name("realmchat.realm")
                .build();
        realm_chat = Realm.getInstance(realm_chats);
        RealmConfiguration realm_msgs = new RealmConfiguration.Builder()
                .name("realmmsg.realm")
                .build();
        realm_msg = Realm.getInstance(realm_msgs);
    }

    @Override
    public void onShareClickListener(String text, long position) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some Text msgs.");
            intent.setType("text/*"); /* This example is sharing jpeg images. */
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(intent);

    }

    class ShareSelectedItems implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            ArrayList<String> paths = cardView_profile_adapter.getSelectedRowsMsgs();
            String msg = "";
            for (String x : paths) {
                msg += "\n" + x;
            }
            if (paths != null && paths.size() != 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some Text msgs.");
                intent.setType("text/*"); /* This example is sharing jpeg images. */
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(intent);
            }
            cardView_profile_adapter.setSelectedRows(new ArrayList<>());
            cardView_profile_adapter.setSelectedRowsMsgs(new ArrayList<>());
            setAdapterData();
        }
    }

    @Override
    public void onProfileClickListener(View view, long position) {
        Intent intent = new Intent(this, ActivityMessagesSingleOKRA.class);
        //send profileinfo
        intent.putExtra("key", cardView_profiles_modelClasses.get((int) position).getProfile_name());
        startActivity(intent);
    }

    @Override
    public void onDeleteClickListener() {
        deleteWarningDialog();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.generic_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                if (recyclerView.getVisibility() == View.VISIBLE && cardView_profile_adapter != null) {
                    ArrayList<String> selectedChats = cardView_profile_adapter.getSelectedRows();

                    if (selectedChats.size() != 0) {
                        RealmResults realmResults = null;
                        for (String id : selectedChats) {
                            realmResults = realm_msg.where(MessagesSingleModelClassOKRA.class).equalTo("name", id).findAll();
                            realm_msg.beginTransaction();
                            realmResults.deleteAllFromRealm();
                            realm_msg.commitTransaction();
                            realmResults = realm_chat.where(AllChatsProfilesModelClassOKRA.class).equalTo("profile_name", id).findAll();
                            realm_chat.beginTransaction();
                            realmResults.deleteAllFromRealm();
                            realm_chat.commitTransaction();
                        }
                    }
                    cardView_profile_adapter.setSelectedRows(new ArrayList<>());
                    cardView_profile_adapter.setSelectedRowsMsgs(new ArrayList<>());
                    setAdapterData();
                }
                break;

                case R.id.menu_share:
                    ArrayList<String> paths = cardView_profile_adapter.getSelectedRowsMsgs();
                    String msg = "";
                    for (String x : paths) {
                        msg += "\n" + x;
                    }
                    if (paths != null && paths.size() != 0) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some Text msgs.");
                        intent.setType("text/*"); /* This example is sharing jpeg images. */
                        intent.putExtra(Intent.EXTRA_TEXT, msg);
                        startActivity(intent);
                    }
                    cardView_profile_adapter.setSelectedRows(new ArrayList<>());
                    cardView_profile_adapter.setSelectedRowsMsgs(new ArrayList<>());
                    setAdapterData();
                break;
        }
        return true;
    }

//    public void loadFbBannerAdd() {
//        AdView adView = new AdView(this, getResources().getString(R.string.fbbannerad), AdSize.BANNER_HEIGHT_50);
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
//
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
//
//        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
//        relativeLayout.addView(adView);
//
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
//    public void loadAdmobBanner() {
//
//        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(ActivityAllChatsProfilesAAT.this);
//        com.google.android.gms.ads.AdSize adSize = AATAdmobFullAdManager.getAdSize(ActivityAllChatsProfilesAAT.this);
//        mAdView.setAdSize(adSize);
//
//        mAdView.setAdUnitId(getResources().getString(R.string.admob_banner));
//
//        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
//
//        final RelativeLayout adViewLayout = (RelativeLayout) findViewById(R.id.bottom_banner);
//        adViewLayout.addView(mAdView);
//
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                adViewLayout.setVisibility(View.GONE);
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
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                adViewLayout.setVisibility(View.VISIBLE);
//            }
//        });
//
//    }

}