package com.example.notesorganizer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesorganizer.modelclass.ModelClass;
import com.example.notesorganizer.R;
import com.example.notesorganizer.activities.FullDataShowActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShowDatabaseAdapter extends RecyclerView.Adapter<ShowDatabaseAdapter.MyViewHolder>/* implements Filterable*/ {
    Context context;
    ArrayList<ModelClass> modelClassArrayList;
   // ArrayList<ModelClass> fullmodelClassArrayList;
   public static class MyViewHolder extends RecyclerView.ViewHolder {
       TextView textView_main, details_text, date_text;
       ConstraintLayout constraintLayout;

       public MyViewHolder(@NonNull View itemView) {
           super(itemView);
           textView_main = itemView.findViewById(R.id.tv_title);
           details_text = itemView.findViewById(R.id.tv_content);
           date_text = itemView.findViewById(R.id.tv_date);
           constraintLayout = itemView.findViewById(R.id.title_details_constraint);
       }
   }

    public ShowDatabaseAdapter(Context context, ArrayList<ModelClass> modelClassArrayList) {
        this.context = context;
        this.modelClassArrayList =modelClassArrayList;
        //fullmodelClassArrayList = new ArrayList<>(modelClassArrayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView_main.setText(modelClassArrayList.get(position).getTitle());
        holder.details_text.setText(modelClassArrayList.get(position).getDetails());
        holder.date_text.setText(modelClassArrayList.get(position).getDate());
        int position1 = modelClassArrayList.get(position).getId();
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FullDataShowActivity.class);
                i.putExtra("numposition", position1);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

   /* @Override
    public Filter getFilter() {
        return examplefilter;
    }*/


    /*  private Filter examplefilter = new Filter() {
          @Override
          protected FilterResults performFiltering(CharSequence constraint) {
              ArrayList<ModelClass> filteredlist = new ArrayList<>();
              if (constraint == null || constraint.length() == 0) {
                  filteredlist.addAll(fullmodelClassArrayList);
              } else {
                  String filterPattern = constraint.toString().toLowerCase().trim();
                  for (ModelClass item : fullmodelClassArrayList) {
                      if (item.getTitle().toLowerCase().contains(filterPattern)) {
                          filteredlist.add(item);
                      }
                  }
              }
              FilterResults filterResults = new FilterResults();
              filterResults.values = filteredlist;
              return filterResults;
          }
          @Override
          protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
              modelClassArrayList.clear();
              modelClassArrayList.addAll((Collection<? extends ModelClass>) filterResults.values);
              notifyDataSetChanged();
          }
//      };*/
//    public void filterlist(ArrayList<ModelClass> modelClassArrayList1) {
//        this.modelClassArrayList = new ArrayList<>(modelClassArrayList1);
//        notifyDataSetChanged();
//    }
}