package com.recovery.data.forwhatsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import com.recovery.data.forwhatsapp.videospkg.AdapterVideosOKRA;
import com.recovery.data.forwhatsapp.videospkg.VideosManageRecoveryDirectoryOKRA;
import com.recovery.data.forwhatsapp.videospkg.VideosUrlInterfaceOKRA;
import com.recovery.data.forwhatsapp.fileobserverspkg.RecoverFilesOKRA;
import com.recovery.data.forwhatsapp.R;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLConnection;
import java.util.ArrayList;

public class Videos_ActivityOKRA extends AppCompatActivity implements VideosUrlInterfaceOKRA, LongClickInterfaceOKRA {

    private static final String TAG = "92727";
    RecyclerView recyclerView;
    AdapterVideosOKRA videos_adapter;
    ArrayList<File> video_Files = new ArrayList<>();
    VideosManageRecoveryDirectoryOKRA manage_directory;
    FrameLayout frameLayout;
    boolean fragment;
    public static Context context;
    ImageView delete, share;
    ProgressBar progressBar;
    LinearLayout linearSelected;
    ImageView selectDot;
    ConstraintLayout constraintLayout;
    SearchView searchView;

    ArrayList<String> selectall=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_videos_okra);
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
//            AATAdsManager.displayInterstitialFacebook();
//        }

        /*getSupportActionBar().setTitle("Videos");*/
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
                videos_adapter.getFilter().filter(newText);
                return true;
            }
        });
        linearSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectall.size()==0) {
                    selectDot.setBackgroundResource(R.drawable.ic_chek_icon);
                    for (int i = 0; i < video_Files.size(); i++) {
                        selectall.add(video_Files.get(i).getAbsolutePath());
                    }
                    videos_adapter.setSelectedRows(selectall);
                    videos_adapter.notifyDataSetChanged();
                }else{
                    selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    videos_adapter.setSelectedRows(new ArrayList<String>());
                    selectall.clear();
                    videos_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initComponents() {
        manage_directory = new VideosManageRecoveryDirectoryOKRA();
        progressBar = findViewById(R.id.videobar);
        delete = findViewById(R.id.delete);
        constraintLayout = findViewById(R.id.nodatacons);
        searchView=findViewById(R.id.searchView8);
        share = findViewById(R.id.share);
        recyclerView = findViewById(R.id.videos_recycleview);
        linearSelected=findViewById(R.id.linearSelectAll);
        selectDot=findViewById(R.id.imageselectdot);
        frameLayout = findViewById(R.id.videoframelayout);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //init adapter
        videos_adapter = new AdapterVideosOKRA(video_Files, this);
        videos_adapter.setVideos_url_interface(this);
        videos_adapter.setLongClickListeners(this);

        //setAadapteData
        //cardView_images_adapter.setItemClickListeners(this);
        recyclerView.setAdapter(videos_adapter);
        delete.setOnClickListener(new DeleteSelectedItems());
        share.setOnClickListener(new ShareSelectedItems());


        delete.setVisibility(View.GONE);
        share.setVisibility(View.GONE);
    }

    class ShareSelectedItems implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ArrayList<String> paths = videos_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                intent.setType("video/*"); /* This example is sharing jpeg images. */
                ArrayList<Uri> files = new ArrayList<Uri>();
                for (String path : paths /* List of the files you want to send */) {
                    File file = new File(path);
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getResources().getString(R.string.authority), file);
                    files.add(uri);
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                startActivity(intent);
            }else{
                Toast.makeText(context, "Kindly Select atleast one Video", Toast.LENGTH_SHORT).show();
            }
            videos_adapter.setSelectedRows(new ArrayList<>());
            setAdapterData();
        }
    }

    class DeleteSelectedItems implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(videos_adapter.getSelectedRows().size()>0) {
                deleteWarningDialog();
            }else {
                Toast.makeText(context, "Kindly Select atleast one Video", Toast.LENGTH_SHORT).show();
            }
            /*ArrayList<String> paths = videos_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                for (String path : paths) {
                    File f = new File(path);
                    f.delete();
                }
                videos_adapter.setSelectedRows(new ArrayList<>());
                setAdapterData();
            }*/
        }
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
//                videos_adapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//        return true;
//    }
    private void setAdapterData() {
        selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
        linearSelected.setVisibility(View.GONE);

        new LoadData().execute();
        //image_files=manage_directory.get_Recovery_Images();
        Log.d(TAG, "setAdapterData: recoverd images size :: " + video_Files.size());
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
            linearSelected.setVisibility(View.GONE);
            selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
        }
    }

    private void deleteWarningDialog() {
        Dialog dialog = new Dialog(Videos_ActivityOKRA.this);
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
            ArrayList<String> paths = videos_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                for (String path : paths) {
                    File f = new File(path);
                    f.delete();
                }
                videos_adapter.setSelectedRows(new ArrayList<>());
                setAdapterData();
            }
            dialog.dismiss();
        });

        dialog.show();
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
            video_Files.clear();
            videos_adapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... strings) {
            video_Files.addAll(manage_directory.get_Recovery_Images());
            return "Task Completed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (video_Files.size() == 0) {
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
                videos_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAdapterData();
    }

    @Override
    public void setUrlandLaunchInterface(String url,String name) {
       /* fragment = true;
        recyclerView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        getSupportActionBar().hide();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Videos_View_Fragment fragment = new Videos_View_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.videoframelayout, fragment, "92727");
        fragmentTransaction.commit();*/

        Intent intent=new Intent(this, ActivityViewVideoOKRA.class);
        intent.putExtra("url",url);
        intent.putExtra("videoname",name);
        startActivity(intent);
    }

    @Override
    public void onLongClick(String Url, int position) {
        PickDialog pickDialog = new PickDialog(Url, position);
        pickDialog.showDialog(this);
    }

    @Override
    public void onBackPressed() {
//        if (fragment) {
//            final Videos_View_Fragment fragment = (Videos_View_Fragment) getSupportFragmentManager().findFragmentByTag("92727");
//            if (fragment.isBackPressed()) {
//                fragment.setBackPressed(false);
//                fragment.stopvideo();
//                fragment.getView().setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//                getSupportActionBar().show();
//            } else
//                super.onBackPressed();
//        } else {
            super.onBackPressed();
//        }
    }

    public class PickDialog {
        String url;
        int position;

        public PickDialog(String url, int position) {
            this.url = url;
            this.position = position;
        }

        public void showDialog(Activity activity) {

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.delete_share_dialogue_okra);
            Button delete = dialog.findViewById(R.id.delete);
            Button share = dialog.findViewById(R.id.share);
            Button save = dialog.findViewById(R.id.save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecoverFilesOKRA recoverFiles = new RecoverFilesOKRA(getApplicationContext());
                    if (recoverFiles.storeFileToRecoveryFolder(url)) {
                        video_Files.remove(position);
                        videos_adapter.notifyDataSetChanged();
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
                    }
                    dialog.dismiss();
                }

            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(url);
                    if (file.exists()) {
                        file.delete();
                        System.gc();
                        video_Files.remove(position);
                        videos_adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType(URLConnection.guessContentTypeFromName(url));
                    //intent.setData(Uri.parse(url));
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
                    Intent chooserIntent = Intent.createChooser(intent, "Sharing Videos");
                    startActivity(chooserIntent);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
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
//        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(Videos_ActivityAAT.this);
//        com.google.android.gms.ads.AdSize adSize = AATAdmobFullAdManager.getAdSize(Videos_ActivityAAT.this);
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