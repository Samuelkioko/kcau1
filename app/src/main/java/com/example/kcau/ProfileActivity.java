package com.example.kcau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    //user profile
    FirebaseUser user;
    DatabaseReference reference;
    String userID;

    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        goBack=findViewById(R.id.Imgprofback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();

        final TextView name = (TextView) findViewById(R.id.pfName);
        final TextView mail = (TextView) findViewById(R.id.pfEmail);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //create user object
                User userProfile =snapshot.getValue(User.class);
                if (userProfile !=null){
                    String fullname = userProfile.fullname;
                    String email = userProfile.email;

                    name.setText(fullname);
                    mail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Not able to get user data",Toast.LENGTH_LONG).show();

            }
        });
    }//end of oncreate

    int counter=0;
    @Override
    public void onBackPressed() {
        counter++;
        if(counter<2){
            Toast.makeText(this,"Press again to go back",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent=new Intent(ProfileActivity.this, DashboardActivity.class);
            startActivity(intent);
        }

    }
}