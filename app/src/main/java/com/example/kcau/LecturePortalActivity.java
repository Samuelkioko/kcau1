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

public class LecturePortalActivity extends AppCompatActivity {
    WebView myWebViewLecturePortal;
    ProgressBar progressBarLecturePortal;

    ImageView goBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_portal);

        //back arrow
        goBack=findViewById(R.id.Imgwebback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LecturePortalActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myWebViewLecturePortal=(WebView)findViewById(R.id.wvLecturePortal);
        progressBarLecturePortal=findViewById(R.id.pbarLecturePortal);
        progressBarLecturePortal.setMax(100);
        progressBarLecturePortal.setProgress(0);
        WebSettings webSettings = myWebViewLecturePortal.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebViewLecturePortal.loadUrl("https://portal.kcau.ac.ke/lecturer/Account/Login");
        myWebViewLecturePortal.setWebViewClient(new WebViewClient());

        myWebViewLecturePortal.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarLecturePortal.setProgress(newProgress);
                if (newProgress==100)
                    progressBarLecturePortal.setVisibility(view.INVISIBLE);
                else
                    progressBarLecturePortal.setVisibility((view.VISIBLE));
                super.onProgressChanged(view, newProgress);
            }
        });


        myWebViewLecturePortal.setDownloadListener(new DownloadListener() {
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

        if(myWebViewLecturePortal.canGoBack()){
            myWebViewLecturePortal.goBack();
        }else {
            counter++;
            if(counter==1){
                Toast.makeText(this,"Press again to go back",Toast.LENGTH_SHORT).show();
            }
            else if(counter==2)
            {
                Intent intent = new Intent(LecturePortalActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                super.onBackPressed();
            }

        }
    }
}