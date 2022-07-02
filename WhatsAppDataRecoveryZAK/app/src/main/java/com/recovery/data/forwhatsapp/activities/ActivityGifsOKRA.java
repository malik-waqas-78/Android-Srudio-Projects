package com.recovery.data.forwhatsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.recovery.data.forwhatsapp.gifspkg.AdapterGifsOKRA;
import com.recovery.data.forwhatsapp.gifspkg.GifsManageRecoveryDirectoryOKRA;
import com.recovery.data.forwhatsapp.gifspkg.GifsUrlInterfaceOKRA;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ActivityGifsOKRA extends AppCompatActivity implements GifsUrlInterfaceOKRA, LongClickInterfaceOKRA {
    private static final String TAG = "92727";
    RecyclerView recyclerView;
    AdapterGifsOKRA _gifs_adapter;
    ArrayList<File> gif_files = new ArrayList<>();
    GifsManageRecoveryDirectoryOKRA manage_directory;
    FrameLayout frameLayout;
    boolean fragmentboolean = false;
    public static Context context;
    ImageView delete, share;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    SearchView searchView;
    LinearLayout linearSelected;
    ImageView selectDot;
    ArrayList<String> selectall=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gifs_okra);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        if(AATAdsManager.isAppInstalledFromPlay(this)) {
////            loadFbBannerAdd();
////            loadAdmobBanner();
//        }
        /*getSupportActionBar().setTitle("Gifs");*/
        context = getApplicationContext();
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
                _gifs_adapter.getFilter().filter(newText);
                return true;
            }
        });
        linearSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectall.size()==0) {
                    selectDot.setBackgroundResource(R.drawable.ic_chek_icon);
                    for (int i = 0; i < gif_files.size(); i++) {
                        selectall.add(gif_files.get(i).getAbsolutePath());
                    }
                    _gifs_adapter.setSelectedRows(selectall);
                    _gifs_adapter.notifyDataSetChanged();
                }else{
                    selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    _gifs_adapter.setSelectedRows(new ArrayList<String>());
                    selectall.clear();
                    _gifs_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initComponents() {
        manage_directory = new GifsManageRecoveryDirectoryOKRA();
        delete = findViewById(R.id.delete);
        share = findViewById(R.id.share);
        constraintLayout = findViewById(R.id.nodatacons);
        searchView=findViewById(R.id.searchView4);
        progressBar = findViewById(R.id.gifbar);
        frameLayout = findViewById(R.id.gifsframealyout);
        recyclerView = findViewById(R.id.recycleview_gifs);
        linearSelected=findViewById(R.id.linearSelectAll5);
        selectDot=findViewById(R.id.imageselectdot5);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        //init adapter
        _gifs_adapter = new AdapterGifsOKRA(gif_files, this);
        _gifs_adapter.setGifs_url_interface(this);
        _gifs_adapter.setLongClickListeners(ActivityGifsOKRA.this);
        //setAadapteData
        //cardView_images_adapter.setItemClickListeners(this);
        recyclerView.setAdapter(_gifs_adapter);
        delete.setOnClickListener(new DeleteSelectedItems());
        share.setOnClickListener(new ShareSelectedItems());

        delete.setVisibility(View.GONE);
        share.setVisibility(View.GONE);
    }

    @Override
    public void onLongClicked(boolean flag) {
        if (flag) {
            delete.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            linearSelected.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
            linearSelected.setVisibility(View.GONE);

        }
    }

    private void deleteWarningDialog() {
        Dialog dialog = new Dialog(ActivityGifsOKRA.this);
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
            dialog.dismiss();
        });
        btn_yes.setOnClickListener(view -> {
            ArrayList<String> paths = _gifs_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                for (String path : paths) {
                    File f = new File(path);
                    f.delete();
                }
                _gifs_adapter.setSelectedRows(new ArrayList<>());
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
//                _gifs_adapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//        return true;
//    }
    class ShareSelectedItems implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ArrayList<String> paths = _gifs_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                intent.setType("gif/*"); /* This example is sharing jpeg images. */
                ArrayList<Uri> files = new ArrayList<Uri>();
                for (String path : paths /* List of the files you want to send */) {
                    File file = new File(path);
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getResources().getString(R.string.authority), file);
                    files.add(uri);
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                startActivity(intent);
            }
            _gifs_adapter.setSelectedRows(new ArrayList<>());
            setAdapterData();
        }
    }

    class DeleteSelectedItems implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            /*ArrayList<String> paths = _gifs_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                for (String path : paths) {
                    File f = new File(path);
                    f.delete();
                }
                _gifs_adapter.setSelectedRows(new ArrayList<>());
                setAdapterData();
            }*/
            if(_gifs_adapter.getSelectedRows().size()>0) {
                deleteWarningDialog();
            }else{
                Toast.makeText(ActivityGifsOKRA.this, "Kindly Select Alteast one gif", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setAdapterData() {
        selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
        linearSelected.setVisibility(View.GONE);

        new LoadData().execute();
        //image_files=manage_directory.get_Recovery_Images();
        Log.d(TAG, "setAdapterData: recoverd images size :: " + gif_files.size());
    }

    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
            gif_files.clear();
            _gifs_adapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... strings) {
            gif_files.addAll(manage_directory.get_Recovery_Gifs());
            return "Task Completed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (gif_files.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                share.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.VISIBLE);
//                share.setVisibility(View.VISIBLE);
//                delete.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.GONE);
                _gifs_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAdapterData();
    }

    @Override
    public void setUrlandLaunchInterface(String url) {
//        fragmentboolean = true;
//        recyclerView.setVisibility(View.GONE);
//        frameLayout.setVisibility(View.VISIBLE);
//        getSupportActionBar().hide();
//        FragmentManager fragmentManager = this.getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Gifs_View_Fragment fragment = new Gifs_View_Fragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        fragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.gifsframealyout, fragment, "92727");
//        fragmentTransaction.commit();

        Intent intent=new Intent(this, ActivityViewVideoOKRA.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    @Override
    public void onLOngClick(String url, int position) {
    }

    @Override
    public void onBackPressed() {
//        if (fragmentboolean) {
//            final Gifs_View_Fragment fragment = (Gifs_View_Fragment) getSupportFragmentManager().findFragmentByTag("92727");
//            if (fragment.isBackPressed()) {
//                fragment.setBackPressed(false);
//                fragment.stopvideo();
//                fragment.getView().setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//                getSupportActionBar().show();
//
//            } else
//                super.onBackPressed();
//        } else {
            super.onBackPressed();
//        }
//        fragmentboolean = false;
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
//
//    public void loadAdmobBanner() {
//
//        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(ActivityGifsAAT.this);
//        com.google.android.gms.ads.AdSize adSize = AATAdmobFullAdManager.getAdSize(ActivityGifsAAT.this);
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