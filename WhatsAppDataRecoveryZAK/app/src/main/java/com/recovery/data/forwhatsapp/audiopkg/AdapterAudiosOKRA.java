package com.recovery.data.forwhatsapp.audiopkg;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.activities.ActivityAudiosOKRA;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterAudiosOKRA extends RecyclerView.Adapter implements Filterable {
    private static final String TAG = "92727";
    ArrayList<File> audio_files;
    ArrayList<File> example_audio_files;
    Context context;
    ArrayList<String> selectedRows = new ArrayList<>();

    LongClickInterfaceOKRA itemLongClickListeners;

    public void setAudios_play_interface(AudiosPlayInterfaceOKRA audios_play_interface) {
        this.audios_play_interface = audios_play_interface;
    }

    public void setLongClickListeners(ActivityAudiosOKRA audios_activity) {
        this.itemLongClickListeners = audios_activity;
    }

    AudiosPlayInterfaceOKRA audios_play_interface;

    public AdapterAudiosOKRA(ArrayList<File> audio_files, Context context) {
        this.audio_files = audio_files;
        this.context = context;
        example_audio_files=audio_files;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_audio_okra, parent, false);
        return new Adapter_ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_ViewHolder row_view = (Adapter_ViewHolder) holder;

        File imageFile = audio_files.get(position);
        if (selectedRows.size() != 0 && selectedRows.contains(imageFile.getAbsolutePath())) {
            row_view.dot.setVisibility(View.VISIBLE);
            row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);

        } else {
            row_view.constraintLayout.setBackgroundColor(Color.TRANSPARENT);
            row_view.dot.setBackgroundResource(R.drawable.ic_unchek_icon);
            row_view.dot.setVisibility(View.GONE);
        }
        Log.d(TAG, "onBindViewHolder: viewing :: " + imageFile.getName());
        row_view.note.setText(imageFile.getName());
        row_view.note.setSelected(true);
        if((imageFile.length()/1024)/1000==0.0){
            float file_size = (imageFile.length()/1024);
            row_view.videoSize.setText(file_size+" KB");
        }else {
            float file_size = (imageFile.length() / 1024) / 1000;
            row_view.videoSize.setText(file_size+" MB");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(imageFile.lastModified()));
        row_view.dateText.setText(dateString+"");
        row_view.playButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audios_play_interface.playAudio(position, imageFile.getAbsolutePath(), imageFile.getName());
            }
        });
        row_view.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (selectedRows.size() != 0 && selectedRows.contains(imageFile.getAbsolutePath())) {
                    row_view.constraintLayout.setBackgroundColor(Color.TRANSPARENT);
                    row_view.dot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    row_view.dot.setVisibility(View.GONE);
                    selectedRows.remove(imageFile.getAbsolutePath());

                    if (selectedRows.size() == 0) {
                        itemLongClickListeners.onLongClicked(false);
                    }
                } else if (selectedRows.size() != 0 && !selectedRows.contains(imageFile.getAbsolutePath())) {
                    row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
                    row_view.dot.setVisibility(View.VISIBLE);
                    selectedRows.add(imageFile.getAbsolutePath());
                } else {
                    audios_play_interface.playAudio(position, imageFile.getAbsolutePath(), imageFile.getName());
                }
            }
        });
        row_view.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View view) {
                if (selectedRows.size() == 0) {
                    itemLongClickListeners.onLongClicked(true);
                    row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
                    row_view.dot.setVisibility(View.VISIBLE);
                    selectedRows.add(imageFile.getAbsolutePath());
                }
                return true;
            }
        });
//        row_view.playButon.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                audios_play_interface.onLongClick(position,imageFile.getAbsolutePath());
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return audio_files.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public ArrayList<String> getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(ArrayList<String> selectedRows) {
        this.selectedRows = selectedRows;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<File> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(example_audio_files);
                } else {
                    String filterPattern = constraint.toString();
                    for (File item : example_audio_files) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                audio_files = filteredList;
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

    private class Adapter_ViewHolder extends RecyclerView.ViewHolder {

        TextView note;
        ImageView playButon;
        FrameLayout constraintLayout;
        TextView videoSize;
        TextView dateText;
        ImageView dot;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.voicename);
            playButon = itemView.findViewById(R.id.playbutton);
            constraintLayout = itemView.findViewById(R.id.framelayout);
            videoSize=itemView.findViewById(R.id.audiosize);
            dateText=itemView.findViewById(R.id.dateTextaudio);
            dot=itemView.findViewById(R.id.dotselect3);
        }
    }
}
