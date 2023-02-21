package com.example.iwayplus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Googlesignin extends Signin {
    private static final int RC_SIGN_IN =101 ;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlesignin);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Google Sign In...");
        progressDialog.show();

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
               // Log.d(TAG,"firebaseAuthWithGoogle:"+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                progressDialog.dismiss();
                finish();
                //Log.w(TAG,"Google sign in failed",e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken)
    {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                          //  Log.d(TAG,"signInWithCredential:success");
                            FirebaseUser user=mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            finish();
                         //   Log.w(TAG,"signInWithCredential:failure",task.getException());
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent=new Intent(Googlesignin.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}