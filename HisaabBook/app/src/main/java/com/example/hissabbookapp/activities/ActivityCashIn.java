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

public class ActivityCashIn extends AppCompatActivity {
    TextView totalAmount;
    EditText editAmount,editRemark;
    EditText editTotalRemarks;
    ArrayList<SubRecordData> subRecordData=new ArrayList<SubRecordData>();
    Button btnSave,btnaddmore,btnAddSub;
    TextView date,time;
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
    int check=0;
    String id;
    String type;
    String amount;
    String mainType;


    RecordData recordDataModal;

    RealmResults<RecordDataRealm> recordDataRealms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);
        initComponents();
//        if(id!=null&&!id.isEmpty()){
//            GetData(id);
//            GetSubData(recordDataModal.getId());
//            totalAmount.setText(recordDataModal.getTotalBalance().substring(2,recordDataModal.getTotalBalance().length()));
//            editTotalRemarks.setText(recordDataModal.getRemark());
//            subRecordAdapter = new SubRecordAdapter(ActivityCashIn.this, subRecordData);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityCashIn.this);
//            layoutManager.setOrientation(RecyclerView.VERTICAL);
//            subRecordRecycler.setLayoutManager(layoutManager);
//            subRecordRecycler.setAdapter(subRecordAdapter);
//            editAmount.setText("");
//            editRemark.setText("");
//        }
        String currentDate1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        date.setText(currentDate1);
        time.setText(currentTime1);
        mainId = System.currentTimeMillis() + "";
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(type.equals("+")) {
                        amount = "+ " + totalAmount.getText();
                        mainType="in";
                        balance = MainActivity.netBalance + Integer.parseInt(amount.substring(2, amount.length()));
                    }else if(type.equals("-")){
                        amount = "- " + totalAmount.getText();
                        mainType="out";
                        balance = MainActivity.netBalance - Integer.parseInt(amount.substring(2, amount.length()));
                    }
                    String remarks = editTotalRemarks.getText().toString();

                    recordDataRealm = new RecordDataRealm(remarks, amount, currentTime1, balance + "", mainId, currentDate1,mainType,currentDate1.substring(0,1),currentDate1.substring(3,4),currentDate1.substring(6,9));
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(recordDataRealm);
                    realm.commitTransaction();
                    Toast.makeText(ActivityCashIn.this, "Data Saved in Realm", Toast.LENGTH_SHORT).show();
                    finish();
            }
        });
//                    btnSave.setText(R.string.update);
//                }else if(check==1){
//                    check=0;
//                    GetData(mainId);
//                    editAmount.setText(recordDataModal.getTotalBalance().substring(2,recordDataModal.getTotalBalance().length()));
//                    editRemark.setText(recordDataModal.getRemark());
//                    id=recordDataModal.getId();
//                    GetSubData(mainId);
//                    totalAmount.setText("");
//                    editTotalRemarks.setText("");
//                    cardRecyclerSub.setVisibility(View.VISIBLE);
//                    btnaddmore.setVisibility(View.VISIBLE);
//                    linearViewRecord.setVisibility(View.VISIBLE);
//                    btnAddSub.setVisibility(View.GONE);
//                    btnSave.setText(R.string.save);
//                    submum=0;
//                }


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
                //if(Integer.parseInt(subAmount)<=num&&num!=0) {
                    subRecordDataRealm = new SubRecordDataRealm(subRemark, subAmount, currentTime, balance + "", id2, mainId, currentDate);
                    subRecordRealm.beginTransaction();
                    subRecordRealm.copyToRealm(subRecordDataRealm);
                    subRecordRealm.commitTransaction();

                    RealmResults<SubRecordDataRealm> realmResults = subRecordRealm.where(SubRecordDataRealm.class).equalTo("mainId", mainId).sort("time", Sort.DESCENDING).findAll();

                    if (realmResults.size() == 0 || realmResults.isEmpty()) {
                        Toast.makeText(ActivityCashIn.this, "No Sub Record", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < realmResults.size(); i++) {
                            subRecordData.add(new SubRecordData(realmResults.get(i).getRemark(), realmResults.get(i).getTotalBalance(), realmResults.get(i).getTime(), realmResults.get(i).getBalance(), realmResults.get(i).getId(), realmResults.get(i).getMainId(), realmResults.get(i).getDate()));

                        }
                    }

                    subRecordAdapter = new SubRecordAdapter(ActivityCashIn.this, subRecordData);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityCashIn.this);
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    subRecordRecycler.setLayoutManager(layoutManager);
                    subRecordRecycler.setAdapter(subRecordAdapter);
                    editAmount.setText("");
                    editRemark.setText("");
                    Toast.makeText(ActivityCashIn.this, "Sub Data Added", Toast.LENGTH_SHORT).show();
            }
        });
        btnAddSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddSub.setVisibility(View.GONE);
                linearAddRecord.setVisibility(View.VISIBLE);
                cardRecyclerSub.setVisibility(View.VISIBLE);
                btnaddmore.setVisibility(View.VISIBLE);
                linearViewRecord.setVisibility(View.VISIBLE);

            }
        });
    }

    private void initComponents() {
        editAmount=findViewById(R.id.editAmount);
        editRemark=findViewById(R.id.editRemarks);
        btnSave=findViewById(R.id.btnSave);
        date=findViewById(R.id.cashInDate);
        time=findViewById(R.id.cashInTime);
        totalAmount=findViewById(R.id.totalAmountTextIn);
        editTotalRemarks=findViewById(R.id.editMainRemarks);
        btnaddmore=findViewById(R.id.btnAddMore);
        cardRecyclerSub=findViewById(R.id.cardRecyclerSub);
        realm=Realm.getInstance(MyApplication.config);
        subRecordRealm=Realm.getInstance(MyApplication.config2);
        subRecordRecycler=findViewById(R.id.recyclerSub);
        btnAddSub=findViewById(R.id.btnAddSub);
        linearViewRecord=findViewById(R.id.linearViewRecord);
        type=getIntent().getStringExtra("type");
        id=getIntent().getStringExtra("id");
        linearAddRecord=findViewById(R.id.linearaddrecord);
        totalAmount.setText("0");
    }
    public void GetData(String id){
        realm=Realm.getInstance(MyApplication.config);
        recordDataRealms=realm.where(RecordDataRealm.class).equalTo("id",id).findAll();
        if(recordDataRealms.size()==0|| recordDataRealms.isEmpty()){
            Toast.makeText(ActivityCashIn.this, "No Data Added Yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<recordDataRealms.size();i++){
                recordDataRealm=new RecordDataRealm(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9));
                recordDataModal=new RecordData(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9));

            }
        }

    }

}