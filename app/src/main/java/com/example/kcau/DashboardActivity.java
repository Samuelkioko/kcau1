package com.example.kcau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class DashboardActivity extends AppCompatActivity {
    //variables
    DrawerLayout drawerLayout;

    Button website,studentPortal,studentMoodle,lecturePortal,staffPortal,eLearning,about;
    SliderView sliderView;
    int[] images = {R.drawable.library2,
            R.drawable.libfromground,
            R.drawable.kioko,
            R.drawable.admin2,
            R.drawable.students,
            R.drawable.building};
    TextView signout;

    LinearLayout buttons;

    ProgressDialog progressDialoglogout;

    //user profile
    FirebaseUser user;
    DatabaseReference reference;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //assign
        drawerLayout=findViewById(R.id.drawer_layout);
        
        signout=findViewById(R.id.txtlogout);
     

        sliderView = findViewById(R.id.image_slider);

        progressDialoglogout=new ProgressDialog(this);
        

        website=findViewById(R.id.btnWebsite);

        studentMoodle=findViewById(R.id.btnStdMoodle);
        studentPortal=findViewById(R.id.btnStdportal);
        lecturePortal=findViewById(R.id.btnLctPortal);
        staffPortal=findViewById(R.id.btnStfPortal);
        eLearning=findViewById(R.id.btneLearning);

        //user profile name
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();

        final TextView name = (TextView) findViewById(R.id.finalName);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //create user object
                User userProfile =snapshot.getValue(User.class);
                if (userProfile !=null){
                    String fullname = userProfile.fullname;

                    name.setText(fullname);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardActivity.this,"Not able to get user data",Toast.LENGTH_LONG).show();

            }
        });
        
        //signout
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize alert dialog
                AlertDialog.Builder builder0= new AlertDialog.Builder(DashboardActivity.this);

                //set tittle
                builder0.setTitle("Logout");
                //set message
                builder0.setMessage("Sign out?");
                //positive yes button
                builder0.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialoglogout.setTitle("Signing you out");
                        progressDialoglogout.setMessage("Please wait...");
                        progressDialoglogout.show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
                        progressDialoglogout.dismiss();
                    }
                });
                //negative NO button
                builder0.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismiss alert dialog
                        dialog.dismiss();
                    }
                });
                //show dialog
                builder0.show();
            }
        });


        //distant learning
        eLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,DistanceLearningActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //lecturer portal
        lecturePortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, LecturePortalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //staff portal
        staffPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, StaffPortalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //website
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashboardActivity.this, WebsiteActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //moodle
        studentMoodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MoodleActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //student portal
        studentPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PortalActivity.class);
                startActivity(intent);
                finish();
            }
        });



        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();


    }//end of oncreate


    int counter =0;
    @Override
    public void onBackPressed() {
        counter++;
        if (counter<2){
            Toast.makeText(this,"Press again to exit",Toast.LENGTH_SHORT).show();
        }else{
            super.onBackPressed();
        }
    }

    //methods
    public void ClickMenu(View view){
        //open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {

        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }


    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void  ClickHome(View view){
        //recreate activity
        recreate();
    }
    public void ClickWebsite(View view){
        //redirect activity to dashboard
        redirectActivity(this,WebsiteActivity.class);
    }
    public void ClickProfile(View view){
        //redirect activity to dashboard
        redirectActivity(this,ProfileActivity.class);
    }
    public void ClickStdPortal(View view){
        //redirect activity to portal
        redirectActivity(this,PortalActivity.class);
    }
    public void ClickStdMoodle(View view){
        //redirect activity moodle
        redirectActivity(this,MoodleActivity.class);
    }
    public void ClickLctPortal(View view){
        //redirect activity to lecture portal
        redirectActivity(this,LecturePortalActivity.class);
    }
    public void ClickStfPortal(View view){
        //redirect activity to staff
        redirectActivity(this,StaffPortalActivity.class);
    }
    public void ClickDlPortal(View view){
        //redirect activity to distant learning
        redirectActivity(this,DistanceLearningActivity.class);
    }

    public void ClickAboutus(View view){
        //redirect activity to about us
        redirectActivity(this,AboutActivity.class);
    }
    public void ClickExit(View view){
        //close app
        exit(this);
    }

    public static void exit(Activity activity) {
        //initialize alert dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        //set tittle
        builder.setTitle("Exit");
        //set message
        builder.setMessage("Close app?");
        //positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Finish activity

                activity.finishAffinity();
                //exit app
                System.exit(0);
            }
        });
        //negative NO button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss alert dialog
                dialog.dismiss();
            }
        });
        //show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity,Class aClass) {
        //initiate intent
        Intent intent  =new Intent(activity,aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }



}