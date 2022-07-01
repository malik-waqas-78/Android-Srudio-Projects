package com.paper.Exams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class Details extends AppCompatActivity {

    HashMap<String,Integer> dict=new HashMap<>();
    int age=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent=getIntent();
        age=intent.getIntExtra("age",0);
        String type=intent.getStringExtra("type");

        dict.put("standardPrice",120);
        dict.put("standardImage",R.drawable.standard_ticket);
        dict.put("premiumPrice",200);
        dict.put("premiumImage",R.drawable.premium_ticket);

        TextView price=findViewById(R.id.tvprice);
        TextView text=findViewById(R.id.tvtext);
        ImageView view=findViewById(R.id.ticket);




        if(type.equals("Standard")){
            view.setImageDrawable(getDrawable( dict.get("standardImage")));

            float priceValue= (float) (dict.get("standardPrice")+(dict.get("standardPrice")*0.05));

            if(age<18||age>60){
                priceValue = 0;
            }

            price.setText("Ticket Price : "+priceValue);
            text.setText("With a standard ticket,\n you can text the pavilions.");
        }else{
            view.setImageDrawable(getDrawable( dict.get("premiumImage")));

            float priceValue= (float) (dict.get("premiumPrice")+(dict.get("premiumPrice")*0.05));

            if(age<18||age>60){
                priceValue = 0;
            }

            price.setText("Ticket Price : "+priceValue);
            text.setText("With a premium ticket,\n you can text all the pavilions and events.");
        }
    }
}