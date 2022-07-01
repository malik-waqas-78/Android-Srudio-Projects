package com.example.whatsappchatlocker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappchatlocker.R;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerMainViewHolder> {
    View view;
    LayoutInflater inflater;
    Context context;
    ArrayList<Record> arrayList;
    Boolean lock;
    Realm realm;
    RealmHelper realmHelper;
    private SharedPreferences preferences;


    public RecyclerAdapter(Context context, ArrayList<Record> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.number, parent, false);
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm, context);
        preferences=this.context.getSharedPreferences("Lock", 0);
        return new RecyclerMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerMainViewHolder holder, int position) {
        boolean Checkbox = preferences.getBoolean("checkbox", false);
        if (!Checkbox){
        Random mRandom = new Random();
            holder.imageView.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
        int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        holder.textView.setText(arrayList.get(position).getName());

        lock = arrayList.get(position).getLock();
        if (lock) {
            holder.imageView.setBackgroundResource(R.drawable.lock);
        } else {
            holder.imageView.setBackgroundResource(R.drawable.unlock);
        }
        holder.icon.setText(arrayList.get(position).getName().substring(0, 1));
        holder.icon.setBackgroundColor(color);
        }else
            {
                Random mRandom = new Random();
                int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
                holder.textView.setText(arrayList.get(position).getName());
                holder.imageView.setVisibility(View.GONE);
                holder.checkbox.setVisibility(View.VISIBLE);
                holder.icon.setText(arrayList.get(position).getName().substring(0, 1));
                holder.icon.setBackgroundColor(color);
            }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerMainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView, icon;
        ImageView imageView, delete;
        CheckBox  checkbox;



        public RecyclerMainViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            icon = itemView.findViewById(R.id.icon);
            imageView = itemView.findViewById(R.id.lock);
           delete = itemView.findViewById(R.id.delete);
            checkbox = itemView.findViewById(R.id.check);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int i = getAdapterPosition();
                      String name = arrayList.get(i).getName();
                      boolean bolean = arrayList.get(i).getLock();
                      if (bolean) {
                          imageView.setBackgroundResource(R.drawable.unlock);
                          realmHelper.Update(name, false);
                          Toast.makeText(context, "UnLocked Successfully", Toast.LENGTH_SHORT).show();
                      }
                      if (!bolean) {
                          imageView.setBackgroundResource(R.drawable.lock);
                          realmHelper.Update(name, true);
                          Toast.makeText(context, "Locked Successfully", Toast.LENGTH_SHORT).show();
                      }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int i = getAdapterPosition();
                    String name = arrayList.get(i).getName();
                    realmHelper.DeleteData(name);
                    arrayList.remove(i);
                    notifyItemRemoved(i);
                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
