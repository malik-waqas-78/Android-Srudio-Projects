package com.ash360.cool.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ash360.cool.Interfaces.Door_Grid_interface;
import com.ash360.cool.Models.DoorModel;
import com.ash360.cool.R;
import com.ash360.cool.Utils.Shared_Pref_DoorLock;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter_Door_Selection extends RecyclerView.Adapter<Adapter_Door_Selection.ViewHolder> {

    private final ArrayList<DoorModel> mDataSet;
    private final Context context;
    private final Door_Grid_interface door_grid_interface;
    private final Shared_Pref_DoorLock shared_pref_doorLock;

    public Adapter_Door_Selection(ArrayList<DoorModel> dataSet, Context context, Door_Grid_interface door_grid_interface) {
        mDataSet = dataSet;
        this.context = context;
        shared_pref_doorLock = new Shared_Pref_DoorLock(context);
        this.door_grid_interface = door_grid_interface;
    }

    @NonNull
    @Override
    public Adapter_Door_Selection.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_row_door_selection, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Door_Selection.ViewHolder holder, int position) {
        DoorModel currentItem = mDataSet.get(position);
        Glide.with(context).load(currentItem.getIcon()).into(holder.doorIcon);
        holder.selection.setVisibility(currentItem.getIsSelected());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private int invertOverlay(int isSelected) {
        return (int) Math.sqrt(Math.pow(isSelected - 8, 2));
    }

    private void DeleteOtherSelection() {
        for (DoorModel model : mDataSet) {
            model.setIsSelected(8);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView doorIcon, selection;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doorIcon = itemView.findViewById(R.id.door_layout);
            selection = itemView.findViewById(R.id.selection_overlay);

            doorIcon.setOnClickListener(v -> {
                if (getAdapterPosition() < mDataSet.size() && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    DoorModel door = mDataSet.get(getAdapterPosition());
                    door_grid_interface.OnItemSelection(door);
                    DeleteOtherSelection();
                    door.setIsSelected(invertOverlay(door.getIsSelected()));
                    notifyDataSetChanged();
                }
            });
        }
    }
}
