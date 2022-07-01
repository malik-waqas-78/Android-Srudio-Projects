package com.niazitvpro.official.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.niazitvpro.official.R;
import com.niazitvpro.official.adapter.NavigationItemAdapter;
import com.niazitvpro.official.adapter.SearchShowListAdapter;
import com.niazitvpro.official.app.MyApplication;
import com.niazitvpro.official.crypto.Main;
import com.niazitvpro.official.fragment.AllVideoPlayingFragment;
import com.niazitvpro.official.fragment.ChannelSubcategoryFragment;
import com.niazitvpro.official.fragment.FMRadioPlayFragment;
import com.niazitvpro.official.fragment.MenuCategoryItemsFragment;
import com.niazitvpro.official.fragment.NotificationFragment;
import com.niazitvpro.official.fragment.OpenPushNotificationFragment;
import com.niazitvpro.official.fragment.SubCategoryFragment;
import com.niazitvpro.official.fragment.WebPageLoadFragment;
import com.niazitvpro.official.model.NavigationItemModel;
import com.niazitvpro.official.model.NotificationItem;
import com.niazitvpro.official.model.SearchItemsModel;
import com.niazitvpro.official.utils.AdmobIntrestrialAd;
import com.niazitvpro.official.utils.Constants;
import com.niazitvpro.official.utils.ReadWriteJsonFileUtils;
import com.niazitvpro.official.utils.RetrofitUtils;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niazitvpro.official.fragment.AllVideoPlayingFragment.playerView;
import static com.niazitvpro.official.fragment.FMRadioPlayFragment.toggleplayButton;
import static com.niazitvpro.official.fragment.WebPageLoadFragment.webView;
import static com.niazitvpro.official.service.RadioService.exoPlayer;
import static com.niazitvpro.official.utils.Constants.APP_LOGO;
import static com.niazitvpro.official.utils.Constants.IS_FIRST_TIME;
import static com.niazitvpro.official.utils.Constants.RATE_APP;
import static com.niazitvpro.official.utils.Constants.RATE_APP_MESSAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    public static DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public static CardView cardView;
    private Toolbar toolbar;
    private ImageView icon_search,icon_notification,icon_share,icon_my_download;
    public  ImageView icon_menu;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    public  LinearLayout ll_menu;
    private RecyclerView recycler_navItemList;
    private SharedPrefTVApp sharedPrefTVApp;
    public static AutoCompleteTextView et_searchShow;
    public static SearchShowListAdapter adapter;
    private SearchItemsModel searchItemsModel;
    private PopupWindow popupWindow;
    private List<SearchItemsModel> searchItemsList = new ArrayList<>();
    private List<NotificationItem> notificationItemList = new ArrayList<>();
    private FrameLayout frameLayout;
    public static RelativeLayout rl_notificationCount;
    private TextView tv_notificationCount;
    public static String notificationCount = "";
    private Bitmap bitmap;
    private Drawable drawable;
    public  ImageView img_arrow_back;
    public static String notificationDataHome = "";
    public static String firstItemID;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setDrawerLayout();
    }


    @SuppressLint("CheckResult")
    private  void initView(){

        notificationItemList.clear();
        sharedPrefTVApp = new SharedPrefTVApp(getApplicationContext());
        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.nav_view);
        cardView =findViewById(R.id.ll_card);
        frameLayout =findViewById(R.id.frame_home);
        toolbar =findViewById(R.id.toolbar);
        icon_menu =findViewById(R.id.img_drawer_menu);
        icon_search =findViewById(R.id.img_drawer_search);
        icon_notification =findViewById(R.id.img_drawer_notification);
        icon_share =findViewById(R.id.img_drawer_share);
        ll_menu =findViewById(R.id.ll_menu);
        img_arrow_back =findViewById(R.id.img_drawer_back);
        rl_notificationCount =findViewById(R.id.rl_notification_count);
        tv_notificationCount =findViewById(R.id.tv_notification_count);
        progressBar =findViewById(R.id.home_progress);
        icon_my_download =findViewById(R.id.icon_my_download);
        et_searchShow =findViewById(R.id.et_search);
        rl_notificationCount.setVisibility(View.GONE);

        icon_menu.setOnClickListener(this);
        ll_menu.setOnClickListener(this);
        icon_share.setOnClickListener(this);
        icon_notification.setOnClickListener(this);
        icon_search.setOnClickListener(this);
        img_arrow_back.setOnClickListener(this);
        icon_my_download.setOnClickListener(this);

        SharedPrefTVApp.transparentStatusAndNavigation(MainActivity.this);

        notificationDataHome = getIntent().getExtras().getString("notificationData");

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        userLoginAPI(android_id);

        et_searchShow.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    hideKeyboard(MainActivity.this);

                    String userSearchUrl= et_searchShow.getText().toString();

                    if(!userSearchUrl.equals("")){

                        userSearchAction(et_searchShow.getText().toString());

                    }else {

                        Toast.makeText(MainActivity.this, "please enter search keyword", Toast.LENGTH_SHORT).show();;

                    }
                    return true;
                }
                return false;
            }

        });

        // Capture Text in EditText
        et_searchShow.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

              String text = et_searchShow.getText().toString();

                    if(adapter!=null){
                        et_searchShow.setAdapter(adapter);
                        et_searchShow.showDropDown();
                        adapter.filter(text);
                        adapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void setSearchItemsList(){
        String getSearchCacheResponse = new ReadWriteJsonFileUtils(getApplicationContext()).readJsonFileData("SearchItemist");
        if(getSearchCacheResponse!=null && !getSearchCacheResponse.equals("")){
            String[] parts = getSearchCacheResponse.split("----------");
            Log.d("string==",getSearchCacheResponse);
            setRecycler_searchItemList(MainActivity.this,parts);
        }
    }

    private  void setDrawerLayout(){

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
               this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);


            }
        };

        drawerLayout.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        navigationView.setItemIconTintList(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();

        progressBar.setVisibility(View.GONE);

        loadNavigationDrawer(true);


    }

    private void userSearchAction(String url){

        if(containsLink(url)){

            fragment = new AllVideoPlayingFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("live_tv_show_url", url);
            bundle.putString("user_agent", "NiaziTv");
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frame_home, fragment,AllVideoPlayingFragment.class.getName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();


        }else {

            Toast.makeText(this, "Link is inValid", Toast.LENGTH_SHORT).show();
        }

    }

    private  void loadHomeFragment(boolean openNotification){

        fragment = new MenuCategoryItemsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("menu_id",firstItemID);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_home, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        if(openNotification){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(!notificationDataHome.equals("")){

                        openNotificationFragment(notificationDataHome);
                    }

                }
            },1000);
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onClick(View v) {

        if(v==img_arrow_back){

            ll_menu.setVisibility(View.VISIBLE);
            icon_notification.setVisibility(View.VISIBLE);

            if(notificationCount.equals("0") || notificationCount.equals("")){

                rl_notificationCount.setVisibility(View.GONE);
            }else
            {

                rl_notificationCount.setVisibility(View.VISIBLE);

            }
            icon_share.setVisibility(View.VISIBLE);
            et_searchShow.setVisibility(View.GONE);
            icon_search.setVisibility(View.VISIBLE);
            icon_menu.setVisibility(View.VISIBLE);
            img_arrow_back.setVisibility(View.GONE);


        }

        if(v==icon_menu){


            drawerLayout.openDrawer(GravityCompat.START);
            loadNavigationDrawer(false);


        }
        if(v==ll_menu){

            drawerLayout.openDrawer(GravityCompat.START);
        }

        if(v==icon_share){

            if(exoPlayer!=null){

                exoPlayer.setPlayWhenReady(false);
                toggleplayButton.setChecked(false);


            }
            shareApp();
        }

        if(v==icon_search){

            icon_notification.setVisibility(View.GONE);
            rl_notificationCount.setVisibility(View.GONE);
            icon_share.setVisibility(View.GONE);
            ll_menu.setVisibility(View.GONE);
            et_searchShow.setVisibility(View.VISIBLE);
            et_searchShow.setText("");
            et_searchShow.setEnabled(true);
            et_searchShow.requestFocus();
            icon_search.setVisibility(View.GONE);
            icon_menu.setVisibility(View.GONE);
            img_arrow_back.setVisibility(View.VISIBLE);
        }

        if(v==icon_my_download){
            startActivity(new Intent(getApplicationContext(),MyDownloadVideoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        if(v==icon_notification){

            Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_home);

            if(f instanceof AllVideoPlayingFragment || f instanceof ChannelSubcategoryFragment
            ||f instanceof OpenPushNotificationFragment) {

                if(ChannelSubcategoryFragment.exoPlayer!=null){
                    ChannelSubcategoryFragment.playerView.onPause();
                    ChannelSubcategoryFragment.exoPlayer.setPlayWhenReady(false);
                }

                if(AllVideoPlayingFragment.exoPlayer!=null){
                    AllVideoPlayingFragment.playerView.onPause();
                    AllVideoPlayingFragment.exoPlayer.setPlayWhenReady(false);
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if (webView != null) {

                webView.onPause();
            }

            if(exoPlayer!=null){

                exoPlayer.setPlayWhenReady(false);
                toggleplayButton.setChecked(false);

            }

            fragment = new NotificationFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_home, fragment, NotificationFragment.class.getName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            AdmobIntrestrialAd.getInstance().loadIntertitialAds(MainActivity.this);

        }
    }

    public void loadNavigationDrawer(boolean loadHomeFragment){

        ImageView imageView =navigationView.getHeaderView(0).findViewById(R.id.img_nav_app_icon);
        recycler_navItemList = navigationView.getHeaderView(0).findViewById(R.id.nav_item_list);

        Glide.with(getApplicationContext()).load(sharedPrefTVApp.getString(APP_LOGO))
                .apply(new RequestOptions().placeholder(android.R.color.black)
                        .error((int) android.R.color.black).
                                diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH))
                .into(imageView);

        String getCacheResponse = new ReadWriteJsonFileUtils(getApplicationContext()).readJsonFileData("MenuItem");

        if(getCacheResponse!=null && !getCacheResponse.equals("")){

            setRecycler_navItemList(getCacheResponse,loadHomeFragment,false);
            setSideMenuCategoryApi(loadHomeFragment);

        }else {

            setSideMenuCategoryApi(loadHomeFragment);

        }

        setSearchItemsList();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        return false;
    }

    private void setSideMenuCategoryApi(boolean loadHomeFragment) {

        if(!SplashScreenActivity.isNetworkAvailable(MainActivity.this)){

            String getCacheResponse = new ReadWriteJsonFileUtils(getApplicationContext()).readJsonFileData("MenuItem");
            if(getCacheResponse!=null && !getCacheResponse.equals("")){

                setRecycler_navItemList(getCacheResponse,loadHomeFragment,false);

            }

        }else {

            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPi(getApplicationContext()).getSideMenuCategory();
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        try {
                            String str_response = responseBody.string();

                            try {
                                new ReadWriteJsonFileUtils(getApplicationContext()).createJsonFileData("MenuItem", str_response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            setRecycler_navItemList(str_response,loadHomeFragment,true);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(MyApplication.getAppContext(), "something went wrong!!!please check your connection", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    private  void setRecycler_navItemList(String str_response,boolean loadHomeFragment,boolean openNotification){

        JSONObject jsonObject = null;

        try {

            jsonObject = new JSONObject(str_response);

            String status = jsonObject.getString("status");

            if(status.equals("1")){

                NavigationItemModel navigationItemModel = new NavigationItemModel(str_response);
                firstItemID = navigationItemModel.navigationItemModelList.get(0).navItemId;
                NavigationItemAdapter adapter = new NavigationItemAdapter(MainActivity.this,navigationItemModel.navigationItemModelList);


                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                recycler_navItemList.setHasFixedSize(true);
                recycler_navItemList.setNestedScrollingEnabled(false);
                recycler_navItemList.setLayoutManager(gridLayoutManager);
                recycler_navItemList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if(loadHomeFragment){
                    loadHomeFragment(openNotification);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onBackPressed() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int back = getSupportFragmentManager().getBackStackEntryCount();

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_home);
        if(f instanceof MenuCategoryItemsFragment){

            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            if(back==0){

                String exitMessage = "Are you sure to Exit App?";
                setPopupDialog(exitMessage,"Niazi TV","yes");

            } else {

                for (int i = 1; i <= back; i++) {

                    getSupportFragmentManager().popBackStackImmediate();

                }
            }
        }

        if(f instanceof AllVideoPlayingFragment){

            if(AllVideoPlayingFragment.playerView!=null && AllVideoPlayingFragment.exoPlayer!=null){
                AllVideoPlayingFragment.exoPlayer.setPlayWhenReady(false);
                playerView.setPlayer(null);
                AllVideoPlayingFragment.exoPlayer.release();
                AllVideoPlayingFragment.exoPlayer.stop();
                AllVideoPlayingFragment.exoPlayer.seekTo(0);
                AllVideoPlayingFragment.playerView=null;
                AllVideoPlayingFragment. exoPlayer=null;

            }
            getSupportFragmentManager().popBackStackImmediate();

        }else if(f instanceof ChannelSubcategoryFragment){

            if(ChannelSubcategoryFragment.recycler_subChannelList.getVisibility()==View.VISIBLE){
                getSupportFragmentManager().popBackStackImmediate();
            }else {
                ChannelSubcategoryFragment.img_FullScreenVideo.performClick();
            }

        }else if(f instanceof SubCategoryFragment){

            getSupportFragmentManager().popBackStackImmediate();

        }else if(f instanceof NotificationFragment){

            getSupportFragmentManager().popBackStackImmediate();

        }else if(f instanceof FMRadioPlayFragment){

            getSupportFragmentManager().popBackStackImmediate();

        }else if(f instanceof WebPageLoadFragment){

            getSupportFragmentManager().popBackStackImmediate();

        }else {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);

            } else if (back >= 1) {
                for (int i = 1; i <= back; i++) {
                    getSupportFragmentManager().popBackStackImmediate();

                }
            }

        }

        if(f==null){

            String exitMessage = "Are you sure to Exit App?";
            setPopupDialog(exitMessage,"Niazi TV","yes");
        }



    }


    private  void setPopupDialog(String message,String title,String setYesButtonText){

        AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(message)
         .setCancelable(false);
        builder.setPositiveButton(setYesButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                        if(title.equalsIgnoreCase("Niazi TV")){

                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);

                        }else {

                            openPlayStore();

                        }

                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        if(sharedPrefTVApp.getString(RATE_APP).equals("1")){
            builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
        }


        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"));
        alert.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#000000"));
    }

    private  void openPlayStore(){

        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivity(i);
    }

    private void shareApp(){

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "TV App");
            String shareMessage= "Hi, Download Niazi TV App & Enjoy All Live TV Channels.\n" +
                    "Click the link below to Download.\n";
            shareMessage = shareMessage + "https://niazitv.com";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));

        } catch(Exception e) {

        }
    }

    private void checkUPdate() {
        VersionChecker versionChecker = new VersionChecker();
        try
        {   String appVersionName = BuildConfig.VERSION_NAME;
            String mLatestVersionName = versionChecker.execute().get();
            if(!appVersionName.equals(mLatestVersionName)){

                String message = "New Version Of Niazi TV is Available,Please Update App And Enjoy!";

                setPopupDialog(message,"Update","Update");

            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class VersionChecker extends AsyncTask<String, String, String> {
        private String newVersion;
        @Override
        protected String doInBackground(String... params) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id="+getPackageName())
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }
    }

    //for hide keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static  void setRecycler_searchItemList(Activity activity,String[] split){

        SearchItemsModel searchItemsModel = null;

        try {
            searchItemsModel = new SearchItemsModel(split);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(searchItemsModel.searchItemsModelList!=null){

            adapter = new SearchShowListAdapter(activity,R.layout.layout_nav_item_list,searchItemsModel.searchItemsModelList);
            et_searchShow.setAdapter(adapter);
        }

    }


    public static boolean containsLink(String input) {
        boolean result = false;

        String[] parts = input.split("\\s+");

        for (String item : parts) {
            if (android.util.Patterns.WEB_URL.matcher(item).matches()) {
                result = true;
                break;
            }
        }

        return result;
    }

    private void userLoginAPI(String androidID){

        Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPi(getApplicationContext()).userLogin(androidID);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null && response.isSuccessful()) {
                    ResponseBody responseBody = response.body();

                    try {

                        String str_response = responseBody.string();

                        JSONObject jsonObject = new JSONObject(str_response);
                        String status = jsonObject.getString("status");
                        if(status.equals("1")){

                            notificationCount = jsonObject.getString("notification_count");

                            if(notificationCount.equals("0")){

                                rl_notificationCount.setVisibility(View.GONE);

                            }else
                            {
                                rl_notificationCount.setVisibility(View.VISIBLE);
                                tv_notificationCount.setText(""+notificationCount);

                            }

                            Log.d("Login---","success");
                        }else
                        {

                            Log.d("Login---","fail");

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(MyApplication.getAppContext(), "something went wrong!!!please check your connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private  void openNotificationFragment(String notificationData){

        Fragment fragment = new OpenPushNotificationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("notificationData", notificationData);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.frame_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

    }




}
