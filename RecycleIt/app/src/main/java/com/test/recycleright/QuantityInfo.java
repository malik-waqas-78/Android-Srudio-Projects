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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuantityInfo extends AppCompatActivity {

    EditText et_quantity;
    ImageView iv_up,iv_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity_info);

        et_quantity=findViewById(R.id.et_number);
        iv_up=findViewById(R.id.iv_inc);
        iv_down=findViewById(R.id.iv_dec);

        iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int x= Integer.parseInt(et_quantity.getText().toString().trim());
                    et_quantity.setText((x+1)+"");
                }catch (NumberFormatException ex){

                }
            }
        });

        iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int x= Integer.parseInt(et_quantity.getText().toString().trim());
                    if(x!=1){
                        et_quantity.setText((x-1)+"");
                    }else{
                        Toast.makeText(
                                QuantityInfo.this,
                                "Quantity can't be zero.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }catch (NumberFormatException ex){

                }
            }
        });
        Button btn=findViewById(R.id.btn_next_q);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecycleInfo.getObj().setQuantity(Integer.parseInt(et_quantity.getText().toString().trim()));
               startActivityForResult(new Intent(QuantityInfo.this,InsertLocation.class),3464);
            }
        });

        findViewById(R.id.include_q).findViewById(R.id.iv_back).setOnClickListener(
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
                                " "+MySharedPreferences.getInstance(QuantityInfo.this).getScores());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode==3464
            ){
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