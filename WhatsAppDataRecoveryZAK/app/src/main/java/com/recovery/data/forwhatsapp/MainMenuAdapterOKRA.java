package com.recovery.data.forwhatsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainMenuAdapterOKRA extends RecyclerView.Adapter<MainMenuAdapterOKRA.ViewHolder> {

    ArrayList<MainMenuModalOKRA> mainMenuModalOKRAS =new ArrayList<MainMenuModalOKRA>();
    Context context;
    OnItemClickListner listner;
    public MainMenuAdapterOKRA(ArrayList<MainMenuModalOKRA> mainMenuModalOKRAS, Context context) {
        this.mainMenuModalOKRAS = mainMenuModalOKRAS;
        this.context = context;
    }
    public  interface OnItemClickListner{
        void onItemClick(View v,int pos);
    }
    public  void setOnItemClickListener(MainMenuAdapterOKRA.OnItemClickListner onItemClickListener){
        this.listner=onItemClickListener;
    }
    @NonNull
    @Override
    public MainMenuAdapterOKRA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_okra, parent, false);
        MainMenuAdapterOKRA.ViewHolder viewHolder = new MainMenuAdapterOKRA.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuAdapterOKRA.ViewHolder holder, int position) {
        ImageView imageView=holder.icon;
        TextView textView=holder.text;
        imageView.setImageResource(mainMenuModalOKRAS.get(position).getIcon());
        textView.setText(mainMenuModalOKRAS.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return mainMenuModalOKRAS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon=(ImageView)itemView.findViewById(R.id.menuIcon);
            text=(TextView)itemView.findViewById(R.id.menuText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n=getAdapterPosition();
                    if(listner!=null && n!=-1){
                        listner.onItemClick(v,n);
                    }
                }
            });

        }
    }
}
