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

public class WebsiteActivity extends AppCompatActivity {
    WebView myWebView;
    ProgressBar progressBar;
    ImageView goBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        //back arrow
        goBack=findViewById(R.id.Imgwebback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebsiteActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myWebView=(WebView)findViewById(R.id.wv);
        progressBar=findViewById(R.id.pbar);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("https://www.kcau.ac.ke/");
        myWebView.setWebViewClient(new WebViewClient());

        myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress==100)
                    progressBar.setVisibility(view.INVISIBLE);
                else
                    progressBar.setVisibility((view.VISIBLE));
                super.onProgressChanged(view, newProgress);
            }
        });


        myWebView.setDownloadListener(new DownloadListener() {
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

        if(myWebView.canGoBack()){
            myWebView.goBack();
        }else {
            counter++;
            if(counter==1){
                Toast.makeText(this,"Press again to go back",Toast.LENGTH_SHORT).show();
            }
            else if(counter==2)
            {
                Intent intent = new Intent(WebsiteActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                super.onBackPressed();
            }
        }
    }
}