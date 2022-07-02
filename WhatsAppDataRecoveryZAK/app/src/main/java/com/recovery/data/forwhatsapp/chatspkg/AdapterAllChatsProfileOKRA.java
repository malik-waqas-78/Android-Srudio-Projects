package com.recovery.data.forwhatsapp.chatspkg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.activities.ActivityAllChatsProfilesOKRA;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class AdapterAllChatsProfileOKRA extends RecyclerView.Adapter  implements Filterable {

    ArrayList<AllChatsProfilesModelClassOKRA> cardView_profiles_modelClasses;
    ArrayList<AllChatsProfilesModelClassOKRA> cardView_profiles;

    ArrayList<ProfileModalClassOKRA> profileModalClassOKRAS;
    ArrayList<ProfileModalClassOKRA> example_profileModalClassOKRAS;

    CardViewChatsInterfaceOKRA itemClickListeners;
    LongClickInterfaceOKRA itemLongClickListeners;

    ArrayList<String> selectedRows=new ArrayList<>();
    ArrayList<String> selectedRowsMsgs=new ArrayList<>();
    Context context;

    public AdapterAllChatsProfileOKRA(ArrayList<AllChatsProfilesModelClassOKRA> cardView_profiles_modelClasses, Context context, ArrayList<ProfileModalClassOKRA> profileModalClassOKRAS) {
        this.cardView_profiles_modelClasses = cardView_profiles_modelClasses;
        this.context = context;
        cardView_profiles=cardView_profiles_modelClasses;
        this.profileModalClassOKRAS = profileModalClassOKRAS;
        example_profileModalClassOKRAS = profileModalClassOKRAS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_profils_okra, parent, false);
        return new Adapter_ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_ViewHolder row_view = (Adapter_ViewHolder) holder;

        ProfileModalClassOKRA row = profileModalClassOKRAS.get(position);
        row_view.chat_profile.setBackgroundColor(Color.WHITE);
        if(selectedRows.size()!=0&&selectedRows.contains(row.getProfile_name())){
//            row_view.chat_profile.setBackgroundColor(ContextCompat.getColor(context, R.color.selected));
            row_view.dot.setVisibility(View.VISIBLE);
            row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
        }else{
            row_view.chat_profile.setBackgroundColor(Color.WHITE);
            row_view.dot.setBackgroundResource(R.drawable.ic_unchek_icon);
            row_view.dot.setVisibility(View.GONE);
        }
        row_view.profile_id.setText(row.getProfile_name());
        row_view.profile_id.setSelected(true);
        row_view.last_msg.setText(row.getLast_msg());
        row_view.time_and_date.setText(row.getTime());
        byte[] bytearray=row.getBytearray();
        if(bytearray!=null&&bytearray.length!=0){
            Bitmap bitmap;
            ByteArrayInputStream inputStream=new ByteArrayInputStream(bytearray);
            bitmap=BitmapFactory.decodeStream(inputStream);
            row_view.profile.setImageBitmap(bitmap);
        }else{
            row_view.profile.setImageDrawable(context.getDrawable(R.drawable.ic_avatar));
        }
        row_view.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListeners.onShareClickListener(row.getProfile_name(),position);
            }
        });
        row_view.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //row_view.chat_profile.setBackgroundColor(ContextCompat.getColor(context, R.color.selected));
                selectedRows.add(row.getProfile_name());
                selectedRowsMsgs.add(row.getLast_msg()) ;
                itemClickListeners.onDeleteClickListener();
            }
        });
        row_view.chat_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedRows.size()!=0&&selectedRows.contains(row.getProfile_name())){
                    row_view.chat_profile.setBackgroundColor(Color.WHITE);
                    row_view.dot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    row_view.dot.setVisibility(View.GONE);
                    selectedRows.remove(row.getProfile_name());
                    selectedRowsMsgs.remove(row.getLast_msg());
                    if (selectedRows.size() == 0){
                        itemLongClickListeners.onLongClicked(false);
                    }
                }else if(selectedRows.size()!=0&&!selectedRows.contains(row.getProfile_name())){
//                    row_view.chat_profile.setBackgroundColor(ContextCompat.getColor(context, R.color.selected));
                    row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
                    row_view.dot.setVisibility(View.VISIBLE);
                    selectedRows.add(row.getProfile_name());
                    selectedRowsMsgs.add(row.getLast_msg()) ;
                }else{
                    itemClickListeners.onProfileClickListener(view, position);
                }
            }
        });
        row_view.chat_profile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(selectedRows.size()==0){

                    itemLongClickListeners.onLongClicked(true);
                    row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
                    row_view.dot.setVisibility(View.VISIBLE);
                    selectedRows.add(row.getProfile_name());
                    selectedRowsMsgs.add(row.getLast_msg());
                }
                return true;
            }
        });
        //Log.d("url", "onBindViewHolder: "+row.getUrl());
//        File f=new File(row.getUrl());
//        if(f!=null&&f.exists()&&f.length()!=0){
//            Glide.with(context).load(row.getUrl()).into(row_view.profile);
//        }else{
//            row_view.profile.setImageDrawable(context.getDrawable(R.drawable.ic_emoji_icon));
//        }

//        byte[] bitmapData=row.getBytearray();
//        if(bitmapData!=null)
//        {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
//            row_view.profile.setImageBitmap(bitmap);
//        }
        //row_view.profile.setImageIcon(row.getIcon());
    }

    public void setItemClickListeners(CardViewChatsInterfaceOKRA itemClickListeners) {
        this.itemClickListeners = itemClickListeners;

    }

    public void setLongClickListeners(ActivityAllChatsProfilesOKRA cardView_messages_activity) {
        this.itemLongClickListeners = cardView_messages_activity;
    }
    @Override
    public int getItemCount() {
        return profileModalClassOKRAS.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public ArrayList<String> getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(ArrayList<String> selectedRows) {
        this.selectedRows = selectedRows;
    }

    public ArrayList<String> getSelectedRowsMsgs() {
        return selectedRowsMsgs;
    }

    public void setSelectedRowsMsgs(ArrayList<String> selectedRowsMsgs) {
        this.selectedRowsMsgs = selectedRowsMsgs;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<ProfileModalClassOKRA> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(example_profileModalClassOKRAS);
                } else {
                    String filterPattern = constraint.toString();
                    for (ProfileModalClassOKRA item : example_profileModalClassOKRAS) {
                        if (item.getProfile_name().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                profileModalClassOKRAS = filteredList;
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    private class Adapter_ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile, delete, share;
        TextView profile_id, last_msg, time_and_date;
        RelativeLayout chat_profile;
        CardView cardView;
        ImageView dot;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile_pic);
            delete = itemView.findViewById(R.id.deleteprofile);
            share = itemView.findViewById(R.id.shareprofile);
            profile_id = itemView.findViewById(R.id.id_name);
            last_msg = itemView.findViewById(R.id.last_msg);
            time_and_date = itemView.findViewById(R.id.time);
            cardView=itemView.findViewById(R.id.cardview);
            chat_profile = itemView.findViewById(R.id.chat_profile);
            dot=itemView.findViewById(R.id.dotselect1);
        }
    }
}
