package com.example.iwayplus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Signin extends AppCompatActivity {
    EditText username_login,pswd_login;
    Button button_login;
    TextView createnewaccount,invalidemailorpswd;
    ImageView google_icon;
    String emailPattern="[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
        username_login=findViewById(R.id.username_login);
        pswd_login=findViewById(R.id.pswd_login);
        button_login=findViewById(R.id.button_login);
        createnewaccount=findViewById(R.id.createnewaccount);
        invalidemailorpswd=findViewById(R.id.invalidemailorpswd);
        google_icon=findViewById(R.id.google_icon);

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        createnewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signin.this,Signup.class));
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        google_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signin.this,Googlesignin.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin() {
        String email = username_login.getText().toString();
        String password = pswd_login.getText().toString();
        if (!email.matches(emailPattern)) {
            username_login.setError("Enter correct email");
        } else if (password.isEmpty() || password.length() < 6) {
            pswd_login.setError("Enter proper password");
        } else {
            progressDialog.setMessage("Please wait while Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Signin.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        //Toast.makeText(Signin.this,"Invalid email or password.Please enter again",Toast.LENGTH_SHORT).show();
                        invalidemailorpswd.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(Signin.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}