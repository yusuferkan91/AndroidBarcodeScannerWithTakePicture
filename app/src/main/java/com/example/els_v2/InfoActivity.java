package com.example.els_v2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class InfoActivity extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getWindow().setLayout((int) (150* this.getResources().getDisplayMetrics().density), (int) (150* this.getResources().getDisplayMetrics().density));

        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000);
    }
    @Override
    public void finish(){
        super.finish();
        this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
