package com.video.trimmer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.trimmer.R;
import com.video.trimmer.interfaces.MenuItemClickListener;
import com.video.trimmer.modelclasses.MainMenuModal;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {
    Context context;
    ArrayList<MainMenuModal> mainMenuModals=new ArrayList<>();
    MenuItemClickListener menuItemClickListener;

    public MainMenuAdapter(Context context, ArrayList<MainMenuModal> mainMenuModals) {
        this.context = context;
        this.mainMenuModals = mainMenuModals;
    }
    public void onMenuItemClick(MenuItemClickListener menuItemClickListener){
        this.menuItemClickListener=menuItemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public MainMenuAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_recycler_item, parent, false);
        MainMenuAdapter.ViewHolder viewHolder = new MainMenuAdapter.ViewHolder (view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MainMenuAdapter.ViewHolder holder, int position) {

        holder.menuText.setText(mainMenuModals.get(position).getMenuItemName());
        holder.menuText.setSelected(true);
        holder.cardView.setBackgroundResource(mainMenuModals.get(position).getMenuItemColor());
        Glide.with(context).load(mainMenuModals.get(position).getMenuItemIcon())
                .into(holder.menuIcon);

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuItemClickListener.onMenuItemClick(position);
            }
        });

        

    }

    @Override
    public int getItemCount() {
        return mainMenuModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cardView;
        ImageView menuIcon;
        TextView menuText;
        FrameLayout frameLayout;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.relMenuIcon);
            menuIcon=itemView.findViewById(R.id.menuIcon);
            menuText=itemView.findViewById(R.id.menuItemName);
            frameLayout=itemView.findViewById(R.id.frame);
        }
    }
}
