package com.example.kcau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar; //progress bar (loading progress)
    private Timer timer; //for progress bar
    private int i=0; //initial state of progress bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //progress bar
        progressBar=findViewById(R.id.progressBar);
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (i<100){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });progressBar.setProgress(i);
                    i++;
                }else {
                    timer.cancel();
                    Intent intent=new Intent(SplashScreen.this,LoginActivity.class); //open a new activity if i<=100
                    startActivity(intent);
                    finish();
                }

            }
        },0,50);

    }

    }

