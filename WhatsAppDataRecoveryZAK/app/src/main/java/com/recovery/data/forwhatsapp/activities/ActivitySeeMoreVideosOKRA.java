package com.recovery.data.forwhatsapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.fragmentstatuspkg.FragmentStatusOKRA;
import com.recovery.data.forwhatsapp.videospkg.AdapterVideosOKRA;
import com.recovery.data.forwhatsapp.videospkg.VideosUrlInterfaceOKRA;
import com.recovery.data.forwhatsapp.fileobserverspkg.RecoverFilesOKRA;
import com.recovery.data.forwhatsapp.fragmentstatuspkg.MyFilesListStatusOKRA;


import java.io.File;
import java.lang.reflect.Field;
import java.net.URLConnection;
import java.util.ArrayList;

public class ActivitySeeMoreVideosOKRA extends AppCompatActivity implements VideosUrlInterfaceOKRA, LongClickInterfaceOKRA {

    private static final String TAG = "92727";
    RecyclerView recyclerView;
    AdapterVideosOKRA videos_adapter;
    ArrayList<File> video_Files = new ArrayList<>();
    FrameLayout frameLayout;
    boolean fragment;
    public static Context context;
    ImageView delete, share;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    SearchView searchView;
    LinearLayout linearSelected;
    ImageView selectDot;
    ArrayList<String> selectall=new ArrayList<String>();
 /*   @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //   container.getLayoutTransition().setAnimateParentHierarchy(false);
        return inflater.inflate(R.layout.activity_videos_,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getContext().getApplicationContext();
        initComponents(view);
        setAdapterData();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

//        findViewById(R.id.tv_all_videos).setVisibility(View.VISIBLE);
        context = getApplicationContext();
        initComponents();
        setAdapterData();
        findViewById(R.id.videobar).setVisibility(View.GONE);


        if (video_Files.size() > 0) {
//            findViewById(R.id.delete).setVisibility(View.VISIBLE);
//            findViewById(R.id.share).setVisibility(View.VISIBLE);
            findViewById(R.id.videos_recycleview).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.nodatacons).setVisibility(View.VISIBLE);
        }

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
   /*     loadBannerAdd();
        admobBanner();*/
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
        //  manage_directory=new Videos_Manage_Recovery_Directory();
        progressBar = findViewById(R.id.videobar);
        delete = findViewById(R.id.delete);
        constraintLayout = findViewById(R.id.nodatacons);
        share = findViewById(R.id.share);
        recyclerView = findViewById(R.id.videos_recycleview);
        searchView=findViewById(R.id.searchView8);
        linearSelected=findViewById(R.id.linearSelectAll);
        selectDot=findViewById(R.id.imageselectdot);
        //recyclerView.getLayoutTransition().setAnimateParentHierarchy(false);
        frameLayout = findViewById(R.id.videoframelayout);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //init adapter
        videos_adapter = new AdapterVideosOKRA(video_Files, this);
        videos_adapter.setVideos_url_interface(this);
        videos_adapter.setLongClickListenersSeeMore(this);
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
            }
            videos_adapter.setSelectedRows(new ArrayList<>());
            setAdapterData();
        }
    }

    class DeleteSelectedItems implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(videos_adapter.getSelectedRows().size()>0){
                deleteWarningDialog();
            }else{
                Toast.makeText(ActivitySeeMoreVideosOKRA.this, "Kindly Select atleast one Video", Toast.LENGTH_SHORT).show();
            }


//            ArrayList<String> paths = videos_adapter.getSelectedRows();
//            if (paths != null && paths.size() != 0) {
//                for (String path : paths) {
//                    File f = new File(path);
//                    f.delete();
//                }
//                videos_adapter.setSelectedRows(new ArrayList<>());
//                setAdapterData();
//            }
        }
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
        Dialog dialog = new Dialog(ActivitySeeMoreVideosOKRA.this);
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
                    FragmentStatusOKRA.videos_files.remove(f);
                    video_Files.remove(f);
                    if (FragmentStatusOKRA.videos_files.size() > 0) {
                        findViewById(R.id.videos_recycleview).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.nodatacons).setVisibility(View.VISIBLE);
                        delete.setVisibility(View.GONE);
                        share.setVisibility(View.GONE);
                        linearSelected.setVisibility(View.GONE);
                        selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    }
                }
                videos_adapter.setSelectedRows(new ArrayList<>());
                videos_adapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setAdapterData() {

        video_Files.clear();
        video_Files.addAll(MyFilesListStatusOKRA.files);
        videos_adapter.notifyDataSetChanged();
        //image_files=manage_directory.get_Recovery_Images();
        Log.d(TAG, "setAdapterData: recoverd images size :: " + video_Files.size());

        if (video_Files.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUrlandLaunchInterface(String url,String name) {
       /* fragment = true;
        recyclerView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        //   getSupportActionBar().hide();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Videos_View_Fragment fragment = new Videos_View_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.videoframelayout, fragment, "92727");
        fragmentTransaction.commit();*/

        Intent intent = new Intent(this, ActivityViewVideoOKRA.class);
        intent.putExtra("url", url);
        intent.putExtra("videoname",name);
        startActivity(intent);

    }

    @Override
    public void onLongClick(String Url, int position) {
        PickDialog pickDialog = new PickDialog(Url, position);
        pickDialog.showDialog(this);
    }

    /*    @Override
        public void onBackPressed() {
            if(fragment){
                final Videos_View_Fragment fragment = (Videos_View_Fragment) getSupportFragmentManager().findFragmentByTag("92727");
                if(fragment.isBackPressed()){
                    fragment.setBackPressed(false);
                    fragment.stopvideo();
                    fragment.getView().setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                }else
                    super.onBackPressed();
            }else{
                super.onBackPressed();
            }
        }*/

  /*  public void loadBannerAdd() {

        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);

        AdListener adListener = new AdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {
                adView.loadAd(adView.buildLoadAdConfig().withAdListener(this).build());
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };


        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
        RelativeLayout relativeLayout = findViewById(R.id.banner);
        relativeLayout.addView(adView);
    }


    public void admobBanner() {

        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(SeeMoreVideos.this);
        com.google.android.gms.ads.AdSize adSize = IntersitialAdds.getAdSize(SeeMoreVideos.this);
        mAdView.setAdSize(adSize);

        mAdView.setAdUnitId(getResources().getString(R.string.admob_banner));

        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();

        final RelativeLayout adViewLayout = (RelativeLayout) findViewById(R.id.mybanner);
        adViewLayout.addView(mAdView);

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                //adViewLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                adViewLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adViewLayout.setVisibility(View.VISIBLE);
            }
        });

    }*/


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
}