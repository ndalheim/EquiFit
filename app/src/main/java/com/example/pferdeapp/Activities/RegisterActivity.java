package com.example.pferdeapp.Activities;

import android.content.Context;
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

import com.example.pferdeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//import android.support.v7.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    EditText mNutzername, mEmail, mPasswort, mPasswort_wiederholen;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    String userID;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNutzername = findViewById(R.id.nutzername);
        mEmail = findViewById(R.id.email);
        mPasswort = findViewById(R.id.passwort);
        mPasswort_wiederholen = findViewById(R.id.passwort_wiederholen);
        mRegisterBtn = findViewById(R.id.register_button);
        mLoginBtn = findViewById(R.id.go_to_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        fStore = FirebaseFirestore.getInstance();

        // Wenn ein Nutzer eingeloggt ist springe direkt zur Main Activity
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String passwort = mPasswort.getText().toString().trim();
                String passwort2 = mPasswort_wiederholen.getText().toString().trim();

                // Überprüfe ob alle Felder ausgefüllt sind
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email adresse fehlt");
                    mPasswort.setText("");
                    mPasswort_wiederholen.setText("");
                    return;
                }
                if (TextUtils.isEmpty(passwort) ) {
                    mPasswort.setError("Passwort fehlt");
                    mPasswort_wiederholen.setText("");
                    return;
                }
                if (TextUtils.isEmpty(passwort2) ) {
                    mPasswort_wiederholen.setError("Passwort fehlt");
                    mPasswort.setText("");
                    return;
                }

                //Überprüft ob beide Passwörter übereinstimmen
                if (!passwort.equals(passwort2) ) {
                    Context context = getApplicationContext();
                    CharSequence text = "Passwort stimmt nicht überein!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    mPasswort.setText("");
                    mPasswort_wiederholen.setText("");

                    return;
                }

                //Überprüft ob das Passwort länger als 6 Zeichen hat
                if(passwort.length() < 6 ){
                    mPasswort.setError("Passwort muss länger als 6 Zeichen sein");

                    mPasswort.setText("");
                    mPasswort_wiederholen.setText("");

                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Methode aufrufen die einen Nutzer der Datenbank hinzufügt
                registerUser(email, passwort);

            }

        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }

    private void registerUser(final String mail, String password) {

        // User in Firebase mit E-Mail-Adresse und Passwort registrieren
        firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //Verifizierungslink an den User schicken der sich gerade registriert hat
                FirebaseUser fUser = firebaseAuth.getCurrentUser();
                fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterActivity.this, "Email wurde versandt", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure:Email not sent"+ e.getMessage());

                    }
                });

                // Dem Nutzer in der Datenbank die E-Mail-Adressse und das Passwort hinzufügen
                userID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("user").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("username", mNutzername.getText().toString());
                user.put("email", mEmail.getText().toString());
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: User profil is created for " + userID + "with mail: " + mail);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());

                    }
                });


                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User ist registriert", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }else {
                    Toast.makeText(RegisterActivity.this, "Es ist ein Fehler aufgetreten!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    mPasswort.setText("");
                    mPasswort_wiederholen.setText("");
                }
            }
        });
    }
}