package com.example.kcau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DistanceLearningActivity extends AppCompatActivity {

    WebView myWebViewElearning;
    ProgressBar progressBarElearning;

    ImageView goBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_learning);

        //back arrow
        goBack=findViewById(R.id.Imgwebback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DistanceLearningActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myWebViewElearning=(WebView)findViewById(R.id.wvElearning);
        progressBarElearning=findViewById(R.id.pbarElearning);
        progressBarElearning.setMax(100);
        progressBarElearning.setProgress(0);
        WebSettings webSettings = myWebViewElearning.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebViewElearning.loadUrl("https://elearning.kca.ac.ke/");
        myWebViewElearning.setWebViewClient(new WebViewClient());

        myWebViewElearning.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarElearning.setProgress(newProgress);
                if (newProgress==100)
                    progressBarElearning.setVisibility(view.INVISIBLE);
                else
                    progressBarElearning.setVisibility((view.VISIBLE));
                super.onProgressChanged(view, newProgress);
            }
        });


        myWebViewElearning.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    int counter =0;
    @Override
    public void onBackPressed() {

        if(myWebViewElearning.canGoBack()){
            myWebViewElearning.goBack();
        }else {
            counter++;
            if(counter==1){
                Toast.makeText(this,"Press again to go back",Toast.LENGTH_SHORT).show();
            }
            else if(counter==2)
            {
                Intent intent = new Intent(DistanceLearningActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                super.onBackPressed();
            }

        }
    }
}