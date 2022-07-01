package com.example.instalockerappsss.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalockerappsss.R;
import com.example.instalockerappsss.database.RealmHelper;
import com.example.instalockerappsss.fragment.HomeFragment;
import com.example.instalockerappsss.modelclass.ModelClass;
import com.example.instalockerappsss.modelclass.SelectedModelClass;

import java.util.ArrayList;

import io.realm.Realm;

public class LockerAdapterClass extends RecyclerView.Adapter<LockerAdapterClass.ViewHolder> {
    Context context;
    ArrayList<ModelClass> modelClassArrayList = new ArrayList<>();
    Realm realm;
    RealmHelper realmHelper;
    ArrayList<String> selectedModelClassArrayList = new ArrayList<>();
    boolean activateTXT;

    public LockerAdapterClass(Context context, ArrayList<ModelClass> modelClassArrayList) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
    }
    public LockerAdapterClass(Context context, ArrayList<ModelClass> modelClassArrayList, boolean activateTXT) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
        this.activateTXT = activateTXT;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.locker_adapter_layout, null);
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm, context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.locker_username.setText(modelClassArrayList.get(i).getName());
        holder.locker_date.setText(modelClassArrayList.get(i).getDate());
        holder.user_firstLetter.setText(modelClassArrayList.get(i).getName_firstLetter());
      /*  if (modelClassArrayList.get(i).visible) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        if (selectedModelClassArrayList.contains(modelClassArrayList.get(i).getName())) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }*/
        if (activateTXT) {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else {
            holder.checkBox.setVisibility(View.GONE);
        }
        //holder.checkBox.setChecked(modelClassArrayList.get(i).isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                /*ModelClass modelClass = modelClassArrayList.get(i);
                realmHelper.update_checked(modelClass.getName(), b);
                if (b && !selectedModelClassArrayList.contains(modelClass.getName())) {
                    selectedModelClassArrayList.add(modelClassArrayList.get(i).getName());
                } else if (!b && selectedModelClassArrayList.contains(modelClass.getName())) {
                    selectedModelClassArrayList.remove(modelClassArrayList.get(i).getName());
                }*/
                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(true);
                    HomeFragment.MakeSelection(holder.checkBox, i);
                } else {
                    holder.checkBox.setChecked(false);
                    HomeFragment.MakeSelection(holder.checkBox, i);
                }
            }
        });
        boolean locked_confirm = modelClassArrayList.get(i).getLock();
        if (locked_confirm) {
            holder.btn_lock.setBackgroundResource(R.drawable.ic_locked);
        }
        if (!locked_confirm) {
            holder.btn_lock.setBackgroundResource(R.drawable.ic_locked_checked);
        }
        holder.btn_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = modelClassArrayList.get(i).getName();
                boolean checked_lock = modelClassArrayList.get(i).getLock();
                if (checked_lock) {
                    holder.btn_lock.setBackgroundResource(R.drawable.ic_locked_checked);
                    realmHelper.Update(name, false);
                }
                if (!checked_lock) {
                    holder.btn_lock.setBackgroundResource(R.drawable.ic_locked);
                    realmHelper.Update(name, true);
                }
            }
        });
    }
    public void RemoveItem(ArrayList<ModelClass> selectionList) {
        for (int i = 0; i < selectionList.size(); i++) {
/*
            realmHelper.deleteData(selectionList.get(i).getUserName());
*/
            modelClassArrayList.remove(selectionList.get(i));
            Log.d("TAG", "RemoveItem: "+selectionList.get(i).getUserName());
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView locker_username, locker_date, user_firstLetter;
        ImageButton btn_lock;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locker_username = itemView.findViewById(R.id.locker_username);
            locker_date = itemView.findViewById(R.id.locker_date);
            user_firstLetter = itemView.findViewById(R.id.user_firstLetter);
            btn_lock = itemView.findViewById(R.id.btn_lock);
            checkBox = itemView.findViewById(R.id.ch_selected);
        }
    }

    public ArrayList<String> getSelectedList() {
        return selectedModelClassArrayList;
    }

    public void setSelected() {
        selectedModelClassArrayList = new ArrayList<>();
    }

    public ArrayList<ModelClass> getListItems() {
        return modelClassArrayList;
    }

    public void removeItem(ArrayList<ModelClass> selectionList) {
        for (int i = 0; i < selectionList.size(); i++) {
            modelClassArrayList.remove(selectionList.get(i));
            notifyDataSetChanged();
        }
    }
}