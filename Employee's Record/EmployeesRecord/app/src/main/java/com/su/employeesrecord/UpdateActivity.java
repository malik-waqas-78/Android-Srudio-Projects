package com.su.employeesrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String number=intent.getStringExtra("number");
        int id=intent.getIntExtra("id",-1);

        Employee emp=new Employee(id,name,number);
        if(id==-1){
            finish();
        }

        EditText empName,epmNumber;
        empName=findViewById(R.id.etName);
        epmNumber=findViewById(R.id.etMobileNumber);
        epmNumber.setText(emp.getEmpPhoneNo());
        empName.setText(emp.getEmpName());
        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emp.setEmpName(empName.getText().toString());
                emp.setEmpPhoneNo(epmNumber.getText().toString());

                InsertActivity.dbHelper.updateEmployee(emp);

                Toast.makeText(UpdateActivity.this,"Record Updated",Toast.LENGTH_SHORT).show();
            }
        });


    }
}