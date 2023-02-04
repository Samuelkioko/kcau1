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

public class StaffPortalActivity extends AppCompatActivity {
    WebView myWebViewStaffPortal;
    ProgressBar progressBarStaffPortal;

    ImageView goBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_portal);

        //back arrow
        goBack=findViewById(R.id.Imgstfback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffPortalActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });


        myWebViewStaffPortal=(WebView)findViewById(R.id.wvStaffPortal);
        progressBarStaffPortal=findViewById(R.id.pbarStaffPortal);
        progressBarStaffPortal.setMax(100);
        progressBarStaffPortal.setProgress(0);
        WebSettings webSettings = myWebViewStaffPortal.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebViewStaffPortal.loadUrl("https://portal.kca.ac.ke/staff/Account/Login");
        myWebViewStaffPortal.setWebViewClient(new WebViewClient());

        myWebViewStaffPortal.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarStaffPortal.setProgress(newProgress);
                if (newProgress==100)
                    progressBarStaffPortal.setVisibility(view.INVISIBLE);
                else
                    progressBarStaffPortal.setVisibility((view.VISIBLE));
                super.onProgressChanged(view, newProgress);
            }
        });


        myWebViewStaffPortal.setDownloadListener(new DownloadListener() {
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

        if(myWebViewStaffPortal.canGoBack()){
            myWebViewStaffPortal.goBack();
        }else {
            counter++;
            if(counter==1){
                Toast.makeText(this,"Press again to go back",Toast.LENGTH_SHORT).show();
            }
            else if(counter==2)
            {
                Intent intent = new Intent(StaffPortalActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                super.onBackPressed();
            }

        }
    }
}