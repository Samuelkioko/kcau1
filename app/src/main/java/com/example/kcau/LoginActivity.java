package com.example.kcau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView gotoSignup,forgotPassword;
    EditText emailLogin;
    TextInputEditText passwordLogin;
    FirebaseAuth mAuth;
    //progress dialogs
    ProgressDialog progressDialog;
    ProgressDialog progressDialog2;
    //For change on state (start and stop)
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.btnLogin);
        emailLogin=findViewById(R.id.edtEmailLogin);
        passwordLogin=findViewById(R.id.edtPasswordLogin);
        gotoSignup=findViewById(R.id.txtCreateNew);
        forgotPassword=findViewById(R.id.txtForgotpass);
        mAuth= FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this); //for login button
        progressDialog2=new ProgressDialog(this);//for reset password YES click

        //login/sign in button click
        login.setOnClickListener(view ->{
            loGin();
        });

        //go to sign up/create new account text view click

        gotoSignup.setOnClickListener(view ->{
            goToSignup();
        });


        //firebase auth listener check if user was logged in if yes send him/her to the dashboard
        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null)//if user is logged in
                {
                    Intent intent=new Intent(LoginActivity.this, DashboardActivity.class);//open dashboard activity
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginActivity.this,"Welcome" ,Toast.LENGTH_SHORT).show();
                }
            }
        };

        //forgot password reset
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an edit text field to let user enter their email to get reset link
                final EditText resetEmail = new EditText(v.getContext());
                final AlertDialog.Builder resetDialog = new AlertDialog.Builder(v.getContext());
                //give title and message to the alert dialog
                resetDialog.setTitle("Reset password");
                resetDialog.setMessage("Enter the email you registered with to get a reset link");
                //set the edit text to the view of the alert dialog
                resetDialog.setView(resetEmail);

                //handle the buttons on reset dialog (Yes and No)
                //OK button
                progressDialog2.setTitle("Password reset");
                progressDialog2.setMessage("Sending reset link, please wait....");
                progressDialog2.show();
                resetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and check if its valid/exists in database then send link else display an error

                        //extract the entered email
                        String mail = resetEmail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() { //if sending was successful
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog2.dismiss();
                                Toast.makeText(LoginActivity.this,"Link sent to "+resetEmail.getText()+". Please click on the link to proceed",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {       //if sending failed
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog2.dismiss();

                                Toast.makeText(LoginActivity.this,"Link not sent! "+resetEmail.getText()+". Error(s) incurred "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

                //Cancel button
                resetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the alert dialog
                        Toast.makeText(LoginActivity.this,"Reset cancelled",Toast.LENGTH_SHORT).show();
                    }
                });
                resetDialog.create().show(); //create it and show
            }
        });

    }//end of oncreate

    //go to sign up
    private void goToSignup() {
        Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
        finish();
    }

    //LOGIN METHOD
    private void loGin() {
        String usermaillog = emailLogin.getText().toString();
        String passwrdlog = passwordLogin.getText().toString();

        //check if the email and password fields are empty, if empty, show error
        if (TextUtils.isEmpty(usermaillog)){
            emailLogin.setError("Email cannot be empty!");
            emailLogin.requestFocus();
        }
        else if (TextUtils.isEmpty(passwrdlog)){
            passwordLogin.setError("Password cannot be empty!");
            passwordLogin.requestFocus();
        }
        else{
            progressDialog.setTitle("Verification");
            progressDialog.setMessage("A moment please as we log you in...");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(usermaillog,passwrdlog).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //dismiss progress dialog
                        progressDialog.dismiss();

                        //check if email is verified
                        /*pending check in sign up*/

                            Toast.makeText(LoginActivity.this, "Log in successful, welcome " + emailLogin.getText(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class)); //open dashboard activity if login was successful
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Log in Error please try again "+task.getException().getMessage(),Toast.LENGTH_LONG).show(); //show toast on error and also the error
                    }
                }
            });
        }
    }

    //firebase auth listener start and stop

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
}