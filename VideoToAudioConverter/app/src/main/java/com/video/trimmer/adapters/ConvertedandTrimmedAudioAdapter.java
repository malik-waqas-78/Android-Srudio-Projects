package com.video.trimmer.adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.video.trimmer.R;
import com.video.trimmer.interfaces.Checkallselected;
import com.video.trimmer.interfaces.OnAudioClickInterface;
import com.video.trimmer.interfaces.OnFolderClicked;
import com.video.trimmer.interfaces.OnOutputItemsLongClicked;
import com.video.trimmer.interfaces.OutputItemShareDeleteClicked;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class ConvertedandTrimmedAudioAdapter extends RecyclerView.Adapter<ConvertedandTrimmedAudioAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<File> audioFiles=new ArrayList<>();
    ArrayList<File> audioFilesExample=new ArrayList<>();
    OnAudioClickInterface onAudioClickInterface;
    OnFolderClicked onFolderClicked;
    ArrayList<String> selectedFiles=new ArrayList<>();
    OutputItemShareDeleteClicked outputItemShareDeleteClicked;
    OnOutputItemsLongClicked onOutputItemsLongClicked;
    Checkallselected checkallselected;
    float type;

    public ConvertedandTrimmedAudioAdapter(Context context, ArrayList<File> audioFiles) {
        this.context = context;
        this.audioFiles = audioFiles;
        audioFilesExample=audioFiles;

    }
    public void onAudioItemClick(OnAudioClickInterface onAudioClickInterface){
        this.onAudioClickInterface=onAudioClickInterface;

    }
    public void onShareDeleteClicked(OutputItemShareDeleteClicked outputItemShareDeleteClicked){
        this.outputItemShareDeleteClicked=outputItemShareDeleteClicked;
    }
    public void onItemLongClicked(OnOutputItemsLongClicked onOutputItemsLongClicked){
        this.onOutputItemsLongClicked=onOutputItemsLongClicked;
    }
    public void onallselected(Checkallselected checkallselected){
        this.checkallselected=checkallselected;
    }

    public ArrayList<String> getSelectedFiles() {
        return selectedFiles;
    }

    public void setSelectedFiles(ArrayList<String> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    @NonNull
    @NotNull
    @Override
    public ConvertedandTrimmedAudioAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
            ConvertedandTrimmedAudioAdapter.ViewHolder viewHolder = new ConvertedandTrimmedAudioAdapter.ViewHolder(view);

             return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ConvertedandTrimmedAudioAdapter.ViewHolder holder, int position) {
        if (selectedFiles.size() != 0 && selectedFiles.contains(audioFiles.get(position).getAbsolutePath())) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

            holder.constraintsharedel.setVisibility(View.VISIBLE);
            if ((audioFiles.get(position).length() / 1024) / 1000 == 0.0) {
                type=1f;
                float file_size = (audioFiles.get(position).length() / 1024);
                if(file_size==0.0){
                    holder.audioSize.setText("0 KB");
                }else {
                    holder.audioSize.setText(timeConversion(getduration(audioFiles.get(position).getAbsolutePath())) + " / " + file_size + " KB");
                }
            } else {
                type=2f;
                float file_size = (audioFiles.get(position).length() / 1024) / 1000;
                holder.audioSize.setText(timeConversion(getduration(audioFiles.get(position).getAbsolutePath())) + " / " + file_size + " MB");
            }
        holder.audioSize.setSelected(true);
            holder.audioName.setSelected(true);
            holder.audioName.setText(audioFiles.get(position).getName());

            holder.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selectedFiles.size() != 0 && selectedFiles.contains(audioFiles.get(position).getAbsolutePath())) {
                       holder.checkBox.setVisibility(View.GONE);
                        selectedFiles.remove(audioFiles.get(position).getAbsolutePath());
                        checkallselected.allselected(selectedFiles.size());
                        if (selectedFiles.size() == 0) {
                            onOutputItemsLongClicked.onLongPressed(false);
                        }
                    } else if (selectedFiles.size() != 0 && !selectedFiles.contains(audioFiles.get(position).getAbsolutePath())) {
                        holder.checkBox.setVisibility(View.VISIBLE);
                        selectedFiles.add(audioFiles.get(position).getAbsolutePath());
                        checkallselected.allselected(selectedFiles.size());
                    } else {
                        onAudioClickInterface.onAudioItemClick(audioFiles.get(position).getAbsolutePath(), audioFiles.get(position).getName(), getduration(audioFiles.get(position).getAbsolutePath()),audioFiles.get(position).length());

                    }

                }
            });
            holder.frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (selectedFiles.size() == 0) {
                        onOutputItemsLongClicked.onLongPressed(true);
                        holder.checkBox.setVisibility(View.VISIBLE);
                        selectedFiles.add(audioFiles.get(position).getAbsolutePath());
                        checkallselected.allselected(selectedFiles.size());
                    }
                    return true;
                }
            });

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outputItemShareDeleteClicked.onShareClicked(audioFiles.get(position).getAbsolutePath());
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outputItemShareDeleteClicked.onDeleteClicked(audioFiles.get(position).getAbsolutePath());
                }
            });


    }

    @Override
    public int getItemCount() {
        return audioFiles.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<File> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(audioFilesExample);
                } else {
                    String filterPattern = constraint.toString();
                    for (File item : audioFilesExample) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                audioFiles = filteredList;
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
        ConstraintLayout constraintsharedel;
        ImageButton share;
        ImageButton delete;
        CheckBox checkBox;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            audioIcon=itemView.findViewById(R.id.audioIcon);
            audioName=itemView.findViewById(R.id.audioName);
            audioSize=itemView.findViewById(R.id.audioSize);
            frameLayout=itemView.findViewById(R.id.frame);
            constraintsharedel=itemView.findViewById(R.id.constraintshareDel);
            share=itemView.findViewById(R.id.imgshare);
            delete=itemView.findViewById(R.id.imgDelete);
            checkBox=itemView.findViewById(R.id.checkbox);

        }
    }

    public String timeConversion(long value) {
        String videoTime=null;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }

    public long getduration(String pathStr){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, Uri.parse(pathStr));
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long millSecond = Integer.parseInt(duration);
        return millSecond;
    }
}
