package com.su.employeesrecord;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {

    private ArrayList<Employee> employees=new ArrayList<>();
    private Context context;

    public RecordsAdapter(ArrayList<Employee> employees, Context context) {
        this.employees = employees;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_records,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(employees.get(position).getEmpName());
        holder.number.setText(employees.get(position).getEmpPhoneNo());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertActivity.dbHelper.deleteEmployee(employees.get(holder.getAdapterPosition()));
                employees.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                Toast.makeText(context,"Record Removed",Toast.LENGTH_SHORT).show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UpdateActivity.class);
                intent.putExtra("name",employees.get(holder.getAdapterPosition()).getEmpName());
                intent.putExtra("number",employees.get(holder.getAdapterPosition()).getEmpPhoneNo());
                intent.putExtra("id",employees.get(holder.getAdapterPosition()).getEmpId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,number;
        ImageView delete,edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tvName);
            number=itemView.findViewById(R.id.tvNumber);
            delete=itemView.findViewById(R.id.ivDelete);
            edit=itemView.findViewById(R.id.ivEdit);
        }
    }
}
