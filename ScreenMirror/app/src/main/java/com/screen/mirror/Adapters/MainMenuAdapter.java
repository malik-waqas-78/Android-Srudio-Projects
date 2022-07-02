package com.screen.mirror.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.screen.mirror.Interfaces.MainMenuInterface;
import com.screen.mirror.Models.Main_Menu_Model;
import com.screen.mirror.R;

import java.util.ArrayList;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {
    private final ArrayList<Main_Menu_Model> menu_list;
    private final Context context;
    private final MainMenuInterface mainMenuInterface;

    public MainMenuAdapter(ArrayList<Main_Menu_Model> menu_list, Context context, MainMenuInterface mainMenuInterface) {
        this.menu_list = menu_list;
        this.context = context;
        this.mainMenuInterface = mainMenuInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_menu_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuAdapter.ViewHolder holder, int position) {
        Main_Menu_Model model = menu_list.get(position);

        Glide.with(context).load(model.getIcon()).into(holder.icon);
        holder.name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return menu_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout menu_card_back;
        ImageView icon;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            menu_card_back = itemView.findViewById(R.id.main_menu_item_back);
            icon = itemView.findViewById(R.id.main_menu_icon);
            name = itemView.findViewById(R.id.main_menu_name);
            name.setSelected(true);

            menu_card_back.setOnClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    mainMenuInterface.OnClick(getAdapterPosition());
                }
            });
        }
    }
}
