package com.example.whatsappdatarecovery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.activities.FullDataShowActivity;
import com.example.whatsappdatarecovery.database.MyRealmHelper;
import com.example.whatsappdatarecovery.interfaces.MyInterfaceClass;
import com.example.whatsappdatarecovery.modelclass.ModelClass;

import java.net.URISyntaxException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Viewholder> {
    private static final String TAG = "92270";

    Realm realm;
    private static final int SCHEMA_V_NOW = 2;// change schema version if any change happened in schema
    /*  Realm realm;
      MyRealmHelper relmHelper;*/
    ArrayList<ModelClass> itemslist;
    ModelClass modelClass;
    private MyInterfaceClass interfaceClass;
    int position1, position2;
    Context context;
    Intent i;

    {
        try {
            i = Intent.getIntent("");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public ChatAdapter(@NonNull Context context, ArrayList<ModelClass> itemslist/*, Realm realm, MyInterfaceClass interfaceClass*/) {
        this.context = context;
        this.itemslist = itemslist;
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        position1 = i.getIntExtra("numposition", 0);
        modelClass = realm.where(ModelClass.class).equalTo("id", position1).findFirst();
     /*   this.interfaceClass = interfaceClass;
        this.realm = realm*/
        ;
        /*realm = Realm.getInstance(getDefaultConfig());
        relmHelper = new MyRealmHelper(realm, context);*/
    }

    public static RealmConfiguration getDefaultConfig() {
        return new RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_V_NOW)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    @NonNull
    @Override
    public ChatAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new Viewholder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_cardview_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.Viewholder holder, final int position) {
        holder.textView_name.setText(itemslist.get(position).getName());
        Log.d(TAG, "onBindViewHolder112: " + itemslist.get(position).getName());
        holder.textView_details.setText(itemslist.get(position).getDetails());
        holder.textView_date.setText(itemslist.get(position).getDate());
        int position2 = itemslist.get(position).getId();
        holder.chat_constraint_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FullDataShowActivity.class);
                i.putExtra("numposition", position2);
                context.startActivity(i);
            }
        });
//      holder.profile_iamges.setImageResource(itemslist.get(position).getImage_path());
        Log.d(TAG, "onBindViewHolder_date: " + itemslist.get(position).getDate());
        holder.btn_delete.setOnClickListener(v -> {
            /*realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    modelClass.deleteFromRealm();
                    itemslist.remove(position);
                    notifyDataSetChanged();
                }
            });*/
           /* interfaceClass.deletedata(itemslist.get(position).getId());
            itemslist.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, itemslist.size());
                notifyDataSetChanged();*/
        });
        holder.btn_share.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = itemslist.get(position).getDetails();
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });
    }

    @Override
    public int getItemCount() {
        return itemslist.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView textView_name;
        TextView textView_details;
        ImageButton btn_delete, btn_share;
        ImageView profile_iamges;
        TextView textView_date;
        ConstraintLayout chat_constraint_layout;

        public Viewholder(@NonNull View view) {
            super(view);
            textView_name = view.findViewById(R.id.textview_name);
            textView_details = view.findViewById(R.id.textview_details);
            btn_delete = view.findViewById(R.id.btn_delete);
            profile_iamges = view.findViewById(R.id.profile_image);
            textView_date = view.findViewById(R.id.total_time);
            btn_share = view.findViewById(R.id.btn_share);
            chat_constraint_layout = view.findViewById(R.id.chat_constraint_layout);
        }
    }
}