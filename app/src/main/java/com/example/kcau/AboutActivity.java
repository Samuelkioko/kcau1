package com.example.kcau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {
    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        goBack=findViewById(R.id.Imgaboutback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    /* end of oncreate */
int counter=0;
    @Override
    public void onBackPressed() {
        counter++;
        if(counter<2){
            Toast.makeText(this,"Press again to go back",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent=new Intent(AboutActivity.this, DashboardActivity.class);
            startActivity(intent);
        }

    }
}