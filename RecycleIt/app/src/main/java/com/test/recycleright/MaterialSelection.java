package com.test.recycleright;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MaterialSelection extends AppCompatActivity {
    boolean isPlastic,isPaper,isCans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrial_selection);
        //findViewById(R.id.iv_cleaning).animate().rotationY(360).setDuration(1000);
        CheckBox cb_plastic=findViewById(R.id.cb_plastic);
        findViewById(R.id.include_ms).findViewById(R.id.iv_back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(34);
                        finish();
                    }
                }
        );
        cb_plastic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPlastic=isChecked;
            }
        });
        CheckBox cb_paper=findViewById(R.id.cb_paper);
        cb_paper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPaper=isChecked;
            }
        });
        CheckBox cb_cans=findViewById(R.id.cb_cans);
        cb_cans.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCans=isChecked;
            }
        });
        Button btn=findViewById(R.id.btn_next_m);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlastic||isPaper||isCans){

                    RecycleInfo.getObj().setPlastic(isPlastic);
                    RecycleInfo.getObj().setPaper(isPaper);
                    RecycleInfo.getObj().setCans(isCans);

                    startActivityForResult(new Intent(MaterialSelection.this,QuantityInfo.class),3464);
                }else{
                    Toast.makeText(MaterialSelection.this,"Please select at least one type.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.iv_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox(getResources().getString(R.string.total_points),
                        getResources().getString(R.string.your_total_points_are)+
                                " "+MySharedPreferences.getInstance(MaterialSelection.this).getScores());
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