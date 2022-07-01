package com.example.hissabbookapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hissabbookapp.MyApplication;
import com.example.hissabbookapp.R;
import com.example.hissabbookapp.adapters.SubRecordAdapter;
import com.example.hissabbookapp.modalclasses.RecordData;
import com.example.hissabbookapp.modalclasses.SubRecordData;
import com.example.hissabbookapp.realmmodals.RecordDataRealm;
import com.example.hissabbookapp.realmmodals.SubRecordDataRealm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ActivityUpdateRecord extends AppCompatActivity {
    TextView totalAmount;
    EditText editAmount,editRemark;
    EditText editTotalRemarks;
    Button btnaddmore,btnAddSub;
    TextView date,time;
    Button btnUpdate;

    ArrayList<SubRecordData> subRecordData=new ArrayList<SubRecordData>();

    RealmResults<SubRecordDataRealm> subRecordDataRealms;
    Realm realm,subRecordRealm;
    RecordDataRealm recordDataRealm;
    SubRecordDataRealm subRecordDataRealm;
    int balance,num=0,submum=0;
    String mainId;
    SubRecordAdapter subRecordAdapter;
    RecyclerView subRecordRecycler;
    CardView cardRecyclerSub;
    LinearLayout linearViewRecord,linearAddRecord;


    String id;
    ArrayList<RecordData> recordData=new ArrayList<RecordData>();

    RecordData recordDataModal;

    RealmResults<RecordDataRealm> recordDataRealms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);
        InitComponents();
        GetData(id);
        GetSubData(recordDataModal.getId());

        SetAdapterData();

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        date.setText(currentDate);
        time.setText(currentTime);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordDataModal.getTotalBalance().contains("+ ")){
                    String amount="+ "+totalAmount.getText();
                    balance=MainActivity.netBalance+Integer.parseInt(amount.substring(2,amount.length()));
                    recordDataRealm=new RecordDataRealm(editTotalRemarks.getText().toString(),amount,currentTime,balance+"",id,currentDate,"in",currentDate.substring(0,1),currentDate.substring(3,4),currentDate.substring(6,9));
                }else if(recordDataModal.getTotalBalance().contains("- ")){
                    String amount="- "+totalAmount.getText();
                    balance=MainActivity.netBalance-Integer.parseInt(amount.substring(2,amount.length()));
                    recordDataRealm=new RecordDataRealm(editTotalRemarks.getText().toString(),amount,currentTime,balance+"",id,currentDate,"out",currentDate.substring(0,1),currentDate.substring(3,4),currentDate.substring(6,9));
                }
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(recordDataRealm);
                realm.commitTransaction();
                Toast.makeText(ActivityUpdateRecord.this, "Data Saved in Realm", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        btnaddmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subRecordData.clear();
                String subRemark=editRemark.getText().toString();
                String subAmount=editAmount.getText().toString();
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String id2=System.currentTimeMillis()+"";
                num+=Integer.parseInt(editAmount.getText().toString());
                totalAmount.setText(num+"");
                int balance=Integer.parseInt(totalAmount.getText().toString());

                subRecordDataRealm = new SubRecordDataRealm(subRemark, subAmount, currentTime, balance + "", id2, id, currentDate);
                subRecordRealm.beginTransaction();
                subRecordRealm.copyToRealm(subRecordDataRealm);
                subRecordRealm.commitTransaction();

                RealmResults<SubRecordDataRealm> realmResults = subRecordRealm.where(SubRecordDataRealm.class).equalTo("mainId", id).sort("time", Sort.DESCENDING).findAll();

                if (realmResults.size() == 0 || realmResults.isEmpty()) {
                    Toast.makeText(ActivityUpdateRecord.this, "No Sub Record", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < realmResults.size(); i++) {
                        subRecordData.add(new SubRecordData(realmResults.get(i).getRemark(), realmResults.get(i).getTotalBalance(), realmResults.get(i).getTime(), realmResults.get(i).getBalance(), realmResults.get(i).getId(), realmResults.get(i).getMainId(), realmResults.get(i).getDate()));

                    }
                }

                subRecordAdapter = new SubRecordAdapter(ActivityUpdateRecord.this, subRecordData);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityUpdateRecord.this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                subRecordRecycler.setLayoutManager(layoutManager);
                subRecordRecycler.setAdapter(subRecordAdapter);
                editAmount.setText("");
                editRemark.setText("");
                Toast.makeText(ActivityUpdateRecord.this, "Sub Data Added", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void SetAdapterData(){
        String amount=recordDataModal.getTotalBalance().substring(2,recordDataModal.getTotalBalance().length());
        totalAmount.setText(amount);
        editTotalRemarks.setText(recordDataModal.getRemark());
        editAmount.setText("");
        editRemark.setText("");
        num=Integer.parseInt(recordDataModal.getTotalBalance().substring(2,recordDataModal.getTotalBalance().length()));

        subRecordAdapter = new SubRecordAdapter(ActivityUpdateRecord.this, subRecordData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityUpdateRecord.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        subRecordRecycler.setLayoutManager(layoutManager);
        subRecordRecycler.setAdapter(subRecordAdapter);
    }
    public void InitComponents(){

        editAmount=findViewById(R.id.editAmount);
        editRemark=findViewById(R.id.editRemarks);
        date=findViewById(R.id.cashInDate);
        time=findViewById(R.id.cashInTime);
        totalAmount=findViewById(R.id.totalAmountTextIn);
        editTotalRemarks=findViewById(R.id.editMainRemarks);
        btnaddmore=findViewById(R.id.btnAddMore);
        cardRecyclerSub=findViewById(R.id.cardRecyclerSub);
        subRecordRealm=Realm.getInstance(MyApplication.config2);
        subRecordRecycler=findViewById(R.id.recyclerSub);
        btnAddSub=findViewById(R.id.btnAddSub);
        linearViewRecord=findViewById(R.id.linearViewRecord);
        linearAddRecord=findViewById(R.id.linearaddrecord);
        btnUpdate=findViewById(R.id.btnUpdate);
        realm=Realm.getInstance(MyApplication.config);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
    }
    public void GetData(String id){
        realm=Realm.getInstance(MyApplication.config);
        recordDataRealms=realm.where(RecordDataRealm.class).equalTo("id",id).findAll();
        if(recordDataRealms.size()==0|| recordDataRealms.isEmpty()){
            Toast.makeText(ActivityUpdateRecord.this, "No Data Added Yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<recordDataRealms.size();i++){
                recordDataRealm=new RecordDataRealm(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9));
                recordDataModal=new RecordData(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9));
            }
        }
    }
    public void GetSubData(String id){
        subRecordRealm=Realm.getInstance(MyApplication.config2);
        subRecordDataRealms=subRecordRealm.where(SubRecordDataRealm.class).equalTo("mainId",id).findAll();
        if(subRecordDataRealms.size()==0||subRecordDataRealms.isEmpty()){
            Toast.makeText(this, "No Sub Data Added yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<subRecordDataRealms.size();i++){
                subRecordData.add(new SubRecordData(subRecordDataRealms.get(i).getRemark(),subRecordDataRealms.get(i).getTotalBalance(),subRecordDataRealms.get(i).getTime(),subRecordDataRealms.get(i).getBalance(),subRecordDataRealms.get(i).getId(),subRecordDataRealms.get(i).getMainId(),subRecordDataRealms.get(i).getDate()));
            }
        }
    }
}