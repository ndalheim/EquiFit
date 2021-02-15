package com.example.pferdeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

//import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterBtn, mForgotPasswordBtn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    TextInputLayout emailLayot, passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        emailLayot = findViewById(R.id.email_layout);
        mPassword = findViewById(R.id.passwort);
        passwordLayout = findViewById(R.id.passwort_layout);
        firebaseAuth = firebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.login_button);
        mRegisterBtn = findViewById(R.id.go_to_register);
        mForgotPasswordBtn = findViewById(R.id.forgot_password);
        progressBar = findViewById(R.id.progressBar);

        db = FirebaseFirestore.getInstance();



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = mEmail.getText().toString().trim();
                String passwort = mPassword.getText().toString().trim();


                // Überprüfe ob alle Felder ausgefüllt sind
                if (TextUtils.isEmpty(email)) {
                    emailLayot.setError("Email adresse fehlt");
                    //mEmail.setError("Email adresse fehlt");
                    mPassword.setText("");
                    return;
                }

                if (TextUtils.isEmpty(passwort) ) {
                    passwordLayout.setError("Passwort fehlt");
                    //mPassword.setError("Passwort fehlt");
                    mPassword.setText("");
                    return;
                }

                if(passwort.length() < 6 ){
                    passwordLayout.setError("Passwort muss mindestens 6 Zeichen lang sein");
                    //mPassword.setError("Passwort muss mindestens 6 Zeichen lang sein");
                    mPassword.setText("");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                login(email, passwort);


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

    // Methode die den User mit seiner Email und Passwort einloggt
    private void login(final String mail, String password) {
        firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){

                // Wenn die Login-Daten korrekt sind wird die MainActivity aufgerufen
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else {
                    //Wenn die Login Daten Falsch sind wird überprüft ob die Email oder das Passwort falsch ist und entsprechende Fehler geworfen
                    db.collection("user").whereEqualTo("email", mail).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().isEmpty()){
                                            // Wenn in der Datenbank die angebenene E-Mail nicht existiert
                                            emailLayot.setError("E-Mail ist falsch / nicht registriert");
                                            Toast.makeText(LoginActivity.this, "E-Mail ist falsch / nicht registriert", Toast.LENGTH_SHORT).show();
                                            mEmail.setText("");
                                            Log.d(TAG, "E-Mail ist falsch / nicht registriert");
                                        }else {
                                            // Wenn die E-mail existiert muss das Passwort Falsch sein
                                            passwordLayout.setError("Passwort ist falsch");
                                            Toast.makeText(LoginActivity.this, "Passwort ist falsch", Toast.LENGTH_SHORT).show();
                                            mPassword.setText("");
                                            Log.d(TAG, "Passwort ist falsch");
                                        }
                                    }
                                }
                            });
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }
}