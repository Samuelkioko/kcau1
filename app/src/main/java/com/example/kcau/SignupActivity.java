package com.example.kcau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.net.Inet4Address;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button signup;
    TextView gotoLogin;
    EditText fullname,email,dob;
    TextInputEditText password1,password2;
    Spinner spinner1;
    DatePickerDialog datePickerDialog;

    AutoCompleteTextView autoCompleteTextView;
    String[] County_Names;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        gotoLogin=findViewById(R.id.txtgotologin);
        signup=findViewById(R.id.btnSignup);
        fullname=findViewById(R.id.edtFullname);
        email=findViewById(R.id.edtEmailSignup);
        password1=findViewById(R.id.edtPassword1);
        password2=findViewById(R.id.edtPassword2);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        //campus spinner
        spinner1=findViewById(R.id.spinner_campus);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.campus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        //autocomplete county
        autoCompleteTextView=(AutoCompleteTextView) findViewById(R.id.county);
        County_Names=getResources().getStringArray(R.array.county);
        //array adapter to add the values to the textview, create layout and specify resource with the string
        ArrayAdapter<String> adapter2 =new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_list_item_1, County_Names);
        //add the adapter to autocompletetextview
        autoCompleteTextView.setAdapter(adapter2);


        //date picker dob
        dob=findViewById(R.id.edtdate);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                final int year=calendar.get(Calendar.YEAR);
                final int month=calendar.get(Calendar.MONTH);
                final int day=calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog=new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        dob.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });


        //sign up click
        signup.setOnClickListener(view ->{
            signUp();
        });

        //go to log in click
        gotoLogin.setOnClickListener(view ->{
            goToSignin();
        });
    }//end of oncreate

    //go to sign in
    private void goToSignin() {
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //sign up method
    private void signUp() {
        String fulname = fullname.getText().toString().trim();
        String usermail = email.getText().toString().trim();
        String passwrd = password1.getText().toString().trim();
        String passwrd1 = password2.getText().toString().trim();

        //check if the email and password fields are empty, if empty, show error
        if (TextUtils.isEmpty(fulname)){
            fullname.setError("PLease enter your name");
            fullname.requestFocus();
        }
        else if (TextUtils.isEmpty(usermail)){
            email.setError("Email cannot be empty!");
            email.requestFocus();
        }

        else if (TextUtils.isEmpty(passwrd)){
            password1.setError("Password cannot be empty!");
            password2.requestFocus();
        }
        //if passwords don't match
        else if(!passwrd.equals(passwrd1)){
            password2.setError("Password did not match, please try again!");
            password2.setText("");
            password2.requestFocus();
        }
        else if(passwrd.length()<6){
            password1.setError("Minimum password length is 6");
            password1.setText("");
            password1.requestFocus();
            password2.setError("Minimum password length is 6");
            password2.setText("");
        }

        else{
            progressDialog.setTitle("Registering new user");
            progressDialog.setMessage("Please wait as we register you...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(usermail,passwrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //check if the creation task was successful
                    if (task.isSuccessful()){

                        //register to database
                        //create user object
                        User user = new User(fulname,usermail);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(SignupActivity.this,"Registered successfully",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(SignupActivity.this,"Registration failure, please try again or contact app support if problem persists",Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }
                    else{
                        //dismiss loading bar
                        progressDialog.dismiss();
                        Toast.makeText(SignupActivity.this,"Registration Error, please try again "+task.getException().getMessage(),Toast.LENGTH_SHORT).show(); //show toast on error and also the error
                    }
                }
            });
        }

    }



    int counter=0;
    @Override
    public void onBackPressed() {
        counter++;
        if(counter<2){
            Toast.makeText(this,"Press again to go back",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}