package com.su.employeesrecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.security.spec.ECParameterSpec;
import java.util.ArrayList;

public class ViewAndDeleteActivity extends AppCompatActivity {
    RecordsAdapter adapter;
    ArrayList<Employee> employees=new ArrayList<>();
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_delete);
        rv=findViewById(R.id.rvRecord);
        adapter=new RecordsAdapter(employees,this);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        employees.clear();
        employees.addAll(InsertActivity.dbHelper.getAllEmployee());
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}