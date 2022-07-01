package com.example.hissabbookapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hissabbookapp.MyApplication;
import com.example.hissabbookapp.R;
import com.example.hissabbookapp.adapters.RecordAdapterMain;
import com.example.hissabbookapp.interfaces.RecordItemClickInterface;
import com.example.hissabbookapp.modalclasses.RecordData;
import com.example.hissabbookapp.realmmodals.RecordDataRealm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements RecordItemClickInterface, PopupMenu.OnMenuItemClickListener {
    RecyclerView recordRecycler;
    ArrayList<RecordData> recordData=new ArrayList<RecordData>();
    Button btnCashIn,btnCashOut;
    RecordAdapterMain recordAdapterMain;
    TextView netAmount,totalInAmount,totalOutAmount,totalIn,totalOut,textNetBal;
    RealmResults<RecordDataRealm> recordDataRealms;
    Realm realm;
    public static int netBalance;
    SearchView searchView;
    Button btnFilter,btnDuration,btnByCashIn,btnBycashOut,btnNone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        btnCashIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ActivityCashIn.class);
                intent.putExtra("type","+");
                startActivity(intent);
            }
        });
        btnCashOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ActivityCashIn.class);
                intent.putExtra("type","-");
                startActivity(intent);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recordAdapterMain.getFilter().filter(s);
                return true;
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(MainActivity.this);
                ViewGroup viewGroup=findViewById(android.R.id.content);
                View view1= LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_dialog,viewGroup,false);
                alertDialog4.setView(view1);
                AlertDialog alertDialog2=alertDialog4.create();
                alertDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog2.show();

                btnByCashIn=view1.findViewById(R.id.btnByCashIn);
                btnBycashOut=view1.findViewById(R.id.btnByCashOut);
                btnNone=view1.findViewById(R.id.btnNone);

                btnByCashIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GetFilteredData("in");
                        alertDialog2.dismiss();
                    }
                });
                btnBycashOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GetFilteredData("out");
                        alertDialog2.dismiss();
                    }
                });
                btnNone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SetAdapterData();
                        alertDialog2.dismiss();
                    }
                });

            }
        });
        btnDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.popup);
                popup.show();
            }
        });

    }

    private void GetFilteredData(String type) {
        recordData.clear();
        realm=Realm.getInstance(MyApplication.config);
        recordDataRealms=realm.where(RecordDataRealm.class).equalTo("type",type).sort("time", Sort.DESCENDING).findAll();
        if(recordDataRealms.size()==0|| recordDataRealms.isEmpty()){
            Toast.makeText(MainActivity.this, "No Data Added Yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<recordDataRealms.size();i++){
                recordData.add(new RecordData(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9)));
            }
        }
       recordAdapterMain.notifyDataSetChanged();
    }
    public  void GetDurationFilteredData(String duration){
        recordData.clear();
        realm=Realm.getInstance(MyApplication.config);
        recordDataRealms=realm.where(RecordDataRealm.class).equalTo("date",duration).sort("time", Sort.DESCENDING).findAll();
        if(recordDataRealms.size()==0|| recordDataRealms.isEmpty()){
            Toast.makeText(MainActivity.this, "No Data Added Yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<recordDataRealms.size();i++){
                recordData.add(new RecordData(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9)));
            }
        }
        recordAdapterMain.notifyDataSetChanged();
    }
    public void GetDurationFilteredDataday(String duration){
        recordData.clear();
        realm=Realm.getInstance(MyApplication.config);
        recordDataRealms=realm.where(RecordDataRealm.class).equalTo("day",duration).sort("time", Sort.DESCENDING).findAll();
        if(recordDataRealms.size()==0|| recordDataRealms.isEmpty()){
            Toast.makeText(MainActivity.this, "No Data Added Yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<recordDataRealms.size();i++){
                recordData.add(new RecordData(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9)));
            }
        }
        recordAdapterMain.notifyDataSetChanged();
    }
    public  void GetDurationFilteredDatamonth(String duration){
        recordData.clear();
        realm=Realm.getInstance(MyApplication.config);
        recordDataRealms=realm.where(RecordDataRealm.class).equalTo("month",duration).sort("time", Sort.DESCENDING).findAll();
        if(recordDataRealms.size()==0|| recordDataRealms.isEmpty()){
            Toast.makeText(MainActivity.this, "No Data Added Yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<recordDataRealms.size();i++){
                recordData.add(new RecordData(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9)));
            }
        }
        recordAdapterMain.notifyDataSetChanged();
    }
    public  void GetDurationFilteredDatayear(String duration){
        recordData.clear();
        realm=Realm.getInstance(MyApplication.config);
        recordDataRealms=realm.where(RecordDataRealm.class).equalTo("year",duration).sort("time", Sort.DESCENDING).findAll();
        if(recordDataRealms.size()==0|| recordDataRealms.isEmpty()){
            Toast.makeText(MainActivity.this, "No Data Added Yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<recordDataRealms.size();i++){
                recordData.add(new RecordData(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9)));
            }
        }
        recordAdapterMain.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
        recordData.clear();
        SetAdapterData();
    }
    public void SetAdapterData(){
        recordData.clear();
        int numTotalIn = 0,numTotolOut = 0;
        realm=Realm.getInstance(MyApplication.config);
        recordDataRealms=realm.where(RecordDataRealm.class).sort("time", Sort.DESCENDING).findAll();
        if(recordDataRealms.size()==0|| recordDataRealms.isEmpty()){
            Toast.makeText(MainActivity.this, "No Data Added Yet", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<recordDataRealms.size();i++){
                recordData.add(new RecordData(recordDataRealms.get(i).getRemark(),recordDataRealms.get(i).getTotalBalance(),recordDataRealms.get(i).getTime(),recordDataRealms.get(i).getBalance(),recordDataRealms.get(i).getId(),recordDataRealms.get(i).getDate(),recordDataRealms.get(i).getType(),recordDataRealms.get(i).getDate().substring(0,1),recordDataRealms.get(i).getDate().substring(3,4),recordDataRealms.get(i).getDate().substring(6,9)));

            }
        }
        recordAdapterMain=new RecordAdapterMain(this,recordData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recordRecycler.setLayoutManager(layoutManager);
        recordRecycler.setAdapter(recordAdapterMain);

        //setting the data in the total amount total cash in, total cash out
        for(int i=0;i<recordData.size();i++){
            if(recordData.get(i).getTotalBalance().contains("+ ")){
                String num=recordData.get(i).getTotalBalance();
                num=num.substring(2,num.length());
                numTotalIn+=Integer.parseInt(num);
            }else if(recordData.get(i).getTotalBalance().contains("- ")){
                String num=recordData.get(i).getTotalBalance();
                num=num.substring(2,num.length());
                numTotolOut+=Integer.parseInt(num);
            }
        }
        netBalance=numTotalIn-numTotolOut;
        netAmount.setText(netBalance+"");
        totalInAmount.setText(numTotalIn+"");
        totalOutAmount.setText(numTotolOut+"");
    }

    private void initComponents() {
        btnCashIn=findViewById(R.id.btnCashIn);
        btnCashOut=findViewById(R.id.btnCashOut);
        recordRecycler=findViewById(R.id.recyclerHistory);
        netAmount=findViewById(R.id.netamounttext);
        totalInAmount=findViewById(R.id.totalintext);
        totalOutAmount=findViewById(R.id.totalouttext);
        searchView=findViewById(R.id.searchView1);
        btnFilter=findViewById(R.id.btnfilter);
        btnDuration=findViewById(R.id.btnAlltime);
        totalIn=findViewById(R.id.totalIn);
        totalOut=findViewById(R.id.totalOut);
        textNetBal=findViewById(R.id.netbal);

    }

    @Override
    public void OnItemClick(int pos) {
        String id=recordData.get(pos).getId();
        Intent intent=new Intent(MainActivity.this,ActivityViewRecord.class);
        intent.putExtra("id",id);
        intent.putExtra("pos",pos);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Calendar c=Calendar.getInstance();
        switch (item.getItemId()) {
            case R.id.allTime:
                SetAdapterData();
                return true;
            case R.id.today:
                String currentDate1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                GetDurationFilteredData(currentDate1);
                return true;
            case R.id.lastDay:
                int num1=Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));
                GetDurationFilteredDataday((num1-1)+"");
                return true;
            case R.id.prsentmonth:
                int num2=Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));
                GetDurationFilteredDatamonth((num2)+"");
                return true;
            case R.id.previosmonth:

                int num3=Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));
                GetDurationFilteredDatamonth((num3-1)+"");
                return true;
            case R.id.presentyear:
                int num4=Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
                GetDurationFilteredDatayear((num4)+"");
                return true;
            case R.id.pastyear:
                int num5=Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
                GetDurationFilteredDatayear((num5-1)+"");
                return true;
            default:
                return false;
        }
    }
}