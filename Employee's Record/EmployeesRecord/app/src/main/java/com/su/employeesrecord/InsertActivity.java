package com.su.employeesrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsertActivity extends AppCompatActivity {

    EditText empName,empNumber;
    public static DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        empName=findViewById(R.id.etName);
        empNumber=findViewById(R.id.etMobileNumber);
        dbHelper=new DBHelper(this);
        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=empName.getText().toString();
                if(name.trim().length()==0){
                    empName.setError("Name can't be empty.");
                    return;
                }
                String number=empNumber.getText().toString().trim();
                if(number.length()==0){
                    empName.setError("Number can't be empty.");
                    return;
                }

                dbHelper.addEmployee(new Employee(name,number));

                empNumber.setText("");
                empName.setText("");
                Toast.makeText(InsertActivity.this,"Data inserted Successfully",Toast.LENGTH_SHORT).show();

            }
        });

        findViewById(R.id.btnViewRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InsertActivity.this,ViewAndDeleteActivity.class);
                startActivity(intent);
            }
        });

    }
}