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

public class PortalActivity extends AppCompatActivity {
    WebView myWebViewPortal;
    ProgressBar progressBarPortal;

    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);

        //back arrow
        goBack=findViewById(R.id.Imgwebback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PortalActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myWebViewPortal=(WebView)findViewById(R.id.wvPortal);
        progressBarPortal=findViewById(R.id.pbarPortal);
        progressBarPortal.setMax(100);
        progressBarPortal.setProgress(0);
        WebSettings webSettings = myWebViewPortal.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebViewPortal.loadUrl("https://portal.kcau.ac.ke/kcaportal/");
        myWebViewPortal.setWebViewClient(new WebViewClient());

        myWebViewPortal.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarPortal.setProgress(newProgress);
                if (newProgress==100)
                    progressBarPortal.setVisibility(view.INVISIBLE);
                else
                    progressBarPortal.setVisibility((view.VISIBLE));
                super.onProgressChanged(view, newProgress);
            }
        });
        myWebViewPortal.setDownloadListener(new DownloadListener() {
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

        if(myWebViewPortal.canGoBack()){
            myWebViewPortal.goBack();
        }else {
            counter++;
            if(counter==1){
                Toast.makeText(this,"Press again to go back",Toast.LENGTH_SHORT).show();
            }
            else if(counter==2)
            {
                Intent intent = new Intent(PortalActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                super.onBackPressed();
            }
        }
    }
}