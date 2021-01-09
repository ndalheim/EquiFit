package com.example.pferdeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.Database.Horse;
import com.google.android.gms.tasks.OnCompleteListener;

import com.example.pferdeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddHorseActivity extends AppCompatActivity {

    private static final String TAG = "AddHorseActivity";

    /**private String horseName; //Name
    private int horseHeight; //Stockmaß
    private int horseWeight; //Gewicht
    private String[] horseCondition; //Trainingszustand
    private String[] defect; //Mängel
    private String[] intolerance; //Intolerant/Allergie*/

    EditText mhorseName, mhorseHeight, mhorseWeight, mhorseCondition, mhorseDefect, mhorseIntolerance;
    Button mSaveHorseBtn, mBackBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horse);

        mhorseName = findViewById(R.id.horse_name);
        mhorseHeight = findViewById(R.id.horse_height);
        mhorseWeight = findViewById(R.id.horse_weight);
        mhorseCondition = findViewById(R.id.horse_condidion);
        mhorseDefect = findViewById(R.id.horse_defect);
        mhorseIntolerance = findViewById(R.id.horse_intolerance);

        mSaveHorseBtn = findViewById(R.id.save_horse_information_button);
        mBackBtn = findViewById(R.id.back_to_FeedFragment_button);


        //weiterleitung zum Feed Fragment
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        mSaveHorseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String horseName = mhorseName.getText().toString().trim();
                int horseHeight = Integer.parseInt(mhorseHeight.getText().toString().trim());
                int horseWeight = Integer.parseInt(mhorseWeight.getText().toString().trim());
                String horseCondition = mhorseCondition.getText().toString().trim();
                String horseDefect = mhorseDefect.getText().toString().trim();
                String horseIntolerance = mhorseIntolerance.getText().toString().trim();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = null;
                if (user !=null) {
                    uid = user.getUid();
                }

                Horse horse = new Horse(horseName, horseHeight, horseWeight, horseCondition, horseDefect, horseIntolerance, uid);
                db.collection("Horse").document("newHorse").set(horse)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddHorseActivity.this, "Pferd erfolgreich hochgeladen!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "addHorse: success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddHorseActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());

                            }
                        });

                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });


    }


}

