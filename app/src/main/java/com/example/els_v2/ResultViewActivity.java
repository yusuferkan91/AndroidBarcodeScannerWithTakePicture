package com.example.els_v2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultViewActivity extends AppCompatActivity {
    TableLayout result_view;
    ImageButton btn_clear;
    ImageButton btn_add;
    ImageButton btn_pdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        result_view = (TableLayout) findViewById(R.id.result_view);
        btn_clear = (ImageButton) findViewById(R.id.btn_clear);
        btn_add = (ImageButton) findViewById(R.id.btn_add);
        btn_pdf = (ImageButton) findViewById(R.id.btn_pdf);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keyboard = new Intent(getApplicationContext(), CreatePdfActivity.class);
                startActivity(keyboard);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarkodClass.list_result.clear();
                Intent qrActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(qrActivity);
                finish();
            }
        });
        System.out.println("size::::::::" + BarkodClass.list_result.size());
        System.out.println("arraylist::::::::" + BarkodClass.list_result);
        for(String txt: BarkodClass.list_result){
            if(BarkodClass.list_result.indexOf(txt)%2 ==0){
                addRow(txt, Color.rgb(95,44,130));
            }else{
                addRow(txt, Color.rgb(73, 160,157));
            }
        }
    }
    public void addRow(String txt, int color){

            TableRow row = new TableRow(this);
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(getDP(300), TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(params2);

            TextView text_id = new TextView(this);
            int id = BarkodClass.list_result.indexOf(txt)+1;
            text_id.setText(String.valueOf(id));
            text_id.setTextSize(18);
            text_id.setTextColor(color);
            text_id.setGravity(Gravity.CENTER);
            text_id.setBackgroundColor(Color.WHITE);

            TableRow.LayoutParams params1 = new TableRow.LayoutParams(getDP(40), TableRow.LayoutParams.MATCH_PARENT);
            params1.setMargins(5,2,0,5);
            text_id.setLayoutParams(params1);
            row.addView(text_id);

            TextView textView = new TextView(this);
            textView.setText(txt);
            textView.setTextSize(18);
            textView.setTextColor(color);
            textView.setBackgroundColor(Color.WHITE);
            TableRow.LayoutParams params = new TableRow.LayoutParams(getDP(200), TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5,2,5,5);
            textView.setLayoutParams(params);
            row.addView(textView);

            result_view.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

    }
    public int getDP(int dp){
        return (int)(dp* this.getResources().getDisplayMetrics().density);
    }
}
