package com.example.whatsappdatarecovery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.modelclass.ImagesModelClass;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ImagesModelClass> voicenotes;
    static MediaPlayer mediaPlayer=new MediaPlayer();

    public AudioAdapter(Context context, ArrayList<ImagesModelClass> voicenotes) {
        this.context = context;
        this.voicenotes = voicenotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.audio_voice_cardview, parent, false));
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("yyy/MM/dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        ((ViewHolder)holder).text_date.setText(thisDate);
        ((ViewHolder)holder).player_seekBar.setMax(100);
        mediaPlayer=new MediaPlayer();
        ((ViewHolder) holder).image_paly_btn.setOnClickListener((View.OnClickListener) v -> {

            if (mediaPlayer.isPlaying()) {
                ((ViewHolder) holder).handler.removeCallbacks((Runnable) ((ViewHolder) holder).update);
               mediaPlayer.pause();
                ((ViewHolder) holder).image_paly_btn.setImageResource(R.drawable.ic_play);
            } else {
                mediaPlayer.start();
                /*((ViewHolder) holder).image_paly_btn.setImageResource(R.drawable.ic_pause_audio);*/
                ((ViewHolder)holder).seekbar();
            }
        });
        ((ViewHolder) holder).prepareMediaPlayer();
        ((ViewHolder)holder).player_seekBar.setOnTouchListener((View.OnTouchListener) (v, event) -> {
            SeekBar seekBar= (SeekBar) v;
            int playposition=(mediaPlayer.getDuration()/100  ) * seekBar.getProgress();
            mediaPlayer.seekTo(playposition);
            ((ViewHolder)holder).start_time.setText(millisecondtotimer(mediaPlayer.getCurrentPosition()));
            return false;
        });
        mediaPlayer.setOnBufferingUpdateListener((MediaPlayer.OnBufferingUpdateListener) (mp, i) -> ((ViewHolder)holder).player_seekBar.setSecondaryProgress(i));
        ((ViewHolder)holder).btn_share.setOnClickListener((View.OnClickListener) v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("audio/*");
            intent.putExtra(Intent.EXTRA_STREAM, voicenotes.get(position).getPath());
            context.startActivity(Intent.createChooser(intent, "Share Audios Using "));
        });
        ((ViewHolder)holder).delete.setOnClickListener((View.OnClickListener) v -> {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
            }
            File deltedFile=new File(voicenotes.get(position).getPath());
            deltedFile.delete();
            System.gc();
            voicenotes.remove(position);
            notifyDataSetChanged();
        });
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton image_paly_btn, delete, btn_share;
        SeekBar player_seekBar;
        TextView total_time, text_date, start_time;
        /*MediaPlayer mediaPlayer;*/
        Handler handler = new Handler();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_paly_btn = itemView.findViewById(R.id.image_play);
            player_seekBar = itemView.findViewById(R.id.seekbar);
            btn_share = itemView.findViewById(R.id.share);
            delete = itemView.findViewById(R.id.delete);
            total_time = itemView.findViewById(R.id.total_time);
            start_time = itemView.findViewById(R.id.start_time);
            text_date = itemView.findViewById(R.id.audio_date);
            player_seekBar.setMax(100);
        }

        Runnable update = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    seekbar();
                    long currentduration = mediaPlayer.getCurrentPosition();
                    start_time.setText(millisecondtotimer(currentduration));
                }
            }
        };

        public void seekbar() {
            if (mediaPlayer.isPlaying()) {
                player_seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
                handler.postDelayed(update, 1000);
            }
        }

        public void prepareMediaPlayer() {
            try {
                mediaPlayer.setDataSource(voicenotes.get(getAdapterPosition()).getPath());
                mediaPlayer.prepare();
                total_time.setText(millisecondtotimer(mediaPlayer.getDuration()));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }
    private String millisecondtotimer(long milliseconds) {
        String timerString = "";
        String secondString;
        int hours = (int) (milliseconds / (100 * 60 * 60));
        int minutes = (int) (milliseconds % (100 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (100 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }
        timerString = timerString + minutes + ":" + secondString;
        return timerString;
    }

    public static void stop_Voice_Notes() {
       mediaPlayer.stop();
    }
    @Override
    public int getItemCount() {
        return voicenotes.size();
    }
}
