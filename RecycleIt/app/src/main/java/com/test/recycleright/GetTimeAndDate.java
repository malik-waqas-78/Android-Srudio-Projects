package com.test.recycleright;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class GetTimeAndDate extends AppCompatActivity {

    String d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_time_and_date);

        findViewById(R.id.include_td).findViewById(R.id.iv_back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(34);
                        finish();
                    }
                }
        );
        findViewById(R.id.iv_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox(getResources().getString(R.string.total_points),
                        getResources().getString(R.string.your_total_points_are)+
                                " "+MySharedPreferences.getInstance(GetTimeAndDate.this).getScores());
            }
        });

        CalendarView cv=findViewById(R.id.calendarView);
        cv.setDate(Calendar.getInstance().getTimeInMillis());
        Date date=new Date(cv.getDate());
        d=date.getDate()+"/"+date.getMonth()+"/"+date.getYear();

        RadioButton cb_am=findViewById(R.id.cb_am);
        RadioButton cb_pm=findViewById(R.id.cb_pm);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                d=dayOfMonth+"/"+month+"/"+year;
            }
        });
        Button btn=findViewById(R.id.btn_next_td);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecycleInfo.getObj().setDate(d);
                if(cb_am.isChecked() ||cb_pm.isChecked()){
                    if(cb_am.isChecked()){
                        RecycleInfo.getObj().setPickUpTime("10:00 - 11:00 AM");
                    }else{
                        RecycleInfo.getObj().setPickUpTime("02:00 - 03:00 AM");
                    }
                    startActivityForResult(new Intent(GetTimeAndDate.this,FinalActivity.class),3464);
                }else{
                    Toast.makeText(GetTimeAndDate.this, "Please select time.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==3464){
            setResult(3464);
            finish();
        }
    }
    private void showDialogBox(String title,String msg) {
        Dialog dialog=new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        TextView tvTitle=dialog.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        TextView text=dialog.findViewById(R.id.tv_msg);
        text.setText(msg);
        Button btnOk=dialog.findViewById(R.id.btn_done);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

}