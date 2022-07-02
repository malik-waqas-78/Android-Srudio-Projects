package com.screen.mirror.Adapters.Images;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool;

import com.screen.mirror.Activities.Images.ImagesGridShowActivity_OKRA;
import com.screen.mirror.Models.MediaModel;
import com.screen.mirror.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.screen.mirror.Utils.Constants_CA.FOLDER_NAME;
import static com.screen.mirror.Utils.Constants_CA.FOLDER_PATH;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private final ArrayList<MediaModel> mediaList;
    private final Context context;
//    private final MediaSelectionInterface callbacks;

    public ImagesAdapter(ArrayList<MediaModel> mediaList, Context context) {
        this.mediaList = mediaList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_media_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.ViewHolder holder, int position) {
        MediaModel model = mediaList.get(position);
        holder.folderName.setText(model.getFolderName());
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        holder.subFolderRecyclerView.setLayoutManager(layoutManager);
        holder.subFolderRecyclerView.setAdapter(new SubFolderImagesAdapter(context, model.getFilesInFolder()));
        holder.linearLayout.setVisibility(model.getIsExpanded());
        holder.btnViewAll.setVisibility(model.getIsExpanded());
        holder.folderName.setText(model.getFolderName());
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    private int InvertState(int isExpanded) {
        return (int) Math.sqrt(Math.pow(isExpanded - GONE, 2));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView media_card_back;
        TextView folderName, btnViewAll;
        RecyclerView subFolderRecyclerView;
        LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            media_card_back = itemView.findViewById(R.id.media_card_back);
            folderName = itemView.findViewById(R.id.folderName);
            btnViewAll = itemView.findViewById(R.id.view_all);
            subFolderRecyclerView = itemView.findViewById(R.id.sub_folder_view);
            linearLayout = itemView.findViewById(R.id.linear_layout_recycler_view);

            btnViewAll.setOnClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    context.startActivity(new Intent(context, ImagesGridShowActivity_OKRA.class)
                            .putExtra(FOLDER_PATH,
                                    mediaList.get(getAdapterPosition()).getFolderPath())
                            .putExtra(FOLDER_NAME,
                                    mediaList.get(getAdapterPosition()).getFolderName())
                    );
                }
            });

            media_card_back.setOnClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    MediaModel currentMedia = mediaList.get(getAdapterPosition());
                    currentMedia.setIsExpanded(InvertState(currentMedia.getIsExpanded()));
                    notifyDataSetChanged();
                }
            });
        }
    }
}
