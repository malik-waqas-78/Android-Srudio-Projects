package com.video.trimmer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.trimmer.R;
import com.video.trimmer.interfaces.OnAudioClickInterface;
import com.video.trimmer.modelclasses.AudioModal;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> implements Filterable {
    Context  context;
    ArrayList<AudioModal> audioModals=new ArrayList<>();
    ArrayList<AudioModal> recordDataExample=new ArrayList<>();
    OnAudioClickInterface onAudioClickInterface;
    float type;
    public AudioAdapter(Context context, ArrayList<AudioModal> audioModals) {
        this.context = context;
        this.audioModals = audioModals;
        recordDataExample=audioModals;
    }
    public void onAudioItemClick(OnAudioClickInterface onAudioClickInterface){
        this.onAudioClickInterface=onAudioClickInterface;
    }


    @NonNull
    @NotNull
    @Override
    public AudioAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        AudioAdapter.ViewHolder viewHolder = new AudioAdapter.ViewHolder( view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AudioAdapter.ViewHolder holder, int position) {


        if((audioModals.get(position).getSize()/1024)/1000==0.0){
            type=1f;
            float file_size = (audioModals.get(position).getSize()/1024);
//            if(audioModals.get(position).getOriginalPath().contains(TRIMMED_AUDIO_FOLDER_PATH)){
//                holder.audioSize.setText("00:"+audioModals.get(position).getDuration()+" / "+file_size+" KB");
//            }else {
                holder.audioSize.setText(timeConversion(audioModals.get(position).getDuration()) + " / " + file_size + " KB");
//            }
        }else {
            type=2f;
            float file_size = (audioModals.get(position).getSize() / 1024) / 1000;
//            if(audioModals.get(position).getOriginalPath().contains(TRIMMED_AUDIO_FOLDER_PATH)){
//                holder.audioSize.setText("00:"+audioModals.get(position).getDuration()+" / "+file_size+" MB");
//            }else {
                holder.audioSize.setText(timeConversion(audioModals.get(position).getDuration()) + " / " + file_size + " MB");
//            }
        }
        holder.audioSize.setSelected(true);
        holder.audioName.setSelected(true);
        holder.audioName.setText(audioModals.get(position).getName());

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAudioClickInterface.onAudioItemClick(audioModals.get(position).getOriginalPath(),audioModals.get(position).getName(),audioModals.get(position).getDuration(),audioModals.get(position).getSize());
            }
        });

    }
    public String timeConversion(long value) {
        String videoTime = null;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else if(scs<0){
            videoTime=context.getResources().getString(R.string.lessthansec);

        }else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }
    @Override
    public int getItemCount() {
        return audioModals.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<AudioModal> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(recordDataExample);
                } else {
                    String filterPattern = constraint.toString();
                    for (AudioModal item : recordDataExample) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                audioModals = filteredList;
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView audioIcon;
        TextView audioName,audioSize;
        FrameLayout frameLayout;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            audioIcon=itemView.findViewById(R.id.audioIcon);
            audioName=itemView.findViewById(R.id.audioName);
            audioSize=itemView.findViewById(R.id.audioSize);
            frameLayout=itemView.findViewById(R.id.frame);
        }
    }
}
