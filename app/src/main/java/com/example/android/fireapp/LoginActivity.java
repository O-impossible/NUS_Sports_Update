package com.example.android.fireapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailIdField;
    private EditText mPasswordField;
    private Button mLoginButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        mEmailIdField = (EditText) findViewById(R.id.login_email_id_field);
        mPasswordField = (EditText) findViewById(R.id.login_password_field);
        mLoginButton = (Button) findViewById(R.id.log_in_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        String emailId = mEmailIdField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if(TextUtils.isEmpty(emailId) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter all fields",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            mProgressDialog.setMessage("Logging In...");
            mProgressDialog.show();

            mAuth.signInWithEmailAndPassword(emailId,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressDialog.dismiss();
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"User signed in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Could not sign in. Please try again",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        }

    }

}
