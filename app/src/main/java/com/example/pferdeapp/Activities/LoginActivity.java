package com.example.pferdeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.ForgotPassword;
import com.example.pferdeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterBtn, mForgotPasswordBtn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passwort);
        firebaseAuth = firebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.login_button);
        mRegisterBtn = findViewById(R.id.go_to_register);
        mForgotPasswordBtn = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressBar);



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String passwort = mPassword.getText().toString().trim();


                // Überprüfe ob alle Felder ausgefüllt sind
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email adresse fehlt");
                    mPassword.setText("");
                    return;
                }

                if (TextUtils.isEmpty(passwort) ) {
                    mPassword.setError("Passwort fehlt");
                    mPassword.setText("");
                    return;
                }

                if(passwort.length() < 6 ){
                    mPassword.setError("Passwort muss mindestens 6 Zeichen lang sein");
                    mPassword.setText("");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, passwort).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if (task.isSuccessful()){
                            //Toast.makeText(Login.this, "Erfolgreich eingeloggt", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(LoginActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            mPassword.setText("");
                        }
                    }
                });


            }

        });

        //weiterleitung zur Register Activity
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


        mForgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));


            }
        });


    }
}