package com.example.deviceinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.deviceinfo.R;
import com.example.deviceinfo.models.General_info_Model;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class General_info_Adapter extends RecyclerView.Adapter<General_info_Adapter.ViewHolder> {

    ArrayList<General_info_Model> infos;
    Context context;

    public General_info_Adapter(ArrayList<General_info_Model> infos, Context context) {
        this.infos = infos;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public General_info_Adapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item_general, parent, false);
        General_info_Adapter.ViewHolder viewHolder = new General_info_Adapter.ViewHolder( view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull General_info_Adapter.ViewHolder holder, int position) {
        holder.textName.setText(infos.get(position).getName());
        holder.textInfo.setText(infos.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textInfo;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textName=itemView.findViewById(R.id.textTitle);
            textInfo=itemView.findViewById(R.id.textInfo);


        }
    }
}
