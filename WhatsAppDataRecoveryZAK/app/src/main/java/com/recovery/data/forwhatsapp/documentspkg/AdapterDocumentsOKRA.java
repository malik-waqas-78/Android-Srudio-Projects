package com.recovery.data.forwhatsapp.documentspkg;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.activities.ActivityDocumentsOKRA;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterDocumentsOKRA extends RecyclerView.Adapter implements Filterable {
    private static final String TAG = "92727";
    ArrayList<File> document_files;
    ArrayList<File> example_document_files;
    Context context;
    ArrayList<String> selectedRows = new ArrayList<>();
    LongClickInterfaceOKRA itemLongClickListeners;

    public void setDocuments_url_interface(DocumentsUrlInterfaceOKRA documents_url_interface) {
        this.documents_url_interface = documents_url_interface;
    }
    public void setLongClickListeners(ActivityDocumentsOKRA documents_activity) {
        this.itemLongClickListeners = documents_activity;
    }

    DocumentsUrlInterfaceOKRA documents_url_interface;

    public AdapterDocumentsOKRA(ArrayList<File> document_files, Context context) {
        this.document_files = document_files;
        this.context = context;
        example_document_files=document_files;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<File> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(example_document_files);
                } else {
                    String filterPattern = constraint.toString();
                    for (File item : example_document_files) {
                        if (item.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                document_files = filteredList;
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
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_documents_okra, parent, false);
        return new Adapter_ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_ViewHolder row_view = (Adapter_ViewHolder) holder;
        File imageFile = document_files.get(position);
        if (selectedRows.size() != 0 && selectedRows.contains(imageFile.getAbsolutePath())) {
           // row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
            row_view.relativeChecked.setVisibility(View.VISIBLE);
        } else {
            row_view.frameLayout.setBackgroundColor(Color.TRANSPARENT);
            row_view.relativeChecked.setVisibility(View.GONE);
        }

        row_view.documentname.setText(imageFile.getName());
        row_view.documentname.setSelected(true);
        if((imageFile.length()/1024)/1000==0.0){
            float file_size = (imageFile.length()/1024);
            row_view.docSize.setText(file_size+" KB");
        }else {
            float file_size = (imageFile.length() / 1024) / 1000;
            row_view.docSize.setText(file_size+" MB");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(imageFile.lastModified()));
        row_view.docDate.setText(dateString+"");
//        row_view.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(Intent.ACTION_VIEW);
////                intent.setDataAndType(Uri.fromFile(imageFile), "application/pdf");
////                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
////                context.startActivity(intent);
//                documents_url_interface.setUrlandLaunchInterface(imageFile.getAbsolutePath());
//            }
//        });
        row_view.frameLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (selectedRows.size() != 0 && selectedRows.contains(imageFile.getAbsolutePath())) {
                    row_view.frameLayout.setBackgroundColor(Color.TRANSPARENT);
                    row_view.relativeChecked.setVisibility(View.GONE);
                    selectedRows.remove(imageFile.getAbsolutePath());
                    if (selectedRows.size() == 0) {
                        itemLongClickListeners.onLongClicked(false);
                    }
                } else if (selectedRows.size() != 0 && !selectedRows.contains(imageFile.getAbsolutePath())) {
                    //row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
                    row_view.relativeChecked.setVisibility(View.VISIBLE);
                    selectedRows.add(imageFile.getAbsolutePath());
                } else {
                    documents_url_interface.setUrlandLaunchInterface(imageFile.getAbsolutePath());
                }
            }
        });
        row_view.frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View view) {
                if (selectedRows.size() == 0) {
                    itemLongClickListeners.onLongClicked(true);
                    //row_view.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
                    row_view.relativeChecked.setVisibility(View.VISIBLE);
                    selectedRows.add(imageFile.getAbsolutePath());
                }
                return true;
            }
        });
//        row_view.imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                documents_url_interface.onLongClickListener(position,imageFile.getAbsolutePath());
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return document_files.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public ArrayList<String> getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(ArrayList<String> selectedRows) {
        this.selectedRows = selectedRows;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    private class Adapter_ViewHolder extends RecyclerView.ViewHolder {

        TextView documentname;
        ImageView imageView;
        FrameLayout frameLayout;
        TextView docSize,docDate;
        RelativeLayout relativeChecked;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            documentname = (TextView) itemView.findViewById(R.id.documentname);
            imageView = itemView.findViewById(R.id.imageView);
            frameLayout = itemView.findViewById(R.id.framelayout);
            docSize=itemView.findViewById(R.id.docSize);
            docDate=itemView.findViewById(R.id.docDate);
            relativeChecked=itemView.findViewById(R.id.checkedrel);
        }
    }
}
