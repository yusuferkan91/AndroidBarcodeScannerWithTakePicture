package com.example.els_v2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;

public class QrScannerActivity extends AppCompatActivity {

    private Code_Scanner mCodeScanner;
    private static final int pic_id = 123;
    static BinaryBitmap capture_image;
    LinearLayout result_scanner_view;
    Button btn_show_list;
    static boolean isResult=false;
    static Bitmap image;
    final int CAMERA_PERMISSION_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        Code_ScannerView scannerView = findViewById(R.id.scanner_view);
        result_scanner_view = (LinearLayout) findViewById(R.id.result_scanner_view);
        btn_show_list = (Button) findViewById(R.id.btn_show_list);
//        list_bitmap = new ArrayList<Bitmap>();
        System.out.println("size::::"+BarkodClass.list_result.size());
        System.out.println("arraylist:::" + BarkodClass.list_result);
        BarkodClass.list_result.clear();
        if(BarkodClass.list_result.size()>0)
            System.out.println("arraylist:::" + BarkodClass.list_result.get(0));

        btn_show_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BarkodClass.list_result.size() > 0){
                    Intent resultActivity = new Intent(getApplicationContext(), ResultViewActivity.class);
                    startActivity(resultActivity);
                }else{
                    Toast.makeText(QrScannerActivity.this, "QR kod listesi boş", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(Build.VERSION.SDK_INT>=23){
            if (checkPermission(Manifest.permission.CAMERA)){
                mCodeScanner = new Code_Scanner(this, scannerView);
                mCodeScanner.setDecodeCallback(new DecodeCallback() {
                    @Override
                    public void onDecoded(@NonNull final Result result) {
                        QrScannerActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showResult(result.getText());
                                mCodeScanner.startPreview();
                            }
                        });
                    }
                });
                scannerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCodeScanner.startPreview();
                    }
                });
            }
            else{
                requestPermission(Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE);
            }
        }else{
            mCodeScanner = new Code_Scanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    QrScannerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showResult(result.getText());

                            mCodeScanner.startPreview();
                        }
                    });
                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCodeScanner.startPreview();
                }
            });
        }
        System.out.println("Camera::" + mCodeScanner.getCamera());

    }

    public void showResult(String resultText){
        if(!BarkodClass.list_result.contains(resultText)){

            Intent keyboard = new Intent(getApplicationContext(), InfoActivity.class);
            startActivity(keyboard);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            Toast.makeText(QrScannerActivity.this, resultText, Toast.LENGTH_SHORT).show();
            addButton(resultText);
        }
    }
    public void addButton(String text){
        BarkodClass.list_result.add(text);
        final Button btn = new Button(this);
        btn.setText(text);
        btn.setTextColor(Color.rgb(95,44,130));
        btn.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,15);
        btn.setLayoutParams(params);
        result_scanner_view.addView(btn);
    }

    private boolean checkPermission(String permission){
        int result = ContextCompat.checkSelfPermission(QrScannerActivity.this, permission);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }
    private void requestPermission(String permision, int code){
        if(ActivityCompat.shouldShowRequestPermissionRationale(QrScannerActivity.this, permision)){

        }else{
            ActivityCompat.requestPermissions(QrScannerActivity.this, new String[]{permision}, code);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        mCodeScanner.startPreview();
    }
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
