package com.speak.to.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.speak.to.Activities.PlayAudio;
import com.speak.to.Dialogs.GeneralDialog_VoiceSMS;
import com.speak.to.Interfaces.GeneralDialogInterface_voiceSMS;
import com.speak.to.Interfaces.RecordingListInterface;
import com.speak.to.Models.Recording_List_Item;
import com.speak.to.R;
import com.speak.to.databinding.RowLayoutRecordingsBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.speak.to.Utils.Constants.NAME;
import static com.speak.to.Utils.Constants.URL;

public class Recording_List_Adapter extends RecyclerView.Adapter<Recording_List_Adapter.ViewHolder> {
    Context context;
    RowLayoutRecordingsBinding binding;
    ArrayList<Recording_List_Item> mDataSet;
    RecordingListInterface recordingListInterface;

    public Recording_List_Adapter(Context context, ArrayList<Recording_List_Item> FilesList, RecordingListInterface recordingListInterface) {
        this.context = context;
        mDataSet = new ArrayList<>();
        mDataSet = FilesList;
        this.recordingListInterface = recordingListInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowLayoutRecordingsBinding
                .inflate(LayoutInflater.from(parent.getContext())
                        , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Recording_List_Item item = mDataSet.get(position);
        File currentFile = new File(item.get_absolute_path());
        String current_time = new SimpleDateFormat("dd/LL/yy hh:mm a", Locale.US).format(new Date(currentFile.lastModified()));
        binding.recordingDetails.setText(current_time.toLowerCase());
        binding.recordingTitle.setText(item.get_name());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(RowLayoutRecordingsBinding inflate) {
            super(inflate.getRoot());
            binding = inflate;
            binding.cardviewRecordingList.setOnClickListener(view -> {
                String path = mDataSet.get(getAdapterPosition()).get_absolute_path();
                String name = mDataSet.get(getAdapterPosition()).get_name();
                context.startActivity(new Intent(context, PlayAudio.class)
                        .putExtra(URL, path).putExtra(NAME, name));
            });
            binding.btnDelete.setOnClickListener(view -> {
                final Recording_List_Item item = mDataSet.get(getAdapterPosition());
                File currentFile = new File(item.get_absolute_path());
                GeneralDialog_VoiceSMS.CreateGeneralDialog(context,
                        context.getString(R.string.delete_title)
                        , context.getString(R.string.delete_desc)
                        , context.getString(R.string.delete)
                        , context.getString(R.string.cancel)
                        , new GeneralDialogInterface_voiceSMS() {
                            @Override
                            public void Positive(Dialog dialog) {
                                mDataSet.remove(item);
                                currentFile.delete();
                                notifyDataSetChanged();
                                recordingListInterface.UpdateList(mDataSet.size());
                                dialog.dismiss();
                            }

                            @Override
                            public void Negative(Dialog dialog) {

                            }
                        }

                );
            });
        }
    }
}
