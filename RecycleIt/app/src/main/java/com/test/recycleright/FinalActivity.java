package com.test.recycleright;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class FinalActivity extends AppCompatActivity {
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        findViewById(R.id.iv_back_f).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(3464);
                        finish();
                    }
                }
        );
        findViewById(R.id.iv_gift_f).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox(getResources().getString(R.string.total_points),
                        getResources().getString(R.string.your_total_points_are)+
                                " "+MySharedPreferences.getInstance(FinalActivity.this).getScores());
            }
        });

        showDialogBox(getResources().getString(R.string.you_have_submitted_following_info),
                RecycleInfo.getObj().toString());
        MySharedPreferences.getInstance(FinalActivity.this).incrementScores();
        player=new MediaPlayer();
        playAudioFile();
    }

    private void showDialogBox(String title,String msg) {
        Dialog dialog=new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        TextView tvTitle=dialog.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        TextView text=dialog.findViewById(R.id.tv_msg);
        text.setText(msg);
        Button btnOk=dialog.findViewById(R.id.btn_done);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void playAudioFile(){
        AssetFileDescriptor afd = null;
        try {
            if (player.isPlaying() == true) {
                player.stop();
                player.release();
                player = new MediaPlayer();
            }
            afd = getAssets().openFd("thankyou.wav");
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            player.prepare();
            player.setVolume(1f, 1f);
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(player!=null&&!player.isPlaying()){
            player.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(player!=null&&player.isPlaying()){
            player.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(player!=null&&player.isPlaying()){
            player.stop();
            player.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(player!=null&&player.isPlaying()){
            player.stop();
            player.release();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(3464);
        finish();
    }
}