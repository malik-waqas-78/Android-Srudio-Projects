package com.recovery.data.forwhatsapp.chatspkg;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.activities.ActivityMessagesSingleOKRA;

import java.util.ArrayList;

public class AdapterMessagesSingleOKRA extends RecyclerView.Adapter implements Filterable {
    ArrayList<MessagesSingleModelClassOKRA> cardView_messages_modelClasses;
    ArrayList<MessagesSingleModelClassOKRA> cardView_messages;
    ArrayList<ChatsModalClassforMessageOnlyOKRA> classforMessageOnlies;
    ArrayList<ChatsModalClassforMessageOnlyOKRA> example_classforMessageOnlies;
    CardViewChatsInterfaceOKRA itemClickListeners;
    LongClickInterfaceOKRA itemLongClickListeners;
    Context context;
    ArrayList<String> selectedRows = new ArrayList<>();
    ArrayList<String> selectedTextMsgs = new ArrayList<>();

    public AdapterMessagesSingleOKRA(@NonNull Context context, ArrayList<MessagesSingleModelClassOKRA> cardView_chats_modelClasses, ArrayList<ChatsModalClassforMessageOnlyOKRA> chatsModalClassforMessageOnlies) {
        this.context = context;
        this.cardView_messages_modelClasses = cardView_chats_modelClasses;
        cardView_messages=new ArrayList<>(cardView_chats_modelClasses);
        this.classforMessageOnlies=chatsModalClassforMessageOnlies;
        example_classforMessageOnlies=chatsModalClassforMessageOnlies;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_msgs_okra, parent, false);
        return new Adapter_ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_ViewHolder row_view = (Adapter_ViewHolder) holder;
        ChatsModalClassforMessageOnlyOKRA row = classforMessageOnlies.get(position);
        if (selectedRows.size() != 0 && selectedRows.contains(row.getId())) {
            //row_view.constraintLayout.setBackgroundResource(R.drawable.ic_bubble);
            row_view.dot.setVisibility(View.VISIBLE);
            row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
        } else {
            row_view.constraintLayout.setBackgroundResource(R.drawable.ic_messageback);
            row_view.dot.setBackgroundResource(R.drawable.ic_unchek_icon);
            row_view.dot.setVisibility(View.GONE);
        }
        row_view.msg.setText(row.getMsgText());
        row_view.msg.setSelected(true);
        row_view.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListeners.onShareClickListener(row.getMsgText(), position);
            }
        });
        row_view.time_and_date.setText(row.getTime());
        row_view.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedRows.size() != 0 && selectedRows.contains(row.getId())) {
                    row_view.constraintLayout.setBackgroundResource(R.drawable.ic_messageback);
                    row_view.dot.setBackgroundResource(R.drawable.ic_unchek_icon);
                    row_view.dot.setVisibility(View.GONE);
                    selectedRows.remove(row.getId());
                    selectedTextMsgs.remove(row.getMsgText());

                    if (selectedRows.size() == 0){
                        itemLongClickListeners.onLongClicked(false);
                    }
                } else if (selectedRows.size() != 0 && !selectedRows.contains(row.getId())) {
                    //row_view.constraintLayout.setBackgroundResource(R.drawable.ic_bubble);
                    row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
                    row_view.dot.setVisibility(View.VISIBLE);
                    selectedRows.add(row.getId());
                    selectedTextMsgs.add(row.getMsgText());
                }
            }
        });
        row_view.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (selectedRows.size() == 0) {
                    itemLongClickListeners.onLongClicked(true);
                    //row_view.constraintLayout.setBackgroundResource(R.drawable.ic_bubble);
                    row_view.dot.setBackgroundResource(R.drawable.ic_chek_icon);
                    row_view.dot.setVisibility(View.VISIBLE);
                    selectedRows.add(row.getId());
                    selectedTextMsgs.add(row.getMsgText());
                }
                return true;
            }
        });
        //row_view.profile.setImageIcon(row.getIcon());
        //bind data with ui
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return classforMessageOnlies.size();
    }

    public void setItemClickListeners(CardViewChatsInterfaceOKRA itemClickListeners) {
        this.itemClickListeners = itemClickListeners;
    }

    public void setLongClickListeners(ActivityMessagesSingleOKRA cardView_messages_activity) {
        this.itemLongClickListeners = cardView_messages_activity;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<ChatsModalClassforMessageOnlyOKRA> filteredList = new ArrayList<ChatsModalClassforMessageOnlyOKRA>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(example_classforMessageOnlies);
                } else {
                    String filterPattern = constraint.toString();
                    for (ChatsModalClassforMessageOnlyOKRA item : example_classforMessageOnlies) {
                        if (item.getMsgText().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                classforMessageOnlies = filteredList;
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
        TextView profile_id, msg, time_and_date;
        ConstraintLayout constraintLayout;
        ImageView dot;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            share = itemView.findViewById(R.id.share);
            msg = itemView.findViewById(R.id.last_msg);
            time_and_date = itemView.findViewById(R.id.time);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            dot=itemView.findViewById(R.id.dotselect2);
        }
    }

    public ArrayList<String> getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(ArrayList<String> selectedRows) {
        this.selectedRows = selectedRows;
    }

    public ArrayList<String> getSelectedTextMsgs() {
        return selectedTextMsgs;
    }

    public void setSelectedTextMsgs(ArrayList<String> selectedTextMsgs) {
        this.selectedTextMsgs = selectedTextMsgs;
    }
}

