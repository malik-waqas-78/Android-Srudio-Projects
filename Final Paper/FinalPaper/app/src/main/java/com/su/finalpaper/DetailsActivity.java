package com.su.finalpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    HashMap<String,Integer> dict=new HashMap<>();
    int age=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent=getIntent();
        age=intent.getIntExtra("age",0);
        String tt=intent.getStringExtra("tt");

        TextView price=findViewById(R.id.tvticketprice);
        TextView access=findViewById(R.id.tvaccess);
        ImageView iv=findViewById(R.id.ivticket);

        dict.put("sPrice",120);
        dict.put("sImage",R.drawable.standard_ticket);
        dict.put("pPrice",200);
        dict.put("pImage",R.drawable.premium_ticket);


        if(tt.equals("Standard")){
            iv.setImageDrawable(getDrawable( dict.get("sImage")));

            float priceValue= (float) (dict.get("sPrice")+(dict.get("sPrice")*0.05));

            if(age<18||age>60){
                priceValue = 0;
            }

            price.setText("Ticket Price : "+priceValue);
            access.setText("With a standard ticket,\n you can access the pavilions.");
        }else{
            iv.setImageDrawable(getDrawable( dict.get("pImage")));

            float priceValue= (float) (dict.get("pPrice")+(dict.get("pPrice")*0.05));

            if(age<18||age>60){
                priceValue = 0;
            }

            price.setText("Ticket Price : "+priceValue);
            access.setText("With a premium ticket,\n you can access all the pavilions and events.");
        }
    }
}