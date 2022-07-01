package com.example.hissabbookapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hissabbookapp.MyApplication;
import com.example.hissabbookapp.R;
import com.example.hissabbookapp.adapters.SubRecordAdapter;
import com.example.hissabbookapp.databinding.ActivityViewRecordBinding;
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

public class ActivityViewRecord extends AppCompatActivity {
    private ActivityViewRecordBinding activityViewRecordBinding;
    Intent intent;
    String id;
    Realm realm,subRealm;
    SubRecordAdapter subRecordAdapter;
    ArrayList<SubRecordData> subRecordData=new ArrayList<SubRecordData>();
    RecordDataRealm recordDataRealm;
    RecordData recordDataModal;
    RealmResults<RecordDataRealm> recordDataRealms;
    RealmResults<SubRecordDataRealm> subRecordDataRealms;
    String type;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewRecordBinding=ActivityViewRecordBinding.inflate(getLayoutInflater());
        setContentView(activityViewRecordBinding.getRoot());

        intent=getIntent();
        id=intent.getStringExtra("id");
        pos=intent.getIntExtra("pos",0);

        activityViewRecordBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityViewRecord.this,ActivityUpdateRecord.class);
                intent.putExtra("id",id);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetData(id);
        GetSubData(recordDataModal.getId());
        SetAdapterData();
        if(recordDataModal.getTotalBalance().contains("+ ")){
            type="+";
        }else if(recordDataModal.getTotalBalance().contains("- ")){
            type="-";
        }

    }

    public void SetAdapterData(){
        String amount=recordDataModal.getTotalBalance().substring(2,recordDataModal.getTotalBalance().length());
        activityViewRecordBinding.amountValue.setText(amount);
        activityViewRecordBinding.remarkTag.setText(recordDataModal.getRemark());
        activityViewRecordBinding.texttime.setText(recordDataModal.getTime());
        activityViewRecordBinding.totalBalance.setText(recordDataModal.getDate());

        subRecordAdapter=new SubRecordAdapter(ActivityViewRecord.this,subRecordData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityViewRecord.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        activityViewRecordBinding.recyclerSub.setLayoutManager(layoutManager);
        activityViewRecordBinding.recyclerSub.setAdapter(subRecordAdapter);

    }
    public void GetData(String id){
        realm= Realm.getInstance(MyApplication.config);
        recordDataRealms=realm.where(RecordDataRealm.class).equalTo("id",id).sort("time", Sort.DESCENDING).findAll();
        if(recordDataRealms.size()==0|| recordDataRealms.isEmpty()){
            Toast.makeText(ActivityViewRecord.this, "No Data Added Yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<recordDataRealms.size();i++){
                recordDataRealm=new RecordDataRealm(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9));
                recordDataModal=new RecordData(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9));
            }
        }
    }
    public void GetSubData(String id){
        subRecordData.clear();
        subRealm=Realm.getInstance(MyApplication.config2);
        subRecordDataRealms=subRealm.where(SubRecordDataRealm.class).equalTo("mainId",id).findAll();
        if(subRecordDataRealms.size()==0||subRecordDataRealms.isEmpty()){
            activityViewRecordBinding.cardRecycler.setVisibility(View.GONE);
        }else{
            for(int i=0;i<subRecordDataRealms.size();i++){
                subRecordData.add(new SubRecordData(subRecordDataRealms.get(i).getRemark(),subRecordDataRealms.get(i).getTotalBalance(),subRecordDataRealms.get(i).getTime(),subRecordDataRealms.get(i).getBalance(),subRecordDataRealms.get(i).getId(),subRecordDataRealms.get(i).getMainId(),subRecordDataRealms.get(i).getDate()));
            }
        }
    }
}