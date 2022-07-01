package com.example.hissabbookapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hissabbookapp.MyApplication;
import com.example.hissabbookapp.R;
import com.example.hissabbookapp.realmmodals.RecordDataRealm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class ActivityCashOut extends AppCompatActivity {
    EditText editAmount,editRemark;
    Button btnSave,btnAddMore;
    TextView date,time;
    Realm realm;
    RecordDataRealm recordDataRealm;
    int balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out);
        initComponents();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        date.setText(currentDate);
        time.setText(currentTime);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount="- "+editAmount.getText();
                balance=MainActivity.netBalance-Integer.parseInt(amount.substring(2,amount.length()));
                String remarks=editRemark.getText().toString();
                String id=System.currentTimeMillis()+"";
                recordDataRealm=new RecordDataRealm(remarks,amount,currentTime,balance+"",id,currentDate,"out",currentDate.substring(0,1),currentDate.substring(3,4),currentDate.substring(6,9));
                realm.beginTransaction();
                realm.copyToRealm(recordDataRealm);
                realm.commitTransaction();
                Toast.makeText(ActivityCashOut.this, "Data Saved in Realm", Toast.LENGTH_SHORT).show();
                btnAddMore.setVisibility(View.VISIBLE);
        }
        });
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initComponents() {
        editAmount=findViewById(R.id.editAmountout);
        editRemark=findViewById(R.id.editRemarksout);
        btnSave=findViewById(R.id.btnSaveout);
        date=findViewById(R.id.cashOutDate);
        time=findViewById(R.id.cashOutTime);
        btnAddMore=findViewById(R.id.btnAddMore);
        realm=Realm.getInstance(MyApplication.config);

    }
}