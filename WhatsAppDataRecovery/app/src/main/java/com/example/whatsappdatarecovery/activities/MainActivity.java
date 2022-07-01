package com.example.whatsappdatarecovery.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.adapter.AudioAdapter;
import com.example.whatsappdatarecovery.adapter.RecyclerViewMainAdapter;
import com.example.whatsappdatarecovery.adapter.VoiceAdapter;
import com.example.whatsappdatarecovery.fragments.AudiosFragment;
import com.example.whatsappdatarecovery.fragments.ChatFragment;
import com.example.whatsappdatarecovery.fragments.DocumentsFragment;
import com.example.whatsappdatarecovery.fragments.GifFragment;
import com.example.whatsappdatarecovery.fragments.ImageFragment;
import com.example.whatsappdatarecovery.fragments.StatusFragment;
import com.example.whatsappdatarecovery.fragments.StickerFragment;
import com.example.whatsappdatarecovery.fragments.VideosFragment;
import com.example.whatsappdatarecovery.fragments.VoiceFragment;
import com.example.whatsappdatarecovery.modelclass.GetImages;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements RecyclerViewMainAdapter.OnItemClick {
    public static RecyclerView recyclerView;
    TextView text_image;
    TextView textView_arraylist_size;
    ImageButton btn_refresh;
    ImageView imageView;
    RecyclerViewMainAdapter recyclerMainAdapter;
    ArrayList<GetImages> arrayList;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        text_image = findViewById(R.id.text_image);
        imageView = findViewById(R.id.image_show);
        btn_refresh = findViewById(R.id.refresh_btn);
        textView_arraylist_size=findViewById(R.id.arraylist_total_size);
        arrayList = new ArrayList<>();
        arrayList.add(new GetImages(R.drawable.ic_chat, R.drawable.ic_chat_checked, "  Chat"));
        arrayList.add(new GetImages(R.drawable.ic_images, R.drawable.ic_photos_checked, "  Images"));
        arrayList.add(new GetImages(R.drawable.ic_audios, R.drawable.ic_headphone_checked, "   Audio"));
        arrayList.add(new GetImages(R.drawable.ic_video, R.drawable.ic_video_camera_checked, "  Videos"));
        arrayList.add(new GetImages(R.drawable.ic_stickers, R.drawable.ic_stickers_checked, "Stickers"));
        arrayList.add(new GetImages(R.drawable.ic_gif, R.drawable.ic_gif_checked, " Gif"));
        arrayList.add(new GetImages(R.drawable.ic_voice, R.drawable.ic_microphone_checked, " Voice"));
        arrayList.add(new GetImages(R.drawable.ic_docs, R.drawable.ic_doc_checked, " Documents"));
        arrayList.add(new GetImages(R.drawable.ic_status, R.drawable.ic_status_checked, " Status"));
        arrayList.get(0).setSelected(true);
        recyclerMainAdapter = new RecyclerViewMainAdapter(this, arrayList, this);
        recyclerView.setAdapter(recyclerMainAdapter);
        //Adding Fragment in MainActivity
        btn_refresh.setOnClickListener(v -> {
            ChatFragment chatFragment = new ChatFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, chatFragment, null);
            arrayList.get(0).setSelected(true);
            for (int i = 1; i < arrayList.size(); i++) {
                arrayList.get(i).setSelected(false);
            }
            recyclerMainAdapter.notifyDataSetChanged();
            fragmentTransaction.commit();
        });
        ChatFragment chatFragment = new ChatFragment();
        if (findViewById(R.id.fragmentContainer) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, chatFragment, null);
        fragmentTransaction.commit();
    }
    @Override
    public void onItemClick(int position) {
        Log.d("abc", "onItemClick: " + position);
        if (position == 0) {
            ChatFragment chatFragment = new ChatFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, chatFragment, null);
            fragmentTransaction.commit();
        }
        if (position == 1) {
            ImageFragment imageFragment = new ImageFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, imageFragment, null);
            fragmentTransaction.commit();
        }
        if (position == 2) {
            AudiosFragment audioFragment = new AudiosFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, audioFragment, null);
            fragmentTransaction.commit();
        }
        if (position == 3) {
            VideosFragment videoFragment = new VideosFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, videoFragment, null);
            fragmentTransaction.commit();
        }
        if (position == 4) {
            StickerFragment stickersFragment = new StickerFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, stickersFragment, null);
            fragmentTransaction.commit();
        }
        if (position == 5) {
            GifFragment gifsFragment = new GifFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, gifsFragment, null);
            fragmentTransaction.commit();
        }
        if (position == 6) {
            VoiceFragment voiceFragment = new VoiceFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, voiceFragment, null);
            fragmentTransaction.commit();
        }
        if (position == 7) {
            DocumentsFragment documentsFragment = new DocumentsFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, documentsFragment, null);
            fragmentTransaction.commit();
        }
        if (position == 8) {
            StatusFragment documentsFragment = new StatusFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, documentsFragment, null);
            fragmentTransaction.commit();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoiceAdapter.stop_Voice_Notes();
        AudioAdapter.stop_Voice_Notes();
    }

    //dialogebox
    public void showDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_confirm_dialoge_box);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView btn_yes = dialog.findViewById(R.id.btn_delete);
        TextView btn_rate_us = dialog.findViewById(R.id.btn_rate_us);

        btn_rate_us.setOnClickListener(v -> rateApp());
        btn_yes.setOnClickListener(v -> {
            MainActivity.this.finishAffinity();
            VoiceAdapter.stop_Voice_Notes();
            AudioAdapter.stop_Voice_Notes();
        });
        TextView btn_no = dialog.findViewById(R.id.btn_cancel);
        btn_no.setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();
        });
        dialog.show();
        dialog.setCancelable(true);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showDialog();
        /*VoiceAdapterRecyclerview.stop_Voice_Notes();
        AudioAdapterRecyclerview.stop_Voice_Notes();*/
    }

    private void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details?id=");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + getPackageName()));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        intent.addFlags(flags);
        return intent;
    }
}