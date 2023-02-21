package com.example.iwayplus;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
EditText edittextview_username,edittextview_pswd;
Button button_proceed;

String emailPattern="[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
ProgressDialog progressDialog;
FirebaseAuth mAuth;
FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("REGISTRATION");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edittextview_username=findViewById(R.id.edittextview_username);
        edittextview_pswd=findViewById(R.id.edittextview_pswd);
        button_proceed=findViewById(R.id.button_proceed);

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        button_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuthentication();
            }
        });
    }

    private void performAuthentication() {
        String email=edittextview_username.getText().toString();
        String password=edittextview_pswd.getText().toString();
        if(!email.matches(emailPattern))
        {
            edittextview_username.setError("Enter correct email");
        }
        else if(password.isEmpty() || password.length()<6)
        {
            edittextview_pswd.setError("Enter proper password");
        }
        else
        {
            progressDialog.setMessage("Please wait while Signing in...");
            progressDialog.setTitle("Sign in");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Signup.this,"Sign in Successfull",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Signup.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(Signup.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    
}