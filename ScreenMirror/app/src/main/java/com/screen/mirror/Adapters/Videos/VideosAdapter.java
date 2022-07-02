package com.screen.mirror.Adapters.Videos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.screen.mirror.Activities.Videos.VideosGridShowActivity_OKRA;
import com.screen.mirror.Models.MediaModel;
import com.screen.mirror.R;

import java.util.ArrayList;

import static com.screen.mirror.Utils.Constants_CA.FOLDER_NAME;
import static com.screen.mirror.Utils.Constants_CA.FOLDER_PATH;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
    private final ArrayList<MediaModel> mediaList;
    private final Context context;

    public VideosAdapter(ArrayList<MediaModel> mediaList, Context context) {
        this.mediaList = mediaList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_video_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.ViewHolder holder, int position) {
        MediaModel model = mediaList.get(position);
        holder.folderName.setText(model.getFolderName());
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView media_card_back;
        TextView folderName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            media_card_back = itemView.findViewById(R.id.media_card_back);
            folderName = itemView.findViewById(R.id.folderName);

            media_card_back.setOnClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    context.startActivity(new Intent(context, VideosGridShowActivity_OKRA.class)
                            .putExtra(FOLDER_PATH,
                                    mediaList.get(getAdapterPosition()).getFolderPath())
                            .putExtra(FOLDER_NAME,
                                    mediaList.get(getAdapterPosition()).getFolderName())
                    );
                }
            });
        }
    }
}
