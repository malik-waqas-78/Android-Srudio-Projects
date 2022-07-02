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
import android.os.Build;
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
import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.imagespkg.AdapterImagesOKRA;
import com.recovery.data.forwhatsapp.imagespkg.ImagesManageRecoveryDirectoryOKRA;
import com.recovery.data.forwhatsapp.imagespkg.ImagesUrlInterfaceOKRA;
import com.recovery.data.forwhatsapp.fileobserverspkg.RecoverFilesOKRA;
import com.recovery.data.forwhatsapp.R;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLConnection;
import java.util.ArrayList;

public class ActivityImagesOKRA extends AppCompatActivity implements ImagesUrlInterfaceOKRA, LongClickInterfaceOKRA {

    private static final String TAG = "92727";
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    AdapterImagesOKRA _images_adapter;
    ArrayList<File> image_files = new ArrayList<>();
    boolean fragment = false;
    ImagesManageRecoveryDirectoryOKRA manage_directory;
    public static Context contex;
    ImageView delete, share;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    SearchView searchView;
    LinearLayout linearSelected;
    ImageView selectDot;
    ArrayList<String> selectall=new ArrayList<String>();

    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_images_okra);
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


//        loadFbBannerAdd();
//        AATAdsManager.displayInterstitialFacebook();
        /*getSupportActionBar().setTitle("Images");*/
        contex = getApplicationContext();
        initComponents();
        setAdapterData();
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
                _images_adapter.getFilter().filter(newText);
                return true;
            }
        });
        linearSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectall.size()==0) {
                    selectDot.setBackgroundResource(R.drawable.ic_chek_icon);
                    for (int i = 0; i < image_files.size(); i++) {
                        selectall.add(image_files.get(i).getAbsolutePath());
                    }
                    _images_adapter.setSelectedRows(selectall);
                    _images_adapter.notifyDataSetChanged();
                }else{
                    selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    _images_adapter.setSelectedRows(new ArrayList<String>());
                    selectall.clear();
                    _images_adapter.notifyDataSetChanged();
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.example_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setIconified(true);
//        searchView.setIconifiedByDefault(true);
//        searchView.setActivated(true);
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
//
//        //searchView.setBackgroundResource(R.drawable.searchview_background);
//
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
//                _images_adapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//        return true;
//    }

    private void initComponents() {
        manage_directory = new ImagesManageRecoveryDirectoryOKRA();
        progressBar = findViewById(R.id.imagebar);
        delete = findViewById(R.id.delete);
        constraintLayout = findViewById(R.id.nodatacons);
        share = findViewById(R.id.share);
        frameLayout = findViewById(R.id.imagesframelayout);
        recyclerView = findViewById(R.id.images_recycleview);
        searchView=findViewById(R.id.searchView);
        linearSelected=findViewById(R.id.linearSelectAll3);
        selectDot=findViewById(R.id.imageselectdot3);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        //init adapter
        _images_adapter = new AdapterImagesOKRA(image_files, this);
        _images_adapter.setInterface(this);
        _images_adapter.setLongClickListeners(this);
        //setAadapteData
        //cardView_images_adapter.setItemClickListeners(this);
        recyclerView.setAdapter(_images_adapter);
        delete.setOnClickListener(new DeleteSelectedItems());
        share.setOnClickListener(new ShareSelectedItems());



        delete.setVisibility(View.GONE);
        share.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.GONE);
    }

    class ShareSelectedItems implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ArrayList<String> paths = _images_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                intent.setType("image/*"); /* This example is sharing jpeg images. */
                ArrayList<Uri> files = new ArrayList<Uri>();
                for (String path : paths /* List of the files you want to send */) {
                    File file = new File(path);
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getResources().getString(R.string.authority), file);
                    files.add(uri);
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                startActivity(intent);
            }
            _images_adapter.setSelectedRows(new ArrayList<>());
            setAdapterData();
        }
    }

    class DeleteSelectedItems implements View.OnClickListener {

        @Override
        public void onClick(View view) {
//            ArrayList<String> paths = _images_adapter.getSelectedRows();
//            if (paths != null && paths.size() != 0) {
            if(_images_adapter.getSelectedRows().size()>0) {
                deleteWarningDialog();
            }else{
                Toast.makeText(ActivityImagesOKRA.this, "Kindly Select atleast one Image", Toast.LENGTH_SHORT).show();
            }
                /*for (String path : paths) {
                    File f = new File(path);
                    f.delete();
                }
                _images_adapter.setSelectedRows(new ArrayList<>());
                setAdapterData();*/
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

         new LoadData().execute();
    }

    private void setAdapterData() {


       // new LoadData().execute();
        //image_files=manage_directory.get_Recovery_Images();
        Log.d(TAG, "setAdapterData: recoverd images size :: " + image_files.size());
    }

    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
//            constraintLayout.setVisibility(View.GONE);
            image_files.clear();
            _images_adapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... strings) {
            image_files.addAll(manage_directory.get_Recovery_Images());
            return "Task Completed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (image_files.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                share.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
                selectDot.setBackgroundResource(R.drawable.ic_unchek_icon);
                linearSelected.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
//                share.setVisibility(View.VISIBLE);
//                delete.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.GONE);
                _images_adapter.notifyDataSetChanged();
            }
        }
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        setAdapterData();
    }*/

    @Override
    public void setUrlandLaunchInterface(String url,String name) {
//        fragment = true;
//        recyclerView.setVisibility(View.GONE);
//        frameLayout.setVisibility(View.VISIBLE);
//        getSupportActionBar().hide();
//        FragmentManager fragmentManager = this.getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Images_View_Fragment fragment = new Images_View_Fragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        fragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.imagesframelayout, fragment, "92727");
//        fragmentTransaction.commit();

        Intent intent = new Intent(ActivityImagesOKRA.this, ActivityViewImageOKRA.class);
        intent.putExtra("url", url);
        intent.putExtra("imgname",name);
        startActivity(intent);
    }

    @Override
    public void onLongClick(String url, int position) {
        PickDialog pickDialog = new PickDialog(url, position);
        pickDialog.showDialog(this);
    }

    @Override
    public void onBackPressed() {
//        if (fragment) {
//            final Images_View_Fragment fragment = (Images_View_Fragment) getSupportFragmentManager().findFragmentByTag("92727");
//            if (fragment.isBackpressed()) {
//                fragment.setBackpressed(false);
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
                        image_files.remove(position);
                        _images_adapter.notifyDataSetChanged();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            final Uri contentUri = Uri.fromFile(new File(url));
                            scanIntent.setData(contentUri);
                            sendBroadcast(scanIntent);
                        } else {
                            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                            sendBroadcast(intent);
                        }
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
                        image_files.remove(position);
                        _images_adapter.notifyDataSetChanged();
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
                    Intent chooserIntent = Intent.createChooser(intent, "Sharing Images");
                    startActivity(chooserIntent);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.generic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                ArrayList<String> paths = _images_adapter.getSelectedRows();
                if (paths != null && paths.size() != 0) {
                    for (String path : paths) {
                        File f = new File(path);
                        f.delete();
                    }
                    _images_adapter.setSelectedRows(new ArrayList<>());
                    setAdapterData();
                }
                break;

            case R.id.menu_share:
                ArrayList<String> paths2 = _images_adapter.getSelectedRows();
                if (paths2 != null && paths2.size() != 0) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                    intent.setType("image/*"); *//* This example is sharing jpeg images. *//*
                    ArrayList<Uri> files = new ArrayList<Uri>();
                    for (String path : paths2 *//* List of the files you want to send *//*) {
                        File file = new File(path);
                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.fha.whatsappdatarecoveryapp.fileprovider", file);
                        files.add(uri);
                    }
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                    startActivity(intent);
                }
                _images_adapter.setSelectedRows(new ArrayList<>());
                setAdapterData();
                break;
        }
        return true;
    }*/

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
        Dialog dialog = new Dialog(ActivityImagesOKRA.this);
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
            ArrayList<String> paths = _images_adapter.getSelectedRows();
            if (paths != null && paths.size() != 0) {
                for (String path : paths) {
                    File f = new File(path);
                    f.delete();
                }
                _images_adapter.setSelectedRows(new ArrayList<>());
                setAdapterData();
            }
            dialog.dismiss();
        });

        dialog.show();
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

}