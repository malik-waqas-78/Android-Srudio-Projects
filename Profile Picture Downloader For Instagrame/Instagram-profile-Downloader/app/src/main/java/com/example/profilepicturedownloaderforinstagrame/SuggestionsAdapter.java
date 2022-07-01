package com.example.profilepicturedownloaderforinstagrame;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profilepicturedownloaderforinstagrame.models.ModelClass;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ViewHolder> {
    Context context;
    ArrayList<ModelClass> modelClassArrayList = new ArrayList<>();

    public SuggestionsAdapter(Context context, ArrayList<ModelClass> modelClassArrayList) {
        this.modelClassArrayList = modelClassArrayList;
        this.context = context;
        Log.d(TAG, "SuggestionsAdapter: " + modelClassArrayList.size());
    }

    @NonNull
    @Override
    public SuggestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.suggestion_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionsAdapter.ViewHolder holder, int position) {
        holder.textUserName.setText(modelClassArrayList.get(position).getName());
        holder.text_fullName.setText(modelClassArrayList.get(position).getUsername());
        holder.user_profile_imageview.setImageURI(Uri.parse(modelClassArrayList.get(position).getProfile_imge()));
        //holder.user_profile_imageview.setImageResource(Integer.parseInt(modelClassArrayList.get(position).getProfile_imge()));
        Log.d(TAG, "getname12: " + modelClassArrayList.get(position).getProfile_imge());
    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_fullName, textUserName;
        ImageView user_profile_imageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_fullName = itemView.findViewById(R.id.full_name);
            textUserName = itemView.findViewById(R.id.username);
            user_profile_imageview = itemView.findViewById(R.id.user_profile_image);
        }
    }
}